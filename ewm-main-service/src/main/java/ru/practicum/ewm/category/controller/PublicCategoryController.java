package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.PublicCategoryService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PublicCategoryController {

    @Autowired
    private final PublicCategoryService publicCategoryService;

    @GetMapping
    public List<CategoryDto> getCategories(
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("PublicCategoryController - GET: /categories from={}, size={}", from, size);
        return publicCategoryService.getCategories(from, size);
    }

    /*
    Получение информации о категории по её идентификатору
     */
    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable @NotNull @Positive Long catId) {
        log.info("PublicCategoryController - GET: /categories/{}", catId);
        return publicCategoryService.getCategoryById(catId);
    }
}
