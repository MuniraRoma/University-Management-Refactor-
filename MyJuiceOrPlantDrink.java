import java.util.*;
import java.io.*;

public class MyJuiceOrPlantDrink {

    private static final Scanner scanner = new Scanner(System.in);
    private static final java.util.Date DATE = new java.util.Date();

    public MyJuiceOrPlantDrink() {
    }

    public void juiceOrPlantBill(String regNumber) {

    }

    private static class Constants {
        public static final String BILL_DIR = "cafe/CafeBills";
        public static final String BILL_FILE = BILL_DIR + "/BillJuiceOrPlant.txt";
        public static final String MENU_HEADER = "\n===== JUICE / PLANT DRINK MENU =====";
        public static final String MENU_FOOTER = "0. Nothing / Go Back\n===================================";
    }

    enum JuiceItem {
        MANGO("Mango Flavour", 50),
        ORANGE("Orange Flavour", 40),
        PINEAPPLE("Pineapple Flavour", 70),
        GRAPE("Grape Flavour", 50),
        MINERAL_WATER("Mineral Water", 30);

        final String displayName;
        final int price;

        JuiceItem(String name, int price) {
            this.displayName = name;
            this.price = price;
        }

        static JuiceItem fromChoice(int choice) {
            if (choice < 1 || choice > values().length) return null;
            return values()[choice - 1];
        }
    }

    static class JuiceOrder {
        final JuiceItem juice;
        final int quantity;

        JuiceOrder(JuiceItem juice, int quantity) {
            this.juice = juice;
            this.quantity = quantity;
        }

        int getTotal() {
            return quantity * juice.price;
        }

        String getBillLine() {
            return String.format("%-25s %3d    %6d", juice.displayName, quantity, getTotal());
        }
    }

    public int JuiceORPlantbill() {
        List<JuiceOrder> orders = new ArrayList<>();
        int total = 0;

        displayMenu();

        while (true) {
            int choice = readInt("Enter Your Choice (0 to finish): ");
            if (choice == 0) break;

            JuiceItem selected = JuiceItem.fromChoice(choice);
            if (selected == null) {
                System.out.println("Invalid choice!");
                continue;
            }

            int quantity = readInt("Enter quantity: ");
            if (quantity <= 0) {
                System.out.println("Quantity must be positive!");
                continue;
            }

            JuiceOrder order = new JuiceOrder(selected, quantity);
            orders.add(order);
            total += order.getTotal();

            System.out.printf("✓ Added: %s x %d = %d TK\n", selected.displayName, quantity, order.getTotal());
            System.out.println("Current total: " + total + " TK");
        }

        if (!orders.isEmpty()) {
            BillFileService.saveOrders(orders);
            System.out.println("\n✅ Juice/Plant Drinks Bill Generated. Total: " + total + " TK");
        }

        return total;
    }

    private void displayMenu() {
        System.out.println(Constants.MENU_HEADER);
        JuiceItem[] items = JuiceItem.values();
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
        static void saveOrders(List<JuiceOrder> orders) {
            try {
                new File(Constants.BILL_DIR).mkdirs();
                try (FileWriter writer = new FileWriter(Constants.BILL_FILE, true)) {
                    for (JuiceOrder order : orders) {
                        writer.write(order.getBillLine() + "\n");
                    }
                }
            } catch (IOException e) {
                System.out.println("Error saving: " + e.getMessage());
            }
        }
    }
}