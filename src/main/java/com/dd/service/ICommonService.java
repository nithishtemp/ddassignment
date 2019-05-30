package com.dd.service;

import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.dd.dto.IDto;

import javassist.NotFoundException;


@Service
@Transactional
public interface ICommonService <T extends IDto, S extends Serializable>{

	/**
	 * @param dto
	 * @return
	 */
	T create(T dto, String username);	
	
	/**
	 * @param dtoList
	 * @return
	 */
	List<T> create(List<T> dtoList, String username);	
	
	/**
	 * @param dto
	 * @return
	 */
	T update(T dto, String username);
	
	/**
	 * @param dto
	 */
	void delete(T dto);
	
	/**
	 * @param id
	 * @return
	 */
	T read(Long id)  throws NotFoundException;	
	
	/**
	 * @return
	 * @throws NotFoundException
	 */
	List<T> readAll() throws NotFoundException;

}