import java.applet.Applet;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import java.awt.*;

public class Head extends Applet implements MouseListener,MouseMotionListener,KeyListener,MouseWheelListener{

	Image bufferCanvas,bufferedImg,Instruction;
	Image slot[] = new Image[1000];
	Graphics backg;
	Timer time;
	Body body;

	boolean state[] = new boolean[10];
	String savePath = null;
	Color color;

	int newMouseX,
		newMouseY,
		oldMouseX,
		oldMouseY,
		drawRes = 1,
		bushSize = 2,
	    mouseMoveX,
	    mouseMoveY,
	    screenW,
	    screenH,
	    provWidth,
	    provHeight,
	    imgCnt,
	    aniCnt,
	    num = 0;

	public void init(){
		setBackground(new Color(240,240,240));
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addMouseListener(this);
		addKeyListener(this);
		setSize(1200,600);

		bufferCanvas = createImage(this.getWidth()-300, this.getHeight()-300);	
		Instruction = getImage(getDocumentBase(), "Instructions.png");	
		body = new Body();
		
		time = new Timer();
		time.schedule(new AnimationTask(), 150, 150); 
	}


	
	private class AnimationTask extends TimerTask{
		public void run(){
			if (aniCnt < body.panel.animPanel-1){
				aniCnt++;
			}else
				aniCnt = num;
			
			repaint();
		}
	}

	
	
	// --- // Double Buffer
	private Image dbImage;  
	private Graphics dbg;  
	public void update (Graphics g){
		   Graphics2D g2 = (Graphics2D)g;
		   RenderingHints rh = new RenderingHints(
		   RenderingHints.KEY_TEXT_ANTIALIASING,
		   RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		   g2.setRenderingHints(rh);

		// ----- // Rescaled buffer image
		if (screenW != this.getWidth() || screenH != this.getHeight()){
			screenW = this.getWidth();
			screenH = this.getHeight();
			
			dbImage = createImage (screenW,screenH);
			dbg = dbImage.getGraphics();
		}
	
		dbg.setColor (getBackground ());
		dbg.fillRect (0, 0,screenW,screenH);
		dbg.setColor (getForeground());
		paint(dbg);
		g.drawImage(dbImage, 0, 0, this);


		// ----- // Rescaled canvas 	
		if(screenW > provWidth || screenH > provHeight ){
			provWidth = screenW;
			provHeight = screenH;
			bufferCanvas = createImage(screenW-190, screenH-150);
		}
	}




	public void paint(Graphics g){
		
		if(state[8] == false){
		   g.drawImage(Instruction, getWidth()/4, 30, 500, 600, this);
		   g.setFont(new Font("Elephant",32,16));
		   g.setColor(Color.blue);
		   g.drawString("Press F2 to Start the Program", getWidth()/4, 20);
			
		}else{
			// Choose folder to save 
		if (state[5] == false){
			state[5] = true;
			FileDialog selectL = new FileDialog((Frame)null, "Please Choose A Place To Save!");
			selectL.setMode(FileDialog.SAVE);
			selectL.setFile("*.jpg;*.jpeg;*.png;*.GIF");
			selectL.setVisible(true);
			savePath = selectL.getDirectory();
		}
		

		// ----- // Displays Canvas 
		backg = bufferCanvas.getGraphics();
		Graphics2D g2D = (Graphics2D) g.create();
				   g2D.translate(180, 135);
				   g2D.drawImage(bufferCanvas, 0, 0, null);
				   backg.translate(-180, -135);
				   
				   if(state[3] == true)
					  backg.drawImage(bufferedImg, 180, 135, bufferedImg.getWidth(this), bufferedImg.getHeight(this), this);
				   	  state[3] = false;
				   	  
		// ----- // The border Rect	   	  
		g.setColor(new Color(100,100,100));
		g.drawRect(180, 135, getWidth()-190, getHeight()-150);


		// ----- //  Body / Panel
		body.panel.position[1][8] = getHeight()-46; 
		body.panel.screenH = getHeight();
		body.panel.screenW = getWidth();
		body.draw(g);


		// ----- // Mouse Events 
		for(int i = 0; i < body.panel.obj_Cnt;i++){
		    if (body.panel.hover[i] == true &&
				mouseMoveX >= body.panel.position[0][i] && mouseMoveX <= body.panel.position[0][i]+body.panel.dimension[0][i] &&
				mouseMoveY >= body.panel.position[1][i] && mouseMoveY <= body.panel.position[1][i]+body.panel.dimension[1][i]){

				state[0] = true;

				// ----- // Animation Sprite Background slots
				if(i == 10 && state[1] == true){
					body.panel.position[0][10]+= 100;
					body.panel.animPanel++;
					body.panel.position[0][34+body.panel.animPanel] = body.panel.position[0][10]-100; 
					
					BufferedImage bufferedImage = new BufferedImage (bufferCanvas.getWidth(this), bufferCanvas.getHeight(this),
							  BufferedImage.TYPE_INT_ARGB);
							  bufferedImage.createGraphics().drawImage(bufferCanvas, 0, 0, this);
							  
							  // ----- // Saves animation sprite
							  try {ImageIO.write( bufferedImage, "png", new File (savePath+"/Img"+(body.panel.animPanel-2)+".png"));
							  }	catch (IOException e1) {e1.printStackTrace();}
				}	

				// Start / Loop / Stop commands
				if(i == 2 && state[1] == true){
					state[6] = true;
					state[7] = true;
				}
				
				if(i == 3 && state[1] == true){
					state[7] = false;
					state[6] = true;
				}
		
				if(i == 4 && state[1] == true){
					state[7] = false;
					state[6] = false;
					num = 0;
				}

				// ----- // Color platter 
				if((i >= 11 && i <= 34) && state[1] == true){
					color = body.panel.color[i];
				}

				
				// ----- //  Open Files
				if( i == 5 && state[1] == true){
					state[1] = false;
					FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
					dialog.setMode(FileDialog.LOAD);
					dialog.setFile("*.jpg;*.jpeg;*.png;*.GIF");
					dialog.setVisible(true);
					String fName = dialog.getFile();
					String fPath = dialog.getDirectory();
				

					if(fName != null){
						try {bufferedImg = ImageIO.read(new File(fPath+""+fName));
						}catch (IOException e) {e.printStackTrace();}
						 state[3] = true;
					}
				}

				
				// ----- // Save Files
				if( i == 6 && state[1] == true){
					state[1] = false;
					FileDialog dialog = new FileDialog((Frame)null, "Select File to Save");
					dialog.setMode(FileDialog.SAVE);
					dialog.setFile("*.jpg;*.jpeg;*.png;*.GIF");
					dialog.setVisible(true);
					String fPath = dialog.getDirectory();
					
					
					BufferedImage bufferedImage = new BufferedImage (bufferCanvas.getWidth(this), bufferCanvas.getHeight(this),
								  BufferedImage.TYPE_INT_ARGB);
								  bufferedImage.createGraphics().drawImage(bufferCanvas, 0, 0, this);
								  imgCnt++;

		  			 			  try {ImageIO.write( bufferedImage, "png", new File (fPath+"Image"+imgCnt+".png" ) );
		  			 			  }	catch (IOException e1) {e1.printStackTrace();}
				}	
			}

		 // ----- // Mouse cursor hover
			if(i >= body.panel.obj_Cnt-1 && state[0] == true){
				Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
				setCursor(cursor);	
				state[0] = false;

			}else if(i >= body.panel.obj_Cnt-1 && mouseMoveX > 180 && mouseMoveY > 135 && mouseMoveX < getWidth()-10 &&
					  mouseMoveY < getHeight()-15){
						state[2] = true;
						Cursor cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
						setCursor(cursor);
						g.drawRect(mouseMoveX-(bushSize/2), mouseMoveY-(bushSize/2), bushSize, bushSize);

			}else if(i >= body.panel.obj_Cnt-1){
					    state[2] = false;
						state[1] = false;
						Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
						setCursor(cursor);
			}
		}
		
		
		
	
		if(state[4] == false){
			body.panel.position[0][10]+= 100;
			body.panel.animPanel++;
			body.panel.position[0][34+body.panel.animPanel] = body.panel.position[0][10]-100;
			state[4] = true;
		}else{
			
			// ----- // Animation sprite slots
			g.drawImage(bufferCanvas, body.panel.position[0][35]+3, 23, 89, 74, this);	
			
			if(state[6] == true){
				num = 1;
				try {bufferCanvas = ImageIO.read(new File(savePath+"/Img"+aniCnt+".png"));
				}catch (IOException e) {e.printStackTrace();}
			}
			
			if(aniCnt >= body.panel.animPanel-1 && state[7] == true){
			   state[6] = false;
			   num = 0;
			}
		}
	}
}
		
	
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

		int wheel = e.getWheelRotation();

			if(wheel >= 1 && bushSize > 2){
			   bushSize-=2;
			   
			}else if(bushSize <= 80){
					 bushSize+=2;	
			}
			repaint(0);
	}

	@Override
	public void keyPressed(KeyEvent c) {
		// Open instruction 
		if (c.getKeyCode() == 112){
			Instruction = getImage(getDocumentBase(), "instructions.png");
			state[8] = false;
		}
		
		if (c.getKeyCode() == 113){
			state[8] = true;
		}
		
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
			mouseMoveX = e.getX();
			mouseMoveY = e.getY();

		if (state[2] == true){
			oldMouseX = newMouseX;
			oldMouseY = newMouseY;
		
			newMouseX = e.getX()/drawRes*drawRes;
			newMouseY = e.getY()/drawRes*drawRes;
		
			Graphics2D g2D = (Graphics2D) backg;
			g2D.setStroke(new BasicStroke(bushSize));
			g2D.setRenderingHint( RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			
			backg.setColor(color);
			backg.drawLine(oldMouseX, oldMouseY,newMouseX,newMouseY);
	
		}else{
			oldMouseX = 0;
			oldMouseY = 0;
		}

		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		newMouseX = e.getX()/drawRes*drawRes;
		newMouseY = e.getY()/drawRes*drawRes;
		
		mouseMoveX = e.getX();
		mouseMoveY = e.getY();
		
		repaint(0);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (state[2] == true){
			oldMouseX = newMouseX;
			oldMouseY = newMouseY;
	
			newMouseX = e.getX()/drawRes*drawRes;
			newMouseY = e.getY()/drawRes*drawRes;
	
			Graphics2D g2D = (Graphics2D) backg;
			g2D.setStroke(new BasicStroke(bushSize));
			g2D.setRenderingHint( RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			backg.setColor(color);
			backg.drawLine(oldMouseX, oldMouseY,newMouseX,newMouseY);
		}
			state[1] = true;
			repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		state[0] = false;
		state[1] = false;
		
		repaint();
	}	
}