package org.example.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.domain.entity.File;
import org.example.validation.Base64File;

import java.util.Base64;

@Data
@AllArgsConstructor
public class FileDto {
    @NotBlank(message = "Заполните заголовок")
    private String title;

    @NotNull(message = "Заполните дату")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$", message = "Дата должна быть в формате yyyy-MM-dd'T'HH:mm:ss")
    private String creationDate;

    @NotBlank(message = "Заполните описание")
    private String description;

    @NotNull(message = "Добавьте файл в формате base64")
    @Base64File(message = "Файл должен быть в формате base64")
    private String content;

    public FileDto(File file) {
        this.title = file.getTitle();
        this.creationDate = file.getCreationDate().toString();
        this.description = file.getDescription();
        this.content = Base64.getEncoder().encodeToString(file.getContent());
    }
}