package com.strato.skylift.education.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.strato.skylift.education.dto.ClassDto;
import com.strato.skylift.education.dto.EdFileDto;
import com.strato.skylift.education.dto.EducationDto;
import com.strato.skylift.education.entity.EdClass;
import com.strato.skylift.education.repository.ClassRepository;
import com.strato.skylift.education.repository.EducationFileRepository;
import com.strato.skylift.education.repository.EducationRepository;
import com.strato.skylift.entity.EdFile;
import com.strato.skylift.entity.Education;
import com.strato.skylift.member.dto.MbMemberDto;
import com.strato.skylift.util.FileUploadUtils;

//import net.bramp.ffmpeg.FFprobe;
//import net.bramp.ffmpeg.probe.FFmpegFormat;





@Service
public class EducationService {

	private final EducationRepository edRepository;
	private final EducationFileRepository edFileRepository;
	private final ClassRepository classRepository;
	private final ModelMapper modelMapper;

	public EducationService(EducationRepository edRepository, ModelMapper modelMapper,
			EducationFileRepository edFileRepository,ClassRepository classRepository
	) {
		this.edRepository = edRepository;
		this.edFileRepository = edFileRepository;
		this.classRepository = classRepository;
		this.modelMapper = modelMapper;
	}

//   @Value("${video.video-url}")
//   private String VIDEO_URL;
//
//   @Value("${video.video-dir}")
//   private String VIDEO_DIR;

	/* 교육 전체 조회 */
	public Page<EducationDto> selectEducationList(int page) {

		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("edCode").ascending());

		Page<Education> educationList = edRepository.findAll(pageable);
		Page<EducationDto> educationDtoList = educationList.map(edu -> modelMapper.map(edu, EducationDto.class));

		return educationDtoList;
	}

	/* 교육 등록 */
	@Transactional
	public void insertMember(EducationDto educationDto) {

		String videoName = UUID.randomUUID().toString().replace("-", "");

		EdFileDto fileDto = new EdFileDto();

//      try {
//         String replaceFilename = FileUploadUtils.saveFile(VIDEO_DIR, videoName, educationDto.getEducationVideos());
//
//         fileDto.setFileName(videoName);
//         fileDto.setFilePath(replaceFilename);
//         fileDto.setFileType("교육영상");
//
//         /* 영상시간 추출해서 저장 */
//         Long videoDuration = getVideoDuration(educationDto.getEducationVideos());
//         educationDto.setEdTime(videoDuration);
//
//         Education newEdu = edRepository.save(modelMapper.map(educationDto, Education.class));
//
//         fileDto.setEdCode(newEdu.getEdCode());
//
//         System.out.println("fileDto :" + fileDto);
//
//         edFileRepository.save(modelMapper.map(fileDto, EdFile.class));
//
//      } catch (IOException e) {
//         e.printStackTrace();
//      }
//
	}
	
	/* 교육 안전리스트 조회 */
	public Page<EducationDto> selectEducationSafetyList(int page) {
		
		Pageable pageable = PageRequest.of(page - 1, 6, Sort.by("edCode").ascending());
		
		Page<Education> educationList = edRepository.findByEdTypeSafety(pageable);
		Page<EducationDto> educationDtoList = educationList.map(edu -> modelMapper.map(edu, EducationDto.class));
		
		return educationDtoList;
	}
	
	/* 교육 직무리스트 조회 */
	public Page<EducationDto> selectEducationDutyList(int page) {
		
		Pageable pageable = PageRequest.of(page - 1, 6, Sort.by("edCode").ascending());
		
		Page<Education> educationList = edRepository.findByEdTypeDuty(pageable);
		Page<EducationDto> educationDtoList = educationList.map(edu -> modelMapper.map(edu, EducationDto.class));
		
		return educationDtoList;
	}
	
	/* 교육 기타리스트 조회 */
	public Page<EducationDto> selectEducationOtherList(int page) {
		
		Pageable pageable = PageRequest.of(page - 1, 6, Sort.by("edCode").ascending());
		
		Page<Education> educationList = edRepository.findByEdTypeOther(pageable);
		Page<EducationDto> educationDtoList = educationList.map(edu -> modelMapper.map(edu, EducationDto.class));
		
		return educationDtoList;
	}
	
	/* 교육 상세조회 */
	public EducationDto selectEducationDetail(Long edCode) {
		
		Education education = edRepository.findById(edCode)
				.orElseThrow(() -> new IllegalArgumentException("해당 코드의 교육이 없습니다. edCode :" + edCode));
		
		EducationDto eduDto = modelMapper.map(education, EducationDto.class);
		
		return eduDto;
	}
	
	/* 교육 영상조회 */
	public EdFileDto selectEducationVideo(Long edCode) {
		
		EdFile file = edFileRepository.findByEdCode(edCode);
		
		System.out.println("edCode : " + edCode);
		
		EdFileDto fileDto = modelMapper.map(file, EdFileDto.class);
		
		return fileDto;
	}
	
	/* 교육 등록 */
	@Transactional
	public void insertClass(MbMemberDto memberDto, Long edCode) {
		
		ClassDto classDto = new ClassDto();
		EducationDto eduDto = new EducationDto();
		eduDto.setEdCode(edCode);
		classDto.setMember(memberDto);
		classDto.setEducation(eduDto);
		classDto.setClassStatus("N");
		
		classRepository.save(modelMapper.map(classDto, EdClass.class));
	}
	


	/* 원본 영상 시간 추출 메소드 */
//   private Long getVideoDuration(MultipartFile videoFile) throws IOException {
//       // Save the video file to a temporary location
//       Path tempFilePath = Files.createTempFile("temp", videoFile.getOriginalFilename());
//       try {
//           try (InputStream inputStream = videoFile.getInputStream();
//                OutputStream outputStream = Files.newOutputStream(tempFilePath)) {
//               IOUtils.copy(inputStream, outputStream);
//           }
//
//           FFprobe ffprobe = new FFprobe();
//           FFmpegFormat format = ffprobe.probe(tempFilePath.toString()).format;
//           double durationSeconds = format.duration;
//
//           // Convert duration to milliseconds
////           long durationInSeconds = Math.round(durationSeconds);
//
////           return durationInSeconds;
//           long durationMillis = (long) (durationSeconds * 1000);
//
//           return durationMillis;
//       } finally {
//           // Delete the temporary file
//           deleteFile(tempFilePath);
//       }
//   }
//
//   /* 임시 파일 삭제 메소드 */
//   private void deleteFile(Path filePath) {
//       try {
//           Files.deleteIfExists(filePath);
//       } catch (IOException e) {
//           // Handle exception or log error message
//           e.printStackTrace();
//       }
//   }
//
}
