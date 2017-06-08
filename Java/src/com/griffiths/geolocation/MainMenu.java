package com.griffiths.geolocation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/*import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;*/
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import javax.swing.border.BevelBorder;
import javax.swing.JPanel;

public class MainMenu extends JFrame 
{
	private static final long serialVersionUID = -5174642765310724600L;

	
	//The Style Document objects allow text to be formatted within the JTextPane's
	StyledDocument currentSearchesStyleDoc;
	StyledDocument pastSearchesStyleDoc;

	//The pattern and matcher objects are to ensure a Post code can be entered with one or two numbers in its first part e.g bh88es / b724dr
	Pattern pattern1 = Pattern.compile("^[a-zA-Z][a-zA-Z][0-9][0-9][a-zA-Z][a-zA-Z]+$");
	Pattern pattern2 = Pattern.compile("^[a-zA-Z][a-zA-Z][0-9][0-9][0-9][a-zA-Z][a-zA-Z]+$");
	Pattern pattern3 = Pattern.compile("^[a-zA-Z][0-9][0-9][0-9][a-zA-Z][a-zA-Z]+$");
	Pattern pattern4 = Pattern.compile("^[a-zA-Z][0-9][0-9][a-zA-Z][a-zA-Z]+$");
	Pattern pattern5 = Pattern.compile("^[a-zA-Z][a-zA-Z][0-9][\\s][0-9][a-zA-Z][a-zA-Z]+$");
	Matcher matcher1;
	Matcher matcher2;
	Matcher matcher3;
	Matcher matcher4;
	Matcher matcher5;
	Search search;
	
	public MainMenu(final User user1) throws BadLocationException {
		//MainMenu JFrame
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setFont(new Font("Dialog", Font.BOLD, 15));
		getContentPane().setBackground(SystemColor.controlHighlight);
		getContentPane().setLayout(null);
		setBounds(100,100,824,662);

		JPanel panelSearch = new JPanel();
		panelSearch.setBounds(12, 11, 786, 602);
		panelSearch.setLayout(null);

		//Previous searches JFrame
		//JFrame show_Prev_Searches = new JFrame("Previous Searches");

		//TextBox for Post code entry
		JTextArea txtBoxPostcode = new JTextArea();
		txtBoxPostcode.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLACK, null, null, null));
		txtBoxPostcode.setFont(new Font("Dialog", Font.BOLD, 15));
		txtBoxPostcode.setBounds(242, 185, 276, 36);
		txtBoxPostcode.setLineWrap(true);
		txtBoxPostcode.setWrapStyleWord(true);
		txtBoxPostcode.setColumns(WIDTH);
		txtBoxPostcode.setRows(HEIGHT);
		panelSearch.add(txtBoxPostcode);

		//Text Pane for displaying the users searche's from the current session
		JTextPane txtBoxTodaysSearches = new JTextPane();
		txtBoxTodaysSearches.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLACK, null, null, null));
		txtBoxTodaysSearches.setBounds(550, 107, 223, 235);
		panelSearch.add(txtBoxTodaysSearches);

		//Text Pane for displaying the users previous 3 searches from their last session
		//JPanel primary = new JPanel();
		JTextPane txtBoxPastSearches = new JTextPane();
		txtBoxPastSearches.setBorder(new BevelBorder(BevelBorder.RAISED, Color.BLACK, null, null, null));
		txtBoxPastSearches.setBounds(10, 353, 477, 238);
		//JScrollPane scrollPane = new JScrollPane(txtBoxPastSearches);

		panelSearch.add(txtBoxPastSearches);


		//TextBox for Address entry
		JTextArea txtBoxAddress = new JTextArea();
		txtBoxAddress.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLACK, null, null, null));
		txtBoxAddress.setFont(new Font("Dialog", Font.BOLD, 15));
		txtBoxAddress.setBounds(242, 114, 276, 36);
		txtBoxAddress.setLineWrap(true);
		txtBoxAddress.setWrapStyleWord(true);
		txtBoxAddress.setColumns(WIDTH);
		txtBoxAddress.setRows(HEIGHT);
		panelSearch.add(txtBoxAddress);

		currentSearchesStyleDoc = txtBoxTodaysSearches.getStyledDocument();
		pastSearchesStyleDoc = txtBoxPastSearches.getStyledDocument();

		//Show user their history in the TextBox for Past Searches
		try
		{
			user1.showHistory(pastSearchesStyleDoc);
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(null, ex);
		}

		//Event Button for searching by address
		JButton btnAddress = new JButton("Search by address");
		btnAddress.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnAddress.setBounds(31, 114, 173, 38);
		panelSearch.add(btnAddress);


		btnAddress.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					String addressString = txtBoxAddress.getText();
					if (addressString.length()!=0)
					{
						user1.current_Session_Searches.add(new Search(user1.usersFile, user1, addressString , txtBoxTodaysSearches));
						txtBoxTodaysSearches.setText("");
						user1.showCurrentSessionHistory(currentSearchesStyleDoc);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Please enter at least some data in the \"Search By Address\" box");
						txtBoxTodaysSearches.setText("");
					}
				}
				catch (Exception ex)
				{
					System.out.println(ex);
					//JOptionPane.showMessageDialog(null, "You need internet for this application to work...");
				}
			}
		});		

		//Event button for searching by Postcode
		JButton btnPostcode = new JButton("Search by postcode");
		btnPostcode.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnPostcode.setMargin(new Insets(0, 0, 0, 0));
		btnPostcode.setBounds(31, 178, 173, 45);
		panelSearch.add(btnPostcode);

		btnPostcode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					String replacedText = txtBoxPostcode.getText();
					matcher1 = pattern1.matcher(replacedText);
					matcher2 = pattern2.matcher(replacedText);
					matcher3 = pattern3.matcher(replacedText);
					matcher4 = pattern4.matcher(replacedText);
					matcher5 = pattern5.matcher(replacedText);
					
					if (replacedText.length()!=0 && matcher1.find() || matcher2.find() || matcher3.find() || matcher4.find() || matcher5.find())
					{
						user1.current_Session_Searches.add(search = new Search(user1.usersFile, user1, replacedText, txtBoxTodaysSearches));
						txtBoxPostcode.setText("");
						user1.showCurrentSessionHistory(currentSearchesStyleDoc);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Please enter a valid postcode");
						txtBoxPostcode.setText("");
					}
				}
				catch (Exception ex)
				{
					JOptionPane.showMessageDialog(null, "Please ensure you have access to the internet");
					JOptionPane.showMessageDialog(null, ex);
				}
			}
		});

		//This button causes the user to log out and re-displays the login screen
		JButton btnLogOut = new JButton("Log Out");
		btnLogOut.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnLogOut.setBounds(171, 262, 156, 38);
		panelSearch.add(btnLogOut);

		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainMenu.this.dispose();
				MainMenu.this.setVisible(false);
				new Login().setVisible(true);

			}
		});

		//This is the event button to log out. This causes the system to terminate the program, also causing the user to log out.
		JButton btnLogOutAndExit = new JButton("Log Out and Exit");
				
		
		btnLogOutAndExit.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnLogOutAndExit.setBounds(365, 262, 156, 38);
		panelSearch.add(btnLogOutAndExit);

		btnLogOutAndExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Goodbye...");
				System.exit(0);
			}
		});

		//This label assists the user in knowing this Text Pane displays the users past 3 searches
		JLabel lblPastSearches = new JLabel("Past Searches");
		lblPastSearches.setForeground(SystemColor.controlDkShadow);
		lblPastSearches.setFont(new Font("Dialog", Font.BOLD, 18));
		lblPastSearches.setBounds(10, 319, 169, 30);
		panelSearch.add(lblPastSearches);
		
		//This label is the title for the main search page
		JLabel lblSearchTitle = new JLabel();
		lblSearchTitle.setForeground(SystemColor.textHighlight);
		lblSearchTitle.setFont(new Font("Tahoma", Font.BOLD, 28));
		lblSearchTitle.setBounds(130, 11, 525, 36);
		lblSearchTitle.setText("Find your location simply and fast !");
		panelSearch.add(lblSearchTitle);

		//This label assists the user in knowing these TextBvoxes are for searching
		JLabel lblSearchByAddress = new JLabel("Search by Address or Postcode below");
		lblSearchByAddress.setFont(new Font("Dialog", Font.BOLD, 18));
		lblSearchByAddress.setForeground(SystemColor.controlDkShadow);
		lblSearchByAddress.setBackground(Color.WHITE);
		lblSearchByAddress.setBounds(31, 77, 361, 26);
		panelSearch.add(lblSearchByAddress);

		//This is the label to assist the user in knowing the Text Pane will display the users searches from today
		JLabel lblTodaysSearches = new JLabel("Today's Searches");
		lblTodaysSearches.setSize(169, 30);
		lblTodaysSearches.setLocation(551, 75);
		lblTodaysSearches.setForeground(SystemColor.controlDkShadow);
		lblTodaysSearches.setFont(new Font("Dialog", Font.BOLD, 18));
		panelSearch.add(lblTodaysSearches);
		try
		{
			Image sat_image = ImageIO.read(getClass().getResource("sat.png"));
			sat_image = sat_image.getScaledInstance(246, 212, java.awt.Image.SCALE_SMOOTH);
			ImageIcon sat_imageIcon = new ImageIcon(sat_image);
			JLabel lblNewLabel = new JLabel(sat_imageIcon);
			lblNewLabel.setBounds(516, 353, 257, 238);
			panelSearch.add(lblNewLabel);			

		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(null,ex);
		}

		this.getContentPane().add(panelSearch);
		
		JLabel lblnoSpaces = new JLabel("(No spaces)");
		lblnoSpaces.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblnoSpaces.setBounds(85, 222, 98, 29);
		panelSearch.add(lblnoSpaces);
	}
}

//This is to ashow the user an image of the location they requested, however as it changes depending on what in specific the user requires to see at that specific location, this function would take a lot more work
//http://maps.googleapis.com/maps/api/streetview?size=640x480 &location=52.6160255,141.3253261 &fov=120 &heading=235 &pitch=10 &sensor=false
