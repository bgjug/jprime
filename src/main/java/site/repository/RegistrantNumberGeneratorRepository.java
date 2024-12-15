package site.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import site.model.Registrant;

@NoRepositoryBean
public interface RegistrantNumberGeneratorRepository<T extends Registrant.NumberGenerator> extends
    JpaRepository<T, Long> {
    T findFirstByOrderByIdAsc();

    default Class<?> repositoryClass() {
        return this.getClass();
    }
}
