package be.hvwebsites.mail.controllers;

import be.hvwebsites.mail.domain.Lid;
import be.hvwebsites.mail.exceptions.KanMailNietZendenException;
import be.hvwebsites.mail.services.LidService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("leden")
public class LidController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final LidService service;

    public LidController(LidService service) {
        this.service = service;
    }

    @GetMapping("registratieform")
    public ModelAndView registratieForm(){
        ModelAndView modelAndView = new ModelAndView("registratieform");
        Lid lid = new Lid("","","");
        modelAndView.addObject("lid", lid);
        return modelAndView;
    }

    @PostMapping
    public String registreer(@Valid Lid lid, Errors errors, RedirectAttributes redirectAttributes){
        if (errors.hasErrors()){
            return "registratieform";
        }
        try {
            service.registreer(lid);
        }catch (KanMailNietZendenException ex){
            logger.error("Kan mail niet zenden", ex);
            redirectAttributes.addAttribute("mailFout", true);
        }
        redirectAttributes.addAttribute("id", lid.getId());
        return "redirect:/leden/geregistreerd/{id}";
    }

    @GetMapping("geregistreerd/{id}")
    private String geregistreerd(@PathVariable long id){
        return "geregistreerd";
    }
    
    @GetMapping("{id}")
    public ModelAndView info(@PathVariable long id){
        ModelAndView modelAndView = new ModelAndView("lidinfo");
        service.findById(id).ifPresent(lid -> modelAndView.addObject(lid));
        return modelAndView;
    }
}
