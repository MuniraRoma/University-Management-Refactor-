import java.util.*;

// ====================================
// ENUM for Drink Categories
// ====================================
enum DrinkCategory {
    SOFT_DRINKS("Soft Drinks", 1),
    COFFEE("Coffee", 2),
    JUICE_PLANT("Juice/Plant Drinks", 3),
    NOTHING("Nothing / Go Back", 0);

    private final String displayName;
    private final int code;

    DrinkCategory(String displayName, int code) {
        this.displayName = displayName;
        this.code = code;
    }

    public String getDisplayName() { return displayName; }
    public int getCode() { return code; }

    public static Optional<DrinkCategory> fromCode(int code) {
        return Arrays.stream(values())
                .filter(c -> c.code == code)
                .findFirst();
    }
}

// ====================================
// Drink Handler Interface
// ====================================
interface DrinkHandler {
    int handle(String regNumber);  // returns bill total
    DrinkCategory getCategory();
}

// ====================================
// Concrete Drink Handlers
// ====================================
class SoftDrinksHandler implements DrinkHandler {
    private final Scanner scanner;
    public SoftDrinksHandler(Scanner scanner) { this.scanner = scanner; }

    @Override
    public int handle(String regNumber) {
        MySoftDrinks soft = new MySoftDrinks();
        int total = soft.SoftDrinkbill(regNumber);
        System.out.println("Soft Drinks Total: " + total + " TK");
        return total;
    }

    @Override
    public DrinkCategory getCategory() { return DrinkCategory.SOFT_DRINKS; }
}

class CoffeeHandler implements DrinkHandler {
    private final Scanner scanner;
    public CoffeeHandler(Scanner scanner) { this.scanner = scanner; }

    @Override
    public int handle(String regNumber) {
        MyCoffee coffee = new MyCoffee();
        int total = coffee.CoffeeBill(regNumber);
        System.out.println("Coffee Total: " + total + " TK");
        return total;
    }

    @Override
    public DrinkCategory getCategory() { return DrinkCategory.COFFEE; }
}

class JuicePlantHandler implements DrinkHandler {
    private final Scanner scanner;
    public JuicePlantHandler(Scanner scanner) { this.scanner = scanner; }

    @Override
    public int handle(String regNumber) {
        MyJuiceOrPlantDrink juice = new MyJuiceOrPlantDrink();
        int total = juice.JuiceORPlantbill();
        System.out.println("Juice/Plant Drinks Total: " + total + " TK");
        return total;
    }

    @Override
    public DrinkCategory getCategory() { return DrinkCategory.JUICE_PLANT; }
}

class NothingDrinkHandler implements DrinkHandler {
    @Override
    public int handle(String regNumber) {
        System.out.println("Exiting Drinks Menu...");
        return 0;
    }

    @Override
    public DrinkCategory getCategory() { return DrinkCategory.NOTHING; }
}

// ====================================
// Drink Handler Factory
// ====================================
class DrinkHandlerFactory {
    private final Map<Integer, DrinkHandler> handlers = new HashMap<>();

    public DrinkHandlerFactory(Scanner scanner) {
        handlers.put(1, new SoftDrinksHandler(scanner));
        handlers.put(2, new CoffeeHandler(scanner));
        handlers.put(3, new JuicePlantHandler(scanner));
        handlers.put(0, new NothingDrinkHandler());
    }

    public Optional<DrinkHandler> getHandler(int code) {
        return Optional.ofNullable(handlers.get(code));
    }
}

// ====================================
// Observer for Logging Orders
// ====================================
interface DrinkOrderObserver {
    void onDrinkOrdered(int total, String category);
}

class LoggingDrinkObserver implements DrinkOrderObserver {
    private int runningTotal = 0;
    @Override
    public void onDrinkOrdered(int total, String category) {
        runningTotal += total;
        System.out.println("[LOG] " + category + " order: " + total + " TK (Running Total: " + runningTotal + " TK)");
    }
}

// ====================================
// Drink Selection Service
// ====================================
class DrinkSelectionService {
    private final Scanner scanner;
    private final DrinkHandlerFactory factory;
    private final List<DrinkOrderObserver> observers = new ArrayList<>();
    private int grandTotal = 0;

    public DrinkSelectionService(Scanner scanner) {
        this.scanner = scanner;
        this.factory = new DrinkHandlerFactory(scanner);
    }

    public void addObserver(DrinkOrderObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(int total, String category) {
        for (DrinkOrderObserver obs : observers)
            obs.onDrinkOrdered(total, category);
    }

    public int selectDrink(String regNumber) {
        grandTotal = 0;
        while (true) {
            displayMenu();
            int choice = readChoice();

            if (choice == 0) break;

            Optional<DrinkHandler> handlerOpt = factory.getHandler(choice);
            if (handlerOpt.isPresent()) {
                DrinkHandler handler = handlerOpt.get();
                int total = handler.handle(regNumber);
                grandTotal += total;

                if (total > 0)
                    notifyObservers(total, handler.getCategory().getDisplayName());
            } else {
                System.out.println("Please select a valid option!");
            }
        }

        System.out.println("Total Drinks Bill: " + grandTotal + " TK");
        return grandTotal;
    }

    private void displayMenu() {
        System.out.println("\n===== DRINK CATEGORIES =====");
        System.out.println("1. Soft Drinks");
        System.out.println("2. Coffee");
        System.out.println("3. Juice/Plant Drinks");
        System.out.println("0. Nothing / Go Back");
        System.out.println("============================");
    }

    private int readChoice() {
        while (true) {
            try {
                System.out.print("Enter Your Choice: ");
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    public int getGrandTotal() { return grandTotal; }
}

// ====================================
// MAIN CLASS
// ====================================
public class DrinkSelection {
    private static final Scanner scanner = new Scanner(System.in);

    public static void myDrink(String regNumber) {
        DrinkSelectionService service = new DrinkSelectionService(scanner);
        service.addObserver(new LoggingDrinkObserver());
        service.selectDrink(regNumber);
    }

    public static void main(String[] args) {
        System.out.print("Enter Registration Number: ");
        String reg = scanner.nextLine().trim();
        myDrink(reg);
    }
}