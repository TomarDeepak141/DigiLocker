package Spring.digiLocker.controller;

import Spring.digiLocker.dto.AdminDocumentResponse;
import Spring.digiLocker.dto.AdminUserResponse;
import Spring.digiLocker.services.AdminService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@SecurityRequirement(
        name = "Bearer Authentication"
)
public class AdminController {

    private final AdminService adminService;

    public AdminController(
            AdminService adminService
    ) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<
            List<AdminUserResponse>
            > getAllUsers() {

        return ResponseEntity.ok(
                adminService
                        .getAllUsers()
        );
    }
    @GetMapping("/documents")
    public ResponseEntity<
            List<AdminDocumentResponse>
            > getAllDocuments(){
        return ResponseEntity.ok(
                adminService
                        .getAllDocuments()
        );
    }
}
