package kopacz.diabetool;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kopac on 11.09.2016.
 */
public class VolleyHelper {
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";
    public static String PROPOSE_PRODUCT_URL = "http://rustforum.ugu.pl/db_propose_product.php";
    public static String QUERY_PRODUCTS_URL = "http://rustforum.ugu.pl/db_query_products.php";

    public static StringRequest reportProduct(Product product, Response.Listener<String> response, Response.ErrorListener error)
    {
//        $name = $_POST["key"];
//        $energia = $_POST["value"];
//        $bialko = $_POST["value"];
//        $tluszcz = $_POST["value"];
//        $weglowodany = $_POST["value"];
//        $blonnik = $_POST["value"];
//        $cukier = $_POST["value"];
//        $kofeina = $_POST["value"];
//        $barcode = $_POST["value"];
        Map<String, String> params = new HashMap<>();
        // the POST parameters:
        params.put(DatabaseSchema.PRODUCT_NAME, product.getName());
        params.put(DatabaseSchema.PRODUCT_ENERGYVALUE, String.valueOf(product.getEnergyValue()));
        params.put(DatabaseSchema.PRODUCT_PROTEINS, String.valueOf(product.getProteins()));
        params.put(DatabaseSchema.PRODUCT_FAT, String.valueOf(product.getFat()));
        params.put(DatabaseSchema.PRODUCT_CARBOHYDRATES, String.valueOf(product.getCarbohydrates()));
        params.put(DatabaseSchema.PRODUCT_ROUGHAGE, String.valueOf(product.getRoughage()));
        params.put(DatabaseSchema.PRODUCT_SUGAR, String.valueOf(product.getSugar()));
        params.put(DatabaseSchema.PRODUCT_CAFFEINE, String.valueOf(product.getCaffeine()));
        params.put("Kod", String.valueOf(product.getBarcode()));
        Log.v("ukrabug", String.valueOf(product.getDateCreatedRaw()));

        params.put("Data_utworzenia", String.valueOf(product.getDateCreatedRaw()));
//)
        return new StringRequest(PROPOSE_PRODUCT_URL, params, response, error);
    }
    public static StringRequest queryProducts(int limit, int offset, Response.Listener<String> response, Response.ErrorListener error)
    {
        Map<String, String> params = new HashMap<>();
        // the POST parameters:
        params.put(LIMIT, String.valueOf(limit));
        params.put(OFFSET, String.valueOf(offset));

        return new StringRequest(QUERY_PRODUCTS_URL, params, response, error);
    }
}
