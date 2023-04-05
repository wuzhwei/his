package com.wzw.his.api.controller.dms;

import com.wzw.his.dms.service.DmsRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class DmsRegistrationController {
    @Autowired
    DmsRegistrationService dmsRegistrationService;
    //


}
