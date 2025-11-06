call ExecuteStatementIfTableExists('Registrant', 'ALTER TABLE Registrant CHANGE branch branch VarChar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci');
call ExecuteStatementIfTableExists('Submission', 'ALTER TABLE Submission CHANGE branch branch VarChar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci');
call ExecuteStatementIfTableExists('User', 'ALTER TABLE User CHANGE branch branch VarChar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci');
