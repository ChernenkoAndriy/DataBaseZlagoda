package database_manegment.database_entities;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionPool {
    private static HikariDataSource dataSource;

    public static void initialize() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/Zlagoda");
        config.setUsername("postgres");
        config.setPassword("Sand5Man9");
        config.setMaximumPoolSize(10); // максимальна кількість з'єднань у пулі
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection(); // Отримання з'єднання з пулу
    }

    public static void close() {
        dataSource.close(); // Закриття пулу, коли програма завершить роботу
    }
}
