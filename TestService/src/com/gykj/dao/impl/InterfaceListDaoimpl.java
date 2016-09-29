package com.gykj.dao.impl;


import org.springframework.stereotype.Repository;

import com.gykj.dao.InterfaceListDao;
import com.gykj.pojo.InterfaceList;




/**
 * 接口清单  持久层实现类
 * 
 * @author xianyl
 * 
 * @param <T>
 */
@Repository(value = "interfaceListDao")
public class InterfaceListDaoimpl extends MongoDaoimpl<InterfaceList> implements InterfaceListDao {

}
