package com.gykj.tcp;

import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Repository;

import com.gykj.tcp.TCPServer.TCPServerListener;





/**
 *TCP服务 的控制
 * @author xianyl
 * 
 */
@Repository(value="tcpControl")
public class TcpControl implements TCPServerListener{
	//tcp服务对象
	private TCPServer tCPServer;
	private int port = 12015;//服务器默认端口


	/**
	 * 使用默认端口12015
	 * @return
	 */
	public boolean openTcp(){
		return openTcp(port);
	}
	
	
	/**
	 * 打开tcp服务
	 * @param port
	 * @return 是否打开成功
	 */
	public boolean openTcp(int port){
		if(tCPServer == null)
			tCPServer = new TCPServer(this);
		return Constant.RETURN_CODE_SUCCESS == tCPServer.open(port);
	}
	
	
	/**
	 * 关闭tcp服务
	 * @return
	 */
	public void closeTcp(){
		if(tCPServer != null){
			tCPServer.close();
			tCPServer = null;
		}
	}
	
	
	/**
	 * 发送消息到客户端
	 * @param clent_id
	 * @param tackJson
	 * @return
	 */
	public int sendResult(IoSession session, String tackJson) {
		return sendResult(session, Constant.CODE_PACKAGEMESSAGE, tackJson);
	}
	/**
	 * 发送消息到客户端
	 * @param clent_id
	 * @param type
	 * @param tackJson
	 * @return
	 */
	public int sendResult(IoSession session, byte type, String tackJson) {
		return tCPServer == null 
				? Constant.RETURN_CODE_FAIL 
						: tCPServer.sendResult(session, type, tackJson);
		
	}
	
	
	
	
	//--------------------tcp服务监听------------------------------------
	@Override
	public void getStask(IoSession session, String json) {
		//他TODO 收到消息,这里要做处理啊
		
	}

	@Override
	public void getAck(IoSession session, byte type) {
		// 收到应答，客户端暂时只有收到消息类型的应答返回
		
	}
	




}
