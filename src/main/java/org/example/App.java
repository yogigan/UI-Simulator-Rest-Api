package org.example;

import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;
import kong.unirest.HttpResponse;
import kong.unirest.UnirestException;
import org.example.util.HmacUtil;
import org.example.util.RestApiUtil;

import javax.swing.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

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

    private static final String PANEL_TITLE = "UI Simulator Rest Api";
    private static final String DEFAULT_PING_IP = "http://10.131.17.5:8080";
    private static final String DEFAULT_PING_PATH_URL = "/Restfull5.5/ping";
    private static final String DEFAULT_PING_BODY = "{\n" +
            "    \"msisdn\": \"082285643282\",\n" +
            "    \"amount\": \"00000000\",\n" +
            "    \"trxDate\": \"2020-06-09T11: 16: 33+07: 00\",\n" +
            "    \"merchantTrxID\": \"0005502227972\"\n" +
            "}";
    private static final String DEFAULT_CHECK_PATH_URL = "/qr/check";
    private static final String DEFAULT_CHECK_BODY = "{\n" +
            "    \"pan\": \"0000000812341234\",\n" +
            "    \"processingCode\": \"360000\",\n" +
            "    \"transactionAmount\": 100000.00,\n" +
            "    \"transmission DateTime\": \"20220728031702\",\n" +
            "    \"system TraceAuditNumber\": \"000009\",\n" +
            "    \"localTransactionDateTime\": \"20220728101702\",\n" +
            "    \"settlementDate\": \"20220728\",\n" +
            "    \"captureDate\": \"20220728\",\n" +
            "    \"merchantType\": \"6017\",\n" +
            "    \"posEntryMode\": \"011\",\n" +
            "    \"feeType\": \"C\",\n" +
            "    \"feeAmount\": 1234.56,\n" +
            "    \"acquirerId\": \"00000008\",\n" +
            "    \"issuerld\": \"009\",\n" +
            "    \"forwardingId\": \"008\",\n" +
            "    \"rrn\": \"000009\",\n" +
            "    \"approvalCode\": \"121212\",\n" +
            "    \"terminalld\": \"123456\",\n" +
            "    \"merchantld\": \"MH111111\",\n" +
            "    \"merchantName\": \"Alfamart\",\n" +
            "    \"merchantCity\": \"Jakarta\",\n" +
            "    \"merchantCountry\": \"62\",\n" +
            "    \"productIndicator\": \"Q001\",\n" +
            "    \"customerData\": \"AAAAAAAAAA\",\n" +
            "    \"merchantCriteria\": \"UMI\",\n" +
            "    \"currencyCode\": \"360\",\n" +
            "    \"postalCode\": \"12270\",\n" +
            "    \"additionalField\": \"\",\n" +
            "    \"customerPan\": \"6666777788889999\"\n" +
            "}";

    public App() {

        initComponents();

        // onclick send button
        btnSend.addActionListener(e -> {

            // validate url
            String url = txtInputBaseUrl.getText() + txtInputPath.getText();
            if (url.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Input Invalid Url");
                throw new RuntimeException();
            }

            // create header
            Map<String, String> headers = new HashMap<>();
            if (!txtInputHeader.getText().isEmpty()) {
                headers.put(txtInputHeader.getText(), txtAreaHmac.getText());
            }

            // send request
            HttpResponse<String> httpResponse;
            try {
                httpResponse = RestApiUtil.call(comboBoxMethod.getSelectedItem().toString(), headers, url, txtAreaBodyRequest.getText());
            } catch (UnirestException exception) {
                JOptionPane.showMessageDialog(null, exception.getMessage());
                throw new RuntimeException();
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

        // onclick generate HMAC button
        btnGenHmac.addActionListener(e -> {
        });

        // on change combo box action
        comboBoxAction.addActionListener(e -> {
            if ("Check".equals(comboBoxAction.getSelectedItem())) {
                txtInputPath.setText(DEFAULT_CHECK_PATH_URL);
                txtAreaBodyRequest.setText(DEFAULT_CHECK_BODY);
            } else {
                txtInputPath.setText("");
                txtAreaBodyRequest.setText("");
            }
        });

        // onclick generate hmac button
        btnGenHmac.addActionListener(e -> {

            // validate input
            if (txtInputUser.getText().isEmpty() || txtInputPassword.getText().isEmpty() || txtInputInKey.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Invalid HMAC Input Paramaters");
                throw new RuntimeException();
            }

            // init variables
            String data = txtInputUser.getText() + ":" + txtInputPassword.getText();
            String algorithm = "HmacSHA256";
            String key = txtInputInKey.getText();
            String result;

            // hash input
            try {
                result = HmacUtil.hash(algorithm, data, key);
                txtAreaHmac.setText(result);
            } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
                JOptionPane.showMessageDialog(null, "Invalid HMAC Input Paramaters");
                throw new RuntimeException(ex);
            }

        });
    }

    public void initComponents() {
        // set combo box data
        txtAreaBodyRequest.setText(DEFAULT_PING_BODY);
        String[] actions = {"Payment", "Check", "Refund", "Inquiry", "Payment Debit"};
        ComboBoxModel<String> comboBoxActionModel = new DefaultComboBoxModel<>(actions);
        comboBoxAction.setModel(comboBoxActionModel);

        String[] methods = {"GET", "POST", "PUT", "DELETE"};
        ComboBoxModel<String> comboBoxMethodModel = new DefaultComboBoxModel<>(methods);
        comboBoxMethod.setModel(comboBoxMethodModel);

        // set default value
        txtInputBaseUrl.setText(DEFAULT_PING_IP);
        txtInputPath.setText(DEFAULT_PING_PATH_URL);

        // set main panel
        setContentPane(mainPanel);
        setTitle(PANEL_TITLE);
        setSize(1080, 720);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        App app = new App();
        app.setVisible(true);
    }

}
