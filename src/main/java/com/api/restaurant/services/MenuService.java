package com.api.restaurant.services;

import com.api.restaurant.models.Dish;
import com.api.restaurant.models.Menu;
import com.api.restaurant.models.MenuItem;
import com.api.restaurant.models.MenuItemCategory;
import com.api.restaurant.repositories.MenuRepository;
import com.api.restaurant.services.interfaces.IMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService implements IMenuService {

    private static final Logger logger = LoggerFactory.getLogger(MenuService.class);

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public Menu saveMenu(Menu menu) {
        logger.info("Guardando nuevo menú: {}", menu.getName());
        try {
            Menu savedMenu = menuRepository.save(menu);
            logger.info("Menú guardado con ID: {}", savedMenu.getId());
            return savedMenu;
        } catch (Exception e) {
            logger.error("Error al guardar menú {}: {}", menu.getName(), e.getMessage());
            throw e;
        }
    }

    @Override
    public Menu getMenuById(Long id) {
        logger.debug("Buscando menú con ID: {}", id);
        try {
            Menu menu = menuRepository.findById(id).orElse(null);
            if (menu == null) {
                logger.info("No se encontró menú con ID: {}", id);
            } else {
                logger.debug("Menú encontrado: {}", menu.getName());
            }
            return menu;
        } catch (Exception e) {
            logger.error("Error al buscar menú con ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Menu> getAllMenus() {
        logger.debug("Obteniendo todos los menús");
        try {
            List<Menu> menus = menuRepository.findAll();
            logger.info("Se encontraron {} menús", menus.size());
            return menus;
        } catch (Exception e) {
            logger.error("Error al obtener todos los menús: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteMenu(Long id) {
        logger.info("Eliminando menú con ID: {}", id);
        try {
            menuRepository.deleteById(id);
            logger.info("Menú con ID {} eliminado correctamente", id);
        } catch (Exception e) {
            logger.error("Error al eliminar menú con ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    public Menu updateMenu(Long id, Menu updatedMenu) {
        logger.info("Actualizando menú con ID: {}", id);
        try {
            return menuRepository.findById(id)
                    .map(menu -> {
                        // Update basic information
                        menu.setName(updatedMenu.getName());
                        menu.setActive(updatedMenu.isActive());

                        // Clear existing menu items
                        menu.getMenuItems().clear();

                        // Add new menu items
                        for (MenuItem item : updatedMenu.getMenuItems()) {
                            item.setMenu(menu);
                            menu.addMenuItem(item);
                        }

                        logger.debug("Menú {} actualizado con {} elementos", menu.getName(),
                                menu.getMenuItems().size());
                        return menuRepository.save(menu);
                    }).orElseThrow(() -> {
                        logger.warn("No se encontró el menú con ID {} para actualizar", id);
                        return new RuntimeException("El menu con el id " + id + " no se ha encontrado");
                    });
        } catch (Exception e) {
            logger.error("Error al actualizar menú con ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    public Menu addItemToMenu(Long menuId, Dish dish, MenuItemCategory category, Double specialPrice,
            boolean available) {
        logger.info("Agregando plato {} al menú con ID: {}", dish.getName(), menuId);
        try {
            Menu menu = getMenuById(menuId);
            if (menu == null) {
                logger.warn("No se encontró el menú con ID: {}", menuId);
                throw new RuntimeException("El menú con el id " + menuId + " no se ha encontrado");
            }

            // Crear el nuevo MenuItem
            MenuItem menuItem = new MenuItem();
            menuItem.setDish(dish);
            menuItem.setCategory(category != null ? category : MenuItemCategory.MAIN_COURSE);
            menuItem.setSpecialPrice(specialPrice);
            menuItem.setAvailable(available);

            // Agregar al menú
            menu.addMenuItem(menuItem);
            logger.debug("Agregado item {} al menú {}", dish.getName(), menu.getName());

            // Guardar y retornar el menú actualizado
            Menu updatedMenu = menuRepository.save(menu);
            logger.info("Menú con ID {} actualizado con nuevo item {}", menuId, dish.getName());
            return updatedMenu;
        } catch (Exception e) {
            logger.error("Error al agregar item al menú con ID {}: {}", menuId, e.getMessage());
            throw e;
        }
    }
}