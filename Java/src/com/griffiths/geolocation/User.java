package com.griffiths.geolocation;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.text.StyledDocument;

public class User 
{
	//These variables are private so no other class's can see or access them as they hold more sensitive information such as password
	private String userName;
	private String password;
	private boolean lat=false;
	private boolean lng=false;
	private boolean date=false;
	private boolean loc=false;
	public boolean windows;

	boolean accepted;

	File usersFile;
	static File loginFile;

	//These Array List's allow for a users current session and past session's to be stored
	ArrayList<Search> current_Session_Searches = new ArrayList<Search>();
	ArrayList<Search> history_Searches = new ArrayList<Search>();

	public void showHistory(StyledDocument doc1) throws Exception
	{
		{
			for (Search s : history_Searches)
			{
				doc1.insertString(0, s.longitude + "\n" + "---------------\n", null);
				doc1.insertString(0, s.getLattitude() + "\n", null);
				doc1.insertString(0, s.location_Name + "\n", null);
				doc1.insertString(0, s.dateString + "\n", null);
			}
		}
	}
	public void showCurrentSessionHistory(StyledDocument doc1) throws Exception
	{
		for (Search s : current_Session_Searches)
		{
			doc1.insertString(0, "Lattitude: " + s.longitude + "\n" + "---------------\n", null);
			doc1.insertString(0, "Longitude: " + s.getLattitude() + "\n", null);
			doc1.insertString(0, "Location: " + s.location_Name + "\n", null);
			doc1.insertString(0,  s.dateString + "\n", null);
		}
	}

	public User (File loginFile, String userName, String password, boolean windows) throws Exception
	{
		this.userName = userName;
		this.windows = windows;
		if (!windows)
		{
			this.usersFile = new File("/home/jord/Uni/GeoCord/usersFilesDir/" + userName);
		}
		else
		{
			this.usersFile = new File("c:\\GeoCord\\usersFiles\\" + userName + ".txt" );
		}
		this.password = password;
		User.loginFile = loginFile;
		
				
		this.checkLogin();
		if (accepted)
		{
			this.setUpUserData();
		}
	}

	public User (File guestFile) throws Exception
	{
		usersFile=guestFile;
		if (!this.usersFile.exists())
		{
			this.usersFile.createNewFile();
		}
	}
	public void setUpUserData() throws Exception
	{
		if (!usersFile.exists())
		{
			JOptionPane.showMessageDialog(null, "No Coordinates history file exists for this user yet.. Creating one...");
			usersFile.createNewFile();
		}
		else
		{
			Scanner scan = new Scanner(usersFile);
			Search tempSearch = new Search();
			String line = "";

			while (scan.hasNextLine())
			{
				line=scan.nextLine();
				if (line.contains("Lattitude"))
				{
					tempSearch.setLattitude(line);
					lat=true;
				}
				if (line.contains("Longitude"))
				{
					tempSearch.longitude = line;
					lng=true;
				}
				if (line.contains("Date"))
				{
					tempSearch.dateString = line;
					date=true;
				}
				if (line.contains("Location"))
				{
					tempSearch.location_Name = line;
					loc=true;
				}
				if (date&&loc&&lat&&lng)
				{
					date=false;
					loc=false;
					lat=false;
					lng=false;
					history_Searches.add(tempSearch);
					tempSearch = new Search();
				}
			}
			scan.close();
		}
	}

	public void checkLogin() throws Exception
	{
		Scanner scan = new Scanner(loginFile);
		String line;
		String userNameOnLine;
		String passOnLine;
		while (scan.hasNext())
		{
			line = scan.nextLine();
			if (line.contains("-"))
			{
				userNameOnLine = line.substring(9);
				if (userNameOnLine.equals(userName))
				{
					line = scan.nextLine();
					if (line.contains("-"))
					{
						passOnLine = line.substring(9);
						if (passOnLine.equals(password))
						{
							this.accepted = true;
							break;
						}
					}
				}
			}
		}
		scan.close();
	}
}