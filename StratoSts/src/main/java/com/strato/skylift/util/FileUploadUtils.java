package com.strato.skylift.util;

import com.strato.skylift.entity.File;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
public class FileUploadUtils
{
    public static String saveFile(String uploadDir , String fileName, MultipartFile multipartFile) throws IOException
    {
        Path uploadPath = Paths.get(uploadDir);

        if(!Files.exists((uploadPath)))
            Files.createDirectories(uploadPath);
        String replaceFileName = fileName + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());

        try(InputStream in = multipartFile.getInputStream())
        {
            Path filePath = uploadPath.resolve(replaceFileName);
            Files.copy(in, filePath , StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) { throw new IOException("파일을 저장하지 못했음 : " + fileName); }

        return replaceFileName;
    }

    public static void deleteFile(String uploadDir , String fileName) throws IOException
    {
        Path uploadPath = Paths.get(uploadDir);
        Path filePath = uploadPath.resolve(fileName);
        log.info("filePath : " + filePath);
        try { Files.delete(filePath);}
        catch (IOException e) {throw new IOException("파일 못했다 삭제 너 : " + fileName);}
    }
}
