package server.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;
import javax.swing.text.DefaultCaret;

public class LogPanel extends JPanel {
	
	private static final long serialVersionUID = 6189418237251978465L;
	private static JTextPane textPane_Log = new JTextPane();

	public LogPanel() {
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.LIGHT_GRAY, Color.BLACK));
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		panel.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.LIGHT_GRAY, Color.BLACK));
		scrollPane.setColumnHeaderView(panel);
		
		JLabel lblNewLabel = new JLabel("Log");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel.add(lblNewLabel);
		
		JPanel panel_1 = new JPanel();
		scrollPane.setViewportView(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		textPane_Log.setBackground(Color.LIGHT_GRAY);
		textPane_Log.setEditable(false);
		panel_1.add(textPane_Log, BorderLayout.CENTER);
		textPane_Log.setAutoscrolls(true);
		
		DefaultCaret caret = (DefaultCaret)textPane_Log.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

	}

	public static JTextPane getTextPane_Log() {
		return textPane_Log;
	}
}
