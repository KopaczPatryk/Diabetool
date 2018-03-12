package kopacz.diabetool;

import android.database.Cursor;

import java.io.Serializable;

import NullAware.BooleanParser;

/**
 * Created by Kopac on 06.08.2016.
 */

@SuppressWarnings("WeakerAccess")
public class MealEntry extends DatabaseEntry implements Serializable {
    public static String SEPARATOR = "|";

    private String name = "";
    private String ingredientids = "";
    private String ingredientWeights = "";
    private float overalWeight = 0f;
    /*public MealOutline(String name, ArrayList<Ingredient> ingredients) {
        this.name = name;
    }*/
    public MealEntry()
    {
        super(); // todo uzupełnić
    }

    public MealEntry (Meal meal) {
        super(meal.getId(), meal.getRid(), meal.isSent(), meal.getDateCreatedRaw(), meal.isFavourite());
        StringBuilder ids = new StringBuilder();
        StringBuilder weights = new StringBuilder();
        for (int i = 0; i < meal.getIngredients().size(); i++)
        {
            ids.append(meal.getIngredient(i).getId());
            ids.append("|");

            weights.append(meal.getIngredient(i).getScale());
            weights.append("|");
        }
        ids.setLength(ids.length() - 1);
        weights.setLength(weights.length() - 1);

        this.name = meal.getName();
        this.ingredientids = ids.toString();
        this.ingredientWeights = weights.toString();
        this.overalWeight = meal.getOveralWeight();
    }

    static MealEntry decomposeCursor(Cursor c)
    {
        //todo dodać dekodowanie daty utworzenia
        MealEntry mo = new MealEntry();

        //cache of positions
        int iofID = c.getColumnIndex(DatabaseSchema.MEAL_ID);
        int iofRID = c.getColumnIndex(DatabaseSchema.MEAL_RID);
        int iofFav = c.getColumnIndex(DatabaseSchema.MEAL_FAVOURITE);
        int iofSent = c.getColumnIndex(DatabaseSchema.MEAL_SENT);

        int iofName = c.getColumnIndex(DatabaseSchema.MEAL_NAME);
        int iofProductsS = c.getColumnIndex(DatabaseSchema.MEAL_INGREDIENT_IDS);
        int iofWeightsS = c.getColumnIndex(DatabaseSchema.MEAL_INGREDIENT_WEIGHTS);
        int iofWeight = c.getColumnIndex(DatabaseSchema.MEAL_WEIGHT);

        mo.setId(c.getLong(iofID));
        mo.setRid(c.getLong(iofRID));
        mo.setFavourite(BooleanParser.parse(c.getLong(iofFav)));
        mo.setSent(BooleanParser.parse(c.getLong(iofSent)));

        //Log.v("urgent", "Id: " + mo.id);
        mo.name = c.getString(iofName);
        mo.ingredientids = c.getString(iofProductsS);
        mo.ingredientWeights = c.getString(iofWeightsS);
        mo.overalWeight = c.getFloat(iofWeight);

        //String productids = c.getString(iofProductsS);
        //String productWeights = c.getString(iofWeightsS);

        //String[] weights = productids.split("|");
        //String[] ids = productWeights.split("|");

        /*StringBuilder idsb = new StringBuilder();
        StringBuilder weightssb = new StringBuilder();
        for (int i = 0; i < weights.length; i++)
        {
            idsb.append(ids[i]);
            idsb.append("|");

            weightssb.append(weights[i]);
            weightssb.append("|");
        }*/

        mo.setSent(BooleanParser.parse(c.getInt(iofSent)));
        //cursor.close(); // chyba lepiej nie zamykać bo skasuje cursor który jest gdzieś używany
        return mo;
    }

    public String getName() {
        return name;
    }

    public String getIngredientids() {
        return ingredientids;
    }

    public String getIngredientWeights() {
        return ingredientWeights;
    }

    public float getOveralWeight() {
        return overalWeight;
    }
}
