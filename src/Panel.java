import java.awt.Color;
import java.awt.Graphics;

public class Panel {
	int position[][] = new int[2][100];
	int dimension[][] = new int[2][100];
	boolean hover[] = new boolean[100];

	Color color[] = new Color[100];
	String str[] = new String[100];
	Color chooser[] = new Color[]{new Color(12,12,12),new Color(240,240,240),new Color(191, 127, 63),new Color(191, 191, 63),
					  new Color(127, 191, 63),new Color(63, 191, 63),new Color(63, 191, 127),new Color(63, 191, 191),
					  new Color(63, 127, 191),new Color(63, 63, 191),new Color(127, 63, 191),new Color(191, 63, 191),
					  new Color(165, 89, 89),new Color(165, 127, 89), new Color(114, 114, 38), new Color(51, 153, 102),
					  new Color(114, 38, 76),new Color(38, 38, 114),new Color(51, 25, 76),new Color(191, 63, 63),
			  		  new Color(36, 72, 81),new Color(130, 130, 130),new Color(154, 34, 0),new Color(221, 49, 100)};

	int obj_Cnt = 0, // obj counter
		screenW = 0,
		animPanel = 0,
		screenH = 0;
	
	
	void setArray(int x,int y,int w,int h,Color color,String str,int index,boolean hover){
		if(this.hover[index] == false){
			if(obj_Cnt < index+1){
			   obj_Cnt = index+1;
			}
		
			position[0][index] = x;
			position[1][index] = y;
		
			dimension[0][index] = w;
			dimension[1][index] = h;
		
			this.color[index] = color;
			this.str[index] = str;
			this.hover[index] = hover;	
		}
	}
	

	
	public void setParimeters(){

		// ----- // Top and side menu
		setArray(10,135,150,screenH-150,new Color(120,120,120),"",0,false);
		setArray(10,-2,screenW,120,new Color(120,120,120),"",1,false);
		
		// ----- // Buttons
		setArray(100,10,60,30,new Color(120, 160, 100),"Start",2,true);
		setArray(100,45,60,30,new Color(97, 122, 126),"Loop",3,true);
		setArray(100,80,60,30,new Color(171, 61, 63),"Stop",4,true);
		setArray(10,0,60,30,new Color(48, 115, 199),"Open",5,true);
		setArray(10,28,60,30,new Color(58, 158, 189),"Save",6,true);

		// ----- // Paint tools
		setArray(10,365,150,30,new Color(64, 102, 126),"Paint tools",7,true);
		
		// ----- // Layers view pot
		setArray(10,365,150,30,new Color(126, 71, 64),"Layers",8,true);	
		
		
		// ----- // Animation view port
		setArray(170,10,(screenW/100*100)-175,100,new Color(193, 196, 186),"",9,false);
		setArray(180,20,95,80,new Color(106, 171, 64),"",10,true);
		
		// ----- // Color Chooser
		int d = -4;
		for(int j = 0; j < 6;j++){ d+=4;
			for(int i = 0; i < 4;i++){	
				setArray(17+(i*35),143+(35*j),30,30,chooser[(d+i)],"",((11+d)+i),true);
			}
		}
		
		// ----- // Added animation slots
		for(int i = 0; i < animPanel;i++){
			setArray(position[0][i+35],20,95,80,new Color(120, 120, 120),"",i+35,true);
		}
	}
}