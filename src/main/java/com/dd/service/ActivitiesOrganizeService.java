package com.dd.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.dd.dto.ActivityDto;
import com.dd.dto.TimeDto;
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
			List<ActivityDto> activities = this.convertFileToObject(file);
			long totalMinutes = this.calculateTotalMinutes(activities);
			Map<Integer, List<ActivityDto>> result = this.segregateActivitiesIntoTeams(activities, totalMinutes);
		} catch (Exception ex) {
			throw ex;
		}
		return null;
	}
	
	public static void main(String args[]) throws Exception {
		ActivitiesOrganizeService serv = new ActivitiesOrganizeService();
		File file = new File("C:\\zendesk\\assignment.txt");
		List<ActivityDto> activities = serv.convertFileToObject(file);
		long totalMinutes = serv.calculateTotalMinutes(activities);
		Map<Integer, List<ActivityDto>> result = serv.segregateActivitiesIntoTeams(activities, totalMinutes);
		for(Map.Entry<Integer, List<ActivityDto>> entry : result.entrySet()) {
			System.out.println("Team: " + entry.getKey());
			for(ActivityDto dt : entry.getValue()) {
				System.out.println(dt.getTime() + dt.getEvent()+dt.getRange());
			}
		}
	}
	
	private Map<Integer, List<ActivityDto>> segregateActivitiesIntoTeams(List<ActivityDto> activities, long totalMinutes) throws Exception{
		Map<Integer, List<ActivityDto>> teamActivityMap = new HashMap<>();
		int type1Minutes = 360;
		int type2Minutes = 420;
		double type1 = (double)totalMinutes/type1Minutes;
		double type2 = (double)totalMinutes/type2Minutes;
		type1 = type1 - Math.floor(type1);
		type2 = type2 - Math.floor(type2);		
		String type = null;
		int typeMinutes = 0;
		if(type1 > type2)
		{
			if(type1 < 0.8) {
				StringBuilder builder = new StringBuilder();
				builder.append("Remove activities with "+ (type1 * type1Minutes) + "Minutes").append("\n OR");
				builder.append("Add activities with "+((1-type1) * type1Minutes) + "Minutes");				
				throw new Exception(builder.toString());				
			}else
			{
				type = "type1";
				typeMinutes = type1Minutes;
			}
		}else {
			if(type2 < 0.8) {
				StringBuilder builder = new StringBuilder();
				builder.append("Remove activities with "+ (type2 * type1Minutes) + "Minutes").append("\n OR");
				builder.append("Add activities with "+((1-type2) * type1Minutes) + "Minutes");				
				throw new Exception(builder.toString());
			}else {
				type = "type2";
				typeMinutes = type2Minutes;
			}
		}
		int keyCounter = 1;
		while(totalMinutes > 0 ) {
			int minutesForSegregation = typeMinutes;
			int morningMinutes = 180;			
			TimeDto time = null;
			List<ActivityDto> result = new ArrayList<>();
			ListIterator<ActivityDto> iter = activities.listIterator();			
			while(iter.hasNext() && minutesForSegregation > 0){
				ActivityDto dto = iter.next();
			    if(morningMinutes > 0) {
			    	int temp = morningMinutes - dto.getMinute();
			    	if(temp > 0)
			    	{
			    		morningMinutes = temp;
			    		minutesForSegregation = minutesForSegregation - dto.getMinute();
			    		time = time == null? new TimeDto(9, 0, "AM") : time;
			    		dto.setTime(time);
			    		result.add(dto);
			    		time.addMinutes(dto.getMinute());
			    		iter.remove();
			    	}
			    	else if(temp >= -30 && temp <= 0) {
			    		morningMinutes = 0;
			    		minutesForSegregation = minutesForSegregation - dto.getMinute();
			    		time = time == null? new TimeDto(9, 0, "AM") : time;
			    		dto.setTime(time);
			    		result.add(dto);
			    		time.addMinutes(dto.getMinute());
			    		iter.remove();
			    		ActivityDto lunchActivity = new ActivityDto();
			    		lunchActivity.setTime(time);
			    		lunchActivity.setEvent("Lunch Break");
			    		lunchActivity.setRange("60min");
			    		result.add(lunchActivity);
			    		time.addMinutes(60);
			    	}
			    	else {
			    		iter.remove();
			    		iter.add(dto);
			    	}
			    	
			    }else {
			    	int temp = minutesForSegregation - dto.getMinute();
			    	temp = type.equalsIgnoreCase("type2")?(temp - 60) : temp;
			    	if(temp > 0) {
			    		minutesForSegregation = minutesForSegregation - dto.getMinute();
			    		dto.setTime(time);
			    		result.add(dto);
			    		time.addMinutes(dto.getMinute());
			    		iter.remove();
			    	}else if(temp >= -60 && temp <= 0){
			    		minutesForSegregation = 0;
			    		totalMinutes = totalMinutes - typeMinutes + temp;
			    		dto.setTime(time);
			    		result.add(dto);
			    		time.addMinutes(dto.getMinute());
			    		iter.remove();
			    	}else {
			    		iter.remove();
			    		iter.add(dto);
			    	}
			    }
			}
			ActivityDto finalSession = new ActivityDto();
			finalSession.setEvent("Staff Motivation Presentation");
			finalSession.setTime(time);
			result.add(finalSession);	
			teamActivityMap.put(keyCounter++, result);
		}
		return teamActivityMap;
	}
	
	
	private void validateActivitiesList() {
		//todo
	}
	
	private long calculateTotalMinutes(List<ActivityDto> activities) {		
		long totalMinutes = 0;
		for(ActivityDto activity: activities) {
			totalMinutes += activity.getMinute();
		}
		return totalMinutes;
	}	
	
	private List<ActivityDto> convertFileToObject(File file) throws Exception{
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

	private ActivityDto generateActivity(String line) throws Exception {
		int minute = 0;
		int index = 0;
		Matcher m = Pattern.compile("\\d+").matcher(line);		
		ActivityDto dto = new ActivityDto();
		if (m.find()) {			
			minute = Integer.valueOf(m.group());
			while(m.find()) {
				minute = Integer.valueOf(m.group());
			}
			dto.setMinute(minute);
			index = line.indexOf(minute + "");
		} else {
			dto.setMinute(15);
			index = line.toLowerCase().indexOf("sprint");
			if(index == 0)
				throw new Exception ("Following Line is invalid \n "+ line +" - Follow pattern: <Activity name>  <Minutes/sprint> ");
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
