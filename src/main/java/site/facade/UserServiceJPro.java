package site.facade;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.model.Visitor;
import site.model.VisitorJPro;
import site.repository.VisitorJProRepository;

@Service
@Transactional
public class UserServiceJPro {

    @Autowired
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
