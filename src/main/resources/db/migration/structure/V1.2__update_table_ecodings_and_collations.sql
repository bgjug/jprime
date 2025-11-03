-- Only alter tables if they exist (for fresh installations, Hibernate creates them with correct encoding)
SET @table_exists = (SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'jprime' AND table_name = 'Article');
SET @sql = IF(@table_exists > 0, 'ALTER TABLE Article CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci', 'SELECT "Article table does not exist, skipping"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @table_exists = (SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'jprime' AND table_name = 'Session');
SET @sql = IF(@table_exists > 0, 'ALTER TABLE Session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci', 'SELECT "Session table does not exist, skipping"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @table_exists = (SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'jprime' AND table_name = 'tags_articles');
SET @sql = IF(@table_exists > 0, 'ALTER TABLE tags_articles CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci', 'SELECT "tags_articles table does not exist, skipping"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @table_exists = (SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'jprime' AND table_name = 'Tag');
SET @sql = IF(@table_exists > 0, 'ALTER TABLE Tag CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci', 'SELECT "Tag table does not exist, skipping"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @table_exists = (SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'jprime' AND table_name = 'User');
SET @sql = IF(@table_exists > 0, 'ALTER TABLE User CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci', 'SELECT "User table does not exist, skipping"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @table_exists = (SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'jprime' AND table_name = 'VenueHall');
SET @sql = IF(@table_exists > 0, 'ALTER TABLE VenueHall CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci', 'SELECT "VenueHall table does not exist, skipping"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @table_exists = (SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'jprime' AND table_name = 'Visitor');
SET @sql = IF(@table_exists > 0, 'ALTER TABLE Visitor CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci', 'SELECT "Visitor table does not exist, skipping"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @table_exists = (SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'jprime' AND table_name = 'Registrant');
SET @sql = IF(@table_exists > 0, 'ALTER TABLE Registrant CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci', 'SELECT "Registrant table does not exist, skipping"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @table_exists = (SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'jprime' AND table_name = 'VisitorJPro');
SET @sql = IF(@table_exists > 0, 'ALTER TABLE VisitorJPro CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci', 'SELECT "VisitorJPro table does not exist, skipping"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @table_exists = (SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'jprime' AND table_name = 'ResetPasswordToken');
SET @sql = IF(@table_exists > 0, 'ALTER TABLE ResetPasswordToken CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci', 'SELECT "ResetPasswordToken table does not exist, skipping"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @table_exists = (SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'jprime' AND table_name = 'Submission');
SET @sql = IF(@table_exists > 0, 'ALTER TABLE Submission CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci', 'SELECT "Submission table does not exist, skipping"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @table_exists = (SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'jprime' AND table_name = 'Partner');
SET @sql = IF(@table_exists > 0, 'ALTER TABLE Partner CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci', 'SELECT "Partner table does not exist, skipping"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;