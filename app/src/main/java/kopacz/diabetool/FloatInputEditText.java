package kopacz.diabetool;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Kopac on 01.08.2016.
 */
public class FloatInputEditText extends EditText {
    public FloatInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.addTextChangedListener(new FloatDotTextWatcher(this));
    }

    public FloatInputEditText(Context context) {
        super(context);
        this.addTextChangedListener(new FloatDotTextWatcher(this));
    }

    public FloatInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.addTextChangedListener(new FloatDotTextWatcher(this));
    }
}