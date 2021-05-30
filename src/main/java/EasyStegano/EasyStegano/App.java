package EasyStegano.EasyStegano;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class App 
{
	// Main function
    public static void main( String[] args )
    {
    	// Open the main panel.
    	openMainPanel();
    }
    
    // Create a frame
    static JFrame frame = new JFrame();
    
    // Function to open the main panel of the program
    public static void openMainPanel() {
    	
    	// Create a button for navigating to the encoding tab.
    	JButton EncodeBTN = new JButton("Encode");
    	// Set the action listener for the encoding button.
    	EncodeBTN.addActionListener(new Encode());
    	// Create a button for navigating to the decoding tab.
    	JButton DecodeBTN = new JButton("Decode");
    	// Set the action listener for the decoding button.
    	DecodeBTN.addActionListener(new Decode());
    	
    	// Create a text element for the Title
    	JLabel title = new JLabel("EasyStegano - Boris NL");
    	
    	// Create a panel
    	JPanel panel = new JPanel();
    	// Set the panel border
    	panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
    	// Set the panel layout
    	panel.setLayout(new GridLayout(0, 1));
    	
    	// Add the title to the panel
    	panel.add(title);
    	// Add the encode button to the panel
    	panel.add(EncodeBTN);
    	// Add the decode button to the panel
    	panel.add(DecodeBTN);
    	
    	// Add the panel to the frame
    	frame.add(panel, BorderLayout.CENTER);
    	// Set the frame exit operation
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	// Set the frame title
    	frame.setTitle("EasyStegano");
    	// Set frame resiable to false
    	frame.setResizable(false);
    	// Pack the frame
    	frame.pack();
    	// Set the frame visible
    	frame.setVisible(true);
    }
    
    // Function to convert a string to a binary integer list.
    public static List<Integer> stringToBinary(String string) {
		byte[] bytes = string.getBytes();
		List<Integer> binary = new ArrayList<Integer>();
		  
		for (byte b : bytes)
		{
		   int val = b;
		   for (int i = 0; i < 8; i++)
		   {
		      binary.add((val & 128) == 0 ? 0 : 1);
		      val <<= 1;
		   }
		}
		
		return binary;
    }
    
    // Function to convert a List from integer bytes to a string
    public static String binaryToString(List<Integer> binary) {
    	// Define some variables.
    	int count = 0;
    	String text = "";
    	String binChar = "";
    	// While the binary array is greater than zero execute all the code below.
    	while(binary.size() > 0) {
    		// Add the first element of the integer list to the binChar variable
    		binChar += binary.remove(0);
    		// Add to the count
    		count++;
    		
    		// If the count is divisible by 8 run the code
    		if(count % 8 == 0) {
    			// Parse the binary code we have now to a letter
				int num = Integer.parseInt(binChar, 2);
				char letter = (char) num;
				// Add the letter to the text.
				text += letter;
				// Reset the current binary code to zero
				binChar = "";
    		}
    	}
    	// Return the final sentence / string
    	return text;
    }
    
    // Simple function to close the main menu.
    public static void closeMainPanel() {
    	// Disposes the main frame.
    	frame.dispose();
    }
    
    // Basic function that returs a file
    // The function prompts the user to pick a file from their computer
    public static File chooseFile() {
    	JFileChooser chooser = new JFileChooser();
    	// Create a filter for PNG or JPEG images
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
        // Set the filter to the file chooser
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(chooser);
        // Checks if the file is a PNG or JPEG
        if(returnVal == JFileChooser.APPROVE_OPTION) {
        	// Return the file
           return chooser.getSelectedFile();
        }
        // If the user selects a file that is not a PNG or JPEG
        // Open the main menu and return nothing
        openMainPanel();
		return null;
    }
}
