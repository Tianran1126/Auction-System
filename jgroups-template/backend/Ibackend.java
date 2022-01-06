package backend;

import client.Buyer;
import client.Client;
import client.Seller;

public interface Ibackend {
    public int createItem(String description, double reserve_price, double start_price, Seller seller);

    public String closeItem(int auctionID, Seller seller) ;

    public boolean emailSame(String email);

    //It saves the information of the clients on the server
    public String addPerson(String email , Client client);

    public String setItemBid(Buyer buyer, int auctionID, double bid_price)  ;

    public Client getPerson(String email) ;

    public  String listAllItems();

    public  int exit();

    public String CheckConnection();

}
