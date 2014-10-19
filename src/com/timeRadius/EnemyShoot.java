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

public class EnemyShoot extends GeneralEnemy{
	private static int LEVEL1 = 30;
	private static int LEVEL2 = 25;
	private static int LEVEL3 = 10;
	
	int bulletInterval;

	//int maxbulletcount = 5;
	int bulletCount = 0;
	double bulletTimer = 0;
	ArrayList<Bullet> bulletList = new ArrayList<Bullet>(0);
	
	public EnemyShoot(int x, int y, int size, int speed, Panel gameEnv){
		super(x,y,size,speed,gameEnv);
		
		playerData = (Player)gameEnv.objList.get(0);
		moveToX = Math.random()*speed;
		moveToY = Math.random()*speed;
		score = 1;
	}
	
	@Override
	public void deathProcedure(){
		
		if(	delaydeath == 0 &&
		   	getDistanceTo(playerData.xPos,playerData.yPos) - size < playerData.size &&
		   	playerData.health > 0){
				playerData.delay = 25;
		}
		
		super.deathProcedure();
		if(bulletList.size() > 0){
			delete = false;
		}
	}
	
	public void habbitAIProcedure(){
		//int shootsig = (int)(10*Math.random() * timeRatio);

		if(levelHolder != gameEnv.level){
			if(gameEnv.level == 1){
				bulletCount = 1;
				bulletInterval = LEVEL1;
			} else if(gameEnv.level == 2){
				bulletCount = 2;
				bulletInterval = LEVEL2;
			} else if(gameEnv.level >= 3){
				bulletCount = 3;
				bulletInterval = LEVEL3;
			}
			levelHolder = gameEnv.level;
		}
		
		if(getDistanceTo(playerData.xPos,playerData.yPos) + size < playerData.timeRadius + this.size){
			timeRatio = 0.15;
		} else {
			timeRatio = 1;
		}
		
		bulletTimer = (int)(bulletTimer*timeRatio);
		
		if(	delaydeath == 0 && 
			//bulletcount < maxbulletcount && 
			bulletTimer >= bulletInterval &&
			getDistanceTo(playerData.xPos,playerData.yPos) + size > playerData.timeRadius + this.size + 75){
				for(int i = 0; i < bulletCount; i++){
					bulletList.add(new Bullet((int)xPos,(int)yPos,3,8,gameEnv,(int)(playerData.xPos + i*2*Math.random()), (int)(playerData.yPos + i*2*Math.random())));
				}
				bulletTimer = 0;
		} else {
			bulletTimer += timeRatio;
		}
		
		/*
		if(bulletTimer >= bulletInterval){
			bulletTimer = 0;
		} else {
			bulletTimer++;
		}
		*/
		//}

		for(int i = 0; i < bulletList.size(); i++){
			if(bulletList.get(i).delete){
				bulletList.remove(i);
				//bulletcount--;
			}
			else{
				bulletList.get(i).updateData();
			}
		}
	}
	
	public void movementProcedure(){
		//movement
		if(xPos > gameEnv.screenW || xPos < 0)
		{
			moveToX = -1*moveToX;
		}

		if(yPos > gameEnv.screenH || yPos < 0)
		{
			moveToY = -1*moveToY;
		}
		
		moveTo(xPos+moveToX,yPos+moveToY);
	}
	
	public void drawObj(Canvas c){
		Paint brush = new Paint();
		/*if(getDistanceTo(playerData.xPos,playerData.yPos) + size < playerData.timeRadius){
			brush.setColor(Color.YELLOW);
		} else {*/
			brush.setColor(Color.GREEN);
		//}
		
		if(delaydeath == 0){
			c.drawCircle((float)xPos,(float)yPos,size,brush);
		} else if(delaydeath <= 100){
			brush.setTextSize(40);
			brush.setColor(Color.BLUE);
			c.drawText("+"+Integer.toString(comboHolder), (int)xPos, (int)yPos, brush);
		}
		
		for(int i = 0; i < bulletList.size(); i++){
			bulletList.get(i).drawObj(c);
		}
		
	}
	
	public void passTouchData(MotionEvent event){
		//passPlayerPos(event.getX(), event.getY());
	}
	
}
