package tech.ibit.mybatis.sqlbuilder;


/**
 * 条件内容对象
 *
 * @author IBIT程序猿
 * @version 1.0
 */
public interface CriteriaItem extends PrepareStatementSupplier {

    /**
     * 生成or条件
     *
     * @return or条件
     */
    default Criteria or() {
        return Criteria.or(this);
    }

    /**
     * 生成and条件
     *
     * @return and条件
     */
    default Criteria and() {
        return Criteria.and(this);
    }

}
