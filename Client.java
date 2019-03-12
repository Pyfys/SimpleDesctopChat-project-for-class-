/**
 *
 *  @author Shkambara Dmytro S15163
 *
 */

package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.StringTokenizer;

public class Client {
	protected String username;
	protected SocketChannel serverSocket;
	private Charset charset = Charset.forName("UTF-8");
	protected boolean isRunning = true;
	
	public Client(String username,int port) {
		this.username = username;
		try {
			SocketChannel socket = SocketChannel.open();
			socket.connect(new InetSocketAddress("localhost", port));
			serverSocket = socket;
			
			MainChat chat = new MainChat(this);
			new Thread () {
				public void run() {
					while(isRunning) {
						
						String msg = read(serverSocket);
						StringTokenizer tk = new StringTokenizer(msg, " ");
						if(!(tk.nextToken().equals(username+":"))) chat.AppendChat(msg);
					}
				}
			}.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String read(SocketChannel sc) {
		if(sc.isOpen()) {
			String msg = "";
			ByteBuffer buf = ByteBuffer.allocate(1024);
			buf.clear();
			
			try {
				
					int n = sc.read(buf);
					
					if(n>0) {
						
						byte[] data = new byte[n];
						
						System.arraycopy(buf.array(), 0, data, 0, n);
						msg = new String(data);
					}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.exit(1);
			}
			return msg;
			
		}else {
			return null;
			
		}
	}
	public void write(String msg) {
		
		ByteBuffer buf = ByteBuffer.allocate(1024);
		buf.clear();
		buf = charset.encode(msg);
		
		try {
			
			while(buf.hasRemaining()) {
				serverSocket.write(buf);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

  public static void main(String[] args) {
	  GUI gui = new GUI();
	  gui.LogWindow();
  }
}
