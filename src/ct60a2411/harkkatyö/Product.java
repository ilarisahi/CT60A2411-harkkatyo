package ct60a2411.harkkatyö;

import java.util.HashMap;
import java.util.Map;

/**
 * Product class
 * 
 * @author Petri Rämö
 * opiskelijanro: 0438578
 * 
 * @author Ilari Sahi
 * opiskelijanro: 0438594
 * 
 * 16.12.2016
 * 
 * Superclass for products
 * Constructor initializes Map for dimensions
 */
public class Product {
    protected String name;
    protected boolean fragile;
    protected int fragile_factor;
    protected Map<String, Double> dimension = new HashMap<>();
    
    public Product() {
        dimension.put("weight", 0.0);
        dimension.put("height", 0.0);
        dimension.put("width", 0.0);
        dimension.put("depth", 0.0);
    }
    
    public Product(Double a, Double b, Double c, Double d, String e, boolean f) {
        name = e;
        dimension.put("weight", a);
        dimension.put("height", b);
        dimension.put("width", c);
        dimension.put("depth", d);
        fragile = f;
        if (f) {
            fragile_factor = 50;
        }
    }
    
    public String getName() {
        return name;
    }

    public boolean isFragile() {
        return fragile;
    }

    public int getFragile_factor() {
        return fragile_factor;
    }

    public Map<String, Double> getDimension() {
        return dimension;
    }
}