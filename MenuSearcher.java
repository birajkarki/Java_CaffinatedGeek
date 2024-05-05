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

    // Main method
    public static void main(String[] args) {
        allMenus = loadMenuData(); // Loads all the text file data.
        ImageIcon icon = new ImageIcon(iconPath);
        // Prompt user to choose between coffee and tea
        int choice = JOptionPane.showOptionDialog(null,
                "Welcome to our app! Would you like to order:\n1) Coffee\n2) Tea",
                appName, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                icon, new String[] { "Coffee", "Tea" }, "Coffee");

        if (choice == 0) {
            // User selected Coffee
            Beverage userCriteria = getUserDreamCoffee();
            List<Beverage> potentialMatches = allMenus.findMatchCoffee(userCriteria);
            if (!potentialMatches.isEmpty()) {
                displayCoffeeResults(potentialMatches, userCriteria);
            } else {
                JOptionPane.showMessageDialog(null, "Ouch!! Our coffee menu cannot meet your expectations", appName,
                        JOptionPane.QUESTION_MESSAGE, icon);
                System.exit(0);
            }
        } else if (choice == 1) {
            // User selected Tea
            Tea userCriteria = getUserDreamTea();
            List<Tea> potentialMatches = allMenus.findMatchTea(userCriteria);
            if (!potentialMatches.isEmpty()) {
                displayTeaResults(potentialMatches, userCriteria);
            } else {
                JOptionPane.showMessageDialog(null, "Ouch!! Our tea menu cannot meet your expectations", appName,
                        JOptionPane.QUESTION_MESSAGE, icon);
                System.exit(0);
            }
        } else {
            // User closed the dialog
            System.exit(0);
        }
    }

    private static Beverage getUserDreamCoffee() {
        int numShots = 0;

        while (true) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            // Label indicating the purpose of the dropdown menu
            JLabel label1 = new JLabel("Select number of shots:");
            panel.add(label1);

            // Dropdown menu with predefined options
            String[] options = { "1", "2", "3" };
            JComboBox<String> comboBox = new JComboBox<>(options);
            comboBox.setSelectedIndex(0); // Default selection
            panel.add(comboBox);

            // Label indicating the purpose of the text field
            JLabel label2 = new JLabel("Or input custom number of shots:");
            panel.add(label2);

            // Text field for entering a custom value
            JTextField textField = new JTextField(10);
            panel.add(textField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Number of Shots", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            // If user cancels, exit the application
            if (result == JOptionPane.CANCEL_OPTION) {
                System.exit(0);
            }

            // If user clicks OK
            if (result == JOptionPane.OK_OPTION) {
                String selectedOption = (String) comboBox.getSelectedItem();
                String customInput = textField.getText().trim();

                if (!customInput.isEmpty()) {
                    try {
                        numShots = Integer.parseInt(customInput);
                        if (numShots <= 0) {
                            JOptionPane.showMessageDialog(null, "Number of shots must be greater than 0.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                } else {
                    numShots = Integer.parseInt(selectedOption);
                }

                if (numShots < 1 || numShots > 3) {
                    JOptionPane.showMessageDialog(null, "Number of shots must be 1, 2, or 3.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    break; // Exit loop if input is valid
                }
            }
        }

        // Get the milk
        Milk milk = (Milk) JOptionPane.showInputDialog(null, "Which milk would you prefer?.", appName,
                JOptionPane.QUESTION_MESSAGE, null, Milk.values(), Milk.SKIP);
        if (milk == null)
            System.exit(0);

        // Convert the selected milk into a Set<Milk>
        Set<Milk> selectedMilkSet = new HashSet<>();
        selectedMilkSet.add(milk);

        // Create a panel to hold checkboxes for extras
        JPanel panel = new JPanel(new GridLayout(0, 1));

        // Create checkboxes for each extra
        Set<Extras> selectedExtras = new HashSet<>();
        JCheckBox skipCheckBox = new JCheckBox("Skip");
        panel.add(skipCheckBox);

        for (Extras extra : Extras.values()) {
            if (extra != Extras.SKIP) {
                JCheckBox checkBox = new JCheckBox(extra.toString());
                panel.add(checkBox);
            }
        }

        // Show the dialog with checkboxes for extras
        int result = JOptionPane.showConfirmDialog(null, panel, "Choose your preferences, or skip",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // Handle user selection
        if (result == JOptionPane.OK_OPTION) {
            // Loop through checkboxes to determine which extras were selected
            Component[] components = panel.getComponents();
            for (int i = 0; i < components.length; i++) {
                if (components[i] instanceof JCheckBox) {
                    JCheckBox checkBox = (JCheckBox) components[i];
                    if (checkBox.isSelected() && i != 0) { // Exclude the first checkbox (Skip)
                        selectedExtras.add(Extras.values()[i - 1]);
                    }
                }
            }

            // If only the "Skip" checkbox is selected, add "Skip"
            if (selectedExtras.isEmpty() && skipCheckBox.isSelected()) {
                selectedExtras.add(Extras.SKIP);
                JOptionPane.showMessageDialog(null, "You have skipped.", "Selected Options",
                        JOptionPane.INFORMATION_MESSAGE);
            } else if (selectedExtras.isEmpty()) {
                // If no extras were selected and "Skip" checkbox is not selected, show an error
                // message
                JOptionPane.showMessageDialog(null, "Please select at least one extra or skip.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                // Show selected extras
                StringBuilder message = new StringBuilder("Selected extras:\n");
                for (Extras extra : selectedExtras) {
                    message.append(extra).append("\n");
                }
                JOptionPane.showMessageDialog(null, message.toString(), "Selected Options",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }

        // Now, you have the selected extras in the `selectedExtras` set.

        Beverage.Sugar sugar = (Beverage.Sugar) JOptionPane.showInputDialog(null, "Do you want to add Sugar ?.",
                appName,
                JOptionPane.QUESTION_MESSAGE, null, Beverage.Sugar.values(), Beverage.Sugar.NO);
        if (sugar == null)
            System.exit(0);

        // for price
        int minPrice = -1, maxPrice = -1;
        while (minPrice == -1) {
            try {
                minPrice = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter your minimum price for coffee. ",
                        appName, JOptionPane.QUESTION_MESSAGE));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid. Please try again.");
            }
        }
        // max price cannot be less than min price
        while (maxPrice < minPrice) {
            try {
                maxPrice = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter your maximum price for coffee.",
                        appName, JOptionPane.QUESTION_MESSAGE));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid. Please try again.");
            }
            if (maxPrice < minPrice)
                JOptionPane.showMessageDialog(null, "Maximum Price must be greater Minimum Price.");
        }

        Beverage userCriteria = new Beverage("type", 0, "", 0, numShots, sugar, selectedMilkSet, selectedExtras, "");
        userCriteria.setMinPrice(minPrice);
        userCriteria.setMaxPrice(maxPrice);
        return userCriteria;
    }

    private static Tea getUserDreamTea() {
        int temperature = 0;
        int steepingTime = 0;

        String[] options = {
                "80 degrees: For a mellow, gentler taste",
                "85 degrees: For slightly sharper than mellow",
                "90 degrees: Balanced, strong but not too strong",
                "95 degrees: Strong, but not acidic",
                "100 degrees: For a bold, strong flavour",
                "Skip"
        };

        // Prompt user to select preferred temperature
        String tempInput = (String) JOptionPane.showInputDialog(null, "Select preferred temperature:",
                "Tea Temperature", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        // Handle user input
        if (tempInput != null && !tempInput.equals("Skip")) {
            temperature = Integer.parseInt(tempInput.split(" ")[0]);
            String description = tempInput.split(":")[1].trim();

            // Display selected temperature and description
            JOptionPane.showMessageDialog(null, "You selected " + temperature + " degrees Celsius. " + description,
                    "Tea Temperature", JOptionPane.INFORMATION_MESSAGE);
        } else if (tempInput == null) {
            JOptionPane.showMessageDialog(null, "Temperature selection cancelled.", "Tea Temperature",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Temperature selection skipped.", "Tea Temperature",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        // Asking user for preferred steeping time
        String steepTimeInput = JOptionPane.showInputDialog(null,
                "Enter preferred steeping time (in minutes) or 'I don't mind':",
                appName, JOptionPane.QUESTION_MESSAGE);

        try {
            if (steepTimeInput == null || steepTimeInput.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No steeping time entered. Defaulting to 5 minutes.", appName,
                        JOptionPane.INFORMATION_MESSAGE);
                steepingTime = 5; // Default to 5 minutes
            } else if (steepTimeInput.equalsIgnoreCase("I don't mind")) {
                JOptionPane.showMessageDialog(null, "Steeping time: I don't mind.", appName,
                        JOptionPane.INFORMATION_MESSAGE);
                // Handle "I don't mind" case according to your application's logic
                // For example, you might choose to set a default steeping time or proceed
                // without setting a specific time.
            } else {
                steepingTime = Integer.parseInt(steepTimeInput);
                if (steepingTime <= 0) {
                    JOptionPane.showMessageDialog(null, "Invalid steeping time. Please enter a positive number.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                JOptionPane.showMessageDialog(null, "Preferred steeping time: " + steepingTime + " minutes.", appName,
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Invalid input for steeping time. Please enter a valid number or 'I don't mind'.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        // Getting the sugar preference
        Tea.Sugar sugar = (Tea.Sugar) JOptionPane.showInputDialog(null, "Do you want to add Sugar?", appName,
                JOptionPane.QUESTION_MESSAGE, null, Tea.Sugar.values(), Tea.Sugar.NO);
        if (sugar == null)
            System.exit(0);

        // Getting the milk preference
        Milk milk = (Milk) JOptionPane.showInputDialog(null, "Which milk would you prefer?", appName,
                JOptionPane.QUESTION_MESSAGE, null, Milk.values(), Milk.SKIP);
        if (milk == null)
            System.exit(0);

        // Converting the selected milk into a Set<Milk>
        Set<Milk> selectedMilkSet = new HashSet<>();
        selectedMilkSet.add(milk);

        // Creating a panel to hold checkboxes for extras
        JPanel panel = new JPanel(new GridLayout(0, 1));

        // Create checkboxes for each extra
        Set<Extras> selectedExtras = new HashSet<>();
        JCheckBox skipCheckBox = new JCheckBox("Skip");
        panel.add(skipCheckBox);

        for (Extras extra : Extras.values()) {
            if (extra != Extras.SKIP) {
                JCheckBox checkBox = new JCheckBox(extra.toString());
                panel.add(checkBox);
            }
        }

        // Show the dialog with checkboxes for extras
        int result = JOptionPane.showConfirmDialog(null, panel, "Choose your preferences, or skip",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // Handle user selection
        if (result == JOptionPane.OK_OPTION) {
            // Loop through checkboxes to determine which extras were selected
            Component[] components = panel.getComponents();
            for (int i = 0; i < components.length; i++) {
                if (components[i] instanceof JCheckBox) {
                    JCheckBox checkBox = (JCheckBox) components[i];
                    if (checkBox.isSelected() && i != 0) { // Exclude the first checkbox (Skip)
                        selectedExtras.add(Extras.values()[i - 1]);
                    }
                }
            }

            // If only the "Skip" checkbox is selected, add "Skip"
            if (selectedExtras.isEmpty() && skipCheckBox.isSelected()) {
                selectedExtras.add(Extras.SKIP);
                JOptionPane.showMessageDialog(null, "You have skipped.", "Selected Options",
                        JOptionPane.INFORMATION_MESSAGE);
            } else if (selectedExtras.isEmpty()) {
                // If no extras were selected and "Skip" checkbox is not selected, show an error
                // message
                JOptionPane.showMessageDialog(null, "Please select at least one extra or skip.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                // Show selected extras
                StringBuilder message = new StringBuilder("Selected extras:\n");
                for (Extras extra : selectedExtras) {
                    message.append(extra).append("\n");
                }
                JOptionPane.showMessageDialog(null, message.toString(), "Selected Options",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
        // for price
        int minPrice = -1, maxPrice = -1;
        while (minPrice == -1) {
            try {
                minPrice = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter your minimum price for tea. ",
                        appName, JOptionPane.QUESTION_MESSAGE));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid. Please try again.");
            }
        }
        // max price cannot be less than min price
        while (maxPrice < minPrice) {
            try {
                maxPrice = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter your maximum price for tea.",
                        appName, JOptionPane.QUESTION_MESSAGE));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid. Please try again.");
            }
            if (maxPrice < minPrice)
                JOptionPane.showMessageDialog(null, "Maximum Price must be greater Minimum Price.");
        }

        Tea userCriteria = new Tea("type", 0, "", 0, temperature, steepingTime, sugar, selectedMilkSet, selectedExtras);
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

    // Method to load data from menu.txt and return an instance of the Menu class to
    // save as a database
    private static Menu loadMenuData() {
        Menu menus = new Menu();
        Path path = Path.of(filePath);
        List<String> menuData = null;
        try {
            menuData = Files.readAllLines(path);
        } catch (IOException e) {
            System.err.println("Cannot read the file");
            System.exit(0);
        }
    
        // Iterate over each line of the menu data
        for (String line : menuData) {
            String[] splitParts = line.split("\\[");
            if (splitParts.length < 2) {
                // Skip invalid lines
                continue;
            }
    
            // Extract coffee attributes
            String[] coffeeAttributes = splitParts[0].split(",", -1);
    
            if (coffeeAttributes.length < 5) {
                // Skip invalid lines
                continue;
            }
    
            int coffeeId;
            try {
                coffeeId = Integer.parseInt(coffeeAttributes[0].trim());
            } catch (NumberFormatException e) {
                // Skip invalid lines
                System.err.println("Invalid coffee ID: " + coffeeAttributes[0].trim());
                continue;
            }
    
            String coffeeName = coffeeAttributes[1].trim();
            double price;
            try {
                price = Double.parseDouble(coffeeAttributes[2].trim());
            } catch (NumberFormatException e) {
                // Skip invalid lines
                System.err.println("Invalid price: " + coffeeAttributes[2].trim());
                continue;
            }
            
            int numShots;
            try {
                numShots = Integer.parseInt(coffeeAttributes[3].trim());
            } catch (NumberFormatException e) {
                // Skip invalid lines
                System.err.println("Invalid number of shots: " + coffeeAttributes[3].trim());
                continue;
            }
            
            Beverage.Sugar sugar = coffeeAttributes[4].equalsIgnoreCase("yes") ? Beverage.Sugar.YES : Beverage.Sugar.NO;
    
            // Extract milk data
            String[] extractMilk = splitParts[1].replace("]", "").replace("\r", "").split(",\\s*");
            Set<Milk> milk = new HashSet<>();
            for (String item : extractMilk) {
                try {
                    String milkOption = item.toUpperCase().replace("-", "_").strip();
                    Milk milkEnum = Milk.valueOf(milkOption);
                    milk.add(milkEnum);
                } catch (IllegalArgumentException ignored) {
                    // Handle invalid milk options gracefully
                    System.err.println("Invalid milk option: " + item);
                }
            }
    
            // Extract extras data
            String[] extractExtra = splitParts[2].replace("]", "").replace("\r", "").split(",\\s*");
            Set<Extras> extras = new HashSet<>();
            for (String item : extractExtra) {
                try {
                    String extrasOption = item.toUpperCase().replace(" ", "").strip();
                    Extras extrasEnum = Extras.valueOf(extrasOption);
                    extras.add(extrasEnum);
                } catch (IllegalArgumentException ignored) {
                    // Handle invalid extras options gracefully
                    System.err.println("Invalid extras option: " + item);
                }
            }
    
            // Extract description of the coffee
            String description = splitParts[3].replace("]", "").replace("\r", "");
    
            Beverage coffee = new Beverage("type", coffeeId, coffeeName, price, numShots, sugar, milk, extras, description);
            menus.addCoffeeMenu(coffee);
        }
        return menus;
    }
    
    // if they wish to order.
    private static void displayCoffeeResults(List<Beverage> potentialMatch, Beverage userCriteria) {
        StringBuilder message = new StringBuilder();
        String[] options = new String[potentialMatch.size()];
        for (int i = 0; i < potentialMatch.size(); i++) {
            Beverage coffee = potentialMatch.get(i);
            String milkString = coffee.getMilk().toString();
            String extrasString = coffee.getExtras().toString();

            // Remove brackets
            milkString = milkString.substring(1, milkString.length() - 1);
            extrasString = extrasString.substring(1, extrasString.length() - 1);
            // message per coffee
            message.append(coffee.getMenuName()).append(" (").append(coffee.getMenuId()).append(")\n")
                    .append(coffee.getDescription()).append("\n")
                    .append("Ingredients:\n")
                    .append("Number of shots: ").append(coffee.getNumberOfShots()).append("\n")
                    .append("Sugar: ").append(coffee.isSugar()).append("\n")
                    .append("Milk options: ").append(milkString).append("\n")
                    .append("Extra/s: ").append(extrasString).append("\n");

            // adding potential matchs to the list of options
            options[i] = potentialMatch.get(i).getMenuName();

        }
        String order = (String) JOptionPane.showInputDialog(null,
                message.toString() + "\n\nPlease select if you want to order coffee:",
                appName, JOptionPane.QUESTION_MESSAGE, null, options, "");
        // Checking if the user selected an option or skipped the order
        if (order != null && !order.isEmpty()) {
            // User selected an option
            Geek user = getUserInformation();

            // write in file
            Beverage selectedCoffee = null;
            for (Beverage coffee : potentialMatch) {
                if (coffee.getMenuName().equals(order)) {
                    selectedCoffee = coffee;
                    break;
                }
            }

            writeOrderToFile(user, selectedCoffee, userCriteria);
            JOptionPane.showMessageDialog(null, "Happy coding geek. have fun", "Order Confirm",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            // User skipped the order
            JOptionPane.showMessageDialog(null, "Thank you for using our app. see you soon", "Order Skipped",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void displayTeaResults(List<Tea> potentialMatch, Tea userCriteria) {
        StringBuilder message = new StringBuilder();
        String[] options = new String[potentialMatch.size()];
        for (int i = 0; i < potentialMatch.size(); i++) {
            Tea tea = potentialMatch.get(i);
            String milkString = tea.getMilk().toString();
            String extrasString = tea.getExtras().toString();

            // Remove brackets
            milkString = milkString.substring(1, milkString.length() - 1);
            extrasString = extrasString.substring(1, extrasString.length() - 1);

            // Message per tea
            message.append(tea.getMenuName()).append(" (").append(tea.getMenuId()).append(")\n")
                    // .append(tea.getDescription()).append("\n")
                    .append("Ingredients:\n")
                    .append("Temperature: ").append(tea.getTemperature()).append("°C\n")
                    .append("Steeping Time: ").append(tea.getSteepingTime()).append(" minutes\n")
                    .append("Sugar: ").append(tea.getSugar()).append("\n")
                    .append("Milk options: ").append(milkString).append("\n")
                    .append("Extra/s: ").append(extrasString).append("\n");

            // Adding potential matches to the list of options
            options[i] = potentialMatch.get(i).getMenuName();

        }
        String order = (String) JOptionPane.showInputDialog(null,
                message.toString() + "\n\nPlease select if you want to order tea:",
                appName, JOptionPane.QUESTION_MESSAGE, null, options, "");
        // Checking if the user selected an option or skipped the order
        if (order != null && !order.isEmpty()) {
            // User selected an option
            Geek user = getUserInformation();

            // Write to file
            Tea selectedTea = null;
            for (Tea tea : potentialMatch) {
                if (tea.getMenuName().equals(order)) {
                    selectedTea = tea;
                    break;
                }
            }

            // Now you have the selected tea and its user criteria, you can write the order
            // to file
            writeOrderToFile(user, selectedTea, userCriteria);
            JOptionPane.showMessageDialog(null, "Enjoy your tea! Have a great day!", "Order Confirmation",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            // User skipped the order
            JOptionPane.showMessageDialog(null, "Thank you for considering our tea menu. Have a great day!",
                    "Order Skipped", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // getting user infomation and returning record of geek.
    private static Geek getUserInformation() {
        while (true) {
            String name = JOptionPane.showInputDialog(null, "Please enter your name:", appName,
                    JOptionPane.QUESTION_MESSAGE);

            if (name == null || name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Name cannot be empty. Please try again.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                continue;
            }

            if (!isValidFullName(name)) {
                JOptionPane.showMessageDialog(null, "Invalid name format. Please enter a valid name.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                continue;
            }

            while (true) {
                String phoneNumber = JOptionPane.showInputDialog(null, "Please enter your phone number:", appName,
                        JOptionPane.QUESTION_MESSAGE);

                if (phoneNumber == null || phoneNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Phone number cannot be empty. Please try again.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                if (!isValidPhoneNumber(phoneNumber)) {
                    JOptionPane.showMessageDialog(null,
                            "Invalid phone number format. Please enter a valid phone number.", "Error",
                            JOptionPane.ERROR_MESSAGE);
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
    private static void writeOrderToFile(Geek user, Beverage selectedCoffee, Beverage userCriteria) {
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

    private static void writeOrderToFile(Geek user, Tea selectedTea, Tea userCriteria) {
        try {
            String fileName = "order" + ".txt";
            FileWriter writer = new FileWriter(fileName, true);
            String milkString = userCriteria.getMilk().toString();
            milkString = milkString.substring(1, milkString.length() - 1);

            // Writing order details
            writer.write("Order details:\n");
            writer.write("Name: " + user.name() + "\n");
            writer.write("Order number: " + user.phoneNumber() + "\n");
            writer.write("Item: " + selectedTea.getMenuName() + " (" + selectedTea.getMenuId() + ")\n");
            writer.write("Milk: " + (milkString) + "\n\n");

            writer.close();
            System.out.println("Order details written to " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing order to file: " + e.getMessage());
        }
    }

}