package diet;

import java.util.*;


/**
 * Represents a takeaway restaurant chain.
 * It allows managing restaurants, customers, and orders.
 */
public class Takeaway {

	/**
	 * Constructor
	 * @param food the reference {@link Food} object with materials and products info.
	 */
	Food food;
	public Takeaway(Food food){
		this.food = food;
	}

	/**
	 * Creates a new restaurant with a given name
	 *
	 * @param restaurantName name of the restaurant
	 * @return the new restaurant
	 */

	 private Map<String,Restaurant> restaurants = new TreeMap<>();

	public Restaurant addRestaurant(String restaurantName) {
		Restaurant restaurant = new Restaurant(restaurantName,this);
		restaurants.put(restaurantName, restaurant);
		return restaurant;
	}

	/**
	 * Retrieves the names of all restaurants
	 *
	 * @return collection of restaurant names
	 */
	public Collection<String> restaurants() {
		return restaurants.keySet();
	}

	/**
	 * Creates a new customer for the takeaway
	 * @param firstName first name of the customer
	 * @param lastName	last name of the customer
	 * @param email		email of the customer
	 * @param phoneNumber mobile phone number
	 *
	 * @return the object representing the newly created customer
	 */
	List<Customer> customers= new ArrayList<>();

	public Customer registerCustomer(String firstName, String lastName, String email, String phoneNumber) {
		Customer c = new Customer(firstName,lastName,email,phoneNumber);
		customers.add(c);
		return c;
	}

	/**
	 * Retrieves all registered customers
	 *
	 * @return sorted collection of customers
	 */
	public Collection<Customer> customers(){
		List<Customer> sortedCustomers= customers.stream().sorted(Comparator.comparing(Customer::getLastName).thenComparing(Customer::getFirstName)).toList(); 
		return sortedCustomers;
	}


	/**
	 * Creates a new order for the chain.
	 *
	 * @param customer		 customer issuing the order
	 * @param restaurantName name of the restaurant that will take the order
	 * @param time	time of desired delivery
	 * @return order object
	 */
	
	public Order createOrder(Customer customer, String restaurantName, String time) {
		int i=0;
		String t;
		Restaurant r = restaurants.get(restaurantName);
		if (time.length() ==4){
			time= "0"+time;
		}

		String [] times = r.getHours();
			if (r.isOpenAt(time)){
			Order o = new Order(customer, restaurantName, time);
			r.addOrder(o);
			
			return o;
		} 
			
		else {
			while ( i < times.length && times[i].compareTo(time)<0){
				i++;
			}
			if (i == times.length ){
				Order o = new Order(customer, restaurantName, times[0]);
				r.addOrder(o);
				return o;
			}

			Order o = new Order(customer, restaurantName, times[i]);
			r.addOrder(o);
			return o;
		}

	}

	/**
	 * Find all restaurants that are open at a given time.
	 *
	 * @param time the time with format {@code "HH:MM"}
	 * @return the sorted collection of restaurants
	 */
	
	public Collection<Restaurant> openRestaurants(String time){
		SortedSet<Restaurant> sortedRestaurants = new TreeSet<>(Comparator.comparing(Restaurant::getName));
		for (Restaurant r:restaurants.values()){
			if (r.isOpenAt(time)) sortedRestaurants.add(r);
		}
		return sortedRestaurants;
	}
}
