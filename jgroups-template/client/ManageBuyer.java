package client;


import api.API;

import java.rmi.RemoteException;
import java.util.Scanner;

public class ManageBuyer {
   // display options for buyer
    public void buyerOptions(API server, Buyer buyer) throws RemoteException {
        while (true) {
            int option = -1;
            System.out.println("--------------------");
            System.out.println("[1] Bid for an item");
            System.out.println("[2] View all auction item(s)");
            System.out.println("[3] Exit the program");
            System.out.println("--------------------");
            System.out.print("Choose an option: ");

            Scanner scanner = new Scanner(System.in);
            option = scanner.nextInt();

            System.out.println("--------------------");

            switch (option){
                case 1:
                    bidItem(server,buyer);
                    break;
                case 2:
                    System.out.println(server.listAllItem());
                    break;
                case 3:
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("please choose from 1 to 3");
            }
        }
    }

    public static void  bidItem(API server, Buyer buyer) throws RemoteException, RemoteException {
        Scanner scanner=new Scanner(System.in);
        System.out.println("Enter Id for the item:");
        String auctionID = scanner.nextLine();
        System.out.println("Enter bid_price");
        double bid_price =Double.parseDouble(scanner.nextLine());
        System.out.println(server.setBid(buyer,Integer.parseInt(auctionID),bid_price));
    }
}
