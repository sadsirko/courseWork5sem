package com.javamaster.springjpapostgres.presentation.controller;

import com.javamaster.springjpapostgres.business.service.CategoryService;
import com.javamaster.springjpapostgres.business.service.SourceCategoryService;
import com.javamaster.springjpapostgres.business.service.SourceService;
import com.javamaster.springjpapostgres.persistence.entity.Category;
import com.javamaster.springjpapostgres.persistence.entity.Source;
import com.javamaster.springjpapostgres.persistence.entity.SourceCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    @Autowired
    private SourceCategoryService sourceCategoryService;

    @GetMapping("/home")
    public String homePage(HttpServletRequest request) {
        List<Source> chooseList = new ArrayList<>();
        List<Source> sourceList = sourceService.findAll();
//        List<Source> sourceListAPI = sourceService.getDialogs();
        List<Source> nameList = new ArrayList<>();

        if( request.getSession().getAttribute("sourceList") == null &&
                request.getSession().getAttribute("chooseList") == null) {
            request.getSession().setAttribute("sourceList", sourceList);
            request.getSession().setAttribute("chooseList", chooseList);
        }
        request.getSession().setAttribute("nameList", nameList);
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

    @RequestMapping(value = "/category/check", method = RequestMethod.POST)
    public String postCategory(Model model,@ModelAttribute("folderName") String folderName,
                               @ModelAttribute("folderId") String folderId, HttpServletRequest request) {
        System.out.println(model.getAttribute("folderId"));
        System.out.println(model.getAttribute("folderName"));
        System.out.println(folderName);
        System.out.println(folderId);

        List<Source> sourceList = sourceService.findByCategory((String)model.getAttribute("folderName"));
        System.out.println(sourceList);
        request.getSession().setAttribute("nameList", sourceList);
        return "home";
    }

    @RequestMapping(value = "/home/name",method = RequestMethod.GET)
    public String find(Model model, @ModelAttribute("name") String name,
                       HttpServletRequest request){
        List<Source> nameList = new ArrayList<>();
        nameList = sourceService.findByName(name);
        System.out.println(nameList);
        request.getSession().setAttribute("nameList", nameList);
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
    public String processingPage(Model model,@ModelAttribute("chooseList") String sourceId,
                          @ModelAttribute("startDate") String startDate,
                          @ModelAttribute("endDate") String endDate,
                          @ModelAttribute("stop") String stop,
                          @ModelAttribute("range") String range,
                          @ModelAttribute("symbolNum") String symbolNum,
                          HttpServletRequest request) {
        List<Source> chooseList = (List<Source>) request.getSession().getAttribute("chooseList");

        System.out.println(chooseList.toString());
        System.out.println(model.getAttribute("sourceId "));
        System.out.println(model.getAttribute("startDate"));
        System.out.println(model.getAttribute("endDate"));
        System.out.println(model.getAttribute("stop"));
        System.out.println(model.getAttribute("symbolNum"));
        System.out.println(model.getAttribute("range"));
        sourceService.process(startDate,endDate,stop,
                Integer.parseInt(symbolNum),Integer.parseInt(range),chooseList);

        return "redirect:download";
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

    @RequestMapping(value = "/addChannelTg",method = RequestMethod.GET)
    public String addChannel(Model model, @ModelAttribute("link") String link, HttpServletRequest request) {
        String info = (String) model.getAttribute("link");
        System.out.println();
        sourceService.addChannel(info);
        return "redirect:home";
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public String getCategories(Model model, @ModelAttribute("link") String link, HttpServletRequest request) {
        List<Category> nameList = new ArrayList<>();
        nameList = categoryService.findAll();
        System.out.println(nameList);
        request.getSession().setAttribute("nameList", nameList);
        return "category";
    }

    @RequestMapping(value = "/folders", method = RequestMethod.GET)
    public String postCategories(Model model, @ModelAttribute("link") String link, HttpServletRequest request) {
        sourceCategoryService.sourceCategories();
        List<Source> nameList = sourceService.findByCategory("????????????????????");
        System.out.println(nameList);
        request.getSession().setAttribute("nameList", nameList);
        request.getSession().setAttribute("newList", nameList);
        return "category";
    }

        @RequestMapping(value = "/download", method = RequestMethod.GET)
        public void downloadPDFResource( HttpServletRequest request,
                                         HttpServletResponse response)
        {
            System.out.println("down");
            String path = "/home/dionis/Desktop/study/courseWork5sem/tg/";
            Path file = Paths.get(path, "stage.json");
            if (Files.exists(file))
            {
                System.out.println("exist");
                response.setContentType("application/json");
                response.addHeader("Content-Disposition", "attachment; filename="+"stage.json");
                try
                {
                    Files.copy(file, response.getOutputStream());
                    response.getOutputStream().flush();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

}
