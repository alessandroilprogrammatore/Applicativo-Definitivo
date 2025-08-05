package implementazionePostgresDAO;

import dao.TeamDAO;
import database.ConnectionManager;
import model.Team;
import model.RichiestaJoin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione PostgreSQL dell'interfaccia TeamDAO.
 * Gestisce tutte le operazioni CRUD e specifiche per i team.
 */
public class TeamPostgresDAO implements TeamDAO {
    
    private final ConnectionManager connectionManager;
    
    /**
     * Costruttore che inizializza il connection manager
     */
    public TeamPostgresDAO() {
        this.connectionManager = ConnectionManager.getInstance();
    }

    @Override
    public int insert(Team team) {
        // TODO: Implementare query INSERT per inserire un nuovo team
        String sql = "INSERT INTO team (nome, hackathon_id, capo_team_id, dimensione_massima) " +
                    "VALUES (?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, team.getNome());
            pstmt.setInt(2, team.getHackathonId());
            pstmt.setInt(3, team.getCapoTeamId());
            pstmt.setInt(4, team.getDimensioneMassima());
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                team.setId(id);
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
    public boolean update(Team team) {
        // TODO: Implementare query UPDATE per aggiornare un team esistente
        String sql = "UPDATE team SET nome = ?, hackathon_id = ?, capo_team_id = ?, " +
                    "dimensione_massima = ? WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, team.getNome());
            pstmt.setInt(2, team.getHackathonId());
            pstmt.setInt(3, team.getCapoTeamId());
            pstmt.setInt(4, team.getDimensioneMassima());
            pstmt.setInt(5, team.getId());
            
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
        // TODO: Implementare query DELETE per eliminare un team
        String sql = "DELETE FROM team WHERE id = ?";
        
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
    public Team findById(int id) {
        // TODO: Implementare query SELECT per trovare un team per ID
        String sql = "SELECT * FROM team WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToTeam(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Team> findAll() {
        // TODO: Implementare query SELECT per trovare tutti i team
        String sql = "SELECT * FROM team ORDER BY nome";
        List<Team> teams = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                teams.add(mapResultSetToTeam(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teams;
    }

    @Override
    public List<Team> findByHackathon(int hackathonId) {
        // TODO: Implementare query per trovare team per hackathon
        String sql = "SELECT * FROM team WHERE hackathon_id = ? ORDER BY nome";
        List<Team> teams = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hackathonId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                teams.add(mapResultSetToTeam(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teams;
    }

    @Override
    public List<Team> findByMembro(int utenteId) {
        // TODO: Implementare query per trovare team di cui un utente è membro
        String sql = "SELECT t.* FROM team t " +
                    "JOIN team_membro tm ON t.id = tm.team_id " +
                    "WHERE tm.utente_id = ? ORDER BY t.nome";
        List<Team> teams = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, utenteId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                teams.add(mapResultSetToTeam(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teams;
    }

    @Override
    public List<Team> findByCapoTeam(int capoTeamId) {
        // TODO: Implementare query per trovare team di cui un utente è capo
        String sql = "SELECT * FROM team WHERE capo_team_id = ? ORDER BY nome";
        List<Team> teams = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, capoTeamId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                teams.add(mapResultSetToTeam(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teams;
    }

    @Override
    public boolean aggiungiMembro(int teamId, int utenteId) {
        // TODO: Implementare query per aggiungere membro al team
        String sql = "INSERT INTO team_membro (team_id, utente_id) VALUES (?, ?)";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, teamId);
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
    public boolean rimuoviMembro(int teamId, int utenteId) {
        // TODO: Implementare query per rimuovere membro dal team
        String sql = "DELETE FROM team_membro WHERE team_id = ? AND utente_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, teamId);
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
    public boolean isMembro(int teamId, int utenteId) {
        // TODO: Implementare query per verificare se utente è membro del team
        String sql = "SELECT COUNT(*) as count FROM team_membro WHERE team_id = ? AND utente_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, teamId);
            pstmt.setInt(2, utenteId);
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
    public boolean isCapoTeam(int teamId, int utenteId) {
        // TODO: Implementare query per verificare se utente è capo del team
        String sql = "SELECT COUNT(*) as count FROM team WHERE id = ? AND capo_team_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, teamId);
            pstmt.setInt(2, utenteId);
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
    public boolean haSpazioDisponibile(int teamId) {
        // TODO: Implementare query per verificare spazio disponibile nel team
        String sql = "SELECT t.dimensione_massima, COUNT(tm.utente_id) as membri " +
                    "FROM team t LEFT JOIN team_membro tm ON t.id = tm.team_id " +
                    "WHERE t.id = ? GROUP BY t.dimensione_massima";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, teamId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int dimensioneMassima = rs.getInt("dimensione_massima");
                int membri = rs.getInt("membri");
                return membri < dimensioneMassima;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int contaMembri(int teamId) {
        // TODO: Implementare query per contare membri del team
        String sql = "SELECT COUNT(*) as membri FROM team_membro WHERE team_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, teamId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("membri");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Integer> findMembri(int teamId) {
        // TODO: Implementare query per trovare membri del team
        String sql = "SELECT utente_id FROM team_membro WHERE team_id = ?";
        List<Integer> membri = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, teamId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                membri.add(rs.getInt("utente_id"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return membri;
    }

    @Override
    public int insertRichiestaJoin(RichiestaJoin richiesta) {
        // TODO: Implementare query INSERT per inserire richiesta di join
        String sql = "INSERT INTO richiesta_join (utente_id, team_id, messaggio_motivazionale, " +
                    "data_richiesta, stato) VALUES (?, ?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, richiesta.getUtenteId());
            pstmt.setInt(2, richiesta.getTeamId());
            pstmt.setString(3, richiesta.getMessaggioMotivazionale());
            pstmt.setTimestamp(4, Timestamp.valueOf(richiesta.getDataRichiesta()));
            pstmt.setString(5, richiesta.getStato().name());
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                richiesta.setId(id);
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
    public boolean updateRichiestaJoin(RichiestaJoin richiesta) {
        // TODO: Implementare query UPDATE per aggiornare richiesta di join
        String sql = "UPDATE richiesta_join SET utente_id = ?, team_id = ?, " +
                    "messaggio_motivazionale = ?, data_richiesta = ?, stato = ? WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, richiesta.getUtenteId());
            pstmt.setInt(2, richiesta.getTeamId());
            pstmt.setString(3, richiesta.getMessaggioMotivazionale());
            pstmt.setTimestamp(4, Timestamp.valueOf(richiesta.getDataRichiesta()));
            pstmt.setString(5, richiesta.getStato().name());
            pstmt.setInt(6, richiesta.getId());
            
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
    public List<RichiestaJoin> findRichiesteJoin(int teamId) {
        // TODO: Implementare query per trovare richieste di join per team
        String sql = "SELECT * FROM richiesta_join WHERE team_id = ? ORDER BY data_richiesta DESC";
        List<RichiestaJoin> richieste = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, teamId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                richieste.add(mapResultSetToRichiestaJoin(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return richieste;
    }

    @Override
    public List<RichiestaJoin> findRichiesteJoinInAttesa(int teamId) {
        // TODO: Implementare query per trovare richieste di join in attesa
        String sql = "SELECT * FROM richiesta_join WHERE team_id = ? AND stato = 'IN_ATTESA' " +
                    "ORDER BY data_richiesta DESC";
        List<RichiestaJoin> richieste = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, teamId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                richieste.add(mapResultSetToRichiestaJoin(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return richieste;
    }

    @Override
    public List<RichiestaJoin> findRichiesteJoinByUtente(int utenteId) {
        // TODO: Implementare query per trovare richieste di join di un utente
        String sql = "SELECT * FROM richiesta_join WHERE utente_id = ? ORDER BY data_richiesta DESC";
        List<RichiestaJoin> richieste = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, utenteId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                richieste.add(mapResultSetToRichiestaJoin(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return richieste;
    }

    @Override
    public boolean accettaRichiestaJoin(int richiestaId) {
        // TODO: Implementare query per accettare richiesta di join
        String sql = "UPDATE richiesta_join SET stato = 'ACCETTATA' WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, richiestaId);
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
    public boolean rifiutaRichiestaJoin(int richiestaId) {
        // TODO: Implementare query per rifiutare richiesta di join
        String sql = "UPDATE richiesta_join SET stato = 'RIFIUTATA' WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, richiestaId);
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
     * Mappa un ResultSet in un oggetto Team
     *
     * @param rs il ResultSet da mappare
     * @return l'oggetto Team mappato
     * @throws SQLException se si verifica un errore durante la lettura
     */
    private Team mapResultSetToTeam(ResultSet rs) throws SQLException {
        Team team = new Team(
            rs.getString("nome"),
            rs.getInt("hackathon_id"),
            rs.getInt("capo_team_id"),
            rs.getInt("dimensione_massima")
        );
        
        team.setId(rs.getInt("id"));
        return team;
    }

    /**
     * Mappa un ResultSet in un oggetto RichiestaJoin
     *
     * @param rs il ResultSet da mappare
     * @return l'oggetto RichiestaJoin mappato
     * @throws SQLException se si verifica un errore durante la lettura
     */
    private RichiestaJoin mapResultSetToRichiestaJoin(ResultSet rs) throws SQLException {
        RichiestaJoin richiesta = new RichiestaJoin(
            rs.getInt("utente_id"),
            rs.getInt("team_id"),
            rs.getString("messaggio_motivazionale")
        );
        
        richiesta.setId(rs.getInt("id"));
        richiesta.setDataRichiesta(rs.getTimestamp("data_richiesta").toLocalDateTime());
        richiesta.setStato(RichiestaJoin.StatoRichiesta.valueOf(rs.getString("stato")));
        
        return richiesta;
    }
} 