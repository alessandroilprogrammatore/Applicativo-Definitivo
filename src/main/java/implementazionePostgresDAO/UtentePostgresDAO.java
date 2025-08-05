package implementazionePostgresDAO;

import dao.UtenteDAO;
import database.ConnectionManager;
import model.Utente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione PostgreSQL dell'interfaccia UtenteDAO.
 * Gestisce tutte le operazioni CRUD e specifiche per gli utenti.
 */
public class UtentePostgresDAO implements UtenteDAO {
    
    private final ConnectionManager connectionManager;
    
    /**
     * Costruttore che inizializza il connection manager
     */
    public UtentePostgresDAO() {
        this.connectionManager = ConnectionManager.getInstance();
    }

    @Override
    public int insert(Utente utente) {
        // TODO: Implementare query INSERT per inserire un nuovo utente
        String sql = "INSERT INTO utente (login, password, nome, cognome, email, ruolo) " +
                    "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, utente.getLogin());
            pstmt.setString(2, utente.getPassword());
            pstmt.setString(3, utente.getNome());
            pstmt.setString(4, utente.getCognome());
            pstmt.setString(5, utente.getEmail());
            pstmt.setString(6, utente.getRuolo());
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                utente.setId(id);
                connectionManager.commit();
                return id;
            }
            
        } catch (SQLException e) {
            try {
                connectionManager.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean update(Utente utente) {
        // TODO: Implementare query UPDATE per aggiornare un utente esistente
        String sql = "UPDATE utente SET login = ?, password = ?, nome = ?, cognome = ?, " +
                    "email = ?, ruolo = ? WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, utente.getLogin());
            pstmt.setString(2, utente.getPassword());
            pstmt.setString(3, utente.getNome());
            pstmt.setString(4, utente.getCognome());
            pstmt.setString(5, utente.getEmail());
            pstmt.setString(6, utente.getRuolo());
            pstmt.setInt(7, utente.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            connectionManager.commit();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            try {
                connectionManager.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        // TODO: Implementare query DELETE per eliminare un utente
        String sql = "DELETE FROM utente WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            connectionManager.commit();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            try {
                connectionManager.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Utente findById(int id) {
        // TODO: Implementare query SELECT per trovare un utente per ID
        String sql = "SELECT * FROM utente WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUtente(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Utente> findAll() {
        // TODO: Implementare query SELECT per trovare tutti gli utenti
        String sql = "SELECT * FROM utente ORDER BY nome, cognome";
        List<Utente> utenti = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                utenti.add(mapResultSetToUtente(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utenti;
    }

    @Override
    public Utente findByLogin(String login) {
        // TODO: Implementare query per trovare utente per login
        String sql = "SELECT * FROM utente WHERE login = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUtente(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Utente findByEmail(String email) {
        // TODO: Implementare query per trovare utente per email
        String sql = "SELECT * FROM utente WHERE email = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUtente(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Utente autentica(String login, String password) {
        // TODO: Implementare autenticazione utente
        String sql = "SELECT * FROM utente WHERE login = ? AND password = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUtente(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Utente> findOrganizzatori() {
        // TODO: Implementare query per trovare organizzatori
        return findByRuolo("ORGANIZZATORE");
    }

    @Override
    public List<Utente> findGiudici() {
        // TODO: Implementare query per trovare giudici
        return findByRuolo("GIUDICE");
    }

    @Override
    public List<Utente> findPartecipanti() {
        // TODO: Implementare query per trovare partecipanti
        return findByRuolo("PARTECIPANTE");
    }

    @Override
    public List<Utente> findByRuolo(String ruolo) {
        // TODO: Implementare query per trovare utenti per ruolo
        String sql = "SELECT * FROM utente WHERE ruolo = ? ORDER BY nome, cognome";
        List<Utente> utenti = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, ruolo);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                utenti.add(mapResultSetToUtente(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utenti;
    }

    @Override
    public boolean isLoginUtilizzato(String login) {
        // TODO: Implementare verifica login utilizzato
        String sql = "SELECT COUNT(*) as count FROM utente WHERE login = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isEmailUtilizzata(String email) {
        // TODO: Implementare verifica email utilizzata
        String sql = "SELECT COUNT(*) as count FROM utente WHERE email = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean cambiaPassword(int utenteId, String nuovaPassword) {
        // TODO: Implementare cambio password
        String sql = "UPDATE utente SET password = ? WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nuovaPassword);
            pstmt.setInt(2, utenteId);
            int rowsAffected = pstmt.executeUpdate();
            connectionManager.commit();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            try {
                connectionManager.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean aggiornaRuolo(int utenteId, String nuovoRuolo) {
        // TODO: Implementare aggiornamento ruolo
        String sql = "UPDATE utente SET ruolo = ? WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nuovoRuolo);
            pstmt.setInt(2, utenteId);
            int rowsAffected = pstmt.executeUpdate();
            connectionManager.commit();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            try {
                connectionManager.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Mappa un ResultSet in un oggetto Utente
     *
     * @param rs il ResultSet da mappare
     * @return l'oggetto Utente mappato
     * @throws SQLException se si verifica un errore durante la lettura
     */
    private Utente mapResultSetToUtente(ResultSet rs) throws SQLException {
        Utente utente = new Utente(
            rs.getString("login"),
            rs.getString("password"),
            rs.getString("nome"),
            rs.getString("cognome"),
            rs.getString("email"),
            rs.getString("ruolo")
        );
        
        utente.setId(rs.getInt("id"));
        return utente;
    }
} 