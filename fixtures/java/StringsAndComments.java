/* A header comment with a fake string: "not a string" and a fake char 'x' */
public class StringsAndComments {
    // a comment containing "quotes" and 'apostrophes' and /* nested-looking */ markers
    static final String SLASHES = "// these slashes stay";
    static final String STARS = "/* and so does this */";
    static final String ESCAPES = "tab\there, quote\" backslash\\ end";
    static final String UNICODE = "héllo wörld ✓";
    static final String SPACES = "  multiple   spaces   survive  ";
    static final char SLASH = '/';
    static final char QUOTE = '"';
    static final char BACKSLASH = '\\';
    static final char APOSTROPHE = '\'';

    /**
     * Javadoc with code: {@code int x = 1; // trailing}
     */
    static String describe() {
        return SLASHES /* inline */ + "|" + STARS // trailing
                + "|" + ESCAPES
                + "|" + UNICODE
                + "|[" + SPACES + "]";
    }

    public static void main(String[] args) {
        System.out.println(describe());
        System.out.println("" + SLASH + QUOTE + BACKSLASH + APOSTROPHE);
        System.out.println("end */ of /* tricky // line");
    }
}
