package me.suanmiao.common.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import me.suanmiao.common.R;


/**
 * Created by suanmiao on 14-11-29.
 */
public class STextView extends TextView {

  private static Typeface font;
  private String fontPath = null;

  public STextView(Context context) {
    super(context);
    init(context);
  }

  public STextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initAttr(context, attrs);
    init(context);
  }

  public STextView(Context context, AttributeSet attrs, int defStyle) {
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
        context.obtainStyledAttributes(attributeSet, R.styleable.STextView);
    try {
      fontPath = a.getString(R.styleable.STextView_textFontPath);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
