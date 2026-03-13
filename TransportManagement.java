import java.util.*;

public class TransportManagement {

	private Scanner input = new Scanner(System.in);

	public void manage(String regNumber){

		TMS transportSystem = new TMS();

		System.out.println("\n*******************************************************\n");
		System.out.println("\tTransport Management System\n");
		System.out.println("*******************************************************\n");

		System.out.println("1. Visit Transport Management System");
		System.out.println("2. Previous Menu");
		System.out.println("3. Exit");

		while(true){

			try{

				System.out.print("Enter your choice : ");
				int choice = input.nextInt();

				switch(choice){

					case 1:
						transportSystem.manage(regNumber);
						return;

					case 2:
						return;

					case 3:
						System.exit(0);

					default:
						System.out.println("Invalid choice! Try again.");
				}

			}

			catch(Exception e){
				System.out.println("Invalid input! Try again.");
				input.nextLine();
			}

		}
	}
}
