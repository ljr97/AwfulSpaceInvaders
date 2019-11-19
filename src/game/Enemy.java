package game;

import java.awt.Graphics;
import java.awt.Rectangle;

import game.Game.STATE;

public class Enemy extends Actor implements EntityB{

	private Textures tex;
	private Game game;
	private Controller c;

	private int currentDirection = 0;
	private int moves = 80;
	
	public Enemy(double x, double y, Textures tex, Controller c, Game game){
		super(x,y);
		this.tex = tex;
		this.game = game;
		this.c = c;

	}
	
	public void tick(){
		if (currentDirection == 0) {
			x += Actor.enemySide;
		} else if (currentDirection == 90) {
			y += Actor.enemyDown;
		} else if (currentDirection == 180) {
			x -= Actor.enemySide;
		} else if (currentDirection == 270) {
			y += Actor.enemyDown;
		}

		moves--;

		if (moves < 0) {
			moves = 25;
			currentDirection += 90;
			moves = 80;
			if(currentDirection > 270) {
				moves = 80;
				currentDirection = 0;
			}
		}
		


		
		
		
					
		//y += Actor.enemySpeed;
		
		if(y > 480)
		{
			Game.gameState = STATE.Lost;	
		}
				
		for(int i = 0; i < game.ea.size(); i ++)
		{
			EntityA tempEnt = game.ea.get(i);
			
			if(Game.Collision(this, tempEnt))
			{
				c.removeEntity(tempEnt);
				Game.SCORE = Game.SCORE +20;
				c.removeEntity(this);
				Game.enemykilled = Game.enemykilled +1;
			}
		}
		
		
		
	}
	
	public void render(Graphics g){
		g.drawImage(tex.enemy, (int)x, (int)y, null);
	}
	
	
	
	public Rectangle getBounds(){
		return new Rectangle((int)x+15, (int)y, 8, 8);
	}

	
	public double getX() {
		return x;
	}

	
	public double getY() {
		return y;
	}

}
