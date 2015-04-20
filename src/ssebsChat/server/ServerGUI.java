package ssebsChat.server;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import ssebsChat.Login;

public class ServerGUI extends JFrame
{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtPort;
	private boolean startPressed = false;

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					ServerGUI frame = new ServerGUI();
					frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServerGUI()
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/com/sun/javafx/scene/control/skin/caspian/fxvk-capslock-button.png")));
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 200);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblPort = new JLabel("Port");
		lblPort.setHorizontalAlignment(SwingConstants.CENTER);
		lblPort.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblPort.setBounds(99, 11, 86, 30);
		contentPane.add(lblPort);

		txtPort = new JTextField();
		txtPort.setBounds(99, 52, 86, 20);
		contentPane.add(txtPort);
		txtPort.setColumns(10);

		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				int port;
				if (txtPort.getText().length() < 5)
				{
					port = Integer.parseInt(txtPort.getText());

					if (port < Math.pow(2, 16))
					{
						if (!startPressed)
						{
							txtPort.setEditable(false);
							new ServerMain(port);
							startPressed = true;
						}
					}
				}
			}
		});
		btnStart.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnStart.setBounds(73, 95, 137, 39);
		contentPane.add(btnStart);
	}
}
