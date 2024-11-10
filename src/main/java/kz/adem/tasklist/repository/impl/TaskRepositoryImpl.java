package kz.adem.tasklist.repository.impl;

import kz.adem.tasklist.config.DataSourceConfig;
import kz.adem.tasklist.domain.task.Task;
import kz.adem.tasklist.exception.ResourceMappingException;
import kz.adem.tasklist.repository.TaskRepository;
import kz.adem.tasklist.web.dto.mappers.TaskRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

//@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final DataSourceConfig dataSourceConfig;
    private final String FIND_BY_ID = """
            SELECT t.id as task_id,
                   t.title as task_title,
                   t.description as task_description,
                   t.expiration_date as task_expiration_date,
                   t.status as task_status
            FROM tasklist.tasks t
            WHERE id = ?
            """;
    private final String FIND_ALL_BY_USER_ID = """
            SELECT t.id as task_id,
                   t.title as task_title,
                   t.description as task_description,
                   t.expiration_date as task_expiration_date,
                   t.status as task_status
            FROM tasklist.tasks t
            JOIN tasklist.users_tasks ut on ut.task_id = t.id 
            WHERE ut.user_id = ?
            """;

    private final String ASSIGN_TO_USER_BY_ID = """
            INSERT INTO tasklist.users_tasks (task_id, user_id) 
            VALUES (?,?)
            """;

    private final String UPDATE_TASK = """
            UPDATE tasklist.tasks
            SET title = ?,
                description = ?,
                expiration_date = ?,
                status = ?
            WHERE id = ?
            """;

    private final String CREATE_TASK = """
            INSERT INTO tasklist.tasks (title, description, expiration_date, status)
            VALUES (?, ?, ?, ?)
            """;

    private final String DELETE_TASK = """
            DELETE FROM tasklist.tasks
            WHERE id = ?
            """;

    @Override
    public Optional<Task> findById(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return Optional.ofNullable(TaskRowMapper.mapRow(resultSet));
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while finding task by id.");
        }
    }

    @Override
    public List<Task> findAllByUserId(Long userId) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_USER_ID);
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return TaskRowMapper.mapRows(resultSet);
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while finding all tasks by user id.");
        }
    }

    @Override
    public void assignToUserById(Long taskId, Long userId) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(ASSIGN_TO_USER_BY_ID);
            preparedStatement.setLong(1, taskId);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while assigning to user ");
        }
    }

    @Override
    public void update(Task task) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TASK);
            preparedStatement.setString(1, task.getTitle());
            if (task.getDescription() == null) {
                preparedStatement.setNull(2, Types.VARCHAR);
            } else {
                preparedStatement.setString(2, task.getDescription());
            }
            if (task.getExpirationDate() == null) {
                preparedStatement.setNull(3, Types.TIMESTAMP);
            } else {
                preparedStatement.setTimestamp(3, Timestamp.valueOf(task.getExpirationDate()));
            }
            preparedStatement.setString(4, task.getStatus().name());
            preparedStatement.setLong(5, task.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while updating task.");
        }
    }

    @Override
    public void save(Task task) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_TASK, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, task.getTitle());
            if (task.getDescription() == null) {
                statement.setNull(2, Types.VARCHAR);
            } else {
                statement.setString(2, task.getDescription());
            }
            if (task.getExpirationDate() == null) {
                statement.setNull(3, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(3, Timestamp.valueOf(task.getExpirationDate()));
            }
            statement.setString(4, task.getStatus().name());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                rs.next();
                task.setId(rs.getLong(1));
            }
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Error while creating task.");
        }
    }

    @Override
    public void delete(Task task) {
        try {
          Connection connection = dataSourceConfig.getConnection();
          PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TASK);
          preparedStatement.setLong(1, task.getId());
          preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new ResourceMappingException("Error while deleting task.");
        }
    }
}
