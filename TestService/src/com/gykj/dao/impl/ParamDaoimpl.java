package com.gykj.dao.impl;


import org.springframework.stereotype.Repository;

import com.gykj.dao.ParamDao;
import com.gykj.pojo.Param;




/**
 * 参数字段  持久层实现类
 * 
 * @author xianyl
 * 
 * @param <T>
 */
@Repository(value = "paramDao")
public class ParamDaoimpl extends MongoDaoimpl<Param> implements ParamDao {

}
