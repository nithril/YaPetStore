package io.github.nithril.yapetstore.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.annotation.Nullable;

/**
 * Created by nlabrot on 21/11/16.
 */
@Value.Immutable
@Value.Style(builder = "newUser")
@JsonSerialize(as = ImmutableUser.class)
@JsonDeserialize(as = ImmutableUser.class)
public interface User {

  String getUsername();

  @Nullable
  String getPassword();
}
