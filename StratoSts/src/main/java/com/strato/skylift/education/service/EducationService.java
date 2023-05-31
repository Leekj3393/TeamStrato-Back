package com.strato.skylift.education.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
import com.strato.skylift.entity.MbFile;
import com.strato.skylift.entity.Member;
import com.strato.skylift.member.dto.MbFileDto;
import com.strato.skylift.member.dto.MbMemberDto;
import com.strato.skylift.member.repository.MbFileRepository;
import com.strato.skylift.member.repository.MemberRepository;
import com.strato.skylift.member.util.MbFileUploadUtils;
import com.strato.skylift.util.FileUploadUtils;

import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegFormat;




@Slf4j
@Service
public class EducationService {

	private final EducationRepository edRepository;
	private final EducationFileRepository edFileRepository;
	private final ClassRepository classRepository;
	private final MemberRepository memberRepository;
	private final MbFileRepository mbFileRepository;
	private final ModelMapper modelMapper;

	public EducationService(EducationRepository edRepository, ModelMapper modelMapper,
			EducationFileRepository edFileRepository,ClassRepository classRepository,
			MemberRepository memberRepository, MbFileRepository mbFileRepository
	) {
		this.edRepository = edRepository;
		this.edFileRepository = edFileRepository;
		this.classRepository = classRepository;
		this.modelMapper = modelMapper;
		this.memberRepository = memberRepository;
		this.mbFileRepository = mbFileRepository;
	}   

   @Value("${video.video-url}")
   private String VIDEO_URL;

   @Value("${video.video-dir}")
   private String VIDEO_DIR;
   
   @Value("${image.image-url}")
	private String IMAGE_URL;
	
	@Value("${image.image-dir}")
	private String IMAGE_DIR;

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

      try {
         String replaceFilename = FileUploadUtils.saveFile(VIDEO_DIR, videoName, educationDto.getEducationVideos());

         fileDto.setFileName(videoName);
         fileDto.setFilePath(replaceFilename);
         fileDto.setFileType("교육영상");

         /* 영상시간 추출해서 저장 */
         Long videoDuration = getVideoDuration(educationDto.getEducationVideos());
         educationDto.setEdTime(videoDuration);

         Education newEdu = edRepository.save(modelMapper.map(educationDto, Education.class));

         fileDto.setEdCode(newEdu.getEdCode());

         System.out.println("fileDto :" + fileDto);

         edFileRepository.save(modelMapper.map(fileDto, EdFile.class));

      } catch (IOException e) {
         e.printStackTrace();
      }

	}
	
	/* 원본 영상 시간 추출 메소드 */
	   	private Long getVideoDuration(MultipartFile videoFile) throws IOException {
	       // Save the video file to a temporary location
	       Path tempFilePath = Files.createTempFile("temp", videoFile.getOriginalFilename());
	       try {
	           try (InputStream inputStream = videoFile.getInputStream();
	                OutputStream outputStream = Files.newOutputStream(tempFilePath)) {
	               IOUtils.copy(inputStream, outputStream);
	           }

	           FFprobe ffprobe = new FFprobe();
	           FFmpegFormat format = ffprobe.probe(tempFilePath.toString()).format;
	           double durationSeconds = format.duration;

	           // Convert duration to milliseconds
//	           long durationInSeconds = Math.round(durationSeconds);

//	           return durationInSeconds;
	           long durationMillis = (long) (durationSeconds * 1000);

	           return durationMillis;
	       } finally {
	           // Delete the temporary file
	           deleteFile(tempFilePath);
	       }
	   }

	   /* 임시 파일 삭제 메소드 */
	   private void deleteFile(Path filePath) {
	       try {
	           Files.deleteIfExists(filePath);
	      } catch (IOException e) {
	          // Handle exception or log error message
	           e.printStackTrace();
	       }
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
	
	/* 수강 등록 */
	@Transactional
	public void insertClass(MbMemberDto memberDto, Long edCode) {
		
		Long memberCode = memberDto.getMemberCode();
		
		EdClass classEntity = classRepository.findByMemberAndEducation(memberCode, edCode);
		System.out.println("classEntity : " + classEntity);
				
		if(classEntity == null) {
		
		ClassDto classDto = new ClassDto();
		EducationDto eduDto = new EducationDto();
		eduDto.setEdCode(edCode);
		classDto.setMember(memberDto);
		classDto.setEducation(eduDto);
		classDto.setClassStatus("N");
		
		log.info("classDto : {}", classDto);
		
		classRepository.save(modelMapper.map(classDto, EdClass.class));
		}
	}
	
	/* 수강 업데이트 */
	@Transactional
	public void updateClass(MbMemberDto memberDto, Long edCode, Long classTime) {
		
		Long memberCode = memberDto.getMemberCode();
		
		EducationDto originEducation = modelMapper.map(edRepository.findByEdCode(edCode),EducationDto.class);
		
		System.out.println("originEducation : " + originEducation);
		
		EdClass classInfo = classRepository.findByMemberAndEducation(memberCode, edCode);	
		ClassDto classDto = modelMapper.map(classInfo, ClassDto.class);
		classDto.setClassTime(classTime);
		classDto.setClassEnd(new Date());
		classDto.setClassView("Y");
		
		Long percent = (long) Math.floor(((double) classTime / originEducation.getEdTime()) * 100);
		
		classDto.setClassPercent(percent);
		
		System.out.println("classTime : " + classTime);
		System.out.println("originTime : " + originEducation.getEdTime());
		System.out.println("percent : " + percent);
		
		if(percent > 90) {
			classDto.setClassStatus("Y");
		} 
		
		classInfo.update(
				classDto.getClassTime(),
				classDto.getClassEnd(),
				classDto.getClassStatus(),
				classDto.getClassView(),
				classDto.getClassPercent()
				);
	}

	/* 수강한 수강 정보 조회 */
	public List<ClassDto> selectClassViewList(MbMemberDto memberDto) {
		
		List<EdClass> classViewList = classRepository.findByMemberCodeList(memberDto.getMemberCode());
		
		return classViewList.stream().map(edClass -> modelMapper.map(edClass, ClassDto.class))
				.collect(Collectors.toList());
	}
	
	/* 수강한 수강 정보 조회 */
	public ClassDto selectClassView(MbMemberDto memberDto, Long edCode) {
		
		EdClass classView = classRepository.findByMemberCode(memberDto.getMemberCode(), edCode);
		
		return modelMapper.map(classView, ClassDto.class);
	}
	
	/* 수강한 수강교육목록 조회 */
	public Page<ClassDto> selectClassList(int page, Long memberCode) {
		
		Pageable pageable = PageRequest.of(page -1, 5, Sort.by("classCode").descending());
		
		Member findMember = memberRepository.findById(memberCode)
				.orElseThrow(() -> new IllegalArgumentException("해당 직원이 없습니다." + memberCode));
		
		Page<EdClass> classList = classRepository.findByMember(pageable, findMember);
		Page<ClassDto> classDtoList = classList.map(data -> modelMapper.map(data, ClassDto.class));
		
		return classDtoList;
	}
	
	/* 교육 사진 등록 */
	public void insertEudcationPhoto(MbMemberDto memberDto, MbFileDto fileDto) {
		
		System.out.println("fileDto : " + fileDto.getEducationImage());
		System.out.println("fileDto : " + fileDto.getFileTitle());
		
		String imageName = fileDto.getFileTitle() + "^" + UUID.randomUUID().toString().replace("-", "");
		
//		MbFileDto fileDto = new MbFileDto();
		
		try {
			
			String replaceFilename = MbFileUploadUtils.saveFile(IMAGE_DIR + "/education", imageName, fileDto.getEducationImage());
			
			fileDto.setFileName(imageName);
			fileDto.setFilePath(replaceFilename);
			fileDto.setFileType("교육사진");
			fileDto.setMemberCode(memberDto.getMemberCode());
			
			mbFileRepository.save(modelMapper.map(fileDto, MbFile.class));
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/* 교육 사진 조회 */
	public Page<MbFileDto> selectEduFileList(int page) {
		
		Pageable pageable = PageRequest.of(page - 1, 6, Sort.by("fileCode").descending());
		
		Page<MbFile> fileList = mbFileRepository.findByFileType(pageable);
		Page<MbFileDto> fileDtoList = fileList.map(photo -> modelMapper.map(photo, MbFileDto.class));
		
		return fileDtoList;
	}

	
	
}
