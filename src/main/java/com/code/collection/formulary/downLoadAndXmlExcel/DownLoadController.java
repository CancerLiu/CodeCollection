package com.code.collection.formulary.downLoadAndXmlExcel;


import com.code.collection.formulary.downLoadAndXmlExcel.excel.config.Header;
import com.code.collection.formulary.downLoadAndXmlExcel.excel.config.Style;
import com.code.collection.formulary.downLoadAndXmlExcel.excel.writer.XmlExcel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("download/fansnumber")
public class DownLoadController {

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public void downExcel(HttpServletResponse response) throws IOException {

        /*
          这里可以调用底层提供的方法得到需要下载成xml形式excel的数据，需要是list。
          这里直接写死
         */

        List<User> userList = new ArrayList<User>() {{
            add(new User(1L, "刘超", 28, 1.78));
            add(new User(2L, "刘然", 29, 1.68));
            add(new User(3L, "刘森", 30, 1.58));
            add(new User(4L, "刘博", 31, 1.88));
        }};


        //得到相应的数据格式
        //表头的样式
        List<Header> headerList = Header.of(
                Header.of("id", "用户ID"),
                Header.of("name", "用户姓名"),
                Header.of("age", "用户年龄").setCellStyle(s -> ((Integer) s) < 18 ? "C01" : null),
                Header.of("height", "用户身高")
        );
        //整体表的样式
        List<Style> styles = Arrays.asList(Style.of("C01", "#FF0000", null));

        //调用方法得到相应的考试名字作为Excel文件名组成部分

        /*
          这里可以从底层的业务中得到具体的文件名，这里demo直接写死
         */
        String fileName = "用户统计表";

        //得到响应流并发送(这里是下载部分)
        response.reset();
        try (OutputStream out = response.getOutputStream()) {
            //这里设定具体http协议中需要的属性字段
            response.setHeader("Content-Disposition", "attachment; filename=\"gradedata-" + fileName + ".xml\"");
            response.setContentType("application/octet-stream;charset=UTF-8");

            try (XmlExcel excel = XmlExcel.of(out, headerList, styles)
                    .start()
                    .appendBody(userList)
                    .finish()) {
            }
            out.flush();

        } catch (Exception e) {
            throw new RuntimeException("生成响应流的时候出错");
        }
    }
}
