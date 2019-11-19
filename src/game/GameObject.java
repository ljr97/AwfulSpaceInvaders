package game;

import java.awt.Rectangle;

public class GameObject {

	public double x, y;
	protected double velX, velY;
	public static double enemySpeed = 0.4;

	public GameObject(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public Rectangle getBounds(int width, int height){
		return new Rectangle((int)x, (int)y, width, height);
	}
}
