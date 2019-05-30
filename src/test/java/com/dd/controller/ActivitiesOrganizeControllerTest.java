package com.dd.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.ServletContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.dd.service.ActivitiesOrganizeService;

@RunWith(SpringRunner.class)
@WebMvcTest(ActivitiesOrganizeController.class)
public class ActivitiesOrganizeControllerTest {
	@Autowired
	private MockMvc mvc;

	@MockBean
	private ActivitiesOrganizeService service;

	@Autowired
	ServletContext context;
	
	@Test
	public void getAllActivityTest() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/activity").accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void getActivityByIdTest() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/activity/{activity_id}", 1).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void organizeActivityTest() throws Exception {
		String absolutePath = context.getRealPath("src\\main\\resources");
		File f = new File(absolutePath, "assignmenttest1.txt");
		//File f = new File("C:\\workspace\\assignment\\assignmenttest1.txt");
		FileInputStream fi1 = new FileInputStream(f);
		MockMultipartFile mockfile = new MockMultipartFile("file", f.getName(), "multipart/form-data",fi1);
		mvc.perform(MockMvcRequestBuilders.fileUpload("/activity-organize").file(mockfile)).andExpect(status().isOk());
	}
}
