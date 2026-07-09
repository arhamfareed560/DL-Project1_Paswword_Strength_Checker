import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasswordStrength implements ActionListener {

    private JFrame frame;
    private Container c;
    private JPanel innerBox; // We will use our custom RoundedPanel here
    private JLabel titleLabel, descLabel, resultLabel;
    private JTextField passwordField;
    private JButton submitButton;

    public PasswordStrength() {
        // Define Colors
        Color bgDark = new Color(2, 6, 23);
        Color surfaceDark = new Color(15, 23, 42);
        Color textLight = new Color(248, 250, 252);
        Color accentPurple = new Color(139, 92, 246);

        // Define Fonts
        Font heading1 = new Font("Roboto", Font.BOLD, 24);
        Font heading2 = new Font("Segoe UI", Font.BOLD, 21);
        Font bodyText = new Font("Segoe UI", Font.PLAIN, 14);
        Font buttonText = new Font("Segoe UI", Font.BOLD, 14);

        // Main Frame
        frame = new JFrame("Password Strength Checker");
        frame.setBounds(325, 150, 1000, 600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);

        c = frame.getContentPane();
        c.setBackground(bgDark);
        c.setLayout(null);

        // Main Title
        titleLabel = new JLabel("Welcome to Password Strength Checker", SwingConstants.CENTER);
        titleLabel.setFont(heading1);
        titleLabel.setForeground(textLight);
        titleLabel.setBounds(0, 60, 1000, 40);
        c.add(titleLabel);

        // --- 5. CREATE THE INNER BOX (Using our custom RoundedPanel) ---
        // CHANGED: Instantiating the custom class created at the bottom of this file
        // The '30' represents how round the corners should be.
        innerBox = new RoundedPanel(30);
        innerBox.setBackground(surfaceDark);
        innerBox.setBounds(200, 120, 600, 360);
        innerBox.setLayout(null);
        c.add(innerBox);

        //  ADD COMPONENTS TO THE INNER BOX
        descLabel = new JLabel("Enter password here:");
        descLabel.setFont(heading2);
        descLabel.setForeground(textLight);
        descLabel.setBounds(50, 40, 500, 30);
        innerBox.add(descLabel);

        // ADD PASSWORD FIELD
        passwordField = new JTextField();
        passwordField.setFont(bodyText);
        passwordField.setBounds(50, 90, 500, 45);
        passwordField.setBackground(bgDark);
        passwordField.setForeground(textLight);
        passwordField.setCaretColor(textLight);
        // Optional: Adding a border so the text field blends in nicely
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(94, 88, 85), 1));
        innerBox.add(passwordField);

        submitButton = new JButton("Check Strength");
        submitButton.setFont(buttonText);
        submitButton.setBounds(50, 180, 500, 45);
        submitButton.setBackground(accentPurple);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        innerBox.add(submitButton);

        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setFont(heading2);
        resultLabel.setForeground(textLight);
        resultLabel.setBounds(50, 260, 500, 40);
        innerBox.add(resultLabel);

        // --- 7. BUTTON CLICK EVENT ---
        submitButton.addActionListener(this);

        // 8. Show the frame
        frame.setVisible(true);
    }

    // Backend Method
    public String strengthChecker(String pass) {
        boolean hasCapital = false;
        boolean hasSmall = false;
        boolean hasDigit = false;
        boolean hasSymbol = false;
        String specialChar = "`~!@#$%^&*()_+-=[]{}|;':\",./<>?";

        // Check length first
        if (pass.length() < 8) {
            return "Invalid: Must be at least 8 characters";
        }

        // Loop through characters
        for (char c : pass.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasCapital = true;
            } else if (Character.isLowerCase(c)) {
                hasSmall = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (specialChar.contains(String.valueOf(c))) {
                hasSymbol = true;
            }
        }

        // Evaluate using your exact if/else structure
        if (hasDigit && hasSmall && hasCapital && hasSymbol) {
            return "Excellent: Unbreachable";
        } else if ((hasCapital && hasSmall && hasDigit) || (hasCapital && hasSmall && hasSymbol) || (hasCapital && hasDigit && hasSymbol) || (hasSymbol && hasDigit && hasSmall)) {
            return "Good: Can be better";
        } else if ((hasCapital && hasSmall) || (hasCapital && hasDigit) || (hasCapital && hasSymbol) || (hasSymbol && hasSmall) || (hasSmall && hasDigit) || (hasDigit && hasSymbol)) {
            return "Average";
        } else {
            return "Poor";
        }
    }

    // --- GUI CLICK ACTION ---
    @Override
    public void actionPerformed(ActionEvent e) {
        // CHANGED: Because we are using JTextField, we use getText() instead of getPassword()
        String pwd = passwordField.getText();

        if (pwd.isEmpty()) {
            resultLabel.setText("Please enter a password!");
            resultLabel.setForeground(new Color(239, 68, 68)); // Red
        } else {
            String resultText = strengthChecker(pwd);
            resultLabel.setText(resultText);

            // Change color dynamically
            if (resultText.contains("Invalid") || resultText.contains("Poor")) {
                resultLabel.setForeground(new Color(239, 68, 68)); // Red
            } else if (resultText.contains("Average")) {
                resultLabel.setForeground(new Color(245, 158, 11)); // Orange
            } else if (resultText.contains("Good")) {
                resultLabel.setForeground(new Color(59, 130, 246)); // Blue
            } else {
                resultLabel.setForeground(new Color(16, 185, 129)); // Green
            }
        }
    }

    // --- MAIN METHOD ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PasswordStrength();
            }
        });
    }

    // =========================================================================
    // NEW: CUSTOM ROUNDED PANEL CLASS
    // This allows us to paint a rounded background instead of a square one.
    // =========================================================================
    class RoundedPanel extends JPanel {
        private int cornerRadius;

        public RoundedPanel(int radius) {
            super();
            this.cornerRadius = radius;
            setOpaque(false); // Makes the square corners of the panel transparent
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();

            // Turns on anti-aliasing so the rounded corners look smooth, not pixelated
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Paint the background color
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

            g2.dispose();
        }
    }
}