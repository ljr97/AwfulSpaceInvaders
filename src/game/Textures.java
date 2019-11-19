package game;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Textures {
	
	public BufferedImage image;
	
	public BufferedImage loadImage(String path) throws IOException{
		
		image = ImageIO.read(getClass().getResource(path));
		return image;
	}
	
	public BufferedImage player, play, pause, stop, bullet, enemy, enemy2, enemy3, enemy4, lives, enemybomb;
	
	private SpriteSheet ss;
	
	public Textures(Game game){
		 ss = new SpriteSheet(game.getSpriteSheet());
		 
		 getTextures();
	}
	
	private void getTextures(){
		player = ss.grabImage(1,  1,  32,  32);
		lives = ss.grabImage(1, 1, 32, 32);
		bullet = ss.grabImage(2, 1, 32, 32);
		enemy = ss.grabImage(3, 1, 32, 32);
		enemy2 = ss.grabImage(4, 1, 32, 32);
		enemy3 = ss.grabImage(5, 1, 32, 32);
		enemy4 = ss.grabImage(6, 1, 32, 32);
		enemybomb = ss.grabImage(2, 1, 32, 32);
		pause = ss.grabImage(8, 1, 32, 32);
		stop = ss.grabImage(1, 2, 32, 32);
		play = ss.grabImage(2, 2, 32, 32);
	}
}
