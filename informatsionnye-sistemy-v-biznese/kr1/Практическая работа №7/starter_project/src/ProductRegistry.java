import java.util.HashSet;
import java.util.Set;

public class ProductRegistry {
    private static final Set<String> registeredIds = new HashSet<>();

    public static synchronized void register(String id) {
        if (registeredIds.contains(id)) {
            throw new IllegalArgumentException("Duplicate Product ID detected: " + id);
        }
        registeredIds.add(id);
    }

    public static synchronized void clear() {
        registeredIds.clear();
    }
}
