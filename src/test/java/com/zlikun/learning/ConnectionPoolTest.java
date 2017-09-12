package com.zlikun.learning;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.time.Instant;

/**
 * 连接池测试
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017/9/12 10:14
 */
public class ConnectionPoolTest {

    private JdbcConnectionPool pool ;
    private Connection connection ;

    @Before
    public void init() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver") ;
        pool = JdbcConnectionPool.create("jdbc:h2:mem:default", "sa", "");
        connection = pool.getConnection() ;
    }

    @Test
    public void test() throws SQLException {

        // 创建表
        connection.createStatement().execute("CREATE TABLE TBL_USER (ID INTEGER PRIMARY KEY AUTO_INCREMENT ,NAME VARCHAR(64) NOT NULL COMMENT '用户姓名' ,CTIME DATETIME NOT NULL COMMENT '创建时间')") ;

        // 查询表
        ResultSet resultSet = connection.createStatement().executeQuery("SHOW TABLES ;") ;
        while (resultSet.next()) {
            // TBL_USER
            System.out.println(resultSet.getString(1));
        }
        resultSet.close();

        // 插入数据
        PreparedStatement statement = connection.prepareStatement("INSERT INTO TBL_USER (NAME ,CTIME) VALUES (? ,?)" , Statement.RETURN_GENERATED_KEYS) ;
        statement.setString(1 ,"zlikun");
        statement.setTimestamp(2 ,Timestamp.from(Instant.now()));
        statement.executeUpdate() ;
        statement.close();

        // 读取数据
        resultSet = connection.createStatement().executeQuery("SELECT * FROM TBL_USER") ;
        while (resultSet.next()) {
            // 1	zlikun	2017-09-12 10:11:05.069
            System.out.println(resultSet.getLong(1) + "\t" + resultSet.getString(2) + "\t" + resultSet.getTimestamp(3));
        }
        resultSet.close();

    }

    @After
    public void destroy() throws SQLException {
        if (connection != null)
            connection.close();
        if (pool != null)
            pool.dispose();
    }

}
