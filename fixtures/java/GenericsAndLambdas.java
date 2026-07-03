import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier; // never used, should be pruned
import java.util.stream.Collectors;

/**
 * Exercises generics, lambdas, method references, anonymous classes,
 * default methods, enums and varargs.
 */
public class GenericsAndLambdas {
    interface Greeter {
        String greet(String name);

        default String greetAll(String... names) {
            StringBuilder sb = new StringBuilder();
            for (String n : names) sb.append(greet(n)).append(';');
            return sb.toString();
        }
    }

    enum Level { LOW, MEDIUM, HIGH }

    static <T extends Comparable<T>> T max(List<T> items) {
        T best = items.get(0);
        for (T item : items) if (item.compareTo(best) > 0) best = item;
        return best;
    }

    public static void main(String[] args) {
        Map<String, List<Integer>> groups = new TreeMap<>();
        groups.computeIfAbsent("odd", k -> new ArrayList<>()).add(1);
        groups.computeIfAbsent("even", k -> new ArrayList<>()).add(2);
        groups.get("odd").add(3);

        Function<Integer, Integer> square = n -> n * n;
        List<Integer> squares = Arrays.asList(1, 2, 3, 4).stream()
                .map(square)
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());

        Greeter polite = new Greeter() {
            @Override
            public String greet(String name) {
                return "Hello, " + name;
            }
        };
        Greeter terse = name -> "Hi " + name;

        for (Level level : Level.values()) {
            switch (level) {
                case LOW: System.out.print("l"); break;
                case MEDIUM: System.out.print("m"); break;
                default: System.out.print("h");
            }
        }
        System.out.println();
        System.out.println(groups + " " + squares + " " + max(Arrays.asList("pear", "apple", "zucchini")));
        System.out.println(polite.greetAll("Ada", "Linus") + " " + terse.greet("Grace"));
        System.out.println(Arrays.asList("b", "a", "c").stream().sorted().map(String::toUpperCase).collect(Collectors.joining(",")));
    }
}
