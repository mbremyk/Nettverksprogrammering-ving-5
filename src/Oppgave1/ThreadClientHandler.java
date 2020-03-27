package Oppgave1;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThreadClientHandler extends Thread
{
    private String patternStr = "[+*/-]";
    private Pattern pattern = Pattern.compile(patternStr);
    private DatagramSocket connection;
    private byte[] reqBytes = new byte[1024];
    private byte[] resBytes = new byte[1024];

    public ThreadClientHandler(DatagramSocket connection)
    {
        this.connection = connection;
    }

    @Override
    public void run()
    {
        DatagramPacket req = new DatagramPacket(reqBytes, reqBytes.length);
        InetAddress IP = req.getAddress();
        int port = 1250;
        try
        {
            req = new DatagramPacket(reqBytes, reqBytes.length);
            connection.receive(req);
            IP = req.getAddress();
            port = req.getPort();
            String reqStr = new String(req.getData());
            System.out.println(reqStr);

            DatagramPacket res = new DatagramPacket("Connection established".getBytes(), "Connection established".getBytes().length, IP, port);
            connection.send(res);
            res = new DatagramPacket("Write a simple math problem, on the form <number><sign><number>. Example: 3+4".getBytes(), "Write a simple math problem, on the form <number><sign><number>. Example: 3+4".getBytes().length, IP, port);
            connection.send(res);

            connection.receive(req);
            IP = req.getAddress();
            double answer = 0;
            String line = new String(req.getData());
            while (!line.equals(""))
            {
                double d1 = 0, d2 = 0;
                System.out.println("Client wrote: " + line);
                Matcher matcher = pattern.matcher(line);
                matcher.find();
                try
                {
                    d1 = Double.parseDouble(line.substring(0, matcher.start()));
                    d2 = Double.parseDouble(line.substring(matcher.start() + 1));
                }
                catch (NumberFormatException nfe)
                {
                    res = new DatagramPacket("Could not parse numbers".getBytes(), "Could not parse numbers".getBytes().length, IP, port);
                    connection.send(res);
                    connection.receive(req);
                    IP = req.getAddress();
                    line = new String(req.getData());
                    continue;
                }
                catch(IllegalStateException iae)
                {
                    res = new DatagramPacket("Could not find sign".getBytes(), "Could not find sign".getBytes().length, IP, port);
                    connection.send(res);
                    connection.receive(req);
                    IP = req.getAddress();
                    line = new String(req.getData());
                    continue;
                }

                char sign = line.charAt(matcher.start());
                switch (sign)
                {
                    case '+':
                        answer = d1 + d2;
                        break;
                    case '-':
                        answer = d1 - d2;
                        break;
                    case '*':
                        answer = d1 * d2;
                        break;
                    case '/':
                        answer = d1 / d2;
                        break;
                    default:
                        break;
                }
                res = new DatagramPacket(Double.toString(answer).getBytes(), Double.toString(answer).getBytes().length, IP, port);
                connection.send(res);
                System.out.println(new String(res.getData()));
                connection.receive(req);
                IP = req.getAddress();
                line = new String(req.getData());
            }
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
            try
            {
                DatagramPacket res = new DatagramPacket("An unhandled exception occured. Please try again".getBytes(), "An unhandled exception occured. Please try again".getBytes().length, IP, port);
                connection.send(res);
            }
            catch (Exception e2)
            {

            }
        }
        finally
        {
            connection.close();
        }
    }
}
