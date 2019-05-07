package com.shushuo.generator.entity;

import java.util.Date;

/**
 * @author xieyaqi
 * @mail 987159036@qq.com
 * @date 2019-05-06 16:30
 */
public class TableQueryEntity {

    private String tableSchema;

    private String tableName;

    private String engine;

    private String tableComment;

    private Date createTime;

    public TableQueryEntity() {
    }

    public TableQueryEntity(String tableSchema, String tableName, String engine, String tableComment, Date createTime) {
        this.tableSchema = tableSchema;
        this.tableName = tableName;
        this.engine = engine;
        this.tableComment = tableComment;
        this.createTime = createTime;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
