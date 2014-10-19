package com.timeRadius;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;

import java.lang.Object;
import java.util.ArrayList;
import android.graphics.*;

public class EnemyRadius extends GeneralEnemy{
	private static int MIN_RADIUS_SIZE = 60;
	private static int HOLESIZE = 15;
	private static int LEVEL1 = 55;
	private static int LEVEL2 = 60;
	private static int LEVEL3 = 65;

	int radiusSize;
	double angleHole;
	double angleCheck;
	double angleStance;
	int angleChange = 10;
	
	boolean entered = false;
	
	
	public EnemyRadius(int x, int y, int size, int speed, Panel gameEnv){
		super(x,y,size,speed,gameEnv);

		playerData = (Player)gameEnv.objList.get(0);
		moveToX = Math.random()*speed;
		moveToY = Math.random()*speed;
		score = 1;
		angleHole = 0;
	}

	@Override
	public void deathProcedure(){

		if(	delaydeath == 0 &&
		   getDistanceTo(playerData.xPos,playerData.yPos) - size < playerData.size &&
		   playerData.health > 0){
			playerData.delay = 25;
		}

		super.deathProcedure();
	}

	public void habbitAIProcedure(){
		
		if(getDistanceTo(playerData.xPos,playerData.yPos) + size < playerData.timeRadius + this.size){
			timeRatio = 0.1;
		} else {
			timeRatio = 1;
		}
		
		if(levelHolder != gameEnv.level){
			if(gameEnv.level == 1){
				radiusSize = (int)(Math.random()*LEVEL1 + MIN_RADIUS_SIZE);
				angleChange = 10;
				speed = (int)(7*Math.random() + playerData.speed);
			} else if(gameEnv.level == 2){
				radiusSize = (int)(Math.random()*LEVEL2 + MIN_RADIUS_SIZE);
				angleChange = 10;
				speed += Math.random()*2;
			} else if(gameEnv.level >= 3){
				radiusSize = (int)(Math.random()*LEVEL3 + MIN_RADIUS_SIZE);
				angleChange = 10;//20
				speed += Math.random()*5;
			}
			levelHolder = gameEnv.level;
		}
		
		
		angleCheck = getFaceToAngle(playerData.xPos,playerData.yPos)*(180/Math.PI);
		if((-90 < angleCheck && angleCheck < 0) || (-270 < angleCheck && angleCheck < -180)){
			angleCheck *= -1;
		} else if((180 < angleCheck && angleCheck < 270) || (0 < angleCheck && angleCheck < 90)){
			angleCheck = 360 - angleCheck;
		}
		angleCheck = 360 - angleCheck;
		
		if(	getDistanceTo(playerData.xPos,playerData.yPos) - playerData.size + 5 < this.radiusSize &&
			playerData.lives > 0){
			
			entered = true;
			if(	delaydeath == 0 &&
			   	angleCheck > angleHole &&
			   	angleCheck < angleHole + HOLESIZE){
					playerData.deathReset();
					delete = true;
			}
			
			angleHole += angleChange*timeRatio;
			
		} else {
			entered = false;
			angleHole = angleCheck + 180;
		}
		
		if(angleHole >= 360){
			angleHole -= 360;
		}
	}

	public void movementProcedure(){
		//movement
		if(xPos > gameEnv.screenW || xPos < 0){
			moveToX = -1*moveToX;
		}

		if(yPos > gameEnv.screenH || yPos < 0){
			moveToY = -1*moveToY;
		}

		if(getDistanceTo(playerData.xPos,playerData.yPos) + size < playerData.timeRadius + this.size){
			timeRatio = 0.75/speed;
		} else {
			timeRatio = 1;
		}

		if(entered){
			moveTo(playerData.xPos,playerData.yPos);
		} else {
			moveTo(xPos+moveToX,yPos+moveToY);
		}

	}

	public void drawObj(Canvas c){
		Paint brush = new Paint();
		

		if(delaydeath == 0){
			brush.setColor(Color.GRAY);
			brush.setStyle(Paint.Style.STROKE);
			c.drawCircle((float)xPos,(float)yPos,radiusSize,brush);
			
			brush.setStyle(Paint.Style.FILL);
			c.drawCircle((float)xPos,(float)yPos,size,brush);
			
			if(entered){
				brush.setColor(Color.RED);
				c.drawArc(new RectF((int)(xPos - radiusSize),(int)(yPos - radiusSize),(int)(xPos + radiusSize),(int)(yPos + radiusSize)),(int)angleHole,HOLESIZE,true,brush);
				//rev sweep//c.drawArc(new RectF((int)(xPos - radiusSize),(int)(yPos - radiusSize),(int)(xPos + radiusSize),(int)(yPos + radiusSize)),(int)(360 - angleHole),HOLESIZE,true,brush);
			} else {
				brush.setColor(Color.GRAY);
				c.drawArc(new RectF((int)(xPos - radiusSize),(int)(yPos - radiusSize),(int)(xPos + radiusSize),(int)(yPos + radiusSize)),(int)angleHole,1,true,brush);
			}
			//c.drawText(Double.toString(angleCheck),(int)xPos,(int)yPos,brush);
		} else if(delaydeath <= 100) {
			brush.setTextSize(40);
			brush.setColor(Color.BLUE);
			c.drawText("+"+Integer.toString(comboHolder), (int)xPos, (int)yPos, brush);
		}

	}

	public void passTouchData(MotionEvent event){
	}

}
