package universite;

/**
 * @param <T>
 * @author Célina Hadjara
 */
@FunctionalInterface
public interface ToString<T> {
    String convertToString(T a);
}
