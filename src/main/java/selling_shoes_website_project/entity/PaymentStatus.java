package selling_shoes_website_project.entity;

public class PaymentStatus {
	private String status;
	private String message;
	private String URL;
	public PaymentStatus() {
		super();
	}
	public String getStatus() {
		return status;
	}
	
	
	public PaymentStatus(String status, String message, String uRL) {
		super();
		this.status = status;
		this.message = message;
		URL = uRL;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	
	
}
