package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryMapper;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.IntegrityException;
import ru.practicum.ewm.exception.NotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    private final EventRepository eventRepository;

    public CategoryDto create(NewCategoryDto newCategoryDto) {
        if (categoryRepository.existsByNameIgnoreCase(newCategoryDto.getName())) {
            throw new IntegrityException("Category with name='" + newCategoryDto.getName() + "' already exists");
        }
        return CategoryMapper.toCategoryDto(
                categoryRepository.save(
                        CategoryMapper.toCategory(newCategoryDto)));
    }

    public void delete(Long catId) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
        if (eventRepository.existsByCategoryId(catId)) {
            throw new IntegrityException("Category with events can't be deleted");
        }
        categoryRepository.deleteById(catId);
    }

    public CategoryDto update(Long catId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
        if (categoryRepository.existsByNameIgnoreCase(categoryDto.getName())) {
            throw new IntegrityException("Category with name=" + categoryDto.getName() + " already exists");
        }
        category.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }
}
