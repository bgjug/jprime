DELIMITER $$

DROP PROCEDURE IF EXISTS RemoveForeignKeyIfExists$$
DROP PROCEDURE IF EXISTS RemoveIndexIfExists$$

CREATE PROCEDURE RemoveForeignKeyIfExists(
    IN tableName VARCHAR(255),
    IN foreignKeyName VARCHAR(255)
)
BEGIN
    DECLARE fkExists INT;

    -- Check if the foreign key exists
SELECT COUNT(*)
INTO fkExists
FROM information_schema.TABLE_CONSTRAINTS
WHERE CONSTRAINT_NAME = foreignKeyName
  AND TABLE_NAME = tableName
  AND CONSTRAINT_TYPE = 'FOREIGN KEY'
  AND TABLE_SCHEMA = DATABASE();

-- If the foreign key exists, drop it
IF fkExists > 0 THEN
        SET @dropFkQuery = CONCAT('ALTER TABLE ', tableName, ' DROP FOREIGN KEY ', foreignKeyName);
PREPARE stmt FROM @dropFkQuery;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END IF;
END$$

CREATE PROCEDURE RemoveIndexIfExists(
    IN tableName VARCHAR(255),
    IN indexName VARCHAR(255)
)
BEGIN
    DECLARE indexExists INT;

    -- Check if the index exists
SELECT COUNT(*)
INTO indexExists
FROM information_schema.STATISTICS
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME = tableName
  AND INDEX_NAME = indexName;

-- If the index exists, drop it
IF indexExists > 0 THEN
        SET @dropIndexQuery = CONCAT('ALTER TABLE ', tableName, ' DROP INDEX ', indexName);
PREPARE stmt FROM @dropIndexQuery;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END IF;
END$$

DELIMITER ;