package com.qidian.mall.user.controller;

import com.alibaba.nacos.client.utils.IPUtil;
import com.central.base.exception.BusinessException;
import com.central.base.mvc.BaseController;
import com.central.base.restparam.RestRequest;
import com.central.base.restparam.RestResponse;
import com.central.base.util.IpUtil;
import com.qidian.mall.file.api.FileApi;
import com.qidian.mall.user.entity.SysUser;
import com.qidian.mall.user.quartz.QuartzJobManager;
import com.qidian.mall.user.quartz.TestQuartz;
import com.qidian.mall.file.response.FileInfoDTO;
import com.qidian.mall.user.request.RegUserDTO;
import com.qidian.mall.user.response.UserInfoVO;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qidian.mall.user.request.SysUserDTO;
import com.qidian.mall.user.response.SysUserVO;
import com.qidian.mall.user.service.ISysUserService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 系统用户名 前端控制器
 * </p>
 *
 * @author binsun
 * @since 2020-01-10
 */
@Slf4j
@Api(tags = {"用户管理"})
@RestController
@RequestMapping("/sys-user")
public class SysUserController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(SysUserController.class);

    @Autowired
    public ISysUserService sysUserService;

    @Resource
    public FileApi fileApi;

    /**
     * 用户注册
     * @param restRequest
     * @return
     */
    @ApiOperation(value = "用户注册", notes = "用户注册")
    @RequestMapping(value = "/regUser", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse regUser(@Validated(RegUserDTO.Add.class)  @RequestBody RestRequest<RegUserDTO> restRequest,HttpServletRequest request) {
        restRequest.getBody().setClientIp(IpUtil.getIpAddress(request));
        boolean result = sysUserService.regUser(restRequest.getBody());
        return new RestResponse().success(result);
    }



    /**
    * 新增系统用户名数据
    * @param restRequest 保存参数
    * @return 是否添加成功
    */
    @ApiOperation(value = "保存", notes = "保存数据到SysUser")
    @RequestMapping(value = "/addUser", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse addSysUser(@Validated(SysUserDTO.Add.class) @RequestBody RestRequest<SysUserDTO> restRequest, HttpServletRequest request) {
        restRequest.getBody().setClientIp(IpUtil.getIpAddress(request));
        Integer result = sysUserService.save(restRequest.getBody());
        return new RestResponse().success(result);
    }

    /**
    * 更新系统用户名(根据主键id更新)
    * @param restRequest 修改参数
    * @return 是否更改成功
    */
    @ApiOperation(value = "更新数据", notes = "根据主键id更新SysUser数据")
    @RequestMapping(value = "/updateUserById", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse updateSysUserById(@Validated(SysUserDTO.Update.class) @RequestBody RestRequest<SysUserDTO> restRequest) {
        Integer result = sysUserService.updateById(restRequest.getBody());
        return new RestResponse().success(result);
    }

    /**
    * 删除系统用户名(根据主键id伪删除)
    * @param restRequest 主键id
    * @return 是否删除成功
    */
    @ApiOperation(value = "删除数据", notes = "根据主键id伪删除SysUser数据")
    @RequestMapping(value = "/deleteUserById", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse deleteSysUserById(@Validated(SysUserDTO.Id.class) @RequestBody RestRequest<SysUserDTO> restRequest) {
        Integer result = sysUserService.deleteById(restRequest.getBody().getId());
        return new RestResponse().success(result);
    }

    /**
    * 根据主键id查询单条系统用户名
    * @param restRequest 主键id
    * @return 查询结果
    */
    @ApiOperation(value = "获取单条数据", notes = "根据主键id获取SysUser数据")
    @RequestMapping(value = "/getUserInfoById", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse getSysUserById(@Validated(SysUserDTO.Id.class) @RequestBody RestRequest<SysUserDTO> restRequest) {
        SysUserVO result = sysUserService.selectById(restRequest.getBody().getId());
        return new RestResponse().success(result);
    }

    /**
    * 查询全部系统用户名
    * @param restRequest 查询条件
    * @return 查询结果
    */
    @ApiOperation(value = "全部查询", notes = "查询SysUser全部数据")
    @RequestMapping(value = "/queryAll", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse getSysUserAll(@RequestBody RestRequest<SysUserDTO> restRequest) throws InterruptedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getPrincipal());
        log.info("查询系统用户入参：{}",restRequest);
        if(restRequest.getBody()==null){
            throw new BusinessException("102101",getMessage("102101"));
        }
        List<SysUserVO> result = sysUserService.selectAll(restRequest.getBody());
        return new RestResponse().success(result);
    }

    /**
    * 分页查询系统用户名
    * @param restRequest 查询条件
    * @return 查询结果
    */
    @ApiOperation(value = "分页查询", notes = "分页查询SysUser全部数据")
    @RequestMapping(value = "/queryPage", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public RestResponse getSysUserPage(@RequestBody RestRequest<SysUserDTO> restRequest) {
        //请求携带token 可以获取token 对应的信息。 因为携带token会经过spring security的过滤链
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getPrincipal());
        IPage<SysUserVO> result = sysUserService.selectPage(restRequest.getBody(),restRequest.getHeader().getPageSize(),restRequest.getHeader().getPageNum());
        return new RestResponse().success(result);
    }



    /**
     * 根据用户名查询用户信息
     */
    @GetMapping(value = "/queryByUsername", params = "username")
    @ApiOperation(value = "根据用户名查询用户")
    public RestResponse queryByUsername(String username) {
        return RestResponse.resultSuccess(sysUserService.selectByUsername(username));
    }

    /**
     * 根据用户名查询用户信息
     */
    @GetMapping(value = "/queryUserInfo", params = "username")
    @ApiOperation(value = "查询用户详细信息")
    public RestResponse<UserInfoVO> queryUserInfo(String username) {
        return RestResponse.resultSuccess(sysUserService.getUserInfo(username));
    }



    /**
     * =============== uaa认证授权获取token 接口使用 =============
     */

    /**
     * 根据用户名查询用户 --
     */
    @GetMapping(value = "/users-anon/login", params = "username")
    @ApiOperation(value = "根据用户名查询用户")
    public RestResponse findByUsername(String username) {
        return RestResponse.resultSuccess(sysUserService.findByUsername(username));
    }

    /**
     * 通过手机号查询用户、角色信息
     *
     * @param mobile 手机号
     */
    @GetMapping(value = "/users-anon/mobile")
    @ApiOperation(value = "根据手机号查询用户（密码/短信验证）")
    public RestResponse findByMobile(@RequestParam("mobile") String mobile) {
        return RestResponse.resultSuccess(sysUserService.findByMobile(mobile));
    }

    /**
     * 根据OpenId查询用户信息
     *
     * @param openId openId
     */
    @GetMapping(value = "/users-anon/openId")
    @ApiOperation(value = "根据OpenId查询用户")
    public RestResponse findByOpenId(@RequestParam("openId") String openId) {
        return RestResponse.resultSuccess(sysUserService.findByOpenId(openId));
    }





    /**
     * =================  测试接口 =================
     */

    /**
     * 测试定时调度
     * @param request
     * @throws Exception
     */
    @PostMapping("/task")
    public void task(HttpServletRequest request) throws Exception {
        QuartzJobManager.getInstance().addJob(TestQuartz.class, "testJob","Group1", "0/5 * * * * ? ");
    }

    /**
     * 测试feign拦截传递token ------因为feign默认不支持文件上传和表单请求，所以这个会报不是文件上传请求的错误，需要加配置，百度
     * @param multipartFile
     * @return
     */
    @PostMapping("/fileUpload")
    public RestResponse testFileUpload(@RequestParam("file") MultipartFile multipartFile){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        RestResponse<FileInfoDTO>  restResponse= fileApi.uploadFile(multipartFile,"1");
        return RestResponse.resultSuccess(restResponse);
    }

}
