package cn.foolish.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpSocket_send {
	public void send() throws IOException{
		DatagramSocket socket = new DatagramSocket();
        String str = "i love you";
        // 把数据进行封装到数据报包中
        DatagramPacket packet = new DatagramPacket(str.getBytes(),
                str.length(), InetAddress.getByName("localhost"), 6666);
        socket.send(packet);// 发送

        byte[] buff = new byte[100];
        DatagramPacket packet2 = new DatagramPacket(buff, 100);
        socket.receive(packet2);
        System.out.println(new String(buff, 0, packet2.getLength()));
        socket.close();
	}
}
