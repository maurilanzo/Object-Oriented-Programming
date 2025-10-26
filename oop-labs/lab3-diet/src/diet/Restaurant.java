package diet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import diet.Order.OrderStatus;

/**
 * Represents a restaurant class with given opening times and a set of menus.
 */
public class Restaurant {
	String name;
	Takeaway takeaway;
	

	public Restaurant(String name, Takeaway takeaway) {
		this.name = name;
		this.takeaway=takeaway;
	}
	
	/**
	 * retrieves the name of the restaurant.
	 *
	 * @return name of the restaurant
	 */
	public String getName() {
		return name;
	}

	/**
	 * Define opening times.
	 * Accepts an array of strings (even number of elements) in the format {@code "HH:MM"},
	 * so that the closing hours follow the opening hours
	 * (e.g., for a restaurant opened from 8:15 until 14:00 and from 19:00 until 00:00,
	 * arguments would be {@code "08:15", "14:00", "19:00", "00:00"}).
	 *
	 * @param hm sequence of opening and closing times
	 */
	String [] hours;
	public void setHours(String ... hm) {
		hours = hm;
	}

	public String [] getHours(){
		return hours;
	}

	/**
	 * Checks whether the restaurant is open at the given time.
	 *
	 * @param time time to check
	 * @return {@code true} is the restaurant is open at that time
	 */
	


	public boolean isOpenAt(String time){
		for (int i=0; i<hours.length;i+=2){
			if (time.compareTo(hours[i])>=0 && time.compareTo(hours[i+1])<0)
				return true;
		}
		return false;
	}

	/**
	 * Adds a menu to the list of menus offered by the restaurant
	 *
	 * @param menu	the menu
	 */
	private Map<String,Menu> menus = new TreeMap<>();


	public void addMenu(Menu menu) {
		menus.put(menu.getName(),menu);
	}

	/**
	 * Gets the restaurant menu with the given name
	 *
	 * @param name	name of the required menu
	 * @return menu with the given name
	 */
	public Menu getMenu(String name) {
		return menus.get(name);
	}

	/**
	 * Retrieve all order with a given status with all the relative details in text format.
	 *
	 * @param status the status to be matched
	 * @return textual representation of orders
	 */
	public SortedSet<Order> orders = new TreeSet<>(Comparator.comparing(Order::getrestaurantName).thenComparing(Order::getCustomer).thenComparing(Order::getTime));
/* 
	public String ordersWithStatus(OrderStatus status) {
		String ord="";
		for(Order o:orders){
			if (o.getrestaurantName().compareTo(this.name)==0 && o.getStatus().compareTo(status)==0){	//same restaurant and correct order status
				ord += o.toString();
			}
		}
		return ord;
	}
		*/

	public String ordersWithStatus(OrderStatus status) { //better with streams
        return orders.stream()
                     .filter(order -> order.getStatus() == status && order.getrestaurantName().equals(this.name))
                     .map(Order::toString)
                     .reduce((o1, o2) -> o1 + "\n" + o2)
                     .orElse("");
    }

	public void addOrder(Order order) {
		orders.add(order);
	}

}
