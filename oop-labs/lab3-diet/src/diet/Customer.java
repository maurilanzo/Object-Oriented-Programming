package diet;


public class Customer {
	private String firstName;
	private String lastName;
	private String email;
	private String phone;

	public Customer(String firstName, String lastName, String email, String phone) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
	}

	public String getLastName() {
		return lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void SetEmail(String email) {
		this.email=email;
	}
	
	public void setPhone(String phone) {
		this.phone=phone;
	}
	
	@Override
	public String toString(){
		return String.format("%s %s",firstName,lastName);
	}
	
}
