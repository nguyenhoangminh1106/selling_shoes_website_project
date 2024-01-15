package selling_shoes_website_project.controller;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import selling_shoes_website_project.DAO.CartItemDAO;
import selling_shoes_website_project.entity.User;
import selling_shoes_website_project.utils.Utils;

@Controller
public class CartItemController {
	
	
	@RequestMapping("/change-quantity")
	public String changeCartItemQuantity(@RequestParam("id") int id,
											@RequestParam("change") String changeMode,
											HttpServletRequest request,
											Model model) {
		SessionFactory factory = Utils.initSessionFactory();
    	Session session = factory.getCurrentSession();
    	session.beginTransaction();
    	
        try {
            CartItemDAO.changeCartItemQuantity(session, id, changeMode);
            
            session.getTransaction().commit();
            
        } catch (Exception e) {
    		if (session != null) {
    			session.getTransaction().rollback();
    		}
    		e.printStackTrace();
    	} finally {
    		factory.close();
    	}
        
        return Utils.toCart(request, model);
	}
	
	@RequestMapping("/remove-cart")
	public String removeCartItem(HttpServletRequest request, Model model) {
		SessionFactory factory = Utils.initSessionFactory();
    	Session session = factory.getCurrentSession();
    	session.beginTransaction();

        try {
        	String id = request.getParameter("id");
        	
        	if (id == null) {
        		User user = (User)request.getSession().getAttribute("user");
        		int user_id = user.getId();
        		CartItemDAO.removeCartItemByUserId(session, user_id);
        	} else {
        		CartItemDAO.removeCartItemById(session, Integer.parseInt(id));
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
        
        return Utils.toCart(request, model);
	}
	
}
