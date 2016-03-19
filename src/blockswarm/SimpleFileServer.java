package blockswarm;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleFileServer
{

    public final static int SOCKET_PORT = 13267;  // you may change this
    public final static String FILE_TO_SEND = "lib/netty-3.5.3.Patched.jar";  // you may change this

    public static void main(String[] args)
    {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        ServerSocket servsock = null;
        Socket sock = null;
        try
        {
            servsock = new ServerSocket(SOCKET_PORT);
            while (true)
            {
                System.out.println("Waiting...");
                try
                {
                    sock = servsock.accept();
                    System.out.println("Accepted connection : " + sock);
                    // send file
                    File myFile = new File(FILE_TO_SEND);
                    byte[] mybytearray = new byte[(int) myFile.length()];
                    fis = new FileInputStream(myFile);
                    bis = new BufferedInputStream(fis);
                    bis.read(mybytearray, 0, mybytearray.length);
                    os = sock.getOutputStream();
                    System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
                    os.write(mybytearray, 0, mybytearray.length);
                    os.flush();
                    System.out.println("Done.");
                } finally
                {
                    if (bis != null)
                    {
                        bis.close();
                    }
                    if (os != null)
                    {
                        os.close();
                    }
                    if (sock != null)
                    {
                        sock.close();
                    }
                }
            }
        } catch (IOException ex)
        {
            Logger.getLogger(SimpleFileServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            if (servsock != null)
            {
                try
                {
                    servsock.close();
                } catch (IOException ex)
                {
                    Logger.getLogger(SimpleFileServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
