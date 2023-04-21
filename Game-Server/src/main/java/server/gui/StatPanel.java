package server.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

public class StatPanel extends JPanel {
	
	private static final long serialVersionUID = -6760771714735074059L;
	private static JLabel lbl_isRun;
	private static JLabel lbl_TPU;
	private static JLabel lbl_UPS;
	private static JLabel lbl_que_r;
	private static JLabel lbl_que_s;
	private static List<String> players = new ArrayList<>();
	private static JTabbedPane tabbedPane;
	private static JLabel lbl_playerCount;
	private static JList<String> list_player;
	private static DefaultListModel<String> listModel = new DefaultListModel<>();

	public StatPanel() {
		setBackground(Color.LIGHT_GRAY);
		setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.LIGHT_GRAY);
		add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Stats", null, panel, null);
		panel.setLayout(new BorderLayout(5, 5));
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);
		
		JPanel panel_2 = new JPanel();
		scrollPane.setViewportView(panel_2);
		panel_2.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("3px"),
				FormSpecs.DEFAULT_COLSPEC,
				ColumnSpec.decode("4px"),
				FormSpecs.DEFAULT_COLSPEC,
				ColumnSpec.decode("3px"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JLabel lblNewLabel = new JLabel("Is Running:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel_2.add(lblNewLabel, "2, 2");
		
		lbl_isRun = new JLabel("false");
		panel_2.add(lbl_isRun, "4, 2");
		
		JLabel lblServerTpu = new JLabel("Server TPU:");
		lblServerTpu.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel_2.add(lblServerTpu, "2, 4");
		
		lbl_TPU = new JLabel("0");
		panel_2.add(lbl_TPU, "4, 4");
		
		JLabel lblServerUpdates = new JLabel("Server UPS:");
		lblServerUpdates.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel_2.add(lblServerUpdates, "2, 6");
		
		lbl_UPS = new JLabel("0");
		panel_2.add(lbl_UPS, "4, 6");
		
		JLabel lblRecQue = new JLabel("Rec. Que:");
		lblRecQue.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel_2.add(lblRecQue, "2, 8");
		
		lbl_que_r = new JLabel("0");
		panel_2.add(lbl_que_r, "4, 8");
		
		JLabel lblServerUpdates_1_1 = new JLabel("Send Que:");
		lblServerUpdates_1_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel_2.add(lblServerUpdates_1_1, "2, 10");
		
		lbl_que_s = new JLabel("0");
		panel_2.add(lbl_que_s, "4, 10");
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.GRAY);
		panel_1.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.WHITE, Color.BLACK));
		tabbedPane.addTab("Players", null, panel_1, null);
		panel_1.setLayout(new BorderLayout(2, 2));
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.WHITE, Color.BLACK));
		panel_3.setBackground(Color.LIGHT_GRAY);
		panel_1.add(panel_3, BorderLayout.NORTH);
		
		lbl_playerCount = new JLabel("Player count: ");
		panel_3.add(lbl_playerCount);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.WHITE, Color.BLACK));
		panel_1.add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		list_player = new JList<String>();
		list_player.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_player.setModel(listModel);
		panel_4.add(list_player);
		
	}
	
	public static void updateStats(double tpu, int ups, boolean running) {
		lbl_isRun.setText(Boolean.toString(running));
		lbl_TPU.setText(Double.toString(tpu));
		lbl_UPS.setText(Integer.toString(ups));
	}
	
	public static void setReceivedQue(int num) {
		lbl_que_r.setText(String.valueOf(num));
	}
	
	public static void setSendQue(int num) {
		lbl_que_s.setText(String.valueOf(num));
	}
	
	private static void updatePlayers (){
		listModel.clear();
		listModel.addAll(players);
		lbl_playerCount.setText("Player count: " + players.size());
	}

	public static void addPlayer(String name) {
		if (players.contains(name)) return;
		players.add(name);
		Collections.sort(players);
		updatePlayers();
	}
	
	public static void removePlayer(String name) {
		if (!players.contains(name)) return;
		players.remove(name);
		updatePlayers();
	}
}
