package com.qna.domain.user.application.mapper;

import com.qna.domain.user.domain.entity.User;
import com.qna.domain.user.presentation.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
}
