package java_text_editor;

import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class About {

	private final JFrame frame;
	private final JPanel panel;
	private final JLabel text;
	private String contentText;
	private final ImageIcon helpIcon = new ImageIcon(getClass().getResource("/icons/help.png"));

	public About(Notepad app) {
		panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		frame = new JFrame();
		frame.setIconImage(helpIcon.getImage());

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}
		});

		frame.setVisible(true);
		frame.setSize(200, 100);
		frame.setLocationRelativeTo(app);
		text = new JLabel();
	}

	public void me() {
		frame.setTitle("About me - Patrik Olin");

		contentText = "<html><body><p>" + "Author: Patrik Olin <br />" + "Contact: hej@patrikolin.se"
				+ "</p></body></html>";

		text.setText(contentText);
		panel.add(text);
		frame.add(panel);
	}

	public void software() {
		frame.setTitle("About JPad");
		contentText = "<html><body><p>" + "JPad - a Notepad clone in Java <br />" + "Version: 2.0"
				+ "</p></body></html>";

		text.setText(contentText);
		panel.add(text);
		frame.add(panel);
	}

}
