package selling_shoes_website_project.DAO;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import selling_shoes_website_project.entity.Brand;
import selling_shoes_website_project.utils.Utils;

public class BrandDAO {
	/**
	 * ADD A BRAND IF NOT EXIST
	 * 
	 * @param session
	 * @param brandName
	 */
	public static void addBrand(Session session, String brandName) {
		Date date = Utils.getCurrentDatetime();
		Brand brand = new Brand(date, date, brandName);
		
        String hql = "FROM Brand b WHERE b.name = :name";
        Query<Brand> query = session.createQuery(hql, Brand.class);
        query.setParameter("name", brandName);
        List<Brand> results = query.list();

        if (results.isEmpty()) {
            session.save(brand);
        }
    }
	
	/**
	 * GET BRAND BY NAME
	 * 
	 * @param session
	 * @param name
	 * @return
	 */
	public static Brand getBrandByName(Session session, String name) {
        String hql = "FROM Brand b WHERE b.name = :name";
        Query<Brand> query = session.createQuery(hql, Brand.class);
        query.setParameter("name", name);
        List<Brand> results = query.list();

        if (!results.isEmpty()) {
            return results.get(0);
        } else {
            return null;
        }
    }
	
	/**
	 * GET BRAND ID BY NAME
	 * 
	 * @param session
	 * @param brandName
	 * @return
	 */
	public static Integer getIdByName(Session session, String brandName) {
        String hql = "SELECT B.id FROM Brand B WHERE B.name = :brandName";
        Query<Integer> query = session.createQuery(hql, Integer.class);
        query.setParameter("brandName", brandName);
        
        Integer result = (Integer) query.uniqueResult();

        return result;
    }
	
	/**
	 * GET BRAND LIST
	 * 
	 * @return
	 */
	public static List<Brand> getBrandList() {
		SessionFactory factory = Utils.initSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		try {
			return session.createQuery("FROM Brand", Brand.class).getResultList();
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
