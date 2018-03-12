package kopacz.diabetool;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Kopac on 16.06.2016.
 */
class AdapterMealOutlineCursor extends CursorAdapter {

    AdapterMealOutlineCursor(Context context, Cursor c) {
        super(context, c, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listview_meal_simple_row, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView header = (TextView) view.findViewById(R.id.listrow_header);
        //FloatFormatter ff = new FloatFormatter();

        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSchema.MEAL_NAME));
        String weight = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSchema.MEAL_WEIGHT));
//        String.format(context.getString(R.string.adapter_meal_cursor_header), name, ff.format(weight))
        header.setText(String.format(context.getString(R.string.adapter_meal_cursor_header), name, weight));
        //Log.v("Adapter", "header: " + name);
        //header.setText(name);
    }
}