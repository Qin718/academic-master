package com.example.common.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.common.expect.expectToExcel;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.apache.poi.ss.usermodel.CellType.STRING;

public class ExcelUtils {
    /**
     * @param path     excel路径
     * @param aimClass 实体类字节码对象
     * @param <T>      实体类
     * @return 封装excel的内容集合
     */
    @SneakyThrows
    public static <T> List<T> parseFromExcel(String path, Class<T> aimClass) {
        List<T> result = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(path);
            Workbook workbook = WorkbookFactory.create(fis);
            //对excel文档的第一页,即sheet1进行操作
            Sheet sheet = workbook.getSheetAt(0);
            int lastRaw = sheet.getLastRowNum();
            for (int i = 1; i <= lastRaw; i++) {
                int passNum = 0;
                //第i行
                Row row = sheet.getRow(i);
                T object = aimClass.newInstance();
                Field[] fields = aimClass.getDeclaredFields();
                for (int j = 0; j < fields.length; j++) {
                    Field field = fields[j];
                    // 字段是否在模板中
                    boolean isTemplate = false;
                    // 字段是否在数据库中
                    boolean isTableField = true;
                    expectToExcel expectToExcel = field.getDeclaredAnnotation(expectToExcel.class);
                    if(StringUtils.isNotEmpty(expectToExcel)) {
                        isTemplate = expectToExcel.template();
                    }
                    TableField tableField = field.getDeclaredAnnotation(TableField.class);
                    if(StringUtils.isNotEmpty(tableField)) {
                        isTableField = tableField.exist();
                    }
                    if(!isTemplate || !isTableField){
                        // 只有在模板中并且字段在数据库中存在，才能继续下面的步骤
                        passNum++;
                        continue;
                    }
                    field.setAccessible(true);
                    Class<?> type = field.getType();
                    //第j列
                    Cell cell = row.getCell(j-passNum);
                    if (cell == null) {
                        continue;
                    }
                    //很重要的一行代码,如果不加,像12345这样的数字是不会给你转成String的,只会给你转成double,而且会导致cell.getStringCellValue()报错
                    cell.setCellType(STRING);
                    String cellContent = cell.getStringCellValue();
                    String empty = "";
                    if (empty.equals(cellContent.replace(" ", ""))) {
                        continue;
                    }
                    if (type.equals(String.class)) {
                        field.set(object, cellContent);
                    } else if (type.equals(char.class) || type.equals(Character.class)) {
                        field.set(object, cellContent.charAt(0));
                    } else if (type.equals(int.class) || type.equals(Integer.class)) {
                        field.set(object, Integer.parseInt(cellContent));
                    } else if (type.equals(long.class) || type.equals(Long.class)) {
                        field.set(object, Long.parseLong(cellContent));
                    } else if (type.equals(float.class) || type.equals(Float.class)) {
                        field.set(object, Float.parseFloat(cellContent));
                    } else if (type.equals(double.class) || type.equals(Double.class)) {
                        field.set(object, Double.parseDouble(cellContent));
                    } else if (type.equals(short.class) || type.equals(Short.class)) {
                        field.set(object, Short.parseShort(cellContent));
                    } else if (type.equals(byte.class) || type.equals(Byte.class)) {
                        field.set(object, Byte.parseByte(cellContent));
                    } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
                        field.set(object, Boolean.parseBoolean(cellContent));
                    } else if (type.equals(LocalDateTime.class)) {
                        field.set(object, LocalDateTime.parse(cellContent));
//                        GregorianCalendar calendar = new GregorianCalendar(1900, Calendar.JANUARY, -1);
//                        try {
//                            Date date = DateUtils.addDays(calendar.getTime(), Integer.parseInt(cellContent));
//                            LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.ofOffset("GMT", ZoneOffset.ofHours(8)));
//                            field.set(object, localDateTime);
//                        } catch (Exception e) {
//                            throw new Exception(e);
//                        }
                    }
                }
                 result.add(object);
            }
            fis.close();
            return result;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

}
