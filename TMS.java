import java.util.Scanner;

public class TMS {

    private RouteService routeService = new RouteService();
    private FileService fileService = new FileService();
    private PaymentService paymentService = new PaymentService();
    private BusService busService = new BusService(routeService);

    private Scanner input = new Scanner(System.in);
    private boolean feePaid = false;

    private static final int ROUTES = 1;
    private static final int FEES = 2;
    private static final int REGISTER = 3;
    private static final int UPDATE = 4;
    private static final int PROFILE = 5;
    private static final int PAY_FEE = 6;
    private static final int DELETE = 7;
    private static final int EXIT = 8;

    public void manage(String regNo) {

        while (true) {

            displayMenu();
            int choice = input.nextInt();

            switch (choice) {

                case ROUTES -> routeService.displayRoutes();

                case FEES -> routeService.displayFees();

                case REGISTER -> registerBus(regNo);

                case UPDATE -> updateBus(regNo);

                case PROFILE -> viewProfile(regNo);

                case PAY_FEE -> payFee();

                case DELETE -> fileService.delete(regNo);

                case EXIT -> { return; }

                default -> System.out.println("Invalid Option!");
            }
        }
    }

    private void displayMenu() {
        System.out.println("""
                
                1. Routes
                2. Fees
                3. Register
                4. Update Info
                5. Profile
                6. Pay Fee
                7. Delete
                8. Exit
                
                """);
    }

    private void registerBus(String regNo) {
        Bus bus = busService.register(regNo);
        fileService.save(bus);
        System.out.println("=====Registered Successfully!=====");
    }

    private void updateBus(String regNo) {
        Bus bus = busService.update(regNo);
        fileService.save(bus);
        System.out.println("=====Updated Successfully!=====");
    }

    private void viewProfile(String regNo) {
        if (feePaid)
            fileService.read(regNo);
        else
            System.out.println("=====Pay fee first!=====");
    }

    private void payFee() {

        System.out.println("""
                1. JazzCash
                2. HBL
                3. Alfalah
                """);

        int option = input.nextInt();
        paymentService.pay(option);

        feePaid = true;
    }
}
