package escalonador;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.nio.charset.MalformedInputException;
import java.awt.event.ActionEvent;

public class escalonadorMain extends JFrame {

    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    escalonadorMain frame = new escalonadorMain();
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.,o
     *
     */
    public escalonadorMain() {
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 270, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JButton btnStart = new JButton("Start");
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                Manager escalonador = new Manager();

                escalonador.readFile();

            }
        });
        btnStart.setBounds(37, 185, 198, 39);
        contentPane.add(btnStart);

        JLabel lblEscalonadorRoundrobin = new JLabel("Escalonador");
        lblEscalonadorRoundrobin.setFont(new Font("OCR A Extended", Font.BOLD, 26));
        lblEscalonadorRoundrobin.setBounds(10, 22, 195, 52);
        contentPane.add(lblEscalonadorRoundrobin);

        JLabel lblRoundrobin = new JLabel("Round-Robin");
        lblRoundrobin.setFont(new Font("OCR A Extended", Font.PLAIN, 26));
        lblRoundrobin.setBounds(75, 70, 185, 26);
        contentPane.add(lblRoundrobin);
    }
}
