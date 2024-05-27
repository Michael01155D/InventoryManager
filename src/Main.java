import java.util.HashMap;
import java.util.Scanner;
public class Main {

//Inv manager has: Map of Serial Code: PName. Map of Serial Code: qty, HashSet of serial codes
//methods: clear inventory, addStock, checkInventory, sellItem, addItem, removeItem, 

//next todo: let user decide to call default constructor or enter their own product names/ starting qtys  
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    String input = "";
    //check if input is neither 1 nor 2
    while (!commandGiven(input, "1") && !commandGiven(input, "2")) {
      System.out.println("----- Welcome! Please Type a Number for the Following Starting Inventory Options: -----\n[1]default\n[2]custom");
      input = scanner.nextLine();
    }
    InventoryManager im = createInventoryManager(input, scanner);
    System.out.println(im);
    boolean endProgram = false;
    while (true) {
      printCommands();
      input = scanner.nextLine();
      String productName;
      switch(input.toLowerCase().trim()) {
        case "inventory":
          System.out.println(im);
          break;
        case "add":
          System.out.println("\nPlease enter the name of the product you'd like to add: ");
          do {
            productName = scanner.nextLine().trim();
          } while(productName.trim().isEmpty());

          im.addProduct(productName, scanner);
          break;
        case "remove":
          System.out.println("Please enter the name of the product you'd like to remove: ");
          do {
            productName = scanner.nextLine().trim();
          } while(productName.trim().isEmpty());
          im.removeProduct(productName);
          break;
        case "restock":
          System.out.println(im);
          System.out.println("\nplease enter the name of the product whose stock you'd like to adjust:");
          productName = scanner.nextLine().trim();
          System.out.println("\nEnter the new stock amount (0 to 999):");
          int newStock = 0;
          try {
            newStock = scanner.nextInt();
          } catch(Exception e) {
            System.out.println("\nInvalid input, setting stock for " + productName + " to 0");
          }
          input = scanner.nextLine();
          im.setInventoryAmount(productName, newStock);
          break;
        case "rename":
          System.out.println("\nPlease enter the name of the product you'd like to rename.");
          String oldName = scanner.nextLine().trim();
          System.out.println("\nPlease enter the new name for " + oldName +":");
          String newName = "";
          do {
            newName = scanner.nextLine();
          } while(newName.trim().isEmpty());
          im.renameProduct(oldName, newName);
          break;
        case "clear":
          im.clearInventory();
          break;
        case "exit":
          System.out.println("\nClosing shop for the day. Come again!");
          endProgram = true;
          break;
        default:
          System.out.println("\nError, invalid command: " + input.trim());
          break;
      }
      if (endProgram) {
        break;
      }
    }
    
    scanner.close();
  }

  //helper function to instantiate InventoryManager with either default or custom input
  public static InventoryManager createInventoryManager(String startingOption, Scanner scanner) {
    if (startingOption.equals("1")) {
      return new InventoryManager();
    }

    HashMap<String, Integer> startingInventory = new HashMap<>();
    while (true) {
      System.out.println("\nPlease enter the name of the product, or 'done' to finish setting up inventory");
      String productName = "";
      do {
        productName = scanner.nextLine().toLowerCase().trim();
      } while (productName.trim().isEmpty());
      if (commandGiven(productName, "done")) {
        System.out.println("\nFinished adding items to the inventory.");
        break;
      } else if (startingInventory.containsKey(productName)) {
        System.out.println("\nError: The Inventory already has " + productName +"!");
        continue;
      }
      //validate input for starting stock amount
      while (true){
        System.out.println("\nHow many " + productName +" are in stock? Please enter a number from 0 to 999, or 'cancel' to not add " + productName + " to inventory.");
          String stock = scanner.nextLine().trim();
          if (commandGiven(stock, "cancel")) {
            System.out.println("\n" + productName + " was not added to the inventory.");
            break;
          }
          try {
            int stockInt = Integer.parseInt(stock);
            if (stockInt < 0 || stockInt > 999) {
              System.out.println("\nError, please enter a number from 0 to 999");
              continue;
            }
            System.out.println("\n"+ productName + " added to inventory with starting stock of: " + stockInt);
            startingInventory.put(productName, stockInt);
            break;
          } catch (Exception e) {
            System.out.println("\nError, please enter a number from 0 to 999");
            continue;
          }
      }
    }
    return  !(startingInventory.isEmpty()) ? new InventoryManager(startingInventory) : new InventoryManager();
  }

  //check if input matches command, or 'command' with single quotes
  public static boolean commandGiven(String input, String command ) {
    return input.toLowerCase().trim().equals(command) || input.toLowerCase().trim().equals("'"+command+"'");
  }

  //todo commands: clear inventory, 
  public static void printCommands(){
    System.out.println("\n****Please type one of the following commands:****");
    System.out.println("\n'inventory': Display the current inventory");
    System.out.println("\n'add': Add a new product to the inventory");
    System.out.println("\n'remove': Remove an existing product from the inventory");
    System.out.println("\n'restock': Adjust a product's quantity");
    System.out.println("\n'rename': Change a product's name");
    System.out.println("\n'clear': Clear out the entire inventory");
    System.out.println("\n'exit': Exit the program");
  }
}