package kz.adem.tasklist.service;

import kz.adem.tasklist.domain.task.Task;

import java.util.List;

public interface TaskService {

    Task getById(Long id);

    List<Task> getAllByUserId(Long userId);

    void update(Task task);

    Task create(Task task, Long userId);

    void delete(Long id);
}
