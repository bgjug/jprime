ALTER DATABASE jprime DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_general_ci;

DELIMITER $$

DROP PROCEDURE IF EXISTS ExecuteStatementIfTableExists$$
DROP PROCEDURE IF EXISTS ExecuteStatementIfTableDoesNotExists$$

CREATE PROCEDURE ExecuteStatementIfTableExists(
    IN tableName VARCHAR(255),
    IN statement VARCHAR(16383)
)
BEGIN
    SET @table_exists = (SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'jprime' AND table_name = tableName);
    SET @sql = IF(@table_exists > 0, statement, concat('SELECT "', tableName, ' table does not exist, skipping"'));
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END$$

CREATE PROCEDURE ExecuteStatementIfTableDoesNotExists(
    IN tableName VARCHAR(255),
    IN statement VARCHAR(16383)
)
BEGIN
    SET @table_exists = (SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'jprime' AND table_name = tableName);
    SET @sql = IF(@table_exists = 0, statement, concat('SELECT "', tableName , ' table exists, skipping"'));
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END$$

DELIMITER ;
