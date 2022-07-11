package com.github.ko4evneg.caloriesApp;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SpringMain {
    public static void main(String[] args) throws SQLException {
        try (GenericXmlApplicationContext appCtx = new GenericXmlApplicationContext()) {
            ConfigurableEnvironment env = appCtx.getEnvironment();
            env.setActiveProfiles("jpa", "hsql");
            appCtx.load("spring/spring-app.xml");
            appCtx.refresh();

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
