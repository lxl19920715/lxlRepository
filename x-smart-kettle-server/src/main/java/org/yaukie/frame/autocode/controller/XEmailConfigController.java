package org.yaukie.frame.autocode.controller;

import lombok.extern.slf4j.Slf4j;

 import org.yaukie.core.annotation.EnablePage;
import org.yaukie.core.base.controller.BaseController;
import org.yaukie.core.constant.BaseResult;
import org.yaukie.core.constant.PageResult;
import org.yaukie.frame.autocode.service.api.XEmailConfigService;
import org.yaukie.frame.autocode.model.XEmailConfig;
import org.yaukie.frame.autocode.model.XEmailConfigExample;
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
* @create: 2021/04/23 09/51/39
**/
@RestController
@RequestMapping(value = "/op/xemailconfig/")
@Api(value = "XEmailConfig控制器", description = "XEmailConfig管理")
@Slf4j
public class XEmailConfigController  extends BaseController {

    @Autowired
    private XEmailConfigService xEmailConfigService;

    @GetMapping(value = "/listPage")
    @ApiOperation("获取列表")
   @EnablePage
     public BaseResult getEmailPageList(
                                        @RequestParam(value = "offset",required = false)String offset,
                                        @RequestParam(value = "limit",required = false)String limit,
                                         @RequestParam(value = "search",required = false)String search) {
XEmailConfigExample xEmailConfigExample = new XEmailConfigExample();
//    if(StringUtils.isNotBlank(search)){
//        xEmailConfigExample.createCriteria().andUserIdEqualTo(search);
//    }
     List<XEmailConfig> xEmailConfigList = this.xEmailConfigService.selectByExample(xEmailConfigExample);
               PageResult pageResult = new PageResult(xEmailConfigList);
                Map<String, Object> result = new HashMap<>();
                result.put(RESULT_ROWS, pageResult.getRows());
                result.put(RESULT_TOTLAL, pageResult.getTotal());
                return BaseResult.success( result);
                }

                @GetMapping(value = "/get/{id}")
                    @ApiImplicitParams({
                    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "string",paramType = "header")
                    })
               @ApiOperation("获取信息")
                public BaseResult getEmail(@PathVariable String id) {
                XEmailConfig xEmailConfig = this.xEmailConfigService.selectByPrimaryKey(Integer.parseInt(id));
                    return BaseResult.success(xEmailConfig);
                    }

                    @PostMapping(value = "/add")
                    @ApiImplicitParams({
                    @ApiImplicitParam(name = "xEmailConfig"+"", value = "xEmailConfig"+"",
                    required = true,dataTypeClass =XEmailConfig.class),
                    })
                    @ApiOperation("新增")
                    public BaseResult addEmail(@RequestBody @Validated XEmailConfig xEmailConfig, BindingResult BindingResult) {
                        if (BindingResult.hasErrors()) {
                        return this.getErrorMessage(BindingResult);
                        }
                        this.xEmailConfigService.insertSelective(xEmailConfig);
                        return BaseResult.success();
                        }

                        @PostMapping(value = "/update")
                        @ApiOperation("更新")
                        @ApiImplicitParams({
                        @ApiImplicitParam(name = "xEmailConfig"+"", value = "xEmailConfig"+"",
                            required = true,dataTypeClass =XEmailConfig.class),
                        })
                        public BaseResult updateEmail(@RequestBody @Validated XEmailConfig xEmailConfig, BindingResult BindingResult) {
                            if (BindingResult.hasErrors()) {
                            return this.getErrorMessage(BindingResult);
                            }

                            this.xEmailConfigService.updateByPrimaryKey(xEmailConfig);
                            return BaseResult.success();
                            }

                            @GetMapping(value = "/delete/{id}")
                            @ApiOperation("删除")
                              @ApiImplicitParams({
                            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "string" ),
                            })
                            public BaseResult deleteEmail(@PathVariable String id) {
                                XEmailConfigExample xEmailConfigExample = new  XEmailConfigExample();
                               // xEmailConfigExample.createCriteria().andIdEqualsTo(id);
                                this.xEmailConfigService.deleteByExample(xEmailConfigExample);
                                return BaseResult.success();
                                }

                                public BaseResult getErrorMessage(BindingResult BindingResult){
                                    String errorMessage = "";
                                    for (ObjectError objectError : BindingResult.getAllErrors()) {
                                    errorMessage += objectError.getDefaultMessage();
                                    }
                                    return BaseResult.fail(errorMessage);
                                    }
        }
