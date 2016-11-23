package io.github.nithril.yapetstore.security;

/**
 * Created by nlabrot on 21/11/16.
 */
public class SecretKey {

  private final byte[] key;

  public SecretKey(byte[] key) {
    this.key = key;
  }

  public byte[] getKey() {
    return key;
  }
}
