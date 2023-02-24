package ru.practicum.ewm.compilation.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCompilationDto {
    List<Long> events;
    Boolean pinned = false;
    @NotBlank
    String title;
}
