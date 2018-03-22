package cn.foolish.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpSocket_receive{
	public void receive() throws IOException{
		// 先接收数据
        DatagramSocket socket = new DatagramSocket(6666);
        byte[] buff = new byte[100];
        DatagramPacket packet = new DatagramPacket(buff, 100);
        socket.receive(packet);// 接受传来的数据包
        System.out.println(new String(buff, 0, packet.getLength()));

        // 发送数据
        String str = "me too";
        DatagramPacket packet2 = new DatagramPacket(str.getBytes(),
                str.length(), packet.getAddress(), packet.getPort());
        socket.send(packet2);
        socket.close();
	}
}
