// Demonstrating Server-side Programming
import java.io.*;
import java.net.*;
import java.util.*;


public class Receiver {
  
    // Initialize socket and input stream
    private Socket s = null;
    private ServerSocket ss = null;
    private DataInputStream in = null;

    // Constructor with port
    public Receiver(int port) throws Exception {
      
        // Starts server and waits for a connection
        try
        {
            ss = new ServerSocket(port);
            System.out.println("Server started");

            System.out.println("Waiting for a client ...");

            s = ss.accept();
            System.out.println("Client accepted");
            List<Byte[]> li = new ArrayList<>();

                       // Takes input from the client socket
            in = new DataInputStream(new BufferedInputStream(s.getInputStream()));
List<byte[]> list = new ArrayList<>();

while (true) {
    try {
        int len = in.readInt();           // first read length
        byte[] fragment = new byte[len];
        in.readFully(fragment);           // read full fragment
        list.add(fragment);
    } catch (EOFException e) {
        break;  // end of stream
    }
}
            String folder="";
            String file= "TCP";
            byte[] reassemble=Fragmented.receiver_reassembly(list);
            //System.out.println(list);
            Fragmented.write_to_file(reassemble);
            System.out.println("Closing connection");

            // Close connection
            s.close();
            in.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }

    public static void main(String args[]) throws Exception
    {
        Receiver s = new Receiver(5000);
    }
}
