package com.ts.qa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ts.qa.pojo.Reply;

public interface ReplyDao extends JpaRepository<Reply,String>,JpaSpecificationExecutor<Reply>{



}
