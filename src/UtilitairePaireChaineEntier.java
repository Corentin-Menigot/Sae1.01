import java.util.ArrayList;

public class UtilitairePaireChaineEntier {

    public static int indicePourChaineV0(ArrayList<PaireChaineEntier> listePaires, String chaine ) {
        // renvoie l'indice d'une chaine ch dans vPaire triée si cette chaine est présente
        // si la chaine n'est pas présente on renvoie -1
        // version antérieure pour comparaison des temps d'exécution
        for (int i=0; i< listePaires.size(); i++) {
            if (listePaires.get(i).getChaine().equals(chaine)) {
                return i;
            }
        }
        return -1;
    }

  //   reponsesJustes.get(UtilitairePaireChaineEntier.indicePourChaine(reponsesJustes, depeches.get(i).getCategorie())).incremente(1);
    public static int indicePourChaine(ArrayList<PaireChaineEntier> vPaire, String ch) {
        // renvoie l'indice d'une chaine ch dans vPaire triée si cette chaine est présente
        // si la chaine n'est pas présente on renvoie -1
        // la recherche est dichotomique
        int inf = 0;
        int sup = vPaire.size() - 1;
        int m;
        if (vPaire.isEmpty()){
            return -1;
        } else if (vPaire.get(vPaire.size() - 1).getChaine().compareTo(ch) < 0) {
            return -1;
        } else {
            while (inf < sup) {
                m = (inf + sup) / 2;
                if (vPaire.get(m).getChaine().compareTo(ch) >= 0) {
                    sup = m;
                } else {
                    inf = m + 1;

                }
            }
            if (ch.equals(vPaire.get(inf).getChaine())) {
                return inf;
            } else {
                return -1;
            }
        }
    }

    public static int entierPourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        // renvoie l'entier associé à une chaine dans listePaires
        int i = 0;
        while (i < listePaires.size() && listePaires.get(i).getChaine().compareTo(chaine) != 0) {
            i++;
        }
        if (i == listePaires.size()) {
            return 0;
        } else {
            return listePaires.get(i).getEntier();
        }
    }

    public static String chaineMax(ArrayList<PaireChaineEntier> listePaires) {
        // renvoie la chaine qui a le plus grand nombre associé dans listePaires
        int max = listePaires.get(0).getEntier();
        String maxChaine = listePaires.get(0).getChaine();
        for (int i = 1; i < listePaires.size(); i++) {
            if (listePaires.get(i).getEntier() > max) {
                max = listePaires.get(i).getEntier();
                maxChaine = listePaires.get(i).getChaine();
            }
        }
        return maxChaine;
    }

    public static float moyenne(ArrayList<PaireChaineEntier> listePaires) {
        // renvoie la moyenne des entiers des PaireChaineEntier de listePaires
        float somme = 0f;
        for (int i=0; i< listePaires.size(); i++) {
            somme = somme + listePaires.get(i).getEntier();
        }
        return ((somme)/(listePaires.size()));
    }

    public static void insereTriPaireVerif(ArrayList<PaireChaineEntier> vPaire, PaireChaineEntier p) {
        // insére la PaireChaineEntier p dans le vecteur vPaire pour qu'il soit trié
        // p est inséré si il n'est pas déjà présent dans vPaire
        int inf = 0;
        int sup = vPaire.size() - 1;
        int m;
        if (vPaire.isEmpty() || vPaire.get(vPaire.size()-1).getChaine().compareTo(p.getChaine())<0) {
            vPaire.add(p);
        } else {
            if (vPaire.get(vPaire.size() - 1).getChaine().compareTo(p.getChaine()) < 0) {
                vPaire.add(p);
            }
            while (inf < sup) {
                m = (inf + sup) / 2;
                if (vPaire.get(m).getChaine().compareTo(p.getChaine()) >= 0) {
                    sup = m;
                } else {
                    inf = m + 1;
                }
            }
            if (vPaire.get(inf).getChaine().compareTo(p.getChaine())!=0) {
                vPaire.add(inf, p);
            }
        }
    }

    public static void triBullePaire(ArrayList<PaireChaineEntier> vPaire) {
        // Trie le vecteur vPaire avec un triBulle
        int i = 0;
        int j;
        boolean onAPermute = true;
        while (onAPermute){
            j = vPaire.size()-1;
            onAPermute = false;
            while(j>i){
                if (vPaire.get(j).getChaine().compareTo(vPaire.get(j-1).getChaine())<0){
                    PaireChaineEntier temporaire = vPaire.get(j);
                    vPaire.set(j, vPaire.get(j-1));
                    vPaire.set(j-1, temporaire);
                    onAPermute=true;
                }
                j=j-1;
            }
            i=i+1;
        }
    }
}
