package gui;

import controller.Controller;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Pannello per l'autenticazione e registrazione degli utenti.
 */
public class LoginPanel extends JPanel {
    
    private final Controller controller;
    private final MainFrame mainFrame;
    
    // Login components
    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    
    // Register components
    private JTextField registerLoginField;
    private JPasswordField registerPasswordField;
    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField emailField;
    private JComboBox<String> ruoloComboBox;
    private JButton confirmRegisterButton;
    private JButton backToLoginButton;
    
    // Panel switching
    private JPanel loginPanel;
    private JPanel registerPanel;
    private CardLayout cardLayout;
    
    /**
     * Costruttore che inizializza il pannello di login
     *
     * @param controller il controller dell'applicazione
     * @param mainFrame  il frame principale
     */
    public LoginPanel(Controller controller, MainFrame mainFrame) {
        this.controller = controller;
        this.mainFrame = mainFrame;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Inizializza i componenti del pannello
     */
    private void initializeComponents() {
        // Login components
        loginField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        registerButton = new JButton("Registrati");
        
        // Register components
        registerLoginField = new JTextField(20);
        registerPasswordField = new JPasswordField(20);
        nomeField = new JTextField(20);
        cognomeField = new JTextField(20);
        emailField = new JTextField(20);
        ruoloComboBox = new JComboBox<>(new String[]{"PARTECIPANTE", "GIUDICE", "ORGANIZZATORE"});
        confirmRegisterButton = new JButton("Conferma Registrazione");
        backToLoginButton = new JButton("Torna al Login");
        
        // Card layout for switching between login and register
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        
        loginPanel = createLoginPanel();
        registerPanel = createRegisterPanel();
        
        add(loginPanel, "LOGIN");
        add(registerPanel, "REGISTER");
        
        cardLayout.show(this, "LOGIN");
    }
    
    /**
     * Crea il pannello di login
     *
     * @return il pannello di login
     */
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Login"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("Hackathon Manager", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Login field
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Login:"), gbc);
        
        gbc.gridx = 1;
        panel.add(loginField, gbc);
        
        // Password field
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        panel.add(passwordField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    /**
     * Crea il pannello di registrazione
     *
     * @return il pannello di registrazione
     */
    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Registrazione"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("Registrazione Nuovo Utente", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Login field
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Login:"), gbc);
        
        gbc.gridx = 1;
        panel.add(registerLoginField, gbc);
        
        // Password field
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        panel.add(registerPasswordField, gbc);
        
        // Nome field
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Nome:"), gbc);
        
        gbc.gridx = 1;
        panel.add(nomeField, gbc);
        
        // Cognome field
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Cognome:"), gbc);
        
        gbc.gridx = 1;
        panel.add(cognomeField, gbc);
        
        // Email field
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        panel.add(emailField, gbc);
        
        // Ruolo field
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Ruolo:"), gbc);
        
        gbc.gridx = 1;
        panel.add(ruoloComboBox, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(confirmRegisterButton);
        buttonPanel.add(backToLoginButton);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    /**
     * Configura il layout del pannello
     */
    private void setupLayout() {
        setPreferredSize(new Dimension(400, 300));
    }
    
    /**
     * Configura gli event handler
     */
    private void setupEventHandlers() {
        // Login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        // Register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(LoginPanel.this, "REGISTER");
            }
        });
        
        // Confirm register button
        confirmRegisterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegister();
            }
        });
        
        // Back to login button
        backToLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(LoginPanel.this, "LOGIN");
                clearRegisterFields();
            }
        });
        
        // Enter key on password field for login
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }
    
    /**
     * Gestisce il tentativo di login
     */
    private void handleLogin() {
        String login = loginField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (login.isEmpty() || password.isEmpty()) {
            mainFrame.showError("Inserisci login e password");
            return;
        }
        
        if (controller.login(login, password)) {
            mainFrame.handleLogin();
            clearLoginFields();
        } else {
            mainFrame.showError("Login o password non corretti");
            passwordField.setText("");
        }
    }
    
    /**
     * Gestisce il tentativo di registrazione
     */
    private void handleRegister() {
        String login = registerLoginField.getText().trim();
        String password = new String(registerPasswordField.getPassword());
        String nome = nomeField.getText().trim();
        String cognome = cognomeField.getText().trim();
        String email = emailField.getText().trim();
        String ruolo = (String) ruoloComboBox.getSelectedItem();
        
        // Validazione
        if (login.isEmpty() || password.isEmpty() || nome.isEmpty() || cognome.isEmpty() || email.isEmpty()) {
            mainFrame.showError("Tutti i campi sono obbligatori");
            return;
        }
        
        if (password.length() < 6) {
            mainFrame.showError("La password deve essere di almeno 6 caratteri");
            return;
        }
        
        if (!email.contains("@")) {
            mainFrame.showError("Inserisci un'email valida");
            return;
        }
        
        if (controller.registraUtente(login, password, nome, cognome, email, ruolo)) {
            mainFrame.showInfo("Registrazione completata con successo! Ora puoi effettuare il login.");
            cardLayout.show(this, "LOGIN");
            clearRegisterFields();
        } else {
            mainFrame.showError("Errore durante la registrazione. Login o email già utilizzati.");
        }
    }
    
    /**
     * Pulisce i campi del form di login
     */
    private void clearLoginFields() {
        loginField.setText("");
        passwordField.setText("");
    }
    
    /**
     * Pulisce i campi del form di registrazione
     */
    private void clearRegisterFields() {
        registerLoginField.setText("");
        registerPasswordField.setText("");
        nomeField.setText("");
        cognomeField.setText("");
        emailField.setText("");
        ruoloComboBox.setSelectedIndex(0);
    }
} 