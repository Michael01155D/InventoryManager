import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
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
            //
            String code = createSerialCode();
            String productName = createRandomProduct();
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

        System.out.println("Testing, the code generated is: " + code);
        this.serialNumbers.add(code);
        return code;
    }

    //todo: ensure this doesn't execute do-while loop if all items in products.txt are in the inventory already!
    public String createRandomProduct() {
        Scanner fileScan;
        String newProduct = "";
        try {
            File productFile = new File("./products.txt");
            fileScan = new Scanner(productFile);
            Random rand = new Random();
            String defaultProducts = "";
            while (fileScan.hasNextLine()){
                defaultProducts += fileScan.nextLine();
            }
            String[] defaultProductsArr = defaultProducts.split("\n");
            System.out.println("Debug check: array after reading from products file is: " + defaultProductsArr);
            //ensure product isn't in the inventory already
            do {
                int randIndex = rand.nextInt(defaultProductsArr.length);
                newProduct = defaultProductsArr[randIndex];
            } while (this.products.values().contains(newProduct));
            
        } catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
            newProduct = "Bug-Shaped Candy";
        }
        return newProduct;
    }

}
