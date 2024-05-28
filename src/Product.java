public class Product {
    
    private String serialCode;

    private String name;

    public Product(String name, String serialCode) {
        this.name = name;
        this.serialCode = serialCode;
    }

    public Product() {
        this("unknown product", "00000000000");
    }

    public String getName() {
        return name;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "\nName: " + this.name + " | Serial Number: " + serialCode+" |\n";
    }
}
