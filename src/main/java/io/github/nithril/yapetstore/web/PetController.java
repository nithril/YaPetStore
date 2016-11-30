package io.github.nithril.yapetstore.web;

import static io.github.nithril.yapetstore.security.SecurityConfiguration.ROLE_ADMIN;
import static io.github.nithril.yapetstore.security.SecurityConfiguration.ROLE_USER;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.status;

import io.github.nithril.yapetstore.domain.Pet;
import io.github.nithril.yapetstore.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


/**
 * Created by nlabrot on 21/11/16.
 */
@RestController
public class PetController {

  @Autowired
  private PetRepository petRepository;

  @Secured({ROLE_ADMIN, ROLE_USER})
  @GetMapping(value = "/api/pets")
  public List<Pet> findAll() {
    return petRepository.findAll();
  }

  @Secured({ROLE_ADMIN, ROLE_USER})
  @GetMapping(value = "/api/pets/{id}")
  public ResponseEntity findById(@PathVariable("id") Long id) {
    return petRepository.findById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> status(NOT_FOUND).body(null));
  }

  @Secured(ROLE_ADMIN)
  @PostMapping(value = "/api/pets")
  public ResponseEntity create(@RequestBody Pet pet) throws URISyntaxException {
    Pet createdPet = petRepository.create(pet);
    return ResponseEntity.created(new URI("/api/pets/" + createdPet.getId())).build();
  }

  @Secured(ROLE_ADMIN)
  @DeleteMapping(value = "/api/pets/{id}")
  public ResponseEntity delete(@PathVariable("id") Long id) {
    if (petRepository.delete(id) != 0) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
