package org.example;

import io.vertx.core.json.DecodeException;
import io.vertx.core.json.EncodeException;
import io.vertx.core.json.JsonObject;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;

public class App extends JFrame {
    private JPanel mainPanel;
    private JTextField txtInputBaseUrl;
    private JTextField txtInputPath;
    private JTextArea txtAreaBodyRequest;
    private JTextArea txtAreaResponse;
    private JTextField txtInputUser;
    private JTextField txtInputPassword;
    private JTextField txtInputInKey;
    private JTextField txtInputHeader;
    private JButton btnGenHmac;
    private JButton btnSend;
    private JButton btnReset;
    private JTextArea txtAreaHmac;
    private JComboBox comboBoxAction;
    private JComboBox comboBoxMethod;

    private static final String DEFAULT_BASE_URL = "http://10.131.17.5:8080";
    private static final String DEFAULT_PATH_URL = "/Restfull5.5/ping";
    private static final String DEFAULT_BODY = "{\n" +
            "    \"msisdn\": \"082285643282\",\n" +
            "    \"amount\": \"00000000\",\n" +
            "    \"trxDate\": \"2020-06-09T11: 16: 33+07: 00\",\n" +
            "    \"merchantTrxID\": \"0005502227972\"\n" +
            "}";

    public App() {

        initComponents();

        // onclick send button
        btnSend.addActionListener(e -> {

            // validate url
            String url = MessageFormat.format("{0}{1}", txtInputBaseUrl.getText(), txtInputPath.getText());
            if (url.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Invalid Url");
                throw new RuntimeException();
            }

            // send request
            HttpResponse<String> httpResponse;
            if ("POST".equals(comboBoxMethod.getSelectedItem().toString())) {
                httpResponse = Unirest.post(url)
                        .header("Content-Type", "application/json")
                        .body(txtAreaBodyRequest.getText())
                        .asString();
            } else if ("PUT".equals(comboBoxMethod.getSelectedItem().toString())) {
                httpResponse = Unirest.put(url)
                        .header("Content-Type", "application/json")
                        .body(txtAreaBodyRequest.getText())
                        .asString();
            } else if ("DELETE".equals(comboBoxMethod.getSelectedItem().toString())) {
                httpResponse = Unirest.delete(url).asString();
            } else {
                httpResponse = Unirest.get(url).asString();
            }

            // check response
            if (!httpResponse.isSuccess()) {
                JOptionPane.showMessageDialog(null, "Failed Send Request");
            } else {
                JOptionPane.showMessageDialog(null, "Success");
            }

            // show response
            try {
                JsonObject responseBody = new JsonObject(httpResponse.getBody());
                txtAreaResponse.setText(responseBody.encodePrettily());
            } catch (DecodeException exception) {
                txtAreaResponse.setText(httpResponse.getBody());
            }
        });

        // onclick reset button
        btnReset.addActionListener(e -> {
            txtInputBaseUrl.setText("");
            txtInputPath.setText("");
            txtAreaBodyRequest.setText("");
            txtAreaResponse.setText("");
            txtInputUser.setText("");
            txtInputPassword.setText("");
            txtInputInKey.setText("");
            txtInputHeader.setText("");
            txtAreaHmac.setText("");
        });
    }

    public static void main(String[] args) {
        App app = new App();
        app.setVisible(true);
    }

    public void initComponents() {
        // set combo box data
        String[] actions = {"Payment", "Check", "Refund", "Inquiry", "Payment Debit"};
        ComboBoxModel<String> comboBoxActionModel = new DefaultComboBoxModel<>(actions);
        comboBoxAction.setModel(comboBoxActionModel);

        String[] methods = {"GET", "POST", "PUT", "DELETE"};
        ComboBoxModel<String> comboBoxMethodModel = new DefaultComboBoxModel<>(methods);
        comboBoxMethod.setModel(comboBoxMethodModel);

        // set default value
        txtInputBaseUrl.setText(DEFAULT_BASE_URL);
        txtInputPath.setText(DEFAULT_PATH_URL);
        txtAreaBodyRequest.setText(DEFAULT_BODY);

        // set main panel
        setContentPane(mainPanel);
        setTitle("UI Simulator Rest Api");
        setSize(720, 480);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }


}
