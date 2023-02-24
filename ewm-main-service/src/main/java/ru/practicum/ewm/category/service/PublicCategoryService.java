package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryMapper;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicCategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getCategories(Integer from, Integer size) {
        return categoryRepository.findAll(PageRequest.of(from / size, size)).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long catId) {
        return CategoryMapper.toCategoryDto(categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found")));
    }
}
