package client;
import api.API;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ManageSeller {

    // display options for seller
    public  void sellerOptions(API server, Seller seller) throws RemoteException {
        while (true) {
            int option = -1;
            System.out.println("--------------------");
            System.out.println("[1] Create an auction");
            System.out.println("[2] View all auction item(s)");
            System.out.println("[3] Cancel an auction");
            System.out.println("[4] Exit the program");
            System.out.println("--------------------");
            System.out.print("Choose an option: ");
            Scanner scanner = new Scanner(System.in);
            option = scanner.nextInt();
            System.out.println("--------------------");
            switch (option){
                case 1:
                    createAuction(server,seller);
                    break;
                case 2:
                    String s= server.listAllItem();
                    System.out.println(s);
                    break;
                case 3:
                    System.out.println(cancelAuction(server,seller));
                    break;
                case 4:
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("please choose from 1 to 3");
            }
        }
    }
    private  void createAuction(API server, Seller seller) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter item description:");
        String description = scanner.nextLine();
        System.out.println("Enter start_price ");
        double start_price = Double.parseDouble(scanner.nextLine());
        System.out.println("Enter reserve_price ");
        double reserve_price = Double.parseDouble(scanner.nextLine());
        if(start_price>=reserve_price){
            System.out.println("The start price can't be higher or equal to the reserve price");
            return ;
        }
        System.out.println("The ID is "+server.createAuctionItem(description,reserve_price,start_price,seller));
    }
    private String cancelAuction(API server, Seller seller) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter item ID:");
        int auctionID = scanner.nextInt();
        return  server.closeAuction(auctionID,seller);
    }
}
