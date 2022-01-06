package client;
import java.io.Serializable;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import api.API;

public class Client  implements Serializable {

  private String name;
  private String email;
  public Client(String name, String email) {
    this.name = name;
    this.email = email;
  }


  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object object) {

    if (object == this) {
      return true;
    }

    if (!(object instanceof Client)) {
      return false;
    }

    Client otherClient = (Client) object;
    return otherClient.email.equals(otherClient.email);
  }

  @Override
  public String toString() {
    return "Client{" +
            "Email= " + email +
            '}';
  }

  public static void main(String[] args) {
    final String SERVER_NAME = "random_number_generator";
    try {
      Registry registry = LocateRegistry.getRegistry();
      API server = (API) registry.lookup(SERVER_NAME);
      Auth auth=new Auth();
      if(server.connect()){
        System.out.println("there are no backend severs , so system shuts down");
        System.out.println("Please create your backend servers before you create the frontend server");
        System.exit(0);
      }
      System.out.println("Enter l for login , s for sign_up");
      Scanner scanner = new Scanner(System.in);
      String option=scanner.nextLine();
      if (option.equals("l")){
        auth.login(server);
      }
      else  if(option.equals("s")){
        auth.sign_up(server);
      }
    } catch (Exception e) {
      System.err.println("🆘 exception:");
      e.printStackTrace();
      System.exit(1);
    }
  }
}


            