package selling_shoes_website_project.DAO;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import selling_shoes_website_project.entity.Product;
import selling_shoes_website_project.entity.User;
import selling_shoes_website_project.entity.WishItem;
import selling_shoes_website_project.utils.Utils;

public class WishListDAO {
	/**
	 * IS ITEM IN WISHLIST OF A USER
	 * 
	 * @param productId
	 * @param userId
	 * @return
	 */
	public static boolean isWishList(int productId, int userId) {
		SessionFactory factory = Utils.initSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		try {
			String sql = "select count(w.id) from WishItem w where w.product.id = :productId and w.customer.id = :userId";
	        Query<Long> query = session.createQuery(sql, Long.class);
	        query.setParameter("productId", productId);
	        query.setParameter("userId", userId);
	        long count = (long) query.getSingleResult();

	        return count > 0;
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		} finally {
			factory.close();
		}
		
	}
	
	/**
	 * CHANGE WISH STATUS
	 * 
	 * @param session
	 * @param productId
	 * @param userId
	 */
	public static void changeWishStatus(Session session, int productId, int userId) {
        try {
            Product product = ProductDAO.getProductById(productId);
            User user = UserDAO.getUserById(session, userId);
            boolean isExists = product.isWishList(userId);
            if (isExists) {
                String hql = "delete from WishItem where product.id = :productId and customer.id = :userId";
                Query query = session.createQuery(hql);
                query.setParameter("productId", productId);
                query.setParameter("userId", userId);
                query.executeUpdate();
            } else {
                Date currentDate = Utils.getCurrentDatetime();
                WishItem wishItem = new WishItem(currentDate, currentDate, user, product);

                session.save(wishItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * GET WISH LIST OF THE USER
	 * 
	 * @param userId
	 * @return
	 */
	public static List<Product> getWishList(int userId) {
        
        SessionFactory factory = Utils.initSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		try {
            String hql = "select w.product from WishItem w where w.customer.id = :userId";
            Query<Product> query = session.createQuery(hql, Product.class);
            query.setParameter("userId", userId);
            List<Product> wishList = query.getResultList();

            return wishList;
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
}
