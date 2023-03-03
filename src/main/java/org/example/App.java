package org.example;

import javax.swing.*;

public class App extends JFrame {
    private JPanel jPanel;
    private JTextField txtInputBaseUrl;
    private JTextField txtInputPath;
    private JTextArea txtAreaBodyRequest;
    private JTextArea txtAreaResponse;
    private JTextField txtInputUser;
    private JTextField txtInputPassword;
    private JTextField txtInputInKey;
    private JTextField txtInputHeader;
    private JTextArea txtAreaHmac;
    private JButton btnGenHmac;
    private JButton btnSend;
    private JButton btnReset;
    private JList listAction;

    public App() {
        setContentPane(jPanel);
        setTitle("UI Simulator Rest Api");
        setSize(720, 480);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        App app = new App();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
