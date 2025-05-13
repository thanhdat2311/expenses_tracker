package vn.dathocjava.dathocjava_sample.dto.mapper;

import vn.dathocjava.dathocjava_sample.dto.request.CategoryDTO;
import vn.dathocjava.dathocjava_sample.dto.response.CategoryResponse;
import vn.dathocjava.dathocjava_sample.model.Category;

public class CategoryMapper {

    public static Category toEntity(CategoryDTO dto) {
        return Category.builder()
                .name(dto.getName())
                .color(dto.getColor())
                .type(dto.getType())
                .build();
    }

    public static CategoryResponse toResponse(Category entity) {
        return CategoryResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .color(entity.getColor())
                .type(entity.getType())
                .build();
    }
}

