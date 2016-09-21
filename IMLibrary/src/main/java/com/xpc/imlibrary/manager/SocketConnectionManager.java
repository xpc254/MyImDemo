package com.xpc.imlibrary.manager;


import com.xpc.imlibrary.config.IMConstant;
import com.xpc.imlibrary.service.ReceiverMessageHandler;
import com.xpc.imlibrary.util.MyLog;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;


/**
 * Socket服务器连接
 * 
 * @author qiaocb
 * @time 2015-11-12上午11:42:25
 */
public class SocketConnectionManager {
	private static SocketConnectionManager xmppConnectionManager;

	private static NioSocketConnector connector;

	private static IoSession ioSession;

	/** 30秒后超时 */
	private static final int IDELTIMEOUT = 30;
	/** 15秒发送一次心跳包 */
	private static final int HEARTBEATRATE = 15;

	public static NioSocketConnector getConnector() {
		if (null == connector) {
			// 创建非阻塞的server端的Socket连接
			connector = new NioSocketConnector();
		}
		return connector;
	}

	public static IoSession getIoSession() {
		return ioSession;
	}

	private SocketConnectionManager() {

	}

	public static SocketConnectionManager getInstance() {
		if (xmppConnectionManager == null) {
			xmppConnectionManager = new SocketConnectionManager();
		}
		return xmppConnectionManager;
	}

	/**
	 * 连接即时通讯
	 * 
	 * @param IP
	 * @param port
	 * @return
	 */
	public IoSession connect(String IP, int port) {
		try {
			disconnect();
			connector = new NioSocketConnector();
			connector.getFilterChain().addLast("codec",new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
			connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,IDELTIMEOUT);

			{// 心跳
//				KeepAliveMessageFactory heartBeatFactory = new KeepAliveMessageFactoryImpl();
//				KeepAliveRequestTimeoutHandler heartBeatHandler = new KeepAliveRequestTimeoutHandlerImpl();
//				KeepAliveFilter heartBeat = new KeepAliveFilter(
//						heartBeatFactory, IdleStatus.BOTH_IDLE,
//						heartBeatHandler);
//				/** 是否回发 */
//				heartBeat.setForwardEvent(true);
//				/** 发送频率 */
//				heartBeat.setRequestInterval(HEARTBEATRATE);
//				connector.getSessionConfig().setKeepAlive(true);
//				connector.getFilterChain().addLast("heartbeat", heartBeat);
			}

			connector.setHandler(new ReceiverMessageHandler());// 业务处理
			ConnectFuture cf = connector.connect(new InetSocketAddress(IP, port));
			cf.awaitUninterruptibly();// 等待连接创建完成
			try {
				ioSession = cf.getSession();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return ioSession;
		} catch (Exception e) {
			MyLog.e("socket connect error:" + e.getMessage());
			return null;
		}
	}

	/**
	 * 连接消息服务器
	 */
	public IoSession socketConnect() {
		return SocketConnectionManager.getInstance().connect(IMConstant.IM_IP,
				IMConstant.IM_PORT);
	}

	/**
	 * 销毁socket连接
	 */
	public void disconnect() {
		if (ioSession != null) {
			ioSession.close(true);
			ioSession = null;
		}
		if (connector != null) {
			connector.dispose();
			connector = null;
		}
	}
}
