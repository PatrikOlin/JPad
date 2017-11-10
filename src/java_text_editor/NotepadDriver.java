package java_text_editor;
import javax.swing.UIManager;

public class NotepadDriver {

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {

		}
		Notepad app = new Notepad();
		app.setVisible(true);
	}
}
