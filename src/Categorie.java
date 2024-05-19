import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Categorie {

    private String nom; // le nom de la catégorie p.ex : sport, politique,...
    private ArrayList<PaireChaineEntier> lexique; //le lexique de la catégorie

    // constructeur
    public Categorie(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public  ArrayList<PaireChaineEntier> getLexique() {
        return lexique;
    }


    // initialisation du lexique de la catégorie à partir du contenu d'un fichier texte
    public void initLexique(String nomFichier) {
        // initialise le lexique de la catégorie en prenant un fichier en entrée pour le remplir
        try {
            // lecture du fichier d'entrée
            lexique= new ArrayList<>();
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                // recherche de ":" pour séparer la chaine de l'entier pour les rentrer séparement dans lexique
                String ligne=scanner.nextLine();
                int separateur= ligne.indexOf(":");
                String mot= ligne.substring(0, separateur);
                int poid= parseInt(ligne.substring(separateur+1, separateur+2));
                PaireChaineEntier unePaire= new PaireChaineEntier(mot, poid);
                lexique.add(unePaire);
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //calcul du score d'une dépêche pour la catégorie
    public int scoreV0(Depeche d) {
        // renvoie le score d'une dépêche pour cette catégorie
        ArrayList<String> mots = d.getMots();
        int score = 0 ;
        for (int i=0; i < mots.size(); i++) {
            for (int j=0; j < lexique.size(); j++) {
                if (mots.get(i).equals(lexique.get(j).getChaine())) {
                    score = score + lexique.get(j).getEntier();
                }
            }
        }
        return score;
    }

    public int score(Depeche d) {
        // renvoie le score d'une dépêche pour cette catégorie
        ArrayList<String> mots = d.getMots();
        int score = 0 ;
        for (int i=0; i < mots.size(); i++) {
            int position= UtilitairePaireChaineEntier.indicePourChaine(lexique, mots.get(i));
            if (position!=-1){
                score=score+lexique.get(position).getEntier();
            }
        }
        return score;
    }


}
