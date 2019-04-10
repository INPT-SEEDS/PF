package Final;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GUI
{

	static float scalex = 1;
	static float scaley = 1;


	// -------------------NewLabel--------------------------------------------------------------------------------------

	public static JLabel NewLabel(String text, Color color, int size, int width, int height, int x, int y)
	{
		JLabel Label = new JLabel(text);
		Label.setFont(new Font("Century Gothic", Font.PLAIN, (int) (GUI.scalex * size)));
		Label.setForeground(color);
		Label.setBounds((int) (GUI.scalex * x), (int) (GUI.scaley * y), (int) (GUI.scalex * width),
				(int) (GUI.scaley * height));
		Label.setHorizontalAlignment(SwingConstants.LEFT);
		return Label;
	}

	public static JLabel NewLabel(String text, int size, int x, int y)
	{
		return GUI.NewLabel(text, Color.BLACK, size, x, y);
	}

	public static JLabel NewLabel(String text, Color color, int size, int x, int y)
	{
		return NewLabel(text, color, size, (int) (text.length() * size / 1.3), (int) (size * 1.2), x, y);
	}

	// --------------------NewButton------------------------------------------------------------------------------------

	public static JButton NewButton(String text,String path, Color color, int size, int width, int height, int x, int y)
	{
		JButton Button;
		if (path==null)
		{
			Button = new JButton(text);
		}
		else 
		{
			ImageIcon image=new ImageIcon(path);
			Image img = image.getImage();
			Image newimg = img.getScaledInstance((int) (GUI.scalex * (height-5)), (int) (GUI.scaley * (height-5)),java.awt.Image.SCALE_SMOOTH);
			image = new ImageIcon(newimg);
			Button = new JButton(text,image);
		}
		Button.setBackground(color);
		Button.setFont(new Font("Century Gothic", Font.BOLD, (int) (GUI.scalex * size)));
		Button.setForeground(Color.WHITE);
		Button.setLocation((int) (GUI.scalex * x), (int) (GUI.scaley * y));
		Button.setSize((int) (GUI.scalex * width), (int) (GUI.scaley * height));
		Button.setFocusPainted(false);

		return Button;
	}

	public static JButton NewButton(String text, Color color, int size, int width, int height, int x, int y)
	{
		return GUI.NewButton(text,null,color, size, width, height,x, y);
	}
	public static JButton NewButton(String test, int x, int y)
	{
		return GUI.NewButton(test, 20, 120, 40, x, y);
	}

	public static JButton NewButton(String test, int size, int x, int y)
	{
		return GUI.NewButton(test, size, 120, 40, x, y);
	}

	public static JButton NewButton(String test, Color color, int x, int y)
	{
		return NewButton(test, color, 20, 120, 40, x, y);
	}

	public static JButton NewButton(String test, Color color, int size, int x, int y)
	{
		return NewButton(test, color, size, 120, 40, x, y);
	}

	public static JButton NewButton(String text, int size, int width, int height, int x, int y)
	{
		return NewButton(text, new Color(0, 175, 255), size, width, height, x, y);
	}
	public static JButton NewButton(String text,String path, int size, int width, int height, int x, int y)
	{
		return NewButton(text,path, new Color(0, 175, 255), size, width, height, x, y);
	}

	// ----------------------NewTextPane--------------------------------------------------------------------------------

	public static JTextField NewTextField(int width, int height, int x, int y)
	{
		JTextField TextField = new JTextField();
		TextField.setFont(new Font("Century Gothic", Font.PLAIN, (int) (GUI.scaley * (height - 13))));
		TextField.setLocation((int) (GUI.scalex * x), (int) (GUI.scaley * y));
		TextField.setSize((int) (GUI.scalex * width), (int) (GUI.scaley * height));
		return TextField;
	}

	public static JPasswordField NewPasswordField(int width, int height, int x, int y)
	{
		JPasswordField PasswordFiel = new JPasswordField();
		PasswordFiel.setFont(new Font("Century Gothic", Font.PLAIN, (int) (GUI.scaley * (height - 13))));
		PasswordFiel.setLocation((int) (GUI.scalex * x), (int) (GUI.scaley * y));
		PasswordFiel.setSize((int) (GUI.scalex * width), (int) (GUI.scaley * height));
		return PasswordFiel;
	}

	// -------------------NewImage--------------------------------------------------------------------------------------
	public static JLabel NewImage(String path, int w, int h, int x, int y)
	{
		ImageIcon img = new ImageIcon(path);
		JLabel label = new JLabel();

		Image image = img.getImage();
		Image newimg = image.getScaledInstance((int) (GUI.scalex * w), (int) (GUI.scaley * h),
				java.awt.Image.SCALE_SMOOTH);
		img = new ImageIcon(newimg);
		label.setBounds(x, y, img.getIconWidth(), img.getIconHeight());
		label.setIcon(img);
		return label;
	}
	public static JLabel NewImage(String path, int x, int y)
	{
		ImageIcon img = new ImageIcon(path);
		return NewImage(path, img.getIconWidth(), img.getIconHeight(), x, y);
	}

	// --------------------NewComboBox----------------------------------------------------------------------------------
	public static JComboBox<String> NewComboBox(String[] list, int w, int h, int x, int y)
	{
		JComboBox<String> ComboBox = new JComboBox<>(list);
		ComboBox.setFont(new Font("Century Gothic", Font.PLAIN, (int) (GUI.scaley * 18)));
		ComboBox.setBounds((int) (GUI.scalex * x), (int) (GUI.scaley * y), (int) (GUI.scalex * w),
				(int) (GUI.scaley * h));
		ComboBox.setBackground(Color.white);
		return ComboBox;
	}

	public static JComboBox<String> NewComboBox(String[] list, int x, int y)
	{
		return NewComboBox(list, 350, 35, x, y);
	}

	// --------------------NewCheckBox----------------------------------------------------------------------------------
	public static JCheckBox NewCheckBox(String text, int x, int y)
	{

		JCheckBox CheckBox = new JCheckBox(text);
		CheckBox.setFont(new Font("Century Gothic", Font.PLAIN, (int) (GUI.scalex * 18)));
		CheckBox.setBounds((int) (GUI.scalex * x), (int) (GUI.scaley * y), (int) (GUI.scalex * (text.length()+2) * 13),
				(int) (GUI.scalex * 20));
		return CheckBox;
	}
	

}
