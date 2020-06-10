package tech.ibit.mybatis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页列表
 *
 * @author IBIT程序猿
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalList<T> {

    /**
     * 数据列表
     */
    private List<T> list;

    /**
     * 总条数
     */
    private Integer total;
}
