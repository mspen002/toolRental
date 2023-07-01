package com.toolRental.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.toolRental.model.ToolSupply;

@NoRepositoryBean
public interface ToolSupplyRepository extends CrudRepository<ToolSupply, String> {

}