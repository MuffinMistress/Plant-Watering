package com.ina.plantcalendar.controller;

import com.ina.plantcalendar.database.MyDataSource;
import com.ina.plantcalendar.database.IMyDataSource;
import com.ina.plantcalendar.dto.PlantDTO;
import com.ina.plantcalendar.model.Event;
import com.ina.plantcalendar.model.Plant;
import com.ina.plantcalendar.services.EventsService;
import com.ina.plantcalendar.services.FooterService;
import com.ina.plantcalendar.services.MyPlantsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Controller
public class MyPlantsController {

    private final MyPlantsService myPlantsService;
    private final IMyDataSource dataSource;
    private final FooterService footerService;

    @Autowired
    public MyPlantsController(MyPlantsService myPlantsService, MyDataSource dataSource, FooterService footerService) {
        this.myPlantsService = myPlantsService;
        this.dataSource = dataSource;
        this.footerService = footerService;
    }

    @RequestMapping(value={"/myplants"})
    public String displayMyPlantsPage(@RequestParam(required = false, name = "add_plant") boolean addPlant, Model model) throws SQLException {
        model.addAttribute("add_plant", addPlant);
        model.addAttribute("plant", new Plant("Calathea Lancifolia", "Rattlesnake plant", Plant.PlantType.CALATHEA, 7));

        // Plants in the gallery

        List<Plant> plants = dataSource.queryPlants();
        for (int i=0; i< plants.size(); i++) {
            model.addAttribute("plant" + (i+1) + "_scientific_name", plants.get(i).getScientificName());
            model.addAttribute("plant" + (i+1) + "_alias", plants.get(i).getAlias());
            model.addAttribute("plant" + (i+1) + "_watering_pattern", plants.get(i).getWateringPatternText());
        }

        footerService.fillFooterData(model);

        return "my-plants.html";
    }

    @PostMapping(value={"/savePlant"})
    public String savePlant(@Valid @ModelAttribute("plant") PlantDTO plant, Errors errors) {
        if (errors.hasErrors()) {
            log.error("From validation failed due to: " + errors);
            return "my-plants.html";
        }
        myPlantsService.savePlant(plant);
        return "redirect:/myplants";
    }
}
