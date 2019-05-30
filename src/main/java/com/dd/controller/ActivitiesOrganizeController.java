package com.dd.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dd.dto.ActivityDto;
import com.dd.service.ActivitiesOrganizeService;
import com.dd.util.Constants;

import javassist.NotFoundException;

@RestController
public class ActivitiesOrganizeController {

	private Logger logger = LoggerFactory.getLogger(ActivitiesOrganizeController.class);

	@Autowired
	private ActivitiesOrganizeService service;

	@RequestMapping(value = Constants.ACTIVITY_ORGANIZE, method = RequestMethod.POST, consumes = "multipart/form-data")
	public @ResponseBody String organizeActivity(@RequestParam("file") MultipartFile file) throws HttpMessageNotReadableException, IOException  {
		logger.info("ActivitiesOrganizeController: organizeActivity - Start");
		return service.organizeActivity(file);
	}
	
	@RequestMapping(value = Constants.ACTIVITY, method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8", "application/xml; charset=utf-8" })
	public @ResponseBody List<ActivityDto> getAllActivities() throws NotFoundException {
		logger.info("ActivitiesOrganizeController: getAllActivities - Start");
		return service.readAll();
	}
	
	@RequestMapping(value = Constants.ACTIVITY_BY_ID, method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8", "application/xml; charset=utf-8" })
	public @ResponseBody ActivityDto getActivityById(@PathVariable("activity_id")long activityId) throws NotFoundException {
		logger.info("ActivitiesOrganizeController: getActivityById - Start");
		return service.read(activityId);
	}
}
