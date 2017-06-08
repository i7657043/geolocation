package com.griffiths.geolocation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JTextPane;

public class Search {


	private String lattitude;
	
	public String getLattitude()
	{
		return lattitude;
	}
	
	public void setLattitude(String lat)
	{
		lattitude = lat;
	}
	String longitude;
	String location_Name = "";	
	BufferedReader read;
	FileWriter write1;
	
	//This is for allowing the user a Time Stamp for their search
	Date date = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("MM:dd:yyyy h:dd:ss a");
	String dateString = sdf.format(date);

	//This sets up the variables needed to make a connection to Google's API, returning a json file with the required data
	final String userAgent = "Mozilla/5.0";		
	String googleAPIURL = "http://maps.googleapis.com/maps/api/geocode/json?address=";
	HttpURLConnection conn = null;
	
	//This constructor allows the user object to be filled with the correct information, after a connection is successfully made to Google's API
	public Search (File usersFile, User user1, String postcode, JTextPane pane1) throws Exception
	{
		performSearch(usersFile, user1, postcode);
		//copyMeth();
	}

	//This constructor is needed in order to allow the Array List's that hold data from the users history to be filled with Search objects
	public Search()
	{
	}
	
	//This will method not work unless the user has Firefox	
	public void useBrowserToShowMap() throws Exception
	{
		String[] argsToRun = {"C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe", "-new-window", "https://www.google.co.uk/maps/place/" + this.location_Name};
		new ProcessBuilder(argsToRun).start();
	}

	//This method performs the search to return data from Google's API to the correct objects and files required
	public void performSearch(File usersFile, User user1, String location) throws Exception
	{				
		String temp = location;
		temp = temp.replaceAll(" ", "");
		googleAPIURL += temp;
		URL url = new URL(googleAPIURL);
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("User Agent", userAgent);
		read = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		write1 = new FileWriter(usersFile, true);
		boolean latDone=false;
		boolean lngDone=false;

		//This loop reads each line from buffer stream and replaces the specified characters, before filling the users object with the correct
		//information and writing more correct information to the specified text file
		while ((line=read.readLine())!=null)
		{
			line = line.replaceAll("\"", "");
			line = line.replaceAll(":", "");
			line = line.replaceAll(" ", "");
			line = line.replace(",", "");

			if (line.contains("lat")&&(!latDone))
			{
				line = line.replace("lat", "");
				this.lattitude = line;
				latDone=true;
			}
			if (line.contains("lng")&&(!lngDone))
			{
				line = line.replace("lng", "");
				this.longitude = line;
				lngDone=true;
			}
		}
		this.location_Name = location;
		int dialogResult = JOptionPane.showConfirmDialog(null, "Would you like to view this address on Google Maps", "title here", 0);
		if (dialogResult==JOptionPane.YES_OPTION)
		{
			useBrowserToShowMap();
		}
		write1.write("\nDate: " + dateString + "\n" + "Location: " + this.location_Name + "\n" + "Lattitude: " + this.lattitude + "\n" + "Longitude: " + this.longitude + "\n----------------------");
		write1.flush();

		write1.close();
	}
}