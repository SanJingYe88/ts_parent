package com.ts.recruit.controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.ts.recruit.pojo.Recruit;
import com.ts.recruit.service.RecruitService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/recruit")
public class RecruitController {

	@Autowired
	private RecruitService recruitService;
	
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",recruitService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",recruitService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<Recruit> pageList = recruitService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Recruit>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",recruitService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param recruit
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Recruit recruit  ){
		recruitService.add(recruit);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param recruit
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Recruit recruit, @PathVariable String id ){
		recruit.setId(id);
		recruitService.update(recruit);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		recruitService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	//推荐职位列表(state=2,以创建日期倒序,查询前10条)
	@GetMapping(value = "/search/recommend")
	public Result recommendRecruitList(){
		Result result = new Result(true,StatusCode.OK,"查询成功");
		result.setData(recruitService.recommendRecruitList());
		return result;
	}

	//最新职位列表(state!=0,以创建日期降序排序,查询前10条记录)
	@GetMapping(value = "/search/newList")
	public Result newRecruitList(){
		Result result = new Result(true,StatusCode.OK,"查询成功");
		result.setData(recruitService.newRecruitList());
		return result;
	}
}