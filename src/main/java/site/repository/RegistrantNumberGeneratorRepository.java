package site.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import site.model.Registrant;

@NoRepositoryBean
public interface RegistrantNumberGeneratorRepository<T extends Registrant.NumberGenerator> extends
    PagingAndSortingRepository<T, Long> {
    T findFirstByOrderByIdAsc();

    default Class<?> repositoryClass() {
        return this.getClass();
    }
}
