package kz.adem.tasklist.web.dto.mappers;

import kz.adem.tasklist.domain.task.Task;
import kz.adem.tasklist.web.dto.task.TaskDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskDTO toDTO(Task task);

    List<TaskDTO> toDTO(List<Task> tasks);

    Task toEntity(TaskDTO taskDTO);

    List<Task> toEntity(List<TaskDTO> taskDTOs);
}
