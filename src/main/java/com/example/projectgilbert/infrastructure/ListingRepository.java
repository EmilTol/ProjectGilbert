package com.example.projectgilbert.infrastructure;

import com.example.projectgilbert.entity.Category;
import com.example.projectgilbert.entity.Listing;
import com.example.projectgilbert.entity.Size;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ListingRepository implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ListingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //opretter ny listing
    @Override
    public void save(Listing ad) {
        String sql = "INSERT INTO listings (seller_id, category_id, size_id, item_type, model, brand, description, conditions, materials, price, max_discount_percent, color, status, is_fair_trade, is_validated) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                ad.getSellerId(),
                ad.getCategoryId(),
                ad.getSizeId(),
                ad.getItemType(),
                ad.getModel(),
                ad.getBrand(),
                ad.getDescription(),
                ad.getConditions(),
                ad.getMaterials(),
                ad.getPrice(),
                ad.getMaxDiscountPercent(),
                ad.getColor(),
                "PENDING",
                false,
                false
        );
    }
    //viser alle kategorier, som women, men, children
    @Override
    public List<Category> findAllCategories() {
        String sql = "SELECT * FROM categories WHERE parent_id IS NULL";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Category.class)); //Den her mapper automatisk
        //BeanProperty mapper automatisk, klassen nedenunder et eksempel på hvordan det ser ud hvis man ikke bruger det
    }


    //viser kategorier der høre under f.eks. women
    @Override
    public List<Category> findSubCategoriesByParentId(Long parentId) {
        String sql = "SELECT * FROM categories WHERE parent_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Category category = new Category();
            category.setCategoryId(rs.getLong("category_id")); //det er er "manuel mapping"
            category.setParentId(rs.getLong("parent_id"));
            category.setName(rs.getString("name"));
            return category;
        }, parentId);
    }

    //viser størrelser af relevante valgte kategorier
    @Override
    public List<Size> findSizesByCategoryId(Long categoryId) {
        String sql = "SELECT * FROM sizes WHERE category_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Size.class), categoryId);
    }

    @Override
    public List<Listing> findListingsBySellerId(Long sellerId) {
        String sql = """
        SELECT l.*, s.size_label 
        FROM listings l
        LEFT JOIN sizes s ON l.size_id = s.size_id
        WHERE l.seller_id = ?
    """;

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Listing.class), sellerId);
    }
}
