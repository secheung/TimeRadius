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

public class Bullet extends DynamicObject{

	Player playerData;
	double dirX;
	double dirY;
	
	public Bullet(int x, int y, int size, int speed, Panel gameEnv, int toX, int toY){
		super(x,y,size,speed,gameEnv);
		
		playerData = (Player)gameEnv.objList.get(0);
		
		dirX = toX - xPos;
		dirY = toY - yPos;
		
		moveToX = dirX;
		moveToY = dirY;
	}

	public void updateData(){
		
		if(xPos < 0 || xPos > gameEnv.screenW || yPos < 0 || yPos > gameEnv.screenH){
			delete = true;
		}
		
		if(getDistanceTo(playerData.xPos,playerData.yPos) < playerData.timeRadius){
			timeRatio = 0.05;
			if(playerData.health == 0 || playerData.lives == 0){
				delete = true;
			}
		} else {
			timeRatio = 1;
		}
		
		if(!delete && getDistanceTo(playerData.xPos,playerData.yPos) < playerData.size){
			playerData.deathReset();
			delete = true;
		}
		
		moveToX += moveToX;
		moveToY += moveToY;
		
		this.moveTo(moveToX,moveToY);
	}

	public void drawObj(Canvas c){
		Paint brush = new Paint();
		brush.setColor(Color.RED);
		c.drawCircle((float)xPos,(float)yPos,size,brush);

	}

	public void passTouchData(MotionEvent event){}
}
