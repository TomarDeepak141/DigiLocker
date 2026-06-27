package Spring.digiLocker.services;

import Spring.digiLocker.dto.AdminDocumentResponse;
import Spring.digiLocker.dto.AdminUserResponse;
import Spring.digiLocker.dto.DeleteResponse;
import Spring.digiLocker.entity.Document;
import Spring.digiLocker.entity.User;
import Spring.digiLocker.enums.Role;
import Spring.digiLocker.repository.DocumentRepository;
import Spring.digiLocker.repository.UserRepository;
import org.antlr.v4.runtime.tree.xpath.XPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;
    private final DocumentService documentService;

    public AdminService(UserRepository userRepository, DocumentRepository documentRepository, DocumentService documentService) {
        this.userRepository = userRepository;
        this.documentRepository = documentRepository;
        this.documentService = documentService;
    }

    public List<AdminUserResponse>
    getAllUsers(){
        List<User> users =
                userRepository
                        .findAll();
        return users.stream()
                .map(user -> {
                    AdminUserResponse response =
                            new AdminUserResponse();

                    response.setId(
                            user.getId()
                    );

                    response.setName(
                            user.getName()
                    );

                    response.setEmail(
                            user.getEmail()
                    );

                    response.setRole(
                            user.getRole()
                    );

                    response.setActive(
                            user.isActive()
                    );

                    response.setCreatedAt(
                            user.getCreatedAt()
                    );

                    return response;
                }).toList();
    }
    public List<AdminDocumentResponse>
    getAllDocuments(){
        List<Document> documents =
                documentRepository
                        .findAll();
        return documents.stream()
                .map(document ->{
                    AdminDocumentResponse response =
                            new AdminDocumentResponse();

                    response.setId(
                            document.getId()
                    );
                    response.setDocumentType(
                            document.getDocumentType()
                    );
                    response.setFileName(
                            document.getFileName()
                    );
                    response.setFileSize(
                            document.getFileSize()
                    );
                    response.setCreatedAt(
                            document.getCreatedAt()
                    );
                    response.setUpdatedAt(
                            document.getUpdatedAt()
                    );
                    response.setOwnerEmail(
                            document.getOwner()
                                    .getEmail()
                    );
                    response.setOwnerName(
                            document.getOwner()
                                    .getName()
                    );
                    return response;
                } ).toList();
    }
    public DeleteResponse deleteUserByAdmin(
            Long userId
    ) throws IOException {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        Long currentAdminId =
                (Long) authentication.getPrincipal();

        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                )
                        );

        // Admin cannot delete himself
        if (user.getId()==currentAdminId) {
            throw new RuntimeException(
                    "Admin cannot delete himself"
            );
        }

        // Admin cannot delete another admin
        if (user.getRole() == Role.ADMIN) {
            throw new RuntimeException(
                    "Cannot delete another admin"
            );
        }

        // Remove user from shared documents
        List<Document> sharedDocuments =
                documentRepository
                        .findBySharedUsersContains(user);

        for (Document document : sharedDocuments) {
            document.getSharedUsers().remove(user);
            documentRepository.save(document);
        }

        // Delete all owned documents
        List<Document> ownedDocuments =
                documentRepository
                        .findByOwner(user);

        for (Document document : ownedDocuments) {
            documentService.deleteDocumentByAdmin(
                    document.getId()
            );
        }

        userRepository.delete(user);

        DeleteResponse response =
                new DeleteResponse();

        response.setMessage(
                "User deleted successfully"
        );

        return response;
    }
}
