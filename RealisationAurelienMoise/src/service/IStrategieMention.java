package service;

/**
 * INTERFACE représentant une stratégie d'attribution de mention
 * à partir d'une moyenne.
 *
 * <p>Ce design suit le <b>Strategy Pattern</b> et met en œuvre :
 * <ul>
 *   <li><b>Principe O de SOLID</b> (Open/Closed) : ajouter un nouveau
 *       barème (ex : nord-américain, luxembourgeois, sur 100, etc.)
 *       se fait en créant une nouvelle classe qui implémente cette
 *       interface, SANS modifier le code existant.</li>
 *   <li><b>Principe I de SOLID</b> (Interface Segregation) :
 *       l'interface reste minimale (deux méthodes seulement).</li>
 * </ul>
 *
 * <p>Deux implémentations sont fournies :
 * {@link MentionFrancaise} et {@link MentionNordAmericaine}.
 *
 * @author Aurelien et Moise
 * @version 3.0
 */
public interface IStrategieMention {

    /**
     * Détermine la mention correspondant à une moyenne donnée.
     *
     * @param moyenne moyenne sur 20
     * @return libellé de la mention (ex : "Très Bien", "A", etc.)
     */
    String determiner(double moyenne);

    /**
     * @return nom lisible du barème (pour affichage)
     */
    String getNom();
}
