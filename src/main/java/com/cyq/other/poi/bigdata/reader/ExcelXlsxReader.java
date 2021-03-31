package com.cyq.other.poi.bigdata.reader;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cyq
 * @description
 * POI读取excel有两种模式，一种是用户模式，一种是事件驱动模式
 * 采用SAX事件驱动模式解决XLSX文件，可以有效解决用户模式内存溢出的问题，
 * 该模式是POI官方推荐的读取大数据的模式，
 * 在用户模式下，数据量较大，Sheet较多，或者是有很多无用的空行的情况下，容易出现内存溢出
 * 用于解决.xlsx2007版本大数据量问题
 * @date 2021/3/31 22:42
 */
public class ExcelXlsxReader extends DefaultHandler {

    // 单元格中可能的数据类型
    enum CellDataType {
        BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER, DATE, NULL
    }

    // 共享字符串表
    private SharedStringsTable sst;

    // 上一次的索引值
    private String lastIndex;

    // 文件的绝对路径
    private String filePath;

    // 工作表索引
    private int sheetIndex;

    // sheet名
    private String sheetName;

    // 总行数
    private int totalRows = 0;

    // 一行内的cell集合
    private List<String> cellList = new ArrayList<String>();

    // 判断整行是否为空行标记
    private boolean flag = false;

    // 当前行
    private int curRow = 1;

    // 当前列
    private int curCol = 0;

    // T元素标识
    private boolean isTElement;

    // 判断上一单元格是否为文本空单元格
    private boolean startElementFlag = true;
    private boolean endElementFlag = false;
    private boolean charactersFlag = false;

    // 异常信息，如果为空则表示没有异常
    private String exceptionMessage;

    // 单元格数据类型，默认为字符串类型
    private CellDataType nextDataType = CellDataType.SSTINDEX;

    private final DataFormatter formatter = new DataFormatter();

    // 单元格日期格式的索引
    private short formatIndex;

    // 日期格式字符串
    private String formatString;

    // 定义前一个元素和当前元素的位置，用来计算其中空的单元格数量，如A6和A8等
    private String prePreRef = "A", preRef = null, ref = null;

    // 定义该文档一行最大的单元格数，用来补全一行最后可能缺失的单元格
    private String maxRef = null;

    // 单元格
    private StylesTable stylesTable;

    /**
     * 遍历工作簿中所有的电子表格，并缓存在mySheetList中
     * @param filename
     * @return
     * @throws Exception
     */
    public int process(String filename) throws Exception {
        filePath = filename;
        OPCPackage pkg = OPCPackage.open(filePath);
        XSSFReader xssfReader = new XSSFReader(pkg);
        stylesTable = xssfReader.getStylesTable();

        return 1;
    }

}
