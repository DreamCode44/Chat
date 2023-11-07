package myclientsocket;

import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyClientSocket {
    private static final String SERVER_IP = "192.168.20.71"; // Reemplaza con la dirección IP del servidor
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
                                        MyClientSocket obj = new MyClientSocket();

            System.out.println("Conectado al servidor");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            // Hilo para enviar mensajes al servidor
            Thread sendMessageThread = new Thread(() -> {
                try {
                    String userInput;
                    while (true) {
                        userInput = consoleInput.readLine();
                        if ("exit".equalsIgnoreCase(userInput)) {
                            out.println(userInput);
                            break;
                        } else if (!userInput.isEmpty()) {
                            
                            
                            out.println(obj.codificar(userInput));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            sendMessageThread.start();

            // Hilo para recibir mensajes del servidor
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println("EL OTRO: " + obj.decodificar(response));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    



 public String codificar(String msj){
        int claveCesar = 5;
        

        StringBuilder reversed = new StringBuilder(msj).reverse();

        StringBuilder mensajeCifra = new StringBuilder();
        for (int i = 0; i < reversed.length(); i++) {
            char character = reversed.charAt(i);
            if (Character.isUpperCase(character)) {
                character = (char) (((character - 'A' + claveCesar) % 26) + 'A');
            } else if (Character.isLowerCase(character)) {
                character = (char) (((character - 'a' + claveCesar) % 26) + 'a');
            }
            mensajeCifra.append(character);
        }

        String mensajeCifradoF = Base64.getEncoder().encodeToString(mensajeCifra.toString().getBytes());
        return mensajeCifradoF;
    }
    
    
    public String decodificar(String msj2){
        int claveCesar = 5;

        byte[] mensajeCifradoBytes = Base64.getDecoder().decode(msj2);
        String mensajeCifrado = new String(mensajeCifradoBytes);

        StringBuilder mensajeDescifrado = new StringBuilder();
        for (int i = 0; i < mensajeCifrado.length(); i++) {
            char character = mensajeCifrado.charAt(i);
            if (Character.isUpperCase(character)) {
                character = (char) (((character - 'A' - claveCesar + 26) % 26) + 'A');
            } else if (Character.isLowerCase(character)) {
                character = (char) (((character - 'a' - claveCesar + 26) % 26) + 'a');
            }
            mensajeDescifrado.append(character);
        }

        String mensajeOriginal = new StringBuilder(mensajeDescifrado.toString()).reverse().toString();

        return mensajeOriginal;
    }
}