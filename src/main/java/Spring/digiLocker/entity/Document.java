package Spring.digiLocker.entity;

import jakarta.persistence.*;
import Spring.digiLocker.enums.DocumentType;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    private String fileName;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    private String filePath;

    private Long fileSize;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "document_shares",
            joinColumns =
            @JoinColumn(
                    name = "document_id"
            ),
            inverseJoinColumns =
            @JoinColumn(
                    name = "user_id"
            )
    )
    private Set<User> sharedUsers =
            new HashSet<>();
    public Set<User> getSharedUsers() {
        return sharedUsers;
    }

    public void setSharedUsers(
            Set<User> sharedUsers
    ) {
        this.sharedUsers =
                sharedUsers;
    }
}
