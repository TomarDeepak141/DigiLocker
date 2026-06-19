package Spring.digiLocker.controller;

import Spring.digiLocker.dto.DocumentResponse;
import Spring.digiLocker.dto.UploadResponse;
import Spring.digiLocker.enums.DocumentType;
import Spring.digiLocker.services.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(
            DocumentService documentService
    ) {
        this.documentService =
                documentService;
    }
    @PostMapping("/documents")
    public ResponseEntity<UploadResponse> upload(
            @RequestParam MultipartFile file,
            @RequestParam DocumentType documentType
    ) throws IOException {

        UploadResponse response =
                documentService.upload(
                        file,
                        documentType
                );

        return ResponseEntity.ok(
                response
        );
    }
    @GetMapping("/documents")
    public ResponseEntity<List<DocumentResponse>>
    getMyDocuments() {

        return ResponseEntity.ok(
                documentService.getMyDocuments()
        );
    }
}
