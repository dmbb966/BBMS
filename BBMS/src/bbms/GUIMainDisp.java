package bbms;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class GUIMainDisp extends JPanel{
	
	public GUIMainDisp() {
		setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));

		JTabbedPane jtp = new JTabbedPane();
		jtp.addTab("Main", new CitiesPanel());
			
		this.add(jtp);
	}
}

@SuppressWarnings("serial")
class CitiesPanel extends JPanel {

	public CitiesPanel() {
		JButton b1 = new JButton("Berlin");
		add(b1);
		JButton b2 = new JButton("London");
		add(b2);
		JButton b3 = new JButton("Rome");
		add(b3);
		JButton b4 = new JButton("Moscow");
		add(b4);
	}
}
