package com.apicloud.applause;

class PathPoint {
 public static final int MOVE = 0;
 public static final int LINE = 1;
 public static final int CURVE = 2;
 float mX;
 float mY;
 float mControl0X;
 float mControl0Y;
 float mControl1X;
 float mControl1Y;
 int mOperation;

 private PathPoint(int operation, float x, float y) {
     this.mOperation = operation;
     this.mX = x;
     this.mY = y;
 }

 private PathPoint(float c0X, float c0Y, float c1X, float c1Y, float x, float y) {
     this.mControl0X = c0X;
     this.mControl0Y = c0Y;
     this.mControl1X = c1X;
     this.mControl1Y = c1Y;
     this.mX = x;
     this.mY = y;
     this.mOperation = 2;
 }

 public static PathPoint lineTo(float x, float y) {
     return new PathPoint(1, x, y);
 }

 public static PathPoint curveTo(float c0X, float c0Y, float c1X, float c1Y, float x, float y) {
     return new PathPoint(c0X, c0Y, c1X, c1Y, x, y);
 }

 public static PathPoint moveTo(float x, float y) {
     return new PathPoint(0, x, y);
 }
}
