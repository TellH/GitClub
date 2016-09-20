package tellh.com.gitclub.presentation.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.utils.StringUtils;

/**
 * Created by tlh on 2016/9/18 :)
 */
public class PersonalPageTextView extends TextView {
    private String txtAppend;
    private BufferType mType;
    private CharSequence mText;

    public PersonalPageTextView(Context context) {
        super(context);
    }

    public PersonalPageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        setText(mText, mType);
    }

    public PersonalPageTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        setText(mText, mType);
    }

    private void init(Context context, AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PersonalPageTextView);
        txtAppend = typedArray.getString(R.styleable.PersonalPageTextView_text_append);
        typedArray.recycle();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        mText = text;
        mType = type;
        if (txtAppend == null) {
            super.setText(text, type);
            return;
        }
        SpannableString sp = new SpannableString(text + txtAppend);
        sp.setSpan(new RelativeSizeSpan(0.6f), text.length(), text.length() + txtAppend.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL), text.length(), text.length() + txtAppend.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        StringUtils.changeFontStyle("fonts/Georgia.ttf", this);
        super.setText(sp, type);
    }
}
