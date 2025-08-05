-- Database Schema for Hackathon Manager
-- PostgreSQL Database Tables

-- Create database (if not exists)
-- CREATE DATABASE hackathon_manager;

-- Connect to database
-- \c hackathon_manager;

-- Drop tables if they exist (for clean setup)
DROP TABLE IF EXISTS valutazione CASCADE;
DROP TABLE IF EXISTS progress CASCADE;
DROP TABLE IF EXISTS richiesta_join CASCADE;
DROP TABLE IF EXISTS registrazione CASCADE;
DROP TABLE IF EXISTS team CASCADE;
DROP TABLE IF EXISTS hackathon CASCADE;
DROP TABLE IF EXISTS utente CASCADE;

-- Create utente table
CREATE TABLE utente (
    id SERIAL PRIMARY KEY,
    login VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    ruolo VARCHAR(20) NOT NULL CHECK (ruolo IN ('ORGANIZZATORE', 'GIUDICE', 'PARTECIPANTE')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create hackathon table
CREATE TABLE hackathon (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    data_inizio TIMESTAMP NOT NULL,
    data_fine TIMESTAMP NOT NULL,
    sede VARCHAR(255) NOT NULL,
    is_virtuale BOOLEAN DEFAULT FALSE,
    organizzatore_id INTEGER REFERENCES utente(id),
    max_partecipanti INTEGER DEFAULT 100,
    max_team INTEGER DEFAULT 20,
    registrazioni_aperte BOOLEAN DEFAULT FALSE,
    descrizione_problema TEXT,
    evento_avviato BOOLEAN DEFAULT FALSE,
    evento_concluso BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create team table
CREATE TABLE team (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    hackathon_id INTEGER REFERENCES hackathon(id) ON DELETE CASCADE,
    capo_team_id INTEGER REFERENCES utente(id),
    dimensione_massima INTEGER DEFAULT 4,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(nome, hackathon_id)
);

-- Create team_members junction table for many-to-many relationship
CREATE TABLE team_members (
    team_id INTEGER REFERENCES team(id) ON DELETE CASCADE,
    utente_id INTEGER REFERENCES utente(id) ON DELETE CASCADE,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (team_id, utente_id)
);

-- Create registrazione table
CREATE TABLE registrazione (
    id SERIAL PRIMARY KEY,
    utente_id INTEGER REFERENCES utente(id) ON DELETE CASCADE,
    hackathon_id INTEGER REFERENCES hackathon(id) ON DELETE CASCADE,
    data_registrazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ruolo VARCHAR(20) NOT NULL CHECK (ruolo IN ('ORGANIZZATORE', 'GIUDICE', 'PARTECIPANTE')),
    confermata BOOLEAN DEFAULT FALSE,
    UNIQUE(utente_id, hackathon_id)
);

-- Create richiesta_join table
CREATE TABLE richiesta_join (
    id SERIAL PRIMARY KEY,
    utente_id INTEGER REFERENCES utente(id) ON DELETE CASCADE,
    team_id INTEGER REFERENCES team(id) ON DELETE CASCADE,
    messaggio_motivazionale TEXT,
    data_richiesta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    stato VARCHAR(20) DEFAULT 'IN_ATTESA' CHECK (stato IN ('IN_ATTESA', 'ACCETTATA', 'RIFIUTATA')),
    UNIQUE(utente_id, team_id)
);

-- Create progress table
CREATE TABLE progress (
    id SERIAL PRIMARY KEY,
    team_id INTEGER REFERENCES team(id) ON DELETE CASCADE,
    hackathon_id INTEGER REFERENCES hackathon(id) ON DELETE CASCADE,
    titolo VARCHAR(200) NOT NULL,
    descrizione TEXT,
    documento_path VARCHAR(500),
    data_caricamento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    commento_giudice TEXT,
    giudice_id INTEGER REFERENCES utente(id),
    data_commento TIMESTAMP
);

-- Create valutazione table
CREATE TABLE valutazione (
    id SERIAL PRIMARY KEY,
    giudice_id INTEGER REFERENCES utente(id) ON DELETE CASCADE,
    team_id INTEGER REFERENCES team(id) ON DELETE CASCADE,
    hackathon_id INTEGER REFERENCES hackathon(id) ON DELETE CASCADE,
    voto INTEGER NOT NULL CHECK (voto >= 0 AND voto <= 10),
    commento TEXT,
    data_valutazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(giudice_id, team_id)
);

-- Create indexes for better performance
CREATE INDEX idx_utente_login ON utente(login);
CREATE INDEX idx_utente_email ON utente(email);
CREATE INDEX idx_utente_ruolo ON utente(ruolo);

CREATE INDEX idx_hackathon_organizzatore ON hackathon(organizzatore_id);
CREATE INDEX idx_hackathon_data_inizio ON hackathon(data_inizio);
CREATE INDEX idx_hackathon_registrazioni_aperte ON hackathon(registrazioni_aperte);
CREATE INDEX idx_hackathon_evento_avviato ON hackathon(evento_avviato);

CREATE INDEX idx_team_hackathon ON team(hackathon_id);
CREATE INDEX idx_team_capo_team ON team(capo_team_id);

CREATE INDEX idx_team_members_team ON team_members(team_id);
CREATE INDEX idx_team_members_utente ON team_members(utente_id);

CREATE INDEX idx_registrazione_utente ON registrazione(utente_id);
CREATE INDEX idx_registrazione_hackathon ON registrazione(hackathon_id);
CREATE INDEX idx_registrazione_confermata ON registrazione(confermata);

CREATE INDEX idx_richiesta_join_utente ON richiesta_join(utente_id);
CREATE INDEX idx_richiesta_join_team ON richiesta_join(team_id);
CREATE INDEX idx_richiesta_join_stato ON richiesta_join(stato);

CREATE INDEX idx_progress_team ON progress(team_id);
CREATE INDEX idx_progress_hackathon ON progress(hackathon_id);
CREATE INDEX idx_progress_data_caricamento ON progress(data_caricamento);

CREATE INDEX idx_valutazione_giudice ON valutazione(giudice_id);
CREATE INDEX idx_valutazione_team ON valutazione(team_id);
CREATE INDEX idx_valutazione_hackathon ON valutazione(hackathon_id);
CREATE INDEX idx_valutazione_voto ON valutazione(voto);

-- Insert sample data for testing
INSERT INTO utente (login, password, nome, cognome, email, ruolo) VALUES
('admin', 'admin123', 'Admin', 'System', 'admin@hackathon.com', 'ORGANIZZATORE'),
('giudice1', 'giudice123', 'Mario', 'Rossi', 'mario.rossi@hackathon.com', 'GIUDICE'),
('giudice2', 'giudice123', 'Giulia', 'Bianchi', 'giulia.bianchi@hackathon.com', 'GIUDICE'),
('partecipante1', 'part123', 'Luca', 'Verdi', 'luca.verdi@hackathon.com', 'PARTECIPANTE'),
('partecipante2', 'part123', 'Anna', 'Neri', 'anna.neri@hackathon.com', 'PARTECIPANTE');

-- Insert sample hackathon
INSERT INTO hackathon (nome, data_inizio, data_fine, sede, is_virtuale, organizzatore_id, max_partecipanti, max_team) VALUES
('Hackathon 2024', '2024-06-15 09:00:00', '2024-06-17 18:00:00', 'Milano, Italia', FALSE, 1, 50, 10);

-- Insert sample registrations
INSERT INTO registrazione (utente_id, hackathon_id, ruolo, confermata) VALUES
(2, 1, 'GIUDICE', TRUE),
(3, 1, 'GIUDICE', TRUE),
(4, 1, 'PARTECIPANTE', TRUE),
(5, 1, 'PARTECIPANTE', TRUE);

-- Insert sample team
INSERT INTO team (nome, hackathon_id, capo_team_id, dimensione_massima) VALUES
('Team Alpha', 1, 4, 4);

-- Insert team member
INSERT INTO team_members (team_id, utente_id) VALUES
(1, 4),
(1, 5);

COMMIT; 