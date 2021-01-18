package signals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChooseSignal extends JFrame implements ActionListener {

    JPanel pnl, bitsInputPnl, pnlChoix;
    JButton drawBtn;
    JTextField bitsInput;

    ButtonGroup choix;
    JRadioButton nrz, nrzi, manchester, manchesterDiff, miller;

    protected ChooseSignal() {

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

    ChooseSignal(String bits, String mode) {
        this();
        bitsInput.setText(bits);
        JRadioButton selected;
        switch (mode) {
            default -> selected = nrz;
            case "nrzi" -> selected = nrzi;
            case "manchester" -> selected = manchester;
            case "manchesterDiff" -> selected = manchesterDiff;
            case "Miller" -> selected = miller;
        }
        choix.setSelected(selected.getModel(), true);
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