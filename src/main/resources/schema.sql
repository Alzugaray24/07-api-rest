-- Script para verificar y crear tipos enum necesarios

-- Primero verificamos si el tipo menu_item_category existe
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'menu_item_category') THEN
        CREATE TYPE menu_item_category AS ENUM (
            'APPETIZER', 
            'MAIN_COURSE', 
            'DESSERT', 
            'BEVERAGE', 
            'SIDE_DISH'
        );
    END IF;
END$$;

-- Ahora creamos una consulta simple para verificar qué tipos existen
SELECT 
    n.nspname as schema,
    t.typname as type_name,
    CASE 
        WHEN t.typtype = 'e' THEN 'enum'
        ELSE 'other'
    END as type_category
FROM pg_type t 
JOIN pg_namespace n ON n.oid = t.typnamespace 
WHERE t.typtype = 'e'; -- 'e' para enums 