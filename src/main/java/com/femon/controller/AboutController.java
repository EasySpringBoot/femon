/**
 * wai InterfaceController.java com.wai.controller
 */
package com.femon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author 一剑 2015年12月29日 下午5:23:22
 */
@Controller
public class AboutController {

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public ModelAndView createService(Model model) {
        return new ModelAndView("/about");
    }

}
