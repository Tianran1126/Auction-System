package frontend;


import client.Buyer;
import client.Client;
import client.Seller;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.MembershipListener;
import org.jgroups.View;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.util.Rsp;
import org.jgroups.util.RspList;

import api.API;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import utility.GroupUtils;

public class Frontend extends UnicastRemoteObject implements API, MembershipListener {

  public static final long serialVersionUID = 42069;

  public final String SERVER_NAME = "random_number_generator";
  public final int REGISTRY_PORT = 1099;

  private JChannel groupChannel;
  private RpcDispatcher dispatcher;

  private final int DISPATCHER_TIMEOUT = 1000;

  public Frontend() throws RemoteException {
    // Connect to the group (channel)
    this.groupChannel = GroupUtils.connect();
    if (this.groupChannel == null) {
      System.exit(1); // error to be printed by the 'connect' function
    }

    // Bind this server instance to the RMI Registry
    this.bind(this.SERVER_NAME);

    // Make this instance of Frontend a dispatcher in the channel (group)
    this.dispatcher = new RpcDispatcher(this.groupChannel, this);
    this.dispatcher.setMembershipListener(this);

  }

  private void bind(String serverName) {
    try {
      Registry registry = LocateRegistry.createRegistry(this.REGISTRY_PORT);
      registry.rebind(serverName, this);
      System.out.println("✅    rmi server running...");
    } catch (Exception e) {
      System.err.println("🆘    exception:");
      e.printStackTrace();
      System.exit(1);
    }
  }

  @Override
  public String listAllItem() throws RemoteException {
    RspList<String> responses = null;
    try {
      responses = this.dispatcher.callRemoteMethods(null, "listAllItems",null,null, new RequestOptions(ResponseMode.GET_ALL, this.DISPATCHER_TIMEOUT));

    } catch (Exception e) {
      e.printStackTrace();
    }
    return (String) performLogic(responses);
  }

  @Override
  public int createAuctionItem(String description, double reserve_price, double start_price, Seller seller) throws RemoteException {
    RspList<Integer> responses = null;
    try {
      responses = this.dispatcher.callRemoteMethods(null ,"createItem",new Object[] { description, reserve_price,start_price,seller},
              new Class[]{String.class,double.class,double.class,Seller.class},new RequestOptions(ResponseMode.GET_ALL, this.DISPATCHER_TIMEOUT));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return (int) performLogic(responses);
  }

  @Override
  public String setBid(Buyer buyer, int auctionID, double bid_price) throws RemoteException {
    RspList<String> responses=null;
    try {
      responses = this.dispatcher.callRemoteMethods(null ,"setItemBid",new Object[] {buyer,auctionID,bid_price },
              new Class[]{Buyer.class,int.class,double.class},new RequestOptions(ResponseMode.GET_ALL, this.DISPATCHER_TIMEOUT));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return (String) performLogic(responses);
  }

  @Override
  public String closeAuction(int auctionID, Seller seller) throws RemoteException {
    RspList<String> responses=null;
    try {
      responses = this.dispatcher.callRemoteMethods(null ,"closeItem",new Object[] {auctionID,seller },
              new Class[]{int.class,Seller.class},new RequestOptions(ResponseMode.GET_ALL, this.DISPATCHER_TIMEOUT));
    } catch (Exception e) {
      e.printStackTrace();
    }
    if(performEqualCheck(responses)) {
      return responses.getResults().get(0);
    }

    return (String) performLogic(responses);
  }

  @Override
  public boolean emailDuplicate(String email) throws RemoteException {
    RspList<Boolean>responses=null;
    try {
      responses =this.dispatcher.callRemoteMethods(null ,"emailSame",new Object[] {email},
              new Class[]{String.class},new RequestOptions(ResponseMode.GET_ALL, this.DISPATCHER_TIMEOUT));
    } catch (Exception e) {
      e.printStackTrace();
    }
     return (boolean)performLogic(responses);
  }

  @Override
  public String addClient(String email, Client client) throws RemoteException {
    RspList<String> responses=null;
    try {
      responses = this.dispatcher.callRemoteMethods(null ,"addPerson",new Object[] {email,client},
              new Class[]{String.class,Client.class},new RequestOptions(ResponseMode.GET_ALL, this.DISPATCHER_TIMEOUT));
    } catch (Exception e) {
      e.printStackTrace();
    }
     return (String) performLogic(responses);
  }

  @Override
  public Client getClient(String email) throws RemoteException {
    RspList<Client> responses=null;
    try {
      responses = this.dispatcher.callRemoteMethods(null ,"getPerson",new Object[] {email},
              new Class[]{String.class},new RequestOptions(ResponseMode.GET_ALL, this.DISPATCHER_TIMEOUT));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return (Client) performLogic(responses);
  }
  @Override
  public boolean connect(){
    RspList<String> responses=null;
    try {
      responses = this.dispatcher.callRemoteMethods(null ,"CheckConnection",null,
              null,new RequestOptions(ResponseMode.GET_ALL, this.DISPATCHER_TIMEOUT));
    } catch (Exception e) {
      e.printStackTrace();
    }
    if(responses.isEmpty()){
      return true;
    }
    return false;
  }

  @Override
  public void viewAccepted(View view) {

  }

  public void suspect(Address suspectedMember) {

  }

  public void block() {
    System.out.printf("👀    jgroups view block indicator\n");
  }

  public void unblock() {
    System.out.printf("👀    jgroups view unblock indicator\n");
  }

  private boolean performEqualCheck(RspList rspList) {
    List results = rspList.getResults();
    return  results.stream().distinct().limit(results.size()).count() <= 1;
  }
  private Object performLogic(RspList responses){
     if(performEqualCheck(responses)) {
      return responses.getResults().get(0);
    }
    return killSever(responses);
  }
  private Object findMajority(List results)
  {
    int maxCount = 0;
    int index = -1;
    for (int i = 0; i < results.size(); i++) {
      int count = 0;
      for (int j = 0; j < results.size(); j++) {
        if (Objects.equals(results.get(i),results.get(j)))
          count++;
      }

      if (count > maxCount) {
        maxCount = count;
        index = i;
      }
    }
    return results.get(index); // return the oldest one
  }
  private Object killSever(RspList responses){
    Map.Entry entry;
    Object major = findMajority(responses.getResults());
    for (Iterator it = responses.entrySet().iterator(); it.hasNext(); ) {
      entry = (Map.Entry) it.next();
      Address address= (Address) entry.getKey();
      Rsp rsp = (Rsp) entry.getValue();
      Object object = rsp.getValue();
      if (!Objects.equals(object,major)) {
        try {
          this.dispatcher.callRemoteMethod(address, "exit", null, null, new RequestOptions(ResponseMode.GET_NONE, this.DISPATCHER_TIMEOUT));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return major;
  }

  public static void main(String args[]) {
    try {
      new Frontend();
    } catch (RemoteException e) {
      System.err.println("🆘    remote exception:");
      e.printStackTrace();
      System.exit(1);
    }
  }

}
