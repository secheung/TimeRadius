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

public class Player extends DynamicObject{
	
	//private static double INIT_TIME_RADIUS = 150;
	private static double TIME_RADIUS_SIZE_MULTI = 6.5;
	private static float TEXT_SIZE = 25;
	
	double oldMoveToX;
	double oldMoveToY;
	double sampleRate = 0;
	int sampleSwipe = 1;
	
	
	double timeRadius;
	double health = 100;
	int lives = 5;
	
	int multiplier = 1;
	double mulBar = 0;
	double nextMulBar = 500;
	int decMulBar = 1;
	 
	double delayMulBar = 0;
	double delayMulBarTot = 75;
	int comboCount = 1;
	
	public Player(int x, int y, int size, int speed, Panel gameEnv){
		super(x,y,size,speed,gameEnv);
		moveToX = x;
		moveToY = y;
		
		timeRadius = size*TIME_RADIUS_SIZE_MULTI;
	}
	
	public void drawObj(Canvas c){
		Paint brush = new Paint();
		double drawBar = Math.min((mulBar/nextMulBar),1)*400;
		
		if(health > 0 && lives > 0){
			brush.setColor(Color.BLUE);
		} else {
			brush.setColor(Color.RED);
		}
		
		if(delay > 0){
			brush.setColor(0xFFF06D2F);
		}
		
		c.drawCircle((float)xPos,(float)yPos,size,brush);
		brush.setStyle(Paint.Style.STROKE);
		c.drawCircle((float)xPos,(float)yPos,(float)timeRadius,brush);
		
		brush.setTextSize(TEXT_SIZE);
		c.drawText("score: " + Integer.toString(gameEnv.score), 40, 25, brush);
		
		brush.setTextSize(TEXT_SIZE);
		c.drawText("lives: " + Integer.toString(lives), 40, 50, brush);
		
		
		//initial MulBar
		//brush.setTextSize(60);
		//c.drawText(Integer.toString(multiplier),gameEnv.screenW - 550, 100,brush);
		brush.setColor(Color.GREEN);
		//c.drawRect(gameEnv.screenW - 500, 50, gameEnv.screenW - 100, 100, brush);
		c.drawRect(40, 60, 40 + gameEnv.screenW*0.30f, 85, brush);
		//brush.setStyle(Paint.Style.FILL);
		//c.drawRect(gameEnv.screenW - 500, 50, gameEnv.screenW - 500 + (int)(drawBar), 100, brush);
		
		//delay MulBar
		//brush.setColor(Color.GREEN);
		if(delayMulBar > 0){
			brush.setStyle(Paint.Style.FILL);
			//at mulbar//c.drawRect(gameEnv.screenW - 500, 50, gameEnv.screenW - 500 + (int)((delayMulBar/delayMulBarTot)*drawBar), 100, brush);
			//c.drawRect(gameEnv.screenW - 500, 50, gameEnv.screenW - 500 + (int)((delayMulBar/delayMulBarTot)*400), 100, brush);
			c.drawRect(40, 60, 40 + (int)((delayMulBar/delayMulBarTot)*(gameEnv.screenW*0.30f)), 85, brush);
			brush.setTextSize(TEXT_SIZE);
			//at mulbar//c.drawText("+"+Integer.toString(comboCount),gameEnv.screenW - 500 + (int)((delayMulBar/delayMulBarTot)*drawBar),100,brush);
			c.drawText("+"+Integer.toString(comboCount),40 + (int)((delayMulBar/delayMulBarTot)*(gameEnv.screenW*0.30f)),85,brush);
		}
		
		//help box
		//brush.setStyle(Paint.Style.STROKE);
		//c.drawRect(gameEnv.screenW - 325, gameEnv.screenH/2 - 150, gameEnv.screenW - 50, gameEnv.screenH/2 + 125,brush);
		
		brush.setStyle(Paint.Style.STROKE);
		brush.setColor(Color.RED);
		if(lives <= 0){
			brush.setTextSize(TEXT_SIZE+25);
			c.drawText("NOPE", gameEnv.screenW/2, gameEnv.screenH/2, brush);
		}
		
		if(health <= 0 && lives > 0){
			brush.setTextSize(TEXT_SIZE);
			//c.drawText("TAP SCREEN RAPIDLY TO LIVE", (int)xPos-200,(int)yPos-30, brush);
			c.drawText("TAP SCREEN RAPIDLY TO LIVE", 20, gameEnv.screenH/2, brush);
			
			brush.setTextSize(40);
			c.drawText("-5", (int)xPos-40,(int)yPos-60, brush);
			brush.setColor(Color.GREEN);
			c.drawCircle((float)xPos,(float)yPos,(float)(timeRadius*health/-100),brush);
			
		}
		
	}

	public void updateData(){
		
		mulBarUpdate();
		if(delay > 0){
			delay--;
			return;
		}
		
		if(xPos > gameEnv.screenW && moveToX > 0){
			//moveToX = moveToX - speed;
			moveToX = -1*moveToX;
		} else if (xPos < 0 && moveToX < 0){
			//moveToX = moveToX + speed;
			moveToX = -1*moveToX;
		}

		if(yPos > gameEnv.screenH && moveToY > 0){
			//moveToY = moveToY - speed;
			moveToY = -1*moveToY;
		} else if (yPos < 0 && moveToY < 0){
			//moveToY = moveToY + speed;
			moveToY = -1*moveToY;
		}
		
		
		if(health > 0 && lives > 0){
			moveTo(xPos + moveToX, yPos + moveToY);
		}
		
	}
	
	public void mulBarUpdate(){
		if(mulBar/nextMulBar > 1){
			mulBar = mulBar - nextMulBar;
			multiplier++;
			nextMulBar += multiplier*100;
			decMulBar += multiplier*1.3;
			//timeRadius = timeRadiusStrt + 15*multiplier;
		} else if(mulBar > 0 && delayMulBar <= 0){
			mulBar -= decMulBar;
		} else if(delayMulBar <= 0) {
			mulBar = 0;
		}
		
		
		if(delayMulBar > 0){
			delayMulBar--;
		} else {
			delayMulBar = 0;
			comboCount = 0;
		}
		
		
		return;
	}
	
	public void deathReset(){
		health = 0;
		lives -= 1;
		mulBar = 0;
		nextMulBar = 500;
		multiplier = 1;
		timeRadius = size*TIME_RADIUS_SIZE_MULTI;
		decMulBar = 1;
		delayMulBar = 0;
		comboCount = 0;
		//gameEnv.score = Math.max(0,gameEnv.score - 5);
	}
	
	public void passTouchData(MotionEvent event){
		if(sampleRate == 0){
			oldMoveToX = event.getX();
			oldMoveToY = event.getY();
		}
		
		//helper box
		//if(event.getX() > gameEnv.screenW - 325 && event.getX() <  gameEnv.screenW - 50 && event.getY() > gameEnv.screenH/2 - 150 && event.getY() < gameEnv.screenH/2 + 125){
		moveToX = (int)event.getX() - oldMoveToX;
		moveToY = (int)event.getY() - oldMoveToY;
		sampleRate++;
		//}
		if(sampleRate >= 500){
			sampleRate = 0;
		}
		
		//moveToX = (int)event.getX();
		//moveToY = (int)event.getY();
		
		if(event.getAction() == event.ACTION_DOWN){
		//if(event.getAction() == event.ACTION_MOVE){
			if(health <= -90){
				health = 100;
			} else if(health <= 0){
			//} else if(sampleSwipe >= 5 && health <= 0 && (Math.abs(moveToX) > 0 || Math.abs(moveToY) > 0)){
				//health -= Math.max(5,3*lives);
				health -= 15;
				sampleSwipe = 0;
			} else if(sampleSwipe <=5 && health <= 0){
				sampleSwipe++;
			}
		} else if(event.getAction() == event.ACTION_UP){
			sampleRate = 0;
		}
	}
}
