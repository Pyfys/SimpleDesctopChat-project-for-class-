package zad1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.javafx.application.PlatformImpl;

import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class GUI {
	
	static JFXPanel jfxPanel;
	static String city;
	
	public JFrame loadGUI(String weatherJson, String city,String temp,String weather,String natCur,String cur,double rate1,double rate2) {
		
		
		 JFrame frame = new JFrame();
		 
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    frame.setLayout(new BorderLayout());
		    frame.setVisible(true);
		    frame.setPreferredSize(new Dimension(825,800));
		    frame.setLayout(new BoxLayout(frame.getContentPane(),BoxLayout.Y_AXIS));
		    
		    JPanel topPanel = new JPanel();
		    topPanel.setPreferredSize(new Dimension(350,150));
		    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		    
		    jfxPanel = new JFXPanel();
		    LoadWiki();
		    JLabel lblNewLabel = new JLabel("Temperature: " + temp + " °C");
		    lblNewLabel.setFont(new Font("Dialog", Font.PLAIN, 28));
		    topPanel.add(lblNewLabel);
		    
		    JLabel label = new JLabel("Weather: " + weather);
		    label.setFont(new Font("Dialog", Font.PLAIN, 28));
		    topPanel.add(label);
		    
		    JLabel label_1 = new JLabel("1 "+ cur + " - " + rate1 + " " + natCur);
		    label_1.setFont(new Font("Dialog", Font.PLAIN, 28));
		    topPanel.add(label_1);
		    
		    JLabel label_2 = new JLabel("Rate in PLN " + rate2 + " " + natCur);
		    label_2.setFont(new Font("Dialog", Font.PLAIN, 28));
		    topPanel.add(label_2);
		    
		    frame.add(topPanel);
		    frame.add(jfxPanel);
		    frame.pack();
		    
		    return frame;
	}

		public static void LoadWiki(){
			 PlatformImpl.startup(new Runnable() {  
		            @Override
		            public void run() { 
			Stage stage = new Stage();
		    stage.setResizable(true);
		    
		    Group root = new Group();
		    Scene scene = new Scene(root,0,0);
		    stage.setScene(scene);
		    
		    WebView view = new WebView();
		    WebEngine engine = view.getEngine();
		    
		    engine.load("https://en.wikipedia.org/wiki/" + city);
		    
		    ObservableList<Node> children = root.getChildren();
		    children.add(view); 
		    
		    jfxPanel.setScene(scene);
		            }
			 });
		}
}
