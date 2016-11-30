package io.github.nithril.yapetstore.repository;

import static io.github.nithril.yapetstore.domain.ImmutablePet.newPet;

import io.github.nithril.yapetstore.AbstractTests;
import io.github.nithril.yapetstore.domain.Pet;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by nlabrot on 21/11/16.
 */
public class PetRepositoryTests extends AbstractTests {

  @Autowired
  private PetRepository petRepository;


  @Test
  public void testCreateAndFindById() throws Exception {
    Pet meow = petRepository.create(newPet().name("meow").description("description").build());
    petRepository.findById(meow.getId()).get().equals(meow);
  }


  @Test
  public void testDelete() throws Exception {
    Pet meow = petRepository.create(newPet().name("meow").description("description").build());
    Assert.assertEquals(1 , petRepository.delete(meow.getId()));
    Assert.assertFalse(petRepository.findById(meow.getId()).isPresent());
  }


  @Test
  public void testFindAll() throws Exception {
    List<Pet> pets = petRepository.findAll();
    Assert.assertEquals(12 , pets.size());
    Assert.assertEquals("Cupcake" , pets.get(0).getName());
  }
}


