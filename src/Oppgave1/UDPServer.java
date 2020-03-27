package Oppgave1;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class UDPServer
{
    public static void main(String[] args) throws IOException
    {
        final int PORTNR = 1250;

        ArrayList<Thread> threads = new ArrayList<Thread>();
        DatagramSocket socket = new DatagramSocket(PORTNR);

        System.out.println("Server log:");

        threads.add(new ThreadClientHandler(socket));
        threads.get(threads.size() - 1).start();
    }
}
