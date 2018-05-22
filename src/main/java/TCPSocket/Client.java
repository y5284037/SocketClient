package TCPSocket;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

/**
 * @author: Arike
 * @program: SocketClient
 * @description: Socket客户端
 * @create: 2018/5/17 0017 14:34
 */

public class Client {
    private static Logger logger = Logger.getLogger(Client.class);
    private static Properties properties = new Properties();
    public static void main(String[] args) throws IOException {
        properties.load(Object.class.getResourceAsStream("/config.properties"));
    
        //客户端请求与本机在20006端口建立TCP连接
        Socket client = new Socket(properties.getProperty("host"), new Integer(properties.getProperty("hostProt")));
        logger.info("连接服务端成功");
        client.setSoTimeout(10000);
        //获取Socket的输出流，用来发送数据到服务端
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(properties.getProperty("dataLocation"), true)));
        boolean flag = true;
        while (flag) {
            try {
                byte[] arr = new byte[new Integer(properties.getProperty("dataSize"))];
                Random random = new Random();
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = (byte) (random.nextInt(94) + 33);
                }
                out.write(arr);
                bw.write(new String(arr));
                bw.newLine();
                bw.flush();
                logger.info("Data Send Success");
                Thread.sleep(1000);
            } catch (SocketException e) {
                boolean a = true;
                while (a) {
                    try {
                        client = new Socket(properties.getProperty("host"), new Integer(properties.getProperty("hostProt")));
                        logger.info("连接服务端成功");
                        a = false;
                    } catch (SocketException e1) {
                        try {
                            logger.info("断开连接，等待重连.");
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
        if (client != null) {
            //如果构造函数建立起了连接，则关闭套接字，如果没有建立起连接，自然不用关闭
            bw.close();
            client.close(); //只关闭socket，其关联的输入输出流也会被关闭
        }
    }
}
