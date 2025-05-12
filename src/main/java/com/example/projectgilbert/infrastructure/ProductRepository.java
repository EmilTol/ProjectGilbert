package com.example.projectgilbert.infrastructure;

import com.example.projectgilbert.entity.Category;
import com.example.projectgilbert.entity.Listing;
import com.example.projectgilbert.entity.Size;

import java.util.List;


public interface ProductRepository {
    void save(Listing ad);

    List<Category> findAllCategories();

    List<Category> findSubCategoriesByParentId(Long parentId);

    List<Size> findSizesByCategoryId(Long categoryId);
    List<Listing> findListingsBySellerId(Long sellerId);

}