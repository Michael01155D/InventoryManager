import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;

public class InventoryManager {
    private HashSet<String> serialNumbers;
    private HashMap<String, String> products;
    private HashMap<String, Integer> inventory;

    //default constructor
    public InventoryManager() {
        this.serialNumbers = new HashSet<>();
        this.products = new HashMap<>();
        this.inventory = new HashMap<>();
        //populate default inventory with 5 randomly generated and unique starting items:
        for (int i = 0; i < 5; i++) {
            String serialCode = createSerialCode();
            String productName = createRandomProduct();
            this.products.put(serialCode, productName);
            //give each item a random starting inventory of 1-50;
            this.inventory.put(productName, new Random().nextInt(1, 51));
        }
    }

    //constructor for if user generates their own starting products and inventory
    public InventoryManager(HashMap<String, Integer> products) {
        this.serialNumbers = new HashSet<>();
        this.products = new HashMap<>();
        this.inventory = new HashMap<>();
        for (String productName : products.keySet()) {
            String code = createSerialCode();
            this.products.put(code, productName);
            this.inventory.put(productName, products.get(productName));
        }
    }

    //private because called only in default constructor and addItem()
    private String createSerialCode() {
        final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        final String NUM = "0123456789";
        Random rand = new Random();
        String code = "";
        //ensure Serial code is unique combination of random letters and numbers
        do {
            for (int i = 0; i < 10; i++) {
                code += (i % 2 == 0) ? ALPHA.split("")[rand.nextInt(52)] : NUM.split("")[rand.nextInt(10)];
            }
        } while (this.serialNumbers.contains(code));
        this.serialNumbers.add(code);
        return code;
    }

    public String createRandomProduct() {
        Scanner fileScan;
        String newProduct = "";
        //true if at least one item in txt file isnt in inventory
        boolean safeToAdd = false;
        try {
            File productFile = new File("../products.txt");
            fileScan = new Scanner(productFile);
            Random rand = new Random();
            ArrayList<String> defaultProducts = new ArrayList<>();
            while (fileScan.hasNextLine()) {
                String product = fileScan.nextLine();
                defaultProducts.add(product);
                if (!this.products.containsKey(product)) {
                    safeToAdd = true;
                }
            }
            if (!safeToAdd) {
                System.out.println("Error: All possible default products are already in the inventory");
                return null;
            }
            //ensure product isn't in the inventory already
            do {
                int randIndex = rand.nextInt(defaultProducts.size());
                newProduct = defaultProducts.get(randIndex);
            } while (this.products.containsValue(newProduct));
            
        } catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
            newProduct = "Bug-Shaped Candy";
        }
        return newProduct;
    }

    public void renameProduct(String serialCode, String newName) {
        if (products.containsKey(serialCode)) {
            products.put(serialCode, newName);
        } else {
            System.out.println("Error: " + serialCode + " not in the Inventory!");
        }
    }

    public void addProduct(String productName, Scanner scanner) {
        if (this.products.containsValue(productName)) {
            System.out.println("Error: " + productName + " is already in the Inventory!");
            return;
        }
        //code gets added to HashSet inside fn
        String newCode = this.createSerialCode();
        this.products.put(newCode, productName);
        System.out.println("Select a starting inventory amount for " + productName + "(0 to 999)");
        int input = scanner.hasNextInt() ? scanner.nextInt() : 0;
        if (input > 999) {
            input = 999;
        } else if (input < 0) {
            input = 0;
        }
        System.out.println(productName + " has been added to the Inventory with " + input + " in stock");
        this.inventory.put(productName, input);
    }

    public void setInventoryAmount(String productName, int newAmount) {
        if (this.inventory.containsKey(productName)) {
            this.inventory.put(productName, newAmount);
        } else {
            System.out.println("Error: " + productName + " is not in the Inventory!");
        }
    }

    public void removeProduct(String serialCode) {

    }

    public String toString(){
        String output = "#### Current Inventory ####";
        for (String serialCode: this.products.keySet()) {
            String productName = this.products.get(serialCode);
            output += "\nName: " + productName + ", Current Stock: " + this.inventory.get(productName) +", Serial Number: " + serialCode+".";
        }
        return output;
    }
}
