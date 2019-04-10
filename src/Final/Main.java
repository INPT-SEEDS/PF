package Final;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Main
{
	static boolean allRoutes;
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.setSize(500, 250);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setLayout(null);
		frame.setResizable(false);
		
		JButton filtre=GUI.NewButton("Filtre", 10, 10); frame.add(filtre);
			frame.add(GUI.NewLabel("All routes (rural, residential...)",17, 170, 20));
			JCheckBox all=GUI.NewCheckBox("", 139, 20); frame.add(all);
		JButton segment=GUI.NewButton("Segment", 10, 60); frame.add(segment);
			frame.add(GUI.NewLabel("the segments maximum distance(km)",17, 170, 70));
			JTextField maxDistance=GUI.NewTextField(30, 30, 135, 65); frame.add(maxDistance);
		JButton show=GUI.NewButton("Map", 10, 110); frame.add(show);
		
		//-------whether-we-selected-(all-the-routes)
		allRoutes=false;
		
		all.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				allRoutes=all.isSelected();
			}
		});
		
		filtre.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				DBFilter.Filter(allRoutes);
			}
		});

		segment.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				try
				{
					if(Integer.valueOf(maxDistance.getText())>0)
					{
						RouteSegmentation.Segment(Integer.valueOf(maxDistance.getText()));
					}
					else
					{
						throw new NumberFormatException();
					}
				}
				catch(NumberFormatException e1)
				{
					JOptionPane.showMessageDialog(frame,"Enter a valid, positive integer.","Warning",JOptionPane.WARNING_MESSAGE);
				}
				
			}
		});
		show.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				DrawTest.main(null);
				frame.dispose();
			}
		});
		frame.setVisible(true);
	}

}
