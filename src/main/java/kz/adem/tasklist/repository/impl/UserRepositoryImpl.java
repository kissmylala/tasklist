package kz.adem.tasklist.repository.impl;

import kz.adem.tasklist.config.DataSourceConfig;
import kz.adem.tasklist.domain.user.Role;
import kz.adem.tasklist.domain.user.User;
import kz.adem.tasklist.exception.ResourceMappingException;
import kz.adem.tasklist.repository.UserRepository;
import kz.adem.tasklist.web.dto.mappers.UserRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

//@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final DataSourceConfig dataSourceConfig;
    private final String FIND_BY_ID = """
            SELECT u.id as user_id,
            u.name as user_name,
            u.username as user_username,
            u.password as user_password,
            ur.role as user_role_role,
            t.id as task_id,
            t.title as task_title,
            t.description as task_description,
            t.expiration_date as task_expiration_date,
            t.status as task_status
            FROM tasklist.users u
            LEFT JOIN tasklist.users_roles ur on u.id = ur.user_id
            LEFT JOIN tasklist.users_tasks ut on u.id = ut.user_id
            LEFT JOIN tasklist.tasks t on ut.task_id = t.id
            WHERE u.id = ?
            """;
    private final String FIND_BY_USERNAME = """
            SELECT u.id as user_id,
            u.name as user_name,
            u.username as user_username,
            u.password as user_password,
            ur.role as user_role_role,
            t.id as task_id,
            t.title as task_title,
            t.description as task_description,
            t.expiration_date as task_expiration_date,
            t.status as task_status
            FROM tasklist.users u
            LEFT JOIN tasklist.users_roles ur on u.id = ur.user_id
            LEFT JOIN tasklist.users_tasks ut on u.id = ut.user_id
            LEFT JOIN tasklist.tasks t on ut.task_id = t.id
            WHERE u.username = ?
            """;

    private final String UPDATE = """
            UPDATE tasklist.users
            SET name = ?,
                username = ?,
                password = ?
            WHERE id = ?
            """;
    private final String CREATE = """
            INSERT INTO tasklist.users (name, username, password)
            VALUES (?, ?, ?);
            """;
    private final String INSERT_USER_ROLE = """
            INSERT INTO tasklist.users_roles (user_id, role)
            VALUES (?, ?);
            """;
    private final String DELETE = """
            DELETE FROM tasklist.users
            WHERE id = ?
            """;

    private final String IS_TASK_OWNER = """
            SELECT EXISTS(
                SELECT 1
                FROM tasklist.users_tasks
                WHERE user_id = ?
                AND task_id = ?
            )
            """;

    @Override
    public Optional<User> findById(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return Optional.ofNullable(UserRowMapper.mapRow(resultSet));
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while finding user by id");
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_USERNAME, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return Optional.ofNullable(UserRowMapper.mapRow(resultSet));
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while finding user by username");
        }
    }

    @Override
    public void update(User user) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setLong(4, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while updating user");
        }
    }

    @Override
    public void create(User user) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                resultSet.next();
                user.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while updating user");
        }
    }

    @Override
    public void insertUserRole(Long userId, Role role) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_ROLE);
            preparedStatement.setLong(1, userId);
            preparedStatement.setString(2, role.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while inserting user role");
        }
    }

    @Override
    public boolean isTaskOwner(Long userId, Long taskId) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(IS_TASK_OWNER);
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, taskId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while checking isTaskOwner");
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while deleting user");
        }
    }
}
