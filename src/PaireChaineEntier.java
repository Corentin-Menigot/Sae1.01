public class PaireChaineEntier {
    //attributs
    private String chaine;
    private int entier;

    //getters
    public String getChaine() {
        return chaine;
    }
    public int getEntier() {
        return entier;
    }

    //setters
    public void setEntier (int entier) {this.entier= entier;}

    public void setChaine (String chaine) {this.chaine= chaine;}

    //constructeurs
    public PaireChaineEntier (String chaine) {
        this.chaine=chaine;
    }
    public PaireChaineEntier (int entier) {
        this.entier=entier;
    }

    //méthodes
    public void incremente (int entier) {
        this.entier = this.entier + entier;
    }
    public void decremente (int entier) {
        this.entier = this.entier - entier;
    }
    public PaireChaineEntier (String chaine, int entier) {
        this.chaine=chaine;
        this.entier=entier;
    }
}
