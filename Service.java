/**
 *
 *  @author Shkambara Dmytro S15163
 *
 */

package zad1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Service {

	private String id;
	private Currency currency;
	private String city;
	private String toCur;
	private String temperature;
	private String weather;

	public Service(String string) throws Exception {
		for(Locale l : Locale.getAvailableLocales()) {
			if(l.getDisplayCountry().equals(string)) {
				id = l.toString().substring(l.toString().indexOf('_') + 1, l.toString().length());
				currency= Currency.getInstance(l);
			}
		}
		if(id == null) {
			throw new Exception("There is no country with this name");
		}
	}

	public String getWeather(String string) throws IOException, JSONException, ParseException {
		
		city = string;
		
		URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+ string +","+id+"&APPID=188efef3fea4cd33f56288db6a12409c");
		BufferedReader buf = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
		StringBuilder sb = new StringBuilder();
		int c;
		
		while((c = buf.read())!= -1) {
			sb.append((char)c);
		}
		
		String result = "";
		JSONParser parser = new JSONParser();
		String g = sb.toString();
		JSONObject json = (JSONObject)parser.parse(g);
		
		for (Object key : json.keySet()) {
			
			String keySTR = (String) key;
			Object keyvalue = json.get(keySTR);
			
			if(keySTR.equals("main")) {
					
				JSONObject json2 = (JSONObject)parser.parse(keyvalue.toString());
				double l = (Double.parseDouble(json2.get("temp").toString())-273.15);
				temperature = Math.round(l)+"";
				
			}else if(keySTR.equals("weather")) {
				org.json.simple.JSONArray ar = (org.json.simple.JSONArray) keyvalue;
				JSONObject json3 = (JSONObject)parser.parse(ar.get(0).toString());
				weather = json3.get("main").toString();
				
			}
			result += keySTR + " - " + keyvalue + "\n";
		}
		return result;
	}

	public Double getRateFor(String string) throws IOException, ParseException {
		toCur = string;
		double first = 0;
		double second = 0;
		
		URL url = new URL("http://data.fixer.io/api/latest?access_key=324d3506a96cba3170a3470d647694b3&symbols="+currency);
		URL url1 = new URL("http://data.fixer.io/api/latest?access_key=324d3506a96cba3170a3470d647694b3&symbols="+string);
		
		BufferedReader buf = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
		StringBuilder sb = new StringBuilder();
		
		int c;
		while((c = buf.read())!= -1) {
		
			sb.append((char)c);
		}
		
		JSONParser parser = new JSONParser();
		String g = sb.toString();
		JSONObject json = (JSONObject)parser.parse(g);
		for (Object key : json.keySet()) {
			String keySTR = (String) key;
			Object keyvalue = json.get(keySTR);
				if(keySTR.equals("rates")) {
					JSONObject json2 = (JSONObject)parser.parse(keyvalue.toString());
					first = (Double.parseDouble(json2.get(currency+"").toString()));
					
				}
		}
		
		buf = new BufferedReader(new InputStreamReader(url1.openStream(), "UTF-8"));
		sb = new StringBuilder();
		
		while((c = buf.read())!= -1) {
			
			sb.append((char)c);
			
		}
		
		
		json = (JSONObject)parser.parse(sb.toString());
		
		for (Object key : json.keySet()) {
			String keySTR = (String) key;
			Object keyvalue = json.get(keySTR);
				if(keySTR.equals("rates")) {
					JSONObject json2 = (JSONObject)parser.parse(keyvalue.toString());
					second = (Double.parseDouble(json2.get(string).toString()));
					
				}
		}
	    BigDecimal bd = new BigDecimal(first/second);
	    bd = bd.setScale(2, RoundingMode.HALF_UP);
	    
		return bd.doubleValue();
	}

	public Double getNBPRate() throws Exception {
		String cur = currency+"";
		boolean found = false;
		Double result = (double) 0;
		if(cur.equals("PLN")) {
			return 1.0;
		} else {
			
		
		URL url = new URL("http://www.nbp.pl/kursy/xml/b012z180321.xml");
		URL url2 = new URL("http://www.nbp.pl/kursy/xml/a060z180326.xml");
		InputStream is = url.openStream();
		
		Document doc = null;
		Document doc1 = null;
		DocumentBuilderFactory dbf;
		DocumentBuilder db;
		dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		dbf.setNamespaceAware(false);
		db = dbf.newDocumentBuilder();
			
		doc = db.parse(is);
		
		is = url2.openStream();
		
		doc1 = db.parse(is);
		
		NodeList list = doc.getElementsByTagName("pozycja");
		NodeList list1 = doc1.getElementsByTagName("pozycja");
		
		for(int i = 0;i<list.getLength();i++) {
			
			Node node = list.item(i);
			
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				
				Element element = (Element)node;
				
					if(element.getElementsByTagName("kod_waluty").item(0).getTextContent().equals(currency+"")) {
						
						result = Double.parseDouble(element.getElementsByTagName("kurs_sredni").item(0).getTextContent().replace(',', '.'));
						found = true;
						break;
						
					}
			}
		}
		if(found == false) {
			
			for(int i = 0;i<list1.getLength();i++) {
				
				Node node = list1.item(i);
				
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					
					Element element = (Element)node;
					
						if(element.getElementsByTagName("kod_waluty").item(0).getTextContent().equals(currency+"")) {
							
							result = Double.parseDouble(element.getElementsByTagName("kurs_sredni").item(0).getTextContent().replace(',', '.'));
							found = true;
							break;
							
						}
				}
			}
			
			
		}
		if(found == false) {
			return 0.0;
		}
		
		 BigDecimal bd = new BigDecimal(result);
		 bd = bd.setScale(2, RoundingMode.HALF_UP);
		
		return bd.doubleValue();
		}
	}
	
	public String getCity() {
		return city;
	}
	
	public String getTemp() {
		return temperature;
	}
	
	public String getWeath() {
		return weather;
	}
	
	public String getCountryCurency() {
		return currency+"";
	}
	
	public String getCurency() {
		return toCur;
	}
	
	
}  
