package EasyStegano.EasyStegano;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Encode implements ActionListener{
	
	// Create variables
	BufferedImage normalizedFile;
	JFrame passwordframe = new JFrame();
	JFrame textFrame;
	String filePath;
	
	JTextField password = new JTextField("", 10);
	JTextField text = new JTextField("", 20);
	
	@Override
	// On action performed run this
	public void actionPerformed(ActionEvent e) {
		// Check the button's name
		// If the buttons name is Encode prompt user to Choose a file and password
		if(((JButton) e.getSource()).getText().equals("Encode")) {
			ChooseFileAndPassword();
		// If the buttons name is Set Password activate the second step
		} else if(((JButton) e.getSource()).getText().equals("Set Password")) {
			ChooseMessage();
		// If the buttons name is Create Image do the final steps
		} else if(((JButton) e.getSource()).getText().equals("Create Image")) {
			EncodeToImage();
		}
	}
	
	// Function for choosing a file and password for encryption
	private void ChooseFileAndPassword() {
		// Get the file the user wants to use
		File file = App.chooseFile();
		// If there's no file returned exit out of the function
		if( file == null) return;
		
		// Set the filePath variable to the chosen file's path
		filePath = file.getAbsolutePath();
		// Normalise the file and set the normalizedFile variable to the output
		normalizedFile = NormalizePicture(file);
		// Close the main menu gui
		App.closeMainPanel();
		
		// Create the title of the new panel
		JLabel title = new JLabel("Password");
		
		// Create the new panel
    	JPanel panel = new JPanel();
    	// Set the panel's border
    	panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
    	// Set the panel's layout
    	panel.setLayout(new GridLayout(0, 1));

    	// Create a new button
    	JButton button = new JButton("Set Password");
    	// Set the action to this class
    	button.addActionListener(this);
    	
    	// Add the title to the panel
    	panel.add(title);
    	// Add the password text field
    	panel.add(password);
    	// Add the confirmation button
    	panel.add(button);
    	
    	// Add the panel to the frame
    	passwordframe.add(panel, BorderLayout.CENTER);
    	// Set the close operation
    	passwordframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	// Set the frame title
    	passwordframe.setTitle("EasyStegano");
    	// Set the frame to not be resizable
    	passwordframe.setResizable(false);
    	// Pack the frame
    	passwordframe.pack();
    	// Make the frame visible
    	passwordframe.setVisible(true);
	}
	
	// Simple function for closing the password frame.
	private void closePasswordFrame() {
		passwordframe.dispose();
	}
	
	// Function for normalising an image.
	private BufferedImage NormalizePicture(File file) {
		BufferedImage img = null;
		
		// Try read the file as an image
		try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			// Catch the exception and log it
			System.out.println(e);
		}
		
		// Define width and height
		int width = img.getWidth();
		int height = img.getHeight();
		
		// Loop over all horizontal pixels
		for (int x = 0; x < width; x++) {
			// Loop over all vertical pixels
			for (int y = 0; y < height; y++) {
				// Get the pixel data for the specific pixel
				int p = img.getRGB(x, y);
				
				// Get the alpha
			    int a = (p>>24) & 0xff;

				// Get the red
			    int r = (p>>16) & 0xff;

				// Get the green
			    int g = (p>>8) & 0xff;

				// Get the blue
			    int b = p & 0xff;
			    
			    // If the alpha is not divisible by two remove one so it makes it even
			    if ( a % 2 != 0 ) a--;
			    // If the red is not divisible by two remove one so it makes it even
			    if ( r % 2 != 0 ) r--;
			    // If the green is not divisible by two remove one so it makes it even
			    if ( g % 2 != 0 ) g--;
			    // If the blue is not divisible by two remove one so it makes it even
			    if ( b % 2 != 0 ) b--;
			    
			    // Write the pixel data
			    p = (a<<24) | (r<<16) | (g<<8) | b;
			    
			    // Set the pixel on the image
			    img.setRGB(x, y, p);
			}
		}
		
		// Return the image
		return img;
	}
	
	// Function for executing the second step
	private void ChooseMessage() {
		// Close the password frame
		closePasswordFrame();
		// Create a new frame
		textFrame = new JFrame();

		// Create a text element with the text Set Text
		JLabel title = new JLabel("Set Text");

		// Create a new button with the text Create Image
    	JButton button = new JButton("Create Image");
    	// Set the handler for the button
    	button.addActionListener(this);
		
    	// Create a new panel
    	JPanel panel = new JPanel();
    	// Set the panel border
    	panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
    	// Set the panel layout
    	panel.setLayout(new GridLayout(0, 1));
    	
    	// Add the title to the panel
    	panel.add(title);
    	// Add the text input to the panel
    	panel.add(text);
    	// Add the button to the panel
    	panel.add(button);
    	
    	// Add the panel to the frame
    	textFrame.add(panel, BorderLayout.CENTER);
    	// Set the close operation
    	textFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	// Set the frame title
    	textFrame.setTitle("EasyStegano");
    	// Set the frame not be able to resize
    	textFrame.setResizable(false);
    	// Pack the frame
    	textFrame.pack();
    	// Set the frame to visible
    	textFrame.setVisible(true);
	}
	
	// Method for getting the final binary list
	private List<Integer> getFinalBinaryList() {
		// Encrypt the text give.
		String encrypted = Encryption.encrypt(text.getText(), password.getText());
		// Declare variable.
		String length = "";
		// Make the length of this variable always 5 characters long
		length = String.format("%05d", encrypted.length());
		// Add them together
		encrypted = length + encrypted;
		// Return the binary of this variable.
		return App.stringToBinary(encrypted);
	}
	
	// Method for encoding to the image
	private void EncodeToImage() {
		// Get the final binary list
		List<Integer> binary = getFinalBinaryList();
		// Set the img variable
		BufferedImage img = normalizedFile;
		
		// Get and set the image data
		int width = img.getWidth();
		int height = img.getHeight();
		// Create a count variable
		int count = 0;

		// Loop over all horizontal pixels
		for (int y = 0; y < height; y++) {
			// Loop over all vertical pixels
			for (int x = 0; x < width; x++) {
				// Get the pixel data
				int p = img.getRGB(x, y);
				
				// Get the Alpha
			    int a = (p>>24) & 0xff;

			    // Get the red
			    int r = (p>>16) & 0xff;

			    // Get the green
			    int g = (p>>8) & 0xff;

			    // Get the blue
			    int b = p & 0xff;
			    
			    // Write the binary to the image
			    if(count < binary.size()) r += binary.get(count++);
			    if(count < binary.size()) g += binary.get(count++);
			    if(count < binary.size()) b += binary.get(count++);
			    
			    // Set pixel to new values
			    p = (a<<24) | (r<<16) | (g<<8) | b;
			    // Set the pixel
			    img.setRGB(x, y, p);
			}
		}
		
		// Write the final output image to the disk
	    try {
	    	// Get the file path of the input file and add "- output.png" to the end of it
	    	File f = new File(filePath + " - output.png");
	    	ImageIO.write(img, "png", f);
	    } catch(IOException e){
	    	System.out.println(e);
	    }
		
	    // Open the main panel
	    App.openMainPanel();
	    // Close this current panel
	    textFrame.dispose();
	}
}
