package selling_shoes_website_project.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import selling_shoes_website_project.DAO.BrandDAO;
import selling_shoes_website_project.DAO.CartItemDAO;
import selling_shoes_website_project.DAO.CategoryDAO;
import selling_shoes_website_project.DAO.OrderDAO;
import selling_shoes_website_project.DAO.ProductDAO;
import selling_shoes_website_project.DAO.ReviewDAO;
import selling_shoes_website_project.DAO.WishListDAO;
import selling_shoes_website_project.entity.Brand;
import selling_shoes_website_project.entity.Category;
import selling_shoes_website_project.entity.Order;
import selling_shoes_website_project.entity.Product;
import selling_shoes_website_project.entity.Review;
import selling_shoes_website_project.entity.User;
import selling_shoes_website_project.utils.Utils;

@Controller
public class HomeController {
	
	@RequestMapping(value={"/","/index"})
	public String toHomePage(Model model) {
		
		Utils.addCategoryListToModel(model);
		List<Product> productListByPrice = ProductDAO.getProductListByPrice(8);
		model.addAttribute("products", productListByPrice);
		
		return "shopper/index";
	}
	
	@RequestMapping("/admin")
	public String toAdminLoginPage() {
		return "admin/login";
	}
	
	@RequestMapping("/customerLogout")
	public String customerLogout(HttpServletRequest request) {
		HttpSession userSession = request.getSession();
		userSession.invalidate();
		
		return "shopper/login";
	}
	
	@RequestMapping("/customerLogin")
	public String toCustomerLoginPage() {
		return "shopper/login";
	}
	
	@RequestMapping("/signupForm")
	public String toSignupForm() {
		return "shopper/register";
	}
	
	@RequestMapping("/cart")
	public String toCart(HttpServletRequest request, Model model) {
		return Utils.toCart(request, model);
	}
	
	@RequestMapping("/add-to-cart")
	public String toCart(@RequestParam("id") int product_id,
							HttpServletRequest request,
							Model model) {
		User user = Utils.getCurrentUser(request);
		int user_id = user.getId();
		
		CartItemDAO.addProductToCart(product_id, user_id);

		return Utils.toCart(request, model);
	}
	
	@RequestMapping("/product-listing")
	public String toProductListing(Model model, HttpServletRequest request) {
		Integer brandId = null;
		Integer categoryId = null;
		int priceMax = -1;
		int priceMin = -1;
		
		try {
			brandId = Integer.valueOf(request.getParameter("brandId"));	
		} catch (Exception e) {
			brandId = null;
		}
		
		try {
			categoryId = Integer.valueOf(request.getParameter("categoryId"));	
		} catch (Exception e) {
			categoryId = null;
		}
		
		try {
			priceMax = Integer.valueOf(request.getParameter("priceMax"));	
		} catch (Exception e) {
			priceMax = -1;
		}
		
		try {
			priceMin = Integer.valueOf(request.getParameter("priceMin"));	
		} catch (Exception e) {
			priceMin = -1;
		}
		
		List<Product> products = ProductDAO.searchProduct(categoryId, brandId, priceMin, priceMax);
		List<Category> categories = CategoryDAO.getCategoryList();
		List<Brand> brands = BrandDAO.getBrandList();
		
		model.addAttribute("products", products);
		model.addAttribute("categories", categories);
		model.addAttribute("CATEGORY_LIST", categories);
		model.addAttribute("brands", brands);
		
		return "shopper/product-listing";
	}
	
	@RequestMapping("/product")
	public String toProductDetail(@RequestParam("id") int id, Model model) {
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
	
	@RequestMapping("/wishlist")
	public String toWishList(HttpServletRequest request, Model model) {
		User user = Utils.getCurrentUser(request);
		int userId = user.getId();
		List<Product> products = WishListDAO.getWishList(userId);
		
		model.addAttribute("products", products);
		Utils.addCategoryListToModel(model);
		return "shopper/wishlist";
	}
	
	@RequestMapping("/about")
	public String toAbout(Model model) {
		Utils.addCategoryListToModel(model);
		return "shopper/about";
	}
	
	@RequestMapping("/contact")
	public String toContact(Model model) {
		Utils.addCategoryListToModel(model);
		return "shopper/contact";
	}
	
	@RequestMapping("/my-account")
	public String toMyAccount(HttpServletRequest request, Model model) {
		Utils.addCategoryListToModel(model);
		User user = Utils.getCurrentUser(request);
		int user_id = user.getId();
		List<Order> orders = OrderDAO.getOrderListByUserId(user_id);
		
		model.addAttribute("orderWebs", orders);
		return "shopper/account";
	}
	
	@RequestMapping("/orderDetail")
	public String toOrderDetail(@RequestParam("id") int id,
								Model model) {
		Order order = OrderDAO.getOrderById(id);
		
		Utils.addCategoryListToModel(model);
		model.addAttribute("orderWeb", order);
		return "shopper/order-detail";
	}
	
	@RequestMapping("/adminDashboard")
	public String toAdminDashboard(Model model) {
		Utils.setAdminView(model, "display_dashboard");
		return "admin/layout";
	}
	
	@RequestMapping("/productsPage")
	public String toProductList(){
		return "admin/products";
	}
	
	@RequestMapping("/addProductForm")
	public String toAddProductForm(@RequestParam("page") int page, Model model) {
		model.addAttribute("page", page);
		return "admin/addProduct";
	}
	
	@RequestMapping("/updateProductForm")
	public String toUpdateProductForm(@RequestParam("page") int page,
										@RequestParam("id") int id,
										Model model) {
		try {
			Product product = ProductDAO.getProductById(id);
			Utils.addCategoryListToModel(model);
			model.addAttribute("product", product);
			model.addAttribute("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "admin/updateProduct";
	}
	
	@RequestMapping("/updateOrderForm")
	public String toUpdateOrderForm(@RequestParam("page") int page,
										@RequestParam("id") int id,
										Model model) {
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
}
