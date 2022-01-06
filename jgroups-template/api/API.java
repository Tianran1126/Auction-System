package api;

import backend.AuctionItem;
import client.Buyer;
import client.Client;
import client.Seller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

public interface API extends Remote {

  public String listAllItem() throws RemoteException;
  public int createAuctionItem(String description, double reserve_price, double start_price, Seller seller) throws RemoteException;
  public String setBid(Buyer buyer, int auctionID, double bid_price) throws RemoteException;

  public String closeAuction(int auctionID, Seller seller) throws RemoteException;

  public boolean emailDuplicate(String email) throws RemoteException;

  public String addClient(String email , Client client) throws RemoteException;

  public Client getClient(String email) throws RemoteException;

  public boolean connect() throws RemoteException;


}
