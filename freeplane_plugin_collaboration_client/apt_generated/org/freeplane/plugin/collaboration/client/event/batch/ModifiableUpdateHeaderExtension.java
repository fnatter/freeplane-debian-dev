package org.freeplane.plugin.collaboration.client.event.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Generated;

/**
 * A modifiable implementation of the {@link UpdateHeaderExtension UpdateHeaderExtension} type.
 * <p>Use the {@link #create()} static factory methods to create new instances.
 * <p><em>ModifiableUpdateHeaderExtension is not thread-safe</em>
 */
@SuppressWarnings("all")
@Generated({"Modifiables.generator", "UpdateHeaderExtension"})
public final class ModifiableUpdateHeaderExtension
    implements UpdateHeaderExtension {
  private static final long INIT_BIT_MAP_ID = 0x1L;
  private static final long INIT_BIT_MAP_REVISION = 0x2L;
  private long initBits = 0x3L;

  private String mapId;
  private long mapRevision;

  private ModifiableUpdateHeaderExtension() {}

  /**
   * Construct a modifiable instance of {@code UpdateHeaderExtension}.
   * @return A new modifiable instance
   */
  public static ModifiableUpdateHeaderExtension create() {
    return new ModifiableUpdateHeaderExtension();
  }

  /**
   * @return value of {@code mapId} attribute
   */
  @Override
  public final String mapId() {
    if (!mapIdIsSet()) {
      checkRequiredAttributes();
    }
    return mapId;
  }

  /**
   * @return value of {@code mapRevision} attribute
   */
  @Override
  public final long mapRevision() {
    if (!mapRevisionIsSet()) {
      checkRequiredAttributes();
    }
    return mapRevision;
  }

  /**
   * Clears the object by setting all attributes to their initial values.
   * @return {@code this} for use in a chained invocation
   */
  public ModifiableUpdateHeaderExtension clear() {
    initBits = 0x3L;
    mapId = null;
    mapRevision = 0;
    return this;
  }

  /**
   * Fill this modifiable instance with attribute values from the provided {@link UpdateHeaderExtension} instance.
   * Regular attribute values will be overridden, i.e. replaced with ones of an instance.
   * Any of the instance's absent optional values will not be copied (will not override current values).
   * @param instance The instance from which to copy values
   * @return {@code this} for use in a chained invocation
   */
  public ModifiableUpdateHeaderExtension from(UpdateHeaderExtension instance) {
    Objects.requireNonNull(instance, "instance");
    setMapId(instance.mapId());
    setMapRevision(instance.mapRevision());
    return this;
  }

  /**
   * Assigns a value to the {@code mapId} attribute.
   * @param mapId The value for mapId
   * @return {@code this} for use in a chained invocation
   */
  public ModifiableUpdateHeaderExtension setMapId(String mapId) {
    this.mapId = Objects.requireNonNull(mapId, "mapId");
    initBits &= ~INIT_BIT_MAP_ID;
    return this;
  }

  /**
   * Assigns a value to the {@code mapRevision} attribute.
   * @param mapRevision The value for mapRevision
   * @return {@code this} for use in a chained invocation
   */
  public ModifiableUpdateHeaderExtension setMapRevision(long mapRevision) {
    this.mapRevision = mapRevision;
    initBits &= ~INIT_BIT_MAP_REVISION;
    return this;
  }

  /**
   * Returns {@code true} if the required attribute {@code mapId} is set.
   * @return {@code true} if set
   */
  public final boolean mapIdIsSet() {
    return (initBits & INIT_BIT_MAP_ID) == 0;
  }

  /**
   * Returns {@code true} if the required attribute {@code mapRevision} is set.
   * @return {@code true} if set
   */
  public final boolean mapRevisionIsSet() {
    return (initBits & INIT_BIT_MAP_REVISION) == 0;
  }

  /**
   * Returns {@code true} if all required attributes are set, indicating that the object is initialized.
   * @return {@code true} if set
   */
  public final boolean isInitialized() {
    return initBits == 0;
  }

  private void checkRequiredAttributes() {
    if (!isInitialized()) {
      throw new IllegalStateException(formatRequiredAttributesMessage());
    }
  }

  private String formatRequiredAttributesMessage() {
    List<String> attributes = new ArrayList<String>();
    if (!mapIdIsSet()) attributes.add("mapId");
    if (!mapRevisionIsSet()) attributes.add("mapRevision");
    return "UpdateHeaderExtension in not initialized, some of the required attributes are not set " + attributes;
  }

  /**
   * This instance is equal to all instances of {@code ModifiableUpdateHeaderExtension} that have equal attribute values.
   * An uninitialized instance is equal only to itself.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(Object another) {
    if (this == another) return true;
    if (!(another instanceof ModifiableUpdateHeaderExtension)) return false;
    ModifiableUpdateHeaderExtension other = (ModifiableUpdateHeaderExtension) another;
    if (!isInitialized() || !other.isInitialized()) {
      return false;
    }
    return equalTo(other);
  }

  private boolean equalTo(ModifiableUpdateHeaderExtension another) {
    return mapId.equals(another.mapId)
        && mapRevision == another.mapRevision;
  }

  /**
   * Computes a hash code from attributes: {@code mapId}, {@code mapRevision}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 31;
    h = h * 17 + mapId.hashCode();
    h = h * 17 + (int) (mapRevision ^ (mapRevision >>> 32));
    return h;
  }

  /**
   * Generates a string representation of this {@code UpdateHeaderExtension}.
   * If uninitialized, some attribute values may appear as question marks.
   * @return A string representation
   */
  @Override
  public String toString() {
    return "ModifiableUpdateHeaderExtension{"
        + "mapId="  + (mapIdIsSet() ? mapId() : "?")
        + ", mapRevision="  + (mapRevisionIsSet() ? mapRevision() : "?")
        + "}";
  }
}
