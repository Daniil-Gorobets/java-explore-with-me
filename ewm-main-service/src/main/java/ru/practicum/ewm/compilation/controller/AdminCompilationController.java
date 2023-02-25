package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.service.AdminCompilationService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminCompilationController {

    private final AdminCompilationService adminCompilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody @Valid @NotNull NewCompilationDto newCompilationDto) {
        log.info("AdminCompilationController - POST: /admin/compilations newCompilationDto={}", newCompilationDto);
        return adminCompilationService.createCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable @Positive @NotNull Long compId) {
        log.info("AdminCompilationController - DELETE: /admin/compilations/{}", compId);
        adminCompilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(
            @PathVariable @Positive @NotNull Long compId,
            @RequestBody @Valid @NotNull UpdateCompilationRequest updateCompilationRequest) {
        log.info("AdminCompilationController - POST: /admin/compilations/{} updateCompilationRequest={}", compId,
                updateCompilationRequest);
        return adminCompilationService.updateCompilation(compId, updateCompilationRequest);
    }
}
