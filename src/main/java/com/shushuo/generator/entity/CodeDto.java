package com.shushuo.generator.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 自动生成代码入参
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class CodeDto {

    @ApiModelProperty(value = "项目路径", example = "D://ceres")
    private String source;

    @ApiModelProperty(value = "库名", example = "ceres")
    private String tableSchema;

    @ApiModelProperty(value = "包名", example = "app")
    private String domainName;

    @ApiModelProperty(value = "表名", example = "[ss_app,ss_app_service]")
    private List<String> tableNames;
}
