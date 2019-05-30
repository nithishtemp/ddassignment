package com.dd.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.dd.dto.ActivityDto;
import com.dd.entity.Activity;
import com.dd.repository.ActivityRepository;

import javassist.NotFoundException;

@Component
public class ActivitiesOrganizeService implements ICommonService<ActivityDto, Activity> {

	@Resource
	private ActivityRepository repository;

	@Override
	public ActivityDto create(ActivityDto dto, String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ActivityDto> create(List<ActivityDto> dtoList, String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActivityDto update(ActivityDto dto, String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(ActivityDto dto) {
		// TODO Auto-generated method stub

	}

	@Override
	public ActivityDto read(Long id) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ActivityDto> readAll() throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ActivityDto> organizeActivity(MultipartFile multipartFile) throws Exception {
		File file = null;
		try {
			file = convertMultiPartToFile(multipartFile);
			
		} catch (Exception ex) {
			throw ex;
		}
		return null;
	}
	
	private List<ActivityDto> convertFileToObject(File file) throws FileNotFoundException{
		List<ActivityDto> activities = new ArrayList<>();
		Scanner scanner = new Scanner(file);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line != null && line.length() > 0) {
				activities.add(generateActivity(line));
			}
		}
		scanner.close();
		return activities;
	}

	private ActivityDto generateActivity(String line) {
		int minute = 0;
		int index = 0;
		Matcher m = Pattern.compile("\\d+").matcher(line);		
		ActivityDto dto = new ActivityDto();
		if (m.find()) {
			minute = Integer.valueOf(m.group());
			dto.setMinute(minute);
			index = line.indexOf(minute + "");
		} else {
			dto.setMinute(minute);
			index = line.toLowerCase().indexOf("sprint");
		}
		dto.setEvent(line.substring(0, index));
		dto.setRange(line.substring(index, line.length()));
		return dto;
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}
}
