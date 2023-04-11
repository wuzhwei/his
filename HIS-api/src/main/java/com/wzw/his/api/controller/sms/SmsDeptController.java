package com.wzw.his.api.controller.sms;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wzw.his.common.api.CommonPage;
import com.wzw.his.common.api.CommonResult;
import com.wzw.his.common.dto.sms.SmsDeptParam;
import com.wzw.his.common.dto.sms.SmsDeptResult;
import com.wzw.his.sms.SmsDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dept")
public class SmsDeptController {
    @Autowired
    SmsDeptService smsDeptService;

    /**
     * 描述:新增一个科室
     * @param smsDeptParam
     * @param result
     * @return
     */
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody SmsDeptParam smsDeptParam, BindingResult result){
        int count = smsDeptService.create(smsDeptParam);
        if (count > 0 ){
            return CommonResult.success(count,"添加科室成功");
        }
        return CommonResult.failed("添加科室失败");
    }

    /**
     * 描述:根据ids删除科室
     * @param ids
     * @return
     */

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("ids")List<Long> ids){
        int count = smsDeptService.delete(ids);
        if (count > 0 ){
            return CommonResult.success(count,"删除科室成功");
        }
        return CommonResult.failed("删除科室失败");
    }

    /**
     * 描述:更新一个科室的全部信息
     * @param id
     * @param smsDeptParam
     * @param result
     * @return
     */

    @RequestMapping(value = "/update/{id}",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable("id")Long id,@RequestBody SmsDeptParam smsDeptParam,BindingResult result){
        int count = smsDeptService.update(id, smsDeptParam);
        if (count > 0){
            return CommonResult.success("更新成功");

        }
        return CommonResult.failed("更新失败");
    }

    /**
     * 模糊查询科室、且分页
     * @param queryParam
     * @param pageSize
     * @param pageNum
     * @return
     */
    @RequestMapping(value = "/select",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<SmsDeptResult>> list(@RequestBody SmsDeptParam queryParam,
                                                        @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
                                                        @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum){
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<SmsDeptResult> list = smsDeptService.select(queryParam);
        Long pageTotal = page.getTotal();
        return CommonResult.success(CommonPage.restPage(list,pageTotal));

    }

    /**
     * 描述：查询所有科室信息
     * @return
     */
    @RequestMapping(value = "/selectAll",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SmsDeptResult>> listAll(){
        List<SmsDeptResult> list = smsDeptService.selectAll();
        return CommonResult.success(list);
    }


}
