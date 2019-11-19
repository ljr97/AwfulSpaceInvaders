package game;

import java.awt.Graphics;
import java.awt.Rectangle;

public class EnemyBomb extends Actor implements EntityB {
	
	private Textures tex;
	
	public EnemyBomb(double x, double y, Textures tex, Game game){
		super(x,y);
		this.tex = tex;
		
	}

	public void tick() {
		y += 0.4;
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, 32, 32);
	}
	
	public void render(Graphics g){
		g.drawImage(tex.enemybomb, (int)x, (int)y, null);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	

}
