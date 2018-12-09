package com.ts.base.dao;

import com.ts.base.pojo.Label;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface LabelDao extends JpaRepository<Label, String> , JpaSpecificationExecutor<Label>{

}
