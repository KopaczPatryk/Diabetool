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
class AdapterProductCursor extends CursorAdapter {

    public AdapterProductCursor(Context context, Cursor c) {
        super(context, c, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listview_row, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView header = (TextView) view.findViewById(R.id.listrow_header);
        String text = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSchema.PRODUCT_NAME));
        header.setText(text);
    }
}