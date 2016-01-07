package jrat.plugin.webcam.client;

import iconlib.IconUtils;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;

import jrat.api.Client;
import jrat.api.ui.BaseControlPanel;
import jrat.api.ui.RATMenuItemActionListener;

public class MenuListener implements RATMenuItemActionListener {

	@Override
	public void onClick(List<Client> servers) {
		try {
			if (servers.size() > 0) {
				final Client client = servers.get(0);
				BaseControlPanel panel = WebcamPlugin.entry.get(client);
						 
				if (panel == null) {
					panel = WebcamPlugin.entry.newPanelInstance(client);
					WebcamPlugin.entry.put(client, panel);
				}

				final BaseControlPanel finalPanel = panel;
				
				JFrame frame = new JFrame();
				frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent arg0) {
						WebcamPlugin.entry.remove(client);
						finalPanel.onClose();
					}
				});
				frame.setTitle("Webcam - " + client.getIP());
				frame.setSize(500, 350);
				frame.setLocationRelativeTo(null);
				frame.setIconImage(IconUtils.getIcon("icon", MenuListener.class).getImage());
				frame.setLocationRelativeTo(null);
				frame.add(panel);
				frame.setVisible(true);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
