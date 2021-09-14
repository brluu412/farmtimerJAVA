package timer;
import java.awt.*; //Component Classes
import java.awt.event.*; //Event classes and listener interfaces
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * @author Brian Luu
 * Timer that flip flops between 2 active timers and an overall timer. The 2 active timers have a customizable amount of time.
 *
 */
public class TimerGUI extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel titlePnl, uiPnl, timePnl, buttonPnl;
	private JLabel title, farmUI, lootUI, actTime, accTime, o1, o2;
	private JButton start, stop;
	private JTextField farmBox, lootBox;// Editable
	private JTextField actBox, accBox; // Non-Editable
	private Timer ovTimer;// Accumulated Timer, counts up
	private Timer actTimer;// Active Timer seconds for looting and farming
	private final int period = 1000;
	private int farmTime = 105, lootTime = 15;
	private int bigTime = 0;
	private int inputFarm = 105;
	private int inputLoot = 15;

	// constructor for GUI components
	public TimerGUI() {
		// timers
		ovTimer = new Timer();
		actTimer = new Timer();
		// Panels
		titlePnl = new JPanel(new FlowLayout(FlowLayout.CENTER));
		uiPnl = new JPanel(new GridLayout(2, 4, 5, 0));
		timePnl = new JPanel(new GridLayout(2, 2, 5, 0));
		buttonPnl = new JPanel(new GridLayout(1, 2, 20, 0));
		// COMPONENTS//
		// Labels
		title = new JLabel("FARM TIME");
		farmUI = new JLabel("Enter amount of seconds to farm");
		lootUI = new JLabel("Enter amount of seconds to loot");
		actTime = new JLabel("Active Timer");
		accTime = new JLabel("Accumulated Time");
		o1 = new JLabel("Farm Time: 0 seconds");// label shows user input went through
		o2 = new JLabel("Loot Time: 0 seconds");// label shows user input went through
		// Buttons
		start = new JButton("Start");
		start.addActionListener(this);
		stop = new JButton("Stop");
		stop.addActionListener(this);
		// TextFields
		farmBox = new JTextField("105", 3);
		lootBox = new JTextField("15", 3);
		actBox = new JTextField("00:00:00", 30);
		actBox.setEditable(false);
		accBox = new JTextField("00:00:00", 30);
		accBox.setEditable(false);
		// check for action
		farmBox.addActionListener(this);
		lootBox.addActionListener(this);

		// FONT
		// label
		title.setFont(title.getFont().deriveFont(48.0f));
		farmUI.setFont(farmUI.getFont().deriveFont(8f));
		lootUI.setFont(lootUI.getFont().deriveFont(8f));
		actTime.setFont(actTime.getFont().deriveFont(24f));
		accTime.setFont(accTime.getFont().deriveFont(24f));
		o1.setFont(o1.getFont().deriveFont(10f));
		o2.setFont(o2.getFont().deriveFont(10f));
		// button
		start.setFont(start.getFont().deriveFont(24.0f));
		stop.setFont(stop.getFont().deriveFont(24f));
		// text field
		farmBox.setFont(farmBox.getFont().deriveFont(24.0f));
		lootBox.setFont(lootBox.getFont().deriveFont(24.0f));
		actBox.setFont(actBox.getFont().deriveFont(24.0f));
		accBox.setFont(accBox.getFont().deriveFont(24.0f));

		// PANEL ORDERING//
		// title
		titlePnl.add(title);
		// uiPnl
		uiPnl.add(farmUI);
		uiPnl.add(farmBox);
		uiPnl.add(lootUI);
		uiPnl.add(lootBox);
		uiPnl.add(new Label());
		uiPnl.add(o1);
		uiPnl.add(new Label());
		uiPnl.add(o2);
		// timePnl
		timePnl.add(actTime);
		timePnl.add(actBox);
		timePnl.add(accTime);
		timePnl.add(accBox);
		// buttonPnl
		buttonPnl.add(start);
		buttonPnl.add(stop);

		// ADD EVERYTHING//
		setLayout(new GridLayout(4, 1, 10, 70));
		//add(new Panel());// for indentation
		add(titlePnl);
		add(uiPnl);
		add(timePnl);
		add(buttonPnl);
		//add(new Panel());// for indentation
	}

	/*
	 * Button actionEvent, actions when buttons are pressed
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (Checker(farmBox) && Checker(lootBox)) {
			o1.setText("Farm Time: " + Integer.parseInt(farmBox.getText()) + " seconds");
			o2.setText("Loot Time: " + Integer.parseInt(lootBox.getText()) + " seconds");
			inputFarm = Integer.parseInt(farmBox.getText());
			inputLoot = Integer.parseInt(lootBox.getText());
		}
		System.out.println(farmTime);
		System.out.println(lootTime);
		String com = e.getActionCommand();
		if (com.contentEquals("Start")) {
			title.setText("FARM TIME");
			// restarts and wipes all previous timers
			ovTimer.cancel();
			ovTimer.purge();
			actTimer.cancel();
			actTimer.purge();
			accBox.setText("00:00:00");
			actBox.setText("00:00:00");
			// instantiates new Timer
			ovTimer = new Timer();
			actTimer = new Timer();
			Accumulated();
		}
		if (com.contentEquals("Stop")) {
			title.setText("FARM TIME");
			ovTimer.cancel();
			ovTimer.purge();
			actTimer.cancel();
			actTimer.purge();
			actBox.setText("00:00:00");
			accBox.setText("00:00:00");
		}

	}

	// accumulated timer will start at 00:00:00 count up when start button is
	// pressed
	private void Accumulated() {
		farmTime = inputFarm;
		lootTime = inputLoot;
		System.out.println(farmTime + " " + lootTime);
		bigTime = 0;
		ovTimer.scheduleAtFixedRate(new TimerTask() {
			int ed = 1;

			@Override
			public void run() {
				accBox.setText(Formatter(bigTime++));// overall timer is called
				if (ed == 1) {// active timer that rotates between two countdowns
					title.setText("FARM TIME");
					if (farmTime > 1) {// if not zero, countdown till done
						actBox.setText(Formatter(farmTime) + "      FARM");
						farmTime--;
					} else if (farmTime == 1) {// when farmtime hits zero, move to lootTime
						actBox.setText(Formatter(farmTime) + "      FARM");
						lootTime = inputLoot;
						ed = 2;
					}
				} else if (ed == 2) {
					if (lootTime > 1) {
						if(lootTime % 2 == 0) {
							title.setText("=======TIME TO LOOT======");
						}
						if(lootTime % 2 == 1) {
							title.setText("|||||||TIME TO LOOT|||||||");
						}
						actBox.setText(Formatter(lootTime) + "      LOOT");
						lootTime--;
					} else if (lootTime == 1) {
						actBox.setText(Formatter(lootTime) + "      LOOT");
						farmTime = inputFarm;
						ed = 1;
					}

				}
			}

		}, 0, period);

	}

	/*
	 * takes an integer and formats it to 0:00:00 bless you stack overflow
	 */
	private String Formatter(int p) {

		String formattedTime = "";
		formattedTime += String.format("%02d", p / 3600) + ":";
		formattedTime += String.format("%02d", p / 60 % 60) + ":";
		formattedTime += String.format("%02d", p % 60);

		return formattedTime;
	}

	/*
	 * Checks if user input is an integer
	 */
	private boolean Checker(JTextField pp) {
		try {
			@SuppressWarnings("unused")
			int e = Integer.parseInt(pp.getText());
			pp.requestFocusInWindow();
			return true;
		} catch (Exception z) {
			JOptionPane.showMessageDialog(this, "Input numbers greater than 0 please :^)", "Big Boy Error",
					JOptionPane.ERROR_MESSAGE);
			pp.setText("");
			pp.requestFocusInWindow();
			return false;
		}
	}
	
	public static void main(String[] args) {
		TimerGUI boy = new TimerGUI();
		boy.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		boy.setTitle("Farm Timer");
		boy.setResizable(false);
		boy.setSize(540, 700);
		boy.setVisible(true);
	}
	
	
}
