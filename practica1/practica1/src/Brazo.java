public class Brazo implements Runnable {
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
