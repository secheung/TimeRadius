 package com.timeRadius;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;

import java.lang.Object;

abstract class DynamicObject{
	double xPos;
	double yPos;
	double speed;
	double timeRatio;
	int size;
	double angle;
	
	double moveToX;
	double moveToY;
	
	int screenW;
	int screenH;
	
	double delay;
	
	boolean delete;
	
	Panel gameEnv;
	
	public DynamicObject(int x, int y, int size, double speed, Panel gameEnv){
		this.xPos = x;
		this.yPos = y;
		this.speed = speed;
		this.angle = 0;
		this.size = size;
		this.timeRatio = 1;
		this.delete = false;
		
		this.gameEnv = gameEnv;
	}
	
	public double getDistanceTo(double pointX, double pointY){
		return Math.sqrt(Math.pow(xPos - pointX,2) + Math.pow(yPos - pointY,2));
	}
	
	public void setFaceToAngle(double pointX,double pointY){
		this.angle = getFaceToAngle(pointX,pointY);
	}
	
	public void moveTo(double pointX, double pointY){
		if(pointX == xPos && pointY == yPos)return;
		
		double angle = getFaceToAngle(pointX,pointY);
		
		xPos = xPos + speed*timeRatio*Math.cos(angle);
		yPos = yPos + speed*timeRatio*Math.sin(angle);
	}
	
	public double getFaceToAngle(double pointX, double pointY){
		double diffX = pointX - xPos;
		double diffY = pointY - yPos;
		double faceToAngle = 0;

		faceToAngle = Math.atan(diffY/diffX);
		
		if(pointX < xPos){
			if(pointY < yPos){
				faceToAngle = faceToAngle + Math.PI;
			} else {
				faceToAngle = faceToAngle - Math.PI;
			}
		}
		
		return faceToAngle;
	}
	
	abstract public void drawObj(Canvas c);
	abstract public void passTouchData(MotionEvent event);
	abstract public void updateData();
	
}
