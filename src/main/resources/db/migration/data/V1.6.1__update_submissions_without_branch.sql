-- Only update if table exists
SET @table_exists = (SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'jprime' AND table_name = 'Submission');
SET @sql = IF(@table_exists > 0, 'UPDATE Submission SET branch = ''YEAR_2015'' WHERE branch IS NULL', 'SELECT "Submission table does not exist, skipping"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;