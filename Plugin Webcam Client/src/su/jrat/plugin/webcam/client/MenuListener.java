package su.jrat.plugin.webcam.client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import jrat.api.BaseControlPanel;
import jrat.api.RATMenuItemActionListener;
import jrat.api.RATObject;

public class MenuListener implements RATMenuItemActionListener {

	@Override
	public void onClick(List<RATObject> servers) {
		try {
			if (servers.size() > 0) {
				final RATObject server = servers.get(0);
				BaseControlPanel panel = null;
						 
				if (WebcamPlugin.entry.instances.containsKey(server.getIP())) {
					panel = WebcamPlugin.entry.instances.get(server.getIP());
				} else {
					panel = WebcamPlugin.entry.newPanelInstance(server);
					WebcamPlugin.entry.instances.put(server.getIP(), panel);
				}

				final BaseControlPanel finalPanel = panel;
				
				JFrame frame = new JFrame();
				frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent arg0) {
						WebcamPlugin.entry.instances.remove(server.getIP());
						finalPanel.onClose();
					}
				});
				frame.setTitle("Webcam - " + server.getIP());
				frame.setSize(500, 350);
				frame.setLocationRelativeTo(null);
				frame.setIconImage(new ImageIcon(WebcamPlugin.ICON_LOCATION).getImage());
				frame.setLocationRelativeTo(null);
				frame.add(panel);
				frame.setVisible(true);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
