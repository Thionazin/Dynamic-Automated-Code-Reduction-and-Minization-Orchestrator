// Every operator pairing the Whitespace Safety Rule has to protect.
public class OperatorGauntlet {
    public static void main(String[] args) {
        int a = 7, b = 3, i = 1;
        int x = a - -b;
        int y = a + +b;
        int z = a / -b;
        int w = a - --b;
        int v = i++ + ++i;
        int u = a * -b;
        int shift = -16 >>> 28;
        int signed = -16 >> 2;
        long big = 0xFFL + 1_000L;
        double tiny = 1e-3 + 1.5f;
        boolean flag = !(a < b) && a >= b || a != b;
        int bits = ~a & 0x0F | a ^ b;
        int cast = (int) -tiny;
        char next = (char) ('a' + 1);
        a += 2; a -= 1; a *= 3; a /= 2; a %= 5; a <<= 1; a >>= 1;
        String ternary = a > 2 ? a > 4 ? "big" : "mid" : "small";
        Object o = "text";
        boolean isString = o instanceof String;
        outer:
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (r * c == 2) break outer;
            }
        }
        System.out.println(x + " " + y + " " + z + " " + w + " " + v + " " + u);
        System.out.println(shift + " " + signed + " " + big + " " + tiny + " " + flag + " " + bits);
        System.out.println(cast + " " + next + " " + a + " " + ternary + " " + isString);
    }
}
