package com.gykj.dao.impl;



import org.springframework.stereotype.Repository;

import com.gykj.dao.TcpDao;
import com.gykj.pojo.Tcp;




/**
 * tcp  持久层实现类
 * 
 * @author xianyl
 * 
 * @param <T>
 */
@Repository(value = "tcpDao")
public class TcpDaoimpl extends MongoDaoimpl<Tcp> implements TcpDao {
	
	
}
