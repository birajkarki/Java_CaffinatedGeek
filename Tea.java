import java.util.Set;

public class Tea {
    public static final int DEFAULT_STEEPING_TIME = 0;
    public static final int DEFAULT_ID = 0;
    // Instance variables
    private final int menuId;
    private final String menuName;
    private final int temperature;
    private final int steepingTime;
    private final double price;
    private final Sugar sugar;
    private final Set<Milk> milk;
    private final Set<Extras> extras;
    private String description; 
    private double minPrice;
    private double maxPrice;


    // Sugar enum
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

    // Milk enum
    public enum Milk {
        FULL_CREAM("Full Cream"),
        SKIMMED("Skimmed"),
        ALMOND("Almond"),
        SOY("Soy"),
        COCONUT("Coconut");

        private final String label;

        Milk(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    // Extras enum
    public enum Extras {
        WHIPPED_CREAM("Whipped Cream"),
        CHOCOLATE_SYRUP("Chocolate Syrup"),
        CARAMEL_SYRUP("Caramel Syrup"),
        VANILLA_SYRUP("Vanilla Syrup");

        private final String label;

        Extras(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    // Constructor
    public Tea(int menuId, String menuName, double price, int temperature, int steepingTime, Tea.Sugar sugar, Set<Milk> selectedMilkSet, Set<Extras> selectedExtras) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.price = price;
        this.temperature = temperature;
        this.steepingTime = steepingTime;
        this.sugar = sugar;
        this.milk = selectedMilkSet;
        this.extras = selectedExtras;
    }
    // Getters
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
}
