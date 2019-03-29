package com.ts.gathering.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ts.gathering.pojo.Gathering;
import com.ts.gathering.service.GatheringService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.web.multipart.MultipartFile;
import util.CheckUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * Gathering 控制器层
 */
@Slf4j
@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/gathering")
public class GatheringController {

    @Autowired
    private GatheringService gatheringService;

    /**
     * 根据ID查询
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        log.info("id={}",id);
        return new Result(true, StatusCode.OK, "查询成功", gatheringService.findById(id));
    }

    /**
     * 分页+多条件查询
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        log.info("page={}",page);
        log.info("size={}",size);
        log.info("searchMap={}",searchMap);
        Page<Gathering> pageList = gatheringService.findSearch(searchMap, page, size);
        PageResult<Gathering> pageResult = new PageResult<>(pageList.getTotalElements(), pageList.getContent());
        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }

    /**
     * 增加
     * @param gathering
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Gathering gathering) {
        log.info("gathering={}",gathering);
        gatheringService.add(gathering);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     * @param gathering
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Gathering gathering, @PathVariable String id) {
        log.info("id={}",id);
        log.info("gathering={}",gathering);
        gathering.setId(id);
        gatheringService.update(gathering);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     * @param id
     */
    @RequestMapping(value = "/show/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        log.info("id={}",id);
        gatheringService.updateShowById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 文件上传
     * @param request
     * @param image
     * @return
     */
    @PostMapping(value = "upload")
    public Result upload(HttpServletRequest request,@RequestParam(value="image")MultipartFile image){
        log.info("upload");
        //要么获取文件在服务器上的储存位置
//        String basePath = request.getSession().getServletContext().getRealPath("resources/upload");
        //要么指定一个位置
        String basePath = "E:\\upload";
        String filePath = gatheringService.saveFile(basePath, image);
        return new Result(true, StatusCode.OK, "文件上传成功", filePath);
    }

    /**
     * 文件下载
     * @param request
     * @param response
     */
    @GetMapping(value = "download")
    public void download(HttpServletRequest request, HttpServletResponse response){
        String name = request.getParameter("fileName");
        CheckUtils.notNullEmptyParam(name,name);
        String path = "E:\\upload" + File.separator + name;
        File imageFile = new File(path);
        if (!imageFile.exists()) {
            throw new RuntimeException("文件不存在");
        }
        log.info("{}",imageFile.getName());
        //下载的文件携带这个名称
        response.setHeader("Content-Disposition", "attachment;filename=" + name);
        //文件下载类型--二进制文件
        response.setContentType("application/octet-stream");

        try {
            FileInputStream fis = new FileInputStream(path);
            byte[] content = new byte[fis.available()];
            fis.read(content);
            fis.close();
            ServletOutputStream sos = response.getOutputStream();
            sos.write(content);
            sos.flush();
            sos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    @GetMapping(value = "download")
//    public ResponseEntity<byte[]> download(){
//        try{
//            File file = new File("E:\\0077U09Sgy1fv4vszd8whg30d30go4qw.gif");
//            HttpHeaders headers= new HttpHeaders();
//            String fileName = new String("你好.gif".getBytes("UTF-8"),"iso-8859-1"); //为了解决中文名称乱码问题
//            headers.setContentDispositionFormData("attachment",fileName);
//            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//            return new ResponseEntity<>(org.apache.commons.io.FileUtils.readFileToByteArray(file),headers,HttpStatus.CREATED);
//        }catch (Exception e){
//            throw new RuntimeException("文件下载出错");
//        }
//    }
}
