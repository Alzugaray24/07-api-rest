package com.api.restaurant.services;

import com.api.restaurant.models.Dish;
import com.api.restaurant.repositories.DishRepository;
import com.api.restaurant.services.interfaces.IDishService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Getter
public class DishService implements IDishService {

    private static final Logger logger = LoggerFactory.getLogger(DishService.class);

    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @Override
    public Dish saveDish(Dish dish) {
        logger.info("Guardando nuevo plato: {}", dish.getName());
        try {
            // Asegurar que el plato está activo por defecto
            dish.setActive(true);
            Dish savedDish = dishRepository.save(dish);
            logger.info("Plato guardado con ID: {}", savedDish.getId());
            return savedDish;
        } catch (Exception e) {
            logger.error("Error al guardar plato {}: {}", dish.getName(), e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public Dish getDishById(Long id) {
        logger.debug("Buscando plato con ID: {}", id);
        try {
            Dish dish = dishRepository.findById(id).orElse(null);
            if (dish == null) {
                logger.info("No se encontró plato con ID: {}", id);
            } else {
                logger.debug("Plato encontrado: {}", dish.getName());
            }
            return dish;
        } catch (Exception e) {
            logger.error("Error al buscar plato con ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public List<Dish> getAllDishes() {
        logger.debug("Obteniendo todos los platos");
        try {
            List<Dish> dishes = dishRepository.findAll();
            logger.info("Se encontraron {} platos", dishes.size());
            return dishes;
        } catch (Exception e) {
            logger.error("Error al obtener todos los platos: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public List<Dish> getActiveDishes() {
        logger.debug("Obteniendo todos los platos activos");
        try {
            List<Dish> dishes = dishRepository.findAll();
            List<Dish> activeDishes = dishes.stream()
                    .filter(Dish::isActive)
                    .collect(Collectors.toList());
            logger.info("Se encontraron {} platos activos", activeDishes.size());
            return activeDishes;
        } catch (Exception e) {
            logger.error("Error al obtener platos activos: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteDish(Long id) {
        logger.info("Eliminando plato con ID: {}", id);
        try {
            dishRepository.deleteById(id);
            logger.info("Plato con ID {} eliminado correctamente", id);
        } catch (Exception e) {
            logger.error("Error al eliminar plato con ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public Dish setDishStatus(Long id, boolean active) {
        logger.info("Cambiando estado del plato con ID {}: activo={}", id, active);
        try {
            return dishRepository.findById(id).map(dish -> {
                dish.setActive(active);
                logger.debug("Estado del plato {} actualizado a: {}", dish.getName(), active);
                return dishRepository.save(dish);
            }).orElseThrow(() -> {
                logger.warn("No se encontró el plato con ID {} para actualizar su estado", id);
                return new RuntimeException("El plato con el id " + id + " no se ha encontrado");
            });
        } catch (Exception e) {
            logger.error("Error al cambiar estado del plato con ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public Dish updateDish(Long id, Dish updatedDish) {
        logger.info("Actualizando plato con ID: {}", id);
        try {
            return dishRepository.findById(id).map(dish -> {
                dish.setName(updatedDish.getName());
                dish.setPrice(updatedDish.getPrice());
                if (updatedDish.getType() != null) {
                    dish.setType(updatedDish.getType());
                }
                // Preservar el estado active a menos que se cambie explícitamente
                if (updatedDish.isActive() != dish.isActive()) {
                    dish.setActive(updatedDish.isActive());
                }
                logger.debug("Plato {} actualizado", dish.getName());
                return dishRepository.save(dish); // Save the modified dish
            }).orElseThrow(() -> {
                logger.warn("No se encontró el plato con ID {} para actualizar", id);
                return new RuntimeException("El plato con el id " + id + " no se ha encontrado");
            });
        } catch (Exception e) {
            logger.error("Error al actualizar plato con ID {}: {}", id, e.getMessage());
            throw e;
        }
    }
}