package com.strato.skylift.notice.controller;

import com.strato.skylift.notice.service.FileDownloadService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.strato.skylift.entity.NoticeFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/skylift/myPage")
public class FileDownloadController {

    private final FileDownloadService fileDownloadService;

//    @GetMapping("/notice/download/{fileId}")
//    public ResponseEntity<FileSystemResource> downloadFile(@PathVariable Long fileId) {
//        NoticeFile file = fileDownloadService.getFile(fileId);
//        String filePath = file.getFilePath() + file.getFileName();
//
//        FileSystemResource resource = new FileSystemResource(filePath);
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(file.getFileType()))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
//                .body(resource);
//    }


    @GetMapping("/notice/files/{noticeCode}")
    public ResponseEntity<List<NoticeFile>> getFilesByNoticeCode(@PathVariable Long noticeCode) {
        List<NoticeFile> files = fileDownloadService.getFilesByNoticeCode(noticeCode);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileDownloadService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
