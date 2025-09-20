/*
 * The program will find the area of a rectangle or a circle based off of the user's inputs.
 * Shawn Oberndorfer
 * Date Finished 9/20/2025
 */ 
import javax.swing.JOptionPane;

public class AreaRec_JOpt {
    public static void main(String[] args) {
        String shape = JOptionPane.showInputDialog("Which shape's area would you like to calculate? (rectangle or circle)");

        if (shape.equalsIgnoreCase("rectangle")) {
            // Rectangle area calculation
            String lengthInput = JOptionPane.showInputDialog("Enter the length of the rectangle:");
            double length = Double.parseDouble(lengthInput);

            String widthInput = JOptionPane.showInputDialog("Enter the width of the rectangle:");
            double width = Double.parseDouble(widthInput);

            double area = length * width;
            JOptionPane.showMessageDialog(null, "The area of the rectangle is: " + area);
        } else if (shape.equalsIgnoreCase("circle")) {
           
            String radiusInput = JOptionPane.showInputDialog("Enter the radius of the circle:");
            double radius = Double.parseDouble(radiusInput);

            double area = Math.PI * radius * radius;
            JOptionPane.showMessageDialog(null, "The area of the circle is: " + area);
        } else {
            // Invalid shape input
            JOptionPane.showMessageDialog(null, "Invalid shape. Please enter 'rectangle' or 'circle'.");
        }
    }
}