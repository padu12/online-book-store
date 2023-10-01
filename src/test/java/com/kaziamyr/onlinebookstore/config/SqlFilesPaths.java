package com.kaziamyr.onlinebookstore.config;

public class SqlFilesPaths {
    public static final String ADD_LISOVA_PISNIA =
            "classpath:database/books/add-lisova-pisnia-to-books-table.sql";
    public static final String ADD_ZAKHAR_BERKUT =
            "classpath:database/books/add-zakhar-berkut-to-books-table.sql";
    public static final String ADD_LESIA_UKRAINKA_FICTION_RELATION = "classpath:database/"
            + "books_categories/add-lesia-ukrainka-fiction-relation-to-books_categories-table.sql";
    public static final String ADD_ZAKHAR_BERKUT_FICTION_RELATION = "classpath:database/"
            + "books_categories/add-zakhar-berkut-fiction-relation-to-books_categories-table.sql";
    public static final String ADD_FANTASY =
            "classpath:database/categories/add-fantasy-to-categories-table.sql";
    public static final String ADD_FICTION =
            "classpath:database/categories/add-fiction-to-categories-table.sql";
    public static final String ADD_ORDER =
            "classpath:database/order/add-order-to-orders-table.sql";
    public static final String ADD_EMPTY_SHOPPING_CART =
            "classpath:database/shoppingcart/add-empty-shopping-cart-to-shopping_carts_table.sql";
    public static final String ADD_SHOPPING_CART =
            "classpath:database/shoppingcart/add-shopping-cart-to-shopping_carts-table.sql";
    public static final String DELETE_ORDER_RELATED_TABLES =
            "classpath:database/delete-all-data-from-order-related-tables.sql";
    public static final String DELETE_SHOPPING_CART_AND_ORDER_RELATED_TABLES =
            "classpath:database/delete-all-data-from-shopping-cart-and-order-related-tables.sql";
    public static final String DELETE_SHOPPING_CART_RELATED_TABLES =
            "classpath:database/delete-all-data-from-shopping-cart-related-tables.sql";
    public static final String DELETE_CATEGORIES_BOOKS_AND_BOOKS_CATEGORIES_TABLES =
            "classpath:database/delete-all-from-categories-books-books_categories-tables.sql";
}
