package zad1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MainChat implements ActionListener {
	
	private JTextField tf;
	private JTextArea ta;
	private JButton sendBut;
	private JButton logOut;
	private JFrame frame;
	private Client client;

	public MainChat(Client client) {
		this.client = client;
		
		frame = new JFrame("MainChat");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setPreferredSize(new Dimension(500,500));
		frame.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		
		
		panel.setLayout(new FlowLayout());
		
		ta = new JTextArea();
		ta.setEditable(true);
		
		tf = new JTextField();
		tf.setEditable(true);
		
		sendBut = new JButton("Send");
		sendBut.addActionListener(this);
		sendBut.setSize(50, 50);
		
		logOut = new JButton("Log Out");
		logOut.addActionListener(this);
		logOut.setSize(50,50);
		
		panel.add(logOut);
		panel.add(sendBut);
		
		JScrollPane pane = new JScrollPane(ta);
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		
		panel1.add(panel,BorderLayout.NORTH);
		panel1.add(pane,BorderLayout.CENTER);
		panel1.add(tf,BorderLayout.SOUTH);
		
		
		
		frame.add(panel,BorderLayout.NORTH);
		frame.add(panel1,BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		
	}
	
	public void AppendChat(String str) {
		if(!(str.equals(""))) ta.append(str + "\n");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == sendBut) {
			
			if(!(tf.getText().equals(""))) {
				
				client.write(client.username+": " + tf.getText());
				ta.append("\n");
				ta.append(tf.getText()+ "\n");
				ta.append("\n");
				tf.setText("");
				
			}
		}else if(e.getSource() == logOut) {
			try {
				client.isRunning = false; 
				client.serverSocket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			frame.dispose();
			
		}
		
	}
}
