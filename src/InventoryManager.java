import java.lang.reflect.Array;
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

    //todo: ensure this doesn't execute do-while loop if all items in products.txt are in the inventory already!
    public String createRandomProduct() {
        Scanner fileScan;
        String newProduct = "";
        try {
            File productFile = new File("src/products.txt");
            fileScan = new Scanner(productFile);
            Random rand = new Random();
            ArrayList<String> defaultProducts = new ArrayList<>();
            while (fileScan.hasNextLine()) {
                defaultProducts.add(fileScan.nextLine());
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

    public String toString(){
        String output = "#### Current Inventory ####";
        for (String product: this.inventory.keySet()) {
            output += "\n" + product + ", current stock: " + this.inventory.get(product);
        }
        return output;
    }

    public void renameProduct(String serialCode, String newName) {
        if (products.containsKey(serialCode)) {
            products.put(serialCode, newName);
        }
    }
}
