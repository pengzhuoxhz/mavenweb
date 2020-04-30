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
        String sourceFilePath = "";//源文件上传文件路径
        String orderFilePath = "";//排序文件上传文件路径
        //判断请求是否为multipart请求
        if(!ServletFileUpload.isMultipartContent(request)){
            throw new RuntimeException("当前请求不支持文件上传");
        }
        try {
            //创建一个FileItem工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //创建文件上传核心组件
            ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
            //解析请求，获取到所有的item
            List<FileItem> items = servletFileUpload.parseRequest(request);
            //遍历items
            for(FileItem item : items){
                if(item.isFormField()){  //若item为普通表单项
                    String fieldName = item.getFieldName();  //获取表单项名称
                    String fieldValue = item.getString();  //获取表单项的值
                    System.out.println(fieldName + " = " + fieldValue);
                }else{  //若item为文件表单项
                    //获取上传文件原始名称
                    String fileName = item.getName();
                    //获取输入流，其中有上传 文件的内容
                    InputStream is = item.getInputStream();
                    //获取文件保存在服务器的路径
                    String path = this.getServletContext().getRealPath("/WEB-INF/upload");
                    if("sourceFile".equals(item.getFieldName())){
                        sourceFilePath = path + "\\" + fileName;
                        System.out.println("源文件路径"+sourceFilePath);
                    } else if("orderFile".equals(item.getFieldName())){
                        orderFilePath = path + "\\" + fileName;
                        System.out.println("排序文件路径"+orderFilePath);
                    }
                    File file = new File(path);
                    //判断上传文件的保存目录是否存在
                    if (!file.exists() && !file.isDirectory()) {
                        System.out.println(path+"目录不存在，需要创建");
                        //创建目录
                        file.mkdirs();
                    }
                    //将输入流中的数据写入到输出流中
                    OutputStream os = new FileOutputStream(path + "\\" + fileName);
                    //将输入流中的数据写入输出流中
                    int len = -1;
                    byte[] buf = new byte[1024];
                    while((len = is.read(buf)) != -1){
                        os.write(buf, 0, len);
                    }

                    os.close();
                    is.close();
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        Map<String, String> map = new HashMap<String, String>();
        // 数据源数据
        String s1 = getInfo(sourceFilePath);
        String[] str = s1.split("\n");
        for (int i = 0; i < str.length; i++) {
            if (i % 2 == 0) {
                map.put(str[i], str[i + 1]);
            }
        }

        // 需要查询的列表
        String s2 = getInfo(orderFilePath);
        String[] str2 = s2.split("\r\n");
        for (String s : str2) {
            for (Map.Entry<String, String> m : map.entrySet()) {
                if (m.getKey().startsWith(">" + s + " ")) {
                    sb.append(m.getKey()+"\r\n");
                    sb.append(m.getValue()+"\r\n");
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