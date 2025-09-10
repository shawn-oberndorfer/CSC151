/* 
Shawn Oberndorfer
CSC 151
Mod 2 Individual Project 
Date Started 9/10/2025
Date Last Edited 9/10/2025
 */


import javax.swing.JOptionPane;
public class AreaRec_JOpt 
{
    public static double length;
    public static double width;
    public static double area;

    public static double radius;
    public static double circleArea;

    public static void main(String[] args)
    {
        while (true) // Adding the while loop with break commands was AI.
        {    
        
        String choice = JOptionPane.showInputDialog("Which area would you like to calculate?\nType 'rectangle' or 'circle':");
        
        if (choice.equalsIgnoreCase("rectangle")) 
        {
            getLength();
            getWidth();
            displayArea();
            break;
        } else if (choice.equalsIgnoreCase("circle")) 
        {
            getRadius();
            displayCircleArea();
            break;
        } else 
        {
            JOptionPane.showMessageDialog(null, "Invalid choice please enter 'rectangle' or 'circle'");
        }
        }  
    }
        public static void getLength()
        {
            length = Double.parseDouble(JOptionPane.showInputDialog("Enter the length of the rectangle:"));
        } 
        public static void getWidth()
        {
            width = Double.parseDouble(JOptionPane.showInputDialog("Enter the width of the rectangle:"));
        }
        public static void displayArea()
        {
            area = length * width;
            JOptionPane.showMessageDialog(null,"Area of rectangle is " + area);
        }
        public static void getRadius()
        {
            radius = Double.parseDouble(JOptionPane.showInputDialog("Enter the radius of the circle:"));
        }
        public static void displayCircleArea()
        {
            circleArea = Math.PI * radius * radius; // The Math.PI was AI generated.
            JOptionPane.showMessageDialog(null,"Area of circle is " + circleArea); 
        }
}

