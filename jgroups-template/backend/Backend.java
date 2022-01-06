package backend;

import client.Buyer;
import client.Client;
import client.Seller;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.blocks.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.jgroups.util.RspList;
import utility.GroupUtils;

public class Backend implements Ibackend {

  private JChannel groupChannel;
  private RpcDispatcher dispatcher;
  private ConcurrentHashMap<Integer, AuctionItem> auctionMap;
  private ConcurrentHashMap  <String, Client> clientMap;
  public Backend() throws Exception {
    // Connect to the group (channel)
    this.groupChannel = GroupUtils.connect();
    if (this.groupChannel == null) {
      System.exit(1); // error to be printed by the 'connect' function
    }
    // Make this instance of Backend a dispatcher in the channel (group)
    this.dispatcher = new RpcDispatcher(this.groupChannel, this);

    RspList<ConcurrentHashMap<Integer,AuctionItem>> auctionMap_list = dispatcher.callRemoteMethods(null,
            "getAuctionMaps",

            null,

            null,

            new RequestOptions(ResponseMode.GET_ALL, 1000));

    RspList<ConcurrentHashMap<String,Client>> clientMap_list = dispatcher.callRemoteMethods(null,
            "getClientMaps",

            null,

            null,

            new RequestOptions(ResponseMode.GET_ALL, 1000));

      auctionMap=getAuctionMap(auctionMap_list);
      clientMap=getClientMap(clientMap_list);
  }


  public synchronized String listAllItems() {
    String result="";
    if (auctionMap.isEmpty()) {
      result= "There are currently no auction items in the system.";
    }
    for (Map.Entry<Integer, AuctionItem> entry : auctionMap.entrySet()) {
      result=result+entry.getValue().toString()+"\n";
    }
    return result;
  }
  public synchronized int exit(){
    System.exit(0);
    return 0;
  }

  public synchronized int createItem(String description, double reserve_price, double start_price, Seller seller){
    int i=0;
    while(auctionMap.containsKey(i)){
      i++;
    }
    AuctionItem auctionItem = new AuctionItem(reserve_price, description, start_price,seller);
    auctionItem.setID(i);
    auctionMap.put(i, auctionItem);
    return auctionItem.getID();
  }
  public synchronized String closeItem(int auctionID, Seller seller) {
    AuctionItem item=auctionMap.get(auctionID);
    if(item==null){
      return "item doesn't not exist";
    }
    if(!Objects.equals(item.getSeller().getEmail(), seller.getEmail())){
      return "you are not the correct seller";
    }
    item=auctionMap.remove(auctionID);
    if(item.getHighest_bid()>item.getReserve_price()) {
      return "The winner is " + item.getLastBidders().getName();
    }
    else if(item.getLastBidders()==null) {
      return "nobody has placed a bid";
    }
    return item.getLastBidders().getName()+" ,The reserve price is higher than your bid price ";
  }


  public synchronized boolean emailSame(String email){
    return clientMap.containsKey(email);
  }

  //It saves the information of the clients on the server
  public synchronized String addPerson(String email , Client client){
    clientMap.put(email,client);
    return client.getEmail();
  }

  public synchronized String setItemBid(Buyer buyer, int auctionID, double bid_price)  {
    AuctionItem item=auctionMap.get(auctionID);
    if(auctionMap.get(auctionID)==null){
      return "item don't exist";
    }

    if(item.getHighest_bid()>=bid_price){
      return "The bid price is lower than current bid";
    }
    item.setHighest_bid(bid_price);
    item.setLastBidders(buyer);
    return "you have succesfully placed a new bid";
  }

  public synchronized Client getPerson(String email)  {
    if(clientMap.isEmpty()){
      return null;
    }
    return clientMap.get(email);
  }

  public static void main(String args[]) throws Exception {
    new Backend();
  }

  public synchronized ConcurrentHashMap<Integer, AuctionItem> getAuctionMaps()
  {
    return auctionMap;
  }

  public synchronized ConcurrentHashMap<String,Client> getClientMaps()
  {
    return clientMap;
  }

  public synchronized String CheckConnection(){
    return "a";
  }
  private  ConcurrentHashMap<Integer, AuctionItem> getAuctionMap( RspList<ConcurrentHashMap<Integer,AuctionItem>> auctionMap_list){
    System.out.println("The auction list "+auctionMap_list.getResults());
    ConcurrentHashMap<Integer, AuctionItem> tempAuctionmap;
    if(auctionMap_list.isEmpty()) {
      tempAuctionmap=new ConcurrentHashMap<Integer,AuctionItem>();
    }
    else{
      tempAuctionmap = auctionMap_list.getResults().get(0);
    }
    return tempAuctionmap;
  }

  private ConcurrentHashMap<String,Client> getClientMap(RspList<ConcurrentHashMap<String,Client>> clientMap_list){
    ConcurrentHashMap<String,Client> tempClientmap;
    System.out.println("The client list "+ clientMap_list.getResults());
    if(clientMap_list.isEmpty()) {
      tempClientmap=new ConcurrentHashMap<String,Client>();
    }
    else{
      tempClientmap = clientMap_list.getResults().get(0);
    }
    return tempClientmap;
  }




}
