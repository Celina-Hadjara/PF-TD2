package universite;


import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Class of Predicates and Functions
 * @author Celina HADJARA
 */
public class MesPrediates {
    /**
     * Somme de deux entiers
     */
    Somme<Integer> sommeInt =
            (Integer x, Integer y) -> x+y;

    /**
     * Concaténation de deux chaines de caractères
     */
    Somme<String> sommeString =
            (String x, String y) -> x+y;

    /**
     * Somme de deux doubles
     */
    Somme <Double> sommeDouble =
            (Double x, Double y) -> x+y;

    /**
     * Somme de deux longs
     */
    Somme <Long> sommeLong =
            (Long x, Long y) -> x+y;

    //Avec les streams
    ToString<List<String>> listToString=(list)->
            list.stream().reduce("",(String a, String b)->a+" ,"+b);

    //Sans les streams
    ToString<List<Object>> listToString1 = (x) -> {
        String total = "";
        for (int i = 0; i < x.size(); i++) {
            total = sommeString.somme(total, x.get(i).toString());
            //virgule entre les elements
            if (i < x.size()-1) {
                total = sommeString.somme(total, ",");
            }
            //Dernier element fini par un "."
            if(i==x.size()-1){
                total=sommeString.somme(total,".");
            }
        }
        return total;
    };

    Map<String,Integer> map=Map.of("L3",23,"M1",25,"M2",30);

    //Avec les streams
    ToString<Map<Integer,String>> mapToString = (x)-> map.keySet()
            .stream()
            .map(key->key + ": " + map.get(key)).collect(Collectors.joining(","));

    //Sans les streams
    ToString<Map<Object,Object>> mapToString1=(x)->{
        String total="";
        Iterator it=x.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry mapentry =(Map.Entry) it.next();
            total=total+mapentry.getKey().toString()+":"+mapentry.getValue().toString()+",";
        }
        return total;

    };

    /**
     * Vérifier si un étudiant est défaillant
     */
    public static Predicate<Etudiant> aDEF = (etudiant)-> {
        for (UE ue: etudiant.annee().ues()) {
            for (Map.Entry<Matiere, Integer> ects : ue.ects().entrySet()) {
                if( ! etudiant.notes().containsKey(ects.getKey()) ) {
                    return true;
                }
            }
        }
        return false;
    };

    /**
     * Question 3
     * Function: Prends un argument (T) et retourne un (R), c'est à dire T convertie en R
     * Predicate: Prends un argument (T) et retourne un boolean
     * Consumer: Prends un argument (T) et ne retourne aucune valeur
     * Supplier: Ne prends rien en argument et retourne (T)
     */

    /**
     * Vérifier si un étudiant a une note éliminatoire cad <6
     */
    public static Predicate<Etudiant> aNoteEliminatoire = a -> {
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
    public static Predicate<Etudiant>  naPasLaMoyennev1 = etudiant -> {
        try {
            return MesFonctions.moyenne(etudiant) < 10;
        }catch (NullPointerException e){
            System.err.println();
        }
        return false;
    };

    /**
     * Verifier si un étudiant n'a pas de moyenne cad défaillant ou a une note <6
     */
    public static Predicate<Etudiant> naPasLaMoyennev2 = etudiant ->{
        return aDEF.or(naPasLaMoyennev1).test(etudiant);
    } ;

    /**
     * On observe que l'ordre des test (d'excecution) correspond aux mêmes ordres
     * dans la disjonction logique des prédicats
     */
    public static Predicate<Etudiant> session2v1 = etudiant -> {
        return aDEF.or(aNoteEliminatoire).or(naPasLaMoyennev1).test(etudiant);
    };

    public static Function<Etudiant, String> functionMoyenne = etudiant -> {
        if (aDEF.negate().test(etudiant))
            return etudiant.prenom() + " " + etudiant.nom() + " : " + MesFonctions.moyenne(etudiant);
        return etudiant.prenom() + " " + etudiant.nom() + " : " + "défaillant";
    };

    public static Function<Etudiant, String> functionMoyenneIndicative = etudiant -> {
        return etudiant.prenom() + " " + etudiant.nom() + " : " + MesFonctions.moyenneIndicative(etudiant);
    };

    public static Predicate <Etudiant> naPasLaMoyenneGeneralise = etudiant ->  {
        if(MesPrediates.aDEF.test(etudiant)) return MesFonctions.moyenneIndicative(etudiant)<10;
        return MesFonctions.moyenne(etudiant)<10;
    };
}
