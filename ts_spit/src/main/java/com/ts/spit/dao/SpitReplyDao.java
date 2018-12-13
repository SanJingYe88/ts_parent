package com.ts.spit.dao;

import com.ts.spit.pojo.SpitReply;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpitReplyDao extends MongoRepository<SpitReply,String> {
}
