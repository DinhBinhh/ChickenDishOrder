package com.springboot.dev_spring_boot_demo.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
public class Product {

    public enum Category {
        GANUONG("Gà nướng"),
        GAHAP("Gà hấp"),
        COMBO("Combo"),
        KHAC("Khác");

        private final String displayName;

        Category(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        @JsonValue
        public String toJson() {
            return name(); // Trả về tên Enum không dấu
        }

        @JsonCreator
        public static Category fromJson(String value) {
            for (Category category : values()) {
                if (category.name().equalsIgnoreCase(value)) {
                    return category;
                }
            }
            throw new IllegalArgumentException("Unknown category: " + value);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(length = 255)
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(name = "spicy_level")
    private Integer spicyLevel = 0;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    // Constructors
    public Product() {
    }

    public Product(Integer id, String name, String description, BigDecimal price,
                   String image, Category category, Integer spicyLevel, Boolean isFeatured) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.category = category;
        this.spicyLevel = spicyLevel;
        this.isFeatured = isFeatured;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getSpicyLevel() {
        return spicyLevel;
    }

    public void setSpicyLevel(Integer spicyLevel) {
        this.spicyLevel = spicyLevel;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void seIsFeatured(Boolean featured) {
        isFeatured = featured;
    }

    // Builder Pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer id;
        private String name;
        private String description;
        private BigDecimal price;
        private String image;
        private Category category;
        private Integer spicyLevel;
        private Boolean isFeatured;

        public boolean isFeatured() {
            return isFeatured;
        }

        // Và setter method
        public void setFeatured(boolean featured) {
            isFeatured = featured;
        }

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder image(String image) {
            this.image = image;
            return this;
        }

        public Builder category(Category category) {
            this.category = category;
            return this;
        }

        public Builder spicyLevel(Integer spicyLevel) {
            this.spicyLevel = spicyLevel;
            return this;
        }

        public Builder isFeatured(Boolean isFeatured) {
            this.isFeatured = isFeatured;
            return this;
        }

        public Product build() {
            return new Product(id, name, description, price, image,
                    category, spicyLevel, isFeatured);
        }
    }

    // Helper methods
    public String getFormattedPrice() {
        return String.format("%,.0f₫", this.price);
    }

    public String getSpicyIcon() {
        return "🌶️".repeat(Math.max(0, this.spicyLevel));
    }
}
