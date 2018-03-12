package kopacz.diabetool;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Created by Kopac on 29.07.2016.
 */
public class SquareImageButton extends ImageButton {
    public SquareImageButton(Context context) {
        super(context);
    }

    public SquareImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minDimension = Math.min(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(minDimension, minDimension);
    }
}
