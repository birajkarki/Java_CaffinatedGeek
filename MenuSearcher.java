import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuSearcher {

    public static String appName = "CaffinatedGeek";
    private static final String filePath = "./menu.txt";
    private final static String iconPath = "./the_caffeinated_geek.png";
    private static Menu allMenus;

    // main method
    public static void main(String[] args) {
        allMenus = loadMenuData(); // loads all the text file data.
        ImageIcon icon = new ImageIcon(iconPath);
        JOptionPane.showMessageDialog(null, "You are welcome to our app!\n\tTo Begin, use OK.", appName, JOptionPane.QUESTION_MESSAGE, icon);
        Coffee userCriteria = getUserDreamCoffee();
        List<Coffee> potentialMatches = allMenus.findMatch(userCriteria);
        if (!potentialMatches.isEmpty() ){
            display(potentialMatches, userCriteria);
        } else {
            JOptionPane.showMessageDialog(null, "Ouch!! our menu cannot meet your expectations" + "\n\tTo exit, click OK.", appName, JOptionPane.QUESTION_MESSAGE, icon);
            System.exit(0);
        }

    }

    private static Coffee getUserDreamCoffee() {


        


        Milk milk = (Milk) JOptionPane.showInputDialog(null, "Which milk would you prefer?.", appName, JOptionPane.QUESTION_MESSAGE, null, Milk.values(), Milk.NONE);
        if (milk == null) System.exit(0);

        // Convert the selected milk into a Set<Milk>
        Set<Milk> selectedMilkSet = new HashSet<>();
        selectedMilkSet.add(milk);

        // Create a panel to hold checkboxes for extras
        JPanel panel = new JPanel(new GridLayout(0, 1));

        // Create checkboxes for each extra
        Set<Extras> selectedExtras = new HashSet<> ();
        for (Extras extra : Extras.values()) {
            if (extra != Extras.SKIP) {
                JCheckBox checkBox = new JCheckBox(extra.toString());
                panel.add(checkBox);
            }
        }

        // Show the dialog with checkboxes for extras
        int result = JOptionPane.showConfirmDialog(null, panel, "choose your preferences, else skip",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);


        // Handle user selection
        if (result == JOptionPane.OK_OPTION) {
            // Loop through checkboxes to determine which extras were selected
            for (Extras extra : Extras.values()) {
                if (extra != Extras.SKIP) {
                    // gets all the extras
                    JCheckBox checkBox = (JCheckBox) panel.getComponent(extra.ordinal());
                    if (checkBox.isSelected()) {
                        selectedExtras.add(extra);
                    }

                }
            }
            // If no extras were selected, add "Skip"
            if (selectedExtras.isEmpty()) {
                selectedExtras.add(Extras.SKIP);
                JOptionPane.showMessageDialog(null, "You have skipped .", "Selected Options", JOptionPane.INFORMATION_MESSAGE);
            }

        }

        Coffee.Sugar sugar = (Coffee.Sugar) JOptionPane.showInputDialog(null, "Do you want to add Sugar ?.", appName, JOptionPane.QUESTION_MESSAGE, null, Coffee.Sugar.values(), Coffee.Sugar.NO);
        if (sugar == null) System.exit(0);

        // Get the number of shots
        int numShots;
        while (true) {
            String numShotsString = JOptionPane.showInputDialog(null, "Please enter the number of shots.", appName, JOptionPane.QUESTION_MESSAGE);
            if (numShotsString == null)
                System.exit(0); // Exit if user cancels
            try {
                numShots = Integer.parseInt(numShotsString);
                if (numShots <= 0) {
                    JOptionPane.showMessageDialog(null, "number of shots must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    break; // Exit loop if input is valid
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (numShots <= 0) {
            JOptionPane.showMessageDialog(null, "Invalid number of shots.", "Error", JOptionPane.ERROR_MESSAGE);
            return null; // Exit method if input is invalid
        }

        // for price
        int minPrice = -1, maxPrice = -1;
        while(minPrice==-1) {
            try {
                minPrice = Integer.parseInt(JOptionPane.showInputDialog(null,"Enter your minimum price for coffee. ",appName,JOptionPane.QUESTION_MESSAGE));
            }
            catch (NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Invalid. Please try again.");
            }
        }
        //max price cannot be less than min price
        while(maxPrice<minPrice) {
            try {
                maxPrice = Integer.parseInt(JOptionPane.showInputDialog(null,"Enter your maximum price for coffee.",appName,JOptionPane.QUESTION_MESSAGE));
            }
            catch (NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Invalid. Please try again.");
            }
            if(maxPrice<minPrice) JOptionPane.showMessageDialog(null,"Maximum Price must be greater Minimum Price.");
        }

        Coffee userCriteria = new Coffee(0, "",0,numShots, sugar, selectedMilkSet, selectedExtras, "" );
            userCriteria.setMinPrice(minPrice);
            userCriteria.setMaxPrice(maxPrice);
            return userCriteria;
        }


    // method to find a checkbox by its label
    private static JCheckBox getCheckBoxByName(JPanel panel, String name) {
        for (int i = 0; i < panel.getComponentCount(); i++) {
            if (panel.getComponent(i) instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) panel.getComponent(i);
                if (checkBox.getText().equals(name)) {
                    return checkBox;
                }
            }
        }
        return null;
    }




    // Method to load data from menu.txt and return an instance of the Menu class to save as a database
    private static Menu loadMenuData() {
        Menu menus = new Menu();
        Path path = Path.of(filePath);
        List<String> menuData = null;
        try {
            menuData = Files.readAllLines(path);
        } catch (IOException e) {
            System.err.println("Cannot the file");
            System.exit(0);
        }


       //getting menu data
        for (int i = 1; i < menuData.size(); i++) {
            String[] splitParts = menuData.get(i).split("\\[");
            String[] coffeeAttributes = splitParts[0].split(",", -1);

            int coffeeId = Integer.parseInt(coffeeAttributes[0].trim());
            String coffeeName = coffeeAttributes[1].trim();
            double price = Double.parseDouble(coffeeAttributes[2].trim());
            int numShots = Integer.parseInt(coffeeAttributes[3].trim());
            Coffee.Sugar sugar;
            if (coffeeAttributes[4].equalsIgnoreCase("yes")) {
                sugar = Coffee.Sugar.YES;
            } else {
                sugar = Coffee.Sugar.NO;
            }

            // Getting milk data
            String[] extractMilk = splitParts[1].replace("]", "").replace("\r", "").split(",\\s*");

            Set<Milk> milk = new HashSet<>();

            if(extractMilk.length == 0){
                milk.add(Milk.NONE);
            }else {

                for (String item : extractMilk) {
                    String milkOption = item.toUpperCase().replace("-", "_").strip();
                    // Mapping strings to the corresponding Milk enum value
                    Milk milkEnum = Milk.valueOf(milkOption);
                    // Add the Milk enum value to the set
                    milk.add(milkEnum);

                }
            }




            // Extracting extras data
            String[] extractExtra = splitParts[2].replace("]", "").replace("\r", "").split(",\\s*");
            Set<Extras> extras = new HashSet<>();

           if(extractExtra.length == 0){
               extras.add(Extras.SKIP);
           }else {

               for (String item : extractExtra) {
                   String extrasOption = item.toUpperCase().replace(" ", "").strip();
                   // Mapping strings to the corresponding Extras enum value
                   Extras extrasEnum = Extras.valueOf(extrasOption);
                   // Add the Extras enum value to the set
                   extras.add(extrasEnum);
               }
           }

            // Extracting description of the coffee
            String description = splitParts[3].replace("]","").replace("\r","");

            Coffee coffee = new Coffee(coffeeId, coffeeName, price, numShots, sugar, milk, extras, description );
            menus.addMenu(coffee);
        }
        return menus;
    }


    // displaying result of the matching coffee and asking user to select the coffee if they wish to order.
    private static void display(List<Coffee> potentialMatch, Coffee userCriteria ){
        StringBuilder message = new StringBuilder();
        String[] options = new String[potentialMatch.size()];
        for(int i=0;i<potentialMatch.size();i++){
            Coffee coffee = potentialMatch.get(i);
            String milkString = coffee.getMilk().toString();
            String extrasString = coffee.getExtras().toString();

            // Remove brackets
            milkString = milkString.substring(1, milkString.length() - 1);
            extrasString = extrasString.substring(1, extrasString.length() - 1);
            //  message per coffee
            message.append(coffee.getMenuName()).append(" (").append(coffee.getMenuId()).append(")\n")
                    .append(coffee.getDescription()).append("\n")
                    .append("Ingredients:\n")
                    .append("Number of shots: ").append(coffee.getNumberOfShots()).append("\n")
                    .append("Sugar: ").append(coffee.isSugar()).append("\n")
                    .append("Milk options: ").append(milkString).append("\n")
                    .append("Extra/s: ").append(extrasString).append("\n")
                    .append("Price: $").append(coffee.getPrice()).append("\n\n");

            //adding potential matchs to the list of options
            options[i]=potentialMatch.get(i).getMenuName();

        }
        String order = (String) JOptionPane.showInputDialog(null, message.toString() + "\n\nPlease select if you want to order coffee:",
                appName, JOptionPane.QUESTION_MESSAGE, null, options, "");
        // Checking if the user selected an option or skipped the order
        if (order != null && !order.isEmpty()) {
            // User selected an option
            Geek user = getUserInformation();

            // write in file
            Coffee selectedCoffee = null;
            for (Coffee coffee : potentialMatch) {
                if (coffee.getMenuName().equals(order)) {
                    selectedCoffee = coffee;
                    break;
                }
            }

            writeOrderToFile(user, selectedCoffee, userCriteria);
            JOptionPane.showMessageDialog(null, "Happy coding geek. have fun", "Order Confirm", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // User skipped the order
            JOptionPane.showMessageDialog(null, "Thank you for using our app. see you soon", "Order Skipped", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // getting user infomation and returning record of geek.
    private static Geek getUserInformation() {
        while (true) {
            String name = JOptionPane.showInputDialog(null, "Please enter your name:", appName, JOptionPane.QUESTION_MESSAGE);

            if (name == null || name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Name cannot be empty. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            if (!isValidFullName(name)) {
                JOptionPane.showMessageDialog(null, "Invalid name format. Please enter a valid name.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            while (true) {
                String phoneNumber = JOptionPane.showInputDialog(null, "Please enter your phone number:", appName, JOptionPane.QUESTION_MESSAGE);

                if (phoneNumber == null || phoneNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Phone number cannot be empty. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                if (!isValidPhoneNumber(phoneNumber)) {
                    JOptionPane.showMessageDialog(null, "Invalid phone number format. Please enter a valid phone number.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                return new Geek(name, phoneNumber);
            }
        }
    }



    // validating user name
    public static boolean isValidFullName(String fullName) {
        String regex = "^[A-Za-z]+(?: [A-Za-z]+)*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fullName);
        return matcher.matches();
    }



    // validating user's phone number
    public static boolean isValidPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("^\\d{10}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }


    // writing order to file
    private static void writeOrderToFile(Geek user, Coffee selectedCoffee, Coffee userCriteria) {
        try {
            String fileName = "order" + ".txt";
            FileWriter writer = new FileWriter(fileName, true);
            String milkString = userCriteria.getMilk().toString();
            milkString = milkString.substring(1, milkString.length() - 1);

            // Writing order details
            writer.write("Order details:\n");
            writer.write("Name: " + user.name() + "\n");
            writer.write("Order number: " + user.phoneNumber() + "\n");
            writer.write("Item: " + selectedCoffee.getMenuName() + " (" + selectedCoffee.getMenuId() + ")\n");
            writer.write("Milk: " + (milkString) + "\n\n");



            writer.close();
            System.out.println("Order details written to " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing order to file: " + e.getMessage());
        }
    }


}
