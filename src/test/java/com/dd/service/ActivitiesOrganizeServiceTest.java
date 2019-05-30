package com.dd.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dd.dto.ActivityDto;
import com.dd.entity.Activity;
import com.dd.repository.ActivityRepository;
import com.dd.repository.TeamRepository;


@RunWith(SpringJUnit4ClassRunner.class)
public class ActivitiesOrganizeServiceTest {


	@InjectMocks
	private ActivitiesOrganizeService service;
	
	@Mock
	private ActivityRepository mockRepository;
	
	@Mock
	private TeamRepository mockTeamRepository;

	@Test
	public void convertDtoToEntityTest() {
		ActivityDto dto = new ActivityDto();
		dto.setEvent("test event");
		dto.setRange("15min");
		Activity entity = service.convertDtoToEntity(dto);
		Assert.assertEquals(dto.getEvent(), entity.getEvent());
		Assert.assertEquals(dto.getRange(), entity.getPeriod());
	}
	
	@Test
	public void convertDtosToEntitiesTest() {
		ActivityDto dto = new ActivityDto();
		dto.setEvent("test event");
		dto.setRange("15min");
		List<ActivityDto> dtosList = new ArrayList<>();
		dtosList.add(dto);
		List<Activity> entities = service.convertDtosToEntities(dtosList);
		Assert.assertEquals(dtosList.size(), entities.size());
		Assert.assertEquals(dto.getEvent(), entities.get(0).getEvent());
		Assert.assertEquals(dto.getRange(), entities.get(0).getPeriod());
	}
	
	
	@Test
	public void organizeActivityTest() throws IOException {
		String absolutePath = System.getProperty("user.dir");
		absolutePath = absolutePath + "\\src\\main\\resources";
		File f = new File(absolutePath, "assignmenttest1.txt");
		FileInputStream fi1 = new FileInputStream(f);
		MockMultipartFile mockfile = new MockMultipartFile("file", f.getName(), "multipart/form-data",fi1);
		String result = service.organizeActivity(mockfile);
		Assert.assertNotNull(result);
	}
	
}
