package ru.practicum.ewm.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;
    String name;
    String email;
}
