package selling_shoes_website_project.DAO;

import org.hibernate.Session;

import selling_shoes_website_project.entity.User;

public class UserDAO {

	/**
	 * GET USER BY EMAIL
	 * 
	 * @param session
	 * @param email
	 * @return
	 */
	public static User getUserByEmail(Session session, String email) {
		return session.createQuery("FROM User WHERE email = :email", User.class)
                .setParameter("email", email)
                .uniqueResult();
	}

	/**
	 * GET USER BY ID
	 * 
	 * @param session
	 * @param user_id
	 * @return
	 */
	public static User getUserById(Session session, int user_id) {
		return session.createQuery("FROM User WHERE id = :id", User.class)
                .setParameter("id", user_id)
                .uniqueResult();
	}

}
