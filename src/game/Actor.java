package game;

import java.awt.Rectangle;

public class Actor {

	public double x, y;
	protected double velX, velY;
	public static double enemySide = 0.8;
	public static double enemyDown = 0.4;

	public Actor(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public Rectangle getBounds(int width, int height){
		return new Rectangle((int)x, (int)y, width, height);
	}
}
