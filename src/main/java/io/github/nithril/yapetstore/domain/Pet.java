package io.github.nithril.yapetstore.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.annotation.Nullable;

/**
 * Created by nlabrot on 21/11/16.
 */
@Value.Immutable
@Value.Style(builder = "newPet")
@JsonSerialize(as = ImmutablePet.class)
@JsonDeserialize(as = ImmutablePet.class)
public interface Pet {

  @Nullable
  Long getId();

  String getName();

  String getDescription();
}
