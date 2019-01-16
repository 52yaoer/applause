package com.apicloud.applause;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

class BubblingImageView extends ImageView {
    public BubblingImageView(Context context) {
        super(context);
    }

    public BubblingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BubblingImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLocation(PathPoint pathPoint) {
        this.setX(pathPoint.mX);
        this.setY(pathPoint.mY);
    }
}
