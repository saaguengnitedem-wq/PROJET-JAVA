package service;

/**
 * Barème français classique des mentions sur une échelle /20.
 *
 * <ul>
 *   <li>[16 ; 20] : Très Bien</li>
 *   <li>[14 ; 16[ : Bien</li>
 *   <li>[12 ; 14[ : Assez Bien</li>
 *   <li>[10 ; 12[ : Passable</li>
 *   <li>[ 0 ; 10[ : Échec</li>
 * </ul>
 *
 * @author Aurelien et Moise
 * @version 3.0
 */
public class MentionFrancaise implements IStrategieMention {

    @Override
    public String determiner(double moyenne) {
        if (moyenne >= 16.0) return "Tres Bien";
        if (moyenne >= 14.0) return "Bien";
        if (moyenne >= 12.0) return "Assez Bien";
        if (moyenne >= 10.0) return "Passable";
        return "Echec";
    }

    @Override
    public String getNom() {
        return "Bareme francais (mentions)";
    }
}
