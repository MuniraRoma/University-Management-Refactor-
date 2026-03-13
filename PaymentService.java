import java.awt.Desktop;
import java.net.URI;

public class PaymentService {

    private static final int JAZZCASH = 1;
    private static final int HBL = 2;
    private static final int ALFALAH = 3;

    private static final String JAZZCASH_URL = "https://www.jazzcash.com.pk";
    private static final String HBL_URL = "https://www.hblibank.com.pk";
    private static final String ALFALAH_URL = "https://netbanking.bankalfalah.com/";

    public void pay(int option) {

        try {

            Desktop desk = Desktop.getDesktop();

            switch (option) {

                case JAZZCASH -> desk.browse(new URI(JAZZCASH_URL));

                case HBL -> desk.browse(new URI(HBL_URL));

                case ALFALAH -> desk.browse(new URI(ALFALAH_URL));

                default -> {
                    System.out.println("==========Invalid option==========\n");
                    return;
                }
            }

            System.out.println("==========Payment Done!==========\n");

        } catch (Exception e) {
            System.out.println("==========Payment Error!==========\n");
        }
    }
}