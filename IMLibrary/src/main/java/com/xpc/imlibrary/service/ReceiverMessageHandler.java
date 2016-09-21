package com.xpc.imlibrary.service;

import com.xpc.myimdemo.util.MyLog;
import com.xpc.myimdemo.util.ResolveMessageUtil;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;


/**
 * 客户端消息接收处理
 * 
 * @author qiaocb
 * @time 2015-12-3下午4:47:27
 */
public class ReceiverMessageHandler implements IoHandler {
	/**
	 * 当接收到客户端的请求信息后触发此方法
	 */
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		ResolveMessageUtil.resolveMessage(message.toString(), false);
	}

	/**
	 * 当接口中其他方法抛出异常未被捕获时触发此方法
	 */
	public void exceptionCaught(IoSession arg0, Throwable arg1)
			throws Exception {
		MyLog.i("客户+异常："+arg1.getMessage());// 显示接收到的消息
	}

	/**
	 * 当信息已经传送给客户端后触发此方法
	 */
	public void messageSent(IoSession arg0, Object arg1) throws Exception {
		MyLog.i("========messageSent========");
	}

	/**
	 * 当连接被关闭时触发，例如客户端程序意外退出等等
	 */
	public void sessionClosed(IoSession arg0) throws Exception {
		MyLog.i("=====sessionClosed========");
	}

	/**
	 * 当一个新客户端连接后触发此方法
	 */
	public void sessionCreated(IoSession arg0) throws Exception {
		MyLog.i("=======sessionCreated======");
	}

	/**
	 * 当连接空闲时触发此方法
	 */
	public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {
		MyLog.i("----sessionIdle---空闲--");
	}

	/**
	 * 当连接后打开时触发此方法，一般此方法与 sessionCreated 会被同时触发
	 */
	public void sessionOpened(IoSession arg0) throws Exception {
		MyLog.i("----sessionOpened---连接打开--");
	}
}
