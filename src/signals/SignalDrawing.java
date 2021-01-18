package signals;

import javax.swing.*;
import java.awt.*;

public class SignalDrawing extends JPanel {

    private Signals sig;

    /**
     * Constructeur de la classe qui récupère le Controleur Signals
     * @param sig Controleur de la classe Signals
     */
    SignalDrawing(Signals sig) {
        super();
        this.sig = sig;
    }

    /**
     * Modifie les dimensions de base du panel
     * @return Les dimensions voulues (1000x400)
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 400);
    }

    /**
     * Appelle la méthode drawSignal à chaque appel de paintComponent
     * @param g Le Graphics du panel
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawSignal(g, sig.bits);
    }

    /**
     * Méthode qui appelle les fonctions qui dessinent le signal en fonction du mode choisi ainsi que la grille
     * @param g Le Graphics du panel
     * @param bits La trame de bits à dessiner
     */
    private void drawSignal(Graphics g, String bits) {

        Graphics2D g2d = (Graphics2D) g.create();

        drawGrid(g2d);

        g2d.setStroke(new BasicStroke(3));

        switch (sig.mode) {
            case "nrz" -> drawNrz(g2d, false);
            case "nrzi" -> drawNrz(g2d, true);
            case "manchester" -> drawManchester(g2d);
            case "manchesterDiff" -> drawManchesterDiff(g2d);
            case "miller" -> drawMiller(g2d);
        }
    }

    /**
     * Méthode qui dessine une grille en pointillés qui sert de repère
     * @param g2d Le Graphics2D du panel
     */
    void drawGrid(Graphics2D g2d) {

        g2d.drawLine(0, 0, 0, 400);

        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2}, 0);
        g2d.setStroke(dashed);
        int xGap = 1000 / sig.bits.length();
        int bit = 0;
        for(Character ignored : sig.bits.toCharArray()) {
            g2d.drawLine(xGap * ++bit, 0, xGap * bit, 400);
        }
        g2d.drawLine(0, 200, 1000, 200);
    }

    /**
     * Méthode qui dessine le signal avec la représentation NRZ ou NRZI
     * @param g2d Le Graphics2D du panel
     * @param inverted True si la représentation est NRZI, false sinon
     */
    void drawNrz(Graphics2D g2d, boolean inverted) {

        sig.setTitle("Electric Signals - NRZ" + (inverted ? "i":""));

        Character previous = null;
        int xGap = 1000 / sig.bits.length();
        int bit = 0;

        for(Character c : sig.bits.toCharArray()) {
            if(previous != null && !previous.equals(c)) // Not the same bit
                g2d.drawLine(xGap * bit, 50, xGap * bit, 350);
            int y = c == '0' && !inverted || c == '1' && inverted ? 350 : 50;
            g2d.drawString(c.toString(), xGap * bit + 10, 20);
            g2d.drawLine(xGap * bit, y, xGap * ++bit, y);

            previous = c;
        }

    }

    /**
     * Méthode qui dessine le signal avec la représentation Manchester
     * @param g2d Le Graphics2D du panel
     */
    void drawManchester(Graphics2D g2d) {

        sig.setTitle("Electric Signals - Manchester");

        Character previous = null;
        int xGap = 1000 / (sig.bits.length() * 2);
        int bit = 0; // Will be incremented 2 times for each bit
        for(Character c : sig.bits.toCharArray()) {
            g2d.drawString(c.toString(), xGap * bit + 10, 20); // Print the bit value
            if(previous != null && previous.equals(c)) {
                g2d.drawLine(xGap * bit, 50, xGap * bit, 350);
            }
            int yLeft = c == '0' ? 350 : 50;
            int yRight = c == '0' ? 50 : 350;
            g2d.drawLine(xGap * bit++, yLeft, xGap * bit, yLeft);
            g2d.drawLine(xGap * bit, 50, xGap * bit, 350); // Transition in the middle
            g2d.drawLine(xGap * bit++, yRight, xGap * bit, yRight);
            previous = c;
        }
    }

    /**
     * Méthode qui dessine le signal avec la représentation Manchester différentielle
     * @param g2d Le Graphics2D du panel
     */
    void drawManchesterDiff(Graphics2D g2d) {

        sig.setTitle("Electric Signals - Manchester Differential");

        int previousY = 350;
        int xGap = 1000 / (sig.bits.length() * 2);
        int bit = 0; // Will be incremented 2 times for each bit
        for(Character c : sig.bits.toCharArray()) {
            g2d.drawString(c.toString(), xGap * bit + 10, 20); // Print the bit value
            int yLeft, yRight;
            if(c == '0') {
                g2d.drawLine(xGap * bit, 50, xGap * bit, 350); // Transition in the begining if the bit is 0
                yLeft = previousY == 350 ? 50 : 350; // The Y of the left part of the bit is the opposite of the previous Y
                yRight = previousY;
            }else {
                yLeft = previousY;
                yRight = previousY == 350 ? 50 : 350; // The Y of the right part of the bit is the opposite of the previous Y
            }
            g2d.drawLine(xGap * bit++, yLeft, xGap * bit, yLeft);
            g2d.drawLine(xGap * bit, 50, xGap * bit, 350); // Transition in the middle
            g2d.drawLine(xGap * bit++, yRight, xGap * bit, yRight);
            previousY = yRight;
        }
    }

    /**
     * Méthode qui dessine le signal avec la représentation Miller
     * @param g2d Le Graphics2D du panel
     */
    void drawMiller(Graphics2D g2d) {

        sig.setTitle("Electric Signals - Miller");

        Character previous = null;
        int previousY = 350;
        int xGap = 1000 / (sig.bits.length() * 2);
        int bit = 0; // Will be incremented 2 times for each bit
        for(Character c : sig.bits.toCharArray()) {
            g2d.drawString(c.toString(), xGap * bit + 10, 20); // Print the bit value
            if(c == '1') {
                g2d.drawLine(xGap * bit++, previousY, xGap * bit, previousY);
                g2d.drawLine(xGap * bit, 50, xGap * bit, 350);
                previousY = previousY == 50 ? 350 : 50; // Can be replaced by previousY = (previousY + 300) % 600
                g2d.drawLine(xGap * bit++, previousY, xGap * bit, previousY);
            }else {
                if(previous != null && previous == '0')
                    g2d.drawLine(xGap * bit, 50, xGap * bit, 350); // Transitions at the begining if the previous bit is also 0

                // Switches the Y if the previous bit is a 0
                previousY = previous != null && previous != '0' ? previousY : (previousY + 300)%600;
                // Draws the complete line at the right Y
                g2d.drawLine(xGap * bit, previousY, xGap * (bit+=2), previousY);
            }
            previous = c;
        }
    }
}
