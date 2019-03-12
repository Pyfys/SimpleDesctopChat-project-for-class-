/**
 *
 *  @author Shkambara Dmytro S15163
 *
 */

package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class Server {
	
	private ServerSocketChannel socket;
	private Selector selector;
	private Charset charset = Charset.forName("UTF-8");
	
	public Server(int port) {
		try {
			
			socket = ServerSocketChannel.open();
			socket.configureBlocking(false);
			socket.socket().bind(new InetSocketAddress("localhost",port));
			
			selector = Selector.open();
			socket.register(selector, SelectionKey.OP_ACCEPT);
			
			while (true) {
				
				try {
					
					selector.select();
					Set<?> key = selector.selectedKeys();
					Iterator<?> iterator = key.iterator();
					
					while(iterator.hasNext()) {
						
						SelectionKey sk = (SelectionKey) iterator.next();
						
						if(!(sk.isValid())) continue;
						
						else if(sk.isAcceptable()) {
							
							SocketChannel sc = socket.accept();
							
							if(sc != null) {
								
								sc.configureBlocking(false);
								sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
								
							}
						}else if(sk.isReadable()) {
							
							SocketChannel sc = (SocketChannel) sk.channel();
							String msg = read(sc);
				
							if(!(msg.equals(""))) {
								writeAll(msg);
							}
						}
					}
				} catch (IOException e) {
					System.exit(1);
					e.printStackTrace();
				}
				
			}
			
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
						
				}else if(n<0) {
					buf.clear();
					sc.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			return msg;
			
		}else {
			return "";
			
		}
	}
	
	public void writeAll(String msg) {
		try {
			selector.select();
			Set<?> key = selector.selectedKeys();
			Iterator<?> iterator = key.iterator();
			
			while(iterator.hasNext()) {
				SelectionKey sk = (SelectionKey) iterator.next();
				
				if(sk.isWritable()) {
					SocketChannel channel = (SocketChannel) sk.channel();
					write(msg,channel);
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void write(String msg, SocketChannel sc) {
		
		ByteBuffer buf = ByteBuffer.allocate(1024);
		buf.clear();
		buf = charset.encode(msg);
		
		try {

			while(buf.hasRemaining()) {
				sc.write(buf);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

  public static void main(String[] args) {
	  new Server(8457);
  }
}
