/**
 *
 *  @author Shkambara Dmytro S15163
 *
 */

package zad1;

import javax.swing.JFrame;

public class Main{
	
public static void main(String[] args) throws Exception {
    Service s = new Service("Poland");
    String weatherJson = s.getWeather("Warsaw");
    Double rate1 = s.getRateFor("USD");
    Double rate2 = s.getNBPRate();
    
    GUI gui = new GUI();
    
    JFrame frame = gui.loadGUI(weatherJson, s.getCity(),s.getTemp(),s.getWeath(),s.getCountryCurency(),s.getCurency(),rate1,rate2);
    frame.show();
    
    
	}
}
