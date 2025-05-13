package vn.dathocjava.dathocjava_sample.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.dathocjava.dathocjava_sample.dto.mapper.CategoryMapper;
import vn.dathocjava.dathocjava_sample.dto.request.CategoryDTO;
import vn.dathocjava.dathocjava_sample.model.Category;
import vn.dathocjava.dathocjava_sample.repository.CategoryRepo;
import vn.dathocjava.dathocjava_sample.service.interfaceClass.ICategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepo categoryRepo;
    @Override
    public List<Category> getAllCategory() {
        return categoryRepo.findAll();
    }

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category category = CategoryMapper.toEntity(categoryDTO);
        categoryRepo.save(category);
        return category;
    }
}
