/*package com.student.feedback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
@SpringBootApplication(
    exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
    }
)
public class StudentFeedbackBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentFeedbackBackendApplication.class, args);
    }
}*/
package com.student.feedback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudentFeedbackBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentFeedbackBackendApplication.class, args);
    }
}