package selling_shoes_website_project.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import selling_shoes_website_project.DAO.BrandDAO;
import selling_shoes_website_project.DAO.CategoryDAO;
import selling_shoes_website_project.DAO.ProductDAO;
import selling_shoes_website_project.entity.Brand;
import selling_shoes_website_project.entity.Category;
import selling_shoes_website_project.entity.Product;
import selling_shoes_website_project.utils.Utils;

@Controller
public class ProductController {
	
	private static int maxResultPerPage = 5;
	
	/**
	 * GET PRODUCT LIST
	 * 
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping("/getProductList")
    public String showProducts(@RequestParam("page") int page, Model model) {
		SessionFactory factory = Utils.initSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		
		if (page == 0) {
			page = 1;
		}		
		int resultIndex = (page - 1) * maxResultPerPage;
		
		try {
			List<Product> products = ProductDAO.getProductList(session, resultIndex, maxResultPerPage);
			
			session.getTransaction().commit();
	        model.addAttribute("PRODUCT_LIST", products);
	        Utils.setAdminView(model, "display_products");
	        model.addAttribute("page", page);
	        
	        
		} finally {
			factory.close();
		}
        return "admin/layout";
    }
	
	/**
	 * SEARCH FOR PRODUCTS BY NAME, STATUS AND BRAND
	 * 
	 * @param nameSearch
	 * @param status
	 * @param brandName
	 * @param model
	 * @return
	 */
	@RequestMapping("/searchProduct")
	public String searchProduct(@RequestParam("nameSearch") String nameSearch,
								@RequestParam("status") String status,
								@RequestParam("brandName") String brandName,
								Model model) {
		SessionFactory factory = Utils.initSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		
		try {
			Integer brand_id = BrandDAO.getIdByName(session, brandName);
			List<Product> products = ProductDAO.searchProduct(session, nameSearch, status, brand_id);
			
			model.addAttribute("PRODUCT_LIST", products);
			Utils.setAdminView(model, "display_products");
	        model.addAttribute("page", 1);
		} finally {
			factory.close();
		}
		
		return "admin/layout";
	}
	
	
	/**
	 * USER SEARCH FOR PRODUCTS
	 * 
	 * @param nameSearch
	 * @param status
	 * @param brandName
	 * @param model
	 * @return
	 */
	@RequestMapping("/userSearchByName")
	public String userSearchProduct(@RequestParam("nameSearch") String nameSearch,
									Model model) {
		SessionFactory factory = Utils.initSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		
		try {
			List<Product> products = ProductDAO.searchProduct(session, nameSearch, "", null);
			Utils.addCategoryListToModel(model);			
			model.addAttribute("products", products);
		} finally {
			factory.close();
		}
		
		return "shopper/search";
	}
	
	/**
	 * ADD PRODUCT
	 * 
	 * @param name
	 * @param productImage
	 * @param description
	 * @param price
	 * @param brandName
	 * @param model
	 * @return
	 */
	@RequestMapping("/addProduct")
	public String addProduct(@RequestParam("productName") String name,
								@RequestParam("productImage") MultipartFile productImage,
								@RequestParam("productDescription") String description,
								@RequestParam("productPrice") int price,
								@RequestParam("productBrand") String brandName,
								@RequestParam("page") int page,
								Model model) {
		SessionFactory factory = Utils.initSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
        try {
        	BrandDAO.addBrand(session, brandName);
        	Brand brand = BrandDAO.getBrandByName(session, brandName);
        	Date date = Utils.getCurrentDatetime();
        	byte[] image_data = productImage.getBytes();
        	
        	Product product = new Product(date, date, description, false, name, price, "Available", image_data, brand);
        	        	
            session.save(product);
            session.getTransaction().commit();
            model.addAttribute("addProduct_success", true);
            model.addAttribute("page", page);
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            model.addAttribute("addProduct_error", true);
        } finally {
        	factory.close();
        }
        
        return "admin/addProduct";
    }
	
	/**
	 * CHANGE PRODUCT STATUS
	 * 
	 * @param status
	 * @param page
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/changeProductStatus")
	public String changeProductStatus(@RequestParam("status") String status, 
										@RequestParam("page") int page,
										@RequestParam("id") int id,
										Model model) {
		SessionFactory factory = Utils.initSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		
		try {
			ProductDAO.changeProductStatus(session, status, id);
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
		} finally {
			factory.close();
		}
		
		return showProducts(page, model);
		
	}
	
	/**
	 * DELETE PRODUCT
	 * 
	 * @param page
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/deleteProduct")
    public String deleteProduct(@RequestParam("page") int page,
								@RequestParam("id") int id,
								Model model) {
		SessionFactory factory = Utils.initSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		
        try {
        	ProductDAO.deleteProduct(session, id);           

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	factory.close();
        }

        return showProducts(page, model);
    
	}
	
	/**
	 * SHOW USER IMAGE
	 * 
	 * @param id
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/productImage")
    public void showUserImage(@RequestParam("id") int id, HttpServletResponse response) throws IOException {
		Product product = ProductDAO.getProductById(id);
		byte[] imageData = product.getImageData();
        if (imageData != null) {
        	InputStream in = new ByteArrayInputStream(imageData);
            IOUtils.copy(in, response.getOutputStream());
        }
    }
	
	/**
	 * UPDATE PRODUCT
	 * 
	 * @param name
	 * @param productImage
	 * @param description
	 * @param price
	 * @param brandName
	 * @param versionName
	 * @param categoryName
	 * @param page
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/updateProduct")
    public String updateProduct(@RequestParam("productName") String name,
					            @RequestParam("productImage") MultipartFile productImage,
					            @RequestParam("productDescription") String description,
					            @RequestParam("productPrice") Integer price,
					            @RequestParam("productBrand") String brandName,
					            @RequestParam("versionName") String versionName,
					            @RequestParam("category") String categoryName,
					            @RequestParam("page") int page,
					            @RequestParam("id") int id,
					            Model model) {
		SessionFactory factory = Utils.initSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		
        try {
        	Brand brand = BrandDAO.getBrandByName(session, brandName);
        	Category category = CategoryDAO.getCategoryByName(session, categoryName);
        	Date currentDate = Utils.getCurrentDatetime();
                	
        	
        	Product product = ProductDAO.getProductById(id);
        	product.setName(name);
        	product.setDescription(description);
        	product.setPrice(price);
        	product.setBrand(brand);
        	product.setCategory(category);
        	product.setVersionName(versionName);
        	product.setUpdatedAt(currentDate);
        	
        	if (!productImage.isEmpty()) {
        		byte[] imageData = productImage.getBytes();
        		product.setImageData(imageData);
        	}
        	
        	ProductDAO.updateProduct(session, product);           

            session.getTransaction().commit();
            
            model.addAttribute("updateProduct_success", true);
            
        } catch (Exception e) {
            e.printStackTrace();
            
            model.addAttribute("updateProduct_error", true);

        } finally {
        	factory.close();
        }
		
        
        return showProducts(page, model);
    }
}
