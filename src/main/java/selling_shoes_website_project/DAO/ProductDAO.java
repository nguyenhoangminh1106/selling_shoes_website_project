package selling_shoes_website_project.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import selling_shoes_website_project.entity.Product;
import selling_shoes_website_project.utils.Utils;

public class ProductDAO {
	public static List<Product> getProductList(Session session, int firstResult, int maxResults) {
		Query<Product> query = session.createQuery("FROM Product WHERE is_delete = 0", Product.class);
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		return query.getResultList();
	}
	
	public static List<Product> getProductList() {
		SessionFactory factory = Utils.initSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		try {
			return session.createQuery("FROM Product WHERE is_delete = 0", Product.class).getResultList();
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
	
	
	public static List<Product> searchProduct(Integer category_id, Integer brand_id, int priceMin, int priceMax) {
		SessionFactory factory = Utils.initSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		try {
			String hql = "FROM Product P WHERE 1=1 AND is_delete = 0 AND status = 'Available'";

	        if (brand_id != null) {
	            hql += " AND P.brand.id = :brand_id";
	        }
	        if (category_id != null) {
	            hql += " AND P.category.id = :category_id";
	        }
	        if (priceMin >= 0) {
	            hql += " AND P.price >= :priceMin";
	        }
	        if (priceMax >= 0) {
	            hql += " AND P.price <= :priceMax";
	        }

	        Query<Product> query = session.createQuery(hql, Product.class);

	        if (brand_id != null) {
	            query.setParameter("brand_id", brand_id);
	        }
	        if (category_id != null) {
	            query.setParameter("category_id", category_id);
	        }
	        if (priceMin >= 0) {
	            query.setParameter("priceMin", priceMin);
	        }
	        if (priceMax >= 0) {
	            query.setParameter("priceMax", priceMax);
	        }

	        List<Product> products = query.list();
			
			return products;
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
	
	public static List<Product> searchProduct(Session session, String nameSearch, String status, Integer brand_id) {
        String hql = "FROM Product P WHERE 1=1 AND is_delete = 0";
        
        if (!nameSearch.equals("")) {
            hql += " AND P.name like :nameSearch";
        }
        if (!status.equals("")) {
            hql += " AND P.status = :status";
        }
        if (brand_id != null) {
            hql += " AND P.brand.id = :brand_id";
        }

        Query<Product> query = session.createQuery(hql, Product.class);

        if (!nameSearch .equals("")) {
            query.setParameter("nameSearch", "%" + nameSearch + "%");
        }
        if (!status .equals("")) {
            query.setParameter("status", status);
        }
        if (brand_id != null) {
            query.setParameter("brand_id", brand_id);
        }

        List<Product> products = query.list();
        return products;
    }
	
	/**
	 * CHANGE PRODUCT STATUS
	 * 
	 * @param session
	 * @param status
	 */
	public static void changeProductStatus(Session session, String status, int id) {
        
        try {
        	String newStatus = "";
    		
    		if (status.equals("Available")) {
    			newStatus = "Unavailable";
    		}
    		else if (status.equals("Unavailable")) {
    			newStatus = "Available";
    		}
        	
            String hql = "UPDATE Product SET status = :newStatus WHERE id = :id";
            Query query = session.createQuery(hql);
            query.setParameter("newStatus", newStatus);
            query.setParameter("id", id);
            query.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/**
	 * DELETE A PRODUCT
	 * 
	 * @param session
	 * @param id
	 */
	public static void deleteProduct(Session session, int id) {
		try {
			String hql = "UPDATE Product SET is_delete = 1 WHERE id = :id";
	        Query query = session.createQuery(hql);
	        query.setParameter("id", id);
	        query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static Product getProductById(int id) {
		SessionFactory factory = Utils.initSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		try {
			return session.get(Product.class, id);
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
	 * GET 'length' PRODUCTS WITH HIGHEST PRICE
	 * 
	 * @param length
	 * @return
	 */
	public static List<Product> getProductListByPrice(int length) {
		SessionFactory factory = Utils.initSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		try {
			String hql = "FROM Product P WHERE is_delete = 0 ORDER BY P.price DESC";
			return session.createQuery(hql, Product.class).setMaxResults(length).list();
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
	 * UPDATE PRODUCT
	 * 
	 * @param session
	 * @param product
	 */
	public static void updateProduct(Session session, Product product) {
		try {
            session.update(product);
        } catch (Exception e) {
            e.printStackTrace();
        }		
	}
	
	
}
