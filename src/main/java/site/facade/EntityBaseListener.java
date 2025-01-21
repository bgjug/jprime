package site.facade;

import java.time.LocalDateTime;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import org.springframework.security.core.context.SecurityContextHolder;

import site.model.EntityBase;

public class EntityBaseListener {

    @PrePersist
    public void beforeSaveNew(EntityBase entity) {
        entity.setCreatedDate(LocalDateTime.now());
        String userName = findUserName();
        entity.setCreatedBy(userName);
    }

    @PreUpdate
    public void beforeUpdate(EntityBase entity) {
        entity.setLastModifiedDate(LocalDateTime.now());
        String userName = findUserName();
        entity.setLastModifiedBy(userName);
    }

    private static String findUserName() {
        return SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext()
            .getAuthentication() != null && SecurityContextHolder.getContext()
            .getAuthentication()
            .getName() != null ? SecurityContextHolder.getContext().getAuthentication().getName() : "SYSTEM";
    }
}
