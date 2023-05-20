package site.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.facade.AdminService;
import site.facade.UserService;

@RestController
@RequestMapping(value = "/api/speaker", produces = MediaType.APPLICATION_JSON_VALUE)
public class SpeakerRestController {

    private final AdminService adminService;

    private final UserService userService;

    @Autowired
    public SpeakerRestController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping(path = "/{branch}")
    public ResponseEntity<?> allSpeakers(@PathVariable(name = "branch") String branch) {
        try {
            return ResponseEntity.ok(userService.findAcceptedSpeakers());
        } catch (Exception e) {
            return ResponseEntity.status(500).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
        }
    }

    @PostMapping(path = "/search/{branch}")
    public ResponseEntity<?> findSpeaker(@PathVariable(name = "branch") String branch,
        @RequestBody SpeakerSearch speakerSearch) {

        try {
            return ResponseEntity.ok(
                adminService.searchSpeaker(branch, speakerSearch.getFirstName(), speakerSearch.getLastName(),
                    speakerSearch.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.status(500).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
        }
    }
}
