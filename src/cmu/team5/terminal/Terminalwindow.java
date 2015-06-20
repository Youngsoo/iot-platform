/*
 * Created by JFormDesigner on Thu Jun 18 01:52:03 IST 2015
 */

package cmu.team5.terminal;

import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import cmu.team5.middleware.Protocol;
import cmu.team5.middleware.Transport;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author swapan pati
 */
public class Terminalwindow extends JPanel {
	
	private String ServerIp="192.168.1.117";//"localhost";
	private String ServerPort="550";
	private static Socket ClientSocket = null; 
	private boolean UserLoggedIn = false;
	
	
	
	public Terminalwindow() {
		initComponents();
	}

	
	
	
	public JMenuBar AddmenuBar(){
		  JMenuBar menuBar = new JMenuBar();
		  
		  JMenu menu = new JMenu("Server Configuration");
          JMenu nodemenu = new JMenu("Node Registration");
		  menuBar.add(menu);
		  menuBar.add(nodemenu);
		  
		  JMenuItem serverIp = new JMenuItem("Server IP");
		  JMenuItem serverPort = new JMenuItem("Server Port");
		  menu.add(serverIp);
		  menu.add(serverPort);
		  
		  JMenuItem NodeReg = new JMenuItem("New Node Registration");
		  JMenuItem NodeUreg = new JMenuItem("Node Un-Registration");
		
		  nodemenu.add(NodeReg);
		  nodemenu.add(NodeUreg);
		  
		  
		  NodeReg.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	            	if (IsuserloggedIn()){
	            		String NodeId = JOptionPane.showInputDialog("Enter Node ID(Serial No):");
	            		try {
							InputStream in = ClientSocket.getInputStream();
							BufferedWriter out;
		        			out = new BufferedWriter(new OutputStreamWriter(ClientSocket.getOutputStream()));
		        			String msg = Protocol.generateRegisterMsg(null,NodeId);
		        			Transport.sendMessage(out, msg);
		        			
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	        			
	            	}
	            	else{
	        			JOptionPane.showMessageDialog(Terminal.Window, "Need to login First..");
	            	}
	                
	            }
	        });
		  
		  
		  NodeUreg.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	            	
	            	if (true){//IsuserloggedIn()){
		       	
	            	Object[] NodeList = {"Node1", "Node2", "Node3"};
	            	UIManager.put("OptionPane.cancelButtonText", "Cancel");
	            	UIManager.put("OptionPane.okButtonText", "UnRegister");
	            	
	            	String SelectedNodeId = (String)JOptionPane.showInputDialog(
	            	                    Terminal.Window,
	            	                    "Registered Node Details..\n",
	            	                    "Node UnRegistration..",
	            	                    JOptionPane.PLAIN_MESSAGE,
	            	                    null,
	            	                    NodeList,
	            	                    NodeList[0]);
	            	
	            	
	            	//If a string was returned, say so.
	            	if ((SelectedNodeId != null) && (SelectedNodeId.length() > 0)) {
	            		
	            	    return;
	            	}  		
	            }
	            	else{
	            		JOptionPane.showMessageDialog(Terminal.Window, "Need to login First..");
	            	}
	            }
	        });
		  
		  
		  
		  serverIp.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	                ServerIp = JOptionPane.showInputDialog(null, "Enter the Server IP:",ServerIp);
	            }
	        });
		  
		  serverPort.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	                ServerPort = JOptionPane.showInputDialog(null,"Enter the Server Port:",ServerPort);
	            }
	        });
		  
		  return menuBar;
		  
	  }
	
	
	public Socket Connection(){
		Socket clientSocket = null;
		try {
			if (ServerIp != null && ServerPort != null ){
				clientSocket = new Socket(ServerIp, Integer.valueOf(ServerPort));
			}
			else{
				JOptionPane.showMessageDialog(Terminal.Window, "Set the Server Configuration");
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(Terminal.Window, "Check the Server Configuration");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return clientSocket;
	}
	
	
	public JTable MakeNodeInfo(){
		DefaultTableModel model = new DefaultTableModel(); 
		JTable table = new JTable(model); 
		model.addColumn("NodeId"); 
		model.addColumn("Sensor Name"); 
		model.addColumn("Value"); 
		
		model.addRow(new Object[]{"v1", "v2", "V3"});
		model.addRow(new Object[]{"v11", "v22", "V33"});
		model.addRow(new Object[]{"v111", "v222", "V333"});
		
		return table;
		
	}
	
	public JTable MakeActuratorTable(){
		 NodeControlUi stm = new NodeControlUi();
	     JTable sourceTable = new JTable(stm);
	     return sourceTable;
	}
	
	private void AddSensordata(){
		
	}


	private boolean IsuserloggedIn(){
		return UserLoggedIn;
	}
	
  	
	private void LoginActionPerformed(ActionEvent e) {
		// TODO add your code here
		ClientSocket = Connection();
		
		try {
			InputStream in = ClientSocket.getInputStream();
			BufferedWriter out;
			
			out = new BufferedWriter(new OutputStreamWriter(ClientSocket.getOutputStream()));
			String msg = Protocol.generateLoginMsg(UserId.getText(), Password.getText());
			Transport.sendMessage(out, msg);
			String ret = Transport.getMessage(in);
			if (Protocol.getResult(ret).compareTo("success") == 0){
				UserLoggedIn = true;
			}			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			UserLoggedIn = false;
		}
		
		if ((ClientSocket !=null) && (UserLoggedIn == true)){
			Login.setEnabled(false);
			UserId.setEnabled(false);
			Password.setEditable(false);
		}
		
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - swapan pati
		label1 = new JLabel();
		label2 = new JLabel();
		UserId = new JTextField();
		label3 = new JLabel();
		Password = new JPasswordField();
		Login = new JButton();
		scrollPane1 = new JScrollPane();
		NodeInfo = MakeNodeInfo();
		scrollPane2 = new JScrollPane();
		NodeControl = MakeActuratorTable();
		Refresh = new JButton();
		Update = new JButton();

		//======== this ========

		// JFormDesigner evaluation mark
		setBorder(new javax.swing.border.CompoundBorder(
			new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
				"", javax.swing.border.TitledBorder.CENTER,
				javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
				java.awt.Color.red), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

		setLayout(new FormLayout(
			"8*(default, $lcgap), default",
			"9*(default, $lgap), default"));

		//---- label1 ----
		label1.setText("IoT Terminal System");
		add(label1, CC.xy(13, 3));

		//---- label2 ----
		label2.setText("User ID");
		add(label2, CC.xy(15, 7));

		//---- UserId ----
		UserId.setColumns(10);
		add(UserId, CC.xy(17, 7));

		//---- label3 ----
		label3.setText("Password");
		add(label3, CC.xy(15, 9));
		add(Password, CC.xy(17, 9));

		//---- Login ----
		Login.setText("Login");
		Login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LoginActionPerformed(e);
			}
		});
		add(Login, CC.xy(17, 13));

		//======== scrollPane1 ========
		{
			scrollPane1.setViewportView(NodeInfo);
		}
		add(scrollPane1, CC.xy(7, 15));

		//======== scrollPane2 ========
		{
			scrollPane2.setViewportView(NodeControl);
		}
		add(scrollPane2, CC.xy(13, 15));

		//---- Refresh ----
		Refresh.setText("Refresh");
		add(Refresh, CC.xy(7, 17));

		//---- Update ----
		Update.setText("Update");
		add(Update, CC.xy(13, 17));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - swapan pati
	private JLabel label1;
	private JLabel label2;
	private JTextField UserId;
	private JLabel label3;
	private JPasswordField Password;
	private JButton Login;
	private JScrollPane scrollPane1;
	private JTable NodeInfo;
	private JScrollPane scrollPane2;
	private JTable NodeControl;
	private JButton Refresh;
	private JButton Update;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
