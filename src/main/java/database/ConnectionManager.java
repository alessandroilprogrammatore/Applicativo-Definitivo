package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestisce le connessioni al database PostgreSQL.
 * Implementa il pattern Singleton per garantire una singola istanza.
 */
public class ConnectionManager {
    private static ConnectionManager instance;
    private static final String URL = "jdbc:postgresql://localhost:5432/hackathon_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "password";
    
    private Connection connection;

    /**
     * Costruttore privato per il pattern Singleton
     */
    private ConnectionManager() {
        // Inizializzazione privata
    }

    /**
     * Ottiene l'istanza singleton del ConnectionManager
     *
     * @return l'istanza del ConnectionManager
     */
    public static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    /**
     * Ottiene una connessione al database
     *
     * @return la connessione al database
     * @throws SQLException se si verifica un errore di connessione
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Carica il driver PostgreSQL
                Class.forName("org.postgresql.Driver");
                
                // Stabilisce la connessione
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                connection.setAutoCommit(false); // Gestione manuale delle transazioni
                
                System.out.println("Connessione al database stabilita con successo");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver PostgreSQL non trovato", e);
            } catch (SQLException e) {
                System.err.println("Errore durante la connessione al database: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }

    /**
     * Chiude la connessione al database
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connessione al database chiusa");
            } catch (SQLException e) {
                System.err.println("Errore durante la chiusura della connessione: " + e.getMessage());
            }
        }
    }

    /**
     * Esegue un commit della transazione corrente
     *
     * @throws SQLException se si verifica un errore durante il commit
     */
    public void commit() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.commit();
        }
    }

    /**
     * Esegue un rollback della transazione corrente
     *
     * @throws SQLException se si verifica un errore durante il rollback
     */
    public void rollback() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.rollback();
        }
    }

    /**
     * Verifica se la connessione è attiva
     *
     * @return true se la connessione è attiva
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Testa la connessione al database
     *
     * @return true se la connessione è riuscita
     */
    public boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Test connessione fallito: " + e.getMessage());
            return false;
        }
    }
} 