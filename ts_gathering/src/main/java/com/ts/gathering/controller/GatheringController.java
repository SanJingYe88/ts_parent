package com.ts.gathering.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value(value = "${file.upload.basePath4Win}")
    private String basePath4Win;
    @Value(value = "${file.upload.basePath4Linux}")
    private String basePath4Linux;

    /**
     * 文件上传
     * @param request
     * @param image
     * @return
     */
    @PostMapping(value = "upload")
    public Result upload(HttpServletRequest request,@RequestParam(value="image")MultipartFile image){
        log.info("upload");
        //要么获取文件在服务器上的储存位置 String basePath = request.getSession().getServletContext().getRealPath("resources/upload");
        //要么指定一个位置
        //指定的是前端项目所在的目录: "C:\\Users\\ThinkPad\Desktop\\ES6\\test\\vue-admin-template-master\\src\\assets\\upload"
        String osName = System.getProperty("os.name");
        String basePath = basePath4Win;
        if (!osName.toLowerCase().contains("win")){
            basePath = basePath4Linux;
        }
        String filePath = gatheringService.saveFile(basePath, image);
        return new Result(true, StatusCode.OK, "文件上传成功", filePath);
    }

    /**
     * 文件下载
     * @param request
     * @param response
     */
    @GetMapping(value = "download")
    public void download(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String name = request.getParameter("fileName");
        CheckUtils.notNullEmptyParam(name,name);
        String path = "E:\\upload" + File.separator + name;
        File file = new File(path);
        if (!file.exists()) {
            throw new RuntimeException("文件不存在");
        }
        log.info("{}",file.getName());
        //下载的文件携带这个名称
        response.setHeader("Content-Disposition", "attachment;filename=" + name);
        //文件下载类型--二进制文件
        response.setContentType("application/octet-stream");

        response.setContentLength((int) file.length());

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] buffer = new byte[128];
            int count = 0;
            while ((count = fis.read(buffer)) > 0) {
                response.getOutputStream().write(buffer, 0, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            response.getOutputStream().flush();
            response.getOutputStream().close();
            fis.close();
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
