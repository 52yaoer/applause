package com.apicloud.applause;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uzmap.pkg.uzcore.UZResourcesIDFinder;
import com.uzmap.pkg.uzcore.UZWebView;
import com.uzmap.pkg.uzcore.uzmodule.UZModule;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;
import com.uzmap.pkg.uzkit.UZUtility;


public class AppLauseModule extends UZModule {

	private static int mApplauseNum = 0;
	
	private static int mX = 0;
	private static int mY = 0;
	public static int mW = 60;
	public static int mH = 60;
	public View mAppluseView ;
	public View mAppluseBtn ;
	private HeartLayout mBubblingView;
	private ImageView mImageView;
	private TextView mTextView;
	public static String mPlaceholderImg;
	public static boolean mIsShowNum;
	private static String[] mIamgesArray = null;
	
	public AppLauseModule(UZWebView webView) {
		super(webView);
	}
	
	@SuppressWarnings("deprecation")
	public void jsmethod_showAppLause(final UZModuleContext moduleContext){
		JSONObject rectObj = moduleContext.optJSONObject("rect");
		if (rectObj != null) {
			mX = rectObj.optInt("x", 0);
			mY = rectObj.optInt("y", 0);
			mW = rectObj.optInt("w", 60);
			mH = rectObj.optInt("h", 60);
		}
		
		mApplauseNum = moduleContext.optInt("applauseNum", 0);
		mPlaceholderImg = moduleContext.optString("placeholderImg");
		mIsShowNum = moduleContext.optBoolean("isShowNum",false);
		if(!TextUtils.isEmpty(mPlaceholderImg)){
			mPlaceholderImg = makeRealPath(mPlaceholderImg);
		}
		
		if(TextUtils.isEmpty(mPlaceholderImg)){
			 JSONObject ret = new JSONObject();
			 JSONObject err = new JSONObject();
     		try {
     			ret.put("status", false);
     			err.put("msg", "显示图片不能为空!");
     		} catch (Exception e) {
     		}
     		moduleContext.error(ret, err, false);
     		
     		return;
		}
		
		JSONArray iamgesArray = moduleContext.optJSONArray("iamges");
		
		if (iamgesArray != null) {
			mIamgesArray = new String[iamgesArray.length()];
			for (int i = 0; i < iamgesArray.length(); i++) {
				try {
					String temp = iamgesArray.getString(i);
					mIamgesArray[i] = makeRealPath(temp);
				} catch (Exception e) {
				}
			}
		}
		
		String mFixedOn=moduleContext.optString("fixedOn");
		//fixed参数标识UI模块是否跟随网页滚动
		Boolean mFixed = moduleContext.optBoolean("fixed", false);
		
		int periscopeBtn = UZResourcesIDFinder.getResLayoutID("applause_ly_periscope_btn");
		mAppluseBtn = View.inflate(mContext, periscopeBtn,null);
		mTextView = (TextView)mAppluseBtn.findViewById(UZResourcesIDFinder.getResIdID("bubbling_text"));
		mImageView =(ImageView)mAppluseBtn.findViewById(UZResourcesIDFinder.getResIdID("applause_btn"));
		
		int periscopeView = UZResourcesIDFinder.getResLayoutID("applause_ly_periscope_view");
		mAppluseView = View.inflate(mContext, periscopeView,null);
		mBubblingView = (HeartLayout)mAppluseView.findViewById(UZResourcesIDFinder.getResIdID("bubbling_view"));
		mBubblingView.setIamgesArray(mIamgesArray);
		
		if(TextUtils.isEmpty(mPlaceholderImg)){
			int applause_1 = UZResourcesIDFinder.getResDrawableID("applause");
			mImageView.setImageResource(applause_1);
		}else{
			if (mPlaceholderImg.startsWith("file:///android_asset/")) {
    			Bitmap bitMap = UZUtility.getLocalImage(mPlaceholderImg);
    			if(bitMap!=null){
    				mImageView.setBackgroundDrawable(new BitmapDrawable(bitMap));
    				mImageView.setImageBitmap(bitMap);
    			}else{
    				int applause_1 = UZResourcesIDFinder.getResDrawableID("applause");
    				mImageView.setImageResource(applause_1);
    			}
    		}else if (mPlaceholderImg.startsWith("http://")) {
    			new Thread(new Runnable() {
    				@Override
    				public void run() {
    					try {
    						byte[] data = Util.getImage(mPlaceholderImg);
    						int length = data.length; 
    						final Bitmap bitMap = BitmapFactory.decodeByteArray(data, 0, length);
    						mImageView.setImageBitmap(bitMap);
    					} catch (Exception e) {
    						e.printStackTrace();
    						int applause_1 = UZResourcesIDFinder.getResDrawableID("applause");
    						mImageView.setImageResource(applause_1);
    					}
    				}
    			}).start();
    		}else{
    			Bitmap bitMap = UZUtility.getLocalImage(mPlaceholderImg);
    			if(bitMap!=null){
    				mImageView.setImageBitmap(bitMap);
    			}else{
    				int applause = UZResourcesIDFinder.getResDrawableID("applause");
    				mImageView.setImageResource(applause);
    			}
    		}
		}
		
		if(mIsShowNum){
			mTextView.setText(String.valueOf(mApplauseNum));
			mTextView.setVisibility(View.VISIBLE);
		}else{
			mTextView.setVisibility(View.GONE);
		}
		mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	if(mApplauseNum>999){
            		mApplauseNum++;
            		if(mIsShowNum){
            			mTextView.setText("999+");
            			mTextView.setVisibility(View.VISIBLE);
            		}else{
            			mTextView.setVisibility(View.GONE);
            		}
            	}else{
            		mApplauseNum++;
            		if(mIsShowNum){
            			mTextView.setText(String.valueOf(mApplauseNum));
            			mTextView.setVisibility(View.VISIBLE);
            		}else{
            			mTextView.setVisibility(View.GONE);
            		}
            	}
            	mBubblingView.addFavor();
                
                JSONObject ret = new JSONObject();
        		try {
        			ret.put("eventType", "click");
        			ret.put("applauseNum", mApplauseNum);
        		} catch (Exception e) {
        		}
        		
        		moduleContext.success(ret, false);
            }
        });
		
		
		RelativeLayout.LayoutParams rlpView = new RelativeLayout.LayoutParams(mW*2, mY);
		rlpView.leftMargin = mX - mW/2 ;
		rlpView.topMargin = 0;
		insertViewToCurWindow(mAppluseView, rlpView, mFixedOn ,mFixed);
		
		RelativeLayout.LayoutParams rlpBtn = new RelativeLayout.LayoutParams(mW, mH);
		rlpBtn.leftMargin = mX;
		rlpBtn.topMargin = mY;
		insertViewToCurWindow(mAppluseBtn, rlpBtn, mFixedOn ,mFixed);
		
		
		JSONObject ret = new JSONObject();
		try {
			ret.put("eventType", "show");
			ret.put("applauseNum", mApplauseNum);
		} catch (Exception e) {
		}
		
		moduleContext.success(ret, false);
		
	}
	
	public void jsmethod_clickAppLause(final UZModuleContext moduleContext){
		if(mBubblingView==null){
			JSONObject ret = new JSONObject();
			 JSONObject err = new JSONObject();
    		try {
    			ret.put("status", false);
    			err.put("msg", "请执行鼓掌初始化接口!");
    		} catch (Exception e) {
    		}
    		moduleContext.error(ret, err, false);
    		
    		return;
		}
		
		
		if(mApplauseNum>999){
    		mApplauseNum++;
    		if(mIsShowNum){
    			mTextView.setText("999+");
    			mTextView.setVisibility(View.VISIBLE);
    		}else{
    			mTextView.setVisibility(View.GONE);
    		}
    	}else{
    		mApplauseNum++;
    		if(mIsShowNum){
    			mTextView.setText(String.valueOf(mApplauseNum));
    			mTextView.setVisibility(View.VISIBLE);
    		}else{
    			mTextView.setVisibility(View.GONE);
    		}
    	}
    	
		mBubblingView.addFavor();
        
        JSONObject ret = new JSONObject();
		try {
			ret.put("eventType", "click");
			ret.put("applauseNum", mApplauseNum);
		} catch (Exception e) {
		}
		
		moduleContext.success(ret, false);
		
	}
	
	
	@Override
	protected void onClean() {
		if(mAppluseBtn!=null){
			removeViewFromCurWindow(mAppluseBtn);
		}
		if(mAppluseView!=null){
			removeViewFromCurWindow(mAppluseView);
		}
		mAppluseBtn = null;
		mAppluseView = null;
		mImageView = null;
		mTextView = null;
		mApplauseNum = 0;
		mPlaceholderImg = null;
	}
}
