import java.util.*;
import java.io.*;

public class MySoftDrinks {

    private static final Scanner scanner = new Scanner(System.in);
    private static final java.util.Date DATE = new java.util.Date();

    public MySoftDrinks() {
    }

    public void softDrinksBill(String regNumber) {

    }

    private static class Constants {
        public static final String BILL_DIR = "cafe/CafeBills";
        public static final String BILL_FILE = BILL_DIR + "/BillSoftDrinks.txt";
        public static final String MENU_HEADER = "\n===== SOFT DRINKS MENU =====";
        public static final String MENU_FOOTER = "0. Nothing / Go Back\n============================";
    }

    enum SoftDrinkItem {
        COCA_COLA("Coca-Cola", 60),
        PEPSI("Pepsi", 60),
        FANTA("Fanta", 60),
        DEW("Dew", 60),
        STING("Sting", 70),
        SPRITE("Sprite", 60);

        final String displayName;
        final int price;

        SoftDrinkItem(String name, int price) {
            this.displayName = name;
            this.price = price;
        }

        static SoftDrinkItem fromChoice(int choice) {
            if (choice < 1 || choice > values().length) return null;
            return values()[choice - 1];
        }
    }

    static class SoftDrinkOrder {
        final SoftDrinkItem softDrink;
        final int quantity;

        SoftDrinkOrder(SoftDrinkItem softDrink, int quantity) {
            this.softDrink = softDrink;
            this.quantity = quantity;
        }

        int getTotal() {
            return quantity * softDrink.price;
        }

        String getBillLine() {
            return String.format("%-25s %3d    %6d", softDrink.displayName, quantity, getTotal());
        }
    }

    public int SoftDrinkbill(String regNumber) {
        List<SoftDrinkOrder> orders = new ArrayList<>();
        int total = 0;

        displayMenu();

        while (true) {
            int choice = readInt("Enter Your Choice (0 to finish): ");
            if (choice == 0) break;

            SoftDrinkItem selected = SoftDrinkItem.fromChoice(choice);
            if (selected == null) {
                System.out.println("Invalid choice!");
                continue;
            }

            int quantity = readInt("Enter quantity: ");
            if (quantity <= 0) {
                System.out.println("Quantity must be positive!");
                continue;
            }

            SoftDrinkOrder order = new SoftDrinkOrder(selected, quantity);
            orders.add(order);
            total += order.getTotal();

            System.out.printf("✓ Added: %s x %d = %d TK\n", selected.displayName, quantity, order.getTotal());
            System.out.println("Current total: " + total + " TK");
        }

        if (!orders.isEmpty()) {
            BillFileService.saveOrders(orders);
            System.out.println("\n✅ Soft Drinks Bill Generated. Total: " + total + " TK");
        }

        return total;
    }

    private void displayMenu() {
        System.out.println(Constants.MENU_HEADER);
        SoftDrinkItem[] items = SoftDrinkItem.values();
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
        static void saveOrders(List<SoftDrinkOrder> orders) {
            try {
                new File(Constants.BILL_DIR).mkdirs();
                try (FileWriter writer = new FileWriter(Constants.BILL_FILE, true)) {
                    for (SoftDrinkOrder order : orders) {
                        writer.write(order.getBillLine() + "\n");
                    }
                }
            } catch (IOException e) {
                System.out.println("Error saving: " + e.getMessage());
            }
        }
    }
}