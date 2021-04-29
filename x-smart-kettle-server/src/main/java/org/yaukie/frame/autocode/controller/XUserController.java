package org.yaukie.frame.autocode.controller;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.yaukie.core.annotation.EnablePage;
import org.yaukie.core.annotation.LogAround;
import org.yaukie.core.base.controller.BaseController;
import org.yaukie.core.constant.BaseResult;
import org.yaukie.core.constant.PageResult;
import org.yaukie.frame.autocode.dao.mapper.XUserMapper;
import org.yaukie.frame.autocode.service.api.XUserService;
import org.yaukie.frame.autocode.model.XUser;
import org.yaukie.frame.autocode.model.XUserExample;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: lxl
 * @create: 2021/04/14 22/55/130
 **/
@RestController
@RequestMapping(value = "/op/xuser/")
@Api(value = "XUser控制器", description = "XUser管理")
@Slf4j
public class XUserController  extends BaseController {

    @Autowired
    private XUserService xUserService;

    @Autowired
    public XUserMapper xUserMapper;

    @GetMapping(value = "/listPage")
    @ApiOperation("获取用户列表")
    @EnablePage
    @LogAround("获取用户列表")
    public BaseResult getUserPageList(
            @RequestParam(value = "offset",required = false)String offset,
            @RequestParam(value = "limit",required = false)String limit,
            @RequestParam(value = "nickName",required = false)String nickName,
            @RequestParam(value =  "userPhone",required = false)String userPhone) {
//        XUserExample xUserExample = new XUserExample();
//        xUserExample.createCriteria().andStatusEqualTo("1");
//        if(StringUtils.isNotBlank(nickName)){
//            // 模糊搜索昵称
//            xUserExample.createCriteria().andNickNameLike("%"+nickName+"%");
//        }
//
//        if(StringUtils.isNotBlank(userPhone)){
//            xUserExample.createCriteria().andUserPhoneLike("%"+userPhone+"%");
//        }
//        List<XUser> xUserList = this.xUserService.selectByExample(xUserExample);
        List<XUser> xUserList = xUserService.getUserListByConditions(nickName,userPhone);

        PageResult pageResult = new PageResult(xUserList);

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
    public BaseResult getUser(@PathVariable String id) {
        XUser xUser = this.xUserService.selectByPrimaryKey(Integer.parseInt(id));
        return BaseResult.success(xUser);
    }

    @PostMapping(value = "/add")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "xUser"+"", value = "xUser"+"",
                    required = true,dataTypeClass =XUser.class),
    })
    @ApiOperation("新增")
    public BaseResult addUser(@RequestBody @Validated XUser xUser, BindingResult BindingResult) {
        if (BindingResult.hasErrors()) {
            return this.getErrorMessage(BindingResult);
        }
        this.xUserService.insertSelective(xUser);
        return BaseResult.success();
    }

    @PostMapping(value = "/update")
    @ApiOperation("更新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "xUser"+"", value = "xUser"+"",
                    required = true,dataTypeClass =XUser.class),
    })
    public BaseResult updateUser(@RequestBody @Validated XUser xUser, BindingResult BindingResult) {
        if (BindingResult.hasErrors()) {
            return this.getErrorMessage(BindingResult);
        }

        this.xUserService.updateByPrimaryKey(xUser);
        return BaseResult.success();
    }

    @GetMapping(value = "/delete/{id}")
    @ApiOperation("删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "string" ),
    })
    public BaseResult deleteUser(@PathVariable String id) {
        XUserExample xUserExample = new  XUserExample();
        // xUserExample.createCriteria().andIdEqualsTo(id);
        this.xUserService.deleteByExample(xUserExample);
        return BaseResult.success();
    }

    public BaseResult getErrorMessage(BindingResult BindingResult){
        String errorMessage = "";
        for (ObjectError objectError : BindingResult.getAllErrors()) {
            errorMessage += objectError.getDefaultMessage();
        }
        return BaseResult.fail(errorMessage);
    }



    /**
     * 用户登录
     * @param xuser
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces={"application/json;charset=UTF-8"})
    @ApiOperation("用户登录")
    @LogAround("用户登录")
    public BaseResult login(@RequestBody XUser xuser, HttpServletRequest request) {
        BaseResult baseresult = new BaseResult();
        if(null == xuser){
            baseresult.setMessage("请输入账号和密码");
            baseresult.setSuccess(false);
            return baseresult;
        }else if(StringUtils.isBlank(xuser.getUserName())){
            baseresult.setMessage("请账号用户名");
            baseresult.setSuccess(false);
            return baseresult;
        }else if(StringUtils.isBlank(xuser.getPassword())){
            baseresult.setMessage("请输入密码");
            baseresult.setSuccess(false);
            return baseresult;
        }
        // 从SecurityUtils里边创建一个 subject
        Subject subject = SecurityUtils.getSubject();
        // 在认证提交前准备 token（令牌）
        UsernamePasswordToken token = new UsernamePasswordToken(xuser.getUserName(), xuser.getPassword());
        // 执行认证登陆
        try {
            subject.login(token);
        } catch (UnknownAccountException uae) {
            baseresult.setMessage("未知账户");
            baseresult.setSuccess(false);
            return baseresult;
        } catch (IncorrectCredentialsException ice) {
            baseresult.setMessage("密码不正确");
            baseresult.setSuccess(false);
            return baseresult;
        } catch (LockedAccountException lae) {
            baseresult.setMessage("账户已锁定");
            baseresult.setSuccess(false);
            return baseresult;
        }
/*        catch (ExcessiveAttemptsException eae) {
            return "用户名或密码错误次数过多";
        } */
        catch (AuthenticationException ae) {
            baseresult.setMessage("用户名或密码不正确");
            baseresult.setSuccess(false);
            return baseresult;
        }
        if (subject.isAuthenticated()) {

            //存入HttpSession
            XUser loginUser = (XUser) subject.getPrincipal();
            request.getSession().setAttribute(loginUser.getToken(),loginUser);

            baseresult.setMessage("登陆成功");
            baseresult.setSuccess(true);
            //返回登录用户信息
            baseresult.setData(subject.getPrincipal());
            return baseresult;
        } else {
            token.clear();
            baseresult.setMessage("登陆失败");
            baseresult.setSuccess(false);
            return baseresult;
        }
    }

    /**
     * 根据 token（UUID）获取当前登录用户信息
     * @param xuser-token
     * @return
     */
    @RequestMapping(value = "/getLoginUser", method = RequestMethod.POST, produces={"application/json;charset=UTF-8"})
    @ApiOperation("根据 token（UUID）获取当前登录用户信息")
    @LogAround("根据 token（UUID）获取当前登录用户信息")
    public BaseResult getLoginUser(@RequestBody XUser xuser,HttpServletRequest request) {
        BaseResult baseresult = new BaseResult();
        //------------------------------------------------------
//        XUserExample xUserExample = new XUserExample();
//        xUserExample.createCriteria().andTokenEqualTo(xuser.getToken());
//        List<XUser> xuserlist = xUserMapper.selectByExample(xUserExample);
//        if(xuserlist.size()> 0){
//            baseresult.setSuccess(true);
//            baseresult.setData(xuserlist.get(0));
//        }else{
//            baseresult.setSuccess(false);
//            baseresult.setData(null);
//            baseresult.setMessage("token失效，请重新登录。");
//        }
        //------------------------------------------------------


        XUser loginUser = (XUser) request.getSession().getAttribute(xuser.getToken());
        if(null != loginUser){
            baseresult.setSuccess(true);
            baseresult.setData(loginUser);
        }else{
            baseresult.setSuccess(false);
            baseresult.setMessage("token失效，请重新登录。");
        }

        return baseresult;

    }

    /**
     * 获取用户列表
     * @return BaseResult
     */
    @RequestMapping(value = "/getUserList", method = RequestMethod.POST, produces={"application/json;charset=UTF-8"})
    @ApiOperation("获取用户列表")
    @LogAround("获取用户列表")
    public BaseResult getUserList(){
        BaseResult baseResult = new BaseResult();
        XUserExample xUserExample = new XUserExample();
        xUserExample.createCriteria().andStatusEqualTo("1");
        List<XUser> resultList = xUserMapper.selectByExample(xUserExample);
        baseResult.setData(resultList);
        baseResult.setSuccess(true);
        return baseResult;
    }

    /**
     * 新增用户
     * @param newUser
     * @return
     */
    @RequestMapping(value = "/addUser", method = RequestMethod.POST, produces={"application/json;charset=UTF-8"})
    @ApiOperation("新增用户")
    @LogAround("新增用户")
    public BaseResult addUser(@RequestBody XUser newUser){
        BaseResult baseResult = new BaseResult();
        //开始校验
        if(null == newUser || StringUtils.isBlank(newUser.getUserName()) ||
                StringUtils.isBlank(newUser.getPassword())){
            return BaseResult.fail("请输入完整用户信息！");

        }else if(StringUtils.isBlank(newUser.getUserEmail())){
            return BaseResult.fail("请输入邮箱！");
        }
        XUserExample xUserExample = new XUserExample();
        xUserExample.createCriteria().andUserNameEqualTo(newUser.getUserName());
        List<XUser> resultList = xUserMapper.selectByExample(xUserExample);
        if(!resultList.isEmpty()){
            return BaseResult.fail("用户账号重复，请重新输入！");
        }
        //校验结束，新增用户
        newUser.setCreateTime(new Date());
        newUser.setUpdateTime(new Date());
        newUser.setStatus("1"); //启用状态
        newUser.setIsAdmin("0"); //是否为管理员
        int result = xUserMapper.insert(newUser);
        if(result > 0){
            baseResult.setSuccess(true);
            baseResult.setMessage("新增用户成功！");
        }else{
            baseResult.setSuccess(false);
            baseResult.setMessage("新增用户失败！");
        }

        return baseResult;
    }

    /**
     * 修改用户
     * @param xuser
     * @return
     */
    @RequestMapping(value = "/updateUser",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    @ApiOperation("修改用户")
    @LogAround("修改用户")
    public BaseResult updateUser(@RequestBody XUser xuser){
        BaseResult baseResult = new BaseResult();
        XUser originalUser = xUserMapper.selectByPrimaryKey(xuser.getId());
        if(null == xuser || null == xuser.getId()){
            baseResult.setSuccess(false);
            baseResult.setMessage("请传入必要参数!");
            return baseResult;
        }
        if(StringUtils.isNotBlank(xuser.getPassword())){
            originalUser.setPassword(xuser.getPassword());
        }
        if(StringUtils.isNotBlank(xuser.getUserPhone())){
            originalUser.setUserPhone(xuser.getUserPhone());
        }
        if(StringUtils.isNotBlank(xuser.getUserEmail())){
            originalUser.setUserEmail(xuser.getUserEmail());
        }
        if(StringUtils.isNotBlank(xuser.getNickName())){
            originalUser.setNickName(xuser.getNickName());
        }
        originalUser.setUpdateTime(new Date());
        xUserMapper.updateByPrimaryKey(originalUser);
        return BaseResult.success();
    }


    /**
     * 删除用户
     * @param xuser
     * @return
     */
    @RequestMapping(value = "/delUser", method = RequestMethod.POST, produces={"application/json;charset=UTF-8"})
    @ApiOperation("删除用户")
    @LogAround("删除用户")
    public BaseResult delUser(@RequestBody XUser xuser){
        BaseResult baseResult  = new BaseResult();
        if(null == xuser || null == xuser.getId()){
            baseResult.setSuccess(false);
            baseResult.setMessage("删除用户失败！请传入参数");
            return baseResult;
        }
//        int result = xUserMapper.deleteByPrimaryKey(xuser.getId());
        //逻辑删除用户
        XUser originalUser = xUserMapper.selectByPrimaryKey(xuser.getId());
        // 设置启用状态为0 ，启用状态（1：启用 ，0：禁用）
        originalUser.setStatus("0");
        int result = xUserMapper.updateByPrimaryKey(originalUser);

        if(result > 0){
            baseResult.setSuccess(true);
            baseResult.setMessage("删除用户成功！");
        }else{
            baseResult.setSuccess(false);
            baseResult.setMessage("删除用户失败！");
        }

        return baseResult;
    }


    /**
     * 发送邮件
     * @param xuser
     * @return
     */
    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST, produces={"application/json;charset=UTF-8"})
    @ApiOperation("发送邮件")
    @LogAround("发送邮件")
    public BaseResult sendEmail(@RequestBody XUser xuser){
        BaseResult baseResult  = new BaseResult();
        Boolean result =  xUserService.sendEmail(xuser.getId(),"测试邮件","event log，这里是日志内容");
        if(result){
            baseResult.setSuccess(true);
        }else{
            baseResult.setSuccess(false);
        }
        return baseResult;
    }

    /**
     * 根据用户id获取指定用户
     * @param xuser
     * @return
     */
    @RequestMapping(value = "/getUserById", method = RequestMethod.POST, produces={"application/json;charset=UTF-8"})
    @ApiOperation("根据用户id获取指定用户")
    @LogAround("根据用户id获取指定用户")
    public BaseResult getUserById(@RequestBody XUser xuser){
        return new BaseResult().success(xUserMapper.selectByPrimaryKey(xuser.getId()));
    }



}
