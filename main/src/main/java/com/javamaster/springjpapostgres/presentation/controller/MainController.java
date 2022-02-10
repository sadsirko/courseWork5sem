package com.javamaster.springjpapostgres.presentation.controller;

import com.javamaster.springjpapostgres.business.service.CategoryService;
import com.javamaster.springjpapostgres.business.service.SourceService;
import com.javamaster.springjpapostgres.persistence.entity.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
@Scope("session")
public class MainController {

    @Autowired
    private SourceService sourceService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/home")
    public String homePage(HttpServletRequest request) {
        List<Source> chooseList = new ArrayList<>();
        List<Source> sourceList = sourceService.findAll();
        List<Source> sourceListAPI = sourceService.getDialogs();
        for(int i = 0; i < sourceListAPI.size();i++){
            System.out.println(sourceListAPI.get(i).toString());
        }

        if( request.getSession().getAttribute("sourceList") == null &&
                request.getSession().getAttribute("chooseList") == null) {
            request.getSession().setAttribute("sourceList", sourceListAPI);
            request.getSession().setAttribute("chooseList", chooseList);
        }
        System.out.println("home");
        return "home";
    }

    @RequestMapping(value = "/home/add", method = RequestMethod.GET)
    public String homeAdd(Model model,@ModelAttribute("sourceId") String sourceId,
                          @ModelAttribute("sourceName") String sourceName,
                          @ModelAttribute("source") Source source,
                          HttpServletRequest request) {
        List<Source> sourceList = (List<Source>) request.getSession().getAttribute("sourceList");
        List<Source> chooseList = (List<Source>) request.getSession().getAttribute("chooseList");
        Source newChosen = new Source();
        newChosen.setId((String) model.getAttribute("sourceId"));
        newChosen.setName((String) model.getAttribute("sourceName"));
        for(int i = 0; i < sourceList.size(); i++){
            if (sourceList.get(i).getId().equals(sourceId)){
                chooseList.add(sourceList.get(i));
                sourceList.remove(sourceList.get(i));
            }
        }
        request.getSession().setAttribute("sourceList",sourceList);
        request.getSession().setAttribute("chooseList",chooseList);
        return "home";
    }

    @RequestMapping(value = "/home/remove",method = RequestMethod.GET)
    public String homeRemove(Model model,@ModelAttribute("sourceId") String sourceId,
                          @ModelAttribute("sourceName") String sourceName,
                          @ModelAttribute("source") Source source,
                          HttpServletRequest request) {
        List<Source> sourceList = (List<Source>) request.getSession().getAttribute("sourceList");
        List<Source> chooseList = (List<Source>) request.getSession().getAttribute("chooseList");
        Source newChosen = new Source();
        newChosen.setId((String) model.getAttribute("sourceId"));
        newChosen.setName((String) model.getAttribute("sourceName"));
        for(int i = 0; i < chooseList.size(); i++){
            if (chooseList.get(i).getId().equals(sourceId)){
                sourceList.add(chooseList.get(i));
                chooseList.remove(chooseList.get(i));

            }
        }
        request.getSession().setAttribute("sourceList",sourceList);
        request.getSession().setAttribute("chooseList",chooseList);
        return "home";
    }

    @RequestMapping(value = "/processing",method = RequestMethod.GET)
    public String processing(Model model,@ModelAttribute("sourceId") String sourceId,
                          @ModelAttribute("sourceName") String sourceName,
                          @ModelAttribute("source") Source source,
                          HttpServletRequest request) {
        List<Source> sourceList = (List<Source>) request.getSession().getAttribute("sourceList");
        List<Source> chooseList = (List<Source>) request.getSession().getAttribute("chooseList");
        System.out.println(model.getAttribute("sourceId"));

        return "process";
    }

    @RequestMapping(value = "/processing",method = RequestMethod.POST)
    public String processingPage(Model model,@ModelAttribute("sourceId") String sourceId,
                          @ModelAttribute("startDate") String startDate,
                          @ModelAttribute("endDate") String endDate,
                          @ModelAttribute("stop") String stop,
                          @ModelAttribute("range") String range,
                          @ModelAttribute("symbolNum") String symbolNum,
                          HttpServletRequest request) {
        System.out.println(model.getAttribute("startDate"));
        System.out.println(model.getAttribute("endDate"));
        System.out.println(model.getAttribute("stop"));
        System.out.println(model.getAttribute("symbolNum"));
        System.out.println(model.getAttribute("range"));
        return "redirect:home";
    }

    @RequestMapping(value = "/addChannel",method = RequestMethod.GET)
    public String addChannelPage(Model model,@ModelAttribute("sourceId") String sourceId,
                          @ModelAttribute("sourceName") String sourceName,
                          @ModelAttribute("source") Source source,
                          HttpServletRequest request) {
        List<Source> sourceList = (List<Source>) request.getSession().getAttribute("sourceList");
        List<Source> chooseList = (List<Source>) request.getSession().getAttribute("chooseList");
        System.out.println(model.getAttribute("sourceId"));

        return "add";
    }

    @RequestMapping(value = "/addChannel",method = RequestMethod.POST)
    public String addChannel(Model model, @ModelAttribute("link") String link, HttpServletRequest request) {
        String info = (String) request.getSession().getAttribute("link");
        System.out.println(model.getAttribute("link"));
        return "redirect:home";
    }
//    @RequestMapping(value = "/home/add",method = RequestMethod.GET)
//    public String homeAdd(Model model,@ModelAttribute("source") String sourceId,
//                          HttpServletRequest request) {
//        List<Source> sourceList = (List<Source>) request.getSession().getAttribute("sourceList");
//        List<Source> chooseList = (List<Source>) request.getSession().getAttribute("chooseList");
//        Source newChosen = new Source();
//        System.out.println(model.getAttribute("sourceId"));
//        newChosen.setId((String) model.getAttribute("sourceId"));
//        newChosen.setName((String) model.getAttribute("sourceName"));
//        System.out.println(newChosen);
//
//        chooseList.add(newChosen);
////        for(int i = 0; i < sourceList.size(); i++){
////            if (sourceList.get(i).getId().equals(sourceId)){
////                chooseList.add(sourceList.get(i));
////                sourceList.remove(sourceList.get(i));
////            }
////        }
//        request.getSession().setAttribute("sourceList",sourceList);
//        request.getSession().setAttribute("chooseList",chooseList);
//        return "redirect:/home";
//    }
//

//    @GetMapping("/home")
//    public String homePage(HttpServletRequest request) {
//        List<Source> chooseList = new ArrayList<>();
//        List<Source> sourceList = sourceService.findAll();
//
//        chooseList.add(sourceList.get(0));
//
//        request.getSession().setAttribute("sourceList",sourceList);
//        request.getSession().setAttribute("chooseList",chooseList);
//        System.out.println("lol");
//        return "home";
//    }
//
//    @GetMapping("/home/add")
//    public String homeAdd(HttpServletRequest request) {
//        Source addedSource = (Source) request.getSession().getAttribute("source");
//        System.out.println(request.getSession().getAttribute("source"));
//        System.out.println(request.getSession().getAttribute("sourceId"));
//        System.out.println(request.getSession().getAttribute("sourceList"));
//        List<Source> chooseSourceList = (List<Source>) request.getSession().getAttribute("chooseSourceList");
//        if (chooseSourceList == null) {
//            chooseSourceList = new ArrayList<>();
//        }
//        chooseSourceList.add(addedSource);
//        request.getSession().setAttribute("chooseList",chooseSourceList);
//        request.getSession().setAttribute("sourceList",sourceService.findAll());
//        System.out.println(chooseSourceList.toString());
//        System.out.println(addedSource.toString());
//        return "home";
//    }
//    @RequestMapping(method = RequestMethod.GET, value = "/search")
//    @ResponseBody
//    public List<Good> search(@RequestParam(value = "query") String search) {
//        return goodService.getSearch(search);
//    }
//
//
//    @RequestMapping(method = RequestMethod.GET, value = "/search")
//    @ResponseBody
//    public List<Good> search(@RequestParam(value = "query") String search) {
//        return goodService.getSearch(search);
//    }


}
