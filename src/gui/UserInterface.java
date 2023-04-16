package gui;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import script.LMS;

public class UserInterface extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7659834944270637489L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public UserInterface() {
		setBounds(100, 100, 200, 78);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);

		JCheckBox slaveCheckBox = new JCheckBox("Slave?");
		contentPane.add(slaveCheckBox);

		JButton startButton = new JButton("START");
		contentPane.add(startButton);
		startButton.addActionListener(e -> {
			LMS.slave = slaveCheckBox.isSelected();
			System.out.println("We are a slave: " + slaveCheckBox.isEnabled());
			dispose();
			LMS.started = true;
		});
	}

}
