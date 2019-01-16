package com.apicloud.applause;

import android.animation.TypeEvaluator;
import android.annotation.SuppressLint;

@SuppressLint("NewApi")
class PathEvaluator implements TypeEvaluator<PathPoint> {
	PathEvaluator() {
	}

	public PathPoint evaluate(float t, PathPoint startValue, PathPoint endValue) {
		float x;
		float y;
		if (endValue.mOperation == 2) {
			float oneMinusT = 1.0F - t;
			x = oneMinusT * oneMinusT * oneMinusT * startValue.mX + 3.0F
					* oneMinusT * oneMinusT * t * endValue.mControl0X + 3.0F
					* oneMinusT * t * t * endValue.mControl1X + t * t * t
					* endValue.mX;
			y = oneMinusT * oneMinusT * oneMinusT * startValue.mY + 3.0F
					* oneMinusT * oneMinusT * t * endValue.mControl0Y + 3.0F
					* oneMinusT * t * t * endValue.mControl1Y + t * t * t
					* endValue.mY;
		} else if (endValue.mOperation == 1) {
			x = startValue.mX + t * (endValue.mX - startValue.mX);
			y = startValue.mY + t * (endValue.mY - startValue.mY);
		} else {
			x = endValue.mX;
			y = endValue.mY;
		}

		return PathPoint.moveTo(x, y);
	}
}
