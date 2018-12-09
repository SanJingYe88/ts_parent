package com.ts.base.service;

import com.ts.base.dao.LabelDao;
import com.ts.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional      //事务
public class LabelService {

    @Autowired
    LabelDao labelDao;

    public List<Label> findAll() {
        return labelDao.findAll();
    }
}
