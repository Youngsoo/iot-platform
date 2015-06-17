/*
 * Created by JFormDesigner on Thu Jun 18 01:52:03 IST 2015
 */

package cmu.team5.terminal;

import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author swapan pati
 */
public class Terminalwindow extends JPanel {
	public Terminalwindow() {
		initComponents();
	}

	private void LoginActionPerformed(ActionEvent e) {
		// TODO add your code here
		System.out.println(" "+ UserId.getText() + " " + Password.getText());
		
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

		//======== this ========

		// JFormDesigner evaluation mark
		setBorder(new javax.swing.border.CompoundBorder(
			new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
				"", javax.swing.border.TitledBorder.CENTER,
				javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
				java.awt.Color.red), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

		setLayout(new FormLayout(
			"7*(default, $lcgap), default",
			"6*(default, $lgap), default"));

		//---- label1 ----
		label1.setText("IoT Terminal System");
		add(label1, CC.xy(11, 3));

		//---- label2 ----
		label2.setText("User ID");
		add(label2, CC.xy(11, 7));

		//---- UserId ----
		UserId.setColumns(10);
		add(UserId, CC.xy(15, 7));

		//---- label3 ----
		label3.setText("Password");
		add(label3, CC.xy(11, 9));
		add(Password, CC.xy(15, 9));

		//---- Login ----
		Login.setText("Login");
		Login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LoginActionPerformed(e);
			}
		});
		add(Login, CC.xy(15, 13));
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
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
