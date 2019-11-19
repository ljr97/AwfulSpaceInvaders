package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, MouseListener, KeyListener {
		
	public static final int WIDTH = 500;
	public static final int HEIGHT = 640;
	
	public static int SCORE;
	public static int HIGHSCORE = 10000;
	public static int LIVES;
	public static int LEVEL;
	
	private boolean running = false;
	private Thread thread;
	
	private BufferedImage image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
	private BufferedImage spriteSheet = null;
	private BufferedImage background = null;

	private boolean isShooting = false;
	public boolean enemyShooting = false;
	
	public static boolean paused = false;
	
	public static int enemycount;
	public static int enemykilled;
	
	public Rectangle playButton = new Rectangle(150, 200, 200, 64);
	public Rectangle rulesButton = new Rectangle(150, 400, 200, 64);
	public Rectangle quitButton = new Rectangle(150, 500, 200, 64);
	public Rectangle howToPlayButton = new Rectangle(150, 300, 200, 64);

	public Rectangle pause2Button = new Rectangle(200, 10, 32, 32);
	public Rectangle play2Button = new Rectangle(240, 10, 32, 32);
	public Rectangle quit2Button = new Rectangle(280, 10, 32, 32);
	
	private Player p;
	//private Enemy eny;
	private Controller c;
	private Textures tex;

	public ArrayList<EntityA> ea;
	public ArrayList<EntityB> eb;
	
	public static STATE gameState = STATE.Menu ;
	
	public enum STATE{
		Menu,
		Game,
		HowToPlay,
		Rules,
		Won,
		Lost
	};
	
	public void init(){
		
		LEVEL = 1;
		LIVES = 3;
		
		requestFocus();

		BufferedImageLoader loader = new BufferedImageLoader();
		try{
			spriteSheet = loader.loadImage("/sprite_sheet.png");
			background = loader.loadImage("/background.png");
		}catch(IOException e){
			e.printStackTrace();
		}		
		
		
		tex = new Textures(this);
		
		addMouseListener(this);
		addKeyListener(this);

		p = new Player(240, 545, tex);
		c = new Controller(tex, this);	
		//eny = new Enemy(200, 200, 0, 0, tex, c, null);
		
		ea = c.getEntityA();
		eb = c.getEntityB();
		
		c.createEnemy(enemycount);
			
	}
	
	public synchronized void start(){ // Start method
		if(running)
			return;
		
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop(){
		if(!running)
			return;
		
		running = false;
		try{
			thread.join();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.exit(1);
	}
	
	public void run() {
		init();
		long lastTime = System.nanoTime();
		final double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int updates = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		
		while(running){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if(delta >= 1){
                tick();
                updates++;
                delta--;
            }
            render();
            frames++;
            
            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
               System.out.println(updates + " Ticks, FPS: " + frames );
               System.out.println("ENEMY KILLED: " + enemykilled);
                updates = 0;
                frames = 0;
            }
        }
		stop();
	}
	
	private void tick() {

		if(gameState == STATE.Game)
		{
			if(!paused)
			{
				
			p.tick();
			c.tick();
			
			}
		}	
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		
		g.drawImage(background, 0, 0, null);
	
		if(paused && !(gameState == STATE.Lost) && !(gameState == STATE.Menu) && !(gameState == STATE.Won) && !(gameState == STATE.Rules) && !(gameState == STATE.HowToPlay)){
			Font fnt = new Font("Monospaced", 1, 20);
			
			g.setFont(fnt);
			g.setColor(Color.GREEN);
			g.drawString("PAUSED", 175, 630);
		}
		
		if(gameState == STATE.Game){
			g.setColor(Color.GREEN);
			g.drawLine(0, 600, 1000, 600);
			
			Graphics2D g2 = (Graphics2D) g;

			Font fnt = new Font("Monospaced", 1, 20);
			
			g.setFont(fnt);
			g.setColor(Color.WHITE);
			g.drawString("SCORE:" + SCORE, 15, 30);
			
			g.setFont(fnt);
			g.setColor(Color.WHITE);
			g.drawString("LIVES:" + LIVES, 15, 630);
			
			g.setFont(fnt);
			g.setColor(Color.WHITE);
			g.drawString("LEVEL:" + LEVEL, 410, 30);
			
			g.setFont(fnt);
			g.setColor(Color.WHITE);
			g.drawString("HIGH-SCORE:" + HIGHSCORE, 330, 630);
					
			g.setColor(Color.BLACK);
			g2.draw(pause2Button);
			g2.draw(play2Button);
			g2.draw(quit2Button);
			
			g.drawImage(tex.play, 240, 10, null);
			g.drawImage(tex.pause, 205, 10, null);
			g.drawImage(tex.stop, 275, 10, null);

			p.render(g);
			c.render(g);
		
		if(enemykilled == 35 && LIVES >= 0)
		{
			eb.clear();
			eb = c.getEntityB();			
			enemykilled = 0;
			LEVEL = LEVEL + 1;
			Actor.enemySide = Actor.enemySide + 0.2;
			Actor.enemyDown = Actor.enemyDown + 0.2;

			c.createEnemy(enemycount);
		}
		
		
		
		
		if(SCORE == 500){
			LIVES ++;
		}
		
		if(LEVEL == 6 && LIVES >= 1)
		{
			Game.gameState = STATE.Won;
		}	
		}else if(gameState == STATE.Menu || gameState == STATE.Rules || gameState == STATE.HowToPlay)
		{
			
			Graphics2D g2 = (Graphics2D) g;
			
			
			if(Game.gameState == STATE.Menu){
				
				Font fnt1 = new Font("Monospaced", 1, 55);
				Font fnt2 = new Font("Monospaced", 1, 50);
				Font fnt3 = new Font("Monospaced", 1 ,25);
			
				g.setFont(fnt1);
				g.setColor(Color.GREEN);
				g.drawString("SPACE INVADERS", 25, 100);

				g.setColor(Color.WHITE);
				g.setFont(fnt3);
				g.drawString("HOW TO PLAY", 170, 340);
				g.setFont(fnt2);
				g.drawString("PLAY", 190, 247);
				g.drawString("RULES", 175, 447);
				g.drawString("QUIT", 187, 545);

				g2.draw(playButton);
				g2.draw(rulesButton);
				g2.draw(howToPlayButton);
				g2.draw(quitButton);
			}else if(Game.gameState == STATE.HowToPlay){
				Font fnt = new Font("Monospaced", 1, 60);
				Font fnt2 = new Font("Monospaced", 1, 50);
				Font fnt3 = new Font("Monospaced", 1 ,18);
				Font fnt4 = new Font("Monospaced", 1 ,17);
				
				g.setFont(fnt);
				g.setColor(Color.GREEN);
				g.drawString("HOW TO PLAY", 50, 100);
				
				g.setFont(fnt3);
				g.setColor(Color.WHITE);
				g.drawString("USE THE A,D KEYS TO CONTROL THE SHIP", 25, 170);
				g.drawString("A-LEFT", 25, 210);
				g.drawString("D-Right", 25, 240);
				g.drawString("YOUR AIM IS TO ELIMINATE ALL OF THE ALEIN", 25, 320);
				g.drawString("INVADERS WITH YOUR SHIP. ONCE ALL OF THE", 25, 340);
				g.drawString("ALIEN INVADERS ARE KILLED, YOU WILL ", 25, 360);
				g.drawString("PROGRESS TO THE NEXT LEVEL. ONCE THE ALIENS", 25, 380);
				g.drawString("GET CLOSE ENOUGH YOU WILL DIE, SO BE QUICK", 25, 400);

				g.setFont(fnt4);
				g.setColor(Color.WHITE);
				g.drawString("USE THE SPACEBAR TO SHOOT THE ALIEN INVADERS", 25, 280);

								
				g.setFont(fnt2);
				g.setColor(Color.WHITE);
				g.drawRect(150, 500, 200, 64);
				g.drawString("BACK", 190, 545);
			}else if(Game.gameState == STATE.Rules){
				
				Font fnt = new Font("Monospaced", 1, 60);
				Font fnt2 = new Font("Monospaced", 1, 50);
				Font fnt3 = new Font("Monospaced", 1, 20);

				g.setFont(fnt);
				g.setColor(Color.GREEN);
				g.drawString("RULES", 160, 100);
				
				g.setFont(fnt3);
				g.setColor(Color.WHITE);
				g.drawString("ALIEN INVADERS SCORING:", 25, 170);
				g.drawString("10 Points(BOTTOM TWO ROWS)", 25, 210);
				g.drawString("20 Points(TWO MIDDLE ROWS)", 25, 240);
				g.drawString("30 Points(TOP ROW)", 25, 270);
				
				g.drawString("MYSTERY SHIP SCORING:", 25, 350);
				g.drawString("50, 100, 150 OR 300 POINTS", 25, 390);


				g.setFont(fnt2);
				g.setColor(Color.WHITE);
				g.drawRect(150, 500, 200, 64);
				g.drawString("BACK", 190, 545);
			}
		}
		
		if(Game.gameState == STATE.Won)
		{
			Graphics2D g2 = (Graphics2D) g;
			
			Font fnt = new Font("Monospaced", 1, 30);
			Font fnt2 = new Font("Monospaced", 1, 50);


			g.setColor(Color.WHITE);
			g2.draw(rulesButton);
			g2.draw(quitButton);
			
			g.setFont(fnt);
			g.drawString("PLAY AGAIN", 160, 440);
			
			g.setFont(fnt2);
			g.drawString("MENU", 190, 545);

			Font fnt3 = new Font("Monospaced", 1, 60);
			
			g.setFont(fnt3);
			g.setColor(Color.GREEN);
			g.drawString("YOU WIN", 123, 100);
		}		
		
		if(Game.gameState == STATE.Lost)
		{

			
			Graphics2D g2 = (Graphics2D) g;
			
			Font fnt = new Font("Monospaced", 1, 30);
			Font fnt2 = new Font("Monospaced", 1, 50);


			g.setColor(Color.WHITE);
			g2.draw(rulesButton);
			g2.draw(quitButton);
			
			g.setFont(fnt);
			g.drawString("PLAY AGAIN", 160, 440);
			
			g.setFont(fnt2);
			g.drawString("MENU", 190, 545);

			Font fnt3 = new Font("Monospaced", 1, 60);
			
			g.setFont(fnt3);
			g.setColor(Color.RED);
			g.drawString("YOU LOSE", 100, 100);
		}
		
		
		
		g.dispose();
		bs.show();
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		
		if(gameState == STATE.Game){
		if(key == KeyEvent.VK_D){
			p.setVelX(5); 
		}else if(key == KeyEvent.VK_A){
			p.setVelX(-5); 
		}else if(key == KeyEvent.VK_RIGHT){
				p.setVelX(5); 
		}else if(key == KeyEvent.VK_LEFT){
				p.setVelX(-5);
		}else if(key == KeyEvent.VK_SPACE && !isShooting){
				isShooting = true;
			c.addEntity(new Bullet(p.getX(), p.getY(), tex, this));
		}else if(key == KeyEvent.VK_P && !enemyShooting){		
			enemyShooting = true;
			//c.addEntity(new EnemyBomb(Enemy.x, Enemy.y, tex, this));
			
		}else if(key == KeyEvent.VK_ESCAPE){
			if(Game.gameState == STATE.Game)
			{
			if(Game.paused) Game.paused =false;
				else Game.paused = true;
			}
		}
		
		}
		
		if(Game.gameState == STATE.Menu)
		{
			if(key == KeyEvent.VK_ESCAPE){
			System.exit(1);
			}
		}
	}
	
	public void keyReleased(KeyEvent e){
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_D){
			p.setVelX(0);
		}else if (key == KeyEvent.VK_A){
			p.setVelX(0);
		}else if (key == KeyEvent.VK_LEFT){
				p.setVelX(0);
		}else if (key == KeyEvent.VK_RIGHT){
					p.setVelX(0);
		}else if(key == KeyEvent.VK_SPACE){
			isShooting = false;
		}else if(key == KeyEvent.VK_P){
			enemyShooting = false;
		}
		
	}
	
	
	
	public void mousePressed(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		
		if(Game.gameState == STATE.Menu){
			
			if(mx >= 150 && mx <= 350 )//Play button
			{
				if(my >= 200 && my <= 264)
				{
					SCORE = 0;
					LEVEL = 1;
					LIVES = 3;
					eb.clear();
					
					enemykilled = 0;
					Actor.enemySide = 0.8;
					Actor.enemyDown = 0.4;

					ea = c.getEntityA();
					eb = c.getEntityB();
								
					c.createEnemy(enemycount);
					Game.gameState = Game.STATE.Game;
				}
			}
			
			if(mx >= 150 && mx <= 350) // How To Play
			{
				if(my >= 300 && my <= 364)
				{
					Game.gameState = Game.STATE.HowToPlay;
				}
			}
		
			if(mx >= 150 && mx <= 350) // Rules
			{
				if(my >= 400 && my <= 464)
				{
					Game.gameState = Game.STATE.Rules;
				}
			}
		
			if(mx >= 150 && mx <= 350) // Quit
			{
				if(my >= 500 && my <= 564)
				{
					System.exit(1);
				}
			}
		}
		
		if(Game.gameState == STATE.Rules){
			if(mx >= 150 && mx <= 350) // Back to Menu
			{
				if(my >= 500 && my <= 564)
				{
					Game.gameState = Game.STATE.Menu;
				}
			}
		}
		
		if(Game.gameState == STATE.HowToPlay){
			if(mx >= 150 && mx <= 350) // Back to Menu
			{
				if(my >= 500 && my <= 564)
				{
					Game.gameState = Game.STATE.Menu;
				}
			}
		}
		
		if(Game.gameState == STATE.Game){ // PAUSE BUTTON
			
			if(mx >= 205 && mx <= 237)
			{
				if(my >= 10 && my <= 42)
				{
					Game.paused = true;
				}
			}
			
			if(mx >= 240 && mx <= 272) // RESUME BUTTON
			{
				if(my >= 10 && my <= 42)
				{
					Game.paused = false;
				}
			}
			
			if(mx >= 275 && mx <= 307) // QUIT TO MENU BUTTON
			{
				if(my >= 10 && my <= 42)
				{
					eb.clear();
					ea.clear();
					enemykilled = 0;
					Game.gameState = Game.STATE.Menu;
					Game.paused = false;
				}
			}
		}
		
		if(Game.gameState == STATE.Won){ 
			
			if(mx >= 150 && mx <= 350) // Play again
			{
				if(my >= 400 && my <= 464)
				{
					SCORE = 0;
					LEVEL = 1;
					LIVES = 3;
					
					ea.clear();
					eb.clear();
					
					enemykilled = 0;
					
					Actor.enemySide = 0.8;
					Actor.enemyDown = 0.4;
					
					eb = c.getEntityB();					
					ea = c.getEntityA();

								
					c.createEnemy(enemycount);

					Game.gameState = Game.STATE.Game;
				}
			}
			
			if(mx >= 150 && mx <= 350) // Quit to Menu
			{
				if(my >= 500 && my <= 564)
				{
					Game.gameState = Game.STATE.Menu;
				}
			}
		}
		
			if(Game.gameState == STATE.Lost){ 
			
			if(mx >= 150 && mx <= 350) // Play again
			{
				if(my >= 400 && my <= 464)
				{
					SCORE = 0;
					LEVEL = 1;
					LIVES = 3;

					eb.clear();
					ea.clear();

					enemykilled = 0;
					
					Actor.enemySide = 0.8;
					Actor.enemyDown = 0.4;					
					
					eb = c.getEntityB();
					ea = c.getEntityA();
			
					c.createEnemy(enemycount);					
					
					Game.gameState = Game.STATE.Game;		

				}
			}
			
			if(mx >= 150 && mx <= 350) // Quit to Menu
			{
				if(my >= 500 && my <= 564)
				{
					Game.gameState = Game.STATE.Menu;
				}
			}
		}

	}

	public void mouseReleased(MouseEvent e) {
		
	}
	
			
	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}
	
	
	
	public static boolean Collision(EntityA enta, EntityB entb){
		
		
		if(enta.getBounds().intersects(entb.getBounds())){
			return true;
		}
		
		return false;
	}
	
	public static boolean Collision(EntityB entb, EntityA enta){
		
			
		if(entb.getBounds().intersects(enta.getBounds())){
			return true;
		}
		
		return false;
	}
	
	public static void main(String args[]) {
		Game game = new Game();
		enemykilled = 0;
		
		game.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		game.setMaximumSize(new Dimension(WIDTH,HEIGHT));
		game.setMinimumSize(new Dimension(WIDTH,HEIGHT));
		
		JFrame frame = new JFrame("Space Invaders");
		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		game.start();
	}
	
	public BufferedImage getSpriteSheet(){
		return spriteSheet;
	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
}

}
