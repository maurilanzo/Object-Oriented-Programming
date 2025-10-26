package diet;

public class RawMaterial implements NutritionalElement {
    String name;
    double calories;
    double proteins;
    double carbs;
    double fat;
    

  

    public RawMaterial(String name, double calories, double proteins, double carbs, double fats) {
        this.name = name;
        this.calories = calories;
        this.proteins = proteins;
        this.carbs = carbs;
        this.fat = fats;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getCalories() {
        return calories;
    }

    @Override
    public double getProteins() {
        return proteins;
    }

    @Override
    public double getCarbs() {
        return carbs;
    }

    @Override
    public double getFat() {
        return fat;
    }

    @Override
    public boolean per100g() {
        // TODO Auto-generated method stub
        return true;
    }

}
