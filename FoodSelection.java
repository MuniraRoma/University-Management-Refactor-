import java.util.*;

// ===========================
// FOOD SELECTION REFACTORED
// ===========================

// Enum for food categories
enum FoodCategory {
    FAST_FOOD("Fast Food"), DESI_FOOD("Desi Food");

    private final String displayName;

    FoodCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static void printMenu() {
        System.out.println("\n===== FOOD CATEGORIES =====");
        int i = 1;
        for (FoodCategory category : values()) {
            System.out.println(i + ". " + category.getDisplayName());
            i++;
        }
        System.out.println("0. Go Back / Exit");
        System.out.println("==========================");
    }

    public static FoodCategory fromCode(int code) {
        if (code == 1) return FAST_FOOD;
        else if (code == 2) return DESI_FOOD;
        else return null;
    }
}

// Observer interface
interface FoodOrderObserver {
    void onFoodOrdered(int total, String category, List<String> billLines);
}

// Logging observer
class LoggingFoodObserver implements FoodOrderObserver {
    private int runningTotal = 0;

    @Override
    public void onFoodOrdered(int total, String category, List<String> billLines) {
        runningTotal += total;
        System.out.println("[LOG] " + category + " order: " + total + " TK (Running Total: " + runningTotal + " TK)");
    }
}

// Handler interface
interface FoodHandler {
    // Returns total amount and list of ordered lines
    FoodOrderResult handle(String regNumber);
}

// Wrapper class to hold total and bill lines
class FoodOrderResult {
    private final int total;
    private final List<String> billLines;

    public FoodOrderResult(int total, List<String> billLines) {
        this.total = total;
        this.billLines = billLines;
    }

    public int getTotal() { return total; }
    public List<String> getBillLines() { return billLines; }
}

// Fast Food Handler
class FastFoodHandler implements FoodHandler {
    @Override
    public FoodOrderResult handle(String regNumber) {
        MyFastFood fastFood = new MyFastFood();
        int total = fastFood.FastfoodBill();

        List<String> billLines = List.of("Fast Food total: " + total + " TK"); // placeholder for actual lines
        System.out.println("Fast Food Total: " + total + " TK");
        System.out.println("********************************************");

        return new FoodOrderResult(total, billLines);
    }
}

// Desi Food Handler
class DesiFoodHandler implements FoodHandler {
    @Override
    public FoodOrderResult handle(String regNumber) {
        MyDesiFood desiFood = new MyDesiFood();
        int total = desiFood.DesifoodBill();

        List<String> billLines = List.of("Desi Food total: " + total + " TK"); // placeholder
        System.out.println("Desi Food Total: " + total + " TK");
        System.out.println("********************************************");

        return new FoodOrderResult(total, billLines);
    }
}

// Factory for food handlers
class FoodHandlerFactory {
    private final Map<FoodCategory, FoodHandler> handlers = new HashMap<>();

    public FoodHandlerFactory() {
        handlers.put(FoodCategory.FAST_FOOD, new FastFoodHandler());
        handlers.put(FoodCategory.DESI_FOOD, new DesiFoodHandler());
    }

    public FoodHandler getHandler(FoodCategory category) {
        return handlers.get(category);
    }
}

// Food selection service
class FoodSelectionService {
    private final Scanner scanner;
    private final FoodHandlerFactory handlerFactory;
    private final List<FoodOrderObserver> observers = new ArrayList<>();

    public FoodSelectionService(Scanner scanner) {
        this.scanner = scanner;
        this.handlerFactory = new FoodHandlerFactory();
    }

    public void addObserver(FoodOrderObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(FoodOrderResult result, String categoryName) {
        for (FoodOrderObserver observer : observers) {
            observer.onFoodOrdered(result.getTotal(), categoryName, result.getBillLines());
        }
    }

    public int selectFood(String regNumber) {
        int grandTotal = 0;
        List<String> allBillLines = new ArrayList<>();

        while (true) {
            FoodCategory.printMenu();
            int choice = readIntInput("Enter Your Choice: ");

            if (choice == 0) break;

            FoodCategory category = FoodCategory.fromCode(choice);
            if (category == null) {
                System.out.println("Invalid option, try again.");
                continue;
            }

            FoodHandler handler = handlerFactory.getHandler(category);
            if (handler != null) {
                FoodOrderResult result = handler.handle(regNumber);
                grandTotal += result.getTotal();
                allBillLines.addAll(result.getBillLines());
                notifyObservers(result, category.getDisplayName());
            }
        }

        if (grandTotal > 0) {
            System.out.println("\n=== Food Section Summary ===");
            System.out.println("Total Food Bill: " + grandTotal + " TK");
            System.out.println("==============================");
        }

        return grandTotal;
    }

    private int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, enter a number.");
            }
        }
    }
}

// Main class
public class FoodSelection {
    private static final Scanner scanner = new Scanner(System.in);
    private static final FoodSelectionService service = new FoodSelectionService(scanner);

    static {
        service.addObserver(new LoggingFoodObserver());
    }

    public static int food(String regNumber) {
        return service.selectFood(regNumber);
    }
}