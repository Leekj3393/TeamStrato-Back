//package com.strato.skylift.education.config;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import net.bramp.ffmpeg.FFmpeg;
//import net.bramp.ffmpeg.FFprobe;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.DefaultResourceLoader;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.ResourceLoader;
//
//import java.io.IOException;
//
//@Slf4j
//@Configuration
//@RequiredArgsConstructor
//public class FFprobeConfig {

//	@Value("${ffmpeg.mpeg}")
//    private String ffmpegLocation;
//
//	@Value("${ffmpeg.probe}")
//    private String ffprobeLocation;
//
//    @Bean(name = "ffmpeg")
//    public FFmpeg ffmpeg() throws IOException{
////        ResourceLoader resourceLoader = new DefaultResourceLoader();
////        Resource resource = resourceLoader.getResource(ffmpegLocation);
////        return new FFmpeg(resource.getURL().getPath());
//    	 return new FFmpeg(ffmpegLocation);
//    }
//
//    @Bean( name = "ffprobe")
//    public FFprobe ffprobe() throws IOException{
////        ResourceLoader resourceLoader = new DefaultResourceLoader();
////        Resource resource = resourceLoader.getResource(ffprobeLocation);
//        return new FFprobe(ffprobeLocation);
//    }
//

//}

//}

