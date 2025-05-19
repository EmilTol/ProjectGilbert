package com.example.projectgilbert.infrastructure;

import com.example.projectgilbert.entity.Category;
import com.example.projectgilbert.entity.Listing;
import com.example.projectgilbert.entity.Size;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ListingRepository {

    private final JdbcTemplate jdbcTemplate;

    public ListingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //opretter ny listing

    public void save(Listing ad) {
        String sql = "INSERT INTO listings (seller_id, category_id, size_id, item_type, model, brand, description, " +
                "conditions, materials, price, max_discount_percent, color, status, is_fair_trade, is_validated, image_file_name) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
                ad.getStatus().name(),
                false,
                false,
                ad.getImageFileName()
        );
    }

    //viser alle kategorier, som women, men, children
//    @Override
    public List<Category> findAllCategories() {
        String sql = "SELECT * FROM categories WHERE parent_id IS NULL";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Category.class)); //Den her mapper automatisk
        //BeanProperty mapper automatisk, klassen nedenunder et eksempel på hvordan det ser ud hvis man ikke bruger det
    }


    //viser kategorier der høre under f.eks. women
//    @Override
//    public List<Category> findSubCategoriesByParentId(Long parentId) {
//        String sql = "SELECT * FROM categories WHERE parent_id = ?";
//        return jdbcTemplate.query(sql, (rs, rowNum) -> {
//            Category category = new Category();
//            category.setCategoryId(rs.getLong("category_id")); //det er er "manuel mapping"
//            category.setParentId(rs.getLong("parent_id"));
//            category.setName(rs.getString("name"));
//            return category;
//        }, parentId);
//    }

    //viser størrelser af relevante valgte kategorier
//    @Override
    public List<Size> findSizesByCategoryId(Long categoryId) {
        String sql = "SELECT * FROM sizes WHERE category_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Size.class), categoryId);
    }

    public List<Category> findAllCategoriesFlat() {
        String sql = "SELECT * FROM categories";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Category.class));
    }

    public List<Size> findAllSizes() {
        String sql = "SELECT * FROM sizes";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Size.class));
    }


    //    @Override
    public List<Listing> findListingsBySellerId(Long sellerId) {
        String sql = """
        SELECT l.*, s.size_label 
        FROM listings l
        LEFT JOIN sizes s ON l.size_id = s.size_id
        WHERE l.seller_id = ?
    """;

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Listing.class), sellerId);
    }

    public List<Listing> findAllListings() {
        String sql = "SELECT l.listing_id, l.seller_id, l.category_id, l.size_id, l.item_type, l.model, l.brand, " +
                "l.description, l.conditions, l.materials, l.price, l.max_discount_percent, l.created_at, " +
                "l.status, l.is_fair_trade, l.is_validated, l.color, s.size_label, l.image_file_name " +
                "FROM listings l LEFT JOIN sizes s ON l.size_id = s.size_id";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Listing listing = new Listing();
            listing.setListingId(rs.getLong("listing_id"));
            listing.setSellerId(rs.getLong("seller_id"));
            listing.setCategoryId(rs.getLong("category_id"));
            listing.setSizeId(rs.getLong("size_id"));
            listing.setItemType(rs.getString("item_type"));
            listing.setModel(rs.getString("model"));
            listing.setBrand(rs.getString("brand"));
            listing.setDescription(rs.getString("description"));
            listing.setConditions(rs.getString("conditions"));
            listing.setMaterials(rs.getString("materials"));
            listing.setPrice(rs.getBigDecimal("price"));
            listing.setMaxDiscountPercent(rs.getBigDecimal("max_discount_percent"));
            listing.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            listing.setStatus(Listing.Status.valueOf(rs.getString("status")));
            listing.setFairTrade(rs.getBoolean("is_fair_trade"));
            listing.setValidated(rs.getBoolean("is_validated"));
            listing.setColor(rs.getString("color"));
            listing.setSizeLabel(rs.getString("size_label"));
            listing.setImageFileName(rs.getString("image_file_name"));
            return listing;
        });
    }

    public Listing findListingById(Long listingId) {
        try {
            String sql = "SELECT l.listing_id, l.seller_id, l.category_id, l.size_id, l.item_type, l.model, l.brand, " +
                    "l.description, l.conditions, l.materials, l.price, l.max_discount_percent, l.created_at, " +
                    "l.status, l.is_fair_trade, l.is_validated, l.color, s.size_label, l.image_file_name " +
                    "FROM listings l LEFT JOIN sizes s ON l.size_id = s.size_id WHERE l.listing_id = ?";

            return jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{listingId},
                    (rs, rowNum) -> {
                        Listing listing = new Listing();
                        listing.setListingId(rs.getLong("listing_id"));
                        listing.setSellerId(rs.getLong("seller_id"));
                        listing.setCategoryId(rs.getLong("category_id"));
                        listing.setSizeId(rs.getLong("size_id"));
                        listing.setItemType(rs.getString("item_type"));
                        listing.setModel(rs.getString("model"));
                        listing.setBrand(rs.getString("brand"));
                        listing.setDescription(rs.getString("description"));
                        listing.setConditions(rs.getString("conditions"));
                        listing.setMaterials(rs.getString("materials"));
                        listing.setPrice(rs.getBigDecimal("price"));
                        listing.setMaxDiscountPercent(rs.getBigDecimal("max_discount_percent"));
                        listing.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                        listing.setStatus(Listing.Status.valueOf(rs.getString("status")));
                        listing.setFairTrade(rs.getBoolean("is_fair_trade"));
                        listing.setValidated(rs.getBoolean("is_validated"));
                        listing.setColor(rs.getString("color"));
                        listing.setSizeLabel(rs.getString("size_label"));
                        listing.setImageFileName(rs.getString("image_file_name"));
                        return listing;
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public void update(Listing listing) {
        String sql = "UPDATE listings SET status = ? WHERE listing_id = ?";
        jdbcTemplate.update(sql, listing.getStatus().name(), listing.getListingId());
    }

    public List<Listing> findByStatus(Listing.Status status) {
        String sql = """
    SELECT l.*, s.size_label 
    FROM listings l
    LEFT JOIN sizes s ON l.size_id = s.size_id
    WHERE l.status = ?
    """;
        return jdbcTemplate.query(sql, new Object[]{status.name()}, (rs, rowNum) -> {
            Listing listing = new Listing();
            listing.setListingId(rs.getLong("listing_id"));
            listing.setSellerId(rs.getLong("seller_id"));
            listing.setCategoryId(rs.getLong("category_id"));
            listing.setSizeId(rs.getLong("size_id"));
            listing.setItemType(rs.getString("item_type"));
            listing.setModel(rs.getString("model"));
            listing.setBrand(rs.getString("brand"));
            listing.setDescription(rs.getString("description"));
            listing.setConditions(rs.getString("conditions"));
            listing.setMaterials(rs.getString("materials"));
            listing.setPrice(rs.getBigDecimal("price"));
            listing.setMaxDiscountPercent(rs.getBigDecimal("max_discount_percent"));
            listing.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            listing.setStatus(Listing.Status.valueOf(rs.getString("status")));
            listing.setFairTrade(rs.getBoolean("is_fair_trade"));
            listing.setValidated(rs.getBoolean("is_validated"));
            listing.setColor(rs.getString("color"));
            listing.setSizeLabel(rs.getString("size_label"));
            listing.setImageFileName(rs.getString("image_file_name"));
            return listing;
        });
    }
}