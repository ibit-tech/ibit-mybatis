package tech.ibit.mybatis.exception;

/**
 * 拆分的表不存在
 *
 * @author IBIT TECH
 */
public class ShardingTableNotFoundException extends RuntimeException {

    public ShardingTableNotFoundException(String tableName) {
        super(tableName + " not found!");
    }
}
