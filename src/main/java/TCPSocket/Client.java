package TCPSocket;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;
import java.util.Random;

/**
 * @author: Arike
 * @program: SocketClient
 * @description: Socket客户端
 * @create: 2018/5/17 0017 14:34
 */

public class Client {
    private static Properties config;
    private static String host;
    private static int hostPort;
    private static int dataSize;
    private static long DataInterval;
    private static long ReconnectInterval;
    private static Socket client;
    private static DataOutputStream socketDataOut;
    
    public static void main(String[] args) throws InterruptedException {
        try {
            init();//进行程序初始化,加载配置
        } catch (Exception e) {
            System.out.println("程序化初始化失败,请确认config.properties在当前目录下,并重新运行程序");
            return;
        }
        
        try {
            connectToServer();//连接到服务端并获取数据输出流
        } catch (IOException e) {
            System.out.println("连接服务器失败,请确保服务器已运行,并重新运行程序!");
            return;
        }
        
        Log.buildLogsFile();//生成日志文件以及对应的流
        Log.runtimeInfo("成功连接到服务器！");//将runtimeInfo输出到RuntimInfo.txt中
        while (true) {
            try {
                byte[] arr = new byte[dataSize];
                Random random = new Random();
                for (int i = 0; i < arr.length; i++) {
                    //因为ASCII字符集只有33-126的字符集是可以正确解析的,所以随机数的取值范围从33-126
                    arr[i] = (byte) (random.nextInt(94) + 33);
                }
                socketDataOut.write(arr);//try是为了处理发送数据的这行代码,如果发送失败会进入Catch阶段.
                Log.dataInfo(arr);//将发送的data数据输出到data.txt中
                Log.runtimeInfo("Data Send Success");
                Thread.sleep(DataInterval);
            } catch (IOException e) {
                Log.runtimeInfo("与服务器断开连接，即将尝试重新连接.");
                Log.buildLogsFile();//生成新的日志文件以及对应的流
                while (true) {
                    try {
                        connectToServer();//重新获取data输出流
                        Log.runtimeInfo("成功连接到服务器！");
                        break;
                    } catch (IOException e1) {
                        Log.runtimeInfo("连接失败，尝试重新连接.");
                        Thread.sleep(ReconnectInterval);
                    }
                }
            }
        }
    }
    
    /**
     * 对成员变量进行初始化(配置的导入)
     *
     * @throws IOException
     */
    private static void init() throws IOException {
        config = new Properties();
//        config.load(Object.class.getResourceAsStream("/config.properties"));
        config.load(new FileInputStream(System.getProperty("user.dir")+"/config.properties"));
        dataSize = Integer.valueOf(config.getProperty("dataSize"));
        ReconnectInterval = Long.valueOf(config.getProperty("ReconnectInterval"));
        DataInterval = Long.valueOf(config.getProperty("DataInterval"));
        host = config.getProperty("host");
        hostPort = Integer.valueOf(config.getProperty("hostPort"));
    }
    
    /**
     * 获取给服务器发送数据的输出流
     *
     * @return
     * @throws IOException
     */
    private static void connectToServer() throws IOException {
        client = new Socket(host, hostPort);
        socketDataOut = new DataOutputStream(client.getOutputStream());
    }
}
