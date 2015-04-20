package ssebsChat;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

public class ClientWindow extends JFrame implements Runnable
{
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField txtMessage;
	private JTextArea history;
	private DefaultCaret caret;
	private Client client;
	private Thread run, listen;
	private boolean running = false;

	public ClientWindow(String name, String address, int port)
	{
		client = new Client(name, address, port);
		boolean connect = client.openConnection(address);
		if (!connect)
		{
			System.err.println("Connection Failed");
			console("Connection Failed");
			return;
		}
		createWindow();
		console("Attempting connection to " + address + ":" + port + ", user: " + name);
		String connection = "/c/" + name;
		client.send(connection.getBytes());
		running = true;
		run = new Thread(this, "Running");
		run.start();

	}

	public void listen()
	{
		listen = new Thread("Listen")
		{
			@Override
			public void run()
			{
				while (running)
				{
					String message = client.receive();
					if (message.startsWith("/c/"))
					{
						client.setID(Integer.parseInt(message.substring(3).trim()));
						console("Succesfully connected to server! ID: " + client.getID());
					} else if (message.startsWith("/m/"))
					{
						String[] text = message.split("/m/|/e/");
						console(text[1]);
					}
				}
			}
		};
		listen.start();

	}

	@Override
	public void run()
	{
		listen();
	}

	private void send(String message, boolean text)
	{
		if (message.equals(""))
			return;

		if (text)
		{
			message = client.getName() + ": " + message;
			message = "/m/" + message;
			message = message.trim();
		}
		client.send(message.getBytes());
		txtMessage.setText("");
	}

	public void console(String text)
	{
		text = text.trim();
		history.append(text + "\n");
	}

	private void createWindow()
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage(Client.class.getResource("/com/sun/javafx/scene/control/skin/caspian/fxvk-capslock-button.png")));
		setTitle("ssebs Chat Client");
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 500);
		setMinimumSize(new Dimension(300, 300));
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 50, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		history = new JTextArea();
		history.setEditable(false);

		caret = (DefaultCaret) history.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		JScrollPane scroll = new JScrollPane(history);
		GridBagConstraints scrollContraints = new GridBagConstraints();
		scrollContraints.gridwidth = 2;
		scrollContraints.insets = new Insets(0, 0, 5, 0);
		scrollContraints.fill = GridBagConstraints.BOTH;
		scrollContraints.gridx = 0;
		scrollContraints.gridy = 0;
		contentPane.add(scroll, scrollContraints);

		txtMessage = new JTextField();
		txtMessage.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					send(txtMessage.getText(), true);
				}
			}
		});
		txtMessage.setToolTipText("");
		GridBagConstraints gbc_txtMessage = new GridBagConstraints();
		gbc_txtMessage.insets = new Insets(0, 0, 0, 5);
		gbc_txtMessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMessage.gridx = 0;
		gbc_txtMessage.gridy = 1;
		contentPane.add(txtMessage, gbc_txtMessage);
		txtMessage.setColumns(10);

		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				send(txtMessage.getText(), true);
			}
		});
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.gridx = 1;
		gbc_btnSend.gridy = 1;
		contentPane.add(btnSend, gbc_btnSend);

		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				String disconnect = "/d/" + client.getID() + "/e/";
				send(disconnect, false);
				System.exit(0);
				client.close();
				running = false;
				
			}
		});

		setVisible(true);
		txtMessage.requestFocusInWindow();
	}

}