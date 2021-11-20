package predicats;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Main class to implement predicates of Paire class
 *
 * @author Célina Hadjara
 */
public class Main {

    public static void main(String[] args) {
        Predicate<Paire<Integer, Double>> tropPetite = taille -> taille.fst < 100;
        Predicate<Paire<Integer, Double>> tropGrand = taille -> taille.fst > 200;

        Predicate<Paire<Integer, Double>> tailleIncorrecte = taille -> tropPetite.or(tropGrand).test(taille);
        Predicate<Paire<Integer, Double>> tailleCorrecte = taille -> tropPetite.and(tropGrand).test(taille);

        Predicate<Paire<Integer, Double>> tropLourd = poid -> poid.snd > 150.0;
        Predicate<Paire<Integer, Double>> poidCorrect = poid -> tropLourd.negate().test(poid);


        Predicate<Paire<Integer, Double>> accesAutorise = list -> poidCorrect
                .and(tailleCorrecte)
                .test(list);


        Paire paire = new Paire(158, 48);
        Paire paire2 = new Paire(18, 480);
        Paire paire3 = new Paire(218, 48);
        Paire paire4 = new Paire(218, 480);

        List elements = new ArrayList();
        elements.add(paire);
        elements.add(paire2);
        elements.add(paire3);
        elements.add(paire4);

        List predicats = new ArrayList();
        predicats.add(tropPetite);
        predicats.add(tropGrand);
        predicats.add(tailleIncorrecte);

        System.out.println(paire.filtragePredicatif(predicats, elements));


    }
}
