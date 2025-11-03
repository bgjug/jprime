-- Only alter tables if they exist and have the branch column
SET @table_exists = (SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = 'jprime' AND table_name = 'Registrant' AND column_name = 'branch');
SET @sql = IF(@table_exists > 0, 'ALTER TABLE Registrant CHANGE branch branch VarChar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci', 'SELECT "Registrant.branch does not exist, skipping"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @table_exists = (SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = 'jprime' AND table_name = 'Submission' AND column_name = 'branch');
SET @sql = IF(@table_exists > 0, 'ALTER TABLE Submission CHANGE branch branch VarChar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci', 'SELECT "Submission.branch does not exist, skipping"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @table_exists = (SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = 'jprime' AND table_name = 'User' AND column_name = 'branch');
SET @sql = IF(@table_exists > 0, 'ALTER TABLE User CHANGE branch branch VarChar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci', 'SELECT "User.branch does not exist, skipping"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
