package site.api;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.facade.AdminService;
import site.model.Visitor;

@RestController
@RequestMapping(value = "/api/visitor", produces = MediaType.APPLICATION_JSON_VALUE)
public class VisitorsRestController {

    private static final Logger log = LogManager.getLogger(VisitorsRestController.class);

    private final AdminService adminFacade;

    public VisitorsRestController(@Qualifier(AdminService.NAME) AdminService adminFacade) {
        this.adminFacade = adminFacade;
    }

    @GetMapping(path = "{branch}")
    public ResponseEntity<?> allByBranch(@PathVariable String branch) {
        try {
            log.debug("Requesting all visitors for branch {}", branch);
            return ResponseEntity.ok(StreamSupport.stream(adminFacade.findAllWithTicket().spliterator(), false)
                .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.status(500).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
        }
    }

    @GetMapping(path = "{branch}/{ticket}")
    public ResponseEntity<?> findVisitorByTicket(@PathVariable String branch,
        @PathVariable String ticket) {
        try {
            Optional<Visitor> visitor = adminFacade.findVisitorByTicket(ticket);
            return visitor.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(500).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
        }
    }

    @PostMapping(path = "search/{branch}")
    public ResponseEntity<?> findVisitor(@PathVariable String branch,
        @RequestBody VisitorSearch visitorSearch) {
        try {
            List<Visitor> visitorList =
                adminFacade.searchVisitor(branch, visitorSearch.getEmail(), visitorSearch.getFirstName(),
                    visitorSearch.getLastName(), visitorSearch.getCompany());
            return ResponseEntity.ok(visitorList);
        } catch (Exception e) {
            return ResponseEntity.status(500).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
        }
    }

}
