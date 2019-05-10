package com.shushuo.generator.utils;

import com.shushuo.generator.entity.ColumnEntity;
import com.shushuo.generator.entity.TableEntity;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器   工具类
 */
public class GenUtils {

	public static List<String> getTemplates(){
		List<String> templates = new ArrayList<String>();
		templates.add("template/SsBo.java.vm");
		templates.add("template/SsController.java.vm");
		templates.add("template/SsDeleteBo.java.vm");
		templates.add("template/SsMapper.java.vm");
		templates.add("template/SsMapper.xml.vm");
		templates.add("template/SsQueryBo.java.vm");
		templates.add("template/SsQueryRequest.java.vm");
		templates.add("template/SsQueryVo.java.vm");
		templates.add("template/SsRepository.java.vm");
		templates.add("template/SsRequest.java.vm");
		templates.add("template/SsService.java.vm");
		templates.add("template/SsServiceImpl.java.vm");
		return templates;
	}
	
	/**
	 * 生成代码
	 */
	public static void generatorCode(String source, Map<String, String> table,
			List<Map<String, String>> columns, String domainName){
		//配置信息
		Configuration config = getConfig();
		boolean hasBigDecimal = false;
		//表信息
		TableEntity tableEntity = new TableEntity();
		tableEntity.setTableName(table.get("tableName"));
		tableEntity.setComments(table.get("tableComment"));

		//表名转换成Java类名
		String className = tableToJava(tableEntity.getTableName(), config.getString("tablePrefix"));
		tableEntity.setClassName(className);
		tableEntity.setClassname(StringUtils.uncapitalize(className));
		
		//列信息
		List<ColumnEntity> columsList = new ArrayList<>();
		for(Map<String, String> column : columns){
			ColumnEntity columnEntity = new ColumnEntity();
			columnEntity.setColumnName(column.get("columnName"));
			columnEntity.setDataType(column.get("dataType"));
			columnEntity.setComments(column.get("columnComment"));
			columnEntity.setExtra(column.get("extra"));
			
			//列名转换成Java属性名
			String attrName = columnToJava(columnEntity.getColumnName());
			columnEntity.setAttrName(attrName);
			columnEntity.setAttrname(StringUtils.uncapitalize(attrName));
			
			//列的数据类型，转换成Java类型
			String attrType = config.getString(columnEntity.getDataType(), "unknowType");
			columnEntity.setAttrType(attrType);
			if (!hasBigDecimal && attrType.equals("BigDecimal" )) {
				hasBigDecimal = true;
			}
			//是否主键
			if("PRI".equalsIgnoreCase(column.get("columnKey")) && tableEntity.getPk() == null){
				tableEntity.setPk(columnEntity);
			}
			
			columsList.add(columnEntity);
		}
		tableEntity.setColumns(columsList);
		
		//没主键，则第一个字段为主键
		if(tableEntity.getPk() == null){
			tableEntity.setPk(tableEntity.getColumns().get(0));
		}

		// requestMapping
		String requestMapping = tableToRequestMapping(table.get("tableName"), config.getString("tablePrefix"));


		//设置velocity资源加载器
		Properties prop = new Properties();  
		prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");  
		Velocity.init(prop);

		String mainPath = config.getString("mainPath" );
		mainPath = StringUtils.isBlank(mainPath) ? "com.shushuo" : mainPath;
		
		//封装模板数据
		Map<String, Object> map = new HashMap<>(16);
		map.put("tableName", tableEntity.getTableName());
		map.put("comments", tableEntity.getComments());
		map.put("pk", tableEntity.getPk());
		map.put("className", tableEntity.getClassName());
		map.put("classname", tableEntity.getClassname());
		map.put("pathName", tableEntity.getClassname().toLowerCase());
		map.put("columns", tableEntity.getColumns());
		map.put("hasBigDecimal", hasBigDecimal);
		map.put("mainPath", mainPath);
		map.put("package", config.getString("package" ));
		map.put("moduleName", config.getString("moduleName" ));
		map.put("domainName", domainName);
		map.put("author", config.getString("author"));
		map.put("email", config.getString("email"));
		map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
		map.put("requestMapping", requestMapping);
        VelocityContext context = new VelocityContext(map);

		//获取模板列表
		List<String> templates = getTemplates();
		for(String template : templates){
			//渲染模板
			StringWriter sw = new StringWriter();
			Template tpl = Velocity.getTemplate(template, "UTF-8");
			tpl.merge(context, sw);

			String fileName = getFileName(source, template, tableEntity.getClassName(), config.getString
					("package"), config.getString("moduleName"), domainName);

			GenUtils.createFile(fileName, sw);

		}
	}

	private static void createFile(String fileName, StringWriter sw) {
		FileWriter fileWriter = null;
		try {
			File file = new File(fileName);
			File parentFile = file.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}

			fileWriter = new FileWriter(file);
			fileWriter.write(sw.toString());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RRException("渲染模板失败，文件名：" + fileName, e);
		} finally {

			try {
				if (fileWriter != null) {
					fileWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	/**
	 * 列名转换成Java属性名
	 */
	public static String columnToJava(String columnName) {
		return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
	}
	
	/**
	 * 表名转换成Java类名
	 */
	public static String tableToJava(String tableName, String tablePrefix) {
		if(StringUtils.isNotBlank(tablePrefix)){
			tableName = tableName.replace(tablePrefix, "");
		}
		return columnToJava(tableName);
	}

	/**
	 * 表名转换成RequestMapping
	 */
	public static String tableToRequestMapping(String tableName, String tablePrefix) {
		if(StringUtils.isNotBlank(tablePrefix)){
			tableName = tableName.replace(tablePrefix, "");
			tableName = tableName.replaceAll("_", "/");
		}
		return tableName;
	}
	
	/**
	 * 获取配置信息
	 */
	public static Configuration getConfig(){
		try {
			return new PropertiesConfiguration("generator.properties");
		} catch (ConfigurationException e) {
			throw new RRException("获取配置文件失败，", e);
		}
	}

	/**
	 * E:\law\com\shushuo\law\dao\mybatis\bo\app\AppBo.java
	 * E:\law\law-dao\src\main\java\com\shushuo\law\dao\mybatis\bo\app\AppBo.java
	 *
	 * 获取文件名
	 */
	public static String getFileName(String source, String template, String className, String packageName, String
			moduleName, String domainName ) {
		String src = "src" + File.separator + "main" + File.separator + "java" + File.separator;

		String daoPackagePath = source + File.separator + moduleName + "-dao" + File.separator + src;
		if (StringUtils.isNotBlank(packageName)) {
			daoPackagePath += packageName.replace(".", File.separator) + File.separator + moduleName + File.separator;
		}

		String xmlPackagePath = source + File.separator + moduleName + "-dao" + File.separator + "src" + File
				.separator + "main" + File.separator + "resources" + File.separator + "mybatis" +  File.separator + "mapper" + File.separator;


		String servicePackagePath = source + File.separator + moduleName + "-service" + File.separator + src;
		if (StringUtils.isNotBlank(packageName)) {
			servicePackagePath += packageName.replace(".", File.separator) + File.separator + moduleName + File.separator;
		}

		String serverPackagePath = source + File.separator + moduleName + "-server" + File.separator + src;
		if (StringUtils.isNotBlank(packageName)) {
			serverPackagePath += packageName.replace(".", File.separator) + File.separator + moduleName + File.separator;
		}

		if (template.contains("SsBo.java.vm" )) {
			return daoPackagePath + "dao" + File.separator + "mybatis" + File.separator + "bo" + File.separator + domainName + File.separator + className + "Bo.java";
		}

		if (template.contains("SsController.java.vm" )) {
			return serverPackagePath + "web" + File.separator + "controller" + File.separator + domainName + File.separator + className + "Controller" + ".java";
		}

		if (template.contains("SsDeleteBo.java.vm" )) {
			return daoPackagePath + "dao" + File.separator + "mybatis" + File.separator + "bo" + File.separator + domainName + File.separator + className + "DeleteBo.java";
		}

		if (template.contains("SsMapper.java.vm" )) {
			return daoPackagePath + "dao" + File.separator + "mybatis" + File.separator + "mapper" + File.separator + domainName + File.separator + className + "Mapper.java";
		}

		if (template.contains("SsMapper.xml.vm" )) {
			return xmlPackagePath + className + "Mapper.xml";
		}

		if (template.contains("SsQueryBo.java.vm" )) {
			return daoPackagePath + "dao" + File.separator + "mybatis" + File.separator + "bo" + File.separator + domainName + File.separator + className + "QueryBo.java";
		}

		if (template.contains("SsQueryRequest.java.vm" )) {
			return serverPackagePath + "web" + File.separator + "request" + File.separator + domainName + File.separator + className + "QueryRequest.java";
		}

		if (template.contains("SsQueryVo.java.vm" )) {
			return daoPackagePath + "dao" + File.separator + "mybatis" + File.separator + "vo" + File.separator + domainName + File.separator + className + "QueryVo.java";
		}

		if (template.contains("SsRepository.java.vm" )) {
			return daoPackagePath + "dao" + File.separator + "jpa" + File.separator + "repository"  + File.separator + domainName + File.separator + className + "Repository.java";
		}

		if (template.contains("SsRequest.java.vm" )) {
			return serverPackagePath + "web" + File.separator + "request" + File.separator + domainName + File.separator + className + "Request.java";
		}

		if (template.contains("SsService.java.vm" )) {
			return servicePackagePath + "service" + File.separator + domainName + File.separator + className + "Service.java";
		}

		if (template.contains("SsServiceImpl.java.vm" )) {
			return servicePackagePath + "service" + File.separator + domainName + File.separator + "impl" + File.separator + className + "ServiceImpl" + ".java";
		}

		return null;
	}
}
