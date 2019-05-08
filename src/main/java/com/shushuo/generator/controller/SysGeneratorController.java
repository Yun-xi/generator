package com.shushuo.generator.controller;

import com.shushuo.generator.entity.CodeDto;
import com.shushuo.generator.entity.TableQueryEntity;
import com.shushuo.generator.service.SysGeneratorService;
import com.shushuo.generator.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 代码生成器
 */
@Api(tags = "自动生成代码")
@RestController
@RequestMapping("/sys/generator")
public class SysGeneratorController {
	@Autowired
	private SysGeneratorService sysGeneratorService;
	
	/**
	 * 列表
	 */
	@ApiOperation("查询表列表")
	@GetMapping("/list")
	public R list(@RequestParam String tableSchema, @RequestParam String tableName){
		List<TableQueryEntity> tableQueryEntities = sysGeneratorService.queryList(tableSchema, tableName);

		return R.ok().put("list", tableQueryEntities);
	}
	
	/**
	 * 生成代码
	 */
	@ApiOperation("生成代码")
	@PostMapping("/code")
	public void code(@RequestBody CodeDto codeDto, HttpServletResponse response) throws IOException{
		byte[] data = sysGeneratorService.generatorCode(codeDto);
		
		response.reset();  
        response.setHeader("Content-Disposition", "attachment; filename=\"generator.zip\"");
        response.addHeader("Content-Length", "" + data.length);  
        response.setContentType("application/octet-stream; charset=UTF-8");  
  
        IOUtils.write(data, response.getOutputStream());  
	}
}
