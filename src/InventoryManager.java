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
            //give each item a random starting inventory of 1-999;
            this.inventory.put(productName, new Random().nextInt(1, 1000));
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
    //populate default products with random ones from txt file
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
                defaultProducts.add(product.toLowerCase());
                if (!this.products.containsKey(product)) {
                    safeToAdd = true;
                }
            }
            if (!safeToAdd) {
                System.out.println("Error: All possible default products are already in the inventory");
                return null;
            }
            //ensure product name is distinct
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

    public void renameProduct(String oldName, String newName) {
        oldName = oldName.toLowerCase().trim();
        newName = newName.toLowerCase().trim();
        if (!this.inventory.containsKey(oldName)) {
            invalidProductError(oldName);
            return;
        }

        if (this.inventory.containsKey(newName)) {
            duplicateProductError(newName);
            return;
        }

        for (String serialCode: this.products.keySet()) {
            if (this.products.get(serialCode).equals(oldName)) {
                this.products.put(serialCode, newName);
                int stock = this.inventory.get(oldName);
                this.inventory.remove(oldName);
                this.inventory.put(newName, stock);
                System.out.println("\nSuccessfully renamed " + oldName +" to " + newName);
                return;
            }
        }
    }

    public void addProduct(String productName, Scanner scanner) {
        productName = productName.toLowerCase().trim();
        if (this.products.containsValue(productName)) {
            duplicateProductError(productName);;
            return;
        }
        //code gets added to HashSet inside fn
        String newCode = this.createSerialCode();
        this.products.put(newCode, productName);
        System.out.println("\nSelect a starting inventory amount for " + productName + "(0 to 999)");
        int input = 0;
        if (scanner.hasNextInt()) {
            input = scanner.nextInt();
            //consume \n so scanner behaves as expected
            scanner.nextLine();
        }
        input = validateQuantity(input);
        System.out.println("\n"+productName + " has been added to the Inventory with " + input + " in stock");
        this.inventory.put(productName, input);
    }

    public void setInventoryAmount(String productName, int newAmount) {
        productName = productName.toLowerCase().trim();
        if (this.inventory.containsKey(productName)) {
            newAmount = validateQuantity(newAmount);
            this.inventory.put(productName, newAmount);
            System.out.println("\nInventory adjustment successful, there are now " + newAmount + " " + productName + " in stock.");
            return;
        }

        invalidProductError(productName);
    }

    public void removeProduct(String productName) {
        productName = productName.toLowerCase().trim();
        if (this.inventory.containsKey(productName)) {
            for (String serialCode : this.products.keySet()) {
                if (this.products.get(serialCode).equals(productName)) {
                    this.serialNumbers.remove(serialCode);
                    this.products.remove(serialCode);
                    this.inventory.remove(productName);
                    System.out.println("\n"+ productName + " successfully removed from the Inventory.");
                    return;
                }
            }
        }
        invalidProductError(productName);
    }

    public int getProductAmount(String productName) {
        productName = productName.toLowerCase().trim();
        if (this.inventory.containsKey(productName)) {
            return this.inventory.get(productName);
        }
        invalidProductError(productName);
        return 0;
    }

    public void invalidProductError(String productName) {
        System.out.println("\nError: " + productName + " is not in the Inventory!");
    }

    public void duplicateProductError(String productName) {
        System.out.println("\nError: " + productName + " is already in the Inventory!");
    }

    //if inputted stock amount is < 0 or > 999, adjust to one of those bounds
    public int validateQuantity(int stock) {
        if (stock > 999) {
            System.out.println("\nError, stock must be between 0 to 999, setting stock to 999.");
            stock = 999;
        }
        else if (stock < 0) {
            System.out.println("\nError, stock must be between 0 to 999, setting stock to 0.");
            stock = 0;
        }

        return stock;
    }

    public String toString(){
        String output = "\n########################## Current Inventory ##########################\n";
        for (String serialCode: this.products.keySet()) {
            String productName = this.products.get(serialCode);
            output += "\nName: " + productName + " | Current Stock: " + this.inventory.get(productName) +" | Serial Number: " + serialCode+" |\n";
        }
        output += "\n#######################################################################\n";
        return output;
    }
}
