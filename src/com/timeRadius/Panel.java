package com.timeRadius;

import java.util.ArrayList;

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


class Panel extends SurfaceView implements SurfaceHolder.Callback {
	private static int MAX_ENEMY = 4;
	
	private GameThread _thread;
	ArrayList<DynamicObject> objList = new ArrayList<DynamicObject>(1);
	
	int screenW;
	int screenH;
	
	int defaultObjSize;
	
	int enemyctr = 1;
	int tutorialMode = 1;
	int maxenemy = 1;
	int level = 1;
	int score = 0;
	
	int resetCtr = 0;
	
	int defaultSpeed = 4;
	
	Player playerData;
	
	public Panel(Context context,int screenw, int screenh) {
		super(context);
		getHolder().addCallback(this);
		_thread = new GameThread(getHolder(), this);
		_thread.setRunning(true);
		
		screenW = screenw;
		screenH = screenh;
		
		defaultObjSize = (int) (screenW*0.025);
		
		//player needs to be added first
		playerData = new Player(screenW/2,screenH/2,defaultObjSize,defaultSpeed,this);
		objList.add(playerData);
		
		setFocusable(true);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		synchronized (_thread.getSurfaceHolder()) {
			for(int i = 0; i < objList.size(); i ++){
				objList.get(i).passTouchData(event);
			}
			
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				if(playerData.health <= 0 && playerData.lives <= 0){
					resetCtr++;
				} 
			}
			
			return true;
		}
	}

	public void gameUpdate(){
		double spawn;
		int spawnX;
		int spawnY;
		
		if(score/50 > level && level <= 3){
			level++;
		}
		
		if(playerData.lives>0 && playerData.health != 0){
			//score++;
		}
		
		for(int i = 0; i < objList.size(); i ++){
			if(!(objList.get(i).delete)){
				objList.get(i).updateData();
			} else {
				objList.remove(i);
				enemyctr--;
				if(maxenemy < MAX_ENEMY)
					maxenemy++;
			}
		}
		
		if(enemyctr <= maxenemy){
			spawn = 100*Math.random();
			spawnX = (int)(screenW*Math.random());
			spawnY = (int)(screenH*Math.random());
			if(playerData.getDistanceTo(spawnX,spawnY) > 250){
				if(spawn > 50 && spawn <= 100){
					EnemyShoot enemy = new EnemyShoot(spawnX,spawnY, defaultObjSize,(int)(5*Math.random() + 2),this);
					objList.add(enemy);
					enemyctr++;
				} else if(spawn > 40 && spawn <= 50){
					EnemyRadius enemy = new EnemyRadius(spawnX,spawnY, defaultObjSize,(int)(4*Math.random() + 3),this);
					objList.add(enemy);
					enemyctr++;
				} else if(spawn > 35 && spawn <= 40){
			 		EnemyLine enemy = new EnemyLine(spawnX,spawnY, defaultObjSize,(int)(2*Math.random() + 1),this);
					objList.add(enemy);
					enemyctr++;
				}
			}
		}
		/*
		else if(tutorialMode == 1){
			if(score == 1){
				tutorialMode = 2;
			} else if(enemyctr == 0){
				//EnemyShoot enemy = new EnemyShoot(100,100,defaultObjSize,(int)(7*Math.random() + 2),this);
				EnemyRadius enemy = new EnemyRadius(100,100, defaultObjSize,(int)(7*Math.random() + 2),this);
				objList.add(enemy);
				enemyctr++;
			}
		} else if(tutorialMode == 2){
			if(score == 2){
				tutorialMode = 3;
			} else if(enemyctr == 0){
				EnemyLine enemy = new EnemyLine(100,100,defaultObjSize,(int)(3*Math.random() + 1),this);
				objList.add(enemy);
				enemyctr++;
			}
		} else if(tutorialMode == 3){
			if(score == 3){
				tutorialMode = -1;
			} else if(enemyctr == 0){
				EnemyRadius enemy = new EnemyRadius(100,100,defaultObjSize,(int)(7*Math.random() + 3),this);
				objList.add(enemy);
				enemyctr++;
			}
		} else if(tutorialMode == -1 && enemyctr == 0){
			maxenemy = MAX_ENEMY;
		}
		
		if(tutorialMode > 0 && playerData.health == 0){
			tutorialMode = 1;
		}
		*/

		if(resetCtr >= 5){
			resetCtr = 0;
			gameReset();
		}
		
	}
	
	public void gameReset(){
		objList.clear();
		playerData = new Player(screenW/2,screenH/2,defaultObjSize,defaultSpeed,this);
		objList.add(playerData);
		
		enemyctr = 0;
		maxenemy = 1;
		level = 1;
		score = 0;
	}
	
	public void threadDestroy(){
		boolean retry = true;
		//_thread.setRunning(false);
		while (retry) {
			try {
				_thread.join();
				_thread.setRunning(false);
				retry = false;
				//_thread = null;
			} catch (InterruptedException e) {
				System.out.println(e);
			}
		}
	}
	
	@Override
	public void onDraw(Canvas canvas) {
	
		//Paint brush = new Paint();
		//brush.setColor(Color.BLUE);
		
		canvas.drawColor(Color.BLACK);
		for(int i = 0; i < objList.size(); i ++){
			objList.get(i).drawObj(canvas);
		}
	}

	//@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
	}

	//@Override
	public void surfaceCreated(SurfaceHolder holder) {
		_thread.setRunning(true);
		_thread.start();
	}

	//@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// simply copied from sample application LunarLander:
		// we have to tell thread to shut down & wait for it to finish, or else
		// it might touch the Surface after we return and explode
		boolean retry = true;
		_thread.setRunning(false);
		while (retry) {
			try {
				_thread.join();
				retry = false;
				_thread = null;
			} catch (InterruptedException e) {
				System.out.println(e);
			}
		}
	}
}
