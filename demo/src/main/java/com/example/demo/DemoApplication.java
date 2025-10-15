package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@SpringBootApplication
public class DemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }

  @Bean
  public org.springframework.boot.CommandLineRunner pingOracle(DataSource dataSource) {
    return args -> {
      try (Connection conn = dataSource.getConnection();
           Statement st = conn.createStatement();
           ResultSet rs = st.executeQuery("SELECT TO_CHAR(SYSDATE,'YYYY-MM-DD HH24:MI:SS') FROM dual")) {

        if (rs.next()) {
          System.out.println("✅ Conectado a Oracle (SYSTEM@localhost:1521:xe). SYSDATE = " + rs.getString(1));
        }

      } catch (Exception e) {
        System.err.println("❌ Error al conectar con Oracle: " + e.getMessage());
        e.printStackTrace();
      }
    };
  }
}
