package com.dd.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.dd.dto.ActivityDto;
import com.dd.dto.TimeDto;
import com.dd.entity.Activity;
import com.dd.entity.Team;
import com.dd.repository.ActivityRepository;
import com.dd.repository.TeamRepository;
import com.dd.util.Constants;

@Component
public class ActivitiesOrganizeService extends CommonBaseServiceImpl<ActivityDto, Activity> {

	@Resource
	private ActivityRepository repository;
	
	@Resource
	private TeamRepository teamRepository;
	
	@Override
	public JpaRepository<Activity, Long> getJPARepository() {
		return repository;
	}
	
	@Override
	public Activity convertDtoToEntity(ActivityDto dto) {
		Activity entity = new Activity();
		if(dto != null) {
			entity.setEvent(dto.getEvent());
			entity.setPeriod(dto.getRange());
			entity.setTime(dto.getTime() == null? null: dto.getTime().toString());
			if(dto.getTeamId()>0)
			{
				entity.setTeam(teamRepository.findOne(dto.getTeamId()));
			}
		}
		return entity;
	}

	@Override
	public List<Activity> convertDtosToEntities(List<ActivityDto> dtoList) {
		List<Activity> entities = new ArrayList<>();
		if(dtoList != null && dtoList.size() > 0) {
			for(ActivityDto dto: dtoList) {
				entities.add(convertDtoToEntity(dto));
			}
		}
		return entities;
	}

	@Override
	public ActivityDto convertEntityToDto(Activity entity) {
		ActivityDto dto = new ActivityDto();
		if(entity != null) {
			dto.setEvent(entity.getEvent());
			dto.setRange(entity.getPeriod());
			dto.setTimeString(entity.getTime());
			if(entity.getTeam() != null)
				dto.setTeamId(entity.getTeam().getTeamId());
		}
		return dto;
	}

	@Override
	public List<ActivityDto> convertEntitiesToDtos(List<Activity> entities) {
		List<ActivityDto> dtos = new ArrayList<>();
		if(dtos != null && dtos.size() > 0) {
			for(Activity entity : entities) {
				dtos.add(convertEntityToDto(entity));
			}
		}
		return dtos;
	}

	public String organizeActivity(MultipartFile multipartFile) throws Exception {
		File file = null;
		StringBuilder builder = new StringBuilder();
		try {
			file = convertMultiPartToFile(multipartFile);
			List<ActivityDto> activities = this.convertFileToObject(file);
			long totalMinutes = this.calculateTotalMinutes(activities);
			Map<Integer, List<ActivityDto>> result = this.segregateActivitiesIntoTeams(activities, totalMinutes);
			
			for(Map.Entry<Integer, List<ActivityDto>> entry : result.entrySet()) {
				Team team = new Team();
				team.setTeamName("Team"+ entry.getKey());
				teamRepository.saveAndFlush(team);
				builder.append("Team: " + entry.getKey()).append(Constants.NEWLINE);
				for(ActivityDto dt : entry.getValue()) {
					builder.append(dt.getTime() + dt.getEvent()+dt.getRange()).append(Constants.NEWLINE);
					Activity activity = new Activity();
					activity.setEvent(dt.getEvent());
					activity.setPeriod(dt.getRange());
					activity.setTime(dt.getTime()==null? null : dt.getTime().toString());
					activity.setTeam(team);
					repository.saveAndFlush(activity);
				}
			}
		} catch (Exception ex) {
			throw ex;
		}
		return builder.toString();
	}
	
	/*public static void main(String arg[]) {
		List<Integer> values = new ArrayList<>();
		values.add(1);
		values.add(2);
		values.add(3);
		values.add(4);
		values.add(5);
		values.add(6);
		int counter = 1;
		int i = 0;
		while (i< values.size()) {
			int temp = values.get(i);
			if(counter == 2) {
				values.remove(i);
				values.add(temp);
				counter++;
				//i++;
				continue;
			}
			counter++;			
			System.out.println(values.get(i));
			i++;
		}
	}*/
	
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
		if(type1 == 0.0)
		{
			type = Constants.TYPE1;
			typeMinutes = type1Minutes;
		}else if(type2 == 0.0) {
			type = Constants.TYPE2;
			typeMinutes = type2Minutes;
		}
		else if(type1 > type2)
		{
			if(type1 < 0.9) {
				StringBuilder builder = new StringBuilder();
				if((int)(type1 * type1Minutes) < (int)(type2 * type2Minutes))
					builder.append("Either remove activities with "+ (int)(type1 * type1Minutes) + Constants.MINUTES).append(" OR ");
				else
					builder.append("Either remove activities with "+ (int)(type2 * type2Minutes) + Constants.MINUTES).append(" OR ");
				
				if((int)((1-type1) * type1Minutes) < (int)((1-type2) * type2Minutes))
					builder.append("Add activities with "+((int)((1-type1) * type1Minutes)) + Constants.MINUTES).append(" OR ");
				else
					builder.append("Add activities with "+((int)((1-type2) * type2Minutes)) + Constants.MINUTES).append(" OR ");
				throw new Exception(builder.toString());				
			}else
			{
				type = Constants.TYPE1;
				typeMinutes = type1Minutes;
			}
		}else {
			if(type2 < 0.8) {
				StringBuilder builder = new StringBuilder();
				if((int)(type1 * type1Minutes) < (int)(type2 * type2Minutes))
					builder.append("Either remove activities with "+ (int)(type1 * type1Minutes) + Constants.MINUTES).append(" OR ");
				else
					builder.append("Either remove activities with "+ (int)(type2 * type2Minutes) + Constants.MINUTES).append(" OR ");
				
				if((int)((1-type1) * type1Minutes) < (int)((1-type2) * type2Minutes))
					builder.append("Add activities with "+((int)((1-type1) * type1Minutes)) + Constants.MINUTES).append(" OR ");
				else
					builder.append("Add activities with "+((int)((1-type2) * type2Minutes)) + Constants.MINUTES).append(" OR ");				
				throw new Exception(builder.toString());
			}else {
				type = Constants.TYPE2;
				typeMinutes = type2Minutes;
			}
		}
		int keyCounter = 1;
		while(totalMinutes > 0 && activities.size()>0) {
			int minutesForSegregation = typeMinutes;
			int morningMinutes = 150;			
			TimeDto time = null;
			List<ActivityDto> result = new ArrayList<>();
			//ListIterator<ActivityDto> iter = activities.listIterator();	
			int i = 0;
			while(i<activities.size() && minutesForSegregation > 0){
				ActivityDto dto = activities.get(i);
			    if(morningMinutes > 0) {
			    	int temp = morningMinutes - dto.getMinute();
			    	if(temp > 0)
			    	{
			    		morningMinutes = temp;
			    		minutesForSegregation = minutesForSegregation - dto.getMinute();
			    		time = time == null? new TimeDto(9, 0, Constants.AM) : time;
			    		dto.setTime(time.clone());
			    		result.add(dto);
			    		time.addMinutes(dto.getMinute());
			    		activities.remove(i);			    		
			    	}
			    	else if(temp >= -30 && temp <= 0) {
			    		morningMinutes = 0;
			    		minutesForSegregation = minutesForSegregation - dto.getMinute();
			    		time = time == null? new TimeDto(9, 0, Constants.AM) : time;
			    		dto.setTime(time.clone());
			    		result.add(dto);
			    		time.addMinutes(dto.getMinute());
			    		activities.remove(i);
			    		ActivityDto lunchActivity = new ActivityDto();
			    		lunchActivity.setTime(time.clone());
			    		lunchActivity.setEvent(Constants.LUNCH_BREAK);
			    		lunchActivity.setRange(Constants.SIXTY_MINUTES);
			    		result.add(lunchActivity);
			    		time.addMinutes(60);
			    		totalMinutes = totalMinutes - 180 + temp;
			    	}
			    	else {
			    		activities.remove(i);
			    		activities.add(dto);
			    	}
			    	
			    }else {
			    	int temp = minutesForSegregation - dto.getMinute();
			    	temp = type.equalsIgnoreCase(Constants.TYPE2)?(temp - 60) : temp;
			    	if(temp > 0) {
			    		minutesForSegregation = minutesForSegregation - dto.getMinute();
			    		totalMinutes = totalMinutes - dto.getMinute();
			    		dto.setTime(time.clone());
			    		result.add(dto);
			    		time.addMinutes(dto.getMinute());
			    		activities.remove(i);
			    	}else if(temp >= -60 && temp <= 0){
			    		minutesForSegregation = minutesForSegregation - dto.getMinute();
			    		totalMinutes = totalMinutes - dto.getMinute();
			    		dto.setTime(time.clone());
			    		result.add(dto);
			    		time.addMinutes(dto.getMinute());
			    		activities.remove(i);
			    	}else if(temp < -60) {
			    		minutesForSegregation = 0;			    		
			    	}else {
			    		activities.remove(i);
			    		activities.add(dto);
			    	}
			    }
			}
			ActivityDto finalSession = new ActivityDto();
			finalSession.setEvent(Constants.FINAL_PRESENTATION);
			if(time.getHour() >= 4)
				finalSession.setTime(time.clone());
			else
				finalSession.setTime(new TimeDto(4, 0, Constants.PM));
			finalSession.setRange(Constants.EMPTY_STRING);
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
			index = line.toLowerCase().indexOf(Constants.SPRINT);
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
