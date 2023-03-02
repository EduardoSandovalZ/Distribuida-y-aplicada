import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Cliente {

	// Dirección y puerto del servidor
	static private String SERVER_ADDRESS;
	static private int SERVER_PORT;

	// Socket para la comunicación con el servidor
	static private Socket socketAlServidor = null;

	// Método para conectar con el servidor con un máximo de intentos
	static private boolean conectarServidor(int maxIntentos){
		// Hasta maxIntentos intentos de conexión, para darle tiempo al servidor a arrancar
		boolean exito = false;     // Hay servidor?
		int van = 0;

		while((van<maxIntentos) && !exito){
			try {
				socketAlServidor = new Socket(SERVER_ADDRESS, SERVER_PORT);
				exito = true;
			} catch (Exception e) {
				van++;
				System.err.println("Failures:" + van);
				try {    // Esperar 1 segundo
    				Thread.sleep(1000);
				} catch (InterruptedException e2) {
    				e2.printStackTrace();
				}
			}
		}
		return exito;
	}

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

		boolean exito; // ¿conectado?

		exito = conectarServidor(10); // 10 intentos

		if(!exito){
			System.err.println("No se puede conectar con el servidor " + SERVER_ADDRESS + ":" + SERVER_PORT);
			System.exit(1);           // Abortar si hay problemas
		}

		// Ya hay conexión
		// Inicializar los flujos de datos del socket para la comunicación con el servidor
		PrintWriter canalSalidaAlServidor = null;
		BufferedReader canalEntradaDelServidor = null;
		try {
			canalSalidaAlServidor = new PrintWriter(socketAlServidor.getOutputStream(), true);
			canalEntradaDelServidor = new BufferedReader(new InputStreamReader(socketAlServidor.getInputStream()));
		} catch (IOException e) {      // Abortar si hay problemas
			System.err.println("Problema de I/O en la comunicación con el servidor " + SERVER_ADDRESS + ":" + SERVER_PORT);
			System.exit(1);
		}

		// Un buffer de entrada para leer de la entrada estándar.
		BufferedReader entradaStandard = new BufferedReader(new InputStreamReader(System.in));
		String userInput = "";

		// Protocolo de comunicación con el Servidor.
		// Mientras no se reciba la secuencia "END OF SERVICE"
		// el servidor contará las vocales que aparecen en las frases
		// que le envía el cliente.
		// El cliente obtiene las frases
		// que le pasa al servidor del usuario que lo está ejecutando.
		try{
			while (!(userInput.equals("END OF SERVICE"))) {
				System.out.print("Introduce una frase (o END OF SERVICE para terminar): ");
				userInput = entradaStandard.readLine();
				if (userInput != null) {
					canalSalidaAlServidor.println(userInput);
					String respuesta = canalEntradaDelServidor.readLine();
					if (respuesta != null) {
						System.out.println("Server answer: " + respuesta);
					} else {
						System.out.println("Comm. is closed!");
					}
				} else {
					System.err.println("Wrong input!");
				}
			}

			// Al cerrar cualquiera de los canales de comunicacin utilizados por un socket,ste se cierra.
			// cerrar el canal de entrada.
			canalEntradaDelServidor.close();

			// cerrar el Socket de comunicacin con el servidor.
			socketAlServidor.close();
		} catch (Exception e){
			System.err.println(e);
		}

	}
}