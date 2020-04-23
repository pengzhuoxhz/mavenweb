package com.ecloudtech;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class JobServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public JobServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String SourcePath = request.getParameter("sourceFile");
        String orderPath = request.getParameter("orderFile");
        StringBuilder sb = new StringBuilder();
        File file = new File("C:/Java/pz.txt");
        if(!file.exists()){
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        Map<String, String> map = new HashMap<String, String>();
        // 数据源数据
        String s1 = getInfo(SourcePath);
        String[] str = s1.split("\n");
        for (int i = 0; i < str.length; i++) {
            if (i % 2 == 0) {
                map.put(str[i], str[i + 1]);
            }
        }

        // 需要查询的列表
        String s2 = getInfo(orderPath);
        String[] str2 = s2.split("\r\n");
//        for (int i = 0; i < str2.length; i++) {
//            System.out.println(str2[i] + "\r\n");
//
//        }
        for (String s : str2) {
            for (Map.Entry<String, String> m : map.entrySet()) {
                if (m.getKey().startsWith(">" + s + " ")) {
                    sb.append(m.getKey()+"\r\n");
                    sb.append(m.getValue()+"\r\n");
//                    System.out.println(m.getKey());
//                    System.out.println(m.getValue());
                }
            }

        }
        request.setAttribute("sb",sb.toString());
        request.getRequestDispatcher("data.jsp").forward(request, response);
    }

    //获取文件数据
    public  String getInfo(String filePath) {
        StringBuilder sb = new StringBuilder();
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filePath);
            int data2 = -1;
            while((data2 = fileReader.read())!= -1) {
                sb.append((char)data2);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

}