import java.util.List; // used
import java.util.ArrayList;
import java.util.HashMap; // unused
import java.util.concurrent.*;
/* block
   comment */
public class Hello {
    static String url = "http://x.com // not a comment";
    static char q = '"';
    static char bs = '\\';
    static char sq = '\'';
    static String esc = "she said \"hi\" /* still string */   spaced";

    static int add(int a, int b) {
        return a/*mid*/+b; // sum
    }

    public static void main(String[] args) {
        List<Integer> xs = new ArrayList<>();
        for (int i = 0; i < 3; i++) xs.add(add(i, 10));
        int a = 5, b = 2;
        int x = 3;
        int c = a - -b;
        int d = x + +b;
        int e = a / -b;
        System.out.println(url + " " + q + bs + sq + " " + esc);
        System.out.println(xs + "\ttab " + c + " " + d + " " + e);
    }
}
