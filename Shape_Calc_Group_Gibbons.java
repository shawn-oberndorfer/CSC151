import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.awt.geom.Path2D;

public class Shape_Calc_Group_Gibbons extends JFrame {

    private JPanel cards;
    private CardLayout cardLayout;

    private JPanel inputPanel;
    private Map<String, JTextField> inputs = new HashMap<>();
    private ShapeRenderPanel renderPanel;
    private ShapeRenderPanel compareRenderPanel;
    private Map<String, JTextField> compareInputs = new HashMap<>();
    private JPanel centerContainer;
    private CardLayout centerLayout;
    private JSplitPane splitPane;
    private JPanel singleWrapper;
    private JLabel resultLabel;
    private JLabel compareResultLabel;
    private JComboBox<String> compareComboBox;
    private JComboBox<String> compareMetricComboBox;
    private JComboBox<String> unitComboBox;
    private JTextField customUnitField;
    private JLabel compareSingleLabel;
    private JLabel compareSecondLabel;
    private String currentShape = "";
    private String previousMenu = "MAIN";
    private Double lastComputedVolume = null;
    private boolean lastIs2D = false;
    private Double lastComputedSurfaceArea = null;
    private Double lastComputedArea2D = null;

    public Shape_Calc_Group_Gibbons() {
        setTitle("Shape Calculator");
        setSize(1100, 760);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        getContentPane().add(cards);

        initMenu();
        init2DMenu();
        init3DMenu();

        cardLayout.show(cards, "MENU");
        setVisible(true);
    }

    // ------------------ Menus ------------------
    private void initMenu() {
        JPanel menuPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        JButton twoDButton = new JButton("2D Shapes");
        JButton threeDButton = new JButton("3D Shapes");
        JButton exitButton = new JButton("Exit");

        twoDButton.addActionListener(e -> { previousMenu = "MAIN"; cardLayout.show(cards, "2D"); });
        threeDButton.addActionListener(e -> { previousMenu = "MAIN"; cardLayout.show(cards, "3D"); });
        exitButton.addActionListener(e -> System.exit(0));

        menuPanel.add(twoDButton);
        menuPanel.add(threeDButton);
        menuPanel.add(exitButton);

        cards.add(menuPanel, "MENU");
    }

    private void init2DMenu() {
        JPanel twoDPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        String[] shapes = {"Rectangle", "Circle", "Ellipse", "Trapezoid", "Triangle"};
        for (String s : shapes) {
            JButton b = new JButton(s);
            b.addActionListener(e -> { currentShape = s; previousMenu = "2D"; showCalcScreen(); });
            twoDPanel.add(b);
        }
        JButton back = new JButton("Back");
        back.addActionListener(e -> cardLayout.show(cards, "MENU"));
        twoDPanel.add(back);
        cards.add(twoDPanel, "2D");
    }

    private void init3DMenu() {
        JPanel threeDPanel = new JPanel(new GridLayout(8, 1, 8, 8));
        String[] shapes = {"Tetrahedron", "Cube", "Octahedron", "Dodecahedron", "Icosahedron", "Sphere", "Cylinder"};
        for (String s : shapes) {
            JButton b = new JButton(s);
            b.addActionListener(e -> { currentShape = s; previousMenu = "3D"; showCalcScreen(); });
            threeDPanel.add(b);
        }
        JButton back = new JButton("Back");
        back.addActionListener(e -> cardLayout.show(cards, "MENU"));
        threeDPanel.add(back);
        cards.add(threeDPanel, "3D");
    }

    // ------------------ Calculation Screen ------------------
    private void showCalcScreen() {
        JPanel calcPanel = new JPanel(new BorderLayout(10, 10));
        inputs.clear();
        inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        switch (currentShape) {
            case "Rectangle":
                addField("Length"); addField("Width"); break;
            case "Triangle":
                addField("Length"); addField("Height"); break;
            case "Trapezoid":
                addField("Length"); addField("Length B"); addField("Height"); break;
            case "Circle":
            case "Sphere":
                addField("Radius"); break;
            case "Ellipse":
                addField("Major Axis"); addField("Minor Axis"); break;
            case "Cylinder":
                addField("Radius"); addField("Height"); break;
            case "Tetrahedron": case "Cube": case "Octahedron": case "Dodecahedron": case "Icosahedron":
                addField("Length"); break;
        }

        JPanel unitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        unitComboBox = new JComboBox<>(new String[]{"cm","m","in","ft","Custom"});
        customUnitField = new JTextField(8);
        customUnitField.setEnabled(false);
        unitComboBox.addActionListener(e -> customUnitField.setEnabled("Custom".equals(unitComboBox.getSelectedItem())));
        unitPanel.add(new JLabel("Units:"));
        unitPanel.add(unitComboBox);
        unitPanel.add(customUnitField);

        renderPanel = new ShapeRenderPanel(inputs);
        renderPanel.setPreferredSize(new Dimension(480, 350));
        renderPanel.setShape(currentShape);

        compareInputs = new HashMap<>();
        compareRenderPanel = new ShapeRenderPanel(compareInputs);
        compareRenderPanel.setPreferredSize(new Dimension(480, 350)); // equal width

        // Label panels
        compareSingleLabel = new JLabel("Shape 1: " + currentShape, SwingConstants.CENTER);
        compareSingleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 19));
        compareSecondLabel = new JLabel("Shape 2: (select)", SwingConstants.CENTER);
        compareSecondLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 19));

        // Pack left panel
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(compareSingleLabel, BorderLayout.NORTH);
        leftPanel.add(renderPanel, BorderLayout.CENTER);

        // Pack right panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(compareSecondLabel, BorderLayout.NORTH);
        rightPanel.add(compareRenderPanel, BorderLayout.CENTER);

        // Split pane
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setResizeWeight(0.5);

        // Center container always shows split pane (even if shape2 not chosen yet)
        centerContainer = new JPanel(new BorderLayout());
        centerContainer.add(splitPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(1,3,10,10));
        JButton calcButton = new JButton("Calculate");
        JButton cancelButton = new JButton("Cancel");
        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setForeground(Color.BLUE);
        resultLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        buttonPanel.add(calcButton); buttonPanel.add(cancelButton); buttonPanel.add(resultLabel);

        // --- Comparison Panel ---
        JPanel comparePanel = new JPanel(new BorderLayout());
        JPanel compareControls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        compareControls.add(new JLabel("Compare Shape 1 to Shape 2: "));
        compareComboBox = new JComboBox<>();
        compareMetricComboBox = new JComboBox<>(new String[]{"Volume","Surface Area"});
        refreshCompareOptions();
        compareControls.add(compareComboBox);
        compareControls.add(compareMetricComboBox);

        comparePanel.add(compareControls, BorderLayout.NORTH);

        // Second shape inputs panel
        JPanel compareInputPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        comparePanel.add(compareInputPanel, BorderLayout.CENTER);

        compareResultLabel = new JLabel(" ", SwingConstants.CENTER);
        compareResultLabel.setForeground(Color.MAGENTA);
        compareResultLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        comparePanel.add(compareResultLabel, BorderLayout.SOUTH);

        bottomPanel.add(unitPanel, BorderLayout.NORTH);
        bottomPanel.add(comparePanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        calcPanel.add(inputPanel, BorderLayout.NORTH);
        calcPanel.add(centerContainer, BorderLayout.CENTER);
        calcPanel.add(bottomPanel, BorderLayout.SOUTH);

        addInputListeners();

        calcButton.addActionListener(e -> calculate());
        cancelButton.addActionListener(e -> {
            resultLabel.setText("");
            compareResultLabel.setText("");
            lastComputedVolume = null;
            lastIs2D = false;
            if(previousMenu.equals("2D")) cardLayout.show(cards, "2D");
            else if(previousMenu.equals("3D")) cardLayout.show(cards, "3D");
            else cardLayout.show(cards, "MENU");
        });

        // --- Always render two panes, but update compare fields as soon as selected ---
        compareComboBox.addActionListener(e -> {
            compareInputs.clear();
            compareInputPanel.removeAll();
            String targetShape = (String)compareComboBox.getSelectedItem();
            compareSecondLabel.setText("Shape 2: " + (targetShape == null || "(select)".equals(targetShape) ? "(select)" : targetShape));
            compareRenderPanel.setShape(targetShape==null||"(select)".equals(targetShape)?"":targetShape);
            if(targetShape==null || "(select)".equals(targetShape)){
                compareResultLabel.setText("");
            } else {
                List<String> fields = new ArrayList<>();
                switch(targetShape){
                    case "Rectangle": fields.addAll(Arrays.asList("Length","Width")); break;
                    case "Triangle": fields.addAll(Arrays.asList("Length","Height")); break;
                    case "Trapezoid": fields.addAll(Arrays.asList("Length","Length B","Height")); break;
                    case "Circle": case "Sphere": fields.add("Radius"); break;
                    case "Ellipse": fields.addAll(Arrays.asList("Major Axis","Minor Axis")); break;
                    case "Cylinder": fields.addAll(Arrays.asList("Radius","Height")); break;
                    case "Tetrahedron": case "Cube": case "Octahedron": case "Dodecahedron": case "Icosahedron":
                        fields.add("Length"); break;
                    
                    case "Equilateral Triangle": fields.addAll(Arrays.asList("Length","Height")); break;
                }
                for(String f : fields){
                    JLabel l = new JLabel(f+":");
                    JTextField tf = new JTextField();
                    compareInputs.put(f, tf);
                    compareInputPanel.add(l);
                    compareInputPanel.add(tf);
                    tf.getDocument().addDocumentListener(new DocumentListener() {
                        public void insertUpdate(DocumentEvent e){ updateComparison(); }
                        public void removeUpdate(DocumentEvent e){ updateComparison(); }
                        public void changedUpdate(DocumentEvent e){ updateComparison(); }
                    });
                }
                compareInputPanel.revalidate();
                compareInputPanel.repaint();
            }
            updateComparison();
        });

        compareMetricComboBox.addActionListener(e -> updateComparison());

        // Always update comparison as soon as Shape 1 inputs change
        for(JTextField tf: inputs.values()){
            tf.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e){ renderPanel.repaint(); updateComparison(); }
                public void removeUpdate(DocumentEvent e){ renderPanel.repaint(); updateComparison(); }
                public void changedUpdate(DocumentEvent e){ renderPanel.repaint(); updateComparison(); }
            });
        }

        cards.add(calcPanel, "CALC");
        cardLayout.show(cards, "CALC");
        revalidate();
        repaint();

        // Initial partial shape2 render to match default compareComboBox state ("(select)")
        compareRenderPanel.setShape("");
        updateComparison();
    }


    private void addField(String label){
        JLabel lbl = new JLabel(label+":");
        JTextField tf = new JTextField();
        inputs.put(label, tf);
        inputPanel.add(lbl);
        inputPanel.add(tf);
    }

    private void addInputListeners(){
        for(JTextField tf: inputs.values()){
            tf.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e){ renderPanel.repaint(); updateComparison(); }
                public void removeUpdate(DocumentEvent e){ renderPanel.repaint(); updateComparison(); }
                public void changedUpdate(DocumentEvent e){ renderPanel.repaint(); updateComparison(); }
            });
        }
    }

    private boolean isCurrent2D(){
        return Arrays.asList("Rectangle","Circle","Ellipse","Trapezoid","Triangle").contains(currentShape);
    }

    private void refreshCompareOptions(){
        if(compareComboBox == null) return;
        compareComboBox.removeAllItems();
        if(isCurrent2D()){
            compareMetricComboBox.setVisible(false);
            compareComboBox.addItem("(select)");
            String[] shapes2D = { "Rectangle", "Circle", "Ellipse", "Trapezoid", "Triangle"};
            for(String s : shapes2D) compareComboBox.addItem(s);
        } else {
            compareMetricComboBox.setVisible(true);
            compareComboBox.addItem("(select)");
            for(String s : new String[]{"Tetrahedron","Cube","Octahedron","Dodecahedron","Icosahedron","Sphere","Cylinder"}){
                compareComboBox.addItem(s);
            }
        }
    }

    private double getVal(String k){
        JTextField tf = inputs.get(k);
        if(tf!=null && !tf.getText().isEmpty()) return Double.parseDouble(tf.getText());
        return 50;
    }
    private double getValFrom(Map<String, JTextField> map, String k){
        JTextField tf = map.get(k);
        if(tf!=null && !tf.getText().isEmpty())
            try{ return Double.parseDouble(tf.getText()); }catch(Exception e){}
        return 0;
    }

    private String getUnit(){
        if("Custom".equals(unitComboBox.getSelectedItem())) {
            String c = customUnitField.getText().trim();
            return c.isEmpty()?"units":c;
        }
        return (String) unitComboBox.getSelectedItem();
    }

    private void calculate() {
        try {
            double length = getVal("Length"), lengthB = getVal("Length B"), width = getVal("Width");
            double height = getVal("Height"), radius = getVal("Radius");
            double major = getVal("Major Axis"), minor = getVal("Minor Axis");

            double result=0; boolean is2D=false;
            Double surfaceArea = null;
            switch(currentShape){
                case "Rectangle": result=length*width; is2D=true; break;
                case "Circle": result=Math.PI*radius*radius; is2D=true; break;
                case "Ellipse": result=Math.PI*major*minor; is2D=true; break;
                case "Trapezoid": result=0.5*(length+lengthB)*height; is2D=true; break;
                case "Triangle": result=0.5*length*height; is2D=true; break;
                case "Tetrahedron":
                    result=Math.pow(length,3)/(6.0*Math.sqrt(2.0));
                    surfaceArea = Math.sqrt(3.0)*length*length;
                    break;
                case "Cube":
                    result=Math.pow(length,3);
                    surfaceArea = 6.0*length*length;
                    break;
                case "Octahedron":
                    result=(Math.sqrt(2.0)/3.0)*Math.pow(length,3);
                    surfaceArea = 2.0*Math.sqrt(3.0)*length*length;
                    break;
                case "Dodecahedron":
                    result=((15.0+7.0*Math.sqrt(5.0))/4.0)*Math.pow(length,3);
                    surfaceArea = 3.0*Math.sqrt(25.0+10.0*Math.sqrt(5.0))*length*length;
                    break;
                case "Icosahedron":
                    result=(5.0/12.0)*(3.0+Math.sqrt(5.0))*Math.pow(length,3);
                    surfaceArea = 5.0*Math.sqrt(3.0)*length*length;
                    break;
                case "Sphere":
                    result=(4.0/3.0)*Math.PI*Math.pow(radius,3);
                    surfaceArea = 4.0*Math.PI*radius*radius;
                    break;
                case "Cylinder":
                    result=Math.PI*Math.pow(radius,2)*height;
                    surfaceArea = 2.0*Math.PI*radius*(radius+height);
                    break;
            }
            String unitPower = is2D ? getUnit() + "\u00B2" : getUnit() + "\u00B3";
            resultLabel.setForeground(Color.BLUE);
            resultLabel.setText("<html><div style='font-size:12px'>Shape 1 ("+currentShape+") result: <b>" + new DecimalFormat("0.000").format(result)+" "+unitPower+"</b></div></html>");
            lastComputedVolume = is2D ? null : result;
            lastComputedSurfaceArea = is2D ? null : surfaceArea;
            lastComputedArea2D = is2D ? result : null;
            lastIs2D = is2D;
            updateComparison();
        } catch(Exception e){
            resultLabel.setForeground(Color.RED);
            resultLabel.setText("<html><div style='font-size:14px'>Invalid input</div></html>");
            compareResultLabel.setText("");
            lastComputedVolume = null;
            lastComputedSurfaceArea = null;
            lastComputedArea2D = null;
            lastIs2D = false;
        }
    }

    // --- Comparison calculation logic ---
    private void updateComparison() {
        Double val1 = calculateShape(currentShape, inputs);
        String target = compareRenderPanel.shape;
        Double val2 = calculateShape(target, compareInputs);
        String units = getUnit();
        String metric = compareMetricComboBox.isVisible() &&
            compareMetricComboBox.getSelectedItem()!=null ? compareMetricComboBox.getSelectedItem().toString() : "Volume";
        if(val1==null || target == null || target.isEmpty() || "(select)".equals(target)){
            compareResultLabel.setText("<html><div style='font-size:12px'>Select a second shape!</div></html>");
            compareSecondLabel.setText("Shape 2: (select)");
            compareRenderPanel.setShape("");
            return;
        }
        compareSecondLabel.setText("Shape 2: " + target);
        String shapeLabel1 = "Shape 1 ("+currentShape+")";
        String shapeLabel2 = "Shape 2 ("+target+")";
        boolean comparing2D = isCurrent2D();
        String unitPower = comparing2D ? getUnit() + "\u00B2" : getUnit() + "\u00B3";
        String shape1Text = shapeLabel1 + " Area: <b>" + (val1==null?"?":new DecimalFormat("0.###").format(val1))+" "+unitPower+"</b>";
        String shape2Text = shapeLabel2 + " Area: <b>" + (val2==null?"?":new DecimalFormat("0.###").format(val2))+" "+unitPower+"</b>";
        if (!comparing2D && compareMetricComboBox.isVisible() && metric.equals("Surface Area")) {
            // If comparing surface area for 3D, calculate accordingly
            Double s1 = calculateSurface(currentShape, inputs);
            Double s2 = calculateSurface(target, compareInputs);
            shape1Text = shapeLabel1 + " Surface Area: <b>" + (s1==null?"?":new DecimalFormat("0.###").format(s1))+" "+getUnit()+"\u00B2"+"</b>";
            shape2Text = shapeLabel2 + " Surface Area: <b>" + (s2==null?"?":new DecimalFormat("0.###").format(s2))+" "+getUnit()+"\u00B2"+"</b>";
            Double ratio = (s2==null||s2==0)?null:((s1==null)?null:(s1/s2));
            String ratioText = "Ratio ("+shapeLabel1 + " / " + shapeLabel2 + "): <b>" + ((ratio==null)?"∞":new DecimalFormat("0.###").format(ratio))+"</b>";
            compareResultLabel.setText("<html><br><div style='font-size:14px'>" +
                shape1Text+"<br>"+shape2Text+"<br>"+ratioText+"</div></html>");
        } else {
            Double ratio = (val2==null||val2==0)?null:((val1==null)?null:(val1/val2));
            String ratioText = "Ratio ("+shapeLabel1 + " / " + shapeLabel2 + "): <b>" + ((ratio==null)?"∞":new DecimalFormat("0.###").format(ratio))+"</b>";
            compareResultLabel.setText("<html><br><div style='font-size:14px'>" +
                shape1Text+"<br>"+shape2Text+"<br>"+ratioText+"</div></html>");
        }
        compareRenderPanel.repaint();
    }

    private Double calculateShape(String shape, Map<String,JTextField> fields){
        try{
            double length = getValFrom(fields, "Length"), lengthB = getValFrom(fields, "Length B"), width = getValFrom(fields, "Width");
            double height = getValFrom(fields, "Height"), radius = getValFrom(fields, "Radius");
            double major = getValFrom(fields, "Major Axis"), minor = getValFrom(fields, "Minor Axis");
            switch(shape){
                case "Rectangle": return length*width;
                case "Circle": return Math.PI*radius*radius;
                case "Ellipse": return Math.PI*major*minor;
                case "Trapezoid": return 0.5*(length+lengthB)*height;
                case "Triangle": return 0.5*length*height;
                
                case "Equilateral Triangle": return 0.5*length*height;
                case "Tetrahedron": return Math.pow(length,3)/(6.0*Math.sqrt(2.0));
                case "Cube": return Math.pow(length,3);
                case "Octahedron": return (Math.sqrt(2.0)/3.0)*Math.pow(length,3);
                case "Dodecahedron": return ((15.0+7.0*Math.sqrt(5.0))/4.0)*Math.pow(length,3);
                case "Icosahedron": return (5.0/12.0)*(3.0+Math.sqrt(5.0))*Math.pow(length,3);
                case "Sphere": return (4.0/3.0)*Math.PI*Math.pow(radius,3);
                case "Cylinder": return Math.PI*Math.pow(radius,2)*height;
            }
        }catch(Exception ex){ return null;}
        return null;
    }

    // Surface area calculation for comparison of 3D
    private Double calculateSurface(String shape, Map<String,JTextField> fields){
        try{
            double length = getValFrom(fields, "Length"), radius = getValFrom(fields, "Radius"), height = getValFrom(fields, "Height");
            switch(shape){
                case "Tetrahedron": return Math.sqrt(3.0)*length*length;
                case "Cube": return 6.0*length*length;
                case "Octahedron": return 2.0*Math.sqrt(3.0)*length*length;
                case "Dodecahedron": return 3.0*Math.sqrt(25.0+10.0*Math.sqrt(5.0))*length*length;
                case "Icosahedron": return 5.0*Math.sqrt(3.0)*length*length;
                case "Sphere": return 4.0*Math.PI*radius*radius;
                case "Cylinder": return 2.0*Math.PI*radius*(radius+height);
            }
        }catch(Exception ex){ return null;}
        return null;
    }

    // --- shape rendering class ---
    static class ShapeRenderPanel extends JPanel{
        private Map<String,JTextField> inputs;
        private String shape="";
        private Timer timer;
        private double angleX=0, angleY=0, angleZ=0;

        public ShapeRenderPanel(Map<String,JTextField> inputs){
            this.inputs=inputs;
            timer = new Timer(40, e -> {
                angleX += 0.02; angleY += 0.01; angleZ += 0.007;
                repaint();
            });
            timer.start();
        }
        public void setShape(String s){ shape=s; repaint(); }
        public String shape(){return shape;}

        private double getRenderVal(String key){
            if(Arrays.asList("Tetrahedron","Cube","Octahedron","Dodecahedron","Icosahedron","Sphere","Cylinder").contains(shape)){
                switch(key){
                    case "Length": return 100.0;
                    case "Radius": return 50.0;
                    case "Height": return 100.0;
                }
            }
            return getVal(key);
        }

        private double getVal(String key){
            JTextField tf = inputs.get(key);
            if(tf!=null && !tf.getText().isEmpty()){
                try { return Double.parseDouble(tf.getText()); } catch(Exception e){ return 50; }
            }
            return 50;
        }

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2=(Graphics2D)g;
            g2.setStroke(new BasicStroke(2));
            int w=getWidth(), h=getHeight();
            int cx=w/2, cy=h/2;

            g2.setColor(Color.BLACK);
            g2.drawString("Legend: rotating 3D shapes", 10, 20);

            switch(shape){
                case "Rectangle": drawRect(g2,w,h,20); break;
                case "Circle": drawCircle(g2,w,h,20); break;
                case "Ellipse": drawEllipse(g2,w,h,20); break;
                case "Triangle": drawTriangle(g2,w,h,20); break;
                case "Trapezoid": drawTrapezoid(g2,w,h,20); break;
                case "Cube": drawPolyhedron(g2,w,h,cx,cy,getCubeVertices(), getCubeFaces(), new Color[]{Color.RED,Color.GREEN,Color.BLUE,Color.ORANGE,Color.MAGENTA,Color.CYAN}); break;
                case "Tetrahedron": drawPolyhedron(g2,w,h,cx,cy,getTetraVertices(), getTetraFaces(), new Color[]{Color.RED,Color.GREEN,Color.BLUE,Color.ORANGE}); break;
                case "Octahedron": drawPolyhedron(g2,w,h,cx,cy,getOctaVertices(), getOctaFaces(), new Color[]{Color.RED,Color.GREEN,Color.BLUE,Color.ORANGE,Color.MAGENTA,Color.CYAN,Color.PINK,Color.YELLOW}); break;
                case "Dodecahedron": drawDodecahedron(g2,w,h,cx,cy); break;
                case "Icosahedron": drawPolyhedron(g2,w,h,cx,cy,getIcosaVertices(), getIcosaFaces(), new Color[]{Color.RED,Color.GREEN,Color.BLUE,Color.ORANGE,Color.MAGENTA,Color.CYAN}); break;
                case "Sphere": drawSphere(g2,w,h,cx,cy); break;
                case "Cylinder": drawCylinder(g2,w,h,cx,cy); break;
            }
        }

        private void drawRect(Graphics2D g,int w,int h,int m){
            double length=getVal("Length"), width=getVal("Width");
            int rw=(int)Math.min(length,w-2*m);
            int rh=(int)Math.min(width,h-2*m);
            g.setColor(Color.LIGHT_GRAY); g.fillRect(m,m,rw,rh);
            g.setColor(Color.BLACK); g.drawRect(m,m,rw,rh);
        }
        private void drawCircle(Graphics2D g,int w,int h,int m){
            double r=getVal("Radius");
            int rad=(int)Math.min(r,Math.min((w-2*m)/2,(h-2*m)/2));
            g.setColor(Color.LIGHT_GRAY); g.fillOval(w/2-rad,h/2-rad,2*rad,2*rad);
            g.setColor(Color.BLACK); g.drawOval(w/2-rad,h/2-rad,2*rad,2*rad);
        }
        private void drawEllipse(Graphics2D g,int w,int h,int m){
            double a=getVal("Major Axis"), b=getVal("Minor Axis");
            int rw=(int)Math.min(a,w-2*m), rh=(int)Math.min(b,h-2*m);
            g.setColor(Color.LIGHT_GRAY); g.fillOval((w-rw)/2,(h-rh)/2,rw,rh);
            g.setColor(Color.BLACK); g.drawOval((w-rw)/2,(h-rh)/2,rw,rh);
        }
        private void drawTriangle(Graphics2D g,int w,int h,int m){
            double l=getVal("Length"), ht=getVal("Height");
            int x1=m,y1=h-m, x2=(int)Math.min(m+l,w-m), y2=h-m, x3=x1+(int)(l/2), y3=(int)Math.max(m,h-ht);
            g.setColor(Color.LIGHT_GRAY); g.fillPolygon(new int[]{x1,x2,x3},new int[]{y1,y2,y3},3);
            g.setColor(Color.BLACK); g.drawPolygon(new int[]{x1,x2,x3},new int[]{y1,y2,y3},3);
        }
        private void drawTrapezoid(Graphics2D g,int w,int h,int m){
            double l=getVal("Length"), lb=getVal("Length B"), ht=getVal("Height");
            int x1=(w-(int)lb)/2, y1=h-m;
            int x2=x1+(int)lb, y2=y1;
            int x3=x1+(int)(lb-(lb-l)/2), y3=y1-(int)ht;
            int x4=x1+(int)((lb-l)/2), y4=y1-(int)ht;
            g.setColor(Color.LIGHT_GRAY); g.fillPolygon(new int[]{x1,x2,x3,x4}, new int[]{y1,y2,y3,y4},4);
            g.setColor(Color.BLACK); g.drawPolygon(new int[]{x1,x2,x3,x4}, new int[]{y1,y2,y3,y4},4);
        }

        private void drawDodecahedron(Graphics2D g, int panelW, int panelH, int cx, int cy){
            double phi = (1.0 + Math.sqrt(5.0)) / 2.0;
            double b = 1.0 / phi;
            double c = 1.0 / (phi * phi);
            double scaleModel = 90;
            double perspective = 600, zOffset = 500;

            double[][][] faces = new double[][][] {
                { { c, 0, 1}, {-c, 0, 1}, {-b, b, b}, {0, 1, c}, { b, b, b } },
                { {0, 1, c}, {0, 1, -c}, {-b, b, -b}, {-1, c, 0}, {-b, b, b } },
                { {-c, 0, 1}, { c, 0, 1}, { b, -b, b}, {0, -1, c}, {-b, -b, b } },
                { {0, -1, c}, {0, -1, -c}, { b, -b, -b}, {1, -c, 0}, { b, -b, b } },
                { { c, 0, -1}, {-c, 0, -1}, {-b, -b, -b}, {0, -1, -c}, { b, -b, -b } },
                { {0, -1, -c}, {0, -1, c}, {-b, -b, b}, {-1, -c, 0}, {-b, -b, -b } },
                { {-c, 0, -1}, { c, 0, -1}, { b, b, -b}, {0, 1, -c}, {-b, b, -b } },
                { {0, 1, -c}, {0, 1, c}, { b, b, b}, {1, c, 0}, { b, b, -b } },
                { {1, c, 0}, {1, -c, 0}, { b, -b, b}, { c, 0, 1}, { b, b, b } },
                { {1, -c, 0}, {1, c, 0}, { b, b, -b}, { c, 0, -1}, { b, -b, -b } },
                { {-1, c, 0}, {-1, -c, 0}, {-b, -b, -b}, {-c, 0, -1}, {-b, b, -b } },
                { {-1, -c, 0}, {-1, c, 0}, {-b, b, b}, {-c, 0, 1}, {-b, -b, b } }
            };

            Color[] faceColors = new Color[]{
                new Color(0xFF6B6B), new Color(0xFFA94D), new Color(0xFFD43B), new Color(0x9AE66E),
                new Color(0x4DDBFF), new Color(0x6C5CE7), new Color(0xFF9CEE), new Color(0x00C49A),
                new Color(0xE17055), new Color(0xF8EFBA), new Color(0x74B9FF), new Color(0xD3B6E8)
            };

            double sx = Math.sin(angleX), cxA = Math.cos(angleX);
            double sy = Math.sin(angleY), cyA = Math.cos(angleY);
            double sz = Math.sin(angleZ), cz = Math.cos(angleZ);

            class FR {
                int faceIndex; int[] x, y; double avgZ;
                FR(int faceIndex, int[] x, int[] y, double avgZ) {
                    this.faceIndex = faceIndex; this.x = x; this.y = y; this.avgZ = avgZ;
                }
            }
            java.util.List<FR> renderFaces = new java.util.ArrayList<>();

            for (int i = 0; i < faces.length; i++) {
                final int faceIdx = i;
                double[][] verts = faces[faceIdx];
                double[][] rotated = new double[5][3];
                double avgZ = 0;

                for (int v = 0; v < 5; v++) {
                    double x = verts[v][0] * scaleModel, y = verts[v][1] * scaleModel, z = verts[v][2] * scaleModel;
                    double y1 = y * cxA - z * sx;        double z1 = y * sx + z * cxA;
                    double x2 = x * cyA + z1 * sy;      double z2 = -x * sy + z1 * cyA;
                    double x3 = x2 * cz - y1 * sz;      double y3 = x2 * sz + y1 * cz;
                    rotated[v][0] = x3; rotated[v][1] = y3; rotated[v][2] = z2; avgZ += z2;
                }
                avgZ /= 5;

                double[] v0 = rotated[0], v1 = rotated[1], v2 = rotated[2];
                double[] edge1 = { v1[0]-v0[0], v1[1]-v0[1], v1[2]-v0[2] };
                double[] edge2 = { v2[0]-v0[0], v2[1]-v0[1], v2[2]-v0[2] };
                double[] normal = { edge1[1]*edge2[2]-edge1[2]*edge2[1], edge1[2]*edge2[0]-edge1[0]*edge2[2], edge1[0]*edge2[1]-edge1[1]*edge2[0]};
                double normLen = Math.sqrt(normal[0]*normal[0]+normal[1]*normal[1]+normal[2]*normal[2]);
                double nz = normal[2] / normLen;

                boolean facing = nz < 0;
                if(!facing) {
                    double[][] reversed = new double[5][3];
                    for(int v=0;v<5;v++) reversed[v]=rotated[4-v];
                    rotated=reversed;
                    avgZ = 0;
                    for(int v=0;v<5;v++) avgZ += rotated[v][2];
                    avgZ /= 5;
                }

                int[] px = new int[5], py = new int[5];
                for (int v = 0; v < 5; v++) {
                    double vz = rotated[v][2] + zOffset;
                    double proj = perspective / (perspective + vz);
                    double sx2 = rotated[v][0] * proj;
                    double sy2 = rotated[v][1] * proj;
                    px[v] = cx + (int)(sx2);
                    py[v] = cy - (int)(sy2);
                }
                renderFaces.add(new FR(faceIdx, px, py, avgZ));
            }

            renderFaces.sort(java.util.Comparator.comparingDouble(fr -> fr.avgZ));
            for (FR fr : renderFaces) {
                Path2D path = new Path2D.Double();
                path.moveTo(fr.x[0], fr.y[0]);
                for (int j = 1; j < fr.x.length; j++) path.lineTo(fr.x[j], fr.y[j]);
                path.closePath();

                g.setColor(faceColors[fr.faceIndex % faceColors.length]);
                g.fill(path);
                g.setColor(Color.BLACK);
                g.setStroke(new BasicStroke(1.5f));
                g.draw(path);
            }
        }

        // ... Other polyhedron shapes unchanged

        private void drawPolyhedron(Graphics2D g,int panelW,int panelH,int cx,int cy,double[][] verts,int[][] faces, Color[] faceColors){
            double[][] rot = new double[verts.length][3];
            for(int i=0;i<verts.length;i++){
                double x=verts[i][0], y=verts[i][1], z=verts[i][2];
                double x1 = x*Math.cos(angleY) - z*Math.sin(angleY);
                double z1 = x*Math.sin(angleY) + z*Math.cos(angleY);
                double y1 = y*Math.cos(angleX) - z1*Math.sin(angleX);
                double z2 = y*Math.sin(angleX) + z1*Math.cos(angleX);
                rot[i][0]=x1; rot[i][1]=y1; rot[i][2]=z2;
            }

            double target = 0.42 * Math.min(panelW, panelH);
            double maxXY = 1.0;
            for(double[] p : rot){
                double m = Math.max(Math.abs(p[0]), Math.abs(p[1]));
                if(m>maxXY) maxXY = m;
            }
            double scale = target / maxXY;

            class FaceDraw {
                int[] xs, ys;
                double depth;
                Color color;
            }
            List<FaceDraw> toDraw = new ArrayList<>();

            for(int f=0; f<faces.length; f++){
                int[] ordered = orderFaceCyclic(faces[f], rot);

                FaceDraw fd = new FaceDraw();
                fd.xs = new int[ordered.length];
                fd.ys = new int[ordered.length];

                double sumZ = 0.0;
                for(int i=0;i<ordered.length;i++){
                    double[] p = rot[ordered[i]];
                    fd.xs[i] = cx + (int)Math.round(p[0]*scale);
                    fd.ys[i] = cy - (int)Math.round(p[1]*scale);
                    sumZ += p[2];
                }
                fd.depth = sumZ / ordered.length;
                if(faceColors != null && faceColors.length > 0) {
                    fd.color = faceColors[f % faceColors.length];
                } else {
                    fd.color = Color.LIGHT_GRAY;
                }
                toDraw.add(fd);
            }

            toDraw.sort((fd1,fd2) -> Double.compare(fd1.depth, fd2.depth));

            for(FaceDraw fd : toDraw){
                g.setColor(fd.color);
                g.fillPolygon(fd.xs, fd.ys, fd.xs.length);
                g.setColor(Color.BLACK);
                g.drawPolygon(fd.xs, fd.ys, fd.xs.length);
            }
        }

        private int[] orderFaceCyclic(int[] faceIdx, double[][] rot){
            double cx=0, cy=0;
            for(int idx : faceIdx){
                cx += rot[idx][0];
                cy += rot[idx][1];
            }
            cx /= faceIdx.length;
            cy /= faceIdx.length;

            double[][] tmp = new double[faceIdx.length][2];
            for(int i=0;i<faceIdx.length;i++){
                int idx = faceIdx[i];
                double ax = rot[idx][0] - cx;
                double ay = rot[idx][1] - cy;
                double ang = Math.atan2(ay, ax);
                tmp[i][0] = ang;
                tmp[i][1] = idx;
            }
            Arrays.sort(tmp, Comparator.comparingDouble(angData -> angData[0]));

            int[] ordered = new int[faceIdx.length];
            for(int i=0;i<faceIdx.length;i++){
                ordered[i] = (int) tmp[i][1];
            }
            return ordered;
        }

        private void drawSphere(Graphics2D g, int panelW, int panelH, int cx, int cy) {
            double r = getRenderVal("Radius");
            double scale = 0.8 * Math.min(panelW, panelH) / (2 * r);
            int nLat = 20, nLong = 20;
            double[][] points = new double[nLat * nLong][3];
            int idx = 0;
            for (int i = 0; i < nLat; i++) {
                double theta = Math.PI * i / (nLat - 1);
                for (int j = 0; j < nLong; j++, idx++) {
                    double phi = 2 * Math.PI * j / nLong;
                    double x = r * Math.sin(theta) * Math.cos(phi);
                    double y = r * Math.sin(theta) * Math.sin(phi);
                    double z = r * Math.cos(theta);
                    points[idx] = rotate3D(x, y, z);
                }
            }
            g.setColor(Color.LIGHT_GRAY);
            for (int i = 0; i < nLat * nLong; i++) {
                int px = cx + (int)(points[i][0] * scale);
                int py = cy - (int)(points[i][1] * scale);
                int s = (int)(3 + 2 * (points[i][2]/r));
                g.fillOval(px-s/2, py-s/2, s, s);
            }
            g.setColor(Color.BLACK);
            g.drawOval(cx-(int)(r*scale), cy-(int)(r*scale), (int)(2*r*scale), (int)(2*r*scale));
        }

        private void drawCylinder(Graphics2D g, int panelW, int panelH, int cx, int cy) {
            double r = getRenderVal("Radius");
            double h = getRenderVal("Height");
            double scale = 0.6 * Math.min(panelW, panelH) / (2 * Math.max(r, h/2));
            double topY = h/2, botY = -h/2;
            int sides = 40;
            double[][] top = new double[sides][3], bot = new double[sides][3];
            for (int i = 0; i < sides; i++) {
                double theta = 2 * Math.PI * i / sides;
                double x = r * Math.cos(theta);
                double y = r * Math.sin(theta);
                top[i] = rotate3D(x, y, topY);
                bot[i] = rotate3D(x, y, botY);
            }
            g.setColor(new Color(200,200,255));
            // draw side lines
            for (int i = 0; i < sides; i++) {
                int px1 = cx + (int)(top[i][0]*scale), py1 = cy - (int)(top[i][1]*scale);
                int px2 = cx + (int)(bot[i][0]*scale), py2 = cy - (int)(bot[i][1]*scale);
                g.drawLine(px1, py1, px2, py2);
            }
            // draw top/bottom circles
            for (int i = 0; i < sides; i++) {
                int px1 = cx + (int)(top[i][0]*scale), py1 = cy - (int)(top[i][1]*scale);
                int px2 = cx + (int)(top[(i+1)%sides][0]*scale), py2 = cy - (int)(top[(i+1)%sides][1]*scale);
                g.drawLine(px1, py1, px2, py2);
            }
            for (int i = 0; i < sides; i++) {
                int px1 = cx + (int)(bot[i][0]*scale), py1 = cy - (int)(bot[i][1]*scale);
                int px2 = cx + (int)(bot[(i+1)%sides][0]*scale), py2 = cy - (int)(bot[(i+1)%sides][1]*scale);
                g.drawLine(px1, py1, px2, py2);
            }
        }

        // Helper rotation for 3D
        private double[] rotate3D(double x, double y, double z) {
            double x1 = x * Math.cos(angleY) - z * Math.sin(angleY);
            double z1 = x * Math.sin(angleY) + z * Math.cos(angleY);
            double y1 = y * Math.cos(angleX) - z1 * Math.sin(angleX);
            double z2 = y * Math.sin(angleX) + z1 * Math.cos(angleX);
            double x2 = x1 * Math.cos(angleZ) - y1 * Math.sin(angleZ);
            double y2 = x1 * Math.sin(angleZ) + y1 * Math.cos(angleZ);
            return new double[]{x2, y2, z2};
        }

        private double[][] getCubeVertices(){ double l=getRenderVal("Length")/2; return new double[][]{{-l,-l,-l},{l,-l,-l},{l,l,-l},{-l,l,-l},{-l,-l,l},{l,-l,l},{l,l,l},{-l,l,l}}; }
        private int[][] getCubeFaces(){ return new int[][]{{0,1,2,3},{4,5,6,7},{0,1,5,4},{2,3,7,6},{0,3,7,4},{1,2,6,5}}; }
        private double[][] getTetraVertices(){ double l=getRenderVal("Length"); double h=Math.sqrt((2.0/3.0)*l*l); return new double[][]{{0,0,0},{l,0,0},{l/2,h,0},{l/2,h/Math.sqrt(2.0),Math.sqrt(2.0)/Math.sqrt(3.0)*l}}; }
        private int[][] getTetraFaces(){ return new int[][]{{0,1,2},{0,1,3},{1,2,3},{2,0,3}}; }
        private double[][] getOctaVertices(){ double l=getRenderVal("Length")/2; return new double[][]{{l,0,0},{-l,0,0},{0,l,0},{0,-l,0},{0,0,l},{0,0,-l}}; }
        private int[][] getOctaFaces(){ return new int[][]{{0,2,4},{2,1,4},{1,3,4},{3,0,4},{0,2,5},{2,1,5},{1,3,5},{3,0,5}}; }
        private double[][] getIcosaVertices(){ double l=getRenderVal("Length")/2; double phi=(1+Math.sqrt(5))/2; return new double[][]{
            {-l,phi*l,0},{l,phi*l,0},{-l,-phi*l,0},{l,-phi*l,0},{0,-l,phi*l},{0,l,phi*l},{0,-l,-phi*l},{0,l,-phi*l},{phi*l,0,-l},{phi*l,0,l},{-phi*l,0,-l},{-phi*l,0,l}}; }
        private int[][] getIcosaFaces(){ return new int[][]{{0,11,5},{0,5,1},{0,1,7},{0,7,10},{0,10,11},{1,5,9},{5,11,4},{11,10,2},{10,7,6},{7,1,8},{3,9,4},{3,4,2},{3,2,6},{3,6,8},{3,8,9},{4,9,5},{2,4,11},{6,2,10},{8,6,7},{9,8,1}}; }
    }

    private JTextField tfOf(double v){
        JTextField tf = new JTextField();
        tf.setText(new DecimalFormat("0.###").format(v));
        return tf;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Shape_Calc_Group_Gibbons::new);
    }
}