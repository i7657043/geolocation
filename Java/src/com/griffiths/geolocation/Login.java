package com.griffiths.geolocation;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.SystemColor;
import java.awt.Color;

public class Login extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4593051884611417379L;
	//private static final long serialVersionUID = 1L;
	private JPanel panelLogin;
	private JTextField txtBoxUser;
	private JTextField txtBoxPass;
	private JLabel lblEnterUsername;
	private JLabel lblEnterPassword;
	private JButton btnSignUp;


	boolean duplicateUsername = false;
	StringBuilder password = new StringBuilder();

	//These File objects are set as default to run within the windows operating system, however a conditional operator is used further into the program
	//to determine whether the values of this object should be changed.
	static File usersFile = new File("c:\\GeoCord\\");
	static File loginFile = new File("c:\\GeoCord\\login.txt");
	static File programRootDir = new File("c:\\GeoCord\\");
	static File usersFilesDir = new File("c:\\GeoCord\\usersFiles\\");
	static File guestFile = new File("c:\\GeoCord\\guest.txt");
	//Boolean to determine what opertating system this program is being run on. Defaults to windows
	static boolean windows = true;

	User user1;

	FileWriter writer1;

	static Login login;
	boolean first = true;
	//Launch the application
	public static void main(String[] args) throws Exception 
	{		
		login = new Login();

		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				login.setLocationRelativeTo(null);
				login.setVisible(true);

				/* FOR LINUX
					//JOptionPane.showMessageDialog(null, System.getProperty("os.name") + " user, Welcome to Jordan's GeoCordinates program");
					String operatingSystemType = System.getProperty("os.name");

					//This is the conditional operator that creates a file system familiar with the linux environment, in order for
					//the program to be supported by a variety of operating systems.
					if (operatingSystemType.contains("Linux"))
					{
						windows = false;
						loginFile = new File("/home/");
						programRootDir = new File("/home//");
						guestFile = new File("/home/");
						usersFilesDir = new File("/home/");
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				 */			}
		});
	}
	//Create the frame
	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 527, 310);
		panelLogin = new JPanel();
		panelLogin.setBackground(SystemColor.controlHighlight);
		panelLogin.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panelLogin);

		JButton btnGo = new JButton("Login");


		btnGo.setBounds(317, 63, 73, 25);
		btnGo.addActionListener(new ActionListener() {
			//Action for Login Button
			public void actionPerformed(ActionEvent e) 
			{
				if (!usersFilesDir.exists())
				{
					usersFilesDir.mkdir(); 
				}
				try
				{
					User user1 = new User(loginFile, txtBoxUser.getText(), txtBoxPass.getText(), windows);

					if (user1.accepted)
					{
						JOptionPane.showMessageDialog(null, "Success");
						login.dispose();
						login.setVisible(false);
						new MainMenu(user1).setVisible(true);
					}
					else if (txtBoxUser.getText().equals("") || txtBoxPass.getText().equals(""))
					{
						JOptionPane.showMessageDialog(null, "Please enter values into both boxes");
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Please enter a valid username and password combo");
					}
				}
				catch (Exception ex)
				{
					JOptionPane.showMessageDialog(null, "No Login file exists... Sign up or ask the Administrator to create one");
				}
			}
		});
		panelLogin.setLayout(null);

		//This label assists the user in knowing where to enter their username
		lblEnterUsername = new JLabel("Enter Username");
		lblEnterUsername.setBounds(20, 41, 114, 15);
		panelLogin.add(lblEnterUsername);

		//This text box allows input to be taken as the user's username
		txtBoxUser = new JTextField();
		txtBoxUser.setBounds(152, 39, 138, 19);
		panelLogin.add(txtBoxUser);
		txtBoxUser.setColumns(10);

		//This label assists the user in knowing where to enter their passsword
		lblEnterPassword = new JLabel("Enter password");
		lblEnterPassword.setBounds(20, 68, 112, 15);
		panelLogin.add(lblEnterPassword);

		//This text box allows input to be taken as the user's passsword
		txtBoxPass = new JTextField();
		txtBoxPass.setBounds(152, 70, 138, 19);
		txtBoxPass.setColumns(10);
		panelLogin.add(txtBoxPass);
		panelLogin.add(btnGo);

		btnSignUp = new JButton("Sign up");

		//Event for sign up button
		//This event first checks if a login file exists, if not it is created and stored on the users computer
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtBoxPass.getText().length()>0&&txtBoxUser.getText().length()>0)
				{
					try
					{
						if (!loginFile.exists())
						{
							programRootDir.mkdir();
							loginFile.createNewFile();
							writer1 = new FileWriter(loginFile, true);
						}
						Scanner scan = new Scanner(loginFile);
						String chosenUsername = txtBoxUser.getText();
						String chosenPass = txtBoxPass.getText().toString();
						String line;

						while (scan.hasNext())
						{
							line = scan.nextLine();
							if (line.contains("username"))
							{
								line = line.substring(9,line.length());
							}							
							if (line.equals(chosenUsername))
							{
								JOptionPane.showMessageDialog(null, "This username already exists, please choose another");
								duplicateUsername = true;
								break;
							}
						}
						if (!duplicateUsername)
						{
							writer1 = new FileWriter(loginFile, true);
							writer1.append("username-" + chosenUsername + "\npassword-" + chosenPass +"\n");
							writer1.flush();
							JOptionPane.showMessageDialog(null, "Signed up...");
							scan.close();
						}

					}
					catch (Exception ex)
					{
						JOptionPane.showMessageDialog(null, "No login file can be created, you do not have the correct permissions\nPlease contact the administrator of this system...");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Please enter a username and password before signing up");
				}
			}
		});
		btnSignUp.setBounds(395, 63, 87, 25);
		panelLogin.add(btnSignUp);

		JLabel lblLoginAsGuest = new JLabel("Login as guest below");
		lblLoginAsGuest.setBounds(200, 185, 152, 15);
		lblLoginAsGuest.setForeground(UIManager.getColor("Menu.acceleratorForeground"));
		lblLoginAsGuest.setBackground(UIManager.getColor("Menu.acceleratorForeground"));
		panelLogin.add(lblLoginAsGuest);
		JButton btnGuest = new JButton("Guest");
		btnGuest.setBounds(71, 207, 370, 25);
		btnGuest.addActionListener(new ActionListener() {

			//Action for Guest button
			public void actionPerformed(ActionEvent e) 
			{
				if (!programRootDir.exists())
				{
					try 
					{
						programRootDir.mkdir();
						guestFile.createNewFile();
					} catch (IOException ex) 
					{
						JOptionPane.showMessageDialog(null, "Creation of guest file error...");
					}
				}
				try
				{
					user1 = new User(guestFile);
					login.dispose();
					login.setVisible(false);
					MainMenu mm = new MainMenu(user1);
					mm.setLocationRelativeTo(null);
					mm.setVisible(true);
					//new MainMenu(user1).setVisible(true);
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(null, "No guest file exists on this computer\nPlease consult the administrator of the system");
				}
			}
		});
		panelLogin.add(btnGuest);

		JLabel lblWarn1 = new JLabel("This program requires Google Chome browser installed at:");
		lblWarn1.setForeground(Color.RED);
		lblWarn1.setBounds(99, 116, 342, 29);
		panelLogin.add(lblWarn1);

		JLabel lblWarn2 = new JLabel("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");
		lblWarn2.setForeground(Color.RED);
		lblWarn2.setBounds(92, 138, 377, 26);
		panelLogin.add(lblWarn2);
	}
}
