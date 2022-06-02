package com.github.ko4evneg.caloriesApp;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.sql.*;

public class SpringMain {
    public static void main(String[] args) throws SQLException {
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            DataSource ds = appCtx.getBean("dataSource", DataSource.class);
            Connection connection = ds.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM USERS");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println(resultSet.getString("email"));
            }
        }
    }

}
