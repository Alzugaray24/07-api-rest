package com.api.restaurant.dto.menuitem;

import com.api.restaurant.models.MenuItemCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuItemRequestDTO {
    private Long dishId;
    private MenuItemCategory category;
    private Double specialPrice;
    private boolean available = true;

    public MenuItemRequestDTO() {
    }
}