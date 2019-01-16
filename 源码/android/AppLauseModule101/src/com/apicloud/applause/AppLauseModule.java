package com.apicloud.applause;

import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uzmap.pkg.uzcore.UZCoreUtil;
import com.uzmap.pkg.uzcore.UZResourcesIDFinder;
import com.uzmap.pkg.uzcore.UZWebView;
import com.uzmap.pkg.uzcore.uzmodule.UZModule;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;
import com.uzmap.pkg.uzkit.UZUtility;


public class AppLauseModule extends UZModule {

	private static int mApplauseNum = 0;
	
	private static int mX = 0;
	private static int mY = 0;
	private static int mW = 60;
	private static int mH = 60;
	public View mAppluseView ;
	private BubblingView mBubblingView;
	private ImageView mImageView;
	private TextView mTextView;
	public static String mPlaceholderImg;
	public static boolean mIsShowNum;
	private static String[] mIamgesArray = null;
	private static int[] drawableIds = new int[] { UZResourcesIDFinder.getResDrawableID("applause_1"),
		UZResourcesIDFinder.getResDrawableID("applause_2"), UZResourcesIDFinder.getResDrawableID("applause_3"), 
		UZResourcesIDFinder.getResDrawableID("applause_4"),UZResourcesIDFinder.getResDrawableID("applause_5"), 
		UZResourcesIDFinder.getResDrawableID("applause_6"), UZResourcesIDFinder.getResDrawableID("applause_7")};
	private Random random = new Random();
	
	public AppLauseModule(UZWebView webView) {
		super(webView);
	}
	
	@SuppressWarnings("deprecation")
	public void jsmethod_showAppLause(final UZModuleContext moduleContext){
		
		JSONObject rectObj = moduleContext.optJSONObject("rect");
		if (rectObj != null) {
			if (!rectObj.isNull("x")) {
				mX = rectObj.optInt("x");
			}else{
				mX = 0;
			}
			if (!rectObj.isNull("y")) {
				mY = rectObj.optInt("y");
			}else{
				mY = 0;
			}

			if (!rectObj.isNull("w")) {
				mW = rectObj.optInt("w");
			}else{
				mW = 60;
			}
			if (!rectObj.isNull("h")) {
				mH = rectObj.optInt("h");
			}else{
				mH = 60;
			}
		}else{
			mX = 0;
			mY = 0;
			mW = 60;
			mH = 60;
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
		
		int activity_player = UZResourcesIDFinder.getResLayoutID("applause_ly_periscope");
		mAppluseView = View.inflate(mContext, activity_player,null);
		
		mBubblingView = (BubblingView)mAppluseView.findViewById(UZResourcesIDFinder.getResIdID("bubbling_view"));
		mTextView = (TextView)mAppluseView.findViewById(UZResourcesIDFinder.getResIdID("bubbling_text"));
		mImageView =(ImageView)mAppluseView.findViewById(UZResourcesIDFinder.getResIdID("applause_btn"));
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(UZCoreUtil.dipToPix(mW), UZCoreUtil.dipToPix(mH));
		params.gravity = Gravity.CENTER_HORIZONTAL;
		mImageView.setLayoutParams(params);
	    
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
            	
            	if(mIamgesArray==null){
                	mBubblingView.addBubblingItem(drawableIds[random.nextInt(drawableIds.length)]);
                }else{
                	int t = random.nextInt(mIamgesArray.length);
                	final String pic = mIamgesArray[t];
                	
                	if (pic.startsWith("file:///android_asset/")) {
            			Bitmap bitMap = UZUtility.getLocalImage(pic);
            			if(bitMap!=null){
            				mBubblingView.addBubblingItem(new BitmapDrawable(bitMap));
            			}else{
            				int applause_1 = UZResourcesIDFinder.getResDrawableID("applause_1");
            				mBubblingView.addBubblingItem(mContext.getResources().getDrawable(applause_1));
            			}
            		}else if (pic.startsWith("http://")) {
            			new Thread(new Runnable() {
            				@Override
            				public void run() {
            					try {
            						byte[] data = Util.getImage(pic);
            						int length = data.length; 
            						final Bitmap bitMap = BitmapFactory.decodeByteArray(data, 0, length);
            						mBubblingView.addBubblingItem(new BitmapDrawable(bitMap));
            					} catch (Exception e) {
            						e.printStackTrace();
            						int applause_1 = UZResourcesIDFinder.getResDrawableID("applause_1");
            	    				mBubblingView.addBubblingItem(mContext.getResources().getDrawable(applause_1));
            					}
            				}
            			}).start();
            		}else{
            			Bitmap bitMap = UZUtility.getLocalImage(pic);
            			if(bitMap!=null){
            				mBubblingView.addBubblingItem(new BitmapDrawable(bitMap));
            			}else{
            				int applause_1 = UZResourcesIDFinder.getResDrawableID("applause_1");
            				mBubblingView.addBubblingItem(mContext.getResources().getDrawable(applause_1));
            			}
            		}
                }
                
                JSONObject ret = new JSONObject();
        		try {
        			ret.put("eventType", "click");
        			ret.put("applauseNum", mApplauseNum);
        		} catch (Exception e) {
        		}
        		
        		moduleContext.success(ret, false);
            }
        });
		
		RelativeLayout.LayoutParams rlp1 = new RelativeLayout.LayoutParams(mW*2, mY+20);
		rlp1.leftMargin = mX/2;
		rlp1.topMargin = 0;
		insertViewToCurWindow(mAppluseView, rlp1, mFixedOn ,mFixed);
		
		
		JSONObject ret = new JSONObject();
		try {
			ret.put("eventType", "show");
			ret.put("applauseNum", mApplauseNum);
		} catch (Exception e) {
		}
		
		moduleContext.success(ret, false);
		
	}
	
	@SuppressWarnings("deprecation")
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
    	
		if(mIamgesArray==null){
        	mBubblingView.addBubblingItem(drawableIds[random.nextInt(drawableIds.length)]);
        }else{
        	int t = random.nextInt(mIamgesArray.length);
        	final String pic = mIamgesArray[t];
        	
        	if (pic.startsWith("file:///android_asset/")) {
    			Bitmap bitMap = UZUtility.getLocalImage(pic);
    			if(bitMap!=null){
    				mBubblingView.addBubblingItem(new BitmapDrawable(bitMap));
    			}else{
    				int applause_1 = UZResourcesIDFinder.getResDrawableID("applause_1");
    				mBubblingView.addBubblingItem(mContext.getResources().getDrawable(applause_1));
    			}
    		}else if (pic.startsWith("http://")) {
    			new Thread(new Runnable() {
    				@Override
    				public void run() {
    					try {
    						byte[] data = Util.getImage(pic);
    						int length = data.length; 
    						final Bitmap bitMap = BitmapFactory.decodeByteArray(data, 0, length);
    						mBubblingView.addBubblingItem(new BitmapDrawable(bitMap));
    					} catch (Exception e) {
    						e.printStackTrace();
    						int applause_1 = UZResourcesIDFinder.getResDrawableID("applause_1");
    	    				mBubblingView.addBubblingItem(mContext.getResources().getDrawable(applause_1));
    					}
    				}
    			}).start();
    		}else{
    			Bitmap bitMap = UZUtility.getLocalImage(pic);
    			if(bitMap!=null){
    				mBubblingView.addBubblingItem(new BitmapDrawable(bitMap));
    			}else{
    				int applause_1 = UZResourcesIDFinder.getResDrawableID("applause_1");
    				mBubblingView.addBubblingItem(mContext.getResources().getDrawable(applause_1));
    			}
    		}
        }
        
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
		if(mAppluseView!=null){
			removeViewFromCurWindow(mAppluseView);
		}
		mBubblingView = null;
		mImageView = null;
		mTextView = null;
		mApplauseNum = 0;
		mPlaceholderImg = null;
	}
}
