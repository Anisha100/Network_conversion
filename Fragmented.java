import java.awt.FileDialog;
import java.awt.Frame;
import java.io.*;
import java.util.*;
import java.util.zip.CRC32;
public class Fragmented {

    public static List<byte[]> sender_fragmentation(byte[] file) {
        int max = 500;
        List<byte[]> fragments = new ArrayList<>();
        int start = 0;
        int count = 0;
        int count2=0;
        CRC32 crc = new CRC32();
        crc.update(file);
        System.out.println("Sender CRC32: " + crc.getValue());
        int check=(int)crc.getValue();

        while (start < file.length) {
            int end = Math.min(file.length, start + max);
            int length = end - start;
            count=count2;
            // Allocate fragment with +4 bytes for index
            byte[] fragment = new byte[length + 4];
           fragment[0]=(byte)(count%256);
           count=count/256;
           fragment[1]=(byte)(count%256);
           count=count/256;
           fragment[2]=(byte)(count%256);
           count=count/256;
           fragment[3]=(byte)count;
             //System.out.println(fragment[0]+" "+fragment[1]+" "+fragment[2]+" "+fragment[3]);

            for (int i = 0; i < length; i++) {
                fragment[i + 4] = file[start + i];
            }

            fragments.add(fragment);
            count2++;
            start += length;
        }

        return fragments;
    }

    public static List<byte[]> network_randomizer(List<byte[]> fragments) {
        List<byte[]> shuffled = new ArrayList<>(fragments);
        Collections.shuffle(shuffled, new Random());
        return shuffled;
    }

    public static byte[] receiver_reassembly(List<byte[]> fragments) throws Exception {
        byte[][] orderedFragments = new byte[fragments.size()][];
        int totalLength = 0;

        for (byte[] fragment : fragments) {
            int index=((fragment[3]&0xFF)*(int)Math.pow(256,3));
            index+=((fragment[2]&0xFF)*(int)Math.pow(256,2));
            index+=((fragment[1]&0xFF)*(int)Math.pow(256,1));
            index+=fragment[0]&0xFF;


            byte[] data = Arrays.copyOfRange(fragment, 4, fragment.length);

            orderedFragments[index] = data;
            totalLength += data.length;
        }

        

        byte[] reassembled = new byte[totalLength];
        int pos = 0;
        for (int i = 0; i < orderedFragments.length; i++) {
            byte[] data = orderedFragments[i];
            if (data == null) {
                throw new RuntimeException("Missing fragment at index: " + i);
            }
            System.arraycopy(data, 0, reassembled, pos, data.length);
            pos += data.length;
        }
        


        CRC32 crc = new CRC32();
        crc.update(reassembled);
        System.out.println("Receiver CRC32: " + crc.getValue());

        return reassembled;
    }
     public static byte[]  givenFile_whenUsingFileInputStreamClass_thenConvert(String FILE_NAME,String folder) throws IOException {
        
        File myFile = new File(folder+FILE_NAME);
        byte[] byteArray = new byte[(int) myFile.length()];
        try (FileInputStream inputStream = new FileInputStream(myFile)) {
            inputStream.read(byteArray);
        }
        return byteArray;
     }

     public static void write_to_file(byte[] array) throws Exception
     {
        String file="";
     HashMap<Integer, int[]> fileSignatures = new HashMap<>();

        fileSignatures.put(1, new int[]{0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A});//p
        fileSignatures.put(2, new int[]{0xFF, 0xD8, 0xFF, 0xE0});
        fileSignatures.put(3, new int[]{0x50, 0x4B, 0x03, 0x04, 0x14, 0x00, 0x06, 0x00});
        fileSignatures.put(4, new int[]{0x25, 0x50, 0x44, 0x46});
        fileSignatures.put(5, new int[]{0x49, 0x44, 0x43});
        fileSignatures.put(6, new int[]{0x00,0x00,0x00, 0x1c,0x66, 0x74, 0x79, 0x70});
// now nested loop , in first loop we loop hashmap, in next we loop array inside hahmap
    int count =0;
     for(int i=1;i<=6;i++)
     {
        boolean a=true;

       for (int b=0;b<(fileSignatures.get(i).length);b++)
       {
        int temp=array[b]&0xFF;
        System.out.println(temp);
        int[] arr=fileSignatures.get(i);

        if((array[b]&0xFF)!=arr[b])
        {
            System.out.println(arr[b]);
            System.out.println(array[b]&0xFF);
            System.err.println("False");
            a=false;
        break;
        }
        count=i;
        System.out.println("printing in loop" +i);
        System.out.println(b);
        System.out.println(array[b]&0xFF);
        System.out.println(arr[b]);
        System.out.println(count);

       }


   
if (count == 1)
    file = ".png";
else if (count == 2)
    file = ".jpg";
else if (count == 3)
    file = ".docx";
else if (count == 4)
    file = ".pdf";
else if (count == 5)
    file = ".mp3";
else if (count == 6)
    file = ".mp4";
else{
    System.out.println(count);
    file = ".txt"; // default for unknown
}
if(a)
break;
     }

     
        
       
        
        try(FileOutputStream output = new FileOutputStream("converted"+file)){
            output.write(array);
            System.out.println("converted"+file);
        }


     }

     public static String[] file_mani() throws Exception
     {
    FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
    
    dialog.setMode(FileDialog.LOAD);
    dialog.setVisible(true);
    String file = dialog.getFile();
    String folder=dialog.getDirectory();
    dialog.dispose();
    System.out.println(file + " chosen.");  
    return new String[]{file,folder};

    
    
     }

    public static void main(String[] args) throws Exception {
        //byte[] bytearray = new byte[800000];
        String file="";
        String folder="";
    String[] arr= new String[2];
    arr= file_mani();   
    file=arr[0];
    folder=arr[1];
        byte[] bytearray= givenFile_whenUsingFileInputStreamClass_thenConvert(file,folder);
        List<byte[]> abc = sender_fragmentation(bytearray);
    List<byte[]> bcd = network_randomizer(abc);
    byte[] reassembled= receiver_reassembly(bcd);
    write_to_file(reassembled);
        //Now i will make this as user selection where user just selects the file
        
        
        
    }
}
