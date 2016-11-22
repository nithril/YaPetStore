package io.github.nithril.yapetstore.security;

/**
 * Created by nlabrot on 21/11/16.
 */
public class SecretKey {
  private final byte[] value;

  public SecretKey(byte[] value) {
    this.value = value;
  }

  public byte[] getValue() {
    return value;
  }
}
