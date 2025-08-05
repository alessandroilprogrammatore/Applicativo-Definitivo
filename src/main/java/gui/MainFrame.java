package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Frame principale dell'applicazione Hackathon Manager.
 * Contiene la barra dei menu e i pannelli per tutte le funzionalità.
 */
public class MainFrame extends JFrame {
    
    private final Controller controller;
    
    // Menu components
    private JMenuBar menuBar;
    private JMenu eventiMenu;
    private JMenu teamMenu;
    private JMenu registrazioniMenu;
    private JMenu valutazioniMenu;
    private JMenu utenteMenu;
    
    // Main panel with card layout
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    // Panels for different functionalities
    private LoginPanel loginPanel;
    private EventiPanel eventiPanel;
    private TeamPanel teamPanel;
    private RegistrazioniPanel registrazioniPanel;
    private ValutazioniPanel valutazioniPanel;
    private UtentePanel utentePanel;
    
    // Constants for card names
    public static final String LOGIN_CARD = "LOGIN";
    public static final String EVENTI_CARD = "EVENTI";
    public static final String TEAM_CARD = "TEAM";
    public static final String REGISTRAZIONI_CARD = "REGISTRAZIONI";
    public static final String VALUTAZIONI_CARD = "VALUTAZIONI";
    public static final String UTENTE_CARD = "UTENTE";
    
    /**
     * Costruttore che inizializza la GUI principale
     */
    public MainFrame() {
        this.controller = new Controller();
        initializeFrame();
        initializeMenuBar();
        initializeMainPanel();
        setupEventHandlers();
        
        // Start with login panel
        showLoginPanel();
    }
    
    /**
     * Inizializza il frame principale
     */
    private void initializeFrame() {
        setTitle("Hackathon Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);
    }
    
    /**
     * Inizializza la barra dei menu
     */
    private void initializeMenuBar() {
        menuBar = new JMenuBar();
        
        // Menu Eventi
        eventiMenu = new JMenu("Eventi");
        JMenuItem creaEventoItem = new JMenuItem("Crea Evento");
        JMenuItem visualizzaEventiItem = new JMenuItem("Visualizza Eventi");
        JMenuItem gestisciEventiItem = new JMenuItem("Gestisci Eventi");
        
        eventiMenu.add(creaEventoItem);
        eventiMenu.add(visualizzaEventiItem);
        eventiMenu.add(gestisciEventiItem);
        
        // Menu Team
        teamMenu = new JMenu("Team");
        JMenuItem creaTeamItem = new JMenuItem("Crea Team");
        JMenuItem gestisciTeamItem = new JMenuItem("Gestisci Team");
        JMenuItem richiesteJoinItem = new JMenuItem("Richieste di Join");
        
        teamMenu.add(creaTeamItem);
        teamMenu.add(gestisciTeamItem);
        teamMenu.add(richiesteJoinItem);
        
        // Menu Registrazioni
        registrazioniMenu = new JMenu("Registrazioni");
        JMenuItem registraUtenteItem = new JMenuItem("Registra Utente");
        JMenuItem gestisciRegistrazioniItem = new JMenuItem("Gestisci Registrazioni");
        
        registrazioniMenu.add(registraUtenteItem);
        registrazioniMenu.add(gestisciRegistrazioniItem);
        
        // Menu Valutazioni
        valutazioniMenu = new JMenu("Valutazioni");
        JMenuItem assegnaVotiItem = new JMenuItem("Assegna Voti");
        JMenuItem visualizzaClassificaItem = new JMenuItem("Visualizza Classifica");
        
        valutazioniMenu.add(assegnaVotiItem);
        valutazioniMenu.add(visualizzaClassificaItem);
        
        // Menu Utente
        utenteMenu = new JMenu("Utente");
        JMenuItem profiloItem = new JMenuItem("Profilo");
        JMenuItem logoutItem = new JMenuItem("Logout");
        
        utenteMenu.add(profiloItem);
        utenteMenu.add(logoutItem);
        
        // Add menus to menu bar
        menuBar.add(eventiMenu);
        menuBar.add(teamMenu);
        menuBar.add(registrazioniMenu);
        menuBar.add(valutazioniMenu);
        menuBar.add(utenteMenu);
        
        setJMenuBar(menuBar);
        
        // Initially disable all menus except login
        setMenuEnabled(false);
    }
    
    /**
     * Inizializza il pannello principale con CardLayout
     */
    private void initializeMainPanel() {
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        
        // Create panels
        loginPanel = new LoginPanel(controller, this);
        eventiPanel = new EventiPanel(controller, this);
        teamPanel = new TeamPanel(controller, this);
        registrazioniPanel = new RegistrazioniPanel(controller, this);
        valutazioniPanel = new ValutazioniPanel(controller, this);
        utentePanel = new UtentePanel(controller, this);
        
        // Add panels to card layout
        mainPanel.add(loginPanel, LOGIN_CARD);
        mainPanel.add(eventiPanel, EVENTI_CARD);
        mainPanel.add(teamPanel, TEAM_CARD);
        mainPanel.add(registrazioniPanel, REGISTRAZIONI_CARD);
        mainPanel.add(valutazioniPanel, VALUTAZIONI_CARD);
        mainPanel.add(utentePanel, UTENTE_CARD);
        
        add(mainPanel);
    }
    
    /**
     * Configura gli event handler per i menu
     */
    private void setupEventHandlers() {
        // Eventi menu handlers
        eventiMenu.getItem(0).addActionListener(e -> showEventiPanel()); // Crea Evento
        eventiMenu.getItem(1).addActionListener(e -> showEventiPanel()); // Visualizza Eventi
        eventiMenu.getItem(2).addActionListener(e -> showEventiPanel()); // Gestisci Eventi
        
        // Team menu handlers
        teamMenu.getItem(0).addActionListener(e -> showTeamPanel()); // Crea Team
        teamMenu.getItem(1).addActionListener(e -> showTeamPanel()); // Gestisci Team
        teamMenu.getItem(2).addActionListener(e -> showTeamPanel()); // Richieste di Join
        
        // Registrazioni menu handlers
        registrazioniMenu.getItem(0).addActionListener(e -> showRegistrazioniPanel()); // Registra Utente
        registrazioniMenu.getItem(1).addActionListener(e -> showRegistrazioniPanel()); // Gestisci Registrazioni
        
        // Valutazioni menu handlers
        valutazioniMenu.getItem(0).addActionListener(e -> showValutazioniPanel()); // Assegna Voti
        valutazioniMenu.getItem(1).addActionListener(e -> showValutazioniPanel()); // Visualizza Classifica
        
        // Utente menu handlers
        utenteMenu.getItem(0).addActionListener(e -> showUtentePanel()); // Profilo
        utenteMenu.getItem(1).addActionListener(e -> logout()); // Logout
    }
    
    /**
     * Mostra il pannello di login
     */
    public void showLoginPanel() {
        cardLayout.show(mainPanel, LOGIN_CARD);
        setMenuEnabled(false);
        setTitle("Hackathon Manager - Login");
    }
    
    /**
     * Mostra il pannello eventi
     */
    public void showEventiPanel() {
        cardLayout.show(mainPanel, EVENTI_CARD);
        eventiPanel.refreshData();
        setTitle("Hackathon Manager - Eventi");
    }
    
    /**
     * Mostra il pannello team
     */
    public void showTeamPanel() {
        cardLayout.show(mainPanel, TEAM_CARD);
        teamPanel.refreshData();
        setTitle("Hackathon Manager - Team");
    }
    
    /**
     * Mostra il pannello registrazioni
     */
    public void showRegistrazioniPanel() {
        cardLayout.show(mainPanel, REGISTRAZIONI_CARD);
        registrazioniPanel.refreshData();
        setTitle("Hackathon Manager - Registrazioni");
    }
    
    /**
     * Mostra il pannello valutazioni
     */
    public void showValutazioniPanel() {
        cardLayout.show(mainPanel, VALUTAZIONI_CARD);
        valutazioniPanel.refreshData();
        setTitle("Hackathon Manager - Valutazioni");
    }
    
    /**
     * Mostra il pannello utente
     */
    public void showUtentePanel() {
        cardLayout.show(mainPanel, UTENTE_CARD);
        utentePanel.refreshData();
        setTitle("Hackathon Manager - Profilo Utente");
    }
    
    /**
     * Abilita/disabilita i menu in base all'autenticazione
     *
     * @param enabled true per abilitare i menu, false per disabilitarli
     */
    private void setMenuEnabled(boolean enabled) {
        eventiMenu.setEnabled(enabled);
        teamMenu.setEnabled(enabled);
        registrazioniMenu.setEnabled(enabled);
        valutazioniMenu.setEnabled(enabled);
        utenteMenu.setEnabled(enabled);
    }
    
    /**
     * Gestisce il login dell'utente
     */
    public void handleLogin() {
        Utente currentUser = controller.getCurrentUser();
        if (currentUser != null) {
            setMenuEnabled(true);
            updateMenuVisibility(currentUser);
            showEventiPanel(); // Default panel after login
            JOptionPane.showMessageDialog(this, 
                "Benvenuto " + currentUser.getNome() + " " + currentUser.getCognome() + "!", 
                "Login Riuscito", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Aggiorna la visibilità dei menu in base al ruolo dell'utente
     *
     * @param user l'utente corrente
     */
    private void updateMenuVisibility(Utente user) {
        // Eventi menu - tutti possono vedere
        eventiMenu.setVisible(true);
        
        // Team menu - solo per partecipanti
        teamMenu.setVisible(user.isPartecipante());
        
        // Registrazioni menu - tutti possono vedere
        registrazioniMenu.setVisible(true);
        
        // Valutazioni menu - solo per giudici
        valutazioniMenu.setVisible(user.isGiudice());
        
        // Utente menu - tutti possono vedere
        utenteMenu.setVisible(true);
    }
    
    /**
     * Gestisce il logout dell'utente
     */
    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this, 
            "Sei sicuro di voler effettuare il logout?", 
            "Conferma Logout", JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            controller.logout();
            setMenuEnabled(false);
            showLoginPanel();
            JOptionPane.showMessageDialog(this, 
                "Logout effettuato con successo!", 
                "Logout", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Mostra un messaggio di errore
     *
     * @param message il messaggio di errore
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Mostra un messaggio di informazione
     *
     * @param message il messaggio di informazione
     */
    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Informazione", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Ottiene il controller
     *
     * @return il controller
     */
    public Controller getController() {
        return controller;
    }
} 