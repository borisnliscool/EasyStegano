package EasyStegano.EasyStegano;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Decode implements ActionListener{

	// Declare some variables
	JFrame passwordframe = new JFrame();
	JTextField password = new JTextField("", 10);
	File file;
	
	@Override
	// On action performed run this
	public void actionPerformed(ActionEvent e) {
		// Check the button's name
		// If the buttons name is Use Password decode the image
		if(((JButton) e.getSource()).getText().equals("Use Password")) {
			DecodeImage();
			closePasswordFrame();
		// Else make the user choose a file
		} else {
			ChooseFile();
		}
	}
	
	// Function that prompts the user to choose a file
	private void ChooseFile() {
		// Make the user chose a file
		file = App.chooseFile();
		// Check if the file returned is not null
		if( file == null) return;
		
		// Close the main menu
		App.closeMainPanel();
		
		// Create a text element with the text Password
		JLabel title = new JLabel("Password");
		
		// Create a new panel
    	JPanel panel = new JPanel();
    	// Set the panel border
    	panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
    	// Set the panel layout
    	panel.setLayout(new GridLayout(0, 1));

    	// Create a button
    	JButton button = new JButton("Use Password");
    	// Add an action to the button when pressed
    	button.addActionListener(this);
    	
    	// Add the title to the panel
    	panel.add(title);
    	// Add the text input to the panel
    	panel.add(password);
    	// Add the button to the panel
    	panel.add(button);
    	
    	// Add the panel to the frame
    	passwordframe.add(panel, BorderLayout.CENTER);
    	// Set the close operation
    	passwordframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	// Set the frame title
    	passwordframe.setTitle("EasyStegano");
    	// Set the frame not be able to resize
    	passwordframe.setResizable(false);
    	// Pack the frame
    	passwordframe.pack();
    	// Set the frame to visible
    	passwordframe.setVisible(true);
	}
	
	// Basic function to close the password frame.
	private void closePasswordFrame() {
		// Disposes the password frame
		passwordframe.dispose();
	}
	
	// Main function for decoding the image
	private void DecodeImage() {
		// Declare variable
		List<Integer> binary = getBinaryFromImage();
		
		// Temporary variable that turns the binary variable to a string
		String tmp = App.binaryToString(binary);
		// Get the encrypted data from the temporary string
		String encryptedData = tmp.substring(5, tmp.length());
		// Decrypt the encrypted data
		String decryptedData = Encryption.decrypt(encryptedData, password.getText());
		
		// Show a dialog box with the message
		JOptionPane.showMessageDialog(null, "Message: " + decryptedData);
		// Open the main panel.
		App.openMainPanel();
	}
	
	// Function to get a integer list with binary in it
	private List<Integer> getBinaryFromImage() {
		// Declare variables
		BufferedImage img = null;
		List<Integer> binary = new ArrayList<Integer>();
		
		// Try read the image
		try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			System.out.println(e);
		}

		// Get the data length
		int dataLength = getDataLength(img);
		
		// Get and set the image data
		int width = img.getWidth();
		int height = img.getHeight();
		// Create a count variable
		int count = 0;

		// Loop over all horizontal pixels
		for (int y = 0; y < height; y++) {
			// Loop over all vertical pixels
			for (int x = 0; x < width; x++) {
				// Check if the data length is shorter than the current count
				if(count > (dataLength + 5) * 8) break;
				// Get the pixel data
				int p = img.getRGB(x, y);

			    // Get the red
			    int r = (p>>16) & 0xff;

			    // Get the green
			    int g = (p>>8) & 0xff;

			    // Get the blue
			    int b = p & 0xff;
			    
			    // Add the values to the binary list
		    	binary.add(r % 2);
		    	binary.add(g % 2);
		    	binary.add(b % 2);
		    	
		    	// Add to the count
			    count += 3;
			}
			// Check if the data length is shorter than the current count
			if(count > (dataLength + 5) * 8) break;
		}
		// Return the binary
		return binary;
	}
	
	// Function for getting the first five characters in an image
	private int getDataLength(BufferedImage img) {
		// Declare new integer list
		List<Integer> binary = new ArrayList<Integer>();
		
		// Loop over the first fifteen pixels
		for (int x = 0; x < 15; x++) {
			// Get the pixel data
			int p = img.getRGB(x, 0);

		    // Get the red
		    int r = (p>>16) & 0xff;

		    // Get the green
		    int g = (p>>8) & 0xff;

		    // Get the blue
		    int b = p & 0xff;

		    // Add the values to the binary list
	    	binary.add(r % 2);
	    	binary.add(g % 2);
	    	binary.add(b % 2);
		}
		// Get the int of the length of the first 5 characters
		int length = Integer.parseInt(App.binaryToString(binary));
		// Return the length
		return length;
	}

}
