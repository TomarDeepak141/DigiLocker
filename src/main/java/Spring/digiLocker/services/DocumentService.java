package Spring.digiLocker.services;

import Spring.digiLocker.dto.*;
import Spring.digiLocker.entity.Document;
import Spring.digiLocker.entity.User;
import Spring.digiLocker.enums.DocumentType;
import Spring.digiLocker.repository.DocumentRepository;
import Spring.digiLocker.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;


    private static final String UPLOAD_DIR =
            "uploads/";

    private static final long MAX_FILE_SIZE =
            10 * 1024 * 1024;

    public DocumentService(
            DocumentRepository documentRepository,
            UserRepository userRepository
    ) {
        this.documentRepository =
                documentRepository;
        this.userRepository =
                userRepository;
    }

    public UploadResponse upload(
            MultipartFile file,
            DocumentType documentType
    ) throws IOException {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        Long currentUserId =
                (Long) authentication
                        .getPrincipal();

        User owner =
                userRepository
                        .findById(currentUserId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                )
                        );
        if(file.isEmpty()) {
            throw new RuntimeException(
                    "File cannot be empty"
            );
        }

        if(file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException(
                    "File too large"
            );
        }
        String uniqueFileName =
                UUID.randomUUID()
                        .toString()
                        + "_"
                        + file.getOriginalFilename();


        File uploadDir =
                new File(UPLOAD_DIR);
        String filePath =
                uploadDir.getAbsolutePath()
                        + File.separator
                        + uniqueFileName;

        if(!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        System.out.println(
                uploadDir.exists()
        );
        file.transferTo(
                new File(filePath)
        );
        System.out.println(
                uploadDir.getAbsolutePath()
        );
        Document document =
                new Document();

        document.setOwner(owner);

        document.setFileName(
                file.getOriginalFilename()
        );

        document.setDocumentType(
                documentType
        );

        document.setFilePath(
                filePath
        );

        document.setFileSize(
                file.getSize()
        );

        LocalDateTime now =
                LocalDateTime.now();

        document.setCreatedAt(now);
        document.setUpdatedAt(now);

        Document savedDocument =
                documentRepository
                        .save(document);
        UploadResponse response =
                new UploadResponse();

        response.setDocumentId(
                savedDocument.getId()
        );

        response.setFileName(
                file.getOriginalFilename()
        );

        response.setMessage(
                "Upload Successful"
        );

        return response;
    }

    public List<DocumentResponse> getMyDocuments() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        Long currentUserId =
                (Long) authentication
                        .getPrincipal();

        User owner =
                userRepository
                        .findById(currentUserId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                )
                        );

        List<Document> documents =
                documentRepository
                        .findByOwner(owner);
        return documents.stream()
                .map(document -> {

                    DocumentResponse response =
                            new DocumentResponse();

                    response.setId(
                            document.getId()
                    );

                    response.setFileName(
                            document.getFileName()
                    );

                    response.setDocumentType(
                            document.getDocumentType()
                    );

                    return response;
                })
                .toList();

    }
    public  byte[] downloadDocument(
            Long documentId
    ) throws IOException {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        Long currentUserId =
                (Long) authentication
                        .getPrincipal();

        Document document =
                documentRepository
                        .findById(documentId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Document not found"
                                )
                        );
        System.out.println("Current User: " + currentUserId);
        System.out.println("Owner User: " + document.getOwner().getId());

        if(
                document.getOwner()
                        .getId()
                        != currentUserId
        ) {
            throw new RuntimeException(
                    "Forbidden"
            );
        }

        File file =
                new File(
                        document.getFilePath()
                );

        if(!file.exists()) {
            throw new RuntimeException(
                    "File not found"
            );
        }

        return Files.readAllBytes(
                file.toPath()
        );
    }

    public DeleteResponse deleteDocument(
            Long documentId
    ) throws IOException {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        Long currentUserId =
                (Long) authentication
                        .getPrincipal();
        Document document =
                documentRepository
                        .findById(documentId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Document not found"
                                )
                        );
        if (
                document.getOwner()
                        .getId()
                        != currentUserId
        ) {
            throw new RuntimeException(
                    "Forbidden"
            );
        }
        File file =
                new File(
                        document.getFilePath()
                );
        if (!file.exists()) {
            throw new RuntimeException(
                    "File not found"
            );
        }
        Files.delete(
                file.toPath()
        );
        documentRepository
                .delete(document);
        DeleteResponse response =
                new DeleteResponse();

        response.setDocumentId(
                documentId
        );

        response.setMessage(
                "Document deleted successfully"
        );

        return response;
    }
    public ShareDocumentResponse
    shareDocument(
            Long documentId,
            ShareDocumentRequest request
    ){
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        Long currentUserId =
                (Long) authentication
                        .getPrincipal();
        Document document =
                documentRepository
                        .findById(documentId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Document not found"
                                )
                        );
        System.out.println(
                "Current User: "
                        + currentUserId
        );

        System.out.println(
                "Owner: "
                        + document.getOwner()
                        .getId()
        );
        if (
                document.getOwner()
                        .getId()
                        != currentUserId
        ) {
            throw new RuntimeException(
                    "Forbidden"
            );
        }
        User targetUser =
                userRepository
                        .findByEmail(
                                request.getEmail()
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                )
                        );
        if(
                targetUser.getId()
                        == currentUserId
        ){
            throw new RuntimeException(
                    "Can't send Urself"
            );
        }
        if(document.getSharedUsers()
                .contains(targetUser)
        )
        {

            throw new RuntimeException(

                    "Already Shared"

            );

        }
        document.getSharedUsers()
                .add(targetUser);

        documentRepository
                .save(document);
        ShareDocumentResponse response =
                new ShareDocumentResponse();

        response.setMessage(
                "Document shared successfully"
        );

        return response;
    }
}

