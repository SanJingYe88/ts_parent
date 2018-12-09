package com.ts.base.dao;

import com.ts.base.pojo.Label;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LabelDao extends JpaRepository<Label, String> {

}
