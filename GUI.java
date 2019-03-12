package zad1;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GUI implements ActionListener {
	
	private JTextField tf;
	private JPasswordField tf2;
	private JButton logButton;
	private JButton signButton;
	private String adress = System.getProperty("user.home")+"/Users.txt"; 
	private Path path = Paths.get(adress);
	private Charset charset = Charset.forName("UTF-8");
	private JFrame frame;
	
	public void LogWindow(){
		
		frame = new JFrame("LogWindow");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		
		logButton = new JButton("Zaloguj");
		logButton.addActionListener(this);
		signButton = new JButton("Rejestruj");
		signButton.addActionListener(this);
		
		JLabel label = new JLabel("<html><div style='text-align: center;'>Welcome to FacePoop<br/> We love to sell your personal data.</div></html>",SwingConstants.CENTER);
		label.setFont(new Font("Dialog",Font.PLAIN,17));
		
		
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(400,400));
		panel.setLayout(new GridLayout(5, 0));
		
		tf = new JTextField(1);
		tf.setEditable(true);
		tf2 = new JPasswordField();
		tf2.setEditable(true);
		
		panel.add(label);
		panel.add(tf);
		panel.add(tf2);
		panel.add(logButton);
		panel.add(signButton);
		
		
		
		frame.add(panel);
		frame.pack();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		HashMap<String,String> users = readFile();
		if(e.getSource() == logButton) {
			if(!(tf.getText().equals(""))) {
				
				if(users.containsKey(tf.getText())) {
				
					if(users.get(tf.getText()).toString().equals(tf2.getText())) {
						
						String username = tf.getText();
						frame.dispose();
						new Thread() {
							public void run() {
								new Client(username,8457);
							}
						}.start();
						
					}else {
					
						JOptionPane.showMessageDialog(null,"Incorrect password, please try again.");
					
					}
				}else {
					
					JOptionPane.showMessageDialog(null,"No such user registered. Please register.");
				
				}
			}
		}else if(e.getSource() == signButton) {
			
			if(!(users.containsKey(tf.getText()))) {
				
				RegUser();
				String username = tf.getText();
				frame.dispose();
				new Thread() {
					public void run() {
						new Client(username,8457);
					}
				}.start();
			}
			
		}
		
	}

	
	public HashMap<String,String> readFile(){
		
		HashMap<String,String> users = new HashMap<String, String>(); 
		
		try {
			FileChannel channel = FileChannel.open(path);
			ByteBuffer buf = ByteBuffer.allocate((int)channel.size());
			channel.read(buf);
			buf.flip();
			
			CharBuffer cbuf = charset.decode(buf);
			StringBuilder sb = new StringBuilder();
			
			for(int i = 0;i<cbuf.capacity();i++) {
				
				sb.append(cbuf.get(i));
			}
			
			StringTokenizer st = new StringTokenizer(sb.toString(),"\t\n");
			
			while(st.hasMoreTokens()) {
				String key = st.nextToken();
				String val = st.nextToken();
				users.put(key,val );
			
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return users;
	}
	
	public void RegUser() {
		
		FileChannel channel;
		try {
			channel = FileChannel.open(path);
		ByteBuffer buf = ByteBuffer.allocate((int)channel.size());
		channel.read(buf);
		buf.flip();
		CharBuffer cbuf = charset.decode(buf);
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0;i<cbuf.capacity();i++) {
			
			sb.append(cbuf.get(i));
		}
		sb.append('\n'+tf.getText()+ '\t'+ tf2.getText());
		
		Files.write(path, sb.toString().getBytes());
		
		buf.clear();					            
		channel.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}
}

