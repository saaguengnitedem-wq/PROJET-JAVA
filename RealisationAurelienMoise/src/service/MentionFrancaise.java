package service;

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
