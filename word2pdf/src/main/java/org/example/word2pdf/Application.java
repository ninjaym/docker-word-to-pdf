package org.example.word2pdf;

import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.document.DocumentFormat;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.core.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Controller
    public static class MainController {
        @Autowired
        private DocumentConverter converter;

        @PostMapping("/wordToPdf")
        public ResponseEntity convertWordToPdf(@RequestParam("file") MultipartFile file)  {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                final DocumentFormat targetFormat = DefaultDocumentFormatRegistry.PDF;
                converter.convert(file.getInputStream()).to(baos).as(targetFormat).execute();

                final HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType(targetFormat.getMediaType()));
                headers.add(
                    "Content-Disposition",
                    "attachment; filename="
                        + FileUtils.getBaseName(file.getOriginalFilename())
                        + "."
                        + targetFormat.getExtension());
                return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);

            } catch (OfficeException | IOException e) {
                return ResponseEntity.status(500).build();
            }
        }
    }
}
