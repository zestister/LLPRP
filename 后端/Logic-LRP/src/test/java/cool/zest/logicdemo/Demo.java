package cool.zest.logicdemo;
import com.jcraft.jsch.*;

import java.io.*;

public class Demo {

    public static void main(String[] args)  {
        try {
            JSch jsch = new JSch();
            // 连接到远程服务器
            String username = "root";
            String host = "8.137.57.246";
            int port = 22; // SSH端口，默认为22
            Session session = jsch.getSession(username, host, port);
            session.setPassword("540788Iknow");
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            // 在远程服务器上执行命令
            String formula="dgf ";
            String command = "/usr/LLRP/CTL/ctl2snf '" + formula + "'";
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);

            // 获取命令执行结果
            InputStream in = channel.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            channel.connect();

            // 解析命令执行结果
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }

            // 关闭连接
            channel.disconnect();
            session.disconnect();

            // 根据返回结果判断Mu公式的真假
            String output = result.toString().trim();
            System.out.println(output);
            return ;
        } catch (JSchException | IOException e) {
            // 发生异常，返回false
            return ;
        }
    }
    // 检查输入字符串中是否包含时序算子
}