package selling_shoes_website_project.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import selling_shoes_website_project.DAO.CartItemDAO;
import selling_shoes_website_project.DAO.OrderDAO;
import selling_shoes_website_project.entity.CartItem;
import selling_shoes_website_project.entity.Order;
import selling_shoes_website_project.entity.OrderDetail;
import selling_shoes_website_project.entity.Product;
import selling_shoes_website_project.entity.User;
import selling_shoes_website_project.utils.Utils;

@Controller
public class OrderController {
	private static int maxResultPerPage = 5;
	
	/**
	 * GET ORDER LIST
	 * 
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping("/getOrderList")
    public String showOrders(@RequestParam("page") int page, Model model) {
		
		SessionFactory factory = Utils.initSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		
		if (page == 0) {
			page = 1;
		}		
		int resultIndex = (page - 1) * maxResultPerPage;
		
		try {
			List<Order> orders = OrderDAO.getOrderList(session, resultIndex, maxResultPerPage);
			
			session.getTransaction().commit();
	        model.addAttribute("ORDER_LIST", orders);
	        Utils.setAdminView(model, "display_orders");
	        model.addAttribute("page", page);
	        
	        
		} finally {
			factory.close();
		}
        return "admin/layout";
    }
	
	/**
	 * ADD ORDER
	 * 
	 * @param consignee
	 * @param consigneePhone
	 * @param deliveryAddress
	 * @param paymentMethod
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/addOrder")
	public String addOrder(@RequestParam("name") String consignee,
							@RequestParam("phone") String consigneePhone,
							@RequestParam("address") String deliveryAddress,
							@RequestParam("paymentMethod") String paymentMethod,
							HttpServletRequest request,
							HttpServletResponse response,
							Model model
							) {
		SessionFactory factory = Utils.initSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		
		Date currentDate = Utils.getCurrentDatetime();
		User user = Utils.getCurrentUser(request);
		int user_id = user.getId();
		
		int totalCartAmount = user.getTotalCartAmount();
		
		try {
			List<CartItem> cartItems = CartItemDAO.getCartItemsByUserId(user_id);
			
			
			Order order = new Order(currentDate, currentDate, consignee, consigneePhone, deliveryAddress, "Approving", paymentMethod, "Unpaid", false, totalCartAmount, user);
			
			session.save(order);
			
			for (CartItem cartItem : cartItems) {
				int totalAmount = cartItem.getTotalAmount();
				int quantity = cartItem.getQuantity();
				Product product = cartItem.getProduct();
				int price = product.getPrice();
				OrderDetail orderDetail = new OrderDetail(currentDate, currentDate, price, quantity , totalAmount, order, product);
				
				session.save(orderDetail);		
			}
			
			CartItemDAO.removeCartItemByUserId(session, user_id);
	
			session.getTransaction().commit();

			model.addAttribute("orderWeb", order);
			model.addAttribute("isSuccess", true);
			model.addAttribute("result", "ĐẶT HÀNG THÀNH CÔNG");
			model.addAttribute("note", "Cảm ơn bạn đã mua hàng tại ToeShoe");
		} catch(Exception e) {
			e.printStackTrace();
			
			model.addAttribute("isSuccess", false);
			model.addAttribute("result", "ĐẶT HÀNG THẤT BẠI");
			model.addAttribute("note", "Vui lòng thử lại! ToeShoe xin lỗi về sự cố này.");
		} finally {
			factory.close();
		}
		
		
		Utils.addCategoryListToModel(model);
		
		if (paymentMethod.equals("ATM")) {
			model.addAttribute("amount", totalCartAmount);
			return "shopper/vnpay-demo";
		}
		else {
			return "shopper/order-result";
		}		
	}
	
	@RequestMapping("/updateOrder")
	public String updateOrder(@RequestParam("action") String action,
								@RequestParam("id") int id,
								@RequestParam("page") int page,
								Model model) {
		SessionFactory factory = Utils.initSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		
		try {
			Order order = OrderDAO.getOrderById(id);
			
			order.changeDeliveryStatus(action);
			
			session.update(order);
			session.getTransaction().commit();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			factory.close();
		}

		return Utils.toUpdateOrderForm(page, id, model);
	}
	
	
	@RequestMapping("/searchOrder")
	public String searchProduct(@RequestParam("nameSearch") String nameSearch,
								@RequestParam("paymentMethod") String paymentMethod,
								@RequestParam("paymentStatus") String paymentStatus,
								@RequestParam("deliveryStatus") String deliveryStatus,
								Model model) {
		SessionFactory factory = Utils.initSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		
		try {
			List<Order> orders = OrderDAO.searchOrder(session, nameSearch, paymentMethod, paymentStatus, deliveryStatus);
			
			model.addAttribute("ORDER_LIST", orders);
			Utils.setAdminView(model, "display_orders");
	        model.addAttribute("page", 1);
		} finally {
			factory.close();
		}
		
		return "admin/layout";
	}
}
