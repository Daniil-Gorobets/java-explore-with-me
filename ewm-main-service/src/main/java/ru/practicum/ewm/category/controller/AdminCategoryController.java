package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.service.AdminCategoryService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminCategoryController {

    @Autowired
    private final AdminCategoryService adminCategoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("AdminCategoryController - POST: /admin/categories newCategoryDto={}", newCategoryDto);
        return adminCategoryService.create(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable @NotNull @Positive Long catId) {
        log.info("AdminCategoryController - DELETE: /admin/categories/{}", catId);
        adminCategoryService.delete(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(
            @PathVariable @NotNull @Positive Long catId,
            @RequestBody @Valid CategoryDto categoryDto) {
        log.info("AdminCategoryController - PATCH: /admin/categories/{} categoryDto={}", catId, categoryDto);
        return adminCategoryService.update(catId, categoryDto);
    }
}
