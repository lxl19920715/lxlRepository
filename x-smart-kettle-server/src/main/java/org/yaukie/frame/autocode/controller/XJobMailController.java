package org.yaukie.frame.autocode.controller;

import lombok.extern.slf4j.Slf4j;

import org.yaukie.core.annotation.EnablePage;
import org.yaukie.core.base.controller.BaseController;
import org.yaukie.core.constant.BaseResult;
import org.yaukie.core.constant.PageResult;
import org.yaukie.frame.autocode.service.api.XJobMailService;
import org.yaukie.frame.autocode.model.XJobMail;
import org.yaukie.frame.autocode.model.XJobMailExample;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: yuenbin
 * @create: 2021/04/23 11/14/935
 **/
@RestController
@RequestMapping(value = "/op/xjobmail/")
@Api(value = "XJobMail控制器", description = "XJobMail管理")
@Slf4j
public class XJobMailController extends BaseController {

    @Autowired
    private XJobMailService xJobMailService;

    @GetMapping(value = "/listPage")
    @ApiOperation("获取列表")
    @EnablePage
    public BaseResult getJobPageList(
            @RequestParam(value = "offset", required = false) String offset,
            @RequestParam(value = "limit", required = false) String limit,
            @RequestParam(value = "search", required = false) String search) {
        XJobMailExample xJobMailExample = new XJobMailExample();
        List<XJobMail> xJobMailList = this.xJobMailService.selectByExample(xJobMailExample);
        PageResult pageResult = new PageResult(xJobMailList);
        Map<String, Object> result = new HashMap<>();
        result.put(RESULT_ROWS, pageResult.getRows());
        result.put(RESULT_TOTLAL, pageResult.getTotal());
        return BaseResult.success(result);
    }

    @GetMapping(value = "/get/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "string", paramType = "header")
    })
    @ApiOperation("获取信息")
    public BaseResult getJob(@PathVariable String id) {
        XJobMail xJobMail = this.xJobMailService.selectByPrimaryKey(Integer.parseInt(id));
        return BaseResult.success(xJobMail);
    }

    @PostMapping(value = "/add")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "xJobMail" + "", value = "xJobMail" + "",
                    required = true, dataTypeClass = XJobMail.class),
    })
    @ApiOperation("新增")
    public BaseResult addJob(@RequestBody @Validated XJobMail xJobMail, BindingResult BindingResult) {
        if (BindingResult.hasErrors()) {
            return this.getErrorMessage(BindingResult);
        }
        this.xJobMailService.insertSelective(xJobMail);
        return BaseResult.success();
    }

    @PostMapping(value = "/update")
    @ApiOperation("更新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "xJobMail" + "", value = "xJobMail" + "",
                    required = true, dataTypeClass = XJobMail.class),
    })
    public BaseResult updateJob(@RequestBody @Validated XJobMail xJobMail, BindingResult BindingResult) {
        if (BindingResult.hasErrors()) {
            return this.getErrorMessage(BindingResult);
        }

        this.xJobMailService.updateByPrimaryKey(xJobMail);
        return BaseResult.success();
    }

    @GetMapping(value = "/delete/{id}")
    @ApiOperation("删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "string"),
    })
    public BaseResult deleteJob(@PathVariable String id) {
        XJobMailExample xJobMailExample = new XJobMailExample();
        // xJobMailExample.createCriteria().andIdEqualsTo(id);
        this.xJobMailService.deleteByExample(xJobMailExample);
        return BaseResult.success();
    }

    public BaseResult getErrorMessage(BindingResult BindingResult) {
        String errorMessage = "";
        for (ObjectError objectError : BindingResult.getAllErrors()) {
            errorMessage += objectError.getDefaultMessage();
        }
        return BaseResult.fail(errorMessage);
    }
}
