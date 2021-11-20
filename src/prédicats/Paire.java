package prédicats;

import java.util.List;
import java.util.function.Predicate;

public class Paire<T,U> {
    public T fst;
    public U snd;
    public Paire(T fst, U snd) {
        this.fst = fst;
        this.snd = snd;
    }

    public T getFst() {
        return fst;
    }

    public void setFst(T fst) {
        this.fst = fst;
    }

    public U getSnd() {
        return snd;
    }

    public void setSnd(U snd) {
        this.snd = snd;
    }

    public List<T> filtragePredicatif(List<Predicate<T>> predicat, List<T> elements){
        List<T> lesElements = null;
        boolean test;
        for (int i = 0; i < elements.size(); i++) {
            test = true;
            for (int j = 0; j < predicat.size(); j++) {
                if (predicat.get(j).test((T) elements)) test = false;
            }
            if (test) lesElements.add((T) elements);
        }
        return lesElements;
    }

    @Override public String toString() {
        return String.format("(%s,%s)",fst.toString(),snd.toString());
    }
}
