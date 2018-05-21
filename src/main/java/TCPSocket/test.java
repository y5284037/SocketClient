package TCPSocket;

import java.io.*;
import java.util.Random;

/**
 * @author: Arike
 * @program: SocketClient
 * @description: ss
 * @create: 2018/5/17 0017 15:39
 */

public class test {
    public static void main(String[] args) throws IOException {
        byte[] arr = new byte[510];
        Random random = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] =(byte) random.nextInt(128);
        }
        FileOutputStream fos = new FileOutputStream("data.txt",true);
        fos.write(arr);
        fos.write("\r\n".getBytes());
        fos.close();
    }
}
