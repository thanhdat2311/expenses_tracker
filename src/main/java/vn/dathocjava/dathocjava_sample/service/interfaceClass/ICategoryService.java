package vn.dathocjava.dathocjava_sample.service.interfaceClass;

import vn.dathocjava.dathocjava_sample.dto.request.CategoryDTO;
import vn.dathocjava.dathocjava_sample.model.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategory();
    Category createCategory(CategoryDTO categoryDTO);
}
