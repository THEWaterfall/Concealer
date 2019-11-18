package waterfall;

public interface Concealer {
    void conceal(String filepath, String text);
    String reveal(String filepath);
    void clean(String filepath);
}
