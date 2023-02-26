package ru.practicum.ewm.util.time.validator;

import ru.practicum.ewm.exception.IntegrityException;
import ru.practicum.ewm.util.time.converter.TimeConverter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class LocalDateTimeValidator implements ConstraintValidator<ValidLocalDateTime, String> {

    @Override
    public boolean isValid(String dateTimeString, ConstraintValidatorContext context) {
        if (dateTimeString == null) {
            return true;
        }
        LocalDateTime dateTime = TimeConverter.toTime(dateTimeString);
        if (dateTime.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IntegrityException("Event start time must be at least 2 hours in the future");
        }
        return true;
    }

}
