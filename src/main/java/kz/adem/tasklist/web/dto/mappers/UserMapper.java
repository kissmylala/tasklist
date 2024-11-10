package kz.adem.tasklist.web.dto.mappers;

import kz.adem.tasklist.domain.user.User;
import kz.adem.tasklist.web.dto.user.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);
}
