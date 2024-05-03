import java.util.*;

public class Menu {

    private final  List<Coffee> menuData = new ArrayList<>();

    // this method to add coffee object to database
    public void addMenu(Coffee coffee){

        this.menuData.add(coffee);
    }

    //it returns the collection of coffee object that matches the user input
    public List<Coffee> findMatch(Coffee userCriteria){
        List<Coffee> compatibleCoffee = new ArrayList<>();

// Loop through each coffee item in the menu data
        for (Coffee coffee : this.menuData) {
            // Check if the coffee price is within the user's specified price range
            if (coffee.getPrice() < userCriteria.getMinPrice() || coffee.getPrice() > userCriteria.getMaxPrice()) {
                // Skip this coffee if its price is outside the specified range
                continue;
            }

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

            //a stream is a sequence of elements that can be processed sequentially
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

}