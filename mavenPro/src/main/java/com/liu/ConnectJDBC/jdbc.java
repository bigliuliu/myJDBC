package com.liu.ConnectJDBC;

import java.sql.*;

import com.mysql.cj.jdbc.Driver;
import com.mysql.cj.jdbc.Driver;

public class jdbc {
	public static void main(String[] args) throws SQLException {
		String driver = "com.mysql.cj.jdbc.Driver";
		String URL = "jdbc:mysql://localhost:3306/student?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useSSL=false";
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (java.lang.ClassNotFoundException e) {
			System.out.println("无法加载驱动.");
		}
//        try
//        {
//            con=DriverManager.getConnection(URL,"root","password");//这里输入你自己安装MySQL时候设置的用户名和密码，用户名默认为root
//            try {
////                创建statement对象
//                Statement statement = con.createStatement();
////              创建sql插入语句
//                String sql = "INSERT INTO student (name, gender, grade, score) VALUES ('小明', 1, 1, 88)";
////               执行sql插入语句
//                int rowsAffected = statement.executeUpdate(sql);
//                if (rowsAffected >0){
//                    System.out.println("successful !" +rowsAffected);
//                }else {
//                    System.out.println("failed");
//                }
//                statement.close();
//            }catch (SQLException e){
//                e.printStackTrace();
//            }
//
//
//
//            System.out.println("连接成功.");
//        }
//        catch(Exception e)
//        {
//            System.out.println("连接失败:" + e.getMessage());
//        }

		try (Connection conn = DriverManager.getConnection(URL, "root", "password")) {
			//      SQL  查询
//            try(PreparedStatement ps = conn.prepareStatement("SELECT id,score FROM student WHERE gender=?")){
////                索引从1开始 ：parameterIndex,代表索引；x：代表sql查询语句中的gender的值
//                ps.setObject(1,2);
//                try(ResultSet rs = ps.executeQuery()) {
//                    while (rs.next()){
//                        long id = rs.getLong("id");
//                        long score = rs.getLong("score");
//                        System.out.println("id:"+id+","+"score:"+score);
//                    }
//                }
//            }
//            SQL 插入
//            try(PreparedStatement ps = conn.prepareStatement("INSERT INTO student (name,gender,grade,score) VALUE (?,?,?,?)",Statement.RETURN_GENERATED_KEYS)){
////                ps.setObject(1,13);// 索引从1开始，id
//                ps.setObject(1,"自增测试2");//name
//                ps.setObject(2,1);//gender
//                ps.setObject(3,2);//grade
//                ps.setObject(4,89); //score
//                int n = ps.executeUpdate();
//                System.out.println("更新了"+n+"条数据");
////                自增主键需要添加标志位
//                try(ResultSet rs = ps.getGeneratedKeys()){
//                    if(rs.next()){
//                        long id = rs.getLong(1); //索引从1开始
//                    }
//                }
//
//            }
//            SQL 更新
//            try(PreparedStatement ps = conn.prepareStatement("UPDATE student SET name=? WHERE id=?")){
//                ps.setObject(1,"改变名字"); //索引从1开始
//                ps.setObject(2,1); //id为1
//                int n= ps.executeUpdate();
//                System.out.println("修改了"+n+"条数据");
//            }
//            SQL删除
//            try(PreparedStatement ps = conn.prepareStatement("DELETE FROM student WHERE id=?")){
//                ps.setObject(1,21);
//                int n= ps.executeUpdate();
//                System.out.println("删除了"+n+"条数据");
//            }
//            SQL 事务练习
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			try {
//                关闭自动提交
				conn.setAutoCommit(false);
//               执行多条SQL语句
//                插入一条数据
				try (PreparedStatement ps = conn.prepareStatement("INSERT INTO student (name,gender,grade,score) VALUE (?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
					ps.setObject(1, "事务的数据");//name
					ps.setObject(2, 1);//gender
					ps.setObject(3, 2);//grade
					ps.setObject(4, 89); //score
					int n = ps.executeUpdate();
					System.out.println("更新了" + n + "条数据");
//                自增主键需要添加标志位
					try (ResultSet rs = ps.getGeneratedKeys()) {
						if (rs.next()) {
							long id = rs.getLong(1); //索引从1开始
						}
					}

				}
//                修改数据
				try (PreparedStatement ps = conn.prepareStatement("UPDATE student SET score=? WHERE name=?")) {
					ps.setObject(1, 0); //索引从1开始
					ps.setObject(2, "事务的数据"); //id为1
					int n = ps.executeUpdate();
					System.out.println("修改了" + n + "条数据");
				}
//				提交事务
				conn.commit();
			} catch (SQLException e) {
//				回滚事务
				conn.rollback();
				e.printStackTrace();
			}finally {
				conn.setAutoCommit(true);
				conn.close();
			}
		}
	}
}
