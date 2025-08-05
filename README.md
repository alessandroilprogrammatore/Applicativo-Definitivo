# Hackathon Manager

Un sistema completo per la gestione di hackathon con architettura MVC in Java.

## 📋 Descrizione

Hackathon Manager è un'applicazione Java Swing che permette di gestire eventi hackathon con diverse funzionalità per organizzatori, giudici e partecipanti.

### Funzionalità Principali

- **Gestione Eventi**: Creazione e gestione di hackathon
- **Gestione Utenti**: Registrazione e autenticazione con ruoli (Organizzatore, Giudice, Partecipante)
- **Gestione Team**: Creazione e gestione di team con richieste di join
- **Gestione Progressi**: Caricamento e commenti sui progressi dei team
- **Sistema di Valutazione**: Assegnazione voti e calcolo classifiche

## 🏗️ Architettura

Il progetto segue l'architettura MVC (Model-View-Controller):

```
src/main/java/
├── model/                    # Entità del dominio
│   ├── Utente.java
│   ├── Hackathon.java
│   ├── Team.java
│   ├── Registrazione.java
│   ├── Progress.java
│   ├── Valutazione.java
│   └── RichiestaJoin.java
├── controller/               # Logica di business
│   └── Controller.java
├── gui/                     # Interfaccia utente Swing
│   ├── MainFrame.java
│   ├── LoginPanel.java
│   ├── EventiPanel.java
│   ├── TeamPanel.java
│   ├── RegistrazioniPanel.java
│   ├── ValutazioniPanel.java
│   └── UtentePanel.java
├── dao/                     # Interfacce Data Access Object
│   ├── HackathonDAO.java
│   ├── UtenteDAO.java
│   ├── TeamDAO.java
│   ├── RegistrazioneDAO.java
│   ├── ProgressDAO.java
│   └── ValutazioneDAO.java
├── implementazionePostgresDAO/  # Implementazioni PostgreSQL
│   ├── HackathonPostgresDAO.java
│   ├── UtentePostgresDAO.java
│   ├── TeamPostgresDAO.java
│   ├── RegistrazionePostgresDAO.java
│   ├── ProgressPostgresDAO.java
│   └── ValutazionePostgresDAO.java
├── database/                # Gestione connessioni database
│   └── ConnectionManager.java
└── Main.java               # Entry point dell'applicazione
```

## 🛠️ Tecnologie Utilizzate

- **Java 8+**: Linguaggio di programmazione
- **Maven**: Build automation e gestione dipendenze
- **PostgreSQL**: Database relazionale
- **JDBC**: Connessione al database
- **Java Swing**: Interfaccia grafica
- **MVC Pattern**: Architettura del software

## 📦 Dipendenze

Il progetto utilizza le seguenti dipendenze Maven:

```xml
<dependencies>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <version>42.7.5</version>
    </dependency>
</dependencies>
```

## 🚀 Installazione e Configurazione

### Prerequisiti

1. **Java 8 o superiore**
2. **Maven 3.6+**
3. **PostgreSQL 12+**

### Setup Database

1. **Installa PostgreSQL** e crea un database:
   ```sql
   CREATE DATABASE hackathon_manager;
   ```

2. **Esegui lo script di setup**:
   ```bash
   psql -d hackathon_manager -f database_schema.sql
   ```

3. **Configura le credenziali** nel file `ConnectionManager.java`:
   ```java
   private static final String URL = "jdbc:postgresql://localhost:5432/hackathon_manager";
   private static final String USERNAME = "your_username";
   private static final String PASSWORD = "your_password";
   ```

### Compilazione e Esecuzione

1. **Clona il repository**:
   ```bash
   git clone <repository-url>
   cd Applicativo-Definitivo
   ```

2. **Compila il progetto**:
   ```bash
   mvn clean compile
   ```

3. **Esegui l'applicazione**:
   ```bash
   mvn exec:java -Dexec.mainClass="Main"
   ```

## 👥 Ruoli e Permessi

### Organizzatore
- Creare e gestire hackathon
- Aprire/chiudere registrazioni
- Selezionare giudici
- Avviare/concludere eventi

### Giudice
- Commentare progressi dei team
- Assegnare voti (0-10)
- Visualizzare classifiche

### Partecipante
- Registrarsi agli hackathon
- Creare o unirsi a team
- Caricare progressi
- Visualizzare commenti

## 🗄️ Schema Database

### Tabelle Principali

- **utente**: Gestione utenti e ruoli
- **hackathon**: Eventi hackathon
- **team**: Team partecipanti
- **registrazione**: Iscrizioni agli eventi
- **progress**: Progressi dei team
- **valutazione**: Voti dei giudici
- **richiesta_join**: Richieste di adesione ai team

### Relazioni

- Un utente può partecipare a più hackathon
- Un hackathon può avere più team
- Un team può avere più membri
- Un giudice può valutare più team
- Un team può caricare più progressi

## 🔧 Configurazione Avanzata

### Personalizzazione Database

Modifica il file `ConnectionManager.java` per cambiare:
- Host del database
- Porta di connessione
- Nome database
- Credenziali

### Personalizzazione GUI

I pannelli Swing sono modulari e possono essere personalizzati:
- Colori e temi
- Layout e componenti
- Validazione input
- Messaggi di errore

## 📝 TODO e Sviluppi Futuri

### Implementazioni da Completare

- [ ] **Error Handling**: Gestione completa degli errori
- [ ] **Logging**: Sistema di logging strutturato
- [ ] **Validazione**: Validazione input più robusta
- [ ] **Testing**: Test unitari e di integrazione
- [ ] **Documentazione**: Javadoc completo
- [ ] **GUI Enhancement**: Miglioramenti interfaccia
- [ ] **Performance**: Ottimizzazioni query database
- [ ] **Security**: Crittografia password
- [ ] **Backup**: Sistema di backup automatico

### Funzionalità Aggiuntive

- [ ] **Notifiche**: Sistema di notifiche in tempo reale
- [ ] **Report**: Generazione report PDF
- [ ] **API REST**: Interfaccia REST per integrazioni
- [ ] **Mobile**: App mobile complementare
- [ ] **Analytics**: Dashboard analitiche
- [ ] **Multi-tenancy**: Supporto multi-organizzazione

## 🐛 Risoluzione Problemi

### Problemi Comuni

1. **Connessione Database**:
   - Verifica che PostgreSQL sia in esecuzione
   - Controlla credenziali in `ConnectionManager`
   - Verifica che il database esista

2. **Compilazione**:
   - Assicurati di avere Java 8+
   - Verifica che Maven sia installato
   - Controlla le dipendenze nel `pom.xml`

3. **Runtime**:
   - Verifica i log per errori specifici
   - Controlla la connessione al database
   - Verifica i permessi utente

## 📄 Licenza

Questo progetto è rilasciato sotto licenza MIT.

## 👨‍💻 Contributi

Per contribuire al progetto:

1. Fork del repository
2. Crea un branch per la feature
3. Implementa le modifiche
4. Aggiungi test se necessario
5. Crea una Pull Request

## 📞 Supporto

Per supporto tecnico o domande:
- Apri una Issue su GitHub
- Contatta il team di sviluppo
- Consulta la documentazione

---

**Versione**: 1.0.0  
**Data**: 2024  
**Autori**: Team Hackathon Manager 