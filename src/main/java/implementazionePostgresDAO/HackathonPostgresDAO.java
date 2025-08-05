package implementazionePostgresDAO;

import dao.HackathonDAO;
import database.ConnectionManager;
import model.Hackathon;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione PostgreSQL dell'interfaccia HackathonDAO.
 * Gestisce tutte le operazioni CRUD e specifiche per gli hackathon.
 */
public class HackathonPostgresDAO implements HackathonDAO {
    
    private final ConnectionManager connectionManager;
    
    /**
     * Costruttore che inizializza il connection manager
     */
    public HackathonPostgresDAO() {
        this.connectionManager = ConnectionManager.getInstance();
    }

    @Override
    public int insert(Hackathon hackathon) {
        // TODO: Implementare query INSERT per inserire un nuovo hackathon
        String sql = "INSERT INTO hackathon (nome, data_inizio, data_fine, sede, is_virtuale, " +
                    "organizzatore_id, max_partecipanti, max_team, registrazioni_aperte, " +
                    "descrizione_problema, evento_avviato, evento_concluso) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // TODO: Impostare i parametri della query
            pstmt.setString(1, hackathon.getNome());
            pstmt.setTimestamp(2, Timestamp.valueOf(hackathon.getDataInizio()));
            pstmt.setTimestamp(3, Timestamp.valueOf(hackathon.getDataFine()));
            pstmt.setString(4, hackathon.getSede());
            pstmt.setBoolean(5, hackathon.isVirtuale());
            pstmt.setInt(6, hackathon.getOrganizzatoreId());
            pstmt.setInt(7, hackathon.getMaxPartecipanti());
            pstmt.setInt(8, hackathon.getMaxTeam());
            pstmt.setBoolean(9, hackathon.isRegistrazioniAperte());
            pstmt.setString(10, hackathon.getDescrizioneProblema());
            pstmt.setBoolean(11, hackathon.isEventoAvviato());
            pstmt.setBoolean(12, hackathon.isEventoConcluso());
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                hackathon.setId(id);
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
    public boolean update(Hackathon hackathon) {
        // TODO: Implementare query UPDATE per aggiornare un hackathon esistente
        String sql = "UPDATE hackathon SET nome = ?, data_inizio = ?, data_fine = ?, sede = ?, " +
                    "is_virtuale = ?, organizzatore_id = ?, max_partecipanti = ?, max_team = ?, " +
                    "registrazioni_aperte = ?, descrizione_problema = ?, evento_avviato = ?, " +
                    "evento_concluso = ? WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // TODO: Impostare i parametri della query
            pstmt.setString(1, hackathon.getNome());
            pstmt.setTimestamp(2, Timestamp.valueOf(hackathon.getDataInizio()));
            pstmt.setTimestamp(3, Timestamp.valueOf(hackathon.getDataFine()));
            pstmt.setString(4, hackathon.getSede());
            pstmt.setBoolean(5, hackathon.isVirtuale());
            pstmt.setInt(6, hackathon.getOrganizzatoreId());
            pstmt.setInt(7, hackathon.getMaxPartecipanti());
            pstmt.setInt(8, hackathon.getMaxTeam());
            pstmt.setBoolean(9, hackathon.isRegistrazioniAperte());
            pstmt.setString(10, hackathon.getDescrizioneProblema());
            pstmt.setBoolean(11, hackathon.isEventoAvviato());
            pstmt.setBoolean(12, hackathon.isEventoConcluso());
            pstmt.setInt(13, hackathon.getId());
            
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
        // TODO: Implementare query DELETE per eliminare un hackathon
        String sql = "DELETE FROM hackathon WHERE id = ?";
        
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
    public Hackathon findById(int id) {
        // TODO: Implementare query SELECT per trovare un hackathon per ID
        String sql = "SELECT * FROM hackathon WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToHackathon(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Hackathon> findAll() {
        // TODO: Implementare query SELECT per trovare tutti gli hackathon
        String sql = "SELECT * FROM hackathon ORDER BY data_inizio DESC";
        List<Hackathon> hackathons = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                hackathons.add(mapResultSetToHackathon(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hackathons;
    }

    @Override
    public List<Hackathon> findByOrganizzatore(int organizzatoreId) {
        // TODO: Implementare query per trovare hackathon per organizzatore
        String sql = "SELECT * FROM hackathon WHERE organizzatore_id = ? ORDER BY data_inizio DESC";
        List<Hackathon> hackathons = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, organizzatoreId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                hackathons.add(mapResultSetToHackathon(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hackathons;
    }

    @Override
    public List<Hackathon> findConRegistrazioniAperte() {
        // TODO: Implementare query per trovare hackathon con registrazioni aperte
        String sql = "SELECT * FROM hackathon WHERE registrazioni_aperte = true ORDER BY data_inizio";
        List<Hackathon> hackathons = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                hackathons.add(mapResultSetToHackathon(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hackathons;
    }

    @Override
    public List<Hackathon> findInCorso() {
        // TODO: Implementare query per trovare hackathon in corso
        String sql = "SELECT * FROM hackathon WHERE evento_avviato = true AND evento_concluso = false " +
                    "AND data_inizio <= NOW() AND data_fine >= NOW() ORDER BY data_inizio";
        List<Hackathon> hackathons = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                hackathons.add(mapResultSetToHackathon(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hackathons;
    }

    @Override
    public List<Hackathon> findConclusi() {
        // TODO: Implementare query per trovare hackathon conclusi
        String sql = "SELECT * FROM hackathon WHERE evento_concluso = true ORDER BY data_fine DESC";
        List<Hackathon> hackathons = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                hackathons.add(mapResultSetToHackathon(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hackathons;
    }

    @Override
    public boolean apriRegistrazioni(int hackathonId) {
        // TODO: Implementare query per aprire le registrazioni
        String sql = "UPDATE hackathon SET registrazioni_aperte = true WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hackathonId);
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
    public boolean chiudiRegistrazioni(int hackathonId) {
        // TODO: Implementare query per chiudere le registrazioni
        String sql = "UPDATE hackathon SET registrazioni_aperte = false WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hackathonId);
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
    public boolean avviaHackathon(int hackathonId, String descrizioneProblema) {
        // TODO: Implementare query per avviare l'hackathon
        String sql = "UPDATE hackathon SET evento_avviato = true, descrizione_problema = ? WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, descrizioneProblema);
            pstmt.setInt(2, hackathonId);
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
    public boolean concludeHackathon(int hackathonId) {
        // TODO: Implementare query per concludere l'hackathon
        String sql = "UPDATE hackathon SET evento_concluso = true WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hackathonId);
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
    public boolean haRaggiuntoLimitePartecipanti(int hackathonId) {
        // TODO: Implementare logica per verificare limite partecipanti
        // Questa query dovrebbe confrontare il numero di partecipanti registrati con max_partecipanti
        String sql = "SELECT COUNT(*) as partecipanti, max_partecipanti FROM registrazione r " +
                    "JOIN hackathon h ON r.hackathon_id = h.id " +
                    "WHERE h.id = ? AND r.confermata = true GROUP BY h.max_partecipanti";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hackathonId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int partecipanti = rs.getInt("partecipanti");
                int maxPartecipanti = rs.getInt("max_partecipanti");
                return partecipanti >= maxPartecipanti;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean haRaggiuntoLimiteTeam(int hackathonId) {
        // TODO: Implementare logica per verificare limite team
        String sql = "SELECT COUNT(*) as team, max_team FROM team t " +
                    "JOIN hackathon h ON t.hackathon_id = h.id " +
                    "WHERE h.id = ? GROUP BY h.max_team";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hackathonId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int team = rs.getInt("team");
                int maxTeam = rs.getInt("max_team");
                return team >= maxTeam;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int contaPartecipanti(int hackathonId) {
        // TODO: Implementare query per contare partecipanti
        String sql = "SELECT COUNT(*) as partecipanti FROM registrazione " +
                    "WHERE hackathon_id = ? AND confermata = true";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hackathonId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("partecipanti");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int contaTeam(int hackathonId) {
        // TODO: Implementare query per contare team
        String sql = "SELECT COUNT(*) as team FROM team WHERE hackathon_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hackathonId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("team");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Mappa un ResultSet in un oggetto Hackathon
     *
     * @param rs il ResultSet da mappare
     * @return l'oggetto Hackathon mappato
     * @throws SQLException se si verifica un errore durante la lettura
     */
    private Hackathon mapResultSetToHackathon(ResultSet rs) throws SQLException {
        Hackathon hackathon = new Hackathon(
            rs.getString("nome"),
            rs.getTimestamp("data_inizio").toLocalDateTime(),
            rs.getString("sede"),
            rs.getBoolean("is_virtuale"),
            rs.getInt("organizzatore_id"),
            rs.getInt("max_partecipanti"),
            rs.getInt("max_team")
        );
        
        hackathon.setId(rs.getInt("id"));
        hackathon.setDataFine(rs.getTimestamp("data_fine").toLocalDateTime());
        hackathon.setRegistrazioniAperte(rs.getBoolean("registrazioni_aperte"));
        hackathon.setDescrizioneProblema(rs.getString("descrizione_problema"));
        hackathon.setEventoAvviato(rs.getBoolean("evento_avviato"));
        hackathon.setEventoConcluso(rs.getBoolean("evento_concluso"));
        
        return hackathon;
    }
} 