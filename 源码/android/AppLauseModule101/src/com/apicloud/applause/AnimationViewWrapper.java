package com.apicloud.applause;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

@SuppressLint("NewApi")
class AnimationViewWrapper {
	private Rect mArea;
	private BubblingImageView mView;
	private ViewGroup mParent;

	public AnimationViewWrapper(Rect area, BubblingImageView view,
			ViewGroup parent) {
		this.mArea = area;
		this.mView = view;
		this.mParent = parent;
	}

	public void startAnimation(boolean enableScale) {
		PathEvaluator evaluator = new PathEvaluator();
		AnimatorPath path = new AnimatorPath();
		path.moveTo((float) (this.mArea.left + this.mArea.width() / 2),
				(float) (this.mArea.bottom - 50));
		path.curveTo(
				(float) (this.mArea.left + this.mArea.width() / 4 + (int) (Math
						.random() * (double) this.mArea.width() / 2.0D)),
				(float) (this.mArea.top + this.mArea.height() / 2),
				(float) (this.mArea.left + this.mArea.width() / 4 + (int) (Math
						.random() * (double) this.mArea.width() / 2.0D)),
				(float) (this.mArea.top + this.mArea.height() / 2),
				(float) (this.mArea.left + this.mArea.width() / 2),
				(float) this.mArea.top);
		PathPoint[] array = new PathPoint[path.getPoints().size()];
		ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(this.mView,
				"alpha", new float[] { 1.0F, 0.0F });
		int alphaDuration = 1800 + (int) (Math.random() * 1200.0D);
		int pathDuration = 3800 + (int) (Math.random() * 2200.0D);
		int count = this.mParent.getChildCount();
		if (count >= 20) {
			count = 20;
		}

		pathDuration = (int) ((float) pathDuration + (3000.0F - 1000.0F * (float) count / 5.0F));
		alphaAnimator.setDuration((long) alphaDuration).setInterpolator(
				new AccelerateInterpolator(2.0F));
		Object[] array1 = path.getPoints().toArray(array);
		ObjectAnimator pathAnimator = ObjectAnimator.ofObject(this.mView,
				"location", evaluator, array1);
		pathAnimator.setDuration((long) pathDuration).setInterpolator(
				new DecelerateInterpolator(1.0F));
		if (enableScale) {
			PropertyValuesHolder scaleXHolder = PropertyValuesHolder.ofFloat(
					"scaleX", new float[] { 0.5F, 1.0F });
			PropertyValuesHolder scaleYHolder = PropertyValuesHolder.ofFloat(
					"scaleY", new float[] { 0.5F, 1.0F });
			ObjectAnimator scaleAnimator = ObjectAnimator
					.ofPropertyValuesHolder(this.mView,
							new PropertyValuesHolder[] { scaleXHolder,
									scaleYHolder });
			short scaleDuration = 1000;
			scaleAnimator.setDuration((long) scaleDuration).setInterpolator(
					new OvershootInterpolator(5.0F));
			scaleAnimator.start();
		}

		pathAnimator.addListener(new AnimatorListener() {
			public void onAnimationStart(Animator animation) {
				AnimationViewWrapper.this.mParent
						.addView(AnimationViewWrapper.this.mView);
			}

			public void onAnimationEnd(Animator animation) {
				AnimationViewWrapper.this.mParent
						.removeView(AnimationViewWrapper.this.mView);
			}

			public void onAnimationCancel(Animator animation) {
				AnimationViewWrapper.this.mParent
						.removeView(AnimationViewWrapper.this.mView);
			}

			public void onAnimationRepeat(Animator animation) {
			}
		});
		pathAnimator.start();
		alphaAnimator.start();
	}
}
