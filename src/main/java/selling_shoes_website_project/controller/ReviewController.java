package selling_shoes_website_project.controller;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import selling_shoes_website_project.DAO.ReviewDAO;
import selling_shoes_website_project.entity.User;
import selling_shoes_website_project.utils.Utils;

@Controller
public class ReviewController {
	@RequestMapping("/addReview")
	public String addReview(@RequestParam("title") String title, 
							@RequestParam("content") String content, 
							@RequestParam("rating") int rating,
							@RequestParam("productId") int productId,
							HttpServletRequest request,
							Model model) {
		SessionFactory factory = Utils.initSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		
        try {
        	User user = Utils.getCurrentUser(request);
        	int userId = user.getId();
        	
        	ReviewDAO.addReview(session, userId, productId, title, content, rating);     

            session.getTransaction().commit();
            
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
        	factory.close();
        }
		
        
        return Utils.toProductDetail(productId, model);
	}
}
