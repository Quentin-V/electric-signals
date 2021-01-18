package signals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Signals extends JFrame implements ActionListener {

    String bits;
    String mode;
    JPanel left;
    JButton back;

    protected Signals(String bits, String mode) {

        this.bits = bits;
        this.mode = mode;

        setTitle("Electrical signals");

        left = new JPanel();
        left.setLayout(new GridLayout(3,1));
        left.add(new JLabel("nV"));
        left.add(new JLabel("0"));
        left.add(new JLabel("-nV"));

        left.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        back = new JButton("Back");
        back.addActionListener(this);

        add(left, BorderLayout.WEST);
        add(new SignalDrawing(this));
        add(back, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
        new ChooseSignal(this.bits, this.mode);
    }

    public static void main(String[] args) {
        new ChooseSignal();
    }
}
