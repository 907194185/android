package com.gykj.dao.impl;


import org.springframework.stereotype.Repository;

import com.gykj.dao.InterfaceGroupDao;
import com.gykj.pojo.InterfaceGroup;



/**
 * 接口组  持久层实现类
 * 
 * @author xianyl
 * 
 * @param <T>
 */
@Repository(value = "interfaceGroupDao")
public class InterfaceGroupDaoimpl extends MongoDaoimpl<InterfaceGroup> implements InterfaceGroupDao {

}
