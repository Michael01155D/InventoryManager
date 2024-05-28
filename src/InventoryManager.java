import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;

public class InventoryManager {
    private HashSet<String> serialNumbers;
    private HashSet<String> productNames;
    private HashMap<String, Product> products;
    private HashMap<String, Integer> inventory;

    //default constructor
    public InventoryManager() {
        this.serialNumbers = new HashSet<>();
        this.productNames = new HashSet<>();
        this.products = new HashMap<>();
        this.inventory = new HashMap<>();
        //populate default inventory with 5 randomly generated and unique starting items:
        for (int i = 0; i < 5; i++) {
            String productName = this.getRandomProductName();
            String productCode = this.createUniqueSerialCode();
            int startingInventory = new Random().nextInt(1, 1000);
            Product newProduct = new Product(productName, productCode);
            this.productNames.add(productName);
            this.products.put(productCode, newProduct);
            //give each item a random starting inventory of 1-999;
            this.inventory.put(productName, startingInventory);
        }
    }

    //constructor for if user generates their own starting products and inventory
    public InventoryManager(HashMap<String, Integer> startingData) {
        this.serialNumbers = new HashSet<>();
        this.productNames = new HashSet<>();
        this.products = new HashMap<>();
        this.inventory = new HashMap<>();
        for (String productName : startingData.keySet()) {
            String code = createUniqueSerialCode();
            Product newProduct = new Product(code, productName);
            this.productNames.add(productName);
            this.products.put(code, newProduct);
            this.inventory.put(productName, startingData.get(productName));
        }
    }

    //private because called only in default constructor and addItem()
    private String createUniqueSerialCode() {
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
    public String getRandomProductName() {
        Scanner fileScan;
        String newProductName = "";
        //true if at least one item in txt file isnt in inventory
        boolean safeToAdd = false;
        try {
            File productFile = new File("./products.txt");
            fileScan = new Scanner(productFile);
            Random rand = new Random();
            ArrayList<String> defaultProducts = new ArrayList<>();
            while (fileScan.hasNextLine()) {
                String productName = fileScan.nextLine();
                defaultProducts.add(productName.toLowerCase());
                if (!this.productNames.contains(productName)) {
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
                newProductName = defaultProducts.get(randIndex);
            } while (this.productNames.contains(newProductName));
            
        } catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
            newProductName = "Bug-Shaped Candy";
        }
        return newProductName;
    }

    public void renameProduct(String oldName, String newName) {
        oldName = oldName.toLowerCase().trim();
        newName = newName.toLowerCase().trim();
        if (!this.productNames.contains(oldName)) {
            invalidProductError(oldName);
            return;
        }

        if (this.productNames.contains(newName)) {
            duplicateProductError(newName);
            return;
        }

        for (String serialCode: this.products.keySet()) {
            if (this.products.get(serialCode).getName().equals(oldName)) {
                this.products.get(serialCode).setName(newName);
                this.productNames.remove(oldName);
                this.productNames.add(newName);
                this.inventory.put(newName, this.inventory.get(oldName));
                this.inventory.remove(oldName);
                System.out.println("\nSuccessfully renamed " + oldName +" to " + newName);
                return;
            }
        }
    }

    public void addProduct(String productName, Scanner scanner) {
        productName = productName.toLowerCase().trim();
        if (this.productNames.contains(productName)) {
            duplicateProductError(productName);;
            return;
        }
        //code gets added to HashSet inside fn
        String newCode = this.createUniqueSerialCode();
        Product newProduct = new Product(productName, newCode);
        this.products.put(newCode, newProduct);
        System.out.println("\nSelect a starting inventory amount for " + productName + "(0 to 999)");
        int inputStock = 0;
        if (scanner.hasNextInt()) {
            inputStock = scanner.nextInt();
            //consume \n so scanner behaves as expected
            scanner.nextLine();
        }
        inputStock = validateQuantity(inputStock);
        System.out.println("\n"+productName + " has been added to the Inventory with " + inputStock + " in stock");
        this.inventory.put(productName, inputStock);
    }

    public void setInventoryAmount(String productName, int newAmount) {
        productName = productName.toLowerCase().trim();
        if (this.inventory.containsKey(productName)) {
            for (Product product: this.products.values()) {
                if (product.getName().equals(productName)) {
                    newAmount = validateQuantity(newAmount);
                    this.inventory.put(productName, newAmount);
                    System.out.println("\nInventory adjustment successful, there are now " + newAmount + " " + productName + " in stock.");
                    return;
                }
            }
        }
        invalidProductError(productName);
    }

    public void removeProduct(String productName) {
        productName = productName.toLowerCase().trim();
        for (Product product: this.products.values()){
            if (product.getName().equals(productName)) {
                this.serialNumbers.remove(product.getSerialCode());
                this.productNames.remove(productName);
                this.products.remove(product.getSerialCode());
                this.inventory.remove(productName);
                System.out.println("\n"+ productName + " successfully removed from the Inventory.");
                return;
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

    public void clearInventory() {
        this.serialNumbers.clear();
        this.productNames.clear();
        this.inventory.clear();
        this.products.clear();
        System.out.println("\nThe Inventory has been cleared off all products.");
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
        for (Product product: this.products.values()) {
            output += product.toString() + " Current Stock: " + this.inventory.get(product.getName()) +"\n";
        }
        output += "\n#######################################################################\n";
        return output;
    }
}
