package com.api.restaurant.dto.menu;

import com.api.restaurant.dto.menuitem.MenuItemRequestDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuRequestDTO {
    private String name;
    private boolean active = true;
    private List<MenuItemRequestDTO> items;

    public MenuRequestDTO() {
    }
}