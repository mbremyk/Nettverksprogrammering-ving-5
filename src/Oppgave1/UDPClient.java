package Oppgave1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient
{
    public static void main(String[] args) throws IOException
    {
        final int PORTNR = 1250;
        byte[] reqBytes;
        byte[] resBytes = new byte[1024];

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        DatagramSocket socket = new DatagramSocket();
        DatagramPacket req;
        DatagramPacket res;

        System.out.print("Enter name of server machine: ");
        String server = br.readLine();

        InetAddress IP = InetAddress.getByName(server);

        System.out.println("Establishing connection...");

        req = new DatagramPacket("SYN".getBytes(), "SYN".length(), IP, PORTNR);
        socket.send(req);

        res = new DatagramPacket(resBytes, resBytes.length);
        socket.receive(res);
        System.out.println(new String(res.getData()));
        socket.receive(res);
        System.out.println(new String(res.getData()));
        resBytes = new byte[1024];
        res = new DatagramPacket(resBytes, resBytes.length);

        String line = br.readLine().trim();
        reqBytes = line.getBytes();
        while (!line.equals(""))
        {
            req = new DatagramPacket(reqBytes, reqBytes.length, IP, PORTNR);
            socket.send(req);
            resBytes = new byte[resBytes.length];
            res = new DatagramPacket(resBytes, resBytes.length);
            socket.receive(res);
            String resSentence = new String(res.getData());
            System.out.println("From server: " + resSentence);
            line = br.readLine().trim();
            reqBytes = line.getBytes();
        }
        socket.close();
    }
}
