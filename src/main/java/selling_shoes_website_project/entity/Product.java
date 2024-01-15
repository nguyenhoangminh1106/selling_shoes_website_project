package selling_shoes_website_project.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import selling_shoes_website_project.DAO.WishListDAO;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "description")
    private String description;

    @Column(name = "is_delete")
    private Boolean isDelete;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "status")
    private String status;

    @Column(name = "version_name")
    private String versionName;
    
    @Column(name = "image_data")
    private byte[] imageData;

    @ManyToOne
    @JoinColumn(name="brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

	public Product() {
		super();
	}
	

	public Product(Date updatedAt, String description, String name, int price, String versionName, byte[] imageData,
			Brand brand, Category category) {
		super();
		this.updatedAt = updatedAt;
		this.description = description;
		this.name = name;
		this.price = price;
		this.versionName = versionName;
		this.imageData = imageData;
		this.brand = brand;
		this.category = category;
	}
	
	







	public Product(Date createdAt, Date updatedAt, String description, Boolean isDelete, String name, int price,
			String status, byte[] imageData, Brand brand) {
		super();
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.description = description;
		this.isDelete = isDelete;
		this.name = name;
		this.price = price;
		this.status = status;
		this.imageData = imageData;
		this.brand = brand;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}
	
	
	public String formatPrice() {
		return "$" + price;
	}
	
	public String formatPrice(int price) {
		return "$" + price;
	}
	
	public boolean isWishList(int user_id) {
		return WishListDAO.isWishList(id, user_id);
	}
	
}    
    
