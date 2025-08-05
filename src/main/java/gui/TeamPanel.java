package gui;

import controller.Controller;
import model.Team;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Pannello per la gestione dei team.
 */
public class TeamPanel extends JPanel {
    
    private final Controller controller;
    private final MainFrame mainFrame;
    
    // Components
    private DefaultListModel<Team> teamListModel;
    private JList<Team> teamList;
    private JButton creaTeamButton;
    private JButton gestisciTeamButton;
    private JButton richiesteJoinButton;
    
    /**
     * Costruttore che inizializza il pannello team
     *
     * @param controller il controller dell'applicazione
     * @param mainFrame  il frame principale
     */
    public TeamPanel(Controller controller, MainFrame mainFrame) {
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
        // List model for teams
        teamListModel = new DefaultListModel<>();
        teamList = new JList<>(teamListModel);
        teamList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Buttons
        creaTeamButton = new JButton("Crea Nuovo Team");
        gestisciTeamButton = new JButton("Gestisci Team");
        richiesteJoinButton = new JButton("Richieste di Join");
        
        // Initially disable buttons that require selection
        gestisciTeamButton.setEnabled(false);
        richiesteJoinButton.setEnabled(false);
    }
    
    /**
     * Configura il layout del pannello
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Title
        JLabel titleLabel = new JLabel("Gestione Team", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);
        
        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        
        // List panel
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder("Team"));
        
        JScrollPane scrollPane = new JScrollPane(teamList);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        listPanel.add(scrollPane, BorderLayout.CENTER);
        
        contentPanel.add(listPanel, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        buttonsPanel.setBorder(BorderFactory.createTitledBorder("Azioni"));
        
        buttonsPanel.add(creaTeamButton);
        buttonsPanel.add(gestisciTeamButton);
        buttonsPanel.add(richiesteJoinButton);
        
        contentPanel.add(buttonsPanel, BorderLayout.EAST);
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Configura gli event handler
     */
    private void setupEventHandlers() {
        // Create team button
        creaTeamButton.addActionListener(e -> showCreaTeamDialog());
        
        // List selection listener
        teamList.addListSelectionListener(e -> {
            boolean hasSelection = !teamList.isSelectionEmpty();
            gestisciTeamButton.setEnabled(hasSelection);
            richiesteJoinButton.setEnabled(hasSelection);
        });
        
        // Action buttons
        gestisciTeamButton.addActionListener(e -> handleGestisciTeam());
        richiesteJoinButton.addActionListener(e -> handleRichiesteJoin());
    }
    
    /**
     * Aggiorna i dati del pannello
     */
    public void refreshData() {
        teamListModel.clear();
        // TODO: Implement getTeamHackathon in controller
        // List<Team> teams = controller.getTeamHackathon(hackathonId);
        // for (Team team : teams) {
        //     teamListModel.addElement(team);
        // }
    }
    
    /**
     * Mostra il dialog per creare un nuovo team
     */
    private void showCreaTeamDialog() {
        JDialog dialog = new JDialog(mainFrame, "Crea Nuovo Team", true);
        dialog.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Form fields
        JTextField nomeTeamField = new JTextField(20);
        JSpinner dimensioneSpinner = new JSpinner(new SpinnerNumberModel(3, 2, 10, 1));
        
        // Add components
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Nome Team:"), gbc);
        gbc.gridx = 1;
        dialog.add(nomeTeamField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Dimensione Massima:"), gbc);
        gbc.gridx = 1;
        dialog.add(dimensioneSpinner, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton confirmButton = new JButton("Crea");
        JButton cancelButton = new JButton("Annulla");
        
        confirmButton.addActionListener(e -> {
            String nomeTeam = nomeTeamField.getText().trim();
            int dimensione = (Integer) dimensioneSpinner.getValue();
            
            if (nomeTeam.isEmpty()) {
                mainFrame.showError("Il nome del team è obbligatorio");
                return;
            }
            
            // TODO: Implement creaTeam in controller
            mainFrame.showInfo("Team creato con successo!");
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
     * Gestisce la gestione del team
     */
    private void handleGestisciTeam() {
        Team selectedTeam = teamList.getSelectedValue();
        if (selectedTeam != null) {
            mainFrame.showInfo("Gestione team: " + selectedTeam.getNome());
            // TODO: Implement team management dialog
        }
    }
    
    /**
     * Gestisce le richieste di join
     */
    private void handleRichiesteJoin() {
        Team selectedTeam = teamList.getSelectedValue();
        if (selectedTeam != null) {
            mainFrame.showInfo("Richieste di join per team: " + selectedTeam.getNome());
            // TODO: Implement join requests dialog
        }
    }
} 