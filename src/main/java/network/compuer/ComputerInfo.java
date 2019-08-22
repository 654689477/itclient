package network.compuer;

import com.fasterxml.jackson.databind.util.JSONPObject;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 获取mac ip 电脑名 外网IP
 */
public class ComputerInfo {

    private static String macAddressStr = null;
    private static String computerName = System.getenv().get("COMPUTERNAME");
    private static final String[] windowsCommand = {"ipconfig", "/all"};
    private static final String[] linuxCommand = {"/sbin/ifconfig", "-a"};
    private static final Pattern macPattern = Pattern.compile(".*((:?[0-9a-f]{2}[-:]){5}[0-9a-f]{2}).*",
            Pattern.CASE_INSENSITIVE);

    /**
     * 接口超时时间
     */
    private static final Integer TIME_OUT = 100000;
    public static String INTRANET_IP = getIntranetIp(); // 内网IP
    public static String INTERNET_IP = getV4IP(); // 外网IP

    /**
     * 获取多个网卡地址
     *
     * @return
     * @throws IOException
     */
    private final static List<String> getMacAddressList() throws IOException {
        final ArrayList<String> macAddressList = new ArrayList<String>();
        final String os = System.getProperty("os.name");
        final String command[];

        if (os.startsWith("Windows")) {
            command = windowsCommand;
        } else if (os.startsWith("Linux")) {
            command = linuxCommand;
        } else {
            throw new IOException("Unknow operating system:" + os);
        }
        // 执行命令
        final Process process = Runtime.getRuntime().exec(command);

        BufferedReader bufReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        for (String line = null; (line = bufReader.readLine()) != null; ) {
            Matcher matcher = macPattern.matcher(line);
            if (matcher.matches()) {
                macAddressList.add(matcher.group(1));
                // macAddressList.add(matcher.group(1).replaceAll("[-:]",
                // ""));//去掉MAC中的“-”
            }
        }
        process.destroy();
        bufReader.close();
        return macAddressList;
    }

    /**
     * 获取一个网卡地址（多个网卡时从中获取一个）
     *
     * @return
     */
    public static String getMacAddress() {
        if (macAddressStr == null || macAddressStr.equals("")) {
            StringBuffer sb = new StringBuffer(); // 存放多个网卡地址用，目前只取一个非0000000000E0隧道的值
            try {
                List<String> macList = getMacAddressList();
                for (Iterator<String> iter = macList.iterator(); iter.hasNext(); ) {
                    String amac = iter.next();
                    if (!amac.equals("0000000000E0")) {
                        sb.append(amac);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            macAddressStr = sb.toString();
        }
        return macAddressStr;
    }

    /**
     * 获取电脑名
     *
     * @return
     */
    public static String getComputerName() {
        if (computerName == null || computerName.equals("")) {
            computerName = System.getenv().get("COMPUTERNAME");
        }
        return computerName;
    }

    /**
     * 限制创建实例
     */
    private ComputerInfo() {

    }

    /**
     * 获得内网IP
     *
     * @return 内网IP
     */
    private static String getIntranetIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args){

        System.out.println(getV4IP());
    }
    /**
     * 获得外网IP
     *
     * @return 外网IP
     */
    private static String getV4IP() {
        String ip = "";
        String chinaz = "http://ip.chinaz.com";
        // String chinaz = "http://www.ip138.com";
        StringBuilder inputLine = new StringBuilder();
        String read = "";
        URL url = null;
        HttpURLConnection urlConnection = null;
        BufferedReader in = null;
        try {
            url = new URL(chinaz);
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(TIME_OUT);
                urlConnection.setReadTimeout(TIME_OUT);
                in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            } catch (Exception e) {
                //如果超时，则返回内网ip
                return INTRANET_IP;
            }
            while ((read = in.readLine()) != null) {
                inputLine.append(read + "\r\n");
            }
            //System.out.println(inputLine.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
        Matcher m = p.matcher(inputLine.toString());
        if (m.find()) {
            String ipstr = m.group(1);
            ip = ipstr;
            //System.out.println(ipstr);
        }
        if ("".equals(ip)) {
            // 如果没有外网IP，就返回内网IP
            return INTRANET_IP;
        }
        return ip;
    }

    //读取注册码
    public  static String getLicense()  {
        File file = new File("C:\\license.txt");
        FileReader f_reader=null;
        BufferedReader reader=null;
        String str= null;
        if(file .exists()){
            try {
                f_reader= new FileReader(file);
                reader = new BufferedReader(f_reader);
                str = reader.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if(reader !=null){
                    try {
                        f_reader.close();
                        reader.close();
                    }catch (Exception e){

                    }
                }
            }
        }
        return str;
    }

    /**
     *
     * @return
     */
    //调用注册码中心接口上次电脑信息
    public static  String uploadComputerInfo() {
        String license = getLicense();
        String mac = getMacAddress();
        String ip = getV4IP();
        String hostname = getComputerName();
        //String urlStr = "http://localhost:8080/api/info/pc/message?computerINfo="+info;
       // String urlStr ="http://10.88.89.149:8082/gunsApi/connect?license=875a6439-64ca-4ee2-945e-6e7ec1dde7a3cm";
        String urlStr = "http://10.88.89.149:8082/gunsApi/connect?license="+license + "&mac="+mac+"&ip="+ip+"&hostname="+hostname;
        System.out.println(license);
        System.out.println(urlStr);
        //链接URL  
        BufferedReader reader = null;
        StringBuffer document = new StringBuffer();
        try {
          HttpURLConnection conn=  getPostHttpConn( urlStr);
            //读取返回结果集
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                document.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return document.toString();
    }

    /**
     * 写入TXT文件
     */
    public static void writeFile(String info) {
        try {
            File writeName = new File("C:\\Users\\dell\\Desktop\\output.txt"); // 相对路径，如果没有则要建立一个新的output.txt文件
            writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
            try (FileWriter writer = new FileWriter(writeName);
                 BufferedWriter out = new BufferedWriter(writer)
            ) {
                out.write(info); // \r\n即为换行
                out.write("\r\n"+info); // \r\n即为换行
                out.flush(); // 把缓存区内容压入文件
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 通过url获取post方法的http连接
     * @param url
     * @return
     * @throws Exception
     */
    public static HttpURLConnection getPostHttpConn(String url) throws Exception {
        URL object = new URL(url);
        HttpURLConnection conn;
        conn = (HttpURLConnection) object.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestMethod("POST");
        return conn;
    }


}
