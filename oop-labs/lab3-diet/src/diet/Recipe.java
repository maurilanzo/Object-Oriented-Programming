package diet;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a recipe of the diet.
 * 
 * A recipe consists of a a set of ingredients that are given amounts of raw materials.
 * The overall nutritional values of a recipe can be computed
 * on the basis of the ingredients' values and are expressed per 100g
 * 
 *
 */
public class Recipe implements NutritionalElement {
	
	/**
	 * Adds the given quantity of an ingredient to the recipe.
	 * The ingredient is a raw material.
	 * 
	 * @param material the name of the raw material to be used as ingredient
	 * @param quantity the amount in grams of the raw material to be used
	 * @return the same Recipe object, it allows method chaining.
	 */

	String name;
    double calories=0;
    double proteins=0;
    double carbs=0;
    double fat=0;
	Food food;
	double weight=0;
	private Map<String, Double> ingredients=new LinkedHashMap<>();
	
	public Recipe (String name, Food food){
		this.name=name;	
		this.food=food;
	}

	public Recipe addIngredient(String material, double quantity) {
		ingredients.put(material, quantity);
		NutritionalElement raw = food.getRawMaterial(material);
		calories+=raw.getCalories()*quantity/100;
		proteins+=raw.getProteins()*quantity/100;
		carbs+=raw.getCarbs()*quantity/100;
		fat+=raw.getFat()*quantity/100;
		weight+=quantity;
		return this;
	}

	@Override
	public String getName() {
		return name;
	}

	
	@Override
	public double getCalories() {
		return calories*100/weight;
	}
	

	@Override
	public double getProteins() {
		return proteins*100/weight;
	}

	@Override
	public double getCarbs() {
		return carbs*100/weight;
	}

	@Override
	public double getFat() {
		return fat*100/weight;
	}

	/**
	 * Indicates whether the nutritional values returned by the other methods
	 * refer to a conventional 100g quantity of nutritional element,
	 * or to a unit of element.
	 * 
	 * For the {@link Recipe} class it must always return {@code true}:
	 * a recipe expresses nutritional values per 100g
	 * 
	 * @return boolean indicator
	 */
	@Override
	public boolean per100g() {
		return true;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		//sb.append(name + ":\n");
		for(String ingredient : ingredients.keySet()){
			sb.append(ingredient + ": " + ingredients.get(ingredient) + "g\n");
		}
		return sb.toString().trim(); 
	}

	
}
