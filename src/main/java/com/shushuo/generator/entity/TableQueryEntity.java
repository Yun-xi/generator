package com.shushuo.generator.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 表查询
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableQueryEntity {

    private String tableSchema;

    private String tableName;

    private String engine;

    private String tableComment;

    private Date createTime;
}
