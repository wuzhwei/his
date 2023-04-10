package com.wzw.his.api.controller.sms;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wzw.his.common.api.CommonPage;
import com.wzw.his.common.api.CommonResult;
import com.wzw.his.common.dto.sms.*;
import com.wzw.his.mbg.model.SmsStaff;
import com.wzw.his.sms.SmsSkdService;
import com.wzw.his.sms.SmsStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/staff")
public class SmsStaffController {
    @Autowired
    SmsSkdService smsSkdService;
    @Autowired
    SmsStaffService smsStaffService;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    //查询上班医生
    //1.调用SmsSkdService的listDocBySkd
    @RequestMapping(value = "/listDocBySkd",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<List<SmsSkdDocResult>> listDocBySkd(@RequestBody SmsSkdDocParam smsSkdDocParam,
                                                            BindingResult result){
        List<SmsSkdDocResult> smsSkdDocResultList;
        smsSkdDocResultList = smsSkdService.listDocBySkd(smsSkdDocParam);
        return CommonResult.success(smsSkdDocResultList);

    }

    /**
     * 描述:新增一个用户
     * @return
     */
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody SmsStaffParam smsStaffParam, BindingResult result){
        SmsStaff smsStaff = smsStaffService.register(smsStaffParam);
        return CommonResult.success(smsStaff);
    }

    /**
     * 描述:根据ids删除用户
     * @param ids
     * @return
     */

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("ids")List<Long> ids){
        int count = smsStaffService.delete(ids);
        if (count > 0){
            return CommonResult.success(count,"删除成功");
        }
        return CommonResult.failed("删除失败");

    }

    @RequestMapping(value = "/update/{id}",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id,@RequestBody SmsStaffParam smsStaffParam,BindingResult result){
        int count = smsStaffService.update(id, smsStaffParam);
        if (id > 0){
            return CommonResult.success(count,"更新成功");

        }
        return CommonResult.failed("更新失败");
    }

    /**
     * 描述：模糊查询用户、且分页
     * @param queryParam
     * @param pageSize
     * @param pageNum
     * @return
     */

    @RequestMapping(value = "/select",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<SmsStaffResult>> list(@RequestBody SmsStaffParam queryParam,
                                                         @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
                                                         @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum){
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<SmsStaffResult> list = smsStaffService.select(queryParam, pageSize, pageNum);
        long pageTotal = page.getTotal();
        return CommonResult.success(CommonPage.restPage(list,pageTotal));
    }

    /**
     * 描述：查询所有用户
     * @return
     */
    @RequestMapping(value = "/selectAll",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<List<SmsStaffResult>> listAll(){
        List<SmsStaffResult> list = smsStaffService.selectAll();
        return CommonResult.success(list);
    }


    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult login(@RequestBody SmsStaffLoginParam smsStaffLoginParam, BindingResult result){
        String token = smsStaffService.login(smsStaffLoginParam.getUsername(), smsStaffLoginParam.getPassword());
        if (token == null){
            return CommonResult.validateFailed("用户名或密码错误");
        }
        //如果 token 不等于 null
        HashMap<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHead);
        return CommonResult.success(tokenMap);

    }


}
