package com.api.restaurant.services;

import com.api.restaurant.repositories.MenuRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;


@Getter
@Service
public class MenuService {
    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

}