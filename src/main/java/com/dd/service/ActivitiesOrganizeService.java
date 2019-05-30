package com.dd.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dd.dto.ActivityDto;
import com.dd.entity.Activity;
import com.dd.repository.ActivityRepository;

import javassist.NotFoundException;

@Component
public class ActivitiesOrganizeService implements ICommonService<ActivityDto, Activity>{
	
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

}
