package tech.ibit.mybatis.sqlbuilder.sql.support.statement;

import tech.ibit.mybatis.sqlbuilder.PrepareStatement;
import tech.ibit.mybatis.sqlbuilder.SetItem;
import tech.ibit.mybatis.sqlbuilder.sql.support.SetSupport;
import tech.ibit.mybatis.utils.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * DefaultSetSupport
 *
 * @author IBIT程序猿
 */
public interface DefaultSetSupport<T> extends SetSupport<T>, DefaultPrepareStatementSupport {

    /**
     * 获取预查询SQL对象
     *
     * @param useAlias 是否使用别名
     * @return 预查询SQL对象
     */
    default PrepareStatement getSetItemPrepareStatement(boolean useAlias) {
        List<SetItem> setItems = getSet().getItems();
        if (CollectionUtils.isEmpty(setItems)) {
            return new PrepareStatement("", Collections.emptyList());
        }

        return getPrepareStatement(" SET ", setItems, ", ", useAlias);
    }
}