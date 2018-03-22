package cn.foolish.socket.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpSocket_server {
	public static void main(String[] args) throws IOException{
		// 1.创建一个ServerSocket对象
	    ServerSocket serverSocket = new ServerSocket(8888);
	    // 2.调用accept()方法接受客户端请求
	    Socket socket = serverSocket.accept();
	    System.out.println(socket.getInetAddress().getHostAddress() + "连接成功");
	    // 3.获取socket对象的输入输出流
	    BufferedReader br = new BufferedReader(new InputStreamReader(
	            socket.getInputStream()));

	    PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
	    String line = null;
	    // 读取客户端传过来的数据
	    while ((line = br.readLine()) != null) {
	        if (line.equals("over")) {
	            break;
	        }
	        System.out.println(line);
	        pw.println(line.toUpperCase());
	    }

	    pw.close();
	    br.close();
	    socket.close();
	    System.out.println(socket.getInetAddress().getHostAddress() + "断开连接");
	}
}
