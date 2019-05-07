package com.shushuo.generator.service;

import com.shushuo.generator.dao.GeneratorDao;
import com.shushuo.generator.entity.TableQueryEntity;
import com.shushuo.generator.utils.GenUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service
public class SysGeneratorService {
	@Autowired
	private GeneratorDao generatorDao;

	public List<TableQueryEntity> queryList(String tableSchema, String tableName) {
		List<TableQueryEntity> list = generatorDao.queryList(tableSchema, tableName);

		return list;
	}

	public Map<String, String> queryTable(String tableSchema, String tableName) {
		return generatorDao.queryTable(tableSchema, tableName);
	}

	public List<Map<String, String>> queryColumns(String tableSchema, String tableName) {
		return generatorDao.queryColumns(tableSchema, tableName);
	}

	public byte[] generatorCode(String tableSchema, String tableName) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);

		//查询表信息
		Map<String, String> table = queryTable(tableSchema, tableName);
		//查询列信息
		List<Map<String, String>> columns = queryColumns(tableSchema, tableName);
		//生成代码
		GenUtils.generatorCode(table, columns, zip);

		IOUtils.closeQuietly(zip);
		return outputStream.toByteArray();
	}
}
