package com.stackfortech.fileUpload.controller;

import com.stackfortech.fileUpload.model.DBFileEntity;
import com.stackfortech.fileUpload.response.UploadResponse;
import com.stackfortech.fileUpload.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/upload/local")
    public String uploadFile(@RequestParam("file") MultipartFile file)
    {
        boolean status = fileUploadService.uploadFileToLocalStorage(file);
        return status == true ? "File Upload successful" : "Oops ! Please re-upload";
    }

    @PostMapping("/upload/db")
    public UploadResponse uploadFileToDb(@RequestParam("file") MultipartFile file)
    {
        DBFileEntity dbFileEntity = fileUploadService.saveFileToDb(file);
        UploadResponse response = new UploadResponse();
        if(dbFileEntity !=null)
        {
           String downloadUri =  ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("api/v1/download/")
                    .path(dbFileEntity.getId())
                    .toUriString();
           response.setDownloadUri(downloadUri);
           response.setFileId(dbFileEntity.getId());
           response.setFileName(dbFileEntity.getFileName());
           response.setUpload(true);
           response.setMessage("Upload Successful!");
           return response;
        }

        response.setMessage("Upload Failed ! Please re-upload.");
        return response;

    }
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String id)
    {
        DBFileEntity dbFileEntity =  fileUploadService.findOne(id);
        if(dbFileEntity !=null){
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(dbFileEntity.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename= "+dbFileEntity.getFileName())
                    .body(new ByteArrayResource(dbFileEntity.getData()));
        }

        return ResponseEntity.noContent()
                .build();

    }
}
