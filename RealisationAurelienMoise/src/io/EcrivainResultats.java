package io;

import exceptions.EcritureException;
import modele.Etudiant;
import service.IStrategieMention;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class EcrivainResultats implements IEcrivainResultats {

    /** Séparateur utilisé pour le fichier de sortie. */
    private static final String SEP = ";";

    /** Stratégie injectée pour déterminer la mention. */
    private final IStrategieMention strategieMention;

    /**
     Construit un écrivain avec la stratégie de mention souhaitée.
     */
    public EcrivainResultats(IStrategieMention strategieMention) {
        this.strategieMention = Objects.requireNonNull(strategieMention,
                "La stratégie de mention ne peut pas être null.");
    }

    /** {@inheritDoc} */
    @Override
    public void ecrire(List<Etudiant> classement, String destination)
            throws EcritureException {

        Objects.requireNonNull(classement,  "Le classement ne peut pas être null.");
        Objects.requireNonNull(destination, "La destination ne peut pas être null.");

        // Création du dossier parent si nécessaire.
        File fichier = new File(destination);
        File parent = fichier.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        // try-with-resources : le FileWriter sera fermé automatiquement.
        try (FileWriter writer = new FileWriter(fichier)) {

            // En-tête du fichier.
            writer.write(String.join(SEP,
                    "rang", "matricule", "nom_complet", "moyenne",
                    "meilleure_note", "pire_note", "mention"));
            writer.write("\n");

            // Une ligne par étudiant.
            int rang = 1;
            for (Etudiant e : classement) {
                double moyenne = e.calculerMoyenne();

                // Locale.ROOT : le point reste le séparateur décimal
                // (compatible avec Excel/LibreOffice partout dans le monde).
                String ligne = String.format(Locale.ROOT,
                        "%d;%s;%s;%.2f;%.2f;%.2f;%s%n",
                        rang,
                        e.getMatricule(),
                        e.getNomComplet(),
                        moyenne,
                        e.meilleureNote(),
                        e.pireNote(),
                        strategieMention.determiner(moyenne));

                writer.write(ligne);
                rang++;
            }

        } catch (IOException e) {
            // On transforme IOException (JDK) en exception métier.
            throw new EcritureException(
                    "Échec d'écriture du fichier : " + destination, e);
        }
    }
}
