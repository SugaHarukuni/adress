package com.tuyano.spring.repositories;

import com.tuyano.spring.MyData;
import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyDataRepository extends JpaRepository<MyData,Long>{

	public Optional<MyData> findById(Long name);
	public List<MyData> findByNameLikeOrderByIdAsc(String name);
	
}
