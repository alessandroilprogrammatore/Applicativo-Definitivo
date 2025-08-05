package implementazionePostgresDAO;

import dao.RegistrazioneDAO;
import database.ConnectionManager;
import model.Registrazione;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione PostgreSQL dell'interfaccia RegistrazioneDAO.
 * Gestisce tutte le operazioni CRUD e specifiche per le registrazioni.
 */
public class RegistrazionePostgresDAO implements RegistrazioneDAO {
    
    private final ConnectionManager connectionManager;
    
    /**
     * Costruttore che inizializza il connection manager
     */
    public RegistrazionePostgresDAO() {
        this.connectionManager = ConnectionManager.getInstance();
    }

    @Override
    public int insert(Registrazione registrazione) {
        // TODO: Implementare query INSERT per inserire una nuova registrazione
        String sql = "INSERT INTO registrazione (utente_id, hackathon_id, data_registrazione, " +
                    "ruolo, confermata) VALUES (?, ?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, registrazione.getUtenteId());
            pstmt.setInt(2, registrazione.getHackathonId());
            pstmt.setTimestamp(3, Timestamp.valueOf(registrazione.getDataRegistrazione()));
            pstmt.setString(4, registrazione.getRuolo().name());
            pstmt.setBoolean(5, registrazione.isConfermata());
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                registrazione.setId(id);
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
    public boolean update(Registrazione registrazione) {
        // TODO: Implementare query UPDATE per aggiornare una registrazione esistente
        String sql = "UPDATE registrazione SET utente_id = ?, hackathon_id = ?, " +
                    "data_registrazione = ?, ruolo = ?, confermata = ? WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, registrazione.getUtenteId());
            pstmt.setInt(2, registrazione.getHackathonId());
            pstmt.setTimestamp(3, Timestamp.valueOf(registrazione.getDataRegistrazione()));
            pstmt.setString(4, registrazione.getRuolo().name());
            pstmt.setBoolean(5, registrazione.isConfermata());
            pstmt.setInt(6, registrazione.getId());
            
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
        // TODO: Implementare query DELETE per eliminare una registrazione
        String sql = "DELETE FROM registrazione WHERE id = ?";
        
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
    public Registrazione findById(int id) {
        // TODO: Implementare query SELECT per trovare una registrazione per ID
        String sql = "SELECT * FROM registrazione WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToRegistrazione(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Registrazione> findAll() {
        // TODO: Implementare query SELECT per trovare tutte le registrazioni
        String sql = "SELECT * FROM registrazione ORDER BY data_registrazione DESC";
        List<Registrazione> registrazioni = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                registrazioni.add(mapResultSetToRegistrazione(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registrazioni;
    }

    @Override
    public List<Registrazione> findByUtente(int utenteId) {
        // TODO: Implementare query per trovare registrazioni di un utente
        String sql = "SELECT * FROM registrazione WHERE utente_id = ? ORDER BY data_registrazione DESC";
        List<Registrazione> registrazioni = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, utenteId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                registrazioni.add(mapResultSetToRegistrazione(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registrazioni;
    }

    @Override
    public List<Registrazione> findByHackathon(int hackathonId) {
        // TODO: Implementare query per trovare registrazioni di un hackathon
        String sql = "SELECT * FROM registrazione WHERE hackathon_id = ? ORDER BY data_registrazione DESC";
        List<Registrazione> registrazioni = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hackathonId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                registrazioni.add(mapResultSetToRegistrazione(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registrazioni;
    }

    @Override
    public Registrazione findByUtenteAndHackathon(int utenteId, int hackathonId) {
        // TODO: Implementare query per trovare registrazione specifica utente-hackathon
        String sql = "SELECT * FROM registrazione WHERE utente_id = ? AND hackathon_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, utenteId);
            pstmt.setInt(2, hackathonId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToRegistrazione(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Registrazione> findConfermateByHackathon(int hackathonId) {
        // TODO: Implementare query per trovare registrazioni confermate di un hackathon
        String sql = "SELECT * FROM registrazione WHERE hackathon_id = ? AND confermata = true " +
                    "ORDER BY data_registrazione DESC";
        List<Registrazione> registrazioni = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hackathonId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                registrazioni.add(mapResultSetToRegistrazione(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registrazioni;
    }

    @Override
    public List<Registrazione> findNonConfermateByHackathon(int hackathonId) {
        // TODO: Implementare query per trovare registrazioni non confermate di un hackathon
        String sql = "SELECT * FROM registrazione WHERE hackathon_id = ? AND confermata = false " +
                    "ORDER BY data_registrazione DESC";
        List<Registrazione> registrazioni = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hackathonId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                registrazioni.add(mapResultSetToRegistrazione(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registrazioni;
    }

    @Override
    public List<Registrazione> findByHackathonAndRuolo(int hackathonId, Registrazione.Ruolo ruolo) {
        // TODO: Implementare query per trovare registrazioni per ruolo in un hackathon
        String sql = "SELECT * FROM registrazione WHERE hackathon_id = ? AND ruolo = ? " +
                    "ORDER BY data_registrazione DESC";
        List<Registrazione> registrazioni = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hackathonId);
            pstmt.setString(2, ruolo.name());
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                registrazioni.add(mapResultSetToRegistrazione(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registrazioni;
    }

    @Override
    public List<Registrazione> findOrganizzatori(int hackathonId) {
        return findByHackathonAndRuolo(hackathonId, Registrazione.Ruolo.ORGANIZZATORE);
    }

    @Override
    public List<Registrazione> findGiudici(int hackathonId) {
        return findByHackathonAndRuolo(hackathonId, Registrazione.Ruolo.GIUDICE);
    }

    @Override
    public List<Registrazione> findPartecipanti(int hackathonId) {
        return findByHackathonAndRuolo(hackathonId, Registrazione.Ruolo.PARTECIPANTE);
    }

    @Override
    public boolean confermaRegistrazione(int registrazioneId) {
        // TODO: Implementare query per confermare una registrazione
        String sql = "UPDATE registrazione SET confermata = true WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, registrazioneId);
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
    public boolean isRegistrato(int utenteId, int hackathonId) {
        // TODO: Implementare query per verificare se utente è registrato
        String sql = "SELECT COUNT(*) as count FROM registrazione WHERE utente_id = ? AND hackathon_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, utenteId);
            pstmt.setInt(2, hackathonId);
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
    public boolean isConfermato(int utenteId, int hackathonId) {
        // TODO: Implementare query per verificare se utente è confermato
        String sql = "SELECT COUNT(*) as count FROM registrazione WHERE utente_id = ? AND hackathon_id = ? AND confermata = true";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, utenteId);
            pstmt.setInt(2, hackathonId);
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
    public int contaRegistrazioni(int hackathonId) {
        // TODO: Implementare query per contare registrazioni di un hackathon
        String sql = "SELECT COUNT(*) as count FROM registrazione WHERE hackathon_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hackathonId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int contaRegistrazioniConfermate(int hackathonId) {
        // TODO: Implementare query per contare registrazioni confermate di un hackathon
        String sql = "SELECT COUNT(*) as count FROM registrazione WHERE hackathon_id = ? AND confermata = true";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hackathonId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int contaRegistrazioniPerRuolo(int hackathonId, Registrazione.Ruolo ruolo) {
        // TODO: Implementare query per contare registrazioni per ruolo in un hackathon
        String sql = "SELECT COUNT(*) as count FROM registrazione WHERE hackathon_id = ? AND ruolo = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hackathonId);
            pstmt.setString(2, ruolo.name());
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Mappa un ResultSet in un oggetto Registrazione
     *
     * @param rs il ResultSet da mappare
     * @return l'oggetto Registrazione mappato
     * @throws SQLException se si verifica un errore durante la lettura
     */
    private Registrazione mapResultSetToRegistrazione(ResultSet rs) throws SQLException {
        Registrazione registrazione = new Registrazione(
            rs.getInt("utente_id"),
            rs.getInt("hackathon_id"),
            Registrazione.Ruolo.valueOf(rs.getString("ruolo"))
        );
        
        registrazione.setId(rs.getInt("id"));
        registrazione.setDataRegistrazione(rs.getTimestamp("data_registrazione").toLocalDateTime());
        registrazione.setConfermata(rs.getBoolean("confermata"));
        
        return registrazione;
    }
} 