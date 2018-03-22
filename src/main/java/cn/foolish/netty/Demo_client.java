package cn.foolish.netty;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Demo_client {

	public static void main(String[] args) {
//		192.168.8.126
		String ipAddr = "127.0.0.1";
		int port = 8379;
		new Demo_client().run(ipAddr, port);
	}
	
	public void run(String ipAddress, int port){
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try{
			Bootstrap bootstrap = new Bootstrap();
			
			bootstrap.group(workerGroup)
			.channel(NioSocketChannel.class)
			.handler(this.initChannelImpl());
			ChannelFuture future = bootstrap.connect(ipAddress, port).sync();
			Channel channel = future.channel();
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			while(true){
				String request = in.readLine();
				if(request.equals("quit")){
					break;
				}
				channel.writeAndFlush(Unpooled.copiedBuffer(request.getBytes()));
			}
			
			channel.closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
		}
	}

	private ChannelInitializer<SocketChannel> initChannelImpl(){
		return new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				socketChannel.pipeline().addLast(new Demo_clientHandler());
			}
		};
	}
}