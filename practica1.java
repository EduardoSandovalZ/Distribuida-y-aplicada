class Contenedor {
    private int id;
    private int cantidadPiezas;
 
    public Contenedor(int id, int cantidadPiezas) {
        this.id = id;
        this.cantidadPiezas = cantidadPiezas;
    }
 
    public synchronized void descargarUnaPieza() {
        cantidadPiezas--;
    }
 
    public int getCantidadPiezas() {
        return cantidadPiezas;
    }
}
 
class Brazo implements Runnable {
    private int id;
    private int cantidadPiezas;
    private Contenedor contenedor;
 
    public Brazo(int id, int cantidadPiezas, Contenedor contenedor) {
        this.id = id;
        this.cantidadPiezas = cantidadPiezas;
        this.contenedor = contenedor;
    }
 
    public void run() {
        for (int i = 0; i < cantidadPiezas; i++) {
            contenedor.descargarUnaPieza();
            System.out.println("Brazo " + id + " toma una pieza. Piezas restantes: " + contenedor.getCantidadPiezas());
        }
    }
}
 
class Sistema {
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
