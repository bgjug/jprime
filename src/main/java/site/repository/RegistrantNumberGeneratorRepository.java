package site.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import site.model.Registrant;

@NoRepositoryBean
public interface RegistrantNumberGeneratorRepository<T extends Registrant.NumberGenerator> extends
    PagingAndSortingRepository<T, Long> {
    T findFirstByOrderByIdAsc();

    // This should provide reference to the actual repository object. Not to a proxy
    default Object lockObject() {
        return this;
    }
}
