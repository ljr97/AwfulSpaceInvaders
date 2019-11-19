package game;

import java.awt.Graphics;
import java.util.ArrayList;

public class Controller {

		private ArrayList<EntityA> ea = new ArrayList<EntityA>();
		private ArrayList<EntityB> eb = new ArrayList<EntityB>();

		EntityA enta;
		EntityB entb;

		Textures tex;
		private Game game;
		
		public Controller(Textures tex, Game game){
			this.tex = tex;
			this.game = game;
				
		}
		
		public void createEnemy(int enemycount){
			for (int row=0;row<1;row++) {
				for (int x=0;x<7;x++) {
					addEntity(new Enemy2(40+(x*50), (80)+row*30, tex, this, game));
				}
			}
			
			for (int row=0;row<2;row++) {
				for (int x=0;x<7;x++) {
					addEntity(new Enemy(40+(x*50), (110)+row*30, tex, this, game));
				}
			}	
			
			for (int row=0;row<2;row++) {
				for (int x=0;x<7;x++) {
					addEntity(new Enemy3(40+(x*50), (170)+row*30, tex, this, game));
				}
			}	
			
			for (int row=0;row<1;row++) {
				for (int x=0;x<1;x++) {
					addEntity(new Enemy4((-500), (45)+row*30, tex, this, game));
				}
			}
			
			
			
			
}
		
		
		public void tick(){
			//A collision
			for(int i = 0; i < ea.size(); i++){
				enta = ea.get(i);
				
				enta.tick();
			}
			
			//B collision
			for(int i = 0; i < eb.size(); i++){
				entb = eb.get(i);
				
				entb.tick();
			}
		}
		
		public void render(Graphics g){
			// A collision
			for(int i = 0; i < ea.size(); i++){
				enta = ea.get(i);
				
				enta.render(g);
			}
			
			//B collision
			for(int i = 0; i < eb.size(); i++){
				entb = eb.get(i);
				
				entb.render(g);
			}
		}
		
		public void addEntity(EntityA block){
			ea.add(block);
		}
		
		public void removeEntity(EntityA block){
			ea.remove(block);
		}
		public void addEntity(EntityB block){
			eb.add(block);
		}
		
		public void removeEntity(EntityB block){
			eb.remove(block);
		}
		public ArrayList<EntityA> getEntityA(){
			return ea;
		}
		public ArrayList<EntityB> getEntityB(){
			return eb;
		}

		
}
