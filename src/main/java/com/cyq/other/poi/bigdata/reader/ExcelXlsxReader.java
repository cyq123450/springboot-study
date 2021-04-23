package com.cyq.other.poi.bigdata.reader;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
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
@Setter
@Getter
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
        SharedStringsTable sst = xssfReader.getSharedStringsTable();
        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        this.sst = sst;
        parser.setContentHandler(this);
        XSSFReader.SheetIterator sheets = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        while (sheets.hasNext()) {
            // 标记初始行为第一行
            curRow = 1;
            sheetIndex++;
            InputStream sheet = sheets.next();            // sheets.next()和sheets.getSheetName()不能换位置，否则sheetName报错
            sheetName = sheets.getSheetName();
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);                          // 解析excel的每条记录，在这个过程中startElement()、characters()、endElement()这三个函数会依次执行
            sheet.close();
        }
        return totalRows;   // 返回该excel文件的总行数，不包括首列和空行
    }

    /**
     * 第一个执行
     * @param uri
     * @param localName
     * @param name
     * @param attributes
     * @throws SAXException
     */
    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        // c => 单元格
        if ("c".equals(name)) {
            // 前一个单元格的位置
            if (preRef == null) {
                preRef = attributes.getValue("r");
            }
        } else {
            // 中部文本空单元格标识'endElementFlag'判断前一次是否为文本空字符串，true则表明不是文本空字符串，false表明是文本空字符串跳过把空字符串的位置赋予preRef
            if (endElementFlag) {
                prePreRef = ref;
            }
        }
        // 当前单元格的位置
        ref = attributes.getValue("r");
        // 首部文本空单元格标识 ‘startElementFlag’ 判断前一次，即首部是否为文本空字符串，true则表明不是文本空字符串，false表明是文本空字符串, 且已知当前格，即第二格带“B”标志，则ref赋予preRef
        if (!startElementFlag && !flag) {   // 上一个单元格为文本空单元格，执行下面的，使ref=preRef；flag为true表明该单元格之前有数据值，即该单元格不是首部空单元格，则跳过
            // 这里只有上一个单元格为文本空单元格，且之前的几个单元格都没有值才会执行
            preRef = ref;
        }
        // this.setNextDataType(attributes);
        endElementFlag = false;
        charactersFlag = false;
        startElementFlag = false;
        //当元素为t时
        if ("t".equals(name)) {
            isTElement = true;
        } else {
            isTElement = false;
        }
        //置空
        lastIndex = "";
    }

    /**
     * 第二个执行
     * 得到单元格对应的索引值或是内容值
     * 如果单元格类型是字符串、INLINESTR、数字、日期，lastIndex则是索引值
     * 如果单元格类型是布尔值、错误、公式，lastIndex则是内容值
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        startElementFlag = true;
        charactersFlag = true;
        lastIndex += new String(ch, start, length);
    }

    /**
     * 第三个执行
     * @param uri
     * @param localName
     * @param name
     * @throws SAXException
     */
    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        // t元素也包含字符串
        if (isTElement) {   // 这个程序没经过
            // 将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符
            String value = lastIndex.trim();
            cellList.add(curCol, value);
            endElementFlag = true;
            curCol++;
            isTElement = false;
            // 如果里面某个单元格含有值，则标识该行不为空行
            if (value != null && !"".equals(value)) {
                flag = true;
            }
        } else if ("v".equals(name)) {
            // v => 单元格的值，如果单元格是字符串，则v标签的值为该字符串在SST中的索引
            //String value = this.getDataValue(lastIndex.trim(), ""); // 根据索引值获取对应的单元格值

            //补全单元格之间的空单元格
            if (!ref.equals(preRef)) {
                //int len = countNullCell(ref, preRef);
                /*for (int i = 0; i < len; i++) {
                    cellList.add(curCol, "");
                    curCol++;
                }*/
            } /*else if (ref.equals(preRef) && !ref.startWith("A")){ //ref等于preRef，且以B或者C...开头，表明首部为空格271                 int len = countNullCell(ref, "A");
                for (int i = 0; i <= len; i++) {
                    cellList.add(curCol, "");
                    curCol++;
                }*/
            }

    }
}
