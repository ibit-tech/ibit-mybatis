package tech.ibit.sqlbuilder.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Page
 *
 * @author IBIT程序猿
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Page<T> {

    /**
     * 开始位置
     */
    private int start;

    /**
     * 返回条数
     */
    private int limit;

    /**
     * 总条数
     */
    private int total;

    /**
     * 返回对象列表
     */
    private List<T> results;
}
