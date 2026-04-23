package io;

import exceptions.DonneeInvalideException;
import exceptions.LectureException;
import exceptions.MatriculeInvalideException;
import exceptions.NoteInvalideException;
import modele.Etudiant;
import modele.Note;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LecteurCSV implements ILecteurEtudiants {

    /** Caractère utilisé pour séparer les colonnes du CSV. */
    private static final String SEPARATEUR = ";";

    /** Nombre exact de colonnes attendues par ligne. */
    private static final int NB_COLONNES = 5;

    /** Compteur des lignes rejetées lors de la dernière lecture. */
    private int lignesRejetees = 0;

    @Override
    public List<Etudiant> lire(String source) throws LectureException {

        // LinkedHashMap préserve l'ordre d'insertion (utile pour les tests).
        Map<String, Etudiant> parMatricule = new LinkedHashMap<>();
        this.lignesRejetees = 0;

        File fichier = new File(source);

        // try-with-resources : le Scanner sera fermé automatiquement,
        // même si une exception est levée à l'intérieur du bloc.
        try (Scanner lecteur = new Scanner(fichier)) {

            int numLigne = 0;
            while (lecteur.hasNextLine()) {
                String ligne = lecteur.nextLine();
                numLigne++;

                // Ligne d'en-tête ou ligne vide : on saute.
                if (numLigne == 1 || ligne.isBlank()) {
                    continue;
                }

                // Erreurs locales : on log et on continue à la ligne suivante.
                try {
                    traiterLigne(ligne, parMatricule);
                } catch (DonneeInvalideException e) {
                    lignesRejetees++;
                    System.err.println("Ligne " + numLigne
                            + " rejetée : " + e.getMessage());
                }
            }

        } catch (FileNotFoundException e) {
            // Erreur globale : on transforme en LectureException
            // (enrichie avec le chemin), en conservant la cause d'origine.
            throw new LectureException(
                    "Fichier introuvable : " + source, e);
        }

        return new ArrayList<>(parMatricule.values());
    }

    /**
     * Traite une ligne individuelle du CSV.
     */
    private void traiterLigne(String ligne,
                              Map<String, Etudiant> parMatricule)
            throws DonneeInvalideException {

        String[] champs = ligne.split(SEPARATEUR);
        if (champs.length != NB_COLONNES) {
            throw new DonneeInvalideException(
                    "Nombre de colonnes incorrect (" + champs.length
                            + " au lieu de " + NB_COLONNES + ").");
        }

        String matricule = champs[0].trim();
        String prenom    = champs[1].trim();
        String nom       = champs[2].trim();
        String cours     = champs[3].trim();
        String noteTxt   = champs[4].trim();

        // Conversion robuste : accepte le point ET la virgule comme séparateur.
        double valeur;
        try {
            valeur = Double.parseDouble(noteTxt.replace(',', '.'));
        } catch (NumberFormatException e) {
            // On transforme l'exception du JDK en exception métier.
            throw new NoteInvalideException(
                    "Valeur numérique invalide : \"" + noteTxt + "\"");
        }

        // Regroupement : on récupère l'étudiant existant, ou on le crée.
        // computeIfAbsent ne convient pas ici car le constructeur peut
        // lever une exception checked.
        Etudiant etudiant = parMatricule.get(matricule);
        if (etudiant == null) {
            etudiant = new Etudiant(matricule, prenom, nom);
            parMatricule.put(matricule, etudiant);
        }

        // Ajout de la note (le constructeur de Note valide la valeur).
        etudiant.ajouterNote(new Note(cours, valeur));
    }

    @Override
    public int getLignesRejetees() {
        return lignesRejetees;
    }
}
