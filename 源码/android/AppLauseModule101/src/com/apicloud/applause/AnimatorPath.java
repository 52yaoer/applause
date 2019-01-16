package com.apicloud.applause;

import java.util.ArrayList;
import java.util.Collection;

class AnimatorPath {
 ArrayList<PathPoint> mPoints = new ArrayList<PathPoint>();

 AnimatorPath() {
 }

 public void moveTo(float x, float y) {
     this.mPoints.add(PathPoint.moveTo(x, y));
 }

 public void lineTo(float x, float y) {
     this.mPoints.add(PathPoint.lineTo(x, y));
 }

 public void curveTo(float c0X, float c0Y, float c1X, float c1Y, float x, float y) {
     this.mPoints.add(PathPoint.curveTo(c0X, c0Y, c1X, c1Y, x, y));
 }

 public Collection<PathPoint> getPoints() {
     return this.mPoints;
 }
}
