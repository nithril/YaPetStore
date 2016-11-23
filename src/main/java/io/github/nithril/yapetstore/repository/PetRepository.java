package io.github.nithril.yapetstore.repository;

import static demo.dom.tables.Pet.PET;
import static io.github.nithril.yapetstore.domain.ImmutablePet.newPet;

import io.github.nithril.yapetstore.domain.ImmutablePet;
import io.github.nithril.yapetstore.domain.Pet;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Created by nlabrot on 21/11/16.
 */
@Service
@Transactional
public class PetRepository {

  private final Configuration configuration;

  @Autowired
  public PetRepository(DSLContext dslContext) {
    this.configuration = dslContext.configuration();
  }

  /**
   * Find all pet ordered by name
   *
   * @return
   */
  public List<Pet> findAll() {
    return DSL.using(configuration).selectFrom(PET)
        .orderBy(PET.NAME)
        .fetch().into(ImmutablePet.class);
  }

  /**
   * Find a pet by id
   *
   * @param id
   * @return
   */
  public Pet findById(Long id) {
    Objects.requireNonNull(id);

    return DSL.using(configuration).selectFrom(PET).where(PET.ID.eq(id))
        .fetchOptionalInto(ImmutablePet.class).orElse(null);
  }

  /**
   * Delete a pet by id
   *
   * @param id
   * @return
   */
  public int delete(Long id) {
    Objects.requireNonNull(id);

    return DSL.using(configuration).deleteFrom(PET).where(PET.ID.eq(id)).execute();
  }

  /**
   * Create a pet
   *
   * @param pet
   * @return The new pet created
   */
  public Pet create(Pet pet) {
    Objects.requireNonNull(pet);

    Long id = DSL.using(configuration).insertInto(PET, PET.NAME, PET.DESCRIPTION)
        .values(pet.getName(), pet.getDescription())
        .returning(PET.ID)
        .fetchOne().getId();
    return newPet().from(pet).id(id).build();
  }


}
