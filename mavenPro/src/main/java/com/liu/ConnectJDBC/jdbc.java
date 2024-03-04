package com.liu.ConnectJDBC;
import java.sql.*;
import com.mysql.cj.jdbc.Driver;
import com.mysql.cj.jdbc.Driver;
public class jdbc {
    public static void main(String[] args) throws SQLException{
        String driver = "com.mysql.cj.jdbc.Driver";
        String URL = "jdbc:mysql://localhost:3306/student?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useSSL=false";
//        String URL = "jdbc:mysql://localhost:3306/mytestsql"
        Connection con = null;
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch(java.lang.ClassNotFoundException e)
        {
            System.out.println("无法加载驱动.");
        }
        try
        {
            con=DriverManager.getConnection(URL,"root","password");//这里输入你自己安装MySQL时候设置的用户名和密码，用户名默认为root
            try {
//                创建statement对象
                Statement statement = con.createStatement();
//              创建sql插入语句
                String sql = "INSERT INTO student (name, gender, grade, score) VALUES ('小明', 1, 1, 88)";
//               执行sql插入语句
                int rowsAffected = statement.executeUpdate(sql);
                if (rowsAffected >0){
                    System.out.println("successful !" +rowsAffected);
                }else {
                    System.out.println("failed");
                }
                statement.close();
            }catch (SQLException e){
                e.printStackTrace();
            }



            System.out.println("连接成功.");
        }
        catch(Exception e)
        {
            System.out.println("连接失败:" + e.getMessage());
        }
    }
}
