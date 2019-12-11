package tech.ibit.mybatis.template.dao;

/**
 * 没有主键的表的DAO
 *
 * @param <P> 实体类型
 * @author IBIT TECH
 */
public interface NoIdDao<P> {

    /**
     * 插入对象
     *
     * @param po 需要插入的对象
     * @return 插入条数
     */
    int insert(P po);
}
