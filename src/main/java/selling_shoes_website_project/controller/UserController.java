package selling_shoes_website_project.controller;


import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import selling_shoes_website_project.DAO.UserDAO;
import selling_shoes_website_project.entity.User;
import selling_shoes_website_project.utils.Utils;

@Controller
public class UserController {
	/**
	 * CHECK LOGIN
	 * 
	 * @param email
	 * @param password
	 * @param model
	 * @return
	 */
	@RequestMapping("/checkLogin")
	public String checkLogin(@RequestParam("email") String email,
							@RequestParam("password") String password,
							@RequestParam("role") String role,
							HttpServletRequest request,
							Model model) {
		SessionFactory factory = Utils.initSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
	    try {
	    	User user = UserDAO.getUserByEmail(session, email);
	        session.getTransaction().commit();
	        if (user != null && user.getPassword().equals(password)) {
	            // login successful, redirect to appropriate page
	        	if (user.getRole().equals("admin") && role.equals("admin")) {
	        		Utils.setAdminView(model, "display_dashboard");
	        		model.addAttribute("auth_success", true);
		            return "admin/layout";
	        	}
	        	else if (user.getRole().equals("customer") && role.equals("customer")) {
	        		HttpSession userSession = request.getSession();
	        		userSession.setAttribute("user", user);
	        		userSession.setAttribute("auth_success", "true");	        		
	        		
	        		return Utils.toHomePage(model);
	        	}
	        } else {
	        	// login failed, redirect back to login page
	        	if (role.equals("admin")) {
	        		
		        	model.addAttribute("auth_error", true);
		            return "admin/login";
	        	}
	        	else if (role.equals("customer" )) {
	        		model.addAttribute("auth_error", true);
		            return "shopper/login";
	        	}            
	        }
	       	
	        
	    } catch (Exception e) {
	    	e.printStackTrace();
	    } finally {
	        factory.close();
	    }
	    
	    return Utils.toHomePage(model);
    
	}
	
	/**
	 * ADMIN LOGOUT
	 * 
	 * @return
	 */
	@RequestMapping("/adminLogout")
	public String toLoginPage() {
		return "admin/login";
	}
	
	/**
	 * USER REGISTER
	 * 
	 * @param name
	 * @param email
	 * @param phone
	 * @param password
	 * @param model
	 * @return
	 */
	@RequestMapping("/userRegister")
	public String userRegister(@RequestParam("name") String name,
								@RequestParam("email") String email,
								@RequestParam("phone") String phone,
								@RequestParam("password") String password,
								Model model) {
		SessionFactory factory = Utils.initSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		
		try {
			Date currentDate = Utils.getCurrentDatetime();
			
            // Create a new User object
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPhone(phone);
            user.setPassword(password);
            user.setCreatedAt(currentDate);
            user.setUpdatedAt(currentDate);
            user.setRole("customer");
            user.setDelete(false);

            // Save the user object
            session.save(user);

            session.getTransaction().commit();
            
            model.addAttribute("register_success", true);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("register_error", true);
        } finally {
            factory.close();
        }
		
		
		return "shopper/register";
	}
	
	
}
