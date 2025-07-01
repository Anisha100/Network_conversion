import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

public class Client
{

    Socket s;
    DataInputStream in;
    DataOutputStream out;

    public Client(String s1, int i)
    {
        s = null;
        in = null;
        out = null;
        try
        {
            s = new Socket(s1, i);
            System.out.println("Connected");
            String as[] = new String[2];
            as = Fragmented.file_mani();
            String s2 = as[0];
            String s3 = as[1];
            byte abyte0[] = Fragmented.givenFile_whenUsingFileInputStreamClass_thenConvert(s2, s3);
            List list = Fragmented.sender_fragmentation(abyte0);
            List list1 = Fragmented.network_randomizer(list);
            DataOutputStream dataoutputstream = new DataOutputStream(s.getOutputStream());
            for(Iterator iterator = list1.iterator(); iterator.hasNext(); dataoutputstream.flush())
            {
                byte abyte1[] = (byte[])iterator.next();
                dataoutputstream.writeInt(abyte1.length);
                dataoutputstream.write(abyte1);
            }

        }
        catch(Exception exception)
        {
            System.out.println("4");
            System.out.println(exception);
            return;
        }
        try
        {
            s.close();
        }
        catch(IOException ioexception)
        {
            System.out.println(ioexception);
        }
    }

    public static void main(String args[])
    {
        Client client = new Client("127.0.0.1", 5000);
    }
}