import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.util.Scanner;

public class UDPClient {

    // Dirección y puerto del servidor
    static private String SERVER_ADDRESS;
    static private int SERVER_PORT;

    // Socket para la comunicación con el servidor
    static private DatagramSocket socketAlServidor = null;

    public static void main(String[] args) {
        // Obtener la dirección IP y el puerto del servidor desde los parámetros de entrada
        if (args.length != 2) {
            System.err.println("Debe especificar la dirección IP y el puerto del servidor como parámetros de entrada");
            System.exit(1);
        }
        SERVER_ADDRESS = args[0];
        try {
            SERVER_PORT = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("El puerto debe ser un número entero");
            System.exit(1);
        }

        try {
            // Crear el socket para la comunicación con el servidor
            socketAlServidor = new DatagramSocket();

            // Leer datos desde la entrada estándar
            Scanner scanner = new Scanner(System.in);
            String inputLine;

            do {
                // Leer una línea de texto desde la entrada estándar
                System.out.print("Introduce una frase (o END OF SERVICE para terminar): ");
                inputLine = scanner.nextLine();

                // Convertir la línea de texto en un array de bytes
                byte[] buf = inputLine.getBytes();

                // Crear el datagrama para enviar al servidor
                DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);

                // Enviar el datagrama al servidor
                socketAlServidor.send(packet);

                // Recibir la respuesta del servidor
                buf = new byte[256];
                packet = new DatagramPacket(buf, buf.length);
                socketAlServidor.receive(packet);

                // Imprimir la respuesta del servidor
                String respuesta = new String(packet.getData(), 0, packet.getLength());
                System.out.println(respuesta);
            } while (!inputLine.equals("END OF SERVICE"));

            // Cerrar el socket
            socketAlServidor.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
