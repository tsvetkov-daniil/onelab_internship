package tsvetkov.daniil.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

//@Configuration
public class JdbcH2Config {

    @Bean
    public DataSource dataSource() {
        try {
            return new EmbeddedDatabaseBuilder()
                    .setType(EmbeddedDatabaseType.H2)
                    .addScripts("classpath:db/schema.sql", "classpath:db/data.sql")
                    .build();
        } catch (Exception e) {
            System.err.println("DataSource bean cannot be created!" + e.getMessage());
            return null;
        }
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
