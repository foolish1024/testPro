package cn.foolish.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * 基于POI的javaee导出Excel工具类
 */
public class ExportExcelUtil {

	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void main(String[] args) {
		System.out.println(sdf.format(new Date()));
	}

	/**
	 * 
	 * @param response
	 *            请求
	 * @param fileName
	 *            文件名 如："学生表"
	 * @param excelHeader
	 *            excel表头数组，存放"姓名#name"格式字符串，"姓名"为excel标题行， "name"为对象字段名
	 * @param dataList
	 *            数据集合，需与表头数组中的字段名一致，并且符合javabean规范
	 * @return 返回一个HSSFWorkbook
	 * @throws Exception
	 */
	public static <T> void export(HttpServletResponse response, String fileName, Map<String, String> excelHeader,
			Collection<T> dataList) {
		try {

			// 创建一个Workbook，对应一个Excel文件
			SXSSFWorkbook wb = new SXSSFWorkbook();
			// 设置标题样式
			CellStyle titleStyle = setTitleStyle(wb);
			// 标题数组
			Set<String> keys = excelHeader.keySet();
			String[] titleArray = new String[keys.size()];
			// 字段名数组
			String[] fieldArray = new String[keys.size()];
			setTitleArrayAndFieldArray(excelHeader, keys, titleArray, fieldArray);
			// 在Workbook中添加一个sheet,对应Excel文件中的sheet
			Sheet sheet = wb.createSheet(fileName);
			// 在sheet中添加标题行
			setValueForTitleWithSerialNum(titleStyle, titleArray, sheet);
			// 数据样式 因为标题和数据样式不同 需要分开设置 不然会覆盖
			CellStyle dataStyle = setDataStyle(wb);
			// 遍历集合数据，产生数据行
			setData(dataList, fieldArray, sheet, dataStyle);
			// 设置请求
			OutputStream outputStream = setResponse(response, fileName);
			wb.write(outputStream);// HSSFWorkbook写入流
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	/**
	* @Name: exportNoSerialNum
	* @Description:
	* @param   
	* @return  
	*/
	public static <T> void exportNoSerialNum(HttpServletResponse response, String fileName, Map<String, String> excelHeader,
			Collection<T> dataList) {
		try {

			// 创建一个Workbook，对应一个Excel文件
			SXSSFWorkbook wb = new SXSSFWorkbook();
			// 设置标题样式
			CellStyle titleStyle = setTitleStyle(wb);
			// 标题数组
			Set<String> keys = excelHeader.keySet();
			String[] titleArray = new String[keys.size()];
			// 字段名数组
			String[] fieldArray = new String[keys.size()];
			setTitleArrayAndFieldArray(excelHeader, keys, titleArray, fieldArray);
			// 在Workbook中添加一个sheet,对应Excel文件中的sheet
			Sheet sheet = wb.createSheet(fileName);
			// 在sheet中添加标题行
			setValueForTitleNoSerialNum(titleStyle, titleArray, sheet);
			// 数据样式 因为标题和数据样式不同 需要分开设置 不然会覆盖
			CellStyle dataStyle = setDataStyle(wb);
			// 遍历集合数据，产生数据行
			setData(dataList, fieldArray, sheet, dataStyle);
			// 设置请求
			OutputStream outputStream = setResponse(response, fileName);
			wb.write(outputStream);// HSSFWorkbook写入流
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	* @Name: setTitleArrayAndFieldArray
	* @Description: 
	* @param   
	* @return  
	*/
	private static void setTitleArrayAndFieldArray(Map<String, String> excelHeader, Set<String> keys,
			String[] titleArray, String[] fieldArray) {
		int num = 0;
		for (String key : keys) {
			titleArray[num] = excelHeader.get(key);
			fieldArray[num] = key;
			num++;
		}
	}

	/**
	* @Name: setResponse
	* @Description:
	* @param   
	* @return  
	*/
	private static OutputStream setResponse(HttpServletResponse response, String fileName)
			throws UnsupportedEncodingException, IOException {
		response.setContentType("/application/vnd.ms-excel;charset=UTF-8");
		response.setHeader("Content-disposition",
				"attachment;filename=" + URLEncoder.encode(fileName + ".xlsx", "UTF-8"));
		OutputStream outputStream = response.getOutputStream();// 打开流
		return outputStream;
	}

	/**
	* @Name: setValueForTitle
	* @Description:
	* @param   
	* @return  
	*/
	private static void setValueForTitleWithSerialNum(CellStyle titleStyle, String[] titleArray, Sheet sheet) {
		Row row = sheet.createRow((int) 0);// 行数从0开始
		Cell sequenceCell = row.createCell(0);// cell列 从0开始 第一列添加序号
		sequenceCell.setCellValue("序号");
		sequenceCell.setCellStyle(titleStyle);
		sheet.autoSizeColumn(0);// 自动设置宽度
		// 为标题行赋值
		for (int i = 0; i < titleArray.length; i++) {
			Cell titleCell = row.createCell(i + 1);// 0号位被序号占用，所以需+1
			titleCell.setCellValue(titleArray[i]);
			titleCell.setCellStyle(titleStyle);
			sheet.autoSizeColumn(i + 1);// 0号位被序号占用，所以需+1
		}
	}
	
	private static void setValueForTitleNoSerialNum(CellStyle titleStyle, String[] titleArray, Sheet sheet) {
		Row row = sheet.createRow((int) 0);// 行数从0开始
		// 为标题行赋值
		for (int i = 0; i < titleArray.length; i++) {
			Cell titleCell = row.createCell(i);
			titleCell.setCellValue(titleArray[i]);
			titleCell.setCellStyle(titleStyle);
			sheet.autoSizeColumn(i);
		}
	}

	/**
	 * @Name: setData @Description: TODO @param 参数说明 @return 返回类型 @throws
	 */
	private static <T> void setData(Collection<T> dataList, String[] fieldArray, Sheet sheet, CellStyle dataStyle)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Row row;
		Iterator<T> it = dataList.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;// 0号位被占用 所以+1
			row = sheet.createRow(index);
			// 为序号赋值
			Cell sequenceCellValue = row.createCell(0);// 序号值永远是第0列
			sequenceCellValue.setCellValue(index);
			sequenceCellValue.setCellStyle(dataStyle);
			sheet.autoSizeColumn(0);
			T t = it.next();
			// 利用反射，根据传过来的字段名数组，动态调用对应的getXxx()方法得到属性值
			for (int i = 0; i < fieldArray.length; i++) {
				Cell dataCell = row.createCell(i + 1);
				dataCell.setCellStyle(dataStyle);
				sheet.autoSizeColumn(i + 1);
				String fieldName = fieldArray[i];
				String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);// 取得对应getXxx()方法
				Class<? extends Object> tCls = t.getClass();// 泛型为Object以及所有Object的子类
				Method getMethod = tCls.getMethod(getMethodName, new Class[] {});// 通过方法名得到对应的方法
				Object value = getMethod.invoke(t, new Object[] {});// 动态调用方,得到属性值
				if (value != null) {
					if (value.getClass().getName().equals(Date.class.getName())) {
						dataCell.setCellValue(sdf.format(value));// 为当前列赋值
					} else {
						dataCell.setCellValue(String.valueOf(value));// 为当前列赋值
					}
				}
			}
		}
	}

	/**
	 * @Name: setDataStyle @Description: TODO @param 参数说明 @return 返回类型 @throws
	 */
	private static CellStyle setDataStyle(SXSSFWorkbook wb) {
		CellStyle dataStyle = wb.createCellStyle();
		// 设置数据边框
		dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		DataFormat format = wb.createDataFormat();
		dataStyle.setDataFormat(format.getFormat("@"));
		// 设置居中样式
		dataStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
		dataStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中
		// 设置数据字体
		Font dataFont = wb.createFont();
		dataFont.setFontHeightInPoints((short) 12); // 字体高度
		dataFont.setFontName("宋体"); // 字体
		dataStyle.setFont(dataFont);
		return dataStyle;
	}

	/**
	 * @Name: setTitleStyle @Description: TODO @param 参数说明 @return 返回类型 @throws
	 */
	private static CellStyle setTitleStyle(SXSSFWorkbook wb) {
		CellStyle titleStyle = wb.createCellStyle();
		// 设置单元格边框样式
		titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框 细边线
		titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 下边框 细边线
		titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框 细边线
		titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框 细边线
		// 设置单元格对齐方式
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中
		// 设置字体样式
		Font titleFont = wb.createFont();
		titleFont.setFontHeightInPoints((short) 15); // 字体高度
		titleFont.setFontName("黑体"); // 字体样式
		titleStyle.setFont(titleFont);
		return titleStyle;
	}
}
