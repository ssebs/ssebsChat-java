package ssebsChat;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame
{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtAddress;
	private JTextField txtPort;

	public Login()
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/com/sun/javafx/scene/control/skin/caspian/fxvk-capslock-button.png")));
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		setForeground(Color.LIGHT_GRAY);
		setResizable(false);
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setBounds(100, 100, 300, 375);
		setSize(300, 375);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		txtName = new JTextField();
		txtName.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtName.setBounds(64, 56, 165, 20);
		contentPane.add(txtName);
		txtName.setColumns(10);

		JLabel lblName = new JLabel("Name");
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		lblName.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblName.setBounds(105, 24, 84, 33);
		contentPane.add(lblName);

		txtAddress = new JTextField();
		txtAddress.setBounds(64, 128, 165, 20);
		contentPane.add(txtAddress);
		txtAddress.setColumns(10);

		JLabel lblIpaddress = new JLabel("IP Address");
		lblIpaddress.setHorizontalAlignment(SwingConstants.CENTER);
		lblIpaddress.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblIpaddress.setBounds(105, 94, 84, 33);
		contentPane.add(lblIpaddress);

		JLabel lblPort = new JLabel("Port");
		lblPort.setHorizontalAlignment(SwingConstants.CENTER);
		lblPort.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPort.setBounds(105, 159, 84, 33);
		contentPane.add(lblPort);

		txtPort = new JTextField();
		txtPort.setColumns(10);
		txtPort.setBounds(64, 193, 165, 20);
		contentPane.add(txtPort);

		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{// login button pressed
				String name = txtName.getText();
				String address = txtAddress.getText();
				int port;
				if (!txtPort.getText().equals(""))
				{
					port = Integer.parseInt(txtPort.getText());
				} else
				{
					port = 0;
				}
				if (port != 0)
				{
					login(name, address, port);
				}
			}
		});
		btnLogin.setBackground(Color.WHITE);
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnLogin.setBounds(105, 261, 89, 23);
		contentPane.add(btnLogin);
	}

	private void login(String name, String address, int port)
	{
		dispose();
		new ClientWindow(name, address, port);
	}

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
}
