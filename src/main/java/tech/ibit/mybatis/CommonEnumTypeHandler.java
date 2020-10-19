package tech.ibit.mybatis;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 通用枚举类处理器（在ResultMap定义映射枚举字段，typeHandler类型）
 *
 * @author iBit程序猿
 */
public class CommonEnumTypeHandler<E extends CommonEnum> extends BaseTypeHandler<E> {

    /**
     * 枚举类
     */
    private final Class<E> type;

    /**
     * 构造函数
     *
     * @param type 枚举类
     */
    public CommonEnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
        E[] enums = type.getEnumConstants();
        if (enums == null) {
            throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getValue());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs == null ? null : CommonEnum.getEnum(type, rs.getInt(columnName));
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs == null ? null : CommonEnum.getEnum(type, rs.getInt(columnIndex));
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs == null ? null : CommonEnum.getEnum(type, cs.getInt(columnIndex));
    }
}
