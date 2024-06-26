import java.util.Set;

public class Beverage {
    // Instance variables

    private final String type; // Type of coffee
    private final int menuId;
    private final String menuName;
    private final double price;
    private final int numberOfShots;
    private final Sugar sugar;
    private final Set<Milk> milk;
    private final Set<Extras> extras;
    private final String description;
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

    public Beverage(String type, int menuId, String menuName, double price, int numberOfShots, Sugar sugar, Set<Milk> milk, Set<Extras> extras, String description) {
        this.type = type;
        this.menuId = menuId;
        this.menuName = menuName;
        this.price = price;
        this.numberOfShots = numberOfShots;
        this.sugar = sugar;
        this.milk = milk;
        this.extras = extras;
        this.description = description;
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

    public int getNumberOfShots() {
        return numberOfShots;
    }

    public Sugar isSugar() {
        return sugar;
    }

    public Set<Milk> getMilk() {
        return milk;
    }

    public Set<Extras> getExtras() {
        return extras;
    }

    public String getDescription() {
        return description;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public boolean matchesCriteria(Beverage userCriteria) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'matchesCriteria'");
    }
}
