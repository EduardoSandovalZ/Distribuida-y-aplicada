import java.net.Socket;
import java.net.ServerSocket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Servidor {

    private static ServerSocket creaListenSocket(int serverSockNum) {
        ServerSocket server = null;

        try {
            server = new ServerSocket(serverSockNum);
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
                    break;
            }
        }

        return res;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Debe especificar el puerto en el que el servidor escuchará peticiones");
            System.exit(1);
        }

        int SERVER_PORT = Integer.parseInt(args[0]);

        ServerSocket server = creaListenSocket(SERVER_PORT);

        try {
            while (true) {
                Socket clientSocket = server.accept();

                Thread thread = new Thread(() -> {
                    try (
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    ) {
                        String inputLine, outputLine;

                        while ((inputLine = in.readLine()) != null) {
                            if (inputLine.equals("END OF SERVICE")) {
                                break;
                            }

                            outputLine = "Número de vocales en \"" + inputLine + "\": " + numeroDeVocales(inputLine);
                            out.println(outputLine);
                        }

                        clientSocket.close();
                    } catch (IOException e) {
                        System.err.println("Problems with I/O.");
                        System.exit(-1);
                    }
                });

                thread.start();
            }
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(-1);
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                System.err.println("Problems with I/O.");
                System.exit(-1);
            }
        }
    }
}
