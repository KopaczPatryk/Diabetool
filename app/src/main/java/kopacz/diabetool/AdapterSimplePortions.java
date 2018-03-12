package kopacz.diabetool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kopac on 06.08.2016.
 */
class AdapterSimplePortions extends BaseAdapter {
    //public static int VALUE_ID = R.id.listrow_value;
    private Context context;
    private ArrayList<Product> ingredients;
    private static LayoutInflater inflater = null;

    public AdapterSimplePortions(Context context, ArrayList<Product> ingredients) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.ingredients = ingredients;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub

        return ingredients.size();
    }

    @Override
    public Product getItem(int position) {
        // TODO Auto-generated method stub
        return ingredients.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;

        if (vi == null) {
            vi = inflater.inflate(R.layout.listview_meal_row_product_entry, null);
        }

        TextView header = (TextView) vi.findViewById(R.id.listrow_header);
        FloatFormatter df = new FloatFormatter();
        header.setText(String.format(context.getString(R.string.adapter_simple_portions_header), ingredients.get(position).getName(), df.format(ingredients.get(position).getScale())));

        /* Fragment */
        TextView eval = (TextView) vi.findViewById(R.id.eval);
        TextView proteins = (TextView) vi.findViewById(R.id.proteins);
        TextView fat = (TextView) vi.findViewById(R.id.fat);
        TextView carbohydrates = (TextView) vi.findViewById(R.id.carbohydrates);
        TextView roughage = (TextView) vi.findViewById(R.id.roughage);
        TextView sugar = (TextView) vi.findViewById(R.id.sugar);

        LocaleFormater lf = new LocaleFormater(df, context);
        eval.setText(lf.catchNull(ingredients.get(position).getEnergyValue(), R.string.f_productinfo_energyvalue, R.string.f_productinfo_unknwn_contents, R.string.f_productinfo_unit_kcal_short_dotted));
        proteins.setText(lf.catchNull(ingredients.get(position).getProteins(), R.string.f_productinfo_proteins, R.string.f_productinfo_unknwn_contents, R.string.f_productinfo_unit_gram_short_dotted));
        fat.setText(lf.catchNull(ingredients.get(position).getFat(), R.string.f_productinfo_fat, R.string.f_productinfo_unknwn_contents, R.string.f_productinfo_unit_gram_short_dotted));
        carbohydrates.setText(lf.catchNull(ingredients.get(position).getCarbohydrates(), R.string.f_productinfo_carbohydrates, R.string.f_productinfo_unknwn_contents, R.string.f_productinfo_unit_gram_short_dotted));
        roughage.setText(lf.catchNull(ingredients.get(position).getRoughage(), R.string.f_productinfo_roughage, R.string.f_productinfo_unknwn_contents, R.string.f_productinfo_unit_gram_short_dotted));
        sugar.setText(lf.catchNull(ingredients.get(position).getSugar(), R.string.f_productinfo_sugar, R.string.f_productinfo_unknwn_contents, R.string.f_productinfo_unit_gram_short_dotted));

        /*eval.setText(String.format(context.getString(R.string.f_productinfo_energyvalue), df.format(ingredients.get(position).getEnergyValue())));
        proteins.setText(String.format(context.getString(R.string.f_productinfo_proteins), df.format(ingredients.get(position).getProteins())));
        fat.setText(String.format(context.getString(R.string.f_productinfo_fat), df.format(ingredients.get(position).getFat())));
        carbohydrates.setText(String.format(context.getString(R.string.f_productinfo_carbohydrates), df.format(ingredients.get(position).getCarbohydrates())));
        roughage.setText(String.format(context.getString(R.string.f_productinfo_roughage), df.format(ingredients.get(position).getRoughage())));
        sugar.setText(String.format(context.getString(R.string.f_productinfo_sugar), df.format(ingredients.get(position).getSugar())));
*/
        return vi;
    }
}