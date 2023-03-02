import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.io.IOException;
import java.util.Scanner;

public class UDPServer {

    private static DatagramSocket creaListenSocket(int serverSockNum) {
        DatagramSocket server = null;

        try {
            server = new DatagramSocket(serverSockNum);
        } catch (IOException e) {
            System.err.println("Problems in port: " + serverSockNum);
            System.exit(-1);
        }

        return server;
    }

    private static int numeroDeVocales(String frase) {
        int res = 0;
        String fraseMin = frase.toLowerCase();

        for (int i = 0; i < fraseMin.length(); ++i) {
            switch (fraseMin.charAt(i)) {
                case 'a':
                case 'á':
                case 'e':
                case 'é':
                case 'i':
                case 'í':
                case 'o':
                case 'ó':
                case 'u':
                case 'ú':
                    res++;
                    break;
                default:
                    // se ignoran las demás letras
            }
        }

        return res;
    }

    public static void main(String[] args) {
        int SERVER_PORT;
        DatagramSocket serverSocket = null;

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Ingresa el número de puerto del servidor: ");
            SERVER_PORT = scanner.nextInt();
        }
        serverSocket = creaListenSocket(SERVER_PORT);

        byte[] buffer = new byte[1024];

        while (true) {
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            try {
                serverSocket.receive(request);
                String inputLine = new String(request.getData(), 0, request.getLength());

                if (inputLine.equals("END OF SERVICE")) {
                    break;
                }

                String respuesta = "'" + inputLine + "' tiene " + numeroDeVocales(inputLine) + " vocales";
                byte[] respuestaBytes = respuesta.getBytes();

                DatagramPacket response = new DatagramPacket(respuestaBytes, respuestaBytes.length, request.getAddress(), request.getPort());
                serverSocket.send(response);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
        serverSocket.close();
    }
}
