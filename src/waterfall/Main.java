package waterfall;


public class Main {

    public static void main(String[] args) {
        Concealer concealer = new Concealer();
        long startTime = System.currentTimeMillis();

        concealer.conceal("image.jpg", "Secret text");
        System.out.println("To conceal it took: " + (System.currentTimeMillis() - startTime)/1000 + "s");

        startTime = System.currentTimeMillis();
        System.out.println(concealer.reveal("image.jpg"));
        System.out.println("To reveal it took: " + (System.currentTimeMillis() - startTime)/1000 + "s");
//        concealer.clean("image.jpg");
    }
}
