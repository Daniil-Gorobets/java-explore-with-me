package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.service.PublicCompilationService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PublicCompilationController {

    @Autowired
    private final PublicCompilationService publicCompilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("PublicCompilationController - GET: /compilations pinned={} from={}, size={}", pinned, from, size);
        return publicCompilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(
            @PathVariable @Positive @NotNull Long compId) {
        log.info("PublicCompilationController - GET: /compilation/{}", compId);
        return publicCompilationService.getCompilationById(compId);
    }

}
