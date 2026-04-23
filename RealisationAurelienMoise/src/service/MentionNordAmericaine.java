package service;

/**
 * Barème nord-américain (lettres A / B / C / D / F).
 *
 * <p>Conversion : la moyenne sur 20 est d'abord multipliée par 5
 * pour obtenir un pourcentage sur 100, puis convertie :
 * <ul>
 *   <li>≥ 90 % : A</li>
 *   <li>≥ 80 % : B</li>
 *   <li>≥ 70 % : C</li>
 *   <li>≥ 60 % : D</li>
 *   <li>&lt; 60 % : F</li>
 * </ul>
 *
 * <p>L'existence même de cette deuxième implémentation prouve que
 * le pattern Strategy fonctionne : on peut changer le barème en
 * modifiant une seule ligne dans {@code Main}.
 *
 * @author Aurelien et Moise
 * @version 3.0
 */
public class MentionNordAmericaine implements IStrategieMention {

    @Override
    public String determiner(double moyenne) {
        double pourcentage = moyenne * 5.0; // conversion /20 → /100
        if (pourcentage >= 90.0) return "A";
        if (pourcentage >= 80.0) return "B";
        if (pourcentage >= 70.0) return "C";
        if (pourcentage >= 60.0) return "D";
        return "F";
    }

    @Override
    public String getNom() {
        return "Bareme nord-americain (lettres)";
    }
}
