import javax.swing.JOptionPane;
import java.util.ArrayList;

public class ShapeCalc {
    //Variables
    static double length;
    static double lengthB; 
    static double width; 
    static double height;
    static double depth;
    static double area;
    static double radius;
    static double majorAxis;
    static double minorAxis;
    static String shapeName; 
    static boolean isValid = false;
    static boolean continueLoop = true;
    static boolean exitProgram = false;
    // AI Generated Lines 20-21
    static ArrayList<String> shapeNames = new ArrayList<>();
    static ArrayList<Double> shapeAreas = new ArrayList<>();

     public static void main(String[] args) {
        while (continueLoop == true) { //This is the main program loop
            while(isValid == false){ // This loop is used if a wrong number is entered.
                // This is where you select what dimension of shapes you would like to calculate
                String dimensionInput = JOptionPane.showInputDialog("What type of shapes would you like to calculate?\n 1.) 2D Shapes \n 2.) 3D Shapes \n 3.) Exit");
                int dimension = Integer.parseInt(dimensionInput);

                switch (dimension){
                    case 1:
                        shapes2D();
                        isValid = true;
                        break;
                    case 2:
                        shapes3D();
                        isValid = true;
                        break;
                    case 3:
                        JOptionPane.showMessageDialog(null, "Exiting shape calculator.");
                        isValid = true;
                        exitProgram = true;
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid shape.");
                        isValid = false;

                }
            }
            if(exitProgram == false){
                isValid = false;// This resets the while loops attached to each shape menu.
                
                int confirm = JOptionPane.showConfirmDialog(null, "Would you like to calculate another shape?");

                if (confirm != JOptionPane.YES_OPTION) {
                    continueLoop = false;
                    // AI Generated Lines 59-71
                    //This displays all calculated areas if you hit no or cancel when prompted if you would like to claculate any other shapes
                    StringBuilder summary = new StringBuilder("Shape Comparison Summary:\n");
                    for (int i = 0; i < shapeNames.size(); i++) {
                        summary.append((i + 1) + ". " + shapeNames.get(i) + " → Area: " + shapeAreas.get(i) + "\n");
                    }
                    JOptionPane.showMessageDialog(null, summary.toString());
                }
            }else{ // This displays all calculated areas if you hit exit on a shapes menu.
                    StringBuilder summary = new StringBuilder("Shape Comparison Summary:\n");
                    for (int i = 0; i < shapeNames.size(); i++) {
                        summary.append((i + 1) + ". " + shapeNames.get(i) + " → Area: " + shapeAreas.get(i) + "\n");
                    }
                    JOptionPane.showMessageDialog(null, summary.toString());
                    break;
                }
        }
    }
    // Function to select which 3D shape will be calculated.
    public static void shapes3D(){
        while(isValid == false){
                String shapeInput = JOptionPane.showInputDialog("Which shape's area would you like to calculate?\n 1.) Tetrahedron \n 2.) Cube \n 3.) Octohedron \n 4.) Dodecahedron \n 5.) Icosahedron \n 6.) Sphere \n 7.) Cylinder \n 8.) Exit");
                int shape = Integer.parseInt(shapeInput);

                switch (shape) {
                    case 1: 
                        shapeName = "Tetrahedron";
                        tetrahedron();   
                        isValid = true;
                        break;
                    case 2:
                        shapeName = "Cube";
                        cube();
                        isValid = true;
                        break;
                    case 3:
                        shapeName = "Octahedron";
                        octohedron();
                        isValid = true;
                        break;
                    case 4:
                        shapeName = "Dodecahedron";
                        dodecahedron();
                        isValid = true;
                        break;
                    case 5:
                        shapeName = "Icosahedron";
                        icosahedron();
                        isValid = true;
                        break;
                    case 6:
                        shapeName = "Sphere";
                        sphere();
                        isValid = true;
                        break;
                    case 7:
                     shapeName = "Cylinder";
                        cylinder();
                        isValid = true;
                        break;
                    case 8: // This case is used to exit the program
                        JOptionPane.showMessageDialog(null, "Exiting shape calculator.");
                        isValid = true;
                        exitProgram = true;
                        break;
                    default: // I used this to loop the program incase that an invalid number is entered.
                        JOptionPane.showMessageDialog(null, "Invalid shape.");
                        isValid = false;
                         
            }
        } 

    }
    // Function to select which 2D shape will be calculated.
    public static void shapes2D(){
        while(isValid == false){
                String shapeInput = JOptionPane.showInputDialog("Which shape's area would you like to calculate?\n 1.) Rectangle \n 2.) Circle \n 3.) Ellipse \n 4.) Trapezoid \n 5.) Triangle \n 6.) Exit");
                int shape = Integer.parseInt(shapeInput);

                switch (shape) {
                    case 1: 
                        shapeName = "Rectangle";
                        rectangle();   
                        isValid = true;
                        break;
                    case 2:
                        shapeName = "Circle";
                        circle();
                        isValid = true;
                        break;
                    case 3:
                        shapeName = "Ellipse";
                        ellipse();
                        isValid = true;
                        break;
                    case 4:
                        shapeName = "Trapezoid";
                        trapezoid();
                        isValid = true;
                        break;
                    case 5:
                        shapeName = "Triangle";
                        triangle();
                        isValid = true;
                        break;
                    case 6: // This case is used to exit the program
                        JOptionPane.showMessageDialog(null, "Exiting shape calculator.");
                        isValid = true;
                        exitProgram = true;
                        break;

                    default: // I used this to loop the program incase that an invalid number is entered.
                        JOptionPane.showMessageDialog(null, "Invalid shape.");
                        isValid = false;
                }
            }
    }
    //Functions for all calculations and user inputs for each shape.
    public static void rectangle(){
        getLength();
        getWidth();
        getRecArea();
    }
    public static void circle(){
        getRadius();
        getCircArea();
    }
    public static void ellipse(){
        getMajorAxis();
        getMinorAxis();
        getEllipseArea();
    }
    public static void trapezoid(){
        length = getLength();
        lengthB = getLength();
        getHeight();
        getTrapezoidArea();
    }
    public static void triangle(){
        getLength();
        getHeight();
        getTriangleArea();
    }
    public static void tetrahedron(){
        getLength();
        getTetrahedronVolume();
    }
    public static void cube(){
        getLength();
        getCubeVolume();
    }
    public static void octohedron(){
        getLength();
        getOctohedronVolume();
    }
    public static void dodecahedron(){
        getLength();
        getDodecahedronVolume();
    }
    public static void icosahedron(){
        getLength();
        getIcosahedronVolume();
    }
    public static void sphere(){
        getRadius();
        getSphereVolume();
    }
    public static void cylinder(){
        getRadius();
        getHeight();
        getCylinderVolume();
    }
    //Functions to prompt a user to enter a length, width, etc... 
    public static double getLength(){
        String lengthInput = JOptionPane.showInputDialog("Enter the length of the " + shapeName + ":");
        length = Double.parseDouble(lengthInput);
        return length;
    }
    public static double getWidth(){
        String widthInput = JOptionPane.showInputDialog("Enter the width of the " + shapeName + ":");
            width = Double.parseDouble(widthInput);
            return width;
    }
    public static double getHeight(){
        String heightInput = JOptionPane.showInputDialog("Enter the height of the " + shapeName + ":");
        height = Double.parseDouble(heightInput);
        return height;
    }
    public static double getDepth(){
        String depthInput = JOptionPane.showInputDialog("Enter the depth of the " + shapeName + ":");
        depth = Double.parseDouble(depthInput);
        return height;
    }
    public static double getRadius(){
        String radiusInput = JOptionPane.showInputDialog("Enter the radius of the " + shapeName + ":");
        radius = Double.parseDouble(radiusInput);
        return radius;
    }
    public static double getMajorAxis(){
        String majorAxisInput = JOptionPane.showInputDialog("Enter the major axis of the " + shapeName + ":");
        majorAxis = Double.parseDouble(majorAxisInput);
        return majorAxis;
    }
    public static double getMinorAxis(){
        String minorAxisInput = JOptionPane.showInputDialog("Enter the minor axis of the " + shapeName + ":");
        minorAxis = Double.parseDouble(minorAxisInput);
        return minorAxis;
    }
    // The shapNames.add(shapeName) and shapeAreas.add(area) in each of these get area functions was AI.
    //Functions to calculate the area of each shape.
    public static void getRecArea(){
        double area = length * width;
        shapeNames.add(shapeName);
        shapeAreas.add(area);
        JOptionPane.showMessageDialog(null, "The area of the rectangle is: " + area);
    }
    public static void getCircArea(){
        area = Math.PI * Math.pow(radius, 2);
        shapeNames.add(shapeName);
        shapeAreas.add(area);
        JOptionPane.showMessageDialog(null, "The area of the circle is: " + area);
    }
    public static void getEllipseArea(){
        double area = majorAxis * minorAxis * Math.PI;
        shapeNames.add(shapeName);
        shapeAreas.add(area);
        JOptionPane.showMessageDialog(null, "The area of the ellipse is: " + area);
    }
    public static void getTrapezoidArea(){
        double area = 0.5 * (length + lengthB) * height;
        shapeNames.add(shapeName);
        shapeAreas.add(area);
        JOptionPane.showMessageDialog(null, "The area of the trapezoid is: " + area);
    }
    public static void getTriangleArea(){
        area = 0.5 * length * height;
        shapeNames.add(shapeName);
        shapeAreas.add(area);
        JOptionPane.showMessageDialog(null, "The area of the triangle is: " + area);
    }
    public static void getTetrahedronVolume(){
        area = Math.sqrt(2.0/12.0) * Math.pow(length, 3);
        shapeNames.add(shapeName);
        shapeAreas.add(area);
        JOptionPane.showMessageDialog(null, "The volume of the tetrahedron is: " + area);
    }
    public static void getCubeVolume(){
        area = Math.pow(length, 3);
        shapeNames.add(shapeName);
        shapeAreas.add(area);
        JOptionPane.showMessageDialog(null, "The volume of the cube is: " + area);
    }
    public static void getOctohedronVolume(){
        area = 2 * (Math.pow(length, 3) * Math.sqrt(2.0/3.0));
        shapeNames.add(shapeName);
        shapeAreas.add(area);
        JOptionPane.showMessageDialog(null, "The volume of the octohedron is: " + area);
    }
    public static void getDodecahedronVolume(){
        area = 7.66 * Math.pow(length, 3);
        shapeNames.add(shapeName);
        shapeAreas.add(area);
        JOptionPane.showMessageDialog(null, "The volume of the dodecahedron is: " + area);
    }
    public static void getIcosahedronVolume(){
        area = (5.0/12.0) * (3 + Math.sqrt(5.0) * Math.pow(length, 3));
        shapeNames.add(shapeName);
        shapeAreas.add(area);
        JOptionPane.showMessageDialog(null, "The volume of the Icosahedron is: " + area);
    }
    public static void getSphereVolume(){
        area = (4/3) * Math.PI * Math.pow(radius, 3);
        shapeNames.add(shapeName);
        shapeAreas.add(area);
        JOptionPane.showMessageDialog(null, "The volume of the sphere is: " + area);
    }
    public static void getCylinderVolume(){
        area = Math.PI * Math.pow(radius, 2) * height;
        shapeNames.add(shapeName);
        shapeAreas.add(area);
        JOptionPane.showMessageDialog(null, "The volume of the cylinder is: " + area);
    }
}