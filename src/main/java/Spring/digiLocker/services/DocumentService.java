package Spring.digiLocker.services;

import Spring.digiLocker.dto.*;
import Spring.digiLocker.entity.Document;
import Spring.digiLocker.entity.User;
import Spring.digiLocker.enums.DocumentType;
import Spring.digiLocker.enums.Role;
import Spring.digiLocker.exception.DocumentNotFoundException;
import Spring.digiLocker.exception.ForbiddenException;
import Spring.digiLocker.exception.InvalidOperationException;
import Spring.digiLocker.exception.UserNotFoundException;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
                                () -> new UserNotFoundException(
                                        "User not found"
                                )
                        );
        if(file.isEmpty()) {
            throw new InvalidOperationException(
                    "File cannot be empty"
            );
        }

        if(file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidOperationException(
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
                                () -> new UserNotFoundException(
                                        "User not found"
                                )
                        );

        List<Document> ownedDocuments =
                documentRepository
                        .findByOwner(owner);
        List<Document> sharedDocuments =
                documentRepository
                        .findBySharedUsersContains(owner);
        Set<Document> documents =
                new HashSet<>();

        documents.addAll(
                ownedDocuments
        );

        documents.addAll(
                sharedDocuments
        );
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
                    response.setShared(
                            sharedDocuments.contains(document)
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
        User currentUser =
                userRepository
                        .findById(currentUserId)
                        .orElseThrow(
                                () -> new UserNotFoundException(
                                        "User not found"
                                )
                        );

        Document document =
                documentRepository
                        .findById(documentId)
                        .orElseThrow(
                                () -> new DocumentNotFoundException(
                                        "Document not found"
                                )
                        );
        System.out.println("Current User: " + currentUserId);
        System.out.println("Owner User: " + document.getOwner().getId());

        if(
                document.getOwner()
                        .getId()
                        != currentUserId
                &&
                        !document.getSharedUsers()
                                .contains(currentUser)
        ) {
            throw new ForbiddenException(
                    "Forbidden"
            );
        }

        File file =
                new File(
                        document.getFilePath()
                );

        if(!file.exists()) {
            throw new DocumentNotFoundException(
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
                                () -> new DocumentNotFoundException(
                                        "Document not found"
                                )
                        );
        if (
                document.getOwner()
                        .getId()
                        != currentUserId
        ) {
            throw new ForbiddenException(
                    "Forbidden"
            );
        }
        File file =
                new File(
                        document.getFilePath()
                );
        if (!file.exists()) {
            throw new DocumentNotFoundException(
                    "File not found"
            );
        }
        return deleteDocumentInternal(document);
    }
    public DeleteResponse deleteDocumentByAdmin(
            Long documentId
    ) throws IOException {

        Document document =
                documentRepository
                        .findById(documentId)
                        .orElseThrow(
                                () -> new DocumentNotFoundException(
                                        "Document not found"
                                )
                        );

        return deleteDocumentInternal(
                document
        );
    }
    private DeleteResponse deleteDocumentInternal(
            Document document
    ) throws IOException {

        File file =
                new File(
                        document.getFilePath()
                );

        if (!file.exists()) {
            throw new DocumentNotFoundException(
                    "File not found"
            );
        }

        Files.delete(
                file.toPath()
        );

        documentRepository.delete(document);

        DeleteResponse response =
                new DeleteResponse();

        response.setDocumentId(
                document.getId()
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
                                () -> new DocumentNotFoundException(
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
            throw new ForbiddenException(
                    "Forbidden"
            );
        }
        User targetUser =
                userRepository
                        .findByEmail(
                                request.getEmail()
                        )
                        .orElseThrow(
                                () -> new UserNotFoundException(
                                        "User not found"
                                )
                        );
        if(
                targetUser.getId()
                        == currentUserId
        ){
            throw new InvalidOperationException(
                    "Can't send Urself"
            );
        }
        if(document.getSharedUsers()
                .contains(targetUser)
        )
        {

            throw new InvalidOperationException(

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

