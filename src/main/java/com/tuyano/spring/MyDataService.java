package com.tuyano.spring;

import org.springframework.beans.factory.annotation.Autowired;

import com.tuyano.spring.repositories.MyDataRepository;

public class MyDataService {
	@Autowired
	MyDataRepository repository;
	
	

}
