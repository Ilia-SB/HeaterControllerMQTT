package heating.mqtt;

import java.awt.EventQueue;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.xml.bind.DatatypeConverter;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import net.miginfocom.swing.MigLayout;
import ohbroker.HeaterItem;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;

public class HeatingControllerMqtt extends JFrame {
	private boolean started = false;
	
	private String selectedItem = "000000";
	
	private int consumptionLimit, totalConsumption;
	private float hysteresis;
	
	private MqttClient mqttClient;
	private MqttConnectOptions mqttConnectOptions;
	private String mqttUser = "mqtt_controller";
	private String mqttPassword = "1234";
	private String mqttId = "JavaController";
	private boolean mqttIsConnected = false;
	
	private static final String COMMAND_TOPIC = "ehome/heating/commands";
	private static final String STATUS_TOPIC = "ehome/heating/statuses";
	private static final String RAW_TOPIC = "ehome/heating/raw";
	private static final String DEBUG_TOPIC = "ehome/heating/debug";
	
	private static final String TOTAL_CONSUMPTION_ITEM = "total_consumption";
	private static final String TEMP_ITEM = "_temp";
	private static final String IS_AUTO_ITEM = "_isauto";
	private static final String IS_ON_ITEM = "_ison";
	private static final String PRIORITY_ITEM = "_prio";
	private static final String TARGET_TEMPERATURE_ITEM = "_targettemp";
	private static final String CONSUMPTION_LIMIT_ITEM = "consumption_limit";
	private static final String HYSTERESIS_ITEM = "hysteresis";
	private static final String SENSOR_ITEM = "_sensor";
	private static final String PORT_ITEM = "_port";
	private static final String TEMPERATURE_ADJUST_ITEM = "_tempadjust";
	private static final String CONSUMPTION_ITEM = "_consumption";
	private static final String IS_ENABLED_ITEM = "_isenabled";
	private static final String EEPROM_ERROR_ITEM = "eeprom_error";
	private static final String TEMPERATURE_READ_ERROR_ITEM = "temp_read_error";
	
	private HashMap<String, HeaterItem> items;
	/**
	 * Interface
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtHysteresis;
	private JButton btnSetHysteresis;
	private JTextField txtConsumptionLimit;
	private JButton btnSetConsumptionLimit;
	private JRadioButton rbIsAuto1;
	private JRadioButton rbIsOn1;
	private JRadioButton rbIsAuto2;
	private JRadioButton rbIsOn2;
	private JRadioButton rbIsAuto3;
	private JRadioButton rbIsOn3;
	private JRadioButton rbIsAuto4;
	private JRadioButton rbIsOn4;
	private JRadioButton rbIsAuto5;
	private JRadioButton rbIsOn5;
	private JRadioButton rbIsAuto6;
	private JRadioButton rbIsOn6;
	private JRadioButton rbIsAuto7;
	private JRadioButton rbIsOn7;
	private JRadioButton rbIsAuto8;
	private JRadioButton rbIsOn8;
	private JRadioButton rbIsAuto9;
	private JRadioButton rbIsOn9;
	private JRadioButton rbIsAuto10;
	private JRadioButton rbIsOn10;
	private JRadioButton rbIsAuto11;
	private JRadioButton rbIsOn11;
	private JRadioButton rbIsAuto12;
	private JRadioButton rbIsOn12;
	private JRadioButton rbIsAuto13;
	private JRadioButton rbIsOn13;
	private JRadioButton rbIsAuto14;
	private JRadioButton rbIsOn14;
	private JTextField txtServerAddress;
	private JButton btnConnectToServer;
	private JComboBox<String> cbItems;
	private JCheckBox chkIsEnabled;
	private JLabel lblTemperature;
	private JCheckBox chkIsAuto;
	private JCheckBox chkIsOn;
	private JTextField txtPort;
	private JButton btnSetPort;
	private JButton btnSetPriority;
	private JTextField txtPriority;
	private JTextField txtTargetTemp;
	private JTextField txtTempAdjust;
	private JTextField txtConsumption;
	private JButton btnSetTargetTemp;
	private JButton btnSetTempAdjust;
	private JButton btnSetConsumption;
	private JLabel lblTotalConsumption;
	private JTextArea txtOutput;
	private JScrollPane scrollPane;
	private JPanel pnlOverview;
	private JLabel lblTemp1;
	private JLabel lblTemp2;
	private JLabel lblTemp3;
	private JLabel lblTemp4;
	private JLabel lblTemp5;
	private JLabel lblTemp6;
	private JLabel lblTemp7;
	private JLabel lblTemp8;
	private JLabel lblTemp9;
	private JLabel lblTemp10;
	private JLabel lblTemp11;
	private JLabel lblTemp12;
	private JLabel lblTemp13;
	private JLabel lblTemp14;
	private JTextField txtSensorAddress;
	private JButton btnSetSensorAddr;
	private JLabel lblMassTemp;
	private JTextField txtMassTemp;
	private JButton btnSetMassTargetTemp;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HeatingControllerMqtt frame = new HeatingControllerMqtt();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public HeatingControllerMqtt() {
		createComponents();
		populateItems();
		createEvents();
		started = true;
	}
	
	private void connectToMqttServer() {
		mqttConnectOptions = new MqttConnectOptions();
		mqttConnectOptions.setCleanSession(true);
		mqttConnectOptions.setKeepAliveInterval(30);
		mqttConnectOptions.setUserName(mqttUser);
		mqttConnectOptions.setPassword(mqttPassword.toCharArray());
		
		try {
			mqttClient = new MqttClient(txtServerAddress.getText(), mqttId);
			mqttClient.setCallback(new MyMqttCallback());
			mqttClient.connect(mqttConnectOptions);
		} catch (MqttException e) {
				e.printStackTrace();
				System.out.println("Unable to connect to server");
				System.exit(-1);
		}
		System.out.println("Connected to mqtt server " + txtServerAddress.getText());
		mqttIsConnected = mqttClient.isConnected();
	}
	
	private void populateItems() {
		items = new HashMap<String, HeaterItem>();
		for (int i=0; i<14; i++) {
			String addr = String.format("%06X", i);
			HeaterItem heater = new HeaterItem();
			items.put(addr, heater);
			cbItems.addItem(addr);
		}
		
		items.get("000000").btnAuto = rbIsAuto1;
		items.get("000001").btnAuto = rbIsAuto2;
		items.get("000002").btnAuto = rbIsAuto3;
		items.get("000003").btnAuto = rbIsAuto4;
		items.get("000004").btnAuto = rbIsAuto5;
		items.get("000005").btnAuto = rbIsAuto6;
		items.get("000006").btnAuto = rbIsAuto7;
		items.get("000007").btnAuto = rbIsAuto8;
		items.get("000008").btnAuto = rbIsAuto9;
		items.get("000009").btnAuto = rbIsAuto10;
		items.get("00000A").btnAuto = rbIsAuto11;
		items.get("00000B").btnAuto = rbIsAuto12;
		items.get("00000C").btnAuto = rbIsAuto13;
		items.get("00000D").btnAuto = rbIsAuto14;
		
		items.get("000000").btnOn = rbIsOn1;
		items.get("000001").btnOn = rbIsOn2;
		items.get("000002").btnOn = rbIsOn3;
		items.get("000003").btnOn = rbIsOn4;
		items.get("000004").btnOn = rbIsOn5;
		items.get("000005").btnOn = rbIsOn6;
		items.get("000006").btnOn = rbIsOn7;
		items.get("000007").btnOn = rbIsOn8;
		items.get("000008").btnOn = rbIsOn9;
		items.get("000009").btnOn = rbIsOn10;
		items.get("00000A").btnOn = rbIsOn11;
		items.get("00000B").btnOn = rbIsOn12;
		items.get("00000C").btnOn = rbIsOn13;
		items.get("00000D").btnOn = rbIsOn14;
		
		items.get("000000").txtTemp = lblTemp1;
		items.get("000001").txtTemp = lblTemp2;
		items.get("000002").txtTemp = lblTemp3;
		items.get("000003").txtTemp = lblTemp4;
		items.get("000004").txtTemp = lblTemp5;
		items.get("000005").txtTemp = lblTemp6;
		items.get("000006").txtTemp = lblTemp7;
		items.get("000007").txtTemp = lblTemp8;
		items.get("000008").txtTemp = lblTemp9;
		items.get("000009").txtTemp = lblTemp10;
		items.get("00000A").txtTemp = lblTemp11;
		items.get("00000B").txtTemp = lblTemp12;
		items.get("00000C").txtTemp = lblTemp13;
		items.get("00000D").txtTemp = lblTemp14;
	}
	
	private void subscribeToTopics() {
		try {
			mqttClient.subscribe(STATUS_TOPIC + "/#", 0);
			mqttClient.subscribe(DEBUG_TOPIC, 0);
		} catch (MqttException e) {
			e.printStackTrace();
			System.out.println("Unable to subscribe to topics");
			System.exit(-1);
		}
	}

	private void createComponents() {
		setTitle("HeatingController mqtt");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 708, 634);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		pnlOverview = new JPanel();
		pnlOverview.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
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
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JLabel label = new JLabel("1");
		pnlOverview.add(label, "2, 2");
		
		rbIsAuto1 = new JRadioButton("");
		rbIsAuto1.setFocusable(false);
		pnlOverview.add(rbIsAuto1, "4, 2");
		
		rbIsOn1 = new JRadioButton("");
		rbIsOn1.setFocusable(false);
		pnlOverview.add(rbIsOn1, "6, 2");
		
		lblTemp1 = new JLabel("");
		pnlOverview.add(lblTemp1, "8, 2");
		
		JLabel label_8 = new JLabel("8");
		pnlOverview.add(label_8, "12, 2");
		
		rbIsAuto8 = new JRadioButton("");
		rbIsAuto8.setFocusable(false);
		pnlOverview.add(rbIsAuto8, "14, 2");
		
		rbIsOn8 = new JRadioButton("");
		rbIsOn8.setFocusable(false);
		pnlOverview.add(rbIsOn8, "16, 2");
		
		lblTemp8 = new JLabel("");
		pnlOverview.add(lblTemp8, "18, 2");
		
		JLabel label_1 = new JLabel("2");
		pnlOverview.add(label_1, "2, 4");
		
		rbIsAuto2 = new JRadioButton("");
		rbIsAuto2.setFocusable(false);
		pnlOverview.add(rbIsAuto2, "4, 4");
		
		rbIsOn2 = new JRadioButton("");
		rbIsOn2.setFocusable(false);
		pnlOverview.add(rbIsOn2, "6, 4");
		
		lblTemp2 = new JLabel("");
		pnlOverview.add(lblTemp2, "8, 4");
		
		JLabel label_9 = new JLabel("9");
		pnlOverview.add(label_9, "12, 4");
		
		rbIsAuto9 = new JRadioButton("");
		rbIsAuto9.setFocusable(false);
		pnlOverview.add(rbIsAuto9, "14, 4");
		
		rbIsOn9 = new JRadioButton("");
		rbIsOn9.setFocusable(false);
		pnlOverview.add(rbIsOn9, "16, 4");
		
		lblTemp9 = new JLabel("");
		pnlOverview.add(lblTemp9, "18, 4");
		
		JLabel label_2 = new JLabel("3");
		pnlOverview.add(label_2, "2, 6");
		
		rbIsAuto3 = new JRadioButton("");
		rbIsAuto3.setFocusable(false);
		pnlOverview.add(rbIsAuto3, "4, 6");
		
		rbIsOn3 = new JRadioButton("");
		rbIsOn3.setFocusable(false);
		pnlOverview.add(rbIsOn3, "6, 6");
		
		lblTemp3 = new JLabel("");
		pnlOverview.add(lblTemp3, "8, 6");
		
		JLabel lblA = new JLabel("10");
		pnlOverview.add(lblA, "12, 6");
		
		rbIsAuto10 = new JRadioButton("");
		rbIsAuto10.setFocusable(false);
		pnlOverview.add(rbIsAuto10, "14, 6");
		
		rbIsOn10 = new JRadioButton("");
		rbIsOn10.setFocusable(false);
		pnlOverview.add(rbIsOn10, "16, 6");
		
		lblTemp10 = new JLabel("");
		pnlOverview.add(lblTemp10, "18, 6");
		
		JLabel label_3 = new JLabel("4");
		pnlOverview.add(label_3, "2, 8");
		
		rbIsAuto4 = new JRadioButton("");
		rbIsAuto4.setFocusable(false);
		pnlOverview.add(rbIsAuto4, "4, 8");
		
		rbIsOn4 = new JRadioButton("");
		rbIsOn4.setFocusable(false);
		pnlOverview.add(rbIsOn4, "6, 8");
		
		lblTemp4 = new JLabel("");
		pnlOverview.add(lblTemp4, "8, 8");
		
		JLabel lblB = new JLabel("11");
		pnlOverview.add(lblB, "12, 8");
		
		rbIsAuto11 = new JRadioButton("");
		rbIsAuto11.setFocusable(false);
		pnlOverview.add(rbIsAuto11, "14, 8");
		
		rbIsOn11 = new JRadioButton("");
		rbIsOn11.setFocusable(false);
		pnlOverview.add(rbIsOn11, "16, 8");
		
		lblTemp11 = new JLabel("");
		pnlOverview.add(lblTemp11, "18, 8");
		
		JLabel label_4 = new JLabel("5");
		pnlOverview.add(label_4, "2, 10");
		
		rbIsAuto5 = new JRadioButton("");
		rbIsAuto5.setFocusable(false);
		pnlOverview.add(rbIsAuto5, "4, 10");
		
		rbIsOn5 = new JRadioButton("");
		rbIsOn5.setFocusable(false);
		pnlOverview.add(rbIsOn5, "6, 10");
		
		lblTemp5 = new JLabel("");
		pnlOverview.add(lblTemp5, "8, 10");
		
		JLabel label_10 = new JLabel("12");
		pnlOverview.add(label_10, "12, 10");
		
		rbIsAuto12 = new JRadioButton("");
		rbIsAuto12.setFocusable(false);
		pnlOverview.add(rbIsAuto12, "14, 10");
		
		rbIsOn12 = new JRadioButton("");
		rbIsOn12.setFocusable(false);
		pnlOverview.add(rbIsOn12, "16, 10");
		
		lblTemp12 = new JLabel("");
		pnlOverview.add(lblTemp12, "18, 10");
		
		JLabel label_5 = new JLabel("6");
		pnlOverview.add(label_5, "2, 12");
		
		rbIsAuto6 = new JRadioButton("");
		rbIsAuto6.setFocusable(false);
		pnlOverview.add(rbIsAuto6, "4, 12");
		
		rbIsOn6 = new JRadioButton("");
		rbIsOn6.setFocusable(false);
		pnlOverview.add(rbIsOn6, "6, 12");
		
		lblTemp6 = new JLabel("");
		pnlOverview.add(lblTemp6, "8, 12");
		
		JLabel label_11 = new JLabel("13");
		pnlOverview.add(label_11, "12, 12");
		
		rbIsAuto13 = new JRadioButton("");
		rbIsAuto13.setFocusable(false);
		pnlOverview.add(rbIsAuto13, "14, 12");
		
		rbIsOn13 = new JRadioButton("");
		rbIsOn13.setFocusable(false);
		pnlOverview.add(rbIsOn13, "16, 12");
		
		lblTemp13 = new JLabel("");
		pnlOverview.add(lblTemp13, "18, 12");
		
		JLabel label_6 = new JLabel("7");
		pnlOverview.add(label_6, "2, 14");
		
		rbIsAuto7 = new JRadioButton("");
		rbIsAuto7.setFocusable(false);
		pnlOverview.add(rbIsAuto7, "4, 14");
		
		rbIsOn7 = new JRadioButton("");
		rbIsOn7.setFocusable(false);
		pnlOverview.add(rbIsOn7, "6, 14");
		
		lblTemp7 = new JLabel("");
		pnlOverview.add(lblTemp7, "8, 14");
		
		JLabel label_12 = new JLabel("14");
		pnlOverview.add(label_12, "12, 14");
		
		rbIsAuto14 = new JRadioButton("");
		rbIsAuto14.setFocusable(false);
		pnlOverview.add(rbIsAuto14, "14, 14");
		
		rbIsOn14 = new JRadioButton("");
		rbIsOn14.setFocusable(false);
		pnlOverview.add(rbIsOn14, "16, 14");
		
		JPanel panel = new JPanel();
		
		txtOutput = new JTextArea();
		txtOutput.setLineWrap(false);
		
		DefaultCaret caret = (DefaultCaret)txtOutput.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		scrollPane = new JScrollPane(txtOutput);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(pnlOverview, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane)
					.addGap(1))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(pnlOverview, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(11)
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 577, Short.MAX_VALUE)))
					.addGap(1))
		);
		
		lblTemp14 = new JLabel("");
		pnlOverview.add(lblTemp14, "18, 14");
		panel.setLayout(new MigLayout("", "[][][]", "[][][][][][][][][][][][26.00][][]"));
		
		txtServerAddress = new JTextField();
		txtServerAddress.setText("tcp://localhost:1883");
		panel.add(txtServerAddress, "cell 0 0 2 1,growx");
		txtServerAddress.setColumns(10);
		
		btnConnectToServer = new JButton("Connect");
		panel.add(btnConnectToServer, "cell 2 0");
		
		cbItems = new JComboBox<String>();
		panel.add(cbItems, "cell 0 1 3 1,growx");
		
		JLabel lblSensorAddr = new JLabel("Sensor addr");
		panel.add(lblSensorAddr, "cell 0 2,alignx left");
		
		txtSensorAddress = new JTextField();
		txtSensorAddress.setFont(new Font("Tahoma", Font.PLAIN, 10));
		panel.add(txtSensorAddress, "cell 1 2,alignx center");
		txtSensorAddress.setColumns(10);
		
		btnSetSensorAddr = new JButton("Set");
		panel.add(btnSetSensorAddr, "cell 2 2");
		
		JLabel lblTemp = new JLabel("Temp");
		panel.add(lblTemp, "cell 0 3");
		
		lblTemperature = new JLabel("");
		panel.add(lblTemperature, "cell 1 3");
		
		chkIsEnabled = new JCheckBox("Enabled");
		panel.add(chkIsEnabled, "flowx,cell 0 4 3 1,alignx left");
		
		JLabel lblNewLabel = new JLabel("Port");
		panel.add(lblNewLabel, "cell 0 5,alignx left");
		
		txtPort = new JTextField();
		panel.add(txtPort, "cell 1 5,alignx center");
		txtPort.setColumns(10);
		
		btnSetPort = new JButton("Set");
		panel.add(btnSetPort, "cell 2 5,alignx leading");
		
		JLabel lblPriority = new JLabel("Priority");
		panel.add(lblPriority, "cell 0 6,alignx left");
		
		txtPriority = new JTextField();
		panel.add(txtPriority, "cell 1 6,alignx center");
		txtPriority.setColumns(10);
		
		btnSetPriority = new JButton("Set");
		panel.add(btnSetPriority, "cell 2 6");
		
		JLabel lblTargetTemp = new JLabel("Target temp");
		panel.add(lblTargetTemp, "cell 0 7,alignx left");
		
		txtTargetTemp = new JTextField();
		panel.add(txtTargetTemp, "cell 1 7,alignx center");
		txtTargetTemp.setColumns(10);
		
		btnSetTargetTemp = new JButton("Set");
		panel.add(btnSetTargetTemp, "cell 2 7");
		
		JLabel lblTempAdjust = new JLabel("Temp adjust");
		panel.add(lblTempAdjust, "cell 0 8,alignx left");
		
		txtTempAdjust = new JTextField();
		panel.add(txtTempAdjust, "cell 1 8,alignx center");
		txtTempAdjust.setColumns(10);
		
		btnSetTempAdjust = new JButton("Set");
		panel.add(btnSetTempAdjust, "cell 2 8");
		
		JLabel lblConsumption = new JLabel("Consumption");
		panel.add(lblConsumption, "cell 0 9,alignx left");
		
		txtConsumption = new JTextField();
		panel.add(txtConsumption, "cell 1 9,alignx center");
		txtConsumption.setColumns(10);
		
		btnSetConsumption = new JButton("Set");
		panel.add(btnSetConsumption, "cell 2 9");
		
		lblMassTemp = new JLabel("Mass target");
		panel.add(lblMassTemp, "cell 0 10,alignx left");
		
		txtMassTemp = new JTextField();
		panel.add(txtMassTemp, "cell 1 10,alignx center");
		txtMassTemp.setColumns(10);
		
		btnSetMassTargetTemp = new JButton("Set");
		panel.add(btnSetMassTargetTemp, "cell 2 10");
		
		JLabel lblTotalCons = new JLabel("Total cons:");
		panel.add(lblTotalCons, "cell 0 11,aligny bottom");
		
		lblTotalConsumption = new JLabel("");
		panel.add(lblTotalConsumption, "cell 1 11,aligny bottom");
		
		JLabel lblConsLimit = new JLabel("Cons limit:");
		panel.add(lblConsLimit, "cell 0 12");
		
		txtConsumptionLimit = new JTextField();
		panel.add(txtConsumptionLimit, "cell 1 12,alignx center");
		txtConsumptionLimit.setColumns(10);
		
		btnSetConsumptionLimit = new JButton("Set");
		panel.add(btnSetConsumptionLimit, "cell 2 12");
		
		JLabel lblHyst = new JLabel("Hysteresis:");
		panel.add(lblHyst, "cell 0 13");
		
		txtHysteresis = new JTextField();
		panel.add(txtHysteresis, "cell 1 13,alignx center");
		txtHysteresis.setColumns(10);
		
		chkIsAuto = new JCheckBox("Auto");
		panel.add(chkIsAuto, "cell 1 4");
		
		chkIsOn = new JCheckBox("On");
		panel.add(chkIsOn, "cell 1 4");
		
		btnSetHysteresis = new JButton("Set");
		panel.add(btnSetHysteresis, "cell 2 13");
		contentPane.setLayout(gl_contentPane);
	}
	
	private void createEvents() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				disconnectFromMqttServer();
			}
		});
		
		btnConnectToServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!mqttIsConnected) {
					connectToMqttServer();
					subscribeToTopics();
				} else {
					disconnectFromMqttServer();
				}
				
				if(!mqttIsConnected) {
					btnConnectToServer.setText("Connect");
				} else {
					btnConnectToServer.setText("Disconnect");
				}
			}
		});
		
		cbItems.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				selectedItem = cbItems.getSelectedItem().toString();
				updateHeater();
			}
		});
		
		btnSetSensorAddr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				publishToTopic(COMMAND_TOPIC + "/item_" + selectedItem + SENSOR_ITEM, txtSensorAddress.getText(), 0, false);
			}
		});
		
		chkIsEnabled.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				publishToTopic(COMMAND_TOPIC + "/item_" + selectedItem + IS_ENABLED_ITEM, chkIsEnabled.isSelected() ? "ON" : "OFF", 0, false);
			}
		});
		
		chkIsAuto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				publishToTopic(COMMAND_TOPIC + "/item_" + selectedItem + IS_AUTO_ITEM, chkIsAuto.isSelected() ? "ON" : "OFF", 0, false);
			}
		});
		
		chkIsOn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				publishToTopic(COMMAND_TOPIC + "/item_" + selectedItem + IS_ON_ITEM, chkIsOn.isSelected() ? "ON" : "OFF", 0, false);
			}
		});
		
		btnSetPort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				publishToTopic(COMMAND_TOPIC + "/item_" + selectedItem + PORT_ITEM, txtPort.getText(), 0, false);
			}
		});
		
		btnSetPriority.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				publishToTopic(COMMAND_TOPIC + "/item_" + selectedItem + PRIORITY_ITEM, txtPriority.getText(), 0, false);
			}
		});
		
		btnSetTargetTemp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				publishToTopic(COMMAND_TOPIC + "/item_" + selectedItem + TARGET_TEMPERATURE_ITEM, txtTargetTemp.getText(), 0, false);
			}
		});
		
		btnSetTempAdjust.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				publishToTopic(COMMAND_TOPIC + "/item_" + selectedItem + TEMPERATURE_ADJUST_ITEM, txtTempAdjust.getText(), 0, false);
			}
		});
		
		btnSetConsumption.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				publishToTopic(COMMAND_TOPIC + "/item_" + selectedItem + CONSUMPTION_ITEM, txtConsumption.getText(), 0, false);
			}
		});
		
		btnSetConsumptionLimit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				publishToTopic(COMMAND_TOPIC + "/" + CONSUMPTION_LIMIT_ITEM, txtConsumptionLimit.getText(), 0, false);
			}
		});
		
		btnSetHysteresis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				publishToTopic(COMMAND_TOPIC + "/" + HYSTERESIS_ITEM, txtHysteresis.getText(), 0, false);
			}
		});
		
		btnSetMassTargetTemp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtMassTemp.getText().isEmpty()) {
					return;
				}
				for(String addr : items.keySet()) {
					publishToTopic(COMMAND_TOPIC + "/item_" + addr + TARGET_TEMPERATURE_ITEM, txtMassTemp.getText(), 0, false);
				}
			}
		});
	}
	
	protected void disconnectFromMqttServer() {
		if (mqttClient != null && mqttClient.isConnected()) {
			try {
				mqttClient.disconnect();
				mqttClient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			mqttIsConnected = mqttClient.isConnected();
		}
	}

	class MyMqttCallback implements MqttCallback {

		@Override
		public void connectionLost(Throwable arg0) {
			System.out.println("Lost connection to mqtt server.");
			System.out.println(arg0.getMessage());
			arg0.printStackTrace(System.out);
			
		}

		@Override
		public void deliveryComplete(IMqttDeliveryToken arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void messageArrived(String topic, MqttMessage message) throws Exception {
			String msg = new String(message.getPayload());
			if (topic.equals(DEBUG_TOPIC)) {
				txtOutput.append(new String(DatatypeConverter.parseHexBinary(msg.substring(2))));
				return;
			}
			
			Pattern p;
			Matcher m;
			String addr = new String(), item = new String();
			HeaterItem heater = null;
			if (topic.contains("item_")) {
				p = Pattern.compile("/item_([0-9a-fA-F]{6})(_.*)");
				m = p.matcher(topic);
				if (m.find()) {
					if (!m.group(1).isEmpty()) {
						addr = m.group(1);
					}
					if (!m.group(2).isEmpty()) {
						item = m.group(2);
					}
				}
				heater = items.get(addr);
				if (heater == null) {
					System.out.println(topic);
					System.out.println("Unknown heater: " + addr);
					return;
				}
			} else {
				p = Pattern.compile("/((?:" + CONSUMPTION_LIMIT_ITEM + ")|(?:" + HYSTERESIS_ITEM + ")|(?:" + TOTAL_CONSUMPTION_ITEM + "))");
				m = p.matcher(topic);
				if (m.find()) {
					if (!m.group(1).isEmpty()) {
						item = m.group(1);
					}
				}
			}
				
			if (item.isEmpty()) {
				return;
			}
			
			float f;
			boolean b, changed = false;
			int i;
			String s;
			byte[] by;
			byte bt;
			short sh;
			switch (item) {
			case TEMP_ITEM:
				f = Float.parseFloat(msg);
				if (heater.temperature != f) {
					heater.temperature = f;
					changed = true;
				}
				break;
			case IS_ENABLED_ITEM:
				b = msg.equals("ON") ? true : false;
				if (heater.isEnabled != b) {
					heater.isEnabled = b;
					changed = true;
				}
				break;
			case IS_AUTO_ITEM:
				b = msg.equals("ON") ? true : false;
				if (heater.isAuto != b) {
					heater.isAuto = b;
					changed = true;
				}
				break;
			case IS_ON_ITEM:
				b = msg.equals("ON") ? true : false;
				if (heater.isOn != b) {
					heater.isOn = b;
					changed = true;
				}
				break;
			case PRIORITY_ITEM:
				i = Integer.parseInt(msg);
				if (heater.priority != i) {
					heater.priority = i;
					changed = true;
				}
				break;
			case TARGET_TEMPERATURE_ITEM:
				i = Integer.parseInt(msg);
				if (heater.targetTemperature != i) {
					heater.targetTemperature = i;
					changed = true;
				}
				break;
			case SENSOR_ITEM:
				by = DatatypeConverter.parseHexBinary(msg);
				if (!Arrays.equals(heater.address, by)) {
					heater.address = by;
					changed = true;
				}
				break;
			case PORT_ITEM:
				bt = Byte.parseByte(msg);
				if (heater.port != bt) {
					heater.port = bt;
					changed = true;
				}
				break;
			case TEMPERATURE_ADJUST_ITEM:
				f = Float.parseFloat(msg);
				if (heater.temperatureAdjust != f) {
					heater.temperatureAdjust = f;
					changed = true;
				}
				break;
			case CONSUMPTION_ITEM:
				sh = Short.parseShort(msg);
				if (heater.powerConsumption != sh) {
					heater.powerConsumption = sh;
					changed = true;
				}
				break;
			case TOTAL_CONSUMPTION_ITEM:
				i = Integer.parseInt(msg);
				if (totalConsumption != i) {
					totalConsumption = i;
					changed = true;
				}
				break;
			case CONSUMPTION_LIMIT_ITEM:
				i = Integer.parseInt(msg);
				if (consumptionLimit != i) {
					consumptionLimit = i;
					changed = true;
				}
				break;
			case HYSTERESIS_ITEM:
				f = Float.parseFloat(msg);
				if (hysteresis != f) {
					hysteresis = f;
					changed = true;
				}
				break;
			}
			
			if (changed) {
				updateHeater();
				changed = false;
			}
			updateOverview();
		}
		
	}

	private void publishToTopic(String topic, String message, int qos, boolean isRetained) {
		//System.out.printf("Publishing %s to topic %s", message, topic);
		MqttTopic mqttTopic = mqttClient.getTopic(topic);
		
		MqttMessage mqttMessage = new MqttMessage(message.getBytes());
		mqttMessage.setQos(qos);
		mqttMessage.setRetained(isRetained);
		
		MqttDeliveryToken mqttToken;
		
		try {
			mqttToken = mqttTopic.publish(mqttMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void updateOverview() {
		for (HeaterItem h : items.values()) {
			h.btnAuto.setSelected(h.isAuto);
			h.btnOn.setSelected(h.isOn);
			h.txtTemp.setText(String.valueOf(h.temperature));
		}		
	}

	private void updateHeater() {
		
		if (!started) {
			return;
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				HeaterItem heater = items.get(selectedItem);
				
				lblTotalConsumption.setText(String.valueOf(totalConsumption));
				lblTemperature.setText(String.valueOf(heater.temperature));
				
				chkIsEnabled.setSelected(heater.isEnabled);
				chkIsAuto.setSelected(heater.isAuto);
				chkIsOn.setSelected(heater.isOn);
				
				if (!txtSensorAddress.isFocusOwner())
					txtSensorAddress.setText(DatatypeConverter.printHexBinary(heater.address));
				if (!txtPort.isFocusOwner())
					txtPort.setText(String.valueOf(heater.port));
				if (!txtPriority.isFocusOwner())
					txtPriority.setText(String.valueOf(heater.priority));
				if (!txtTargetTemp.isFocusOwner())
					txtTargetTemp.setText(String.valueOf(heater.targetTemperature));
				if (!txtConsumption.isFocusOwner())
					txtConsumption.setText(String.valueOf(heater.powerConsumption));
				if (!txtTempAdjust.isFocusOwner())
					txtTempAdjust.setText(String.valueOf(heater.temperatureAdjust));
				
				if (!txtConsumptionLimit.isFocusOwner())
					txtConsumptionLimit.setText(String.valueOf(consumptionLimit));
				if (!txtHysteresis.isFocusOwner())
					txtHysteresis.setText(String.valueOf(hysteresis));
			}
		});
	}
}
