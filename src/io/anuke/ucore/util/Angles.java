package io.anuke.ucore.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Angles{
	static public Vector2 vector = new Vector2(1,1);
	
	static public float ForwardDistance(float angle1, float angle2){
		return angle1 > angle2 ? angle1-angle2 : angle2-angle1;
	}

	static public float BackwardDistance(float angle1, float angle2){
		return 360 - ForwardDistance(angle1, angle2);
	}

	static public float angleDist(float a, float b){
		return Math.min(ForwardDistance(a, b), BackwardDistance(a, b));
	}

	static public float MoveToward(float angle, float to, float speed){
		if(Math.abs(angleDist(angle, to)) < speed)return to;

		if((angle > to && BackwardDistance(angle, to) > ForwardDistance(angle, to)) || 
				(angle < to && BackwardDistance(angle, to) < ForwardDistance(angle, to)) ){
			angle -= speed;
		}else{
			angle += speed;
		}
		
		return angle;
	}
	
	static public Vector2 rotate(float x, float y, float angle){
		if(MathUtils.isEqual(angle, 0, 0.001f)) return vector.set(x,y);
		return vector.set(x,y).rotate(angle);
	}
	
	static public Vector2 translation(float angle, float amount){
		if(amount < 0) angle += 180f;
		return vector.setAngle(angle).setLength(amount);
	}

	static public float mouseAngle(OrthographicCamera camera, float cx, float cy){
		Vector3 avector = camera.project(new Vector3(cx, cy, 0));
		vector.set(Gdx.input.getX() - avector.x, Gdx.graphics.getHeight() - Gdx.input.getY() - avector.y);
		return vector.angle();
	}
}
