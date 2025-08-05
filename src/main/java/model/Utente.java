package model;

/**
 * Rappresenta un utente del sistema Hackathon Manager.
 * Può essere organizzatore, giudice o partecipante.
 */
public class Utente {
    private int id;
    private String login;
    private String password;
    private String nome;
    private String cognome;
    private String email;
    private String ruolo; // ORGANIZZATORE, GIUDICE, PARTECIPANTE

    /**
     * Costruttore per creare un nuovo utente
     *
     * @param login    il login dell'utente
     * @param password la password dell'utente
     * @param nome     il nome dell'utente
     * @param cognome  il cognome dell'utente
     * @param email    l'email dell'utente
     * @param ruolo    il ruolo dell'utente
     */
    public Utente(String login, String password, String nome, String cognome, String email, String ruolo) {
        this.login = login;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.ruolo = ruolo;
    }

    /**
     * Costruttore di default per compatibilità
     *
     * @param login    the login
     * @param password the password
     */
    public Utente(String login, String password) {
        this(login, password, "", "", "", "PARTECIPANTE");
    }

    /**
     * Verifica se l'utente è un organizzatore
     *
     * @return true se l'utente è un organizzatore
     */
    public boolean isOrganizzatore() {
        return "ORGANIZZATORE".equals(ruolo);
    }

    /**
     * Verifica se l'utente è un giudice
     *
     * @return true se l'utente è un giudice
     */
    public boolean isGiudice() {
        return "GIUDICE".equals(ruolo);
    }

    /**
     * Verifica se l'utente è un partecipante
     *
     * @return true se l'utente è un partecipante
     */
    public boolean isPartecipante() {
        return "PARTECIPANTE".equals(ruolo);
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getRuolo() { return ruolo; }
    public void setRuolo(String ruolo) { this.ruolo = ruolo; }

    @Override
    public String toString() {
        return "Utente{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", email='" + email + '\'' +
                ", ruolo='" + ruolo + '\'' +
                '}';
    }
}
