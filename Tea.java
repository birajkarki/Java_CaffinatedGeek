import java.util.Set;

public class Tea {
    private final String type; 
    private final int menuId;
    private final String menuName;
    private final double price;
    private final int temperature;
    private final int steepingTime;
    private final Sugar sugar;
    private final Set<Milk> milk;
    private final Set<Extras> extras;
    private double minPrice;
    private double maxPrice;

    public enum Sugar {
        YES("Yes"),
        NO("No"),
        DONT_MIND("I don't mind");

        private final String label;

        Sugar(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    public Tea(String type, int menuId, String menuName, double price, int temperature, int steepingTime, Sugar sugar, Set<Milk> milk, Set<Extras> extras) {
        this.type = type;
        this.menuId = menuId;
        this.menuName = menuName;
        this.price = price;
        this.temperature = temperature;
        this.steepingTime = steepingTime;
        this.sugar = sugar;
        this.milk = milk;
        this.extras = extras;
    }

    public String getType() {
        return type;
    }

    public int getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public double getPrice() {
        return price;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getSteepingTime() {
        return steepingTime;
    }

    public Sugar getSugar() {
        return sugar;
    }

    public Set<Milk> getMilk() {
        return milk;
    }

    public Set<Extras> getExtras() {
        return extras;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public boolean matchesCriteria(Tea userCriteria) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'matchesCriteria'");
    }

    // Define Milk and Extras interfaces here
}
