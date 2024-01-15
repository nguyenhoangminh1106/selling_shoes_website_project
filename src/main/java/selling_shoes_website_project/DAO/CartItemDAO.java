package selling_shoes_website_project.DAO;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import selling_shoes_website_project.entity.CartItem;
import selling_shoes_website_project.entity.Product;
import selling_shoes_website_project.entity.User;
import selling_shoes_website_project.utils.Utils;

public class CartItemDAO {
	/**
	 * GET CART ITEMS LIST BY USER ID
	 * 
	 * @param session
	 * @param userId
	 * @return
	 */
    public static List<CartItem> getCartItemsByUserId(int userId) {
    	SessionFactory factory = Utils.initSessionFactory();
    	Session session = factory.getCurrentSession();
    	session.beginTransaction();
    	
        try {
            String hql = "FROM CartItem WHERE customer.id = :userId";
            return session.createQuery(hql, CartItem.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
    		if (session != null) {
    			session.getTransaction().rollback();
    		}
    		e.printStackTrace();
    		return null;
    	} finally {
    		factory.close();
    	}
        
        
    }
    
    /**
     * ADD PRODUCT TO CART
     * 
     * @param product_id
     * @param user_id
     */
    public static void addProductToCart(int product_id, int user_id) {
    	SessionFactory factory = Utils.initSessionFactory();
    	Session session = factory.getCurrentSession();
    	session.beginTransaction();
    	
        try {
            Product product = ProductDAO.getProductById(product_id);
            User user = UserDAO.getUserById(session, user_id);
            Date currentDate = Utils.getCurrentDatetime();
            
            CartItem cartItem = new CartItem(currentDate, currentDate, user, product);
            
            CartItem existingCartItem = CartItemDAO.getCartItemByUserAndProduct(session, user_id, product_id);
            
            if (existingCartItem == null) {
            	session.save(cartItem);
            }
            
            session.getTransaction().commit();
            
        } catch (Exception e) {
    		if (session != null) {
    			session.getTransaction().rollback();
    		}
    		e.printStackTrace();
    	} finally {
    		factory.close();
    	}
        
    }
    
    
    /**
     * GET CART ITEM BY USER ID AND PRODUCT ID 
     * 
     * @param session
     * @param user_id
     * @param product_id
     * @return
     */
    public static CartItem getCartItemByUserAndProduct(Session session, int user_id, int product_id) {
        String hql = "FROM CartItem WHERE customer_id = :user_id AND product_id = :product_id";
        Query<CartItem> query = session.createQuery(hql, CartItem.class);
        query.setParameter("user_id", user_id);
        query.setParameter("product_id", product_id);
        List<CartItem> results = query.getResultList();
        
        if (!results.isEmpty()) {
            // The product is already in the user's cart
            return (CartItem) results.get(0);
        } else {
            // The product is not in the user's cart
            return null;
        }
    }

    /**
     * CHANGE CART ITEM QUANTITY
     * 
     * @param session
     * @param id
     * @param changeMode
     */
	public static void changeCartItemQuantity(Session session, int id, String changeMode) {
		try {
	        // Fetch the CartItem from the database
	        CartItem cartItem = session.get(CartItem.class, id);
	        
	        if (cartItem != null) {
	            // Change the quantity based on the changeMode
	            if ("plus".equals(changeMode)) {
	                cartItem.setQuantity(cartItem.getQuantity() + 1);
	            } else if ("minus".equals(changeMode)) {
	                cartItem.setQuantity(Math.max(0, cartItem.getQuantity() - 1));
	            }
	            
	            // Update the CartItem in the database
	            session.update(cartItem);
	        }
	    } catch (Exception e) {
	        if (session != null) {
	            session.getTransaction().rollback();
	        }
	        e.printStackTrace();
	    }
		
	}

	public static void removeCartItemById(Session session, int id) {
		CartItem cartItem = (CartItem) session.get(CartItem.class, id);
        if (null != cartItem) {
            session.delete(cartItem);
        }
		
	}

	public static void removeCartItemByUserId(Session session, int user_id) {
		try {
			String hql = "DELETE FROM CartItem WHERE customer_id = :user_id";
		    Query query = session.createQuery(hql);
		    query.setParameter("user_id", user_id);
		    query.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
