package org.example.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.dto.FileDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class File {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String title;
    private LocalDateTime creationDate;
    private String description;
    private byte[] content;


    public File(FileDto fileDto) {
        this.id = UUID.randomUUID();
        this.title = fileDto.getTitle();
        this.creationDate = LocalDateTime.parse(fileDto.getCreationDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.description = fileDto.getDescription();
        this.content = Base64.getDecoder().decode(fileDto.getContent());
    }
}
