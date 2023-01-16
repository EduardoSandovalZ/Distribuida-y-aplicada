public class Sistema {
    public static void main(String[] args) {
        Contenedor contenedor = new Contenedor(1, 50);

        Brazo brazo1 = new Brazo(1, 25, contenedor);
        Brazo brazo2 = new Brazo(2, 25, contenedor);

        Thread hiloBrazo1 = new Thread(brazo1);
        Thread hiloBrazo2 = new Thread(brazo2);

        hiloBrazo1.start();
        hiloBrazo2.start();

    }
}


