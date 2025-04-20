package com.api.restaurant.dto.menu;

import com.api.restaurant.dto.menuitem.MenuItemResponseDTO;
import com.api.restaurant.models.Menu;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MenuResponseDTO {
    private Long id;
    private String name;
    private boolean active;
    private List<MenuItemResponseDTO> items;

    public MenuResponseDTO() {
    }

    public MenuResponseDTO(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.active = menu.isActive();
        this.items = menu.getMenuItems().stream()
                .map(MenuItemResponseDTO::new)
                .collect(Collectors.toList());
    }
}