package me.suanmiao.common.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import me.suanmiao.common.R;


/**
 * Created by suanmiao on 14-11-29.
 */
public class SEditText extends EditText {
  private static Typeface font;
  private String fontPath = null;

  public SEditText(Context context) {
    super(context);
    init(context);
  }

  public SEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    initAttr(context, attrs);
    init(context);
  }

  public SEditText(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initAttr(context, attrs);
    init(context);
  }

  private void init(Context context) {
    try {
      if (fontPath != null) {
        font = Typeface.createFromAsset(getResources().getAssets(), fontPath);
      }
      if (font != null) {
        setTypeface(font);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initAttr(Context context, AttributeSet attributeSet) {
    TypedArray a =
        context.obtainStyledAttributes(attributeSet, R.styleable.SEditText);
    try {
      fontPath = a.getString(R.styleable.SEditText_fontPath);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
