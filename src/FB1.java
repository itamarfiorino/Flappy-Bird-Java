import java.awt.*;

import javax.swing.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;

public class FB1 extends JComponent {
private static final long serialVersionUID = 1L;
public static int width, height, bottom1x, bottom2x, score=0;
public static double fba, fbda, fbdy=0;
public static Rectangle fbrect, button;
public static BufferedImage bg = null, bottom1=null, tbs = null, loseimg=null, startimg=null;
public static boolean addedtoscore=false, dead=false, click=false, start=true;
public static int floor=405, count=0, tbX, tbY, tbY2, tbX2, wing=0, highscore=0;
public static BufferedImage[] wings = new BufferedImage[3], medals=new BufferedImage[4]; 
public static ImageIcon[] wingsi = new ImageIcon[3]; 
public static JFrame frame = new JFrame("Flappy Bird");
public static AffineTransform tx;
public static AffineTransformOp op;
public static Thread animationThread;
public static Font sfont, dfont;
public static int[] extra =new int[4];
public static BufferedImage bufferedfromicon(ImageIcon icon){
	BufferedImage bi = new BufferedImage(
		    icon.getIconWidth(),
		    icon.getIconHeight(),
		    BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.createGraphics();
		// paint the Icon to the BufferedImage.
		icon.paintIcon(null, g, 0,0);
		g.dispose();
	return bi;
}
public static void lose(){
	if (click){
		fba=0;
		fbda=0;
		fbdy=0;
		dead=false;
		addedtoscore=false;
		score=0;
		tbX = width;
		tbY = (int)((-Math.random() * 150));
		tbY2 = (int)((-Math.random() * 150));
		tbX2 = (2*width)-150;
		fbrect.y=(height/2)-(wings[0].getHeight()/2);
		start=true;
	}
}
public FB1(){
	animationThread = new Thread(new Runnable() {
        public void run() {
            while (true) {
            	//FileReader fr = new FileReader(FB1.class.getResource("High_Score.txt"));
            	//highscore = cat;
            	if (!dead){
            		if (!start){
                	tbX --;
                	tbX2--;}
                	bottom1x--;
                	bottom2x--;
                	count++;
                	if (count%10==0){
                		wing++;
                	}
                	wing%=3;
            	}


            	if (!start){
            		fbrect.y+=fbdy;
	            	fbdy+=.05;
	            	fba+=fbda;
	            	fbda+=.1;}

            	//
            	if(tbX <= -tbs.getWidth()){
            		tbX = width;
            		tbY =  (int)((-Math.random() * 150));
            		
            		addedtoscore=false;
            	}
            	if(tbX2 <= -tbs.getWidth()){
            		tbX2 = width;
            		tbY2 =  (int)((-Math.random() * 150));
            		addedtoscore=false;
            	}
            	if ((tbX+(tbs.getWidth()/2)<fbrect.x+(fbrect.width/2) || tbX2+(tbs.getWidth()/2)<fbrect.x+(fbrect.width/2))&&!addedtoscore){
            		addedtoscore=true;
            		score++;
            	}
            	if (tbX<fbrect.x+fbrect.width && tbX+tbs.getWidth()>fbrect.x){
            		
            		if(fbrect.y+10<tbY+217 || fbrect.y+fbrect.height>tbY+340){
            			dead=true;
            		}
            	}
            	if (tbX2<fbrect.x+fbrect.width && tbX2+tbs.getWidth()>fbrect.x){
            		
            		if(fbrect.y+10<tbY2+217 || fbrect.y+fbrect.height>tbY2+340){
            			dead=true;
            		}
            	}
            	
            	if (fba>90){
            		fba=90;}
            	if (fba<-25){
            		fba=-25;}
            	if (bottom1x<-bottom1.getWidth()){
            		bottom1x=width;}
            	if (bottom2x<-bottom1.getWidth()){
            		bottom2x=width;}
            	
            	if (fbrect.y>floor){
            		dead=true;
            		fbrect.y=floor;
            		lose();
            		//dead=false;

            	}
                repaint();
                try {Thread.sleep(6);} catch (Exception ex) {}
            }
        }
    });

    animationThread.start();}
public void paintComponent(Graphics g) {
    Graphics2D gg = (Graphics2D) g;  
    gg.setColor(Color.BLACK);
    gg.drawImage(bg,0, 0, null);
    gg.drawImage(tbs, tbX2, (tbY2), null);
    gg.drawImage(tbs, (tbX), (tbY), null);
    tx = AffineTransform.getRotateInstance(Math.toRadians(fba), wings[0].getWidth()*.55, wings[0].getHeight()*.5);
    op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
    gg.drawImage(op.filter(wings[wing], null), fbrect.x, fbrect.y, null);
    gg.drawImage(bottom1, bottom1x, height-bottom1.getHeight(), null);      
    gg.drawImage(bottom1, bottom2x, height-bottom1.getHeight(), null); 
    //gg.drawRect(fbrect.x, fbrect.y+10, fbrect.width, fbrect.height-10);
    if (start){
    	gg.drawImage(startimg, 0, 0, null); 
    }
	if (dead){
		gg.drawImage(loseimg, 0, 0, null);  	   
	    gg.setFont(dfont);
	    gg.setColor(Color.BLACK);
	    if (score>highscore){
	    	highscore=score;
	    }
	    int outline=2;
	    for (int x=-1;x<2;x++){
	    	for (int y=-1;y<2;y++){
	    		gg.drawString(""+score,  260+(outline*x), 265+(outline*y));
	    		gg.drawString(""+highscore,  260+(outline*x), 315+(outline*y));}}
	    gg.setColor(Color.WHITE);
	    gg.drawString(""+score,  260, 265);
	    gg.drawString(""+highscore,  260, 315);
	    
	    if (score>=40){
	    	gg.drawImage(medals[3], 69, 250, null);}
	    else if (score>=30){
	    	gg.drawImage(medals[2], 69, 250, null);}
	    else if (score>=20){
	    	gg.drawImage(medals[1], 69, 250, null);}
	    else if (score>=10){
	    	gg.drawImage(medals[0], 69, 250, null);}
	    
		} 
	else{
	    gg.setFont(sfont);
	    gg.setColor(Color.BLACK);
	    int outline=2;
	    for (int x=-1;x<2;x++){
	    	for (int y=-1;y<2;y++){
	    		gg.drawString(""+score,  width/2+(outline*x), 50+(outline*y));
	    		
	    	}}
	    gg.setColor(Color.WHITE);
	    gg.drawString(""+score,  width/2, 50);
		} 	
}
public static void main(String[] args){
    bg =         bufferedfromicon(new ImageIcon(FB1.class.getResource("bg.png")));
    FB1.wings[0]=bufferedfromicon(new ImageIcon(FB1.class.getResource("1.png")));
    FB1.wings[1]=bufferedfromicon(new ImageIcon(FB1.class.getResource("2.png")));
    FB1.wings[2]=bufferedfromicon(new ImageIcon(FB1.class.getResource("3.png")));
    
    FB1.medals[0]=bufferedfromicon(new ImageIcon(FB1.class.getResource("bronze.png")));
    FB1.medals[1]=bufferedfromicon(new ImageIcon(FB1.class.getResource("silver.png")));
    FB1.medals[2]=bufferedfromicon(new ImageIcon(FB1.class.getResource("gold.png")));
    FB1.medals[3]=bufferedfromicon(new ImageIcon(FB1.class.getResource("platinum.png")));
    
    bottom1 =    bufferedfromicon(new ImageIcon(FB1.class.getResource("bottom.png")));
    loseimg =    bufferedfromicon(new ImageIcon(FB1.class.getResource("lose.png")));
    tbs =        bufferedfromicon(new ImageIcon(FB1.class.getResource("Tabv.png")));
    startimg =   bufferedfromicon(new ImageIcon(FB1.class.getResource("start.png")));
	width=bg.getWidth();
	height=bg.getHeight();
	bottom1x=0;
	bottom2x=width;
	tbX = width;
	tbY = (int)((-Math.random() * 150));
	tbY2 = (int)((-Math.random() * 150));
	tbX2 = (2*width)-150;
	JFrame temp = new JFrame("");
	temp.pack();
	Insets insets = temp.getInsets();
	temp = null;
	
	InputStream is = FB1.class.getResourceAsStream("04B_19__.TTF");
	if (is==null){
		System.out.println("not loaded");
	}else{
		System.out.println("loaded");
	}
	
	try {
		Font uniFont = Font.createFont(Font.TRUETYPE_FONT,is);
		dfont = uniFont.deriveFont(25f);
		sfont = uniFont.deriveFont(30f);
	} catch (FontFormatException | IOException e){
		e.printStackTrace();
	}
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setPreferredSize(new Dimension(width+insets.left+insets.right,height+insets.top+insets.bottom));
    frame.pack();
    extra[0]=insets.left;
    extra[1]=insets.right;
    extra[2]=insets.top;
    extra[3]=insets.bottom;
    width = frame.getContentPane().getSize().width;
    height = frame.getContentPane().getSize().height;
    fbrect = new Rectangle(40, (height/2)-(wings[0].getHeight()/2), wings[0].getWidth(), wings[0].getHeight());
    
    frame.requestFocus();
	
	 class MyKeyListener extends KeyAdapter{
       public void keyPressed(KeyEvent e){
	    int keyCode = e.getKeyCode();
	    if (keyCode==KeyEvent.VK_SPACE&&!dead){
	    	if (fbrect.y>0){
		    	fbda=-7;
		    	fbdy=-2;
			    if (start){
			    	start=false;}	
	    	}
	    }

       }
	}
	 class myMouseListener implements MouseListener {
		 @Override
		 public void mouseClicked(MouseEvent arg0) { }
		 @Override
		 public void mouseEntered(MouseEvent arg0) { }

		 @Override
		 public void mouseExited(MouseEvent arg0) { }

		 @Override
		 public void mousePressed(MouseEvent arg0) {
			 Rectangle button=new Rectangle(116, 369, 108, 38);
			 if (button.contains(arg0.getX()-extra[1], arg0.getY()-extra[2])){
				 click=true;}
			 if (!dead){
			    	if (fbrect.y>0){
				    	fbda=-7;
				    	fbdy=-2;
				    if (start){
				    	start=false;}				 
				    }
			 }
		 }

		 @Override
		 public void mouseReleased(MouseEvent arg0) {
			 click=false;
		 }
	 }
	
	frame.addKeyListener(new MyKeyListener());
	frame.addMouseListener(new myMouseListener());
	frame.setVisible(true);
    frame.setState(Frame.NORMAL);
    
    frame.setContentPane(new FB1());
    }
}