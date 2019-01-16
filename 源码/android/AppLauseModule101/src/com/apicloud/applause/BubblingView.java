package com.apicloud.applause;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.uzmap.pkg.uzcore.UZCoreUtil;
import com.uzmap.pkg.uzcore.UZResourcesIDFinder;

public class BubblingView extends FrameLayout {
    public static final int OVER_MODE_DISCARD = 1;
    public static final int OVER_MODE_DELAY = 2;
    private int mDelay;
    private int mMaxChildCount;
    private int mOverMode;
    Handler mHandler = new Handler(Looper.getMainLooper());

    public BubblingView(Context context) {
        super(context);
    }

    public BubblingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        int[] AlivcBubblingView = { UZResourcesIDFinder.getResIdID("max_child_count"), UZResourcesIDFinder.getResIdID("over_mode"), 
        		UZResourcesIDFinder.getResIdID("delay") };
        
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, AlivcBubblingView, 0, 0);

        try {
            this.mMaxChildCount = typedArray.getInt(UZResourcesIDFinder.getResIdID("AlivcBubblingView_max_child_count"), 100);
            this.mOverMode |= typedArray.getInt(UZResourcesIDFinder.getResIdID("AlivcBubblingView_over_mode"), 1);
            this.mDelay = typedArray.getInt(UZResourcesIDFinder.getResIdID("AlivcBubblingView_delay"), 500);
        } finally {
            typedArray.recycle();
        }

    }

    public BubblingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int[] AlivcBubblingView = { UZResourcesIDFinder.getResIdID("max_child_count"), UZResourcesIDFinder.getResIdID("over_mode"), 
        		UZResourcesIDFinder.getResIdID("delay") };
        
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, AlivcBubblingView, defStyleAttr, 0);

        try {
            this.mMaxChildCount = typedArray.getInt(UZResourcesIDFinder.getResIdID("AlivcBubblingView_max_child_count"), 100);
            this.mOverMode |= typedArray.getInt(UZResourcesIDFinder.getResIdID("AlivcBubblingView_over_mode"), 1);
            this.mDelay = typedArray.getInt(UZResourcesIDFinder.getResIdID("AlivcBubblingView_delay"), 500);
        } finally {
            typedArray.recycle();
        }

    }

    public void addBubblingItem(final Drawable drawable) {
        Message msg;
        if(this.getChildCount() < this.mMaxChildCount) {
            msg = Message.obtain(this.mHandler, new Runnable() {
                public void run() {
                    BubblingImageView imageView = new BubblingImageView(BubblingView.this.getContext());
                    imageView.setImageDrawable(drawable);
                    imageView.setLayoutParams(new LayoutParams(UZCoreUtil.dipToPix(35), UZCoreUtil.dipToPix(35)));
                    Rect rect = new Rect();
                    BubblingView.this.getDrawingRect(rect);
                    AnimationViewWrapper wrapper = new AnimationViewWrapper(rect, imageView, BubblingView.this);
                    wrapper.startAnimation(true);
                }
            });
            this.mHandler.sendMessage(msg);
        } else if((this.mOverMode & 2) != 0) {
            msg = Message.obtain(this.mHandler, new Runnable() {
                public void run() {
                    BubblingView.this.addBubblingItem(drawable);
                }
            });
            this.mHandler.sendMessageDelayed(msg, (long)this.mDelay);
        }
    }

    public void addBubblingItem(int resID) {
        Drawable drawable = this.getResources().getDrawable(resID);
        this.addBubblingItem(drawable);
    }

    public void setMaxChildCount(int maxChildCount) {
        if(maxChildCount > 400) {
            Log.e("BubblingView", "max child count over the allowed value");
            this.mMaxChildCount = 400;
        } else if(maxChildCount > 0) {
            this.mMaxChildCount = maxChildCount;
        } else {
            this.mMaxChildCount = 1;
        }

    }

    public void setOverMode(int value) {
        this.mOverMode |= value;
    }
}
