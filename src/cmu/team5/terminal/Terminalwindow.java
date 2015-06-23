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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	private static String ServerIp="localhost"; //"192.168.1.149";//"localhost";
	private static String ServerPort="550";
	public static Socket ClientSocket = null; 
	private boolean UserLoggedIn = false;
	
	private static String[] RegNodeList = null;
	
	
	private NodeControlUi NodeControler = null;
	
	
	JMenu NodeMenu;
	JMenu LogMenu;
	JMenu ConfigMenu;
	JMenu LogOff;
	
	public Terminalwindow() {
		initComponents();
	}

	
	private void MakeEmptySensorTable(){
		JTable table = getSensorTable();
		DefaultTableModel t = (DefaultTableModel)table.getModel();
		while(t.getRowCount() != 0){
			t.removeRow(0);	
		}
	}
	public void UpdateSensorTable(String NodeId, HashMap<String, String> SensorInfo){
		
		JTable table = getSensorTable();
		DefaultTableModel t = (DefaultTableModel)table.getModel();
		
		for(Map.Entry<String, String> entry: SensorInfo.entrySet()) {
		     t.addRow(new Object[]{NodeId, entry.getKey(), entry.getValue()});	
		}	
	}
	
	private void ServerNodeStatusRes(String Msg)
	{
		HashMap<String, String> SensorInfo = new HashMap<String, String>();
		HashMap<String, String> ActuratorInfo = new HashMap<String, String>();
		
		SensorInfo = Protocol.getSensorInfo(Msg);
		UpdateSensorTable(Protocol.getNodeId(Msg), SensorInfo);
	
		ActuratorInfo = Protocol.getActuratorInfo(Msg);
		UpdateActuratorTable(Protocol.getNodeId(Msg), ActuratorInfo);
	}
	
	public static void ServerConfigUpdateReq(String light, String alarm, String log)
	{
		try {
			InputStream in = ClientSocket.getInputStream();
			BufferedWriter out;
			out = new BufferedWriter(new OutputStreamWriter(ClientSocket.getOutputStream()));
			
			String msg = Protocol.generateConfigDataMsg(RegNodeList[0],"light",light);	
			Transport.sendMessage(out, msg);		
			
			msg = Protocol.generateConfigDataMsg(RegNodeList[0],"alarm",light);	
			Transport.sendMessage(out, msg);		
			
			msg = Protocol.generateConfigDataMsg(RegNodeList[0],"log",light);	
			Transport.sendMessage(out, msg);		
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void GetNodeInformation(String NodeId){
		try {
			InputStream in = ClientSocket.getInputStream();
			BufferedWriter out;
			out = new BufferedWriter(new OutputStreamWriter(ClientSocket.getOutputStream()));
			String msg = Protocol.generateNodeInfoMsg(NodeId);	
			Transport.sendMessage(out, msg);		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void ShowLogData(String Msg)
	{
		
			Object[] cols = {
			    "NodeId","logType","Time","Name", "Value"};
			
			DefaultTableModel model = new DefaultTableModel(); 
			JTable table = new JTable(model);
			for(int i =0; i < cols.length; i++){
				model.addColumn(cols[i]); 
			}
			List<HashMap<String,String>> data = new ArrayList<HashMap<String,String>>(); 
			data = Protocol.getLogsData(Msg);
			 
			 
			for(int i =0; i <data.size(); i++) {
				HashMap<String,String> logdata = new HashMap<String,String>();
				logdata = data.get(i);
				Object[] rowdata = new Object[5];
						
				for(Map.Entry<String, String> entry: logdata.entrySet()) {
					switch(entry.getKey()){
					case "nodeId": rowdata[0] = entry.getValue(); break;
					case "logType": rowdata[1] = entry.getValue(); break;
					case "time": rowdata[2] = entry.getValue(); break;	
					case "name": rowdata[3] = entry.getValue(); break;
					case "value": rowdata[4] = entry.getValue(); break;
					}
				}  		
				model.addRow(rowdata);
			}
			
			JOptionPane.showMessageDialog(null, new JScrollPane(table));		
	}
	
	
	
	private void NodeConfiguration(){
		NodeConfigWindow configwin = new NodeConfigWindow();
		JFrame frame = new JFrame();
		frame.setSize(400,400);   
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(configwin);
        frame.pack();
        frame.setVisible(true);
	}
	
	
	
	private void RequstLogsFromServer(){
		if (IsuserloggedIn()){
    		try {
				BufferedWriter out;
    			out = new BufferedWriter(new OutputStreamWriter(ClientSocket.getOutputStream()));
    			String msg = Protocol.generateLogInfoMsg();
    			Transport.sendMessage(out, msg);
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}
	}
	
	
	private void ServerNodeRegistrationRes(String Msg)
	{
  		if (Protocol.getResult(Msg).compareTo("success") == 0){
  			UIManager.put("OptionPane.okButtonText", "Ok");
			JOptionPane.showMessageDialog(Terminal.Window, "Node Registration Success..");
			MakeEmptySensorTable();
			RequestRegsNodeList();
			GetNodeInformation(Protocol.getNodeId(Msg));
		}
		else{
			UIManager.put("OptionPane.okButtonText", "Ok");
			JOptionPane.showMessageDialog(Terminal.Window, "Node Registration Failed!!!");
		}
	}
	
	
	private void ServerNodeUnRegistrationRes(String Msg)
	{
		if (Protocol.getResult(Msg).compareTo("success") == 0){
			UIManager.put("OptionPane.okButtonText", "Ok");
			JOptionPane.showMessageDialog(Terminal.Window, "Node Un-Registration Success..");
			RequestRegsNodeList();
		}	
		RefreshActionPerformed(null);
	}
	
	
	private void DoNodeUnRegistration(String [] NodeList)
	{
		String SelectedNodeId = null;	
		if (NodeList != null && NodeList.length >= 1){
    		UIManager.put("OptionPane.cancelButtonText", "Cancel");
    		UIManager.put("OptionPane.okButtonText", "UnRegister");
    	
    		SelectedNodeId = (String)JOptionPane.showInputDialog(
    	                    Terminal.Window,
    	                    "Registered Node Details..\n",
    	                    "Node UnRegistration..",
    	                    JOptionPane.PLAIN_MESSAGE,
    	                    null,
    	                    NodeList,
    	                    NodeList[0]);
    	}
    	else{
    		UIManager.put("OptionPane.okButtonText", "Ok");
    		JOptionPane.showMessageDialog(Terminal.Window, "There is no Node present for Unregister");
    	}
		
    	if ((SelectedNodeId != null) && (SelectedNodeId.length() > 0)) {
        	
    		try{
        		BufferedWriter out;	
        		out = new BufferedWriter(new OutputStreamWriter(ClientSocket.getOutputStream()));
        		String msg = Protocol.generateUnregisterNodeMsg(SelectedNodeId);		
        		Transport.sendMessage(out, msg);
        	}catch (Exception e){}
    	}
	}
	
	private void ServerNodeRegisteredRes(String Msg)
	{
		RegNodeList = Protocol.getNodeList(Msg);	
	}
	
	private void RequestRegsNodeList()
	{
		if (IsuserloggedIn()){
        	try{
        		BufferedWriter out;
        		out = new BufferedWriter(new OutputStreamWriter(ClientSocket.getOutputStream()));
        		String msg = Protocol.generateRegisteredNodeMsg();	
        		Transport.sendMessage(out, msg);
        	
        	}catch (Exception e){
        		e.printStackTrace();
        	}
		}
	}
	
	public JMenuBar AddmenuBar(){
		  JMenuBar menuBar = new JMenuBar();
		  
		  JMenu menu = new JMenu("Server Configuration");
          NodeMenu = new JMenu("Node Operation");
		  LogMenu = new JMenu("Log Infomation");
          ConfigMenu = new JMenu("Configuration");
		  LogOff = new JMenu("LogOut");
          
		  menuBar.add(menu);
		  menuBar.add(NodeMenu);
		  menuBar.add(LogMenu);
		  menuBar.add(ConfigMenu);
		  menuBar.add(LogOff);
		  
		  NodeMenu.setEnabled(false);
		  LogMenu.setEnabled(false);
		  ConfigMenu.setEnabled(false);
		  LogOff.setVisible(false);
		  
		  
		  JMenuItem serverIp = new JMenuItem("Server IP");
		  JMenuItem serverPort = new JMenuItem("Server Port");
		  menu.add(serverIp);
		  menu.add(serverPort);
		  
		  JMenuItem NodeReg = new JMenuItem("Node Registration");
		  JMenuItem NodeUreg = new JMenuItem("Node Un-Registration");
		
		  NodeMenu.add(NodeReg);
		  NodeMenu.add(NodeUreg);
		  
		  JMenuItem  LogDetails = new JMenuItem("Log Details");
		  LogMenu.add(LogDetails);
		  
		  JMenuItem  configDetails = new JMenuItem("Configuration");
		  ConfigMenu.add(configDetails);
		  
		  
		  LogOff.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	            	TerminalLogOut();
	            }
		  });

		  configDetails.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	            	NodeConfiguration();
	            }
		  });
		  
		  
		  LogDetails.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	            	RequstLogsFromServer();
	            }
		  });
		  
		  
		  NodeReg.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	            	String NodeId = null;
	            	if (IsuserloggedIn()){
	            		UIManager.put("OptionPane.okButtonText", "Register");
	            		NodeId = JOptionPane.showInputDialog("Enter Node ID(Serial No):");
	            		try {
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
	            		UIManager.put("OptionPane.okButtonText", "Ok");
	        			JOptionPane.showMessageDialog(Terminal.Window, "Need to login First..");
	            	}
	                
	            }
	        });
		  
		  
		  NodeUreg.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {	
	            	if (IsuserloggedIn()){
	            		DoNodeUnRegistration(RegNodeList);	       	
	            	}
	            else{
	            		UIManager.put("OptionPane.okButtonText", "Ok");
	            		JOptionPane.showMessageDialog(Terminal.Window, "Need to login First..");
	            }
	            }
	           
	            });
		  
		  
		  
		  serverIp.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	            	UIManager.put("OptionPane.okButtonText", "Ok");
	                ServerIp = JOptionPane.showInputDialog(null, "Enter the Server IP:",ServerIp);
	            }
	        });
		  
		  serverPort.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	            	UIManager.put("OptionPane.okButtonText", "Ok");
	                ServerPort = JOptionPane.showInputDialog(null,"Enter the Server Port:",ServerPort);
	            }
	        });
		  
		  return menuBar;
		  
	  }
	
	public void TerminalLogOut(){
		  NodeMenu.setEnabled(false);
		  LogMenu.setEnabled(false);
		  ConfigMenu.setEnabled(false);
		  LogOff.setVisible(false);
		  CloseConnection();
		  
		  UserId.setText("");
		  Password.setText("");
		  Login.setEnabled(true);
		  Refresh.setEnabled(false);
		  ActNameList.setEnabled(false);
		  Update.setEnabled(false);
	}
		
	private void CloseConnection()
	{
		try {
			ClientSocket.close();
			UserLoggedIn = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Socket Connection(){
		Socket clientSocket = null;
		try {
			if (ServerIp != null && ServerPort != null ){
				clientSocket = new Socket(ServerIp, Integer.valueOf(ServerPort));
			}
			else{
				UIManager.put("OptionPane.okButtonText", "Ok");
				JOptionPane.showMessageDialog(Terminal.Window, "Set the Server Configuration");
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			UIManager.put("OptionPane.okButtonText", "Ok");
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
		return table;
		
	}
	
	public JTable getSensorTable(){
		return NodeInfo;
	}
	
	public JTable MakeActuratorTable(){
		 NodeControler = new NodeControlUi();
	     JTable sourceTable = new JTable(NodeControler);
	     return sourceTable;
	}
	

	private boolean IsuserloggedIn(){
		return UserLoggedIn;
	}
	
	
	public static void UpdateActNameList(String Name){
		boolean exists = false;
		 for (int index = 0; index < ActNameList.getItemCount() && !exists; index++) {
		   if (Name.equals(ActNameList.getItemAt(index))) {
		     exists = true;
		   }
		 }
		 if (!exists) {
			 ActNameList.addItem(Name);
		 }
	}
	
	private void UpdateActuratorTable(String NodeId, HashMap <String, String> ActratorInfo)
	{
		if (NodeControler == null){
			NodeControl = MakeActuratorTable();
		}
		NodeControler.UpdateSourceList(NodeId, ActratorInfo);
	}
  	
	private void NodeUpdateRequest(String NodeId, String ActuratorName, String Value){
		if (IsuserloggedIn()){
	    	try{
	    		BufferedWriter out;
	    		out = new BufferedWriter(new OutputStreamWriter(ClientSocket.getOutputStream()));
	    		String msg = Protocol.generateCommandMsg(NodeId, ActuratorName, Value);		
	    		Transport.sendMessage(out, msg);
	    	}catch (Exception e){
	    		e.printStackTrace();
	    	}
	      }
	}
	
	private void UpdateActionPerformed(ActionEvent e){
		String OptValue= null;
		String [] NodeList = RegNodeList;
		
		for (int i=0; i<NodeControler.getRowCount(); i++){
				boolean Value = (boolean)NodeControler.getValueAt(i,0);
				String  ActName = (String)NodeControler.getValueAt(i,2);
				String NodeId = (String)NodeControler.getValueAt(i, 3);
				
				switch(ActName){
				case "door":
					if (Value == true){
						OptValue = "close";
					}
					else{
						OptValue = "open";
					}
					break;
				case "light":
				case "alarm":
					if (Value == true){
						OptValue = "on";
						
					}
					else{
						OptValue = "off";
					}
					break;
				}
				if (ActName.equals(ActNameList.getSelectedItem())){
					NodeUpdateRequest(NodeId,ActName,OptValue);
				}
		}
		
	}
	
	
	
	private void RefreshActionPerformed(ActionEvent e){
		String []NodeList = RegNodeList;
		MakeEmptySensorTable();
		for (int i=0; i<NodeList.length; i++){
			GetNodeInformation(NodeList[i]);		
		}
		
		if (NodeControler == null){
			NodeControl.removeAll();
			NodeControl = MakeActuratorTable();
			NodeControl.repaint();
		}
	}
	
	private void EmergencyMsgDisplay(String Msg){
		String data = Protocol.getMsgEmergency(Msg);
		UIManager.put("OptionPane.okButtonText", "Ok");
		JOptionPane.showMessageDialog(Terminal.Window, new JLabel(
			    "<html><h2><font color='red'>" + data + "</font></h2></html>"));
	}
	
	private void InformationMsgDisplay(String Msg){
		String data = Protocol.getMsgInformation(Msg);
		UIManager.put("OptionPane.okButtonText", "Ok");
		JOptionPane.showMessageDialog(Terminal.Window, data);
	}
	
	public void HandleServerResponse(String MsgType, String Msg){
		
		switch(MsgType){
		case "login":
			ServerLogInResponse(Msg);
			break;
		case "sensor":
			break;
		case "command":
			break;
		case "register":
			ServerNodeRegistrationRes(Msg);
			break;
		case "unregister":
			ServerNodeUnRegistrationRes(Msg);
			break;
		case "emergency":
			System.out.println(Msg);
			EmergencyMsgDisplay(Msg);
			break;
		case "information":
			System.out.println(Msg);
			InformationMsgDisplay(Msg);
			break;
		case "nodeRegistered":
			ServerNodeRegisteredRes(Msg);
			break;
		case "nodeStatus":
			ServerNodeStatusRes(Msg);
			break;
		case "logData":
			ShowLogData(Msg);
			break;	
		}		
	}
	
	
	public void ServerLogInResponse(String Msg){
		if (Protocol.getResult(Msg).compareTo("success") == 0){
			UserLoggedIn = true;
			RequestRegsNodeList();
		}
		else if (Protocol.getResult(Msg).compareTo("fail") == 0){
			UIManager.put("OptionPane.okButtonText", "Ok");
			JOptionPane.showMessageDialog(Terminal.Window, "InValid Login !!!", "Login", JOptionPane.INFORMATION_MESSAGE);
			UserLoggedIn = false;
		}
		
		if ((ClientSocket !=null) && (UserLoggedIn == true)){
			Login.setEnabled(false);
			UserId.setEnabled(false);
			Password.setEditable(false);
			Refresh.setEnabled(true);
			Update.setEnabled(true);
			ActNameList.setEnabled(true);
			NodeControl.setEnabled(true);
			NodeMenu.setEnabled(true);
			LogMenu.setEnabled(true);
			ConfigMenu.setEnabled(true);
			LogOff.setVisible(false);
			System.out.println("SUCCESS..");
		}	
		
	}
	
	
	
	
	private void LoginActionPerformed(ActionEvent e) {
		// TODO add your code here
		if (ClientSocket == null){
			ClientSocket = Connection();
		}
		
		try {
			InputStream in = ClientSocket.getInputStream();
			BufferedWriter out;
			
			out = new BufferedWriter(new OutputStreamWriter(ClientSocket.getOutputStream()));
			String msg = Protocol.generateLoginMsg(UserId.getText(), Password.getText());
			Transport.sendMessage(out, msg);
			} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			UserLoggedIn = false;
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
		NodeControl.setEnabled(false);
		Refresh = new JButton();
		Refresh.setEnabled(false);
		ActNameList = new JComboBox();
		ActNameList.setEnabled(false);
		Update = new JButton();
		Update.setEnabled(false);

		//======== this ========

		// JFormDesigner evaluation mark
		setBorder(new javax.swing.border.CompoundBorder(
			new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
				"", javax.swing.border.TitledBorder.CENTER,
				javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
				java.awt.Color.red), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

		setLayout(new FormLayout(
			"9*(default, $lcgap), default",
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
		add(scrollPane1, CC.xywh(3, 15, 11, 1));

		//======== scrollPane2 ========
		{
			scrollPane2.setViewportView(NodeControl);
		}
		add(scrollPane2, CC.xywh(15, 15, 5, 1));

		//---- Refresh ----
		Refresh.setText("Refresh");
		add(Refresh, CC.xy(11, 17));
		add(ActNameList, CC.xy(15, 17));
		Refresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RefreshActionPerformed(e);
			}
		});

		//---- Update ----
		Update.setText("Update");
		add(Update, CC.xy(17, 17));
		Update.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UpdateActionPerformed(e);
			}
		});
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
	private static JComboBox ActNameList;
	private JButton Update;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
