package spring;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class Config {

    private static MariaDbDataSource ds;

    @Bean
    public Flyway flyway() {
        Flyway flyway = Flyway
                .configure()
                .locations("filesystem:src/test/resources/employees/db")
                .dataSource(dataSource())
                .load();
        return flyway;
    }

    @Bean
    public MariaDbDataSource dataSource()  {
        ds = new MariaDbDataSource();

        try {
            ds.setUrl("jdbc:mariadb://localhost:3308/employees?useUnicode=true");
            ds.setUser("employees");
            ds.setPassword("employees");
        } catch (SQLException se) {
            throw new IllegalStateException("Cannot connect to db" , se);
        }
        return ds;
    }

    @Bean
    public EmployeeDao employeeDao() {
        return new EmployeeDao(dataSource());
    }
}
