package universite;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Class of Predicates and Functions
 *
 * @author Celina HADJARA
 */
public class MesPrediates {
    /**
     * Somme de deux entiers
     */
    Somme<Integer> sommeInt =
            (Integer x, Integer y) -> x + y;

    /**
     * Concaténation de deux chaines de caractères
     */
    Somme<String> sommeString =
            (String x, String y) -> x + y;

    /**
     * Somme de deux doubles
     */
    Somme<Double> sommeDouble =
            (Double x, Double y) -> x + y;

    /**
     * Somme de deux longs
     */
    Somme<Long> sommeLong =
            (Long x, Long y) -> x + y;

    //Avec les streams
    ToString<List<String>> listToString = list ->
            list.stream().reduce("", (String a, String b) -> a + " ," + b);

    //Sans les streams
    ToString<List<Object>> listToString1 = x -> {
        String total = "";
        for (int i = 0; i < x.size(); i++) {
            total = sommeString.somme(total, x.get(i).toString());
            //virgule entre les elements
            if (i < x.size() - 1) {
                total = sommeString.somme(total, ",");
            }
            //Dernier element fini par un "."
            if (i == x.size() - 1) {
                total = sommeString.somme(total, ".");
            }
        }
        return total;
    };

    Map<String, Integer> map = Map.of("L3", 23, "M1", 25, "M2", 30);

    //Avec les streams
    ToString<Map<Integer, String>> mapToString = x -> map.keySet()
            .stream()
            .map(key -> key + ": " + map.get(key)).collect(Collectors.joining(","));

    //Sans les streams
    ToString<Map<Object, Object>> mapToString1 = x -> {
        StringBuilder total = new StringBuilder(" ");
        x.forEach((key, value) -> total.append(total)
                .append(key
                        .toString()).append(":")
                .append(value.toString())
                .append(","));
        return total.toString();

    };

    /**
     * Vérifier si un étudiant est défaillant
     */
    public static final Predicate<Etudiant> aDEF = etudiant -> {
        for (UE ue : etudiant.annee().ues()) {
            for (Map.Entry<Matiere, Integer> ects : ue.ects().entrySet()) {
                if (!etudiant.notes().containsKey(ects.getKey())) {
                    return true;
                }
            }
        }
        return false;
    };

    /*
      Question 3
      Function: Prends un argument (T) et retourne un (R), c'est à dire T convertie en R
      Predicate: Prends un argument (T) et retourne un boolean
      Consumer: Prends un argument (T) et ne retourne aucune valeur
      Supplier: Ne prends rien en argument et retourne (T)
     */

    /**
     * Vérifier si un étudiant a une note éliminatoire cad <6
     */
    public static final Predicate<Etudiant> aNoteEliminatoire = a -> {
        for (Double note : a.notes().values()) {
            if (note < 6.0)
                return true;
        }

        return false;
    };

    /**
     * Without try catch, this predicate make a null pointer exception
     * Because we can't compare null with double
     */
    public static final Predicate<Etudiant> naPasLaMoyennev1 = etudiant -> {
        Double aDouble = MesFonctions.moyenne(etudiant) == null ? null : MesFonctions.moyenne(etudiant);
        if (aDouble != null) {
            return aDouble < 10;
        }
        return false;
    };

    /**
     * Verifier si un étudiant n'a pas de moyenne cad défaillant ou a une note <6
     */
    public static final Predicate<Etudiant> naPasLaMoyennev2 = etudiant -> aDEF.or(naPasLaMoyennev1).test(etudiant);

    /**
     * On observe que l'ordre des test (d'excecution) correspond aux mêmes ordres
     * dans la disjonction logique des predicats
     */
    public static final Predicate<Etudiant> session2v1 = etudiant -> aDEF.or(aNoteEliminatoire).or(naPasLaMoyennev1).test(etudiant);

    public static final Function<Etudiant, String> functionMoyenne = etudiant -> {
        if (aDEF.negate().test(etudiant))
            return etudiant.prenom() + " " + etudiant.nom() + " : " + MesFonctions.moyenne(etudiant);
        return etudiant.prenom() + " " + etudiant.nom() + " : " + "défaillant";
    };

    public static final Function<Etudiant, String> functionMoyenneIndicative = etudiant ->
            etudiant.prenom() + " " + etudiant.nom() + " : " + MesFonctions.moyenneIndicative(etudiant);

    public static final Predicate<Etudiant> naPasLaMoyenneGeneralise = etudiant -> {
        Double aDoubleIndic = MesFonctions.moyenneIndicative(etudiant) == null ? null : MesFonctions.moyenneIndicative(etudiant);
        Double aDouble = MesFonctions.moyenne(etudiant) == null ? null : MesFonctions.moyenne(etudiant);

        if (aDoubleIndic != null && MesPrediates.aDEF.test(etudiant)) return aDoubleIndic < 10;
        if (aDouble != null) return aDouble < 10;

        return false;
    };
}
