package tech.ibit.mybatis.sqlbuilder.sql.support.statement;

import tech.ibit.mybatis.sqlbuilder.ColumnValue;
import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.SimpleNameColumn;
import tech.ibit.mybatis.sqlbuilder.sql.field.LimitField;
import tech.ibit.mybatis.sqlbuilder.sql.support.LimitSupport;

import java.util.Arrays;
import java.util.List;

/**
 * DefaultLimitSupport
 *
 * @author IBIT程序猿
 */
public interface DefaultLimitSupport<T> extends LimitSupport<T> {

    /**
     * 获取预查询SQL对象
     *
     * @return 预查询SQL对象
     */
    default PrepareStatement getLimitPrepareStatement() {
        LimitField limitField = getLimit();
        int limit = limitField.getLimit();
        if (limit < 0) {
            return PrepareStatement.empty();
        }

        int start = limitField.getStart();

        String prepareSql = " LIMIT ?, ?";
        List<ColumnValue> values = Arrays.asList(
                new ColumnValue(new SimpleNameColumn("$start"), start),
                new ColumnValue(new SimpleNameColumn("$limit"), limit));
        return new PrepareStatement(prepareSql, values);
    }

}