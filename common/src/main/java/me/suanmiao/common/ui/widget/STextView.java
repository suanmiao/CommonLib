package me.suanmiao.common.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import me.suanmiao.common.R;


/**
 * Created by suanmiao on 14-11-29.
 */
public class STextView extends TextView {

  private static Map<String, Typeface> fontCache = new HashMap<>();
  private String currentFontPath = null;

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
      if (currentFontPath != null) {
        Typeface font;
        if (fontCache.get(currentFontPath) != null) {
          font = fontCache.get(currentFontPath);
        } else {
          font = Typeface.createFromAsset(getResources().getAssets(), currentFontPath);
        }
        if (font != null) {
          fontCache.put(currentFontPath, font);
          if (!font.equals(getTypeface())) {
            setTypeface(font);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initAttr(Context context, AttributeSet attributeSet) {
    TypedArray a =
        context.obtainStyledAttributes(attributeSet, R.styleable.STextView);
    try {
      currentFontPath = a.getString(R.styleable.STextView_textFontPath);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
