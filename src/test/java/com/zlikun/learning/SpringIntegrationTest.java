package com.zlikun.learning;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 与Spring集成以实现单元测试
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017/9/12 10:26
 */
public class SpringIntegrationTest {

    private Connection connection ;

    @Before
    public void init() throws ClassNotFoundException, SQLException {
        // 配置内嵌H2数据库连接
        EmbeddedDatabase database = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:scripts/schema.sql")
                .addScript("classpath:scripts/data.sql")
                .build() ;
        connection = database.getConnection() ;
    }

    @Test
    public void test() throws SQLException {

        // 读取数据
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM TBL_USER") ;
        while (resultSet.next()) {
            // 1	zlikun	2017-09-12 10:54:50.472
            // 2	kevin	2017-09-12 10:55:32.207
            // 3	jackson	2017-09-12 10:55:32.207
            System.out.println(resultSet.getLong(1) + "\t" + resultSet.getString(2) + "\t" + resultSet.getTimestamp(3));
        }
        resultSet.close();

    }

    @After
    public void destroy() throws SQLException {
        if (connection != null)
            connection.close();
    }

}
