package sleitnick.roblox.launcher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.PlainDocument;

public class RobloxLauncherApp implements ActionListener {
	
	private JFrame frame;
	private JPanel panel;
	private JPanel gamePanel;
	private JLabel logoLabel;
	private JLabel idLabel;
	private JTextField idField;
	private JButton searchButton;
	private JLabel gameTitle, gameCreator;
	private JLabel gameImage;
	private JButton launchButton;
	private JButton launchBrowserButton;
	
	private final Color background = new Color(75, 75, 75);
	
	private final String ID_INPUT_KEY = "idInput";
	
	private RobloxPlace currentSearch = null;
	
	private void createGui() {
		frame = new JFrame("ROBLOX Launcher");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setBackground(background);
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel.setBackground(background);
		gamePanel = new JPanel();
		gamePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.PAGE_AXIS));
		gamePanel.setBackground(background);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.getContentPane().add(gamePanel, BorderLayout.SOUTH);
		URL logoURL = getClass().getResource("logo.png");
		ImageIcon logo = new ImageIcon(logoURL);
		logoLabel = new JLabel(logo);
		logoLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		idLabel = new JLabel("Place ID:");
		idLabel.setFont(new Font("Arial", Font.BOLD, 14));
		idLabel.setForeground(new Color(255, 255, 255));
		idField = new JTextField(20);
		idField.setFont(new Font("Arial", Font.PLAIN, 12));
		{
			Object idObj = DataStoreService.get(ID_INPUT_KEY);
			if (idObj != null && idObj instanceof Integer) {
				int id = (Integer)idObj;
				idField.setText(Integer.toString(id));
			}
		}
		idLabel.setLabelFor(idField);
		searchButton = new JButton("Search");
		searchButton.setBackground(background);
		searchButton.setFont(new Font("Arial", Font.PLAIN, 12));
		frame.getContentPane().add(logoLabel, BorderLayout.NORTH);
		panel.add(idLabel);
		panel.add(idField);
		panel.add(searchButton);
		gameTitle = new JLabel();
		gameTitle.setBorder(new EmptyBorder(10, 0, 0, 0));
		gameTitle.setFont(new Font("Arial", Font.PLAIN, 18));
		gameTitle.setForeground(new Color(255, 255, 255));
		gameCreator = new JLabel();
		gameCreator.setFont(new Font("Arial", Font.PLAIN, 14));
		gameCreator.setForeground(new Color(255, 255, 255));
		gameImage = new JLabel();
		gameImage.setBorder(new EmptyBorder(10, 0, 10, 0));
		JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
		sep.setBackground(new Color(100, 100, 100));
		sep.setForeground(background);
		gamePanel.add(sep);
		gamePanel.add(gameTitle);
		gamePanel.add(gameCreator);
		gamePanel.add(gameImage);
		launchButton = new JButton("Play");
		launchButton.setBackground(background);
		launchButton.setFont(new Font("Arial", Font.BOLD, 14));
		launchBrowserButton = new JButton("Open in Browser");
		launchBrowserButton.setBackground(background);
		launchBrowserButton.setFont(new Font("Arial", Font.PLAIN, 14));
		gamePanel.add(launchBrowserButton);
		gamePanel.add(launchButton);
		frame.pack();
		frame.setLocation(100, 100);
		URL iconURL = getClass().getResource("icon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		frame.setIconImage(icon.getImage());
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				DataStoreService.save();
				super.windowClosing(e);
			}
		});
		frame.setVisible(true);
	}
	
	private void setIdFieldIntegerOnly() {
		PlainDocument doc = (PlainDocument)idField.getDocument();
		doc.setDocumentFilter(new IntegerDocumentFilter());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("launchGame")) {
			if (!launchButton.isEnabled()) {
				return;
			}
			String idStr = idField.getText().trim();
			if (!idStr.isEmpty()) {
				int id = Integer.parseInt(idStr);
				launchButton.setEnabled(false);
				try {
					RobloxPlace place = new RobloxPlace(id);
					RobloxLauncher.launch(place, frame);
				} catch(RobloxVersionException rbxVersionException) {
					Browser.browse(RobloxLauncher.URL_DOWNLOAD_ROBLOX);
					System.exit(0);
				} catch (RobloxPlaceException e1) {
					gameTitle.setText("Invalid place ID");
					gameCreator.setText("");
					gameImage.setIcon(null);
					launchButton.setVisible(false);
					launchBrowserButton.setVisible(false);
				}
				launchButton.setEnabled(true);
			}
		} else if (e.getActionCommand().equals("searchGame")) {
			if (!idField.getText().isEmpty()) {
				int id = Integer.parseInt(idField.getText());
				try {
					frame.getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					idField.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					RobloxPlace place = new RobloxPlace(id);
					currentSearch = place;
					gameTitle.setText(place.getPlaceName());
					gameCreator.setText("Creator: " + place.getCreator());
					try {
						URL url = new URL(place.getThumbnail());
						ImageIcon gameIcon = new ImageIcon(url);
						Image img = gameIcon.getImage();
						img = img.getScaledInstance((int)(img.getWidth(null) / 1.2f), (int)(img.getHeight(null) / 1.2f), Image.SCALE_SMOOTH);
						gameIcon = new ImageIcon(img);
						gameImage.setIcon(gameIcon);
					} catch (Exception e1) {
						gameImage.setIcon(null);
					}
					DataStoreService.set(ID_INPUT_KEY, id);
					launchButton.setVisible(true);
					launchBrowserButton.setVisible(true);
					frame.pack();
				} catch (RobloxPlaceException e1) {
					gameTitle.setText(e1.getFailedToConnect() ? "Cannot connect to ROBLOX" : "Invalid place ID");
					gameCreator.setText("");
					gameImage.setIcon(null);
					launchButton.setVisible(false);
					launchBrowserButton.setVisible(false);
				} finally {
					frame.getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					idField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
				}
				frame.pack();
			}
		} else if (e.getActionCommand().equals("openInBrowser")) {
			if (currentSearch != null) {
				currentSearch.openInBrowser();
			}
		}
	}
	
	public RobloxLauncherApp() {
		createGui();
		setIdFieldIntegerOnly();
		searchButton.setActionCommand("searchGame");
		searchButton.addActionListener(this);
		launchButton.setActionCommand("launchGame");
		launchButton.addActionListener(this);
		launchBrowserButton.setActionCommand("openInBrowser");
		launchBrowserButton.addActionListener(this);
		idField.setActionCommand("searchGame");
		idField.addActionListener(this);
		idField.requestFocus();
		idField.setCaretPosition(idField.getText().length());
		if (!idField.getText().isEmpty()) {
			for (ActionListener listener : idField.getActionListeners()) {
				listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "searchGame") {
					private static final long serialVersionUID = 1L;
				});
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new RobloxLauncherApp();
	}

}
