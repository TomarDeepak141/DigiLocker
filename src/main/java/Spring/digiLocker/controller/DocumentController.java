package Spring.digiLocker.controller;

import Spring.digiLocker.dto.*;
import Spring.digiLocker.enums.DocumentType;
import Spring.digiLocker.services.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/documents/{id}")
    public ResponseEntity<byte[]> download(
            @PathVariable Long id
    ) throws IOException {

        byte[] fileBytes =
                documentService
                        .downloadDocument(id);

        return ResponseEntity
                .ok()
                .body(fileBytes);
    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity<DeleteResponse> delete(
            @PathVariable Long id
    ) throws IOException {

        return ResponseEntity.ok(
                documentService.deleteDocument(id)
        );
    }
    @PostMapping(
            "/documents/{id}/share"
    )
    public ResponseEntity<
            ShareDocumentResponse
            > shareDocument(
            @PathVariable Long id,
            @RequestBody
            ShareDocumentRequest request
    ) {

        return ResponseEntity.ok(
                documentService
                        .shareDocument(
                                id,
                                request
                        )
        );
    }

}
