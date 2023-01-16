public class Contenedor {
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
