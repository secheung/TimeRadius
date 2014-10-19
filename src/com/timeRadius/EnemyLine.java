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

public class EnemyLine extends GeneralEnemy{
	int HOR = 1;
	int VER = 2;
	//int bulletInterval = 100;
	
	int lineEnd1;
	int lineEnd2;
	int initSpeed;
	int type;
	
	int attack;
	
	//ArrayList<Bullet> bulletList = new ArrayList<Bullet>(0);
	double bulletTimer;

	public EnemyLine(int x, int y, int size, int speed, Panel gameEnv){
		super(x,y,size,speed,gameEnv);
		
		playerData = (Player)gameEnv.objList.get(0);
		//moveToX = Math.random()*speed;
		//moveToY = Math.random()*speed;
		score = 1;
		
		type = (int)(Math.random()*2 + 1);
		initSpeed = speed;
		
		if(type == VER){
			lineEnd1 = 0;
			lineEnd2 = gameEnv.screenH;
			moveToX = speed;
			moveToY = 0;
		} else if(type == HOR){
			lineEnd1 = 0;
			lineEnd2 = gameEnv.screenW;
			moveToX = 0;
			moveToY = speed;
		}
	}

	@Override
	public void deathProcedure(){

		if(	delaydeath == 0 &&
			getDistanceTo(playerData.xPos,playerData.yPos) - size < playerData.size &&
		   	playerData.health > 0 &&
		   	Math.abs(attack) != 1){
				playerData.delay = 25;
		}
		
		if(Math.abs(attack) == 1){
			return;
		}

		super.deathProcedure();
		
		/*
		if(bulletList.size() > 0){
			delete = false;
		}
		*/
		
	}

	public void habbitAIProcedure(){
		if(levelHolder != gameEnv.level){
			if(gameEnv.level == 1){
				//bulletInterval = 100;
			} else if(gameEnv.level == 2){
				//bulletInterval = 50;
			} else if(gameEnv.level >= 3){
				//bulletInterval = 10;
			}
			levelHolder = gameEnv.level;
		}
		
		if(getDistanceTo(playerData.xPos,playerData.yPos) + size < playerData.timeRadius + this.size){
			timeRatio = 0.05;
		} else {
			timeRatio = 1;
		}
		
		if(type == HOR){
			if(playerData.yPos < yPos + size && playerData.yPos > yPos - size){
				attack = 1;
			} else {
				attack = 2;
			}
		} else if(type == VER){
			if(playerData.xPos < xPos + size && playerData.xPos > xPos - size){
				attack = -1;
			} else {
				attack = -2;
			}
		}
		
		if(	(Math.abs(attack) == 1) &&
			getDistanceTo(playerData.xPos,playerData.yPos) - size < playerData.size &&
			delaydeath == 0){
				playerData.deathReset();
				delete = true;
		}
		
		/*
		if(delaydeath == 0 && bulletTimer >= bulletInterval){
			if(type == HOR){
				bulletList.add(new Bullet((int)xPos,(int)yPos,10,7,gameEnv,lineEnd1,(int)yPos));
				bulletList.add(new Bullet((int)xPos,(int)yPos,10,7,gameEnv,lineEnd2,(int)yPos));
			} else if(type == VER){
				bulletList.add(new Bullet((int)xPos,(int)yPos,10,7,gameEnv,(int)xPos,lineEnd1));
				bulletList.add(new Bullet((int)xPos,(int)yPos,10,7,gameEnv,(int)xPos,lineEnd2));				
			}
			bulletTimer = 0;
		} else {
			bulletTimer += timeRatio;
		}
		
		
		for(int i = 0; i < bulletList.size(); i++){
			if(bulletList.get(i).delete){
				bulletList.remove(i);
			} else {
				bulletList.get(i).updateData();
			}
		}
		*/
	}

	public void movementProcedure(){
		//movement
		if(xPos > gameEnv.screenW || xPos < 0){
			moveToX = -1*moveToX;
		}

		if(yPos > gameEnv.screenH || yPos < 0){
			moveToY = -1*moveToY;
		}
		
		if(attack == 1){
			speed = 25;
			moveToX = (playerData.xPos - xPos);
			moveToY = 0;
		} else if(attack == 2) {
			speed = initSpeed;
			moveToX = 0;
			moveToY = (playerData.yPos - yPos);
		} else if(attack == -1){
			speed = 25;
			moveToX = 0;
			moveToY = (playerData.yPos - yPos);
		} else if(attack == -2) {
			speed = initSpeed;
			moveToX = (playerData.xPos - xPos);
			moveToY = 0;
		}

		moveTo(xPos+moveToX,yPos+moveToY);
	}

	public void drawObj(Canvas c){
		Paint brush = new Paint();
		if(Math.abs(attack) == 1){
			brush.setColor(Color.RED);
		} else {
			brush.setColor(Color.YELLOW);
		}

		if(delaydeath == 0){
			c.drawCircle((float)xPos,(float)yPos,size,brush);
			if(type == VER){
				c.drawLine((int)xPos,(int)yPos,(int)xPos,lineEnd1,brush);
				c.drawLine((int)xPos,(int)yPos,(int)xPos,lineEnd2,brush);
			} else if(type == HOR) {
				c.drawLine((int)xPos,(int)yPos,lineEnd1,(int)yPos,brush);
				c.drawLine((int)xPos,(int)yPos,lineEnd2,(int)yPos,brush);
			}
		} else if(delaydeath <= 100){
			brush.setTextSize(40);
			brush.setColor(Color.BLUE);
			c.drawText("+"+Integer.toString(comboHolder), (int)xPos, (int)yPos, brush);
		}
		
		/*
		for(int i = 0; i < bulletList.size(); i++){
			bulletList.get(i).drawObj(c);
		}
		*/
	}

	public void passTouchData(MotionEvent event){}

}
