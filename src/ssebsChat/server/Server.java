package ssebsChat.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable
{

	private List<ServerClient> clients = new ArrayList<ServerClient>();

	private DatagramSocket socket;
	private int port;
	private boolean running = false;
	private Thread run, send, receive;

	public Server(int port)
	{
		this.port = port;
		try
		{
			socket = new DatagramSocket(port);
		} catch (SocketException e)
		{
			e.printStackTrace();
			return;
		}
		run = new Thread(this, "Server");
		run.start();
	}

	@Override
	public void run()
	{
		running = true;
		System.out.println("Server started on port " + port);
		receive();
	}

	private void receive()
	{
		receive = new Thread("Receive")
		{
			@Override
			public void run()
			{
				while (running)
				{
					byte[] data = new byte[1024];
					DatagramPacket packet = new DatagramPacket(data, data.length);
					try
					{
						socket.receive(packet);
					} catch (IOException e)
					{
						e.printStackTrace();
					}
					process(packet);
				}
			}
		};
		receive.start();
	}

	private void sendToAll(String message)
	{
		for (int i = 0; i < clients.size(); i++)
		{
			ServerClient client = clients.get(i);
			send(message.getBytes(), client.address, client.port);
		}
	}

	private void send(final byte[] data, final InetAddress address, final int port)
	{
		send = new Thread("Send")
		{
			@Override
			public void run()
			{
				DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
				try
				{
					socket.send(packet);
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		};
		send.start();
	}

	private void process(DatagramPacket packet)
	{
		String string;
		try
		{

			string = new String(packet.getData(), 0, packet.getLength(), "UTF-8");
			// ^ get string from packet
			if (string.startsWith("/c/"))
			{
				// UUID id = UUID.randomUUID();
				int id = UniqueIdentifier.getIdentifier();
				System.out.println("Identifier: " + id);
				clients.add(new ServerClient(string.substring(3, string.length()), packet.getAddress(), packet.getPort(), id));
				System.out.println(string.substring(3, string.length()));
				String ID = "/c/" + id;
				send(ID.getBytes(), packet.getAddress(), packet.getPort());
			}  else if (string.startsWith("/m/"))
			{
				sendToAll(string);
				System.out.println(string);
			} else if (string.startsWith("/d/"))
			{
				String id = string.split("/d/|/e/")[1];
				disconnect(Integer.parseInt(id), true);
			} else
			{
				System.out.println(string);
			}
		} catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}

	private void disconnect(int id, boolean status)
	{
		String name = "";
		for (int i = 0; i < clients.size(); i++)
		{
			if (clients.get(i).getID() == id)
			{
				name = clients.get(i).name;
				clients.remove(i);
				break;
			}
		}
		String msg = "";
		if (status)
		{
			msg = "Client " + name + " disconnected.";
		} else
		{
			msg = "Client " + name + " timed out.";
		}
		System.out.println(msg);
	}
}
