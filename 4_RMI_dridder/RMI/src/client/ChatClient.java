package client;

import server.IChatServer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatClient extends JFrame implements IChatClientCallback {

	private static final long serialVersionUID = 1L;
	private static final int callbackPort = 0;
	private IChatServer serverObject;
	private String userName;
	private JMenuItem loginMenuItem;
	private JMenuItem logoutMenuItem;
	private JTextArea userList;
	private JTextField inputField;
	private JTextArea textArea;

	private boolean newline = false; // No newline symbol at the first write

	private ChatClient(String serverAddress) {
		// Setup GUI
		this.setBounds(25, 18, 500, 400);

		JMenuBar jMenuBar = new JMenuBar();
		this.setJMenuBar(jMenuBar);

		JMenu jMenu = new JMenu("Menu");
		jMenuBar.add(jMenu);

		loginMenuItem = new JMenuItem("Login");
		jMenu.add(loginMenuItem);
		loginMenuItem.addActionListener(new LoginMenuItemListener());

		logoutMenuItem = new JMenuItem("Logout");
		jMenu.add(logoutMenuItem);
		logoutMenuItem.addActionListener(new LogoutMenuItemListener());
		logoutMenuItem.setEnabled(false);

		JMenuItem exitMenuItem = new JMenuItem("Exit");
		jMenu.add(exitMenuItem);
		exitMenuItem.addActionListener(new ExitMenuItemListener());

		Container thisContent = this.getContentPane();
		this.setFont(new java.awt.Font("dialog", 0, 12));
		this.setTitle("RMI-Chat");

		JPanel eastPanel = new JPanel();
		eastPanel.setPreferredSize(new Dimension(100, 10));
		eastPanel.setBorder(new EtchedBorder());
		eastPanel.setMinimumSize(new Dimension(8, 25));

		userList = new JTextArea();
		Border userListBorder0 = new EmptyBorder(2, 2, 2, 2);
		userList.setBorder(userListBorder0);
		userList.setEditable(false);
		userList.setFont(new java.awt.Font("dialog", Font.BOLD, 12));

		BorderLayout eastPanelLayout = new BorderLayout();
		eastPanel.setLayout(eastPanelLayout);
		eastPanel.add(userList, BorderLayout.CENTER);

		JPanel southPanel = new JPanel();
		southPanel.setPreferredSize(new Dimension(10, 30));
		southPanel.setBorder(new EtchedBorder());

		inputField = new JTextField();
		Border inputFieldBorder0 = new EmptyBorder(2, 2, 2, 2);
		inputField.setBorder(inputFieldBorder0);
		inputField.setLayout(null);
		inputField.addActionListener(new InputFieldListener());

		BorderLayout southPanelLayout = new BorderLayout();
		southPanel.setLayout(southPanelLayout);
		southPanel.add(inputField, BorderLayout.CENTER);

		JPanel centerPanel = new JPanel();
		Border centerPanelBorder0 = new EtchedBorder();
		centerPanel.setBorder(centerPanelBorder0);

		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		textArea = new JTextArea();
		Border textAreaBorder0 = new EmptyBorder(2, 2, 2, 2);
		textArea.setBorder(textAreaBorder0);
		textArea.setEditable(false);
		textArea.setFont(new java.awt.Font("dialog", Font.BOLD, 12));
		jScrollPane.getViewport().setView(textArea);

		BorderLayout centerPanelLayout = new BorderLayout();
		centerPanel.setLayout(centerPanelLayout);
		centerPanel.add(jScrollPane, BorderLayout.CENTER);

		BorderLayout thisContentLayout = new BorderLayout();
		thisContent.setLayout(thisContentLayout);
		thisContent.add(eastPanel, BorderLayout.EAST);
		thisContent.add(southPanel, BorderLayout.SOUTH);
		thisContent.add(centerPanel, BorderLayout.CENTER);

		this.addWindowListener(new MyWindowListener());
		try {
			UnicastRemoteObject.exportObject(this, ChatClient.callbackPort);
			serverObject = (IChatServer) Naming.lookup("rmi://" + serverAddress
					+ "/chatServer");
		} catch (Exception e) {
			System.out.println("Exception" + e);
			e.printStackTrace();
		}
	}

	/**
	 * Callback-Methode: wird vom Server aufgerufen, wenn jemand eine Nachricht
	 * verschickt
	 */
	public void receiveChat(String userID, String message)
			throws RemoteException {
		addToChat(userID + ": " + message);
	}

	/**
	 * Callback-Methode: wird vom Server aufgerufen, wenn es einen neuen
	 * Benutzer gibt
	 */
	public void receiveUserLogin(String userID, Object[] users)
			throws RemoteException {
		UpdateUserList(users);
		addToChat(userID + " entered the Chat!");
	}

	/**
	 * Callback-Methode: wird vom Server aufgerufen, wenn ein Benutzer das
	 * System verlaesst
	 */
	public void receiveUserLogout(String userID, Object[] users)
			throws RemoteException {
		UpdateUserList(users);
		addToChat(userID + " left the Chat.");
	}

	/**
	 * Callback-Methode: wird vom Server aufgerufen, wenn ein Benutzer den
	 * besitzer des Callbacks anfluestern will
	 */
	public void whisper(String fromID, String message) throws RemoteException {
		addToChat("(" + fromID + " fluestert: " + message + ")");
	}

	public static void main(String[] args) {
		ChatClient chat = new ChatClient("localhost");
		chat.setVisible(true);
	}

	/**
	 * Wenn wir das Fenster schliessen, dann erfolgt ein Logout
	 */
	private class MyWindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			try {
				serverObject.logout(userName);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			System.exit(0);
		}
	}

	private class ExitMenuItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				serverObject.logout(userName);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			System.exit(0);
		}
	}

	private class LoginMenuItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String name = JOptionPane.showInputDialog(ChatClient.this,
					"Please enter a nickname.", "LOGIN",
					JOptionPane.PLAIN_MESSAGE);
			try {
				// fuehre Login durch
				if (serverObject.login(name, ChatClient.this)) {
					// falls erfolgreich
					userName = name;
					setTitle("RMI-Chat (" + name + ")");
					loginMenuItem.setEnabled(false);
					logoutMenuItem.setEnabled(true);
				} else {
					textArea.setText("\nThis name is already in use." + "\n"
							+ "Please choose another name.\n\n");
					loginMenuItem.setEnabled(true);
				}
			} catch (Exception ex) {
				System.err.println("Exception " + ex);
			}
			// inputField.setText("");
		}
	}

	private class LogoutMenuItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				// Logout
				serverObject.logout(userName);
				setTitle("RMI-Chat");
				userName = "";
				userList.setText("");
				loginMenuItem.setEnabled(true);
				logoutMenuItem.setEnabled(false);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private class InputFieldListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String whisperMessage = null;
			String[] messageParts = null;
			String message = inputField.getText();
			if (message.length() > 0) {
				try {
					if (message.startsWith("\\pn ")) { // pn
						message = message.substring("\\pn ".length());
						messageParts = message.split(" ", 2);

						if (messageParts.length == 0) {
							addToChat("Error: No User specified");
						} else if (messageParts.length == 1) {
							whisperMessage = ""; // Empty whisper
						} else {
							whisperMessage = messageParts[1];
						}

						if (whisperMessage != null) {
							boolean userrecived = serverObject.pn(userName,
									messageParts[0], whisperMessage);
							if (!userrecived) {
								addToChat("Error: Unknown User.");
							}
						}
					} else if (message.startsWith("\\")) { // Other Commands
						addToChat("Error: Unkonw Command.");
					} else {                               // Chat
						serverObject.chat(userName, message);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				inputField.setText("");
			}
		}
	}

	private void UpdateUserList(Object[] users) {
		StringBuilder userliststr = new StringBuilder();

		for (Object user : users) {
			userliststr.append(user.toString() + "\n");
		}

		userList.setText(userliststr.toString());
	}

	private void addToChat(String message) {
		if (newline) {
			textArea.append("\n");
		}
		textArea.append(message);
		newline = true;
	}
}