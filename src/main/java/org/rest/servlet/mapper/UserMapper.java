package org.rest.servlet.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.rest.model.User;
import org.rest.servlet.user.dto.UserIncomingDto;
import org.rest.servlet.user.dto.UserOutGoingDto;
import org.rest.servlet.user.dto.UserSimpleOutGoingDto;
import org.rest.servlet.user.dto.UserUpdateDto;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings({
            @Mapping(target= "id", source = "user.id"),
            @Mapping(target= "name", source = "user.name")
    })
    UserSimpleOutGoingDto userToUserSimpleOutGoingDto (User user);
    @Mappings({
            @Mapping(target= "name", source = "userDto.name"),
            @Mapping(target= "city", source = "userDto.city")
    })
    User userIncomingDtoToUser (UserIncomingDto userDto);

    @Mappings({
            @Mapping(target= "id", source = "userDto.id"),
            @Mapping(target= "name", source = "userDto.name")
    })
    User userUpdateDtoToUser(UserUpdateDto userDto);

    @Mappings({
            @Mapping(target= "name", source = "user.name"),
            @Mapping(target= "city", source = "user.city")
    })
    @IterableMapping(qualifiedByName = "bankToBankSimpleOutGoingDto")
    UserOutGoingDto userToUserOutGoingDto (User user);
}
