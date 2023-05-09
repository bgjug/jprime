package site.facade;

import javax.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import site.model.Visitor;
import site.model.VisitorJPro;
import site.repository.VisitorJProRepository;

@Service(UserServiceJPro.NAME)
@Transactional
public class UserServiceJPro {
    public static final String NAME = "userJProFacade";

    @Autowired
    @Qualifier(VisitorJProRepository.NAME)
    private VisitorJProRepository visitorJProRepository;

    public void clearVisitors() {
        visitorJProRepository.deleteAll();
    }

    public VisitorJPro save(VisitorJPro visitorJPro) {
        return visitorJProRepository.save(visitorJPro);
    }

    public boolean setPresentByNameIgnoreCaseAndEmailIgnoreCase(Visitor visitorExample){
        List<VisitorJPro> matchedVisitors = visitorJProRepository.findByNameIgnoreCaseAndEmailIgnoreCase(visitorExample.getName(), visitorExample.getEmail());
        if (!matchedVisitors.isEmpty()) {
            matchedVisitors.forEach(v -> v.setPresent(true));
            visitorJProRepository.saveAll(matchedVisitors);
            return true;
        }
        return false;
    }

    public boolean setJProPresentByNameIgnoreCase(Visitor visitor) {
        List<VisitorJPro> matchedVisitors = visitorJProRepository.findByNameIgnoreCase(visitor.getName());

        if (!matchedVisitors.isEmpty()) {
            matchedVisitors.forEach(v -> v.setPresent(true));
            visitorJProRepository.saveAll(matchedVisitors);
            return true;
        }
        return false;
    }

    public List<VisitorJPro> findAllNewestVisitors() {
        return visitorJProRepository.findAllNewestUsers();
    }
}
