package selling_shoes_website_project.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import selling_shoes_website_project.entity.Category;
import selling_shoes_website_project.utils.Utils;

public class CategoryDAO {
	/**
	 * GET CATEGORY LIST
	 * 
	 * @return
	 */
	public static List<Category> getCategoryList() {
		SessionFactory factory = Utils.initSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		try {
			return session.createQuery("FROM Category", Category.class).getResultList();
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
	 * GET CATEGORY BY NAME
	 * 
	 * @param session
	 * @param name
	 * @return
	 */
	public static Category getCategoryByName(Session session, String name) {
        String hql = "FROM Category c WHERE c.name = :name";
        Query<Category> query = session.createQuery(hql, Category.class);
        query.setParameter("name", name);
        List<Category> results = query.list();

        if (!results.isEmpty()) {
            return results.get(0);
        } else {
            return null;
        }
    }
}
