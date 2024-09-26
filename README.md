Sample project to modify Data model.

Takes values from a specific column in one table and creates a row of enriched data for each of those values in another table if such a row doesn't exist.

Uses Spring Batch to handle database data in chunks. Uses separate data connections for main model and batch tables.

@see [implementation](src/main/java/dk/lector/lts/utils/data_transformation) classes
