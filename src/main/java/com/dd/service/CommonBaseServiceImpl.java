package com.dd.service;

import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.dd.dto.IDto;

import javassist.NotFoundException;

@Service
@Transactional
public abstract class CommonBaseServiceImpl <T extends IDto, S extends Serializable> implements ICommonService<T,S>{
	
	@Override
	public T create(T dto) {
		S entity = convertDtoToEntity(dto);
		getJPARepository().saveAndFlush(entity);
		return convertEntityToDto(entity);
	}

	@Override
	public List<T> create(List<T> dtoList) {
		List<S> entities = convertDtosToEntities(dtoList);
		getJPARepository().save(entities);
 		return convertEntitiesToDtos(entities);
	}

	@Override
	public T update(T dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long id) {
		getJPARepository().delete(id);
	}

	@Override
	public T read(Long id) throws NotFoundException {
		S entity = getJPARepository().findOne(id);
		if(entity != null) {
			return convertEntityToDto(entity);
		}else
		{
			throw new NotFoundException("No such activity");
		}
	}

	@Override
	public List<T> readAll() throws NotFoundException {
		List<S> entities = getJPARepository().findAll();
		if(entities != null && entities.size() > 0)
		{
			return convertEntitiesToDtos(entities);
		}else {
			throw new NotFoundException("No activities in the system");
		}
	}
	
	public abstract JpaRepository<S, Long> getJPARepository();
	public abstract S convertDtoToEntity(T dto);
	public abstract List<S> convertDtosToEntities(List<T> dtoList);
	public abstract T convertEntityToDto(S entity);
	public abstract List<T> convertEntitiesToDtos(List<S> entities);
}
