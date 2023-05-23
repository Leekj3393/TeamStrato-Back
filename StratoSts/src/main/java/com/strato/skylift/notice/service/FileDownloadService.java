package com.strato.skylift.notice.service;

import com.strato.skylift.entity.NoticeFile;
import com.strato.skylift.notice.repository.NoticeFileRepository;
import org.springframework.core.io.Resource;

import lombok.AllArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@AllArgsConstructor
@Service
public class FileDownloadService {

    private final NoticeFileRepository noticeFileRepository;

//    public NoticeFile getFile(Long fileId) {
//        return noticeFileRepository.findById(fileId).orElseThrow(() -> new IllegalArgumentException("파일이 존재하지 않습니다."));
//    }

    //
    public List<NoticeFile> getFilesByNoticeCode(Long noticeCode) {
        return noticeFileRepository.findByNoticeCode(noticeCode);
    }


    public Resource loadFileAsResource(String fileName) {
        NoticeFile noticeFile = noticeFileRepository.findByFileName(fileName);
        Path filePath = Paths.get(noticeFile.getFilePath()).toAbsolutePath().normalize();
        Resource resource = null;

        try {
            resource = new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return resource;
    }

    }
