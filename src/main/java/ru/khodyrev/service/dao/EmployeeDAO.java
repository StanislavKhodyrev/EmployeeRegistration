package ru.khodyrev.service.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.khodyrev.service.model.Employee;

import java.util.List;

/**
 * @author Stanislav Khodyrev
 */

@Component
public class EmployeeDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EmployeeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Employee> index() {
        return jdbcTemplate.query("SELECT * FROM Employee", new BeanPropertyRowMapper<>(Employee.class));
    }

    public Employee show(int id) {
        return jdbcTemplate.query("SELECT * FROM Employee WHERE id=?",
                new Object[]{id}, new BeanPropertyRowMapper<>(Employee.class))
                .stream().findAny().orElse(null);
    }

    public void save(Employee employee) {
        jdbcTemplate.update("INSERT INTO Employee(name, surname, position, age," +
                        " work_experience, email, tel_number) VALUES (?, ? , ?, ?, ?, ?, ?)",
                employee.getName(), employee.getSurname(), employee.getPosition(),
                employee.getAge(), employee.getWorkExperience(), employee.getEmail(),
                validateTelNumber(employee.getTelNumber()));
    }

    public void update(int id, Employee employee) {
        jdbcTemplate.update("UPDATE Employee SET name=?, surname=?, position=?, " +
                        "age=?, work_experience=?, email=?, tel_number=? WHERE id=?",
                employee.getName(), employee.getSurname(), employee.getPosition(),
                employee.getAge(), employee.getWorkExperience(), employee.getEmail(),
                validateTelNumber(employee.getTelNumber()), id);

    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Employee WHERE id=?", id);
    }

    /* Телефонный номер в формате X (XXX) XX-XX-XX */
    private String validateTelNumber(String telNumber) {
        StringBuilder numbers = new StringBuilder(telNumber.replaceAll("\\D*", ""));
        if (numbers.length() != 11) {
            return null;
        }
        numbers.insert(1, ' ');
        numbers.insert(2, '(');
        numbers.insert(6, ')');
        numbers.insert(7, ' ');
        numbers.insert(11, '-');
        numbers.insert(14, '-');

        return numbers.toString();

    }
}
