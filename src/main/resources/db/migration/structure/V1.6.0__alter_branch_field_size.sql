ALTER TABLE Registrant
    CHANGE branch branch VarChar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE Submission
    CHANGE branch branch VarChar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE User
    CHANGE branch branch VarChar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
