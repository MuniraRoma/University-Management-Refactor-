import java.util.*;
import java.io.*;

public class MyFastFood {

    private static final Scanner scanner = new Scanner(System.in);
    private static final java.util.Date DATE = new java.util.Date();

    public void fastFoodBill() {
    }

    private static class Constants {
        public static final String BILL_DIR = "cafe/CafeBills";
        public static final String BILL_FILE = BILL_DIR + "/BillFastfood.txt";
        public static final String MENU_HEADER = "\n===== FAST FOOD MENU =====";
        public static final String MENU_FOOTER = "0. Nothing / Go Back\n==========================";
    }

    enum FastFoodItem {
        BURGER("Burger", 80),
        ZINGER_BURGER("Zinger Burger", 250),
        SHAWARMA("Shawarma", 120),
        PIZZA("Pizza", 350),
        SANDWICH("Sandwich", 70),
        FRIES("Fries", 50);

        final String displayName;
        final int price;

        FastFoodItem(String name, int price) {
            this.displayName = name;
            this.price = price;
        }

        static FastFoodItem fromChoice(int choice) {
            if (choice < 1 || choice > values().length) return null;
            return values()[choice - 1];
        }
    }

    static class OrderedItem {
        final FastFoodItem item;
        final int quantity;

        OrderedItem(FastFoodItem item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }

        int getTotalPrice() { return quantity * item.price; }

        String getBillLine() {
            return String.format("%-25s %3d    %6d", item.displayName, quantity, getTotalPrice());
        }
    }

    public int FastfoodBill() {
        List<OrderedItem> orders = new ArrayList<>();
        int sessionTotal = 0;

        displayMenu();

        while (true) {
            int choice = readInt("Enter Your Choice (0 to finish): ");
            if (choice == 0) break;

            FastFoodItem selected = FastFoodItem.fromChoice(choice);
            if (selected == null) {
                System.out.println("Invalid choice!");
                continue;
            }

            int quantity = readInt("Enter quantity: ");
            if (quantity <= 0) {
                System.out.println("Quantity must be positive!");
                continue;
            }

            OrderedItem order = new OrderedItem(selected, quantity);
            orders.add(order);
            sessionTotal += order.getTotalPrice();

            System.out.printf("✓ Added: %s x %d = %d TK\n", selected.displayName, quantity, order.getTotalPrice());
            System.out.println("Current total: " + sessionTotal + " TK");
        }

        if (!orders.isEmpty()) {
            BillFileService.saveOrders(orders);
            System.out.println("\n✅ Fast Food Bill Generated. Total: " + sessionTotal + " TK");
        }

        return sessionTotal;
    }

    private void displayMenu() {
        System.out.println(Constants.MENU_HEADER);
        FastFoodItem[] items = FastFoodItem.values();
        for (int i = 0; i < items.length; i++) {
            System.out.printf("%d. %s (%d Rs)\n", i + 1, items[i].displayName, items[i].price);
        }
        System.out.println(Constants.MENU_FOOTER);
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input!");
            }
        }
    }

    // ==========================
    // FILE SERVICE
    // ==========================
    private static class BillFileService {
        static void saveOrders(List<OrderedItem> orders) {
            try {
                new File(Constants.BILL_DIR).mkdirs();
                try (FileWriter writer = new FileWriter(Constants.BILL_FILE, true)) {
                    for (OrderedItem order : orders) {
                        writer.write(order.getBillLine() + "\n");
                    }
                }
            } catch (IOException e) {
                System.out.println("Error saving: " + e.getMessage());
            }
        }
    }
}