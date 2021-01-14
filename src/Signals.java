import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Signals extends JFrame {

    String bits;
    String mode;

    private Signals(String bits, String mode) {

        this.bits = bits;
        this.mode = mode;

        setTitle("Electrical signals");

        JPanel left = new JPanel();
        left.setLayout(new GridLayout(3,1));
        left.add(new JLabel("nV"));
        left.add(new JLabel("0"));
        left.add(new JLabel("-nV"));

        left.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        add(left, BorderLayout.WEST);
        add(new SignalDrawing());

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private class SignalDrawing extends JPanel {

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1000, 400);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.GREEN);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setColor(Color.BLACK);
            drawSignal(g, bits);
        }

        private void drawSignal(Graphics g, String bits) {

            Graphics2D g2d = (Graphics2D) g.create();

            drawGrid(g2d, bits);

            switch (mode) {
                case "nrz" -> drawNrz(g2d, false);
                case "nrzi" -> drawNrz(g2d, true);
                case "manchester" -> drawManchester(g2d);
            }
        }

        void drawGrid(Graphics2D g2d, String bits) {

            g2d.drawLine(0, 0, 0, 400);

            //set the stroke of the copy, not the original
            Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2}, 0);
            g2d.setStroke(dashed);
            int xGap = 1000 / bits.length();
            int bit = 0;
            for(Character ignored : bits.toCharArray()) {
                g2d.drawLine(xGap * ++bit, 0, xGap * bit, 400);
            }
            g2d.drawLine(0, 200, 1000, 200);
        }

        void drawNrz(Graphics2D g2d, boolean inverted) {

            Character previous = null;
            g2d.setStroke(new BasicStroke(3));
            int xGap = 1000 / bits.length();
            int bit = 0;

            for(Character c : bits.toCharArray()) {
                if(previous != null && !previous.equals(c)) // Not the same bit
                    g2d.drawLine(xGap * bit, 50, xGap * bit, 350);
                int y = c == '0' && !inverted || c == '1' && inverted ? 350 : 50;
                g2d.drawString(c.toString(), xGap * bit + 10, 20);
                g2d.drawLine(xGap * bit, y, xGap * ++bit, y);

                previous = c;
            }

        }

        void drawManchester(Graphics2D g2d) {
            Character previous = null;
            g2d.setStroke(new BasicStroke(3));
            int xGap = 1000 / (bits.length() * 2);
            int bit = 0; // Will be incremented 2 times for each bit
            for(Character c : bits.toCharArray()) {
                g2d.drawString(c.toString(), xGap * bit + 10, 20); // Print the bit value
                if(previous != null && previous.equals(c)) {
                    g2d.drawLine(xGap * bit, 50, xGap * bit, 350);
                }
                int yLeft = c == '0' ? 350 : 50;
                int yRight = c == '0' ? 50 : 350;
                g2d.drawLine(xGap * bit++, yLeft, xGap * bit, yLeft);
                g2d.drawLine(xGap * bit, 50, xGap * bit, 350);
                g2d.drawLine(xGap * bit++, yRight, xGap * bit, yRight);
                previous = c;
            }
        }
    }

    private static class ChooseSignal extends JFrame implements ActionListener {

        JPanel pnl, bitsInputPnl, pnlChoix;
        JButton drawBtn;
        JTextField bitsInput;

        ButtonGroup choix;
        JRadioButton nrz, nrzi, manchester, manchesterDiff, miller;

        ChooseSignal() {

            setTitle("Choose the signal");
            setLayout(new FlowLayout());

            pnl = new JPanel();
            pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));

            drawBtn = new JButton("Draw the signal");
            drawBtn.addActionListener(this);

            bitsInputPnl = new JPanel();
            bitsInputPnl.add(new JLabel("Bits : "));
            bitsInput = new JTextField(20);
            bitsInputPnl.add(bitsInput);


            choix = new ButtonGroup();
            pnlChoix = new JPanel(new GridLayout(6, 1));

            // Create buttons
            nrz = new JRadioButton("NRZ"); nrz.setActionCommand("nrz");
            nrzi = new JRadioButton("NRZI"); nrzi.setActionCommand("nrzi");
            manchester = new JRadioButton("Manchester"); manchester.setActionCommand("manchester");
            manchesterDiff = new JRadioButton("Manchester Differentiel"); manchesterDiff.setActionCommand("manchesterDiff");
            miller = new JRadioButton("Miller"); miller.setActionCommand("miller");

            // Add buttons to the group
            choix.add(nrz);
            nrz.setSelected(true);
            choix.add(nrzi);
            choix.add(manchester);
            choix.add(manchesterDiff);
            choix.add(miller);

            // Add all buttons to the panel
            pnlChoix.add(new JLabel("Select representation type : "));
            pnlChoix.add(nrz);
            pnlChoix.add(nrzi);
            pnlChoix.add(manchester);
            pnlChoix.add(manchesterDiff);
            pnlChoix.add(miller);

            pnl.add(bitsInputPnl);
            pnl.add(pnlChoix);
            pnl.add(drawBtn);

            add(pnl);

            pack();
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setVisible(true);

        }

        @Override
        public void actionPerformed(ActionEvent e) {

            if(bitsInput.getText().matches("[01]+")) {
                new Signals(bitsInput.getText(), choix.getSelection().getActionCommand());
                this.dispose();
            }else {
                JOptionPane.showMessageDialog(null, "Invalid bits input", "Error", JOptionPane.INFORMATION_MESSAGE);
            }

        }
    }

    public static void main(String[] args) {
        new ChooseSignal();
    }
}
