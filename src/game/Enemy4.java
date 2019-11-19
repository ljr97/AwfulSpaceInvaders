package game;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;

public class Enemy4 extends Actor implements EntityB{
	
	private Textures tex;
	private Controller c;
	private Game game;
	
	ArrayList<Integer> lista = new ArrayList<Integer>();
	
	public Enemy4(double x, double y, Textures tex, Controller c, Game game){
		super(x,y);
		this.tex = tex;
		this.game = game;
		this.c = c;
		
		lista.add(50);
		lista.add(50);
		lista.add(50);
		lista.add(50);
		lista.add(50);
		lista.add(50);
		lista.add(50);
		lista.add(125);
		lista.add(125);
		lista.add(125);
		lista.add(125);
		lista.add(125);
		lista.add(125);
		lista.add(250);
		lista.add(250);
		lista.add(250);
		lista.add(300);
		
	}
	
	public void tick(){
		Collections.shuffle(lista);
		
		x += 2;
		if(x >= 1000){
			x = -50;
		}
		for(int i = 0; i < game.ea.size(); i ++)
		{
			EntityA tempEnt = game.ea.get(i);
			
			if(Game.Collision(this, tempEnt))
			{
				c.removeEntity(tempEnt);
				Game.SCORE = Game.SCORE + lista.get(0);
				c.removeEntity(this);
			}
		}
	}
	
	public void render(Graphics g){
		g.drawImage(tex.enemy4, (int)x, (int)y, null);
	}

	public Rectangle getBounds(){
		return new Rectangle((int)x+15, (int)y, 8, 8);
	}
	
	public double getX() {
		return y;
	}

	
	public double getY() {
		return x;
	}

}