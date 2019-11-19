package game;

import java.awt.Graphics;
import java.awt.Rectangle;

public class Bullet extends Actor implements EntityA{
	
	private Textures tex;
	
	public Bullet(double x, double y, Textures tex, Game game){
		super(x,y);
		this.tex = tex;

	}
	
	public void tick(){
		y-=5;
	}
	
	public Rectangle getBounds(){
		return new Rectangle((int)x, (int)y, 32, 32);
	}
	
	public void render(Graphics g){
		g.drawImage(tex.bullet, (int)x, (int)y, null);
	}
	
	
	public double getY(){
		return y;
	}
	
	public double getX(){
		return x;
	}
	
	
}
