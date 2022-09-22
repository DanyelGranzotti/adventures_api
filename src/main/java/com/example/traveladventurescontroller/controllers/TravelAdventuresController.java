package com.example.traveladventurescontroller.controllers;

import com.example.traveladventurescontroller.models.Adventure;
import com.example.traveladventurescontroller.repositories.AdventureRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/traveladventures")
public class TravelAdventuresController {
    @Autowired
    private AdventureRepository adventureRepository;

    public TravelAdventuresController(AdventureRepository adventureRepository) {
        this.adventureRepository = adventureRepository;
    }
    @PostMapping
    public ResponseEntity<Adventure> saveAdventure(@RequestBody Adventure adventure) {
        Adventure adv = new Adventure();
        BeanUtils.copyProperties(adventure, adv);
        return ResponseEntity.created(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/traveladventures")
                .buildAndExpand(adventure.getId())
                .toUri()).body(adventureRepository.save(adventure));
    }

    @GetMapping
    public ResponseEntity<Iterable> getAllAdventures() {
        return ResponseEntity.ok().body(adventureRepository.findAll());
    }

    @GetMapping("/bycountry/{country}")
    public ResponseEntity<List> getAdventuresByCountry(@PathVariable String country) {
        return ResponseEntity.ok().body(adventureRepository.findByCountry(country));
    }

    @GetMapping("/bystate")
    public ResponseEntity<List> getAdventuresByState(@RequestParam String state) {
        return ResponseEntity.ok().body(adventureRepository.findByState(state));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Adventure> updateAdventure(@PathVariable Integer id, @RequestBody Adventure adventure) {
        Optional<Adventure> adv = adventureRepository.findById(id);
        if (adv.isPresent()) {
            BeanUtils.copyProperties(adventure, adv.get(), "id");
            return ResponseEntity.ok().body(adventureRepository.save(adv.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Adventure> deleteAdventure(@PathVariable Integer id) {
        Optional<Adventure> adv = adventureRepository.findById(id);
        if (adv.isPresent()) {
            adventureRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
