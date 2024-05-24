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
      System.out.println("##### Please Type a Number for the Following Starting Inventory Options:\n[1]default\n[2]custom");
      input = scanner.next();
    }
    InventoryManager im = createInventoryManager(input);
    System.out.println(im);
    boolean endProgram = false;
    while (true) {
      printCommands();
      input = scanner.next();
      switch(input.toLowerCase().trim()) {
        case "inventory":
          System.out.println(im);
          break;
        case "exit":
          System.out.println("Closing shop for the day. Come again!");
          endProgram = true;
          break;
        default:
          System.out.println("Unrecognized command: " + input);
          break;
      }
      if (endProgram) {
        break;
      }
    }
    
    scanner.close();
  }

  //helper function to instantiate InventoryManager with either default or custom input
  public static InventoryManager createInventoryManager(String startingOption) {
    if (startingOption.equals("1")) {
      return new InventoryManager();
    }
    HashMap<String, Integer> startingInventory = new HashMap<>();
    Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.println("Please enter the name of the product, or 'done' to finish setting up inventory");
      String productName = scanner.next();
      if (commandGiven(productName, "done")) {
        System.out.println("Finished adding items to the inventory.");
        break;
      } else if (startingInventory.containsKey(productName)) {
        System.out.println("Error: The Inventory already has " + productName +"!");
        continue;
      }
      //validate input for starting stock amount
      while (true){
        System.out.println("How many " + productName +" are in stock? Please enter a number from 0 to 999, or 'cancel' to not add " + productName + " to inventory.");
          String stock = scanner.next();
          if (commandGiven(stock, "cancel")) {
            System.out.println(productName + " was not added to the inventory.");
            break;
          }
          try {
            int stockInt = Integer.parseInt(stock);
            if (stockInt < 0 || stockInt > 999) {
              System.out.println("Error, please enter a number from 0 to 999");
              continue;
            }
            System.out.println(productName + " added to inventory with starting stock of: " + stockInt);
            startingInventory.put(productName, stockInt);
            break;
          } catch (Exception e) {
            System.out.println("Error, please enter a number from 0 to 999");
            continue;
          }
      }
    }
    scanner.close();
    return  !(startingInventory.isEmpty()) ? new InventoryManager(startingInventory) : new InventoryManager();
  }

  //check if input matches command, or 'command' with single quotes
  public static boolean commandGiven(String input, String command ) {
    return input.toLowerCase().trim().equals(command) || input.toLowerCase().trim().equals("'"+command+"'");
  }

  //todo commands: add product, remove product, restock product, clear inventory, update product name,  
  public static void printCommands(){
    System.out.println("Please type one of the following commands:");
    System.out.println("'inventory': Display the current inventory");
    System.out.println("'exit': Exit the program");
  }
}