/**
   @author Patrik Olin
   @email hej@patrikolin.se
   @name JPad 2.0
   @date 2017-11-07
  	
  	Detta program är en enkel Notepad-klon som jag har knåpat ihop i syfte att lära mig mer Java, framförallt
  	att arbeta med GUIn. Ta koden och gör vad du vill med den.
  	
 */

package java_text_editor;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultEditorKit;

public class Notepad extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JTextArea textArea = new JTextArea("", 0, 0);

	// ICONS
	private final ImageIcon newIcon = new ImageIcon(getClass().getResource("/icons/new.png"));
	private final ImageIcon openIcon = new ImageIcon(getClass().getResource("/icons/open.png"));
	private final ImageIcon saveIcon = new ImageIcon(getClass().getResource("/icons/save.png"));
	private final ImageIcon closeIcon = new ImageIcon(getClass().getResource("/icons/exit.png"));
	private final ImageIcon cutIcon = new ImageIcon(getClass().getResource("/icons/cut.png"));
	private final ImageIcon copyIcon = new ImageIcon(getClass().getResource("/icons/copy.png"));
	private final ImageIcon pasteIcon = new ImageIcon(getClass().getResource("/icons/paste.png"));
	private final ImageIcon selectAllIcon = new ImageIcon(getClass().getResource("/icons/selectall.png"));
	private final ImageIcon jPadIcon = new ImageIcon(getClass().getResource("/icons/jpad.png"));
	private final ImageIcon helpIcon = new ImageIcon(getClass().getResource("/icons/help.png"));
	private final ImageIcon attentionIcon64 = new ImageIcon(getClass().getResource("/icons/attention64.png"));

	private String fileName = null;
	private boolean unsavedChanges = false;

	public Notepad() {
		this.setSize(500, 300); // Bestämmer den initiala storleken på fönstret
		this.setTitle("Untitled - JPad Text Editor"); // Sätter en titel på fönstret
		setDefaultCloseOperation(EXIT_ON_CLOSE); // Sätter default close operation.
		this.textArea.setFont(new Font("Century Gothic", Font.PLAIN, 12)); // Bestämmer default typsnitt
		this.getContentPane().setLayout(new BorderLayout()); // BorderLayout ser till så att textArea fyller fönstret
		this.getContentPane().add(textArea);
		this.setIconImage(jPadIcon.getImage());

		createMenuBar();

		textArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				unsavedChanges = true;
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				unsavedChanges = true;
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				unsavedChanges = true;
			}
		});

	}

	/**
	 * Skapar vår menylist i toppen av programmet. Den har tre menyer, File, Edit
	 * och About. Skapar också enskilda JMenuItems och stoppar in dom i sina
	 * respektive menyer.
	 */

	public void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu file = new JMenu("File");
		JMenu edit = new JMenu("Edit");
		JMenu about = new JMenu("About");

		// Skapar exitknappen i menyn. Den använder this.dispose() för att släppa alla
		// resurser fria och avslutar sedan programmet.
		JMenuItem mExit = new JMenuItem("Exit");
		mExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
		mExit.setToolTipText("Exit editor");
		mExit.setIcon(closeIcon);
		mExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exitApp(e);
			}
		});

		// Skapar en menyknapp för att skapa en ny fil. Markerar helt enkelt all text i
		// textArea och ändrar den sedan till "" (dvs inget)
		JMenuItem mNew = new JMenuItem("New file");
		mNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		mNew.setToolTipText("Create a new file");
		mNew.setIcon(newIcon);
		mNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newFile(e);
			}
		});

		// Skapar en menyknapp för att öppna en fil. Använder JFileChooser som har ett
		// filter för att bara visa textfiler, läser sedan in filen rad för rad med en
		// FileReader.
		JMenuItem mOpen = new JMenuItem("Open file");
		mOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		mOpen.setToolTipText("Open an existing file");
		mOpen.setIcon(openIcon);
		mOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openFile(e);
			}
		});

		// Skapar en menyknapp för att spara den öppna filen. Använder JFileChooser för
		// att välja var filen ska sparas och skriver den med en FileWriter.
		JMenuItem mSave = new JMenuItem("Save file");
		mSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		mSave.setToolTipText("Save your file");
		mSave.setIcon(saveIcon);
		mSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveFile(fileName);
			}
		});

		JMenuItem mCut = new JMenuItem(new DefaultEditorKit.CutAction());
		mCut.setText("Cut");
		mCut.setIcon(cutIcon);
		mCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
		mCut.setToolTipText("Cut selection");

		JMenuItem mCopy = new JMenuItem(new DefaultEditorKit.CopyAction());
		mCopy.setText("Copy");
		mCopy.setIcon(copyIcon);
		mCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
		mCopy.setToolTipText("Copy selection");

		JMenuItem mPaste = new JMenuItem(new DefaultEditorKit.PasteAction());
		mPaste.setText("Paste");
		mPaste.setIcon(pasteIcon);
		mPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
		mPaste.setToolTipText("Paste at location");

		JMenuItem mSelectAll = new JMenuItem("Select all");
		mSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
		mSelectAll.setToolTipText("Select all text");
		mSelectAll.setIcon(selectAllIcon);
		mSelectAll.addActionListener((ActionEvent event) -> {
			textArea.selectAll();
		});

		JMenuItem mAboutMe = new JMenuItem("About me");
		mAboutMe.setToolTipText("About the author of this software");
		mAboutMe.setIcon(helpIcon);
		mAboutMe.addActionListener((ActionEvent event) -> {
			new About(this).me();
		});

		JMenuItem mAboutApp = new JMenuItem("About JPad");
		mAboutApp.setToolTipText("Info about JPad");
		mAboutApp.setIcon(helpIcon);
		mAboutApp.addActionListener((ActionEvent event) -> {
			new About(this).software();
		});

		file.add(mNew);
		file.add(mOpen);
		file.add(mSave);
		file.add(mExit);

		edit.add(mCut);
		edit.add(mCopy);
		edit.add(mPaste);
		edit.add(mSelectAll);

		about.add(mAboutMe);
		about.add(mAboutApp);

		menuBar.add(file);
		menuBar.add(edit);
		menuBar.add(about);

		setJMenuBar(menuBar);
	}

	public void openFile(ActionEvent e) {
		if (unsavedChanges) {
			Object[] options = { "Save", "Don't save", "Cancel" };
			int n = JOptionPane.showOptionDialog(this, "Do you want to save your changes first?",
					"You have unsaved changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
					attentionIcon64, options, options[2]);
			if (n == 0) { // Save
				saveFile(fileName);
				unsavedChanges = false;
			} else if (n == 1) {
				JFileChooser fileOpener = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "TEXT FILES", "txt", "text");
				fileOpener.setFileFilter(filter);
				int option = fileOpener.showOpenDialog(this);

				if (option == JFileChooser.APPROVE_OPTION) {
					try {
						File openFile = fileOpener.getSelectedFile();
						Scanner scan = new Scanner(new FileReader(openFile.getPath()));
						setTitle(openFile.getName() + "- JPad text editor");
						fileName = openFile.getName();
						while (scan.hasNext()) {
							textArea.append(scan.next() + "\n");
						}
						scan.close();
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
				}
			} else if (n == 2) {

			}
		} else {
			JFileChooser fileOpener = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "TEXT FILES", "txt", "text");
			fileOpener.setFileFilter(filter);
			int option = fileOpener.showOpenDialog(this);

			if (option == JFileChooser.APPROVE_OPTION) {
				try {
					File openFile = fileOpener.getSelectedFile();
					Scanner scan = new Scanner(new FileReader(openFile.getPath()));
					setTitle(openFile.getName() + "- JPad text editor");
					fileName = openFile.getName();
					while (scan.hasNext()) {
						textArea.append(scan.next() + "\n");
					}
					scan.close();
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
			unsavedChanges = false;
		}
	}

	public void saveFile(String fileName) {
		JFileChooser save = new JFileChooser();
		int option = save.showSaveDialog(this);
		if (option == JFileChooser.APPROVE_OPTION) {
			try {
				File openFile = save.getSelectedFile();
				setTitle(openFile.getName() + "- JPad text editor");
				fileName = openFile.getName();
				BufferedWriter out = new BufferedWriter(new FileWriter(save.getSelectedFile().getPath()));
				out.write(this.textArea.getText());
				out.close();
				unsavedChanges = false;
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	public void exitApp(ActionEvent e) {
		if (unsavedChanges) {
			Object[] options = { "Save", "Don't save", "Cancel" };
			int n = JOptionPane.showOptionDialog(this, "Do you want to save your changes first?",
					"You have unsaved changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
					attentionIcon64, options, options[2]);
			if (n == 0) { // Save
				saveFile(fileName);
			} else if (n == 1) {
				unsavedChanges = false;
				this.dispose();
			}

		} else {
			this.dispose();
		}
	}

	public void newFile(ActionEvent e) {
		setTitle("Untitled - JPad text editor");
		if (unsavedChanges) {
			Object[] options = { "Save", "Don't save", "Cancel" };
			int n = JOptionPane.showOptionDialog(this, "Do you want to save your changes first?",
					"You have unsaved changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
					attentionIcon64, options, options[2]);
			if (n == 0) { // Save
				saveFile(fileName);
				unsavedChanges = false;
			} else if (n == 1) {
				this.textArea.selectAll();
				this.textArea.setText("");
				unsavedChanges = false;
			}

		} else {
			this.textArea.selectAll();
			this.textArea.setText("");
			unsavedChanges = false;
		}
	}

	// public void search() {
	// String editorText = textArea.getText();
	// String searchValue = searchField.getText();
	// }

	@Override
	public void actionPerformed(ActionEvent e) {
	}

}
