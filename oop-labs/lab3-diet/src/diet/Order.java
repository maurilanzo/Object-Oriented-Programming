package diet;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Represents and order issued by an {@link Customer} for a {@link Restaurant}.
 *
 * When an order is printed to a string is should look like:
 * <pre>
 *  RESTAURANT_NAME, USER_FIRST_NAME USER_LAST_NAME : DELIVERY(HH:MM):
 *  	MENU_NAME_1->MENU_QUANTITY_1
 *  	...
 *  	MENU_NAME_k->MENU_QUANTITY_k
 * </pre>
 */
public class Order {
	Customer customer;
	String restaurantName;
	String time;
	
	public Order (Customer customer, String restaurantName, String time){
		this.customer=customer;
		this.restaurantName=restaurantName;
		this.time=time;
	}

	/**
	 * Possible order statuses
	 */
	public enum OrderStatus {
		ORDERED, READY, DELIVERED
	}

	/**
	 * Accepted payment methods
	 */
	public enum PaymentMethod {
		PAID, CASH, CARD
	}

	/**
	 * Set payment method
	 * @param pm the payment method
	 */
	PaymentMethod pm = PaymentMethod.CASH;
	public void setPaymentMethod(PaymentMethod pm) {
		this.pm=pm;
	}

	/**
	 * Retrieves current payment method
	 * @return the current method
	 */
	public PaymentMethod getPaymentMethod() {
		return pm;
	}

	/**
	 * Set the new status for the order
	 * @param os new status
	 */
	OrderStatus os = OrderStatus.ORDERED;
	public void setStatus(OrderStatus os) {
		this.os=os;
	}

	/**
	 * Retrieves the current status of the order
	 *
	 * @return current status
	 */
	public OrderStatus getStatus() {
		return os;
	}

	public String getrestaurantName() {
		return restaurantName;
	}

	public String getCustomer() {
		return customer.getFirstName() + " " + customer.getLastName();
	}
	public String getTime() {
		return time;
	}

	/**
	 * Add a new menu to the order with a given quantity
	 *
	 * @param menu	menu to be added
	 * @param quantity quantity
	 * @return the order itself (allows method chaining)
	 */
	private SortedMap<String,Integer> menus = new TreeMap<>();
	public Order addMenus(String menu, int quantity) {
		menus.put(menu,quantity);
		return this;
	}

	
	@Override
	public String toString() { //all of this in order not to put \n if it is the last element
		StringBuilder x = new StringBuilder();
		x.append(String.format("%s, %s %s : (%s):\n", restaurantName, customer.getFirstName(), customer.getLastName(), time));
	
		Iterator<Map.Entry<String, Integer>> iterator = menus.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Integer> entry = iterator.next();
			x.append("\t").append(entry.getKey()).append("->").append(entry.getValue());
			if (iterator.hasNext()) {
				x.append("\n");
			}
		}
	
		return x.toString();
	}

	
	
}
