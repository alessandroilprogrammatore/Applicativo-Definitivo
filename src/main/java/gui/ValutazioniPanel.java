package gui;

import controller.Controller;
import model.Valutazione;

import javax.swing.*;
import java.awt.*;

/**
 * Pannello per la gestione delle valutazioni.
 */
public class ValutazioniPanel extends JPanel {
    
    private final Controller controller;
    private final MainFrame mainFrame;
    
    // Components
    private DefaultListModel<Valutazione> valutazioniListModel;
    private JList<Valutazione> valutazioniList;
    private JButton assegnaVotiButton;
    private JButton visualizzaClassificaButton;
    
    /**
     * Costruttore che inizializza il pannello valutazioni
     *
     * @param controller il controller dell'applicazione
     * @param mainFrame  il frame principale
     */
    public ValutazioniPanel(Controller controller, MainFrame mainFrame) {
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
        // List model for evaluations
        valutazioniListModel = new DefaultListModel<>();
        valutazioniList = new JList<>(valutazioniListModel);
        valutazioniList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Buttons
        assegnaVotiButton = new JButton("Assegna Voti");
        visualizzaClassificaButton = new JButton("Visualizza Classifica");
        
        // Initially disable buttons that require selection
        assegnaVotiButton.setEnabled(false);
    }
    
    /**
     * Configura il layout del pannello
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Title
        JLabel titleLabel = new JLabel("Gestione Valutazioni", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);
        
        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        
        // List panel
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder("Valutazioni"));
        
        JScrollPane scrollPane = new JScrollPane(valutazioniList);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        listPanel.add(scrollPane, BorderLayout.CENTER);
        
        contentPanel.add(listPanel, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        buttonsPanel.setBorder(BorderFactory.createTitledBorder("Azioni"));
        
        buttonsPanel.add(assegnaVotiButton);
        buttonsPanel.add(visualizzaClassificaButton);
        
        contentPanel.add(buttonsPanel, BorderLayout.EAST);
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Configura gli event handler
     */
    private void setupEventHandlers() {
        // Assign votes button
        assegnaVotiButton.addActionListener(e -> showAssegnaVotiDialog());
        
        // List selection listener
        valutazioniList.addListSelectionListener(e -> {
            boolean hasSelection = !valutazioniList.isSelectionEmpty();
            assegnaVotiButton.setEnabled(hasSelection);
        });
        
        // Action buttons
        visualizzaClassificaButton.addActionListener(e -> handleVisualizzaClassifica());
    }
    
    /**
     * Aggiorna i dati del pannello
     */
    public void refreshData() {
        valutazioniListModel.clear();
        // TODO: Implement get valutazioni in controller
        // List<Valutazione> valutazioni = controller.getValutazioni();
        // for (Valutazione valutazione : valutazioni) {
        //     valutazioniListModel.addElement(valutazione);
        // }
    }
    
    /**
     * Mostra il dialog per assegnare voti
     */
    private void showAssegnaVotiDialog() {
        JDialog dialog = new JDialog(mainFrame, "Assegna Voti", true);
        dialog.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Form fields
        JComboBox<String> teamComboBox = new JComboBox<>(new String[]{"Seleziona Team"});
        JSpinner votoSpinner = new JSpinner(new SpinnerNumberModel(5, 0, 10, 1));
        JTextArea commentoArea = new JTextArea(4, 20);
        commentoArea.setLineWrap(true);
        commentoArea.setWrapStyleWord(true);
        
        // Add components
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Team:"), gbc);
        gbc.gridx = 1;
        dialog.add(teamComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Voto (0-10):"), gbc);
        gbc.gridx = 1;
        dialog.add(votoSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Commento:"), gbc);
        gbc.gridx = 1;
        dialog.add(new JScrollPane(commentoArea), gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton confirmButton = new JButton("Assegna Voto");
        JButton cancelButton = new JButton("Annulla");
        
        confirmButton.addActionListener(e -> {
            String selectedTeam = (String) teamComboBox.getSelectedItem();
            int voto = (Integer) votoSpinner.getValue();
            String commento = commentoArea.getText().trim();
            
            if ("Seleziona Team".equals(selectedTeam)) {
                mainFrame.showError("Seleziona un team");
                return;
            }
            
            // TODO: Implement assegnaVoto in controller
            mainFrame.showInfo("Voto assegnato con successo!");
            dialog.dispose();
            refreshData();
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);
        
        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }
    
    /**
     * Gestisce la visualizzazione della classifica
     */
    private void handleVisualizzaClassifica() {
        mainFrame.showInfo("Visualizzazione classifica");
        // TODO: Implement classifica dialog
    }
} 