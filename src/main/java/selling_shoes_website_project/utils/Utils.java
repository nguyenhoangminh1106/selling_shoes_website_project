package selling_shoes_website_project.utils;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.ui.Model;

import selling_shoes_website_project.DAO.CartItemDAO;
import selling_shoes_website_project.DAO.CategoryDAO;
import selling_shoes_website_project.DAO.OrderDAO;
import selling_shoes_website_project.DAO.ProductDAO;
import selling_shoes_website_project.DAO.ReviewDAO;
import selling_shoes_website_project.DAO.WishListDAO;
import selling_shoes_website_project.entity.Brand;
import selling_shoes_website_project.entity.CartItem;
import selling_shoes_website_project.entity.Category;
import selling_shoes_website_project.entity.Order;
import selling_shoes_website_project.entity.OrderDetail;
import selling_shoes_website_project.entity.Product;
import selling_shoes_website_project.entity.ProductSize;
import selling_shoes_website_project.entity.Review;
import selling_shoes_website_project.entity.User;
import selling_shoes_website_project.entity.WishItem;

public class Utils {
	/**
	 * GET CURRENT DATE AND TIME
	 * 
	 * @return
	 */
	public static Date getCurrentDatetime() {
		return new Date();
	}
	
	/**
	 * CHOOSE ADMIN VIEW LAYOUT
	 * 
	 * @param model
	 * @param mode
	 */
	public static void setAdminView (Model model, String mode) {
		model.addAttribute("display_dashboard", false);
		model.addAttribute("display_products", false);
		model.addAttribute("display_orders", false);
		model.addAttribute("display_order_detail", false);
		
		model.addAttribute(mode, true);
	}
	
	public static String toHomePage(Model model) {
		List<Category> categories = CategoryDAO.getCategoryList();
		List<Product> productListByPrice = ProductDAO.getProductListByPrice(8);
		
		model.addAttribute("CATEGORY_LIST", categories);
		model.addAttribute("products", productListByPrice);;
		return "shopper/index";
	}
	
	public static void addCategoryListToModel(Model model) {
		List<Category> categories = CategoryDAO.getCategoryList();
		model.addAttribute("CATEGORY_LIST", categories);
	}
	
	public static String toCart(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		int user_id = user.getId();

		List<CartItem> cartItems = CartItemDAO.getCartItemsByUserId(user_id);
		
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("customer", user);
		addCategoryListToModel(model);
		return "shopper/cart";
	}
	
	public static User getCurrentUser(HttpServletRequest request) {
		return (User)request.getSession().getAttribute("user");
	}
	
	public static String toUpdateOrderForm(int page, int id, Model model) {
		try {
			Order order = OrderDAO.getOrderById(id);
			model.addAttribute("order", order);
			model.addAttribute("page", page);
			Utils.setAdminView(model, "display_order_detail");
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return "admin/layout";
	}
	
	public static SessionFactory initSessionFactory() {
		return new Configuration().configure()
				.addAnnotatedClass(Order.class)
				.addAnnotatedClass(User.class)
				.addAnnotatedClass(OrderDetail.class)
				.addAnnotatedClass(Brand.class)
				.addAnnotatedClass(CartItem.class)
				.addAnnotatedClass(Category.class)
				.addAnnotatedClass(Product.class)
				.addAnnotatedClass(ProductSize.class)
				.addAnnotatedClass(User.class)
				.addAnnotatedClass(WishItem.class)
				.addAnnotatedClass(Review.class)
				.buildSessionFactory();
	}

	public static String toProductDetail(int id, Model model) {
		Product product = ProductDAO.getProductById(id);
		Brand brand = product.getBrand();
		Integer brand_id = brand.getId();
		List<Product> relatedProducts = ProductDAO.searchProduct(null, brand_id, -1, -1);
		List<Review> reviews = ReviewDAO.getReviewsByProductId(id);
		model.addAttribute("product", product);
		model.addAttribute("relatedProducts", relatedProducts);
		model.addAttribute("reviews", reviews);
		
		Utils.addCategoryListToModel(model);
		return "shopper/product";
	}
	

	public static String toWishList(HttpServletRequest request, Model model) {
		User user = Utils.getCurrentUser(request);
		int userId = user.getId();
		List<Product> products = WishListDAO.getWishList(userId);
		
		model.addAttribute("products", products);
		Utils.addCategoryListToModel(model);
		return "shopper/wishlist";
	}
}
