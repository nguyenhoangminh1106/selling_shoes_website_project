package selling_shoes_website_project.DAO;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import selling_shoes_website_project.entity.Product;
import selling_shoes_website_project.entity.Review;
import selling_shoes_website_project.entity.User;
import selling_shoes_website_project.utils.Utils;

public class ReviewDAO {
	/**
	 * GET REVIEWS BY PRODUCT ID
	 * 
	 * @param productId
	 * @return
	 */
	public static List<Review> getReviewsByProductId(int productId) {
		SessionFactory factory = Utils.initSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		try {
			String hql = "from Review r where r.product.id = :productId order by r.updatedAt desc";
            Query<Review> query = session.createQuery(hql, Review.class);
            query.setParameter("productId", productId);
            return query.getResultList();
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
	 * WRITE REVIEW
	 * 
	 * @param session
	 * @param userId
	 * @param productId
	 */
	public static void addReview(Session session, int userId, int productId, String title, String content, int rating) {
        try {
        	User user = UserDAO.getUserById(session, userId);
        	Product product = ProductDAO.getProductById(productId);
        	
        	Date currentDate = Utils.getCurrentDatetime();
        	
        	Review review = new Review(currentDate, currentDate, content, rating, title, user, product);

            session.save(review);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
