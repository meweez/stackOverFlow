import java.net.*;
import java.io.*;
import java.util.*;


public class Client {

    // for I/O
    private ObjectInputStream sInput;		// to read from the socket
    private ObjectOutputStream sOutput;		// to write on the socket
    private Socket socket;

    // the Server, the port and the username
    private String server, username;
    private int port;

    
    public Client(String server, int port, String username) {
        
    	this.server = server;
        this.port = port;
        this.username = username;
    }

    
    private boolean start() {
        // try to connect to the Server
        try {
            socket = new Socket(server, port);
        }
        // if it failed not much I can so
        catch(Exception ec) {
             System.out.println("Error connecting to Server:" + ec);
            return false;
        }

        String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
         System.out.println(msg);

		/* Creating both Data Stream */
        try
        {
            sInput  = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());
        }
        catch (IOException eIO) {
             System.out.println("Exception creating new Input/output Streams: " + eIO);
            return false;
        }

        // creates the Thread to listen from the Server
        new ListenFromServer().start();
        // Send our username to the Server this is the only message that we
        // will send as a String. All other messages will be Message objects
        try
        {
            sOutput.writeObject(username);
        }
        catch (IOException eIO) {
             System.out.println("Exception doing login : " + eIO);
            disconnect();
            return false;
        }
        // success we inform the caller that it worked
        return true;
    }


    /*
     * To send a message to the Server
     */
    public void sendMessage(Message msg) {
        try {
            sOutput.writeObject(msg);
        }
        catch(IOException e) {
            System.out.println("Exception writing to Server: " + e);
        }
    }

    /*
     * When something goes wrong
     * Close the Input/Output streams and disconnect not much to do in the catch clause
     */
    private void disconnect() {
        try {
            if(sInput != null) sInput.close();
        }
        catch(Exception ignored) {} // not much else I can do
        try {
            if(sOutput != null) sOutput.close();
        }
        catch(Exception ignored) {} // not much else I can do
        try{
            if(socket != null) socket.close();
        }
        catch(Exception ignored) {} // not much else I can do

    }

    public static void main(String[] args) {
        // default values
        int portNumber = 8000;
        String serverAddress = "127.0.0.1";
        String userName = "user";

       
        
        // create the Client object
        Client client = new Client(serverAddress, portNumber, userName);
        
        //new frame 
        new Frame(client);
        
        // test if we can start the connection to the Server
        // if it failed nothing we can do
        if(!client.start())
            return;

        
        
        // done disconnect
       // client.disconnect();
    }

    /*
     * a class that waits for the message from the Server and append them to the JTextArea
     * if we have a GUI or simply System.out.println() it in console mode
     */
    class ListenFromServer extends Thread {

        public void run() {
            while(true) {
                try {
                    String msg = (String) sInput.readObject();
                    // if console mode print the message and add back the prompt
                    System.out.println(msg);
                }
                catch(IOException e) {
                    System.out.println("Server has close the connection: " + e);
                    break;
                }
                // can't happen with a String object but need the catch anyhow
                catch(ClassNotFoundException ignored) {
                }
            }
        }
    }
}

