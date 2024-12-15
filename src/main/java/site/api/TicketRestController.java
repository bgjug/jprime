package site.api;

import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import site.facade.AdminService;
import site.model.Visitor;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(value = "/api/ticket", produces = MediaType.APPLICATION_JSON_VALUE)
public class TicketRestController {

    private final AdminService adminFacade;

    public TicketRestController(AdminService adminFacade) {
        this.adminFacade = adminFacade;
    }

    @PostMapping(path = "{ticket}")
    public ResponseEntity<?> registerTicket(@PathVariable String ticket) {
        try {
            Optional<Visitor> visitor = adminFacade.findVisitorByTicket(ticket);

            return visitor.map(this::registerVisitor).orElse(notFound().build());
        } catch (Exception e) {
            return status(500).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
        }
    }

    private ResponseEntity<?> registerVisitor(Visitor visitor) {
        visitor.setRegistered(true);
        adminFacade.saveVisitor(visitor);

        return ok().build();
    }
}
