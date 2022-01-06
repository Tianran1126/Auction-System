package client;
import api.API;
import java.rmi.RemoteException;
import java.util.Objects;
import java.util.Scanner;
public class Auth {

    public Auth(){

    }
    public  void login(API server) throws RemoteException {
        ManageSeller manageSeller=new ManageSeller();
        ManageBuyer manageBuyer=new ManageBuyer();
        Scanner scanner=new Scanner(System.in);
        System.out.println("Login Instruction");
        System.out.println("Enter account name");
        String accountName = scanner.nextLine();
        System.out.println("Enter account email");
        String accountEmail = scanner.nextLine();
        Client client=server.getClient(accountEmail);
        if(client==null){
            System.out.println("The account does not exist");
        }
        else if(!Objects.equals(accountName,client.getName())){
            System.out.println("The account name is not correct");
        }
        else  {
            if (client instanceof Seller) {
                manageSeller.sellerOptions(server, (Seller) client);
            } else if (client instanceof Buyer) {
                manageBuyer.buyerOptions(server, (Buyer) client);
            }
        }
    }
    public void sign_up(API server) throws RemoteException {
        ManageSeller manageSeller=new ManageSeller();
        ManageBuyer manageBuyer=new ManageBuyer();
        String accountEmail="";
        String accountName="";
        String option="";
        int time=0;
        do {
            if(time>0) {
                System.out.println("duplicated email or didn't enter s for seller, b for buyer ");
            }
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter account name");
            accountName = scanner.nextLine();
            System.out.println("Enter account email");
            accountEmail = scanner.nextLine();
            System.out.println("Enter s for seller, b for buyer");
            option = scanner.nextLine();
            time++;
        } while(server.emailDuplicate(accountEmail)|| (!Objects.equals(option, "s")&&!Objects.equals(option, "b")));

        if(option.equals("s")){
            Seller seller=new Seller(accountName,accountEmail);
            server.addClient(accountEmail, seller);
            manageSeller.sellerOptions(server,seller);
        }
        else if(option.equals("b")){
            Buyer buyer=new Buyer(accountName,accountEmail);
            server.addClient(accountEmail, buyer);
            manageBuyer.buyerOptions(server,buyer);
            }
        }
    }



