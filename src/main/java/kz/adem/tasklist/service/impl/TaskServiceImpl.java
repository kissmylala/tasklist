package kz.adem.tasklist.service.impl;

import kz.adem.tasklist.domain.task.Status;
import kz.adem.tasklist.domain.task.Task;
import kz.adem.tasklist.exception.ResourceNotFoundException;
import kz.adem.tasklist.repository.TaskRepository;
import kz.adem.tasklist.repository.UserRepository;
import kz.adem.tasklist.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Task getById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found with id = " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllByUserId(Long userId) {
        return taskRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    public void update(Task task) {
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
         taskRepository.update(task);
    }

    @Override
    @Transactional
    public Task create(Task task, Long userId) {
        task.setStatus(Status.TODO);
        taskRepository.save(task);
        taskRepository.assignToUserById(task.getId(), userId);
        return task;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Task task = getById(id);
        taskRepository.delete(task);
    }
}
