package ssebsChat.server;

public class ServerMain
{

	public ServerMain(int port)
	{
		new Server(port);
	}

	public static void main(String[] args)
	{
		int port = 0;

		if (args.length == 0)
		{
			port = 1336;
		} else if (args.length != 1)
		{
			System.err.println("Usage: java -jar ssebsChat.jar [port]");
			System.exit(1);
		} else
		{
			port = Integer.parseInt(args[0]);
		}

		new ServerMain(port);
	}
}
