import gui.MainFrame;

import javax.swing.*;

/**
 * Classe principale per avviare l'applicazione Hackathon Manager.
 */
public class Main {
    
    /**
     * Metodo principale per avviare l'applicazione
     *
     * @param args argomenti da riga di comando (non utilizzati)
     */
    public static void main(String[] args) {
        // Imposta il look and feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            System.err.println("Errore nell'impostazione del look and feel: " + e.getMessage());
        }
        
        // Avvia l'applicazione Swing nell'Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
                System.out.println("Hackathon Manager avviato con successo!");
            } catch (Exception e) {
                System.err.println("Errore nell'avvio dell'applicazione: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Errore nell'avvio dell'applicazione: " + e.getMessage(), 
                    "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}