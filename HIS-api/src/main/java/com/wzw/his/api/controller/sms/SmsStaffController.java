package com.wzw.his.api.controller.sms;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wzw.his.common.api.CommonPage;
import com.wzw.his.common.api.CommonResult;
import com.wzw.his.common.dto.sms.SmsSkdDocParam;
import com.wzw.his.common.dto.sms.SmsSkdDocResult;
import com.wzw.his.common.dto.sms.SmsStaffParam;
import com.wzw.his.common.dto.sms.SmsStaffResult;
import com.wzw.his.mbg.model.SmsStaff;
import com.wzw.his.sms.SmsSkdService;
import com.wzw.his.sms.SmsStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/staff")
public class SmsStaffController {
    @Autowired
    SmsSkdService smsSkdService;
    @Autowired
    SmsStaffService smsStaffService;

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



}
