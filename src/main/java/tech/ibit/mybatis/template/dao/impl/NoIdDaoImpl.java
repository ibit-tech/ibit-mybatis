package tech.ibit.mybatis.template.dao.impl;


import tech.ibit.mybatis.template.dao.NoIdDao;

/**
 * 没有主键的表的DAO实现
 *
 * @param <P> 实体类型
 * @author IBIT程序猿
 */
public abstract class NoIdDaoImpl<P> extends AbstractDaoImpl<P> implements NoIdDao<P> {
}
