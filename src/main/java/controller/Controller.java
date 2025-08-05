package controller;

import dao.*;
import implementazionePostgresDAO.*;
import model.*;
import database.ConnectionManager;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller principale del sistema Hackathon Manager.
 * Media tra la GUI e la logica di business, gestendo tutte le operazioni principali.
 */
public class Controller {
    
    // DAO instances
    private final HackathonDAO hackathonDAO;
    private final UtenteDAO utenteDAO;
    private final TeamDAO teamDAO;
    private final RegistrazioneDAO registrazioneDAO;
    private final ProgressDAO progressDAO;
    private final ValutazioneDAO valutazioneDAO;
    
    // Current user session
    private Utente currentUser;
    
    /**
     * Costruttore che inizializza tutti i DAO
     */
    public Controller() {
        this.hackathonDAO = new HackathonPostgresDAO();
        this.utenteDAO = new UtentePostgresDAO();
        this.teamDAO = new TeamPostgresDAO();
        this.registrazioneDAO = new RegistrazionePostgresDAO();
        this.progressDAO = new ProgressPostgresDAO();
        this.valutazioneDAO = new ValutazionePostgresDAO();
    }

    // ==================== AUTENTICAZIONE E GESTIONE UTENTI ====================

    /**
     * Autentica un utente nel sistema
     *
     * @param login    il login dell'utente
     * @param password la password dell'utente
     * @return true se l'autenticazione è riuscita
     */
    public boolean login(String login, String password) {
        currentUser = utenteDAO.autentica(login, password);
        return currentUser != null;
    }

    /**
     * Registra un nuovo utente nel sistema
     *
     * @param login    il login del nuovo utente
     * @param password la password del nuovo utente
     * @param nome     il nome del nuovo utente
     * @param cognome  il cognome del nuovo utente
     * @param email    l'email del nuovo utente
     * @param ruolo    il ruolo del nuovo utente
     * @return true se la registrazione è riuscita
     */
    public boolean registraUtente(String login, String password, String nome, String cognome, String email, String ruolo) {
        // Verifica che login ed email non siano già utilizzati
        if (utenteDAO.isLoginUtilizzato(login)) {
            return false; // Login già utilizzato
        }
        
        if (utenteDAO.isEmailUtilizzata(email)) {
            return false; // Email già utilizzata
        }
        
        Utente nuovoUtente = new Utente(login, password, nome, cognome, email, ruolo);
        int id = utenteDAO.insert(nuovoUtente);
        return id > 0;
    }

    /**
     * Ottiene l'utente corrente
     *
     * @return l'utente corrente o null se non autenticato
     */
    public Utente getCurrentUser() {
        return currentUser;
    }

    /**
     * Effettua il logout dell'utente corrente
     */
    public void logout() {
        currentUser = null;
    }

    // ==================== GESTIONE HACKATHON ====================

    /**
     * Crea un nuovo hackathon
     *
     * @param nome              il nome dell'hackathon
     * @param dataInizio        la data di inizio
     * @param sede              la sede dell'evento
     * @param isVirtuale        se l'evento è virtuale
     * @param maxPartecipanti   il numero massimo di partecipanti
     * @param maxTeam           il numero massimo di team
     * @return l'ID dell'hackathon creato o -1 se fallito
     */
    public int creaHackathon(String nome, LocalDateTime dataInizio, String sede, 
                            boolean isVirtuale, int maxPartecipanti, int maxTeam) {
        if (currentUser == null || !currentUser.isOrganizzatore()) {
            return -1; // Solo gli organizzatori possono creare hackathon
        }
        
        Hackathon hackathon = new Hackathon(nome, dataInizio, sede, isVirtuale, 
                                          currentUser.getId(), maxPartecipanti, maxTeam);
        return hackathonDAO.insert(hackathon);
    }

    /**
     * Ottiene tutti gli hackathon
     *
     * @return lista di tutti gli hackathon
     */
    public List<Hackathon> getTuttiHackathon() {
        return hackathonDAO.findAll();
    }

    /**
     * Ottiene gli hackathon con registrazioni aperte
     *
     * @return lista degli hackathon con registrazioni aperte
     */
    public List<Hackathon> getHackathonConRegistrazioniAperte() {
        return hackathonDAO.findConRegistrazioniAperte();
    }

    /**
     * Ottiene gli hackathon in corso
     *
     * @return lista degli hackathon in corso
     */
    public List<Hackathon> getHackathonInCorso() {
        return hackathonDAO.findInCorso();
    }

    /**
     * Apre le registrazioni per un hackathon
     *
     * @param hackathonId l'ID dell'hackathon
     * @return true se l'operazione è riuscita
     */
    public boolean apriRegistrazioni(int hackathonId) {
        if (currentUser == null || !currentUser.isOrganizzatore()) {
            return false;
        }
        
        Hackathon hackathon = hackathonDAO.findById(hackathonId);
        if (hackathon == null || hackathon.getOrganizzatoreId() != currentUser.getId()) {
            return false; // Solo l'organizzatore può aprire le registrazioni
        }
        
        return hackathonDAO.apriRegistrazioni(hackathonId);
    }

    /**
     * Chiude le registrazioni per un hackathon
     *
     * @param hackathonId l'ID dell'hackathon
     * @return true se l'operazione è riuscita
     */
    public boolean chiudiRegistrazioni(int hackathonId) {
        if (currentUser == null || !currentUser.isOrganizzatore()) {
            return false;
        }
        
        Hackathon hackathon = hackathonDAO.findById(hackathonId);
        if (hackathon == null || hackathon.getOrganizzatoreId() != currentUser.getId()) {
            return false;
        }
        
        return hackathonDAO.chiudiRegistrazioni(hackathonId);
    }

    /**
     * Avvia un hackathon (pubblica il problema)
     *
     * @param hackathonId           l'ID dell'hackathon
     * @param descrizioneProblema   la descrizione del problema
     * @return true se l'operazione è riuscita
     */
    public boolean avviaHackathon(int hackathonId, String descrizioneProblema) {
        if (currentUser == null || !currentUser.isOrganizzatore()) {
            return false;
        }
        
        Hackathon hackathon = hackathonDAO.findById(hackathonId);
        if (hackathon == null || hackathon.getOrganizzatoreId() != currentUser.getId()) {
            return false;
        }
        
        return hackathonDAO.avviaHackathon(hackathonId, descrizioneProblema);
    }

    // ==================== GESTIONE REGISTRAZIONI ====================

    /**
     * Registra un utente ad un hackathon
     *
     * @param hackathonId l'ID dell'hackathon
     * @param ruolo       il ruolo dell'utente nell'hackathon
     * @return true se la registrazione è riuscita
     */
    public boolean registraUtenteAdHackathon(int hackathonId, Registrazione.Ruolo ruolo) {
        if (currentUser == null) {
            return false;
        }
        
        // Verifica che l'hackathon esista e abbia le registrazioni aperte
        Hackathon hackathon = hackathonDAO.findById(hackathonId);
        if (hackathon == null || !hackathon.isRegistrazioniAperte()) {
            return false;
        }
        
        // Verifica che l'utente non sia già registrato
        if (registrazioneDAO.isRegistrato(currentUser.getId(), hackathonId)) {
            return false;
        }
        
        // Verifica i limiti se l'utente è un partecipante
        if (ruolo == Registrazione.Ruolo.PARTECIPANTE) {
            if (hackathonDAO.haRaggiuntoLimitePartecipanti(hackathonId)) {
                return false;
            }
        }
        
        Registrazione registrazione = new Registrazione(currentUser.getId(), hackathonId, ruolo);
        int id = registrazioneDAO.insert(registrazione);
        return id > 0;
    }

    /**
     * Conferma una registrazione
     *
     * @param registrazioneId l'ID della registrazione
     * @return true se la conferma è riuscita
     */
    public boolean confermaRegistrazione(int registrazioneId) {
        if (currentUser == null || !currentUser.isOrganizzatore()) {
            return false;
        }
        
        return registrazioneDAO.confermaRegistrazione(registrazioneId);
    }

    // ==================== GESTIONE TEAM ====================

    /**
     * Crea un nuovo team
     *
     * @param hackathonId       l'ID dell'hackathon
     * @param nomeTeam          il nome del team
     * @param dimensioneMassima la dimensione massima del team
     * @return l'ID del team creato o -1 se fallito
     */
    public int creaTeam(int hackathonId, String nomeTeam, int dimensioneMassima) {
        if (currentUser == null) {
            return -1;
        }
        
        // Verifica che l'utente sia registrato come partecipante all'hackathon
        Registrazione registrazione = registrazioneDAO.findByUtenteAndHackathon(currentUser.getId(), hackathonId);
        if (registrazione == null || !registrazione.isPartecipante() || !registrazione.isConfermata()) {
            return -1;
        }
        
        // Verifica che non abbia già un team per questo hackathon
        List<Team> teamUtente = teamDAO.findByMembro(currentUser.getId());
        for (Team team : teamUtente) {
            if (team.getHackathonId() == hackathonId) {
                return -1; // L'utente è già in un team per questo hackathon
            }
        }
        
        Team team = new Team(nomeTeam, hackathonId, currentUser.getId(), dimensioneMassima);
        return teamDAO.insert(team);
    }

    /**
     * Invia una richiesta di join ad un team
     *
     * @param teamId                 l'ID del team
     * @param messaggioMotivazionale il messaggio motivazionale
     * @return true se la richiesta è stata inviata
     */
    public boolean inviaRichiestaJoin(int teamId, String messaggioMotivazionale) {
        if (currentUser == null) {
            return false;
        }
        
        Team team = teamDAO.findById(teamId);
        if (team == null || !team.haSpazioDisponibile()) {
            return false;
        }
        
        RichiestaJoin richiesta = new RichiestaJoin(currentUser.getId(), teamId, messaggioMotivazionale);
        int id = teamDAO.insertRichiestaJoin(richiesta);
        return id > 0;
    }

    /**
     * Accetta una richiesta di join
     *
     * @param richiestaId l'ID della richiesta
     * @return true se l'accettazione è riuscita
     */
    public boolean accettaRichiestaJoin(int richiestaId) {
        if (currentUser == null) {
            return false;
        }
        
        // TODO: Verificare che l'utente sia capo del team
        return teamDAO.accettaRichiestaJoin(richiestaId);
    }

    /**
     * Rifiuta una richiesta di join
     *
     * @param richiestaId l'ID della richiesta
     * @return true se il rifiuto è riuscito
     */
    public boolean rifiutaRichiestaJoin(int richiestaId) {
        if (currentUser == null) {
            return false;
        }
        
        // TODO: Verificare che l'utente sia capo del team
        return teamDAO.rifiutaRichiestaJoin(richiestaId);
    }

    // ==================== GESTIONE PROGRESSI ====================

    /**
     * Carica un progresso per un team
     *
     * @param teamId        l'ID del team
     * @param titolo        il titolo del progresso
     * @param descrizione   la descrizione del progresso
     * @param documentoPath il percorso del documento
     * @return l'ID del progresso caricato o -1 se fallito
     */
    public int caricaProgresso(int teamId, String titolo, String descrizione, String documentoPath) {
        if (currentUser == null) {
            return -1;
        }
        
        // Verifica che l'utente sia membro del team
        if (!teamDAO.isMembro(teamId, currentUser.getId())) {
            return -1;
        }
        
        Team team = teamDAO.findById(teamId);
        if (team == null) {
            return -1;
        }
        
        Progress progress = new Progress(teamId, team.getHackathonId(), titolo, descrizione, documentoPath);
        return progressDAO.insert(progress);
    }

    /**
     * Aggiunge un commento di giudice ad un progresso
     *
     * @param progressId l'ID del progresso
     * @param commento   il commento del giudice
     * @return true se il commento è stato aggiunto
     */
    public boolean aggiungiCommentoGiudice(int progressId, String commento) {
        if (currentUser == null || !currentUser.isGiudice()) {
            return false;
        }
        
        return progressDAO.aggiungiCommentoGiudice(progressId, currentUser.getId(), commento);
    }

    // ==================== GESTIONE VALUTAZIONI ====================

    /**
     * Assegna un voto ad un team
     *
     * @param teamId   l'ID del team
     * @param voto     il voto (0-10)
     * @param commento il commento del giudice
     * @return true se la valutazione è stata assegnata
     */
    public boolean assegnaVoto(int teamId, int voto, String commento) {
        if (currentUser == null || !currentUser.isGiudice()) {
            return false;
        }
        
        if (voto < 0 || voto > 10) {
            return false; // Voto non valido
        }
        
        Team team = teamDAO.findById(teamId);
        if (team == null) {
            return false;
        }
        
        // Verifica se il giudice ha già valutato questo team
        if (valutazioneDAO.haGiudiceValutatoTeam(currentUser.getId(), teamId)) {
            return false; // Già valutato
        }
        
        Valutazione valutazione = new Valutazione(currentUser.getId(), teamId, team.getHackathonId(), voto, commento);
        Valutazione savedValutazione = valutazioneDAO.insert(valutazione);
        return savedValutazione != null && savedValutazione.getId() > 0;
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Ottiene tutti gli utenti
     *
     * @return lista di tutti gli utenti
     */
    public List<Utente> getTuttiUtenti() {
        return utenteDAO.findAll();
    }

    /**
     * Ottiene tutti i team di un hackathon
     *
     * @param hackathonId l'ID dell'hackathon
     * @return lista dei team dell'hackathon
     */
    public List<Team> getTeamHackathon(int hackathonId) {
        return teamDAO.findByHackathon(hackathonId);
    }

    /**
     * Ottiene tutti i progressi di un team
     *
     * @param teamId l'ID del team
     * @return lista dei progressi del team
     */
    public List<Progress> getProgressiTeam(int teamId) {
        return progressDAO.findByTeam(teamId);
    }

    /**
     * Ottiene tutte le valutazioni di un hackathon
     *
     * @param hackathonId l'ID dell'hackathon
     * @return lista delle valutazioni dell'hackathon
     */
    public List<Valutazione> getValutazioniHackathon(int hackathonId) {
        return valutazioneDAO.findByHackathon(hackathonId);
    }

    /**
     * Ottiene la classifica dei team in un hackathon
     *
     * @param hackathonId l'ID dell'hackathon
     * @return lista degli ID dei team ordinati per valutazione media
     */
    public List<Integer> getClassificaTeam(int hackathonId) {
        return valutazioneDAO.findClassificaTeam(hackathonId);
    }

    /**
     * Ottiene il team vincitore di un hackathon
     *
     * @param hackathonId l'ID dell'hackathon
     * @return l'ID del team vincitore o null se non ci sono valutazioni
     */
    public Integer getTeamVincitore(int hackathonId) {
        return valutazioneDAO.findTeamVincitore(hackathonId);
    }

    /**
     * Ottiene la valutazione media di un team
     *
     * @param teamId l'ID del team
     * @return la valutazione media del team
     */
    public double getValutazioneMediaTeam(int teamId) {
        return valutazioneDAO.findValutazioneMediaTeam(teamId);
    }

    /**
     * Verifica se l'utente corrente è autenticato
     *
     * @return true se l'utente è autenticato
     */
    public boolean isAutenticato() {
        return currentUser != null;
    }

    /**
     * Verifica se l'utente corrente è un organizzatore
     *
     * @return true se l'utente è un organizzatore
     */
    public boolean isOrganizzatore() {
        return currentUser != null && currentUser.isOrganizzatore();
    }

    /**
     * Verifica se l'utente corrente è un giudice
     *
     * @return true se l'utente è un giudice
     */
    public boolean isGiudice() {
        return currentUser != null && currentUser.isGiudice();
    }

    /**
     * Verifica se l'utente corrente è un partecipante
     *
     * @return true se l'utente è un partecipante
     */
    public boolean isPartecipante() {
        return currentUser != null && currentUser.isPartecipante();
    }
}
