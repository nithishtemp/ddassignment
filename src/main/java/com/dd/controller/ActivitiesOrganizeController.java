package com.dd.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dd.dto.ActivityDto;
import com.dd.service.ActivitiesOrganizeService;
import com.dd.util.Constants;

@RestController
public class ActivitiesOrganizeController {

	private Logger logger = LoggerFactory.getLogger(ActivitiesOrganizeController.class);

	@Autowired
	private ActivitiesOrganizeService service;

	@RequestMapping(value = Constants.ACTIVITY_ORGANIZE, method = RequestMethod.POST, consumes = "multipart/form-data")
	public @ResponseBody List<ActivityDto> organizeActivity(@RequestParam("file") MultipartFile file) throws Exception {
		logger.info("ActivitiesOrganizeController: organizeActivity - Start");
		return service.organizeActivity(file);
	}
}
