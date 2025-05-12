package com.example.projectgilbert.infrastructure;

import com.example.projectgilbert.entity.Category;
import com.example.projectgilbert.entity.SaleAdvertisement;
import com.example.projectgilbert.entity.Size;

import java.util.List;


public interface ProductRepository {
    void save(SaleAdvertisement ad);

    List<Category> findAllCategories();

    List<Category> findSubCategoriesByParentId(Long parentId);

    List<Size> findSizesByCategoryId(Long categoryId);
    List<SaleAdvertisement> findListingsBySellerId(Long sellerId);

}