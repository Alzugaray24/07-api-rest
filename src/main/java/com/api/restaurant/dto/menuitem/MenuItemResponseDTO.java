package com.api.restaurant.dto.menuitem;

import com.api.restaurant.dto.dish.DishResponseDTO;
import com.api.restaurant.models.MenuItem;
import com.api.restaurant.models.MenuItemCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuItemResponseDTO {
    private Long id;
    private DishResponseDTO dish;
    private MenuItemCategory category;
    private Double specialPrice;
    private Double finalPrice;
    private boolean available;

    public MenuItemResponseDTO() {
    }

    public MenuItemResponseDTO(MenuItem menuItem) {
        this.id = menuItem.getId();
        this.dish = new DishResponseDTO(menuItem.getDish());
        this.category = menuItem.getCategory();
        this.specialPrice = menuItem.getSpecialPrice();
        this.finalPrice = menuItem.getFinalPrice();
        this.available = menuItem.isAvailable();
    }
}