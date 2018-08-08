package UI;


import folders.comperator.ComparingFolders;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class FolderComparatorGui {
	private JButton btnOldVersion;
	private JButton btnNewVersion;
	private JButton btnDestination;
	private JButton btnNewButton_1;
	private JFrame frame;
	private JLabel lblNewLabel;
	private JLabel oldV;
	private JLabel newV;
	private JLabel destination;
	private JLabel statusLabel;
	private Map<String, String> paths;
	private boolean gotNulls = false;


	public Map<String, String> getPaths() {
		return paths;
	}
	public JLabel getStatusLabel() { return statusLabel;}
	public boolean getGotNulls() {
		return gotNulls;
	}
	/**
	 * Create the application.
	 */
	public FolderComparatorGui() {
		paths = new HashMap<String, String>();
		paths.put("Old version path", null);
		paths.put("New version path", null);
		paths.put("Destination path", null);
		initialize();
		init2();

	}

	public JFrame getFrame() {
		return frame;
	}

	private void init2() {

		eventGetOldVersionPath oldV = new eventGetOldVersionPath();
		btnOldVersion.addActionListener(oldV);
		eventGetNewVersionPath newV = new eventGetNewVersionPath();
		btnNewVersion.addActionListener(newV);

		eventGetDestinationPath destenation = new eventGetDestinationPath();
		btnDestination.addActionListener(destenation);

		eventStartComparing compareEvent = new eventStartComparing();
		btnNewButton_1.addActionListener(compareEvent);


	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.frame = new JFrame();
		this.frame.getContentPane().setBackground(new Color(242, 247, 253));
		this.frame.setBounds(100, 100, 600, 300);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setResizable(false);

		this.btnOldVersion = new JButton("Old Version");

		this.btnNewVersion = new JButton("New Version");

		this.btnDestination = new JButton("Destenation Path");

		this.lblNewLabel = new JLabel("");
		this.lblNewLabel.setIcon(new ImageIcon(FolderComparatorGui.class.getResource("/UI/matrix.jpg")));

		this.oldV = new JLabel("");

		this.newV = new JLabel("");

		this.destination = new JLabel("");

		this.btnNewButton_1 = new JButton("Start");

		ImageIcon loading = new ImageIcon("/UI/ajax-loader.gif");
		statusLabel = new JLabel("");
//		statusLabel.setIcon(new ImageIcon(FolderComparatorGui.class.getResource("/UI/ajax-loader.gif")));
		frame.getContentPane().add(statusLabel);

		JLabel lblByWmbteam = new JLabel("by WMB-Team v1.0");


		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup()
												.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
														.addComponent(lblNewLabel)
														.addGroup(groupLayout.createSequentialGroup()
																.addContainerGap()
																.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
																		.addComponent(btnDestination, GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
																		.addComponent(btnOldVersion, GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
																		.addComponent(btnNewVersion, GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE))))
												.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
														.addGroup(groupLayout.createSequentialGroup()
																.addGap(19)
																.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
																		.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
																				.addComponent(destination, GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
																				.addComponent(oldV, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE))
																		.addComponent(newV, GroupLayout.PREFERRED_SIZE, 355, GroupLayout.PREFERRED_SIZE)))
														.addGroup(groupLayout.createSequentialGroup()
																.addGap(66)
																.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)
																.addGap(18)
																.addComponent(statusLabel, GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE))))
										.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
												.addContainerGap(538, Short.MAX_VALUE)
												.addComponent(lblByWmbteam)))
								.addContainerGap())
		);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
								.addGap(0)
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(lblNewLabel)
										.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
										.addComponent(statusLabel, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(oldV, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
										.addComponent(btnOldVersion, GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(newV, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(btnNewVersion, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
								.addPreferredGap(ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(btnDestination, GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
										.addComponent(destination, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
								.addGap(8)
								.addComponent(lblByWmbteam)
								.addContainerGap())
		);
		frame.getContentPane().setLayout(groupLayout);
	}

	public class eventGetOldVersionPath implements ActionListener {
		public JFileChooser chooser;
		public String path;

		@Override
		public void actionPerformed(ActionEvent arg) {
			chooser = new JFileChooser();
			File workingDirectory = new File(System.getProperty("user.dir"));
			chooser.setCurrentDirectory(workingDirectory);
			chooser.setDialogTitle("Choose your directory");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (chooser.showOpenDialog(btnOldVersion) == JFileChooser.APPROVE_OPTION) {
				path = chooser.getSelectedFile().getAbsolutePath();
			}
			boolean exists = (new File(path).exists());
			if(exists) {
				oldV.setText(path);
				paths.put("Old version path", path);
			} else {
				paths.put("Old version path", null);
				oldV.setText("<html><font color='red'>You have to choose existing folder!</font></html>");
			}

		}
	}

	public class eventGetNewVersionPath implements ActionListener {
		public JFileChooser chooser;
		public String path;

		@Override
		public void actionPerformed(ActionEvent arg) {
			chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File("C:\\"));
			chooser.setDialogTitle("Choose your directory");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (chooser.showOpenDialog(btnNewVersion) == JFileChooser.APPROVE_OPTION) {
				path = chooser.getSelectedFile().getAbsolutePath();
			}
			boolean exists = (new File(path).exists());
			if(exists) {
				newV.setText(path);
				paths.put("New version path", path);
			} else {
				paths.put("New version path", null);
				newV.setText("<html><font color='red'>You have to choose existing folder!</font></html>");
			}


		}
	}

	public class eventGetDestinationPath implements ActionListener {
		public JFileChooser chooser;
		public String path;

		@Override
		public void actionPerformed(ActionEvent arg) {
			chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File("C:\\"));
			chooser.setDialogTitle("Choose your directory");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (chooser.showOpenDialog(btnDestination) == JFileChooser.APPROVE_OPTION) {
				path = chooser.getSelectedFile().getAbsolutePath();
			}
			String folderName;
			if ((path.substring(path.length() - 1, path.length()).equals("\\"))) {
				folderName = "Result";
			} else {
				folderName = "\\Result";
			}
			boolean exists = (new File(path).exists());
			if(exists) {
				destination.setText(path + folderName);
				paths.put("Destination path", path);
			} else {
				destination.setText("<html><font color='red'>You have to choose existing folder!</font></html>");
				paths.put("Destination path", null);
			}


		}
	}


	public class eventStartComparing implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg) {

			for (Map.Entry<String, String> path : paths.entrySet()) {

				if (path.getValue() == null) {

					switch (path.getKey()) {
						case "Old version path":
							oldV.setText("<html><font color='red'>You have to choose old version path!</font></html>");
							break;
						case "New version path":
							newV.setText("<html><font color='red'>You have to choose new version path!</font></html>");
							break;
						case "Destination path":
							destination.setText("<html><font color='red'>You have to choose Destination path!</font></html>");
							break;

					}

				}

			}
			if(!paths.values().contains(null)) {

				try {
					ComparingFolders cf = new ComparingFolders(oldV.getText(), newV.getText(), destination.getText());
					setOffLoading();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}



		public void setOffLoading(){
			statusLabel.setText("Finished");
			statusLabel.setIcon(new ImageIcon(FolderComparatorGui.class.getResource("/UI/done.png")));
			statusLabel.setVisible(true);

		}


	}
}
