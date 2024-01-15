package selling_shoes_website_project.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;


import selling_shoes_website_project.entity.Order;
import selling_shoes_website_project.utils.Utils;

public class OrderDAO {
	/**
	 * GET PRODUCT LIST
	 * 
	 * @param session
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	public static List<Order> getOrderList(Session session, int firstResult, int maxResults) {
		Query<Order> query = session.createQuery("FROM Order", Order.class);
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		return query.getResultList();
	}

	/**
	 * GET ORDER BY ID
	 * 
	 * @param id
	 * @return
	 */
	public static Order getOrderById(int id) {
		SessionFactory factory = Utils.initSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		try {
			return session.get(Order.class, id);
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
	 * GET ORDER LIST BY USER_ID
	 * 
	 * @param userId
	 * @return
	 */
	public static List<Order> getOrderListByUserId(int userId) {
		SessionFactory factory = Utils.initSessionFactory();
    	Session session = factory.getCurrentSession();
    	session.beginTransaction();
    	
        try {
            String hql = "FROM Order WHERE customer.id = :userId";
            return session.createQuery(hql, Order.class)
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

	public static List<Order> searchOrder(Session session, String nameSearch, String paymentMethod,
			String paymentStatus, String deliveryStatus) {
		String hql = "FROM Order O WHERE 1=1";
        
        if (!nameSearch.equals("")) {
            hql += " AND O.customer.name like :nameSearch";
        }
        if (!paymentMethod.equals("")) {
            hql += " AND O.paymentMethod = :paymentMethod";
        }
        if (!paymentStatus.equals("")) {
            hql += " AND O.paymentStatus = :paymentStatus";
        }
        if (!deliveryStatus.equals("")) {
            hql += " AND O.deliveryStatus = :deliveryStatus";
        }

        Query<Order> query = session.createQuery(hql, Order.class);

        if (!nameSearch.equals("")) {
            query.setParameter("nameSearch", "%" + nameSearch + "%");
        }
        if (!paymentMethod.equals("")) {
            query.setParameter("paymentMethod", paymentMethod);
        }
        if (!paymentStatus.equals("")) {
            query.setParameter("paymentStatus", paymentStatus);
        }
        if (!deliveryStatus.equals("")) {
            query.setParameter("deliveryStatus", deliveryStatus);
        }

        List<Order> orders = query.list();
        return orders;
	}
}
