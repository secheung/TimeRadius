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

abstract public class GeneralEnemy extends DynamicObject{
	int delaydeath = 0;
	int score;
	Player playerData;
	int comboHolder;
	int levelHolder = 0;

	public GeneralEnemy(int x, int y, int size, int speed, Panel gameEnv){
		super(x,y,size,speed,gameEnv);

		playerData = (Player)gameEnv.objList.get(0);
		moveToX = Math.random()*speed;
		moveToY = Math.random()*speed;
	}

	public void updateData(){
		if(delay > 0){
			delay--;
			return;
		}
		deathProcedure();
		habbitAIProcedure();
		
		if(delaydeath == 0){
			movementProcedure();
		}

	}
	
	public void deathProcedure(){
		if(		delaydeath == 0 &&
				getDistanceTo(playerData.xPos,playerData.yPos) - size < playerData.size &&
				playerData.health > 0){

			delaydeath = 1;
			//if(playerData.delayMulBar > 0){
			playerData.comboCount++;
			//}
			gameEnv.score = gameEnv.score + playerData.comboCount*this.score;
			playerData.mulBar = playerData.mulBar + playerData.comboCount*this.score*100;
			playerData.delayMulBar = playerData.delayMulBarTot;
			comboHolder = playerData.comboCount;
		} else if (delaydeath > 0){
			delaydeath++;
			moveToX = 0;
			moveToY = 0;
		}

		if(delaydeath >= 100){
			delete = true;
		
		}
	}
	
	abstract public void habbitAIProcedure();
	abstract public void movementProcedure();

}
