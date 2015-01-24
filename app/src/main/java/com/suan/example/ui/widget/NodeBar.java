package com.suan.example.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.suan.example.R;
import com.suan.example.util.TextUtil;

import java.util.List;

/**
 * Created by suanmiao on 14/12/27.
 */
public class NodeBar extends View {
    private int currentNode = 0;
    private List<Node> nodeList;

    private int progressPaddingHorizontal = 0;
    private int progressBarHeight = 0;
    private int normalNodeRadius = 0;
    private int selectedNodeRadius = 0;
    private int barMarginBottom = 0;
    private int textMarginBottom = 0;
    private Drawable progressDrawable;
    private Drawable selectedNodeDrawable;
    private Drawable normalNodeDrawable;
    private static final int MOVE_SLOP = 6;
    private float startX, startY;
    private float lastX, lastY;
    private long downTime;
    private static final long CLICK_TIME_THRESHOLD = 500l;
    private float nodeTextSize;

    private OnClickListener onClickListener;

    private NodeProgressListener progressListener;
    private Paint mPaint;

    public NodeBar(Context context) {
        super(context);
        init();
    }

    public NodeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NodeBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        progressPaddingHorizontal = getResources().getDimensionPixelSize(R.dimen.default_node_bar_horizontal_padding);
        normalNodeRadius = getResources().getDimensionPixelSize(R.dimen.default_normal_node_radius);
        selectedNodeRadius = getResources().getDimensionPixelSize(R.dimen.default_selected_node_radius);
        progressBarHeight = getResources().getDimensionPixelSize(R.dimen.default_node_progress_height);
        barMarginBottom = getResources().getDimensionPixelSize(R.dimen.default_node_bar_margin_bottom);
        textMarginBottom = getResources().getDimensionPixelSize(R.dimen.default_node_text_margin_bottom);
        nodeTextSize = getResources().getDimensionPixelSize(R.dimen.default_node_text_size);

        normalNodeDrawable = getResources().getDrawable(R.drawable.ic_default_normal_node);
        selectedNodeDrawable = getResources().getDrawable(R.drawable.ic_default_selected_node);
        progressDrawable = getResources().getDrawable(R.drawable.ic_default_node_bar);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    public void setCurrentNode(int currentNode) {
        this.currentNode = currentNode;
    }

    public void setNodeList(List<Node> nodeList) {
        this.nodeList = nodeList;
    }

    public void setProgressListener(NodeProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    public void setNormalNodeDrawable(Drawable normalNodeDrawable) {
        this.normalNodeDrawable = normalNodeDrawable;
    }

    public void setSelectedNodeDrawable(Drawable selectedNodeDrawable) {
        this.selectedNodeDrawable = selectedNodeDrawable;
    }

    public void setProgressBarHeight(int progressBarHeight) {
        this.progressBarHeight = progressBarHeight;
    }

    public void setProgressDrawable(Drawable progressDrawable) {
        this.progressDrawable = progressDrawable;
    }

    public void setProgressPaddingHorizontal(int progressPaddingHorizontal) {
        this.progressPaddingHorizontal = progressPaddingHorizontal;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgressBar(canvas);
        drawNode(canvas);
    }

    private void drawProgressBar(Canvas canvas) {
        int length = getWidth() - progressPaddingHorizontal * 2 - (selectedNodeRadius - normalNodeRadius) * 2;
        int paddingX = (getWidth() - length) / 2;
        Rect progressDound = new Rect(paddingX, getHeight() - barMarginBottom - progressBarHeight, getWidth() - paddingX, getHeight() - barMarginBottom);
        progressDrawable.setBounds(progressDound);
        progressDrawable.draw(canvas);
    }

    private void drawNode(Canvas canvas) {
        if (nodeList == null || nodeList.size() <= 1) {
            return;
        }
        int length = getWidth() - progressPaddingHorizontal * 2 - normalNodeRadius * 2;
        int paddingX = (getWidth() - length) / 2;
        int deltaX = length / (nodeList.size() - 1);
        for (int i = 0; i < nodeList.size(); i++) {
            int centerX = paddingX + deltaX * i;
            int centerY = getHeight() - barMarginBottom - progressBarHeight / 2;
            if (i != currentNode) {
                Rect nodeBounds = new Rect(centerX - normalNodeRadius, centerY - normalNodeRadius, centerX + normalNodeRadius, centerY + normalNodeRadius);
                Drawable nodeDrawable = nodeList.get(i).getNodeDrawable() == null ? normalNodeDrawable : nodeList.get(i).getNodeDrawable();
                nodeDrawable.setBounds(nodeBounds);
                nodeDrawable.draw(canvas);
            } else {
                Rect nodeBounds = new Rect(centerX - selectedNodeRadius, centerY - selectedNodeRadius, centerX + selectedNodeRadius, centerY + selectedNodeRadius);
                selectedNodeDrawable.setBounds(nodeBounds);
                selectedNodeDrawable.draw(canvas);
            }
            mPaint.setColor(getResources().getColor(R.color.gray_87));
            mPaint.setTextSize(nodeTextSize);
            String text = nodeList.get(i).getText();
            if (text == null) {
                text = "";
            }
            float textWidth = TextUtil.getTextWidth(mPaint, text,nodeTextSize);
            float textCenterY = getHeight() - barMarginBottom - progressBarHeight - textMarginBottom;
            canvas.drawText(text, centerX - textWidth / 2, textCenterY - nodeTextSize / 2, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currentX = event.getX();
        float currentY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downTime = System.currentTimeMillis();
                startX = currentX;
                startY = currentY;
                if (progressListener != null) {
                    progressListener.onPressed();
                }
                break;
            case MotionEvent.ACTION_MOVE: {
                float moveX = currentX - startX;
                float moveY = currentY - startY;

                if (Math.abs(moveX) > MOVE_SLOP || Math.abs(moveY) > MOVE_SLOP) {
                    int node = getIndexFromPosition((int) currentX);
                    if (progressListener != null) {
                        int length = getWidth() - progressPaddingHorizontal * 2;
                        int paddingX = (getWidth() - length) / 2;
                        float percent = (currentX - paddingX) / length;
                        progressListener.onProgress(percent);
                        if (node != currentNode) {
                            progressListener.onSelectNode(node);
                        }
                    }

                    currentNode = node;
                    invalidate();
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                float moveX = currentX - startX;
                float moveY = currentY - startY;

                if (Math.abs(moveX) < MOVE_SLOP && Math.abs(moveY) < MOVE_SLOP && System.currentTimeMillis() - downTime < CLICK_TIME_THRESHOLD && onClickListener != null) {
                    onClickListener.onClick(this);
                }
                currentNode = getIndexFromPosition((int) currentX);
                if (progressListener != null) {
                    progressListener.onSelectNode(currentNode);
                }
                invalidate();
                if (progressListener != null) {
                    progressListener.onRelease();
                }
            }
            case MotionEvent.ACTION_CANCEL: {
                currentNode = getIndexFromPosition((int) currentX);
                invalidate();
                if (progressListener != null) {
                    progressListener.onRelease();
                }
                break;
            }
        }

        lastX = currentX;
        lastY = currentY;
        return true;
    }

    private int getIndexFromPosition(int x) {
        if (nodeList == null || nodeList.size() <= 1) {
            return 0;
        }
        int length = getWidth() - progressPaddingHorizontal * 2;
        int paddingX = (getWidth() - length) / 2;
        int deltaX = length / (nodeList.size() - 1);
        int index = (x - paddingX + deltaX / 2) / deltaX;
        if (index >= nodeList.size()) {
            index = nodeList.size() - 1;
        } else if (index < 0) {
            index = 0;
        }
        return index;
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface NodeProgressListener {

        public void onPressed();

        public void onRelease();

        /**
         * @param progress progress in one
         */
        public void onProgress(float progress);

        public void onSelectNode(int node);
    }

    public static class Node {
        private final Drawable nodeDrawable;
        private final String text;

        public Node(Drawable nodeDrawable, String text) {
            this.nodeDrawable = nodeDrawable;
            this.text = text;
        }

        public Drawable getNodeDrawable() {
            return nodeDrawable;
        }

        public String getText() {
            return text;
        }
    }
}
