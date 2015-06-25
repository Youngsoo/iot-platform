/*
 * Created by JFormDesigner on Wed Jun 24 02:02:34 IST 2015
 */

package cmu.team5.terminal;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author swapan pati
 */
public class NodeConfigWindow extends JPanel {
	
	static String lightVal="300" ;
	static String alarmVal="300";
	static String logVal="300";
	
	private void UpdateConfigVal()
	{
		lightVal = getLightConfig();
		alarmVal=getAlarmConfig();
		logVal=getLogConfig();
	}
	
	public NodeConfigWindow() {
		initComponents();
		SetInitValue(lightVal,alarmVal,logVal);
		
		update.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UpdateOptPerform();
			}
		});
		
		Cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CancelOptPerform();
			}
		});
	}
	
	private void CancelOptPerform()
	{
		JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
		frame.dispose();
	}
	
	private void UpdateOptPerform()
	{
	   Terminalwindow.ServerConfigUpdateReq(getLightConfig(), getAlarmConfig(), getLogConfig());	
	   CancelOptPerform();
	   UpdateConfigVal();
	}
	
	private void SetInitValue(String lightVal, String alarmVal, String logVal)
	{
		lightText.setText(lightVal);
		alarmText.setText(alarmVal);
		logText.setText(logVal);
	}
	
	public void setLightConfig(String Value)
	{
		lightText.setText(Value);
	}
	
	public void setAlarmConfig(String Value)
	{
		alarmText.setText(Value);
	}
	
	public void setLogConfig(String Value)
	{
		logText.setText(Value);
	}
	
	public String getLightConfig()
	{
		return lightText.getText();
	}
	public String getAlarmConfig()
	{
		return alarmText.getText();
	}
	
	public String getLogConfig()
	{
		return logText.getText();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - swapan pati
		configLevel = new JLabel();
		lightconfig = new JLabel();
		lightText = new JTextField();
		alarmconfig = new JLabel();
		alarmText = new JTextField();
		logconfig = new JLabel();
		logText = new JTextField();
		update = new JButton();
		Cancel = new JButton();

		//======== this ========

		// JFormDesigner evaluation mark
		setBorder(new javax.swing.border.CompoundBorder(
			new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
				"", javax.swing.border.TitledBorder.CENTER,
				javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
				java.awt.Color.red), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

		setLayout(new FormLayout(
			"3*(default, $lcgap), 8dlu, $lcgap, default, $lcgap, 34dlu",
			"11*(default, $lgap), default"));

		//---- configLevel ----
		configLevel.setText("Node Configuration:");
		add(configLevel, CC.xywh(3, 1, 9, 1));

		//---- lightconfig ----
		lightconfig.setText("Light Auto-Switch Off Duration (in Sec) :");
		add(lightconfig, CC.xy(3, 5));

		//---- lightText ----
		lightText.setColumns(10);
		add(lightText, CC.xywh(8, 5, 4, 1));

		//---- alarmconfig ----
		alarmconfig.setText("Alarm Auto-Switch On Duration (in Sec):");
		add(alarmconfig, CC.xy(3, 9));

		//---- alarmText ----
		alarmText.setColumns(10);
		add(alarmText, CC.xywh(8, 9, 4, 1));

		//---- logconfig ----
		logconfig.setText("Log History Capture Duration :");
		add(logconfig, CC.xy(3, 13));

		//---- logText ----
		logText.setColumns(10);
		add(logText, CC.xywh(8, 13, 4, 1));

		//---- update ----
		update.setText("Update");
		add(update, CC.xy(3, 23));
		//---- Cancel ----
		Cancel.setText("Cancel");
		add(Cancel, CC.xywh(5, 23, 7, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - swapan pati
	private JLabel configLevel;
	private JLabel lightconfig;
	private JTextField lightText;
	private JLabel alarmconfig;
	private JTextField alarmText;
	private JLabel logconfig;
	private JTextField logText;
	private JButton update;
	private JButton Cancel;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
