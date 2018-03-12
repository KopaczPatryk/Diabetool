package kopacz.diabetool;

import java.io.Serializable;
import java.util.ArrayList;

import NullAware.NullAwareFloat;

/**
 * Created by Kopac on 06.08.2016.
 */

@SuppressWarnings({"WeakerAccess", "DefaultFileTemplate"})
public class Meal extends Edible implements Serializable {
    private String name;
    private ArrayList<Product> ingredients;

    public Meal() {
        super();
        this.name = "";
        this.ingredients = new ArrayList<>();
    }

    public Meal (long id, long rid, String name, ArrayList<Product> products)
    {
        super();
        this.setId(id);
        this.setRid(rid);
        this.setName(name);
        this.setProducts(products);
    }

    public Meal(long id, long rid, boolean sent, long date_created, boolean favourite, String name, float energyValue, float proteins, float fat, float carbohydrates, float roughage, float sugar, float caffeine, String name1, ArrayList<Product> ingredients) {
        super(id, rid, sent, date_created, favourite, name, energyValue, proteins, fat, carbohydrates, roughage, sugar, caffeine);
        this.name = name1;
        this.ingredients = ingredients;
    }

    /*public Meal(String name, ArrayList<Product> ingredients) {
        // todo uzupełnić super
        super();
        this.name = name;
        this.ingredients = ingredients;
    }*/

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Product> getIngredients ()
    {
        return ingredients;
    }
    public Product getProduct(long idx)
    {
        return ingredients.get((int) idx);
    }
    public ArrayList<Product> getProducts() {
        return ingredients;
    }
    public void setProducts(ArrayList<Product> ingredients) {
        this.ingredients = ingredients;
    }

    public void addProduct(Product ingredient) {
        this.ingredients.add(ingredient);
    }

    public void removeProduct (int index)
    {
        ingredients.remove(index);
    }

    public Product getIngredient(int i) {
        return ingredients.get(i);
    }

    public MealEntry getMealOutline () //todo skasować, helper powinien mieć jedyną możliowść kreacji
    {
        return new MealEntry(this);
    }

    @Override
    public float getEnergyValue() {
        NullAwareFloat v = new NullAwareFloat();
        for (int i = 0; i < this.ingredients.size(); i++) {
            v.add(this.ingredients.get(i).getEnergyValue());
        }
        return v.getValue();
    }

    @Override
    public float getFat() {
        NullAwareFloat v = new NullAwareFloat();
        for (int i = 0; i < this.ingredients.size(); i++) {
            v.add(this.ingredients.get(i).getFat());
        }
        return v.getValue();
    }

    @Override
    public float getProteins() {
        NullAwareFloat v = new NullAwareFloat();
        for (int i = 0; i < this.ingredients.size(); i++) {
            v.add(this.ingredients.get(i).getProteins());
        }
        return v.getValue();
    }

    @Override
    public float getRoughage() {
        NullAwareFloat v = new NullAwareFloat();
        for (int i = 0; i < this.ingredients.size(); i++) {
            v.add(this.ingredients.get(i).getRoughage());
        }
        return v.getValue();
    }

    @Override
    public float getSugar() {
        NullAwareFloat v = new NullAwareFloat();
        for (int i = 0; i < this.ingredients.size(); i++) {
            v.add(this.ingredients.get(i).getSugar());
        }
        return v.getValue();
    }

    @Override
    public float getCarbohydrates() {
        NullAwareFloat v = new NullAwareFloat();
        for (int i = 0; i < this.ingredients.size(); i++) {
            v.add(this.ingredients.get(i).getCarbohydrates());
        }
        return v.getValue();
    }

    public float getOveralWeight () {
        float w = 0f;
        for (Product p : getIngredients()) {
            //if (p.getScale() != -1)
            //{
                w += p.getScale();
            //}
        }
        return w;
    }
}