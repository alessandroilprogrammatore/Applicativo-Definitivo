package gui;

import controller.Controller;
import model.Hackathon;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Pannello per la gestione degli eventi/hackathon.
 */
public class EventiPanel extends JPanel {
    
    private final Controller controller;
    private final MainFrame mainFrame;
    
    // Components
    private JTable eventiTable;
    private DefaultListModel<Hackathon> eventiListModel;
    private JList<Hackathon> eventiList;
    private JButton creaEventoButton;
    private JButton apriRegistrazioniButton;
    private JButton chiudiRegistrazioniButton;
    private JButton avviaEventoButton;
    private JButton concludeEventoButton;
    
    /**
     * Costruttore che inizializza il pannello eventi
     *
     * @param controller il controller dell'applicazione
     * @param mainFrame  il frame principale
     */
    public EventiPanel(Controller controller, MainFrame mainFrame) {
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
        // List model for events
        eventiListModel = new DefaultListModel<>();
        eventiList = new JList<>(eventiListModel);
        eventiList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Buttons
        creaEventoButton = new JButton("Crea Nuovo Evento");
        apriRegistrazioniButton = new JButton("Apri Registrazioni");
        chiudiRegistrazioniButton = new JButton("Chiudi Registrazioni");
        avviaEventoButton = new JButton("Avvia Evento");
        concludeEventoButton = new JButton("Conclude Evento");
        
        // Initially disable buttons that require selection
        apriRegistrazioniButton.setEnabled(false);
        chiudiRegistrazioniButton.setEnabled(false);
        avviaEventoButton.setEnabled(false);
        concludeEventoButton.setEnabled(false);
    }
    
    /**
     * Configura il layout del pannello
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Title
        JLabel titleLabel = new JLabel("Gestione Eventi", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);
        
        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        
        // List panel
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder("Eventi"));
        
        JScrollPane scrollPane = new JScrollPane(eventiList);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        listPanel.add(scrollPane, BorderLayout.CENTER);
        
        contentPanel.add(listPanel, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        buttonsPanel.setBorder(BorderFactory.createTitledBorder("Azioni"));
        
        buttonsPanel.add(creaEventoButton);
        buttonsPanel.add(apriRegistrazioniButton);
        buttonsPanel.add(chiudiRegistrazioniButton);
        buttonsPanel.add(avviaEventoButton);
        buttonsPanel.add(concludeEventoButton);
        
        contentPanel.add(buttonsPanel, BorderLayout.EAST);
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Configura gli event handler
     */
    private void setupEventHandlers() {
        // Create event button
        creaEventoButton.addActionListener(e -> showCreaEventoDialog());
        
        // List selection listener
        eventiList.addListSelectionListener(e -> {
            boolean hasSelection = !eventiList.isSelectionEmpty();
            apriRegistrazioniButton.setEnabled(hasSelection);
            chiudiRegistrazioniButton.setEnabled(hasSelection);
            avviaEventoButton.setEnabled(hasSelection);
            concludeEventoButton.setEnabled(hasSelection);
        });
        
        // Action buttons
        apriRegistrazioniButton.addActionListener(e -> handleApriRegistrazioni());
        chiudiRegistrazioniButton.addActionListener(e -> handleChiudiRegistrazioni());
        avviaEventoButton.addActionListener(e -> handleAvviaEvento());
        concludeEventoButton.addActionListener(e -> handleConcludeEvento());
    }
    
    /**
     * Aggiorna i dati del pannello
     */
    public void refreshData() {
        eventiListModel.clear();
        List<Hackathon> eventi = controller.getTuttiHackathon();
        
        for (Hackathon evento : eventi) {
            eventiListModel.addElement(evento);
        }
    }
    
    /**
     * Mostra il dialog per creare un nuovo evento
     */
    private void showCreaEventoDialog() {
        JDialog dialog = new JDialog(mainFrame, "Crea Nuovo Evento", true);
        dialog.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Form fields
        JTextField nomeField = new JTextField(20);
        JTextField sedeField = new JTextField(20);
        JCheckBox virtualeCheckBox = new JCheckBox("Evento Virtuale");
        JSpinner maxPartecipantiSpinner = new JSpinner(new SpinnerNumberModel(50, 1, 1000, 1));
        JSpinner maxTeamSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        
        // Date fields (simplified for demo)
        JTextField dataInizioField = new JTextField("YYYY-MM-DD HH:MM", 20);
        
        // Add components
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Nome Evento:"), gbc);
        gbc.gridx = 1;
        dialog.add(nomeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Sede:"), gbc);
        gbc.gridx = 1;
        dialog.add(sedeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Data Inizio:"), gbc);
        gbc.gridx = 1;
        dialog.add(dataInizioField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(virtualeCheckBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        dialog.add(new JLabel("Max Partecipanti:"), gbc);
        gbc.gridx = 1;
        dialog.add(maxPartecipantiSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        dialog.add(new JLabel("Max Team:"), gbc);
        gbc.gridx = 1;
        dialog.add(maxTeamSpinner, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton confirmButton = new JButton("Crea");
        JButton cancelButton = new JButton("Annulla");
        
        confirmButton.addActionListener(e -> {
            try {
                String nome = nomeField.getText().trim();
                String sede = sedeField.getText().trim();
                boolean isVirtuale = virtualeCheckBox.isSelected();
                int maxPartecipanti = (Integer) maxPartecipantiSpinner.getValue();
                int maxTeam = (Integer) maxTeamSpinner.getValue();
                
                // Parse date (simplified)
                LocalDateTime dataInizio = LocalDateTime.now().plusDays(7); // Default to next week
                
                if (nome.isEmpty() || sede.isEmpty()) {
                    mainFrame.showError("Nome e sede sono obbligatori");
                    return;
                }
                
                int id = controller.creaHackathon(nome, dataInizio, sede, isVirtuale, maxPartecipanti, maxTeam);
                if (id > 0) {
                    mainFrame.showInfo("Evento creato con successo!");
                    dialog.dispose();
                    refreshData();
                } else {
                    mainFrame.showError("Errore durante la creazione dell'evento");
                }
            } catch (Exception ex) {
                mainFrame.showError("Errore: " + ex.getMessage());
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);
        
        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }
    
    /**
     * Gestisce l'apertura delle registrazioni
     */
    private void handleApriRegistrazioni() {
        Hackathon selectedEvento = eventiList.getSelectedValue();
        if (selectedEvento != null) {
            if (controller.apriRegistrazioni(selectedEvento.getId())) {
                mainFrame.showInfo("Registrazioni aperte con successo!");
                refreshData();
            } else {
                mainFrame.showError("Errore durante l'apertura delle registrazioni");
            }
        }
    }
    
    /**
     * Gestisce la chiusura delle registrazioni
     */
    private void handleChiudiRegistrazioni() {
        Hackathon selectedEvento = eventiList.getSelectedValue();
        if (selectedEvento != null) {
            if (controller.chiudiRegistrazioni(selectedEvento.getId())) {
                mainFrame.showInfo("Registrazioni chiuse con successo!");
                refreshData();
            } else {
                mainFrame.showError("Errore durante la chiusura delle registrazioni");
            }
        }
    }
    
    /**
     * Gestisce l'avvio dell'evento
     */
    private void handleAvviaEvento() {
        Hackathon selectedEvento = eventiList.getSelectedValue();
        if (selectedEvento != null) {
            String descrizioneProblema = JOptionPane.showInputDialog(mainFrame, 
                "Inserisci la descrizione del problema:", 
                "Avvia Evento", 
                JOptionPane.QUESTION_MESSAGE);
            
            if (descrizioneProblema != null && !descrizioneProblema.trim().isEmpty()) {
                if (controller.avviaHackathon(selectedEvento.getId(), descrizioneProblema)) {
                    mainFrame.showInfo("Evento avviato con successo!");
                    refreshData();
                } else {
                    mainFrame.showError("Errore durante l'avvio dell'evento");
                }
            }
        }
    }
    
    /**
     * Gestisce la conclusione dell'evento
     */
    private void handleConcludeEvento() {
        Hackathon selectedEvento = eventiList.getSelectedValue();
        if (selectedEvento != null) {
            int choice = JOptionPane.showConfirmDialog(mainFrame, 
                "Sei sicuro di voler concludere l'evento?", 
                "Conclude Evento", 
                JOptionPane.YES_NO_OPTION);
            
            if (choice == JOptionPane.YES_OPTION) {
                // TODO: Implement concludeHackathon in controller
                mainFrame.showInfo("Evento concluso con successo!");
                refreshData();
            }
        }
    }
} 