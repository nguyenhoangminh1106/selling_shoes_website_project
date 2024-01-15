package selling_shoes_website_project.entity;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "consignee")
    private String consignee;

    @Column(name = "consignee_phone")
    private String consigneePhone;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "delivery_status")
    private String deliveryStatus;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "sent_mail")
    private boolean sentMail;

    @Column(name = "total_amount")
    private int totalAmount;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private User customer;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy="order")
    private List<OrderDetail> orderDetails = new ArrayList<>();

	public Order() {
		super();
	}

	public Order(Date createdAt, Date updatedAt, String consignee, String consigneePhone, String deliveryAddress,
			String deliveryStatus, String paymentMethod, String paymentStatus, boolean sentMail, int totalAmount,
			User customer) {
		super();
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.consignee = consignee;
		this.consigneePhone = consigneePhone;
		this.deliveryAddress = deliveryAddress;
		this.deliveryStatus = deliveryStatus;
		this.paymentMethod = paymentMethod;
		this.paymentStatus = paymentStatus;
		this.sentMail = sentMail;
		this.totalAmount = totalAmount;
		this.customer = customer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getConsigneePhone() {
		return consigneePhone;
	}

	public void setConsigneePhone(String consigneePhone) {
		this.consigneePhone = consigneePhone;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public boolean isSentMail() {
		return sentMail;
	}

	public void setSentMail(boolean sentMail) {
		this.sentMail = sentMail;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public User getCustomer() {
		return customer;
	}

	public void setCustomer(User customer) {
		this.customer = customer;
	}
	
	public String getFormatId() {
		return "#TS" + id;
	}
    
	
	
	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public String formatPrice(int price) {
		return "$" + price;
	}
	
	public String getDeliveryAction() {
		switch(deliveryStatus) {
			case "Approving":
				return "Approve";
			case "Waiting":
				return "Deliver";
			case "Waiting 2":
				return "Deliver again";	
			case "Delivered 2":
				return "Deliver unsuccessfully";
			default:
				return "";
		}
	}
	
	public void changeDeliveryStatus(String action) {
		switch (action) {
			case "Approve":
				deliveryStatus =  "Waiting";
				break;
			case "Deliver":
				deliveryStatus = "Waiting 2";
				break;
			case "Deliver again":
				deliveryStatus = "Delivered 2";
				break;
			case "Deliver unsuccessfully":
				deliveryStatus = "Unsuccessful";
				break;
			case "Deliver successfully":
				deliveryStatus = "Successful";
				break;
			case "Cancel":
				deliveryStatus = "Cancelled";
				break;
			default:
				return;
		}
	}
    
}
