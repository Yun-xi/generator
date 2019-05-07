package com.shushuo.generator.dao;

import com.shushuo.generator.entity.TableQueryEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 数据库接口
 *
 * @author Mark sunlightcs@gmail.com
 */
@Repository
public interface GeneratorDao {
    List<TableQueryEntity> queryList(@Param("tableSchema") String tableSchema, @Param("tableName") String tableName);

    Map<String, String> queryTable(@Param("tableSchema") String tableSchema, @Param("tableName") String tableName);

    List<Map<String, String>> queryColumns(@Param("tableSchema") String tableSchema, @Param("tableName") String tableName);
}
