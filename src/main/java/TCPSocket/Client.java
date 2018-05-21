package TCPSocket;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;

/**
 * @author: Arike
 * @program: SocketClient
 * @description: Socket客户端
 * @create: 2018/5/17 0017 14:34
 */

public class Client {
    public static void main(String[] args) throws IOException {
        //客户端请求与本机在20006端口建立TCP连接
        Socket client = new Socket("127.0.0.1", 20006);
        client.setSoTimeout(10000);
        //获取Socket的输出流，用来发送数据到服务端
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        
        boolean flag = true;
        while(flag){
            try {
                byte[] arr = new byte[510];
                Random random = new Random();
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = (byte) random.nextInt(128);
                }
                out.write(arr);
                FileOutputStream fos = new FileOutputStream("data.txt", true);
                fos.write(arr);
                fos.write("\r\n".getBytes());
                fos.close();
                Thread.sleep(1000);
            } catch (SocketException e) {
                boolean a = true;
                while (a) {
                    try {
                        client = new Socket("127.0.0.1", 20006);
                        a = false;
                    } catch (SocketException e1) {
                        System.out.println("连接失败,5秒后重连");
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                    }
                    client.setSoTimeout(10000);
                    //获取Socket的输出流，用来发送数据到服务端
                    out = new DataOutputStream(client.getOutputStream());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(client != null){
            //如果构造函数建立起了连接，则关闭套接字，如果没有建立起连接，自然不用关闭
            client.close(); //只关闭socket，其关联的输入输出流也会被关闭
        }
    }
    
    
}
