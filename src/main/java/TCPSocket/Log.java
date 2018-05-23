package TCPSocket;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author: Arike
 * @program: SocketServer
 * @description: 功能性Log
 * @create: 2018/5/22 0022 10:30
 */

public class Log {
    
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH`mm`ss");
    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
    private static BufferedWriter dataWriter;
    private static BufferedWriter runtimeInfoWriter;
    private static Properties config =new Properties();
    
    /**
     * 加载配置文件
     */
    static {
        try {
            config.load(new FileInputStream(System.getProperty("user.dir")+"/config.properties"));
//            config.load(Object.class.getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 生成客户端连接成功时间的日志文件,并给相应的输出流赋值
     * <p>
     * |--Data.txt
     * 2018-5-12 10:30:30---
     * |--RuntimeInfo.txt
     *
     * @return 返回两个文件的相对路径数组
     */
    public static void buildLogsFile() {
        try {
            if(dataWriter!=null){
                dataWriter.close();
            }
            if(runtimeInfoWriter!=null){
                runtimeInfoWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        String path = "logs/" + sdf.format(new Date());
        File dir = new File(path);
        dir.mkdirs();
        //System.out.println(System.getProperty("user.dir"));
        File dataTxt = new File(path + "/Data.txt");
        File runtimeInfoTxt = new File(path + "/RuntimeInfo.txt");
        try {
            dataTxt.createNewFile();
            runtimeInfoTxt.createNewFile();
            dataWriter = new BufferedWriter(new FileWriter(dataTxt.getPath(), true));
            runtimeInfoWriter = new BufferedWriter(new FileWriter(runtimeInfoTxt.getPath(), true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 用于打印Data
     *
     * @param arr
     */
    public static void dataInfo(byte[] arr) {
        try {
            dataWriter.write(new String(arr));
            dataWriter.newLine();
            dataWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 用于打印RuntimeInfo日志
     *
     * @param message 需要输出的日志信息
     */
    public static void runtimeInfo(String message) {
        String info = "[INFO ]" + sdf1.format(new Date());
        System.out.println(info);
        System.out.println(message);
        try {
            runtimeInfoWriter.write(info);
            runtimeInfoWriter.newLine();
            runtimeInfoWriter.write(message);
            runtimeInfoWriter.newLine();
            runtimeInfoWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
