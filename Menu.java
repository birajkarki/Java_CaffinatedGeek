import java.util.*;

public class Menu {

    private final List<Coffee> coffeeMenuData = new ArrayList<>();
    private final List<Tea> teaMenuData = new ArrayList<>();

    // Method to add coffee object to database
    public void addCoffeeMenu(Coffee coffee){
        this.coffeeMenuData.add(coffee);
    }

    // Method to add tea object to database
    public void addTeaMenu(Tea tea){
        this.teaMenuData.add(tea);
    }

    // Method to find coffee menu items matching user criteria
    public List<Coffee> findMatchCoffee(Coffee userCriteria){
        List<Coffee> compatibleCoffee = new ArrayList<>();

        // Loop through each coffee item in the menu data
        for (Coffee coffee : this.coffeeMenuData) {
            // Check if the number of shots in the coffee matches the user's criteria
            if (coffee.getNumberOfShots() != userCriteria.getNumberOfShots()) {
                // Skip this coffee if the number of shots doesn't match
                continue;
            }

            // Check if the sugar preference of the coffee matches the user's preference
            if (!coffee.isSugar().equals(userCriteria.isSugar())) {
                // Skip this coffee if the sugar preference doesn't match
                continue;
            }

            // Check if the user skipped selecting both extras and milk
            if (userCriteria.getMilk().isEmpty() && userCriteria.getExtras().isEmpty()) {
                // Skip this coffee if it contains any extras or milk
                if (!coffee.getMilk().isEmpty() || !coffee.getExtras().isEmpty()) {
                    continue;
                }
            } else {
                // Check if any of the milk options in the coffee match the user's milk preference
                if (!userCriteria.getMilk().isEmpty() && userCriteria.getMilk().stream().noneMatch(coffee.getMilk()::contains)) {
                    // Skip this coffee if none of the user's milk preferences match
                    continue;
                }

                // Check if any of the extras in the coffee match the user's extra preferences
                if (!userCriteria.getExtras().isEmpty() && userCriteria.getExtras().stream().noneMatch(coffee.getExtras()::contains)) {
                    // Skip this coffee if none of the user's extra preferences match
                    continue;
                }
            }
            // If all criteria are met, add this coffee to the list of compatible coffees
            compatibleCoffee.add(coffee);
        }

        return compatibleCoffee;
    }

    // Method to find tea menu items matching user criteria
    public List<Tea> findMatchTea(Tea userCriteria){
        List<Tea> compatibleTea = new ArrayList<>();

        // Loop through each tea item in the menu data
        for (Tea tea : this.teaMenuData) {
            // Check if the temperature of the tea matches the user's criteria
            if (tea.getTemperature() != userCriteria.getTemperature()) {
                // Skip this tea if the temperature doesn't match
                continue;
            }

            // Check if the steeping time of the tea matches the user's criteria
            if (tea.getSteepingTime() != userCriteria.getSteepingTime()) {
                // Skip this tea if the steeping time doesn't match
                continue;
            }

            // Check if the sugar preference of the tea matches the user's preference
            if (!tea.getSugar().equals(userCriteria.getSugar())) {
                // Skip this tea if the sugar preference doesn't match
                continue;
            }

            // Check if the user skipped selecting both extras and milk
            if (userCriteria.getMilk().isEmpty() && userCriteria.getExtras().isEmpty()) {
                // Skip this tea if it contains any extras or milk
                if (!tea.getMilk().isEmpty() || !tea.getExtras().isEmpty()) {
                    continue;
                }
            } else {
                // Check if any of the milk options in the tea match the user's milk preference
                if (!userCriteria.getMilk().isEmpty() && userCriteria.getMilk().stream().noneMatch(tea.getMilk()::contains)) {
                    // Skip this tea if none of the user's milk preferences match
                    continue;
                }

                // Check if any of the extras in the tea match the user's extra preferences
                if (!userCriteria.getExtras().isEmpty() && userCriteria.getExtras().stream().noneMatch(tea.getExtras()::contains)) {
                    // Skip this tea if none of the user's extra preferences match
                    continue;
                }
            }
            // If all criteria are met, add this tea to the list of compatible teas
            compatibleTea.add(tea);
        }

        return compatibleTea;
    }
}
