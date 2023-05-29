package server.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import server.Server;

public class MainServerFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4990311917343178188L;
	private JPanel contentPane;
	private JTextField textField;

	public void open() {
		setVisible(true);
	}

	public MainServerFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Server.stopServer();
			}
		});
		setBounds(100, 100, 400, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu_1 = new JMenu("Server");
		menuBar.add(mnNewMenu_1);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Options");
		mnNewMenu_1.add(mntmNewMenuItem_3);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		mnNewMenu_1.add(verticalStrut);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Start");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Server.startServer();
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Stop");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Server.stopServer();
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Restart");
		mntmNewMenuItem_2.setEnabled(false);
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Server.restart();
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(2, 2));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		LogPanel panel_1 = new LogPanel();
		panel.add(panel_1, BorderLayout.CENTER);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));
		
		textField = new JTextField();
		panel_2.add(textField);
		textField.setColumns(10);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		panel_2.add(horizontalStrut);
		
		JButton btnNewButton = new JButton("Execute");
		panel_2.add(btnNewButton);
		
		StatPanel panel_3 = new StatPanel();
		panel_3.setPreferredSize(new Dimension(200, 90));
		contentPane.add(panel_3, BorderLayout.EAST);
		
		
	}

}
