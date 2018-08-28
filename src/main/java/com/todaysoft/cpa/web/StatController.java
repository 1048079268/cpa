package com.todaysoft.cpa.web;

import com.todaysoft.cpa.mongo.TaskManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author liceyo
 * @version 2018/8/28
 */
@RestController
@RequestMapping("/stat")
public class StatController {

    @Autowired
    private TaskManagerService taskManagerService;

    @RequestMapping("/mongo")
    public Map<String,Object> mongo(){
        return taskManagerService.statMongo();
    }
}
