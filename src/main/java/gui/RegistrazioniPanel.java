package gui;

import controller.Controller;
import model.Registrazione;

import javax.swing.*;
import java.awt.*;

/**
 * Pannello per la gestione delle registrazioni.
 */
public class RegistrazioniPanel extends JPanel {
    
    private final Controller controller;
    private final MainFrame mainFrame;
    
    // Components
    private DefaultListModel<Registrazione> registrazioniListModel;
    private JList<Registrazione> registrazioniList;
    private JButton registraUtenteButton;
    private JButton gestisciRegistrazioniButton;
    
    /**
     * Costruttore che inizializza il pannello registrazioni
     *
     * @param controller il controller dell'applicazione
     * @param mainFrame  il frame principale
     */
    public RegistrazioniPanel(Controller controller, MainFrame mainFrame) {
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
        // List model for registrations
        registrazioniListModel = new DefaultListModel<>();
        registrazioniList = new JList<>(registrazioniListModel);
        registrazioniList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Buttons
        registraUtenteButton = new JButton("Registra Utente");
        gestisciRegistrazioniButton = new JButton("Gestisci Registrazioni");
        
        // Initially disable buttons that require selection
        gestisciRegistrazioniButton.setEnabled(false);
    }
    
    /**
     * Configura il layout del pannello
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Title
        JLabel titleLabel = new JLabel("Gestione Registrazioni", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);
        
        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        
        // List panel
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder("Registrazioni"));
        
        JScrollPane scrollPane = new JScrollPane(registrazioniList);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        listPanel.add(scrollPane, BorderLayout.CENTER);
        
        contentPanel.add(listPanel, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        buttonsPanel.setBorder(BorderFactory.createTitledBorder("Azioni"));
        
        buttonsPanel.add(registraUtenteButton);
        buttonsPanel.add(gestisciRegistrazioniButton);
        
        contentPanel.add(buttonsPanel, BorderLayout.EAST);
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Configura gli event handler
     */
    private void setupEventHandlers() {
        // Register user button
        registraUtenteButton.addActionListener(e -> showRegistraUtenteDialog());
        
        // List selection listener
        registrazioniList.addListSelectionListener(e -> {
            boolean hasSelection = !registrazioniList.isSelectionEmpty();
            gestisciRegistrazioniButton.setEnabled(hasSelection);
        });
        
        // Action buttons
        gestisciRegistrazioniButton.addActionListener(e -> handleGestisciRegistrazioni());
    }
    
    /**
     * Aggiorna i dati del pannello
     */
    public void refreshData() {
        registrazioniListModel.clear();
        // TODO: Implement get registrazioni in controller
        // List<Registrazione> registrazioni = controller.getRegistrazioni();
        // for (Registrazione registrazione : registrazioni) {
        //     registrazioniListModel.addElement(registrazione);
        // }
    }
    
    /**
     * Mostra il dialog per registrare un utente
     */
    private void showRegistraUtenteDialog() {
        JDialog dialog = new JDialog(mainFrame, "Registra Utente", true);
        dialog.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Form fields
        JComboBox<String> hackathonComboBox = new JComboBox<>(new String[]{"Seleziona Hackathon"});
        JComboBox<String> ruoloComboBox = new JComboBox<>(new String[]{"PARTECIPANTE", "GIUDICE", "ORGANIZZATORE"});
        
        // Add components
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Hackathon:"), gbc);
        gbc.gridx = 1;
        dialog.add(hackathonComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Ruolo:"), gbc);
        gbc.gridx = 1;
        dialog.add(ruoloComboBox, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton confirmButton = new JButton("Registra");
        JButton cancelButton = new JButton("Annulla");
        
        confirmButton.addActionListener(e -> {
            String selectedHackathon = (String) hackathonComboBox.getSelectedItem();
            String selectedRuolo = (String) ruoloComboBox.getSelectedItem();
            
            if ("Seleziona Hackathon".equals(selectedHackathon)) {
                mainFrame.showError("Seleziona un hackathon");
                return;
            }
            
            // TODO: Implement registraUtenteAdHackathon in controller
            mainFrame.showInfo("Utente registrato con successo!");
            dialog.dispose();
            refreshData();
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);
        
        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }
    
    /**
     * Gestisce la gestione delle registrazioni
     */
    private void handleGestisciRegistrazioni() {
        Registrazione selectedRegistrazione = registrazioniList.getSelectedValue();
        if (selectedRegistrazione != null) {
            mainFrame.showInfo("Gestione registrazione: " + selectedRegistrazione.getId());
            // TODO: Implement registration management dialog
        }
    }
} 