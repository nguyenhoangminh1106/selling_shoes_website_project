package selling_shoes_website_project.controller;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import selling_shoes_website_project.DAO.WishListDAO;
import selling_shoes_website_project.entity.User;
import selling_shoes_website_project.utils.Utils;

@Controller
public class WishListController {
	@RequestMapping("/changeWishStatus")
	public String changeWishStatus(@RequestParam("id") int productId, HttpServletRequest request, Model model) {
		SessionFactory factory = Utils.initSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
        try {
        	User user = Utils.getCurrentUser(request);
        	int userId = user.getId();
        	WishListDAO.changeWishStatus(session, productId, userId);
        	
        	session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
        	factory.close();
        }
        
        return Utils.toProductDetail(productId, model);
	}
	
	@RequestMapping("/changeWishListStatus")
	public String changeWishListStatus(@RequestParam("id") int productId, HttpServletRequest request, Model model) {
		changeWishStatus(productId, request, model);
        
        return Utils.toWishList(request, model);
	}
}
