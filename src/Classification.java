


import javax.sound.midi.Soundbank;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory ;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public class Classification {

    private static ArrayList<Depeche> lectureDepeches(String nomFichier) {
        //creation d'un tableau de dépêches
        ArrayList<Depeche> depeches = new ArrayList<>();
        try {
            // lecture du fichier d'entrée
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                String id = ligne.substring(3);
                ligne = scanner.nextLine();
                String date = ligne.substring(3);
                ligne = scanner.nextLine();
                String categorie = ligne.substring(3);
                ligne = scanner.nextLine();
                String lignes = ligne.substring(3);
                while (scanner.hasNextLine() && !ligne.equals("")) {
                    ligne = scanner.nextLine();
                    if (!ligne.equals("")) {
                        lignes = lignes + '\n' + ligne;
                    }
                }
                Depeche uneDepeche = new Depeche(id, date, categorie, lignes);
                depeches.add(uneDepeche);
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return depeches;
    }

    public static void classementDepechesV0(ArrayList<Depeche> depeches, ArrayList<Categorie> categories, String nomfichier) {
        // renvoie dans un fichier la catégorie d'une dépêche déterminé par le programme
        // ainsi que les pourcentages de bonnes réponses ( quand la catégorie est effectivement correcte )
        long startTime = System.currentTimeMillis();
        ArrayList<String> reponses = new ArrayList<>();
        // ArrayList pour stocker la catégorie déterminée par le programme pour chaque dépêche dans l'ordre des dépêches
        ArrayList<PaireChaineEntier> scoreParCat = new ArrayList<>();
        // ArrayList pour stocker temporairement les scores d'une dépêche pour chaque catégorie
        for (int i=0; i<categories.size(); i++){
            scoreParCat.add(new PaireChaineEntier (categories.get(i).getNom(), 0));
        }
        for (int i=0; i<depeches.size(); i++){
            // parcours des dépêches pour déterminer leur catégorie
            for (int j=0; j<categories.size(); j++){
                // mise à jour du score de chaque catégorie dans le vecteur scoreParCat pour cette dépêche
                scoreParCat.get(j).setEntier(categories.get(j).scoreV0(depeches.get(i)));
            }
            reponses.add(UtilitairePaireChaineEntier.chaineMax(scoreParCat));
        }
        ArrayList<PaireChaineEntier> reponsesJustes = new ArrayList<>();
        // ArrayList pour stocker le nombre de réponses qui sont bonnes par catégories
        ArrayList<PaireChaineEntier> nbDepechesCat = new ArrayList<>();
        // Arraylist contenant le nombre de dépêches de chaque catégorie, pour calculer un pourcentage de bonnes réponses
        for (int i=0; i<categories.size(); i++){
            reponsesJustes.add(new PaireChaineEntier (categories.get(i).getNom(), 0));
            nbDepechesCat.add(new PaireChaineEntier (categories.get(i).getNom(), 0));
        }
        for (int i=0; i<depeches.size(); i++){
            // on incrémente pour chaque catégorie le nombre de bonnes réponses ainsi que le nombre total de dépêches de chaque catégorie
            if (depeches.get(i).getCategorie().equals(reponses.get(i))){
                // vérification de si la réponse est bonne
                reponsesJustes.get(UtilitairePaireChaineEntier.indicePourChaineV0(reponsesJustes, depeches.get(i).getCategorie())).incremente(1);
            }
            nbDepechesCat.get(UtilitairePaireChaineEntier.indicePourChaineV0(nbDepechesCat, depeches.get(i).getCategorie())).incremente(1);
        }
        for (int i=0; i<reponsesJustes.size(); i++){
            // transformation du nombre de bonnes réponses en proportions de réponses bonne par rapport au nombre de dépêches de cahque catégorie
            reponsesJustes.get(i).setEntier((100*(reponsesJustes.get(i).getEntier()))/nbDepechesCat.get(i).getEntier());
        }
        try {
            // écriture dans le fichier de réponses
            FileWriter file = new FileWriter(nomfichier);
            for (int i=0; i<reponses.size(); i++) {
                file.write((i+1)+":"+reponses.get(i)+"\n");
            }
            for (int i=0; i<reponsesJustes.size(); i++){
                file.write(reponsesJustes.get(i).getChaine()+": "+reponsesJustes.get(i).getEntier()+"%\n");
            }
            file.write("Moyenne: "+UtilitairePaireChaineEntier.moyenne(reponsesJustes)+"%\n");
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Temps pour la génération du fichier de résultats: "+(endTime-startTime)+"ms");
    }

    public static void classementDepeches(ArrayList<Depeche> depeches, ArrayList<Categorie> categories, String nomfichier) {
        // renvoie dans un fichier la catégorie d'une dépêche déterminé par le programme
        // ainsi que les pourcentages de bonnes réponses ( quand la catégorie est effectivement correcte )
        long startTime = System.currentTimeMillis();
        ArrayList<String> reponses = new ArrayList<>();
        // ArrayList pour stocker la catégorie déterminée par le programme pour chaque dépêche dans l'ordre des dépêches
        ArrayList<PaireChaineEntier> scoreParCat = new ArrayList<>();
        // ArrayList pour stocker temporairement les scores d'une dépêche pour chaque catégorie
        for (int i=0; i<categories.size(); i++){
            scoreParCat.add(new PaireChaineEntier (categories.get(i).getNom(), 0));
        }
        for (int i=0; i<depeches.size(); i++){
            // parcours des dépêches pour déterminer leur catégorie
            for (int j=0; j<categories.size(); j++){
                // mise à jour du score de chaque catégorie dans le vecteur scoreParCat pour cette dépêche
                scoreParCat.get(j).setEntier(categories.get(j).score(depeches.get(i)));
            }
            reponses.add(UtilitairePaireChaineEntier.chaineMax(scoreParCat));
        }
        ArrayList<PaireChaineEntier> reponsesJustes = new ArrayList<>();
        // ArrayList pour stocker le nombre de réponses qui sont bonnes par catégories
        ArrayList<PaireChaineEntier> nbDepechesCat = new ArrayList<>();
        // Arraylist contenant le nombre de dépêches de chaque catégorie, pour calculer un pourcentage de bonnes réponses
        for (int i=0; i<categories.size(); i++){
            UtilitairePaireChaineEntier.insereTriPaireVerif(reponsesJustes, new PaireChaineEntier (categories.get(i).getNom(), 0));
            UtilitairePaireChaineEntier.insereTriPaireVerif(nbDepechesCat, new PaireChaineEntier (categories.get(i).getNom(), 0));
        }
        for (int i=0; i<depeches.size(); i++){
            // on incrémente pour chaque catégorie le nombre de bonnes réponses ainsi que le nombre total de dépêches de chaque catégorie
            if (depeches.get(i).getCategorie().equals(reponses.get(i))){
                // vérification de si la réponse est bonne
                reponsesJustes.get(UtilitairePaireChaineEntier.indicePourChaine(reponsesJustes, depeches.get(i).getCategorie())).incremente(1);
            }
            nbDepechesCat.get(UtilitairePaireChaineEntier.indicePourChaine(nbDepechesCat, depeches.get(i).getCategorie())).incremente(1);
        }
        for (int i=0; i<reponsesJustes.size(); i++){
            // transformation du nombre de bonnes réponses en proportions de réponses bonne par rapport au nombre de dépêches de cahque catégorie
            reponsesJustes.get(i).setEntier((100*(reponsesJustes.get(i).getEntier()))/nbDepechesCat.get(i).getEntier());
        }
        try {
            // écriture dans le fichier de réponses
            FileWriter file = new FileWriter(nomfichier);
            for (int i=0; i<reponses.size(); i++) {
                file.write((i+1)+":"+reponses.get(i)+"\n");
            }
            for (int i=0; i<reponsesJustes.size(); i++){
                file.write(reponsesJustes.get(i).getChaine()+": "+reponsesJustes.get(i).getEntier()+"%\n");
            }
            file.write("Moyenne: "+UtilitairePaireChaineEntier.moyenne(reponsesJustes)+"%\n");
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Temps pour la génération du fichier de résultats: "+(endTime-startTime)+"ms");
    }

    public static ArrayList<PaireChaineEntier> initDicoV0(ArrayList<Depeche> depeches, String categorie) {
        // initialise un ArrayList<PaireChaineEntier> avec les mots présents dans les depeche de l'ArrayList depeches
        // si un mot est déjà présent dans l'ArrayList<PaireChaineEntier> on ne le rajoute pas
        // version antérieure pour comparaison des temps d'exécution
        ArrayList<PaireChaineEntier> resultat = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < depeches.size(); i++) {
            //parcours des dépêches de l'ArrayList depeches
            if (depeches.get(i).getCategorie().compareTo(categorie) == 0) {
                ArrayList<String> motsDepeche = depeches.get(i).getMots();
                for (int j = 0; j < motsDepeche.size(); j++) {
                    //parcours du contenu d'une dépêche mot par mot
                    int k = 0;
                    while (k < resultat.size() && resultat.get(k).getChaine().compareTo(motsDepeche.get(j)) != 0) {
                        //recherche du mot dans le dictionnaire
                        k++;
                    }
                    if (k == resultat.size()) {
                        //ajout du mot dans le disctionnaire si il n'y est pas
                        resultat.add(new PaireChaineEntier(motsDepeche.get(j), 0));
                    }
                }
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Temps d'éxecution (initialisation): "+(endTime-startTime)+"ms");
        return resultat;
    }

    public static ArrayList<PaireChaineEntier> initDico(ArrayList<Depeche> depeches, String categorie) {
        // initialise un ArrayList<PaireChaineEntier> avec les mots présents dans les depeche de l'ArrayList depeches
        // si un mot est déjà présent dans l'ArrayList<PaireChaineEntier> on ne le rajoute pas
        ArrayList<PaireChaineEntier> resultat = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < depeches.size(); i++) {
            //parcours des dépêches de l'ArrayList depeches
            if (depeches.get(i).getCategorie().compareTo(categorie) == 0) {
                ArrayList<String> motsDepeche = depeches.get(i).getMots();
                for (int j = 0; j < motsDepeche.size(); j++) {
                    //parcours du contenu d'une dépêche mot par mot
                    UtilitairePaireChaineEntier.insereTriPaireVerif(resultat, new PaireChaineEntier(motsDepeche.get(j), 0));
                    //ajout du mot dans le dictionnaire si il n'y est pas, de façon à ce qu'il reste trié
                    }
                }
            }
        long endTime = System.currentTimeMillis();
        System.out.println("Temps d'éxecution (initialisation): "+(endTime-startTime)+"ms");
        return resultat;
    }

    public static void calculScoresV0(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {
        // calcul le score associé à chaque mot selon son nombre d'apparitions dans des dépêches de la catégorie categorie
        // auquel on soustrait le nombre d'apparitions dans des dépêches d'autres catégories
        // version antérieure pour comparaison des temps d'exécution
        long startTime = System.currentTimeMillis();
        for (int i=0; i<depeches.size(); i++){
            //parcours des dépêches de l'ArrayList depeches
            ArrayList<String> motsDepeche = depeches.get(i).getMots();
            String categorieDepeche= depeches.get(i).getCategorie();
            for (int j=0; j<motsDepeche.size(); j++){
                //parcours du contenu d'une dépêche mot par mot
                int place = UtilitairePaireChaineEntier.indicePourChaineV0(dictionnaire, motsDepeche.get(j));
                //on incrémente ou décremente le score du mot selon si il est ou non dans le dictionnaire de la catégorie et si il provient d'une dépêche de la même catégorie
                if (place !=-1 & categorieDepeche.compareTo(categorie) == 0) {
                    dictionnaire.get(place).incremente(1);
                } else if (place !=-1 & categorieDepeche.compareTo(categorie) != 0) {
                    dictionnaire.get(place).decremente(1);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Temps d'éxecution (scores): "+(endTime-startTime)+"ms");
    }

    public static void calculScores(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {
        // calcul le score associé à chaque mot selon son nombre d'apparitions dans des dépêches de la catégorie categorie
        // auquel on soustrait le nombre d'apparitions dans des dépêches d'autres catégories
        long startTime = System.currentTimeMillis();
        for (int i=0; i<depeches.size(); i++){
            //parcours des dépêches de l'ArrayList depeches
            ArrayList<String> motsDepeche = depeches.get(i).getMots();
            String categorieDepeche= depeches.get(i).getCategorie();
            for (int j=0; j<motsDepeche.size(); j++){
                //parcours du contenu d'une dépêche mot par mot
                int place= UtilitairePaireChaineEntier.indicePourChaine(dictionnaire, motsDepeche.get(j));
                    //on incrémente ou décremente le score du mot selon si il est ou non dans le dictionnaire de la catégorie et si il provient d'une dépêche de la même catégorie
                    if (place !=-1 & categorieDepeche.compareTo(categorie) == 0) {
                        dictionnaire.get(place).incremente(1);
                    } else if (place !=-1 & categorieDepeche.compareTo(categorie) != 0) {
                        dictionnaire.get(place).decremente(1);
                    }
                }
            }
        long endTime = System.currentTimeMillis();
        System.out.println("Temps d'éxecution (scores): "+(endTime-startTime)+"ms");
        }

    public static int poidsPourScore(int score) {
        //attribut un poid selon le score d'un mot
        if (score<-10) {
            return 0;
        } else if (score<-4){
            return 1;
        } else if (score<2) {
            return 2;
        } else {
            return 3;
        }
    }

    public static void generationLexiqueV0(ArrayList<Depeche> depeches, String categorie, String nomFichier) {
        // génération automatique du fichier contenant le lexique de la catégorie de nom categorie
        // à partir des mots contenus dans les dépêches
        // version antérieure pour comparaison des temps d'exécution
        long startTime = System.currentTimeMillis();
        ArrayList<PaireChaineEntier> resultat = initDicoV0(depeches, categorie);
        calculScoresV0(depeches, categorie, resultat);
        PaireChaineEntier p;
        for (int i = 0; i < resultat.size(); i++) {
            // attribution d'un poid à chaque mot selon son score
            // si le poid est de 0 on supprime le mot du lexique pour le raccourcir
            p = resultat.get(i);
            resultat.set(i, new PaireChaineEntier(p.getChaine(), poidsPourScore(p.getEntier())));
            if (resultat.get(i).getEntier()==0){
                resultat.remove(i);
                i--;
            }
        }
        //écriture du lexique dans un fichier
        try {
            FileWriter file = new FileWriter(nomFichier);
            for (int i=0; i<resultat.size(); i++) {
                file.write(resultat.get(i).getChaine()+":"+resultat.get(i).getEntier()+"\n");
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Temps d'éxecution (génération compléte) du lexique pour la catégorie "+categorie+": "+(endTime-startTime)+"ms");
    }

    public static void generationLexique(ArrayList<Depeche> depeches, String categorie, String nomFichier) {
        long startTime = System.currentTimeMillis();
        ArrayList<PaireChaineEntier> resultat = initDico(depeches, categorie);
        calculScores(depeches, categorie, resultat);
        PaireChaineEntier p;
        for (int i = 0; i < resultat.size(); i++) {
            // attribution d'un poid à chaque mot selon son score
            // si le poid est de 0 on supprime le mot du lexique pour le raccourcir
            p = resultat.get(i);
            resultat.set(i, new PaireChaineEntier(p.getChaine(), poidsPourScore(p.getEntier())));
            if (resultat.get(i).getEntier()==0){
                resultat.remove(i);
                i--;
            }
        }
        //écriture du lexique dans un fichier
        try {
            FileWriter file = new FileWriter(nomFichier);
            for (int i=0; i<resultat.size(); i++) {
                file.write(resultat.get(i).getChaine()+":"+resultat.get(i).getEntier()+"\n");
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Temps d'éxecution (génération compléte) du lexique pour la catégorie "+categorie+": "+(endTime-startTime)+"ms");
    }

    public static void main(String[] args) {
        // début du timer pour l'exécution du main
        long startTime = System.currentTimeMillis();

        //Chargement des dépêches en mémoire
        System.out.println("chargement des dépêches");
        ArrayList<Depeche> depeches = lectureDepeches("./Sae1.012-master/depeches.txt");
        ArrayList<Depeche> test = lectureDepeches("./Sae1.012-master/test.txt");

        //initialisation des lexiques
        System.out.println("Initialisation des lexiques");
        Categorie economie= new Categorie("ECONOMIE");
        Categorie environnement_sciences= new Categorie("ENVIRONNEMENT-SCIENCES");
        Categorie sports= new Categorie("SPORTS");
        Categorie culture= new Categorie("CULTURE");
        Categorie politique= new Categorie("POLITIQUE");

//        // génération automatique des lexiques ( version antérieure V0, pour comparer les temps d'exécution )
//        System.out.println("Génération automatique du lexique pour la catégorie économie: ");
//        generationLexiqueV0(depeches, "ECONOMIE", "lexiqueEconomieAutomatique.txt");
//        System.out.println("Génération automatique du lexique pour la catégorie environnement-sciences: ");
//        generationLexiqueV0(depeches, "ENVIRONNEMENT-SCIENCES", "lexiqueEnvironnement-sciencesAutomatique.txt");
//        System.out.println("Génération automatique du lexique pour la catégorie sports: ");
//        generationLexiqueV0(depeches, "SPORTS", "lexiqueSportsAutomatique.txt");
//        System.out.println("Génération automatique du lexique pour la catégorie culture: ");
//        generationLexiqueV0(depeches, "CULTURE", "lexiqueCultureAutomatique.txt");
//        System.out.println("Génération automatique du lexique pour la catégorie politique: ");
//        generationLexiqueV0(depeches, "POLITIQUE", "lexiquePolitiqueAutomatique.txt");
        
        // génération automatique des lexiques
        System.out.println("Génération automatique du lexique pour la catégorie économie: ");
        generationLexique(depeches, "ECONOMIE", "lexiqueEconomieAutomatique.txt");
        System.out.println("Génération automatique du lexique pour la catégorie environnement-sciences: ");
        generationLexique(depeches, "ENVIRONNEMENT-SCIENCES", "lexiqueEnvironnement-sciencesAutomatique.txt");
        System.out.println("Génération automatique du lexique pour la catégorie sports: ");
        generationLexique(depeches, "SPORTS", "lexiqueSportsAutomatique.txt");
        System.out.println("Génération automatique du lexique pour la catégorie culture: ");
        generationLexique(depeches, "CULTURE", "lexiqueCultureAutomatique.txt");
        System.out.println("Génération automatique du lexique pour la catégorie politique: ");
        generationLexique(depeches, "POLITIQUE", "lexiquePolitiqueAutomatique.txt");

//        // initialisation des ArrayList lexique pour chaque catégorie ( lexique généré manuellement )
//        economie.initLexique("lexiqueEconomieManuel.txt");
//        UtilitairePaireChaineEntier.triBullePaire(economie.getLexique());
//        environnement_sciences.initLexique("lexiqueEnvironnement-sciencesManuel.txt");
//        UtilitairePaireChaineEntier.triBullePaire(environnement_sciences.getLexique());
//        sports.initLexique("lexiqueSportsManuel.txt");
//        UtilitairePaireChaineEntier.triBullePaire(sports.getLexique());
//        culture.initLexique("lexiqueCultureManuel.txt");
//        UtilitairePaireChaineEntier.triBullePaire(culture.getLexique());
//        politique.initLexique("lexiquePolitiqueManuel.txt");
//        UtilitairePaireChaineEntier.triBullePaire(politique.getLexique());

        // initialisation des ArrayList lexique pour chaque catégorie pour pouvoir calculer les scores ( lexique généré automatiquement )
        economie.initLexique("lexiqueEconomieAutomatique.txt");
        environnement_sciences.initLexique("lexiqueEnvironnement-sciencesAutomatique.txt");
        sports.initLexique("lexiqueSportsAutomatique.txt");
        culture.initLexique("lexiqueCultureAutomatique.txt");
        politique.initLexique("lexiquePolitiqueAutomatique.txt");

        //création de l'ArrayList contenant les catégories
        ArrayList<Categorie> categories= new ArrayList<>(Arrays.asList(economie, environnement_sciences, sports, culture, politique));

//        //génération des résultats ( v0 )
//        classementDepechesV0(test, categories, "resultats.txt");

//        //génération des résultats ( lexiques manuels )
//        classementDepeches(test, categories, "resultatsManuel.txt");

        //génération des résultats ( lexiques automatiques )
        classementDepeches(test, categories, "resultatsAutomatique.txt");

        // fin du timer pour l'exécution du main
        long endTime = System.currentTimeMillis();
        System.out.println();
        System.out.println("Temps total pour la génération des résultats: "+(endTime-startTime)+"ms");
    }
}

