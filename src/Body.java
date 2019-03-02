import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Body {
	Panel panel = new Panel();	
	Font font = new Font("Franklin Gothic Book",32,16);
	
	void draw(Graphics g){
		panel.setParimeters();
		
		// ----- // Draws all objects 
		for(int i = 0; i < panel.obj_Cnt;i++){
			g.setColor(new Color(90,90,90));
			g.fillRoundRect(panel.position[0][i],panel.position[1][i],panel.dimension[0][i],panel.dimension[1][i],1,1);
			
			g.setColor(panel.color[i]);
			g.fillRoundRect(panel.position[0][i]+3,panel.position[1][i]+3,panel.dimension[0][i]-6,panel.dimension[1][i]-6,3,3);
			
			g.setFont(font);
			g.setColor(Color.black);
			g.drawString(panel.str[i]+"",(panel.dimension[0][i]/2-panel.dimension[0][i]/3)+panel.position[0][i], 
						(panel.dimension[1][i]/2-panel.dimension[1][i]/3)+panel.position[1][i]+15);	
		}
	}	
}