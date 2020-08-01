package com.stackfortech.fileUpload.service;

import com.stackfortech.fileUpload.model.DBFileEntity;
import com.stackfortech.fileUpload.repository.DbFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadService {

    private String UploadFolder = "/Users/deomrinal/Desktop/FILES";
    @Autowired
    private DbFileRepository dbFileRepository;

    public boolean uploadFileToLocalStorage(MultipartFile file){
        try
        {
            if(file.isEmpty())
                return false;
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UploadFolder + file.getOriginalFilename());
            Files.write(path, bytes);
            return true;
        }
        catch (IOException e)
        {
            System.out.println(e.getLocalizedMessage());
            return false;
        }

    }

    public DBFileEntity saveFileToDb(MultipartFile file)
    {
        try {
            if(file.isEmpty())
                return null;
            byte[] bytes = file.getBytes();
            DBFileEntity dbFileEntity = new DBFileEntity();
            dbFileEntity.setData(bytes);
            dbFileEntity.setContentType(file.getContentType());
            dbFileEntity.setFileName(StringUtils.cleanPath(file.getOriginalFilename()));
            DBFileEntity dbFileEntityToRet = dbFileRepository.save(dbFileEntity);
            return dbFileEntityToRet;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public DBFileEntity findOne(String id)
    {
        return dbFileRepository.getOne(id);
    }

}
