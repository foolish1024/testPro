package cn.foolish.socket.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TcpSocket_client {
	public static void main(String[] args) throws IOException{
		Socket socket = new Socket("127.0.0.1", 8888);
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	    PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
	    BufferedReader reader = new BufferedReader(new InputStreamReader(
	            socket.getInputStream()));
	    while (true) {
	        String line = br.readLine();// 获取键盘所输入的字符串
	        pw.println(line);
	        if (line.equals("over")) {
	            break;
	        }
	        System.out.println(reader.readLine());// 获取服务端传过来的大写字符串
	    }
	    reader.close();
	    br.close();
	    pw.close();
	    socket.close();
	}
}
