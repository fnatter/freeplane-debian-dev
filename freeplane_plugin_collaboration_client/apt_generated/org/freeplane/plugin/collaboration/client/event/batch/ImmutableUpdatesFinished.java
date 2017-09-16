package org.freeplane.plugin.collaboration.client.event.batch;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.annotation.Generated;
import org.freeplane.plugin.collaboration.client.event.MapUpdated;

/**
 * Immutable implementation of {@link UpdatesFinished}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableUpdatesFinished.builder()}.
 */
@SuppressWarnings("all")
@Generated({"Immutables.generator", "UpdatesFinished"})
public final class ImmutableUpdatesFinished
    implements UpdatesFinished {
  private final String mapId;
  private final long mapRevision;
  private final List<MapUpdated> updateEvents;

  private ImmutableUpdatesFinished(
      String mapId,
      long mapRevision,
      List<MapUpdated> updateEvents) {
    this.mapId = mapId;
    this.mapRevision = mapRevision;
    this.updateEvents = updateEvents;
  }

  /**
   * @return The value of the {@code mapId} attribute
   */
  @JsonProperty("mapId")
  @Override
  public String mapId() {
    return mapId;
  }

  /**
   * @return The value of the {@code mapRevision} attribute
   */
  @JsonProperty("mapRevision")
  @Override
  public long mapRevision() {
    return mapRevision;
  }

  /**
   * @return The value of the {@code updateEvents} attribute
   */
  @JsonProperty("updateEvents")
  @Override
  public List<MapUpdated> updateEvents() {
    return updateEvents;
  }

  /**
   * Copy the current immutable object by setting a value for the {@link UpdatesFinished#mapId() mapId} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param mapId A new value for mapId
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableUpdatesFinished withMapId(String mapId) {
    if (this.mapId.equals(mapId)) return this;
    String newValue = Objects.requireNonNull(mapId, "mapId");
    return new ImmutableUpdatesFinished(newValue, this.mapRevision, this.updateEvents);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link UpdatesFinished#mapRevision() mapRevision} attribute.
   * A value equality check is used to prevent copying of the same value by returning {@code this}.
   * @param mapRevision A new value for mapRevision
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableUpdatesFinished withMapRevision(long mapRevision) {
    if (this.mapRevision == mapRevision) return this;
    return new ImmutableUpdatesFinished(this.mapId, mapRevision, this.updateEvents);
  }

  /**
   * Copy the current immutable object with elements that replace the content of {@link UpdatesFinished#updateEvents() updateEvents}.
   * @param elements The elements to set
   * @return A modified copy of {@code this} object
   */
  public final ImmutableUpdatesFinished withUpdateEvents(MapUpdated... elements) {
    List<MapUpdated> newValue = createUnmodifiableList(false, createSafeList(Arrays.asList(elements), true, false));
    return new ImmutableUpdatesFinished(this.mapId, this.mapRevision, newValue);
  }

  /**
   * Copy the current immutable object with elements that replace the content of {@link UpdatesFinished#updateEvents() updateEvents}.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param elements An iterable of updateEvents elements to set
   * @return A modified copy of {@code this} object
   */
  public final ImmutableUpdatesFinished withUpdateEvents(Iterable<? extends MapUpdated> elements) {
    if (this.updateEvents == elements) return this;
    List<MapUpdated> newValue = createUnmodifiableList(false, createSafeList(elements, true, false));
    return new ImmutableUpdatesFinished(this.mapId, this.mapRevision, newValue);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableUpdatesFinished} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(Object another) {
    if (this == another) return true;
    return another instanceof ImmutableUpdatesFinished
        && equalTo((ImmutableUpdatesFinished) another);
  }

  private boolean equalTo(ImmutableUpdatesFinished another) {
    return mapId.equals(another.mapId)
        && mapRevision == another.mapRevision
        && updateEvents.equals(another.updateEvents);
  }

  /**
   * Computes a hash code from attributes: {@code mapId}, {@code mapRevision}, {@code updateEvents}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 31;
    h = h * 17 + mapId.hashCode();
    h = h * 17 + (int) (mapRevision ^ (mapRevision >>> 32));
    h = h * 17 + updateEvents.hashCode();
    return h;
  }

  /**
   * Prints the immutable value {@code UpdatesFinished} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return "UpdatesFinished{"
        + "mapId=" + mapId
        + ", mapRevision=" + mapRevision
        + ", updateEvents=" + updateEvents
        + "}";
  }

  /**
   * Utility type used to correctly read immutable object from JSON representation.
   * @deprecated Do not use this type directly, it exists only for the <em>Jackson</em>-binding infrastructure
   */
  @Deprecated
  @JsonDeserialize
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
  static final class Json implements UpdatesFinished {
    String mapId;
    long mapRevision;
    boolean mapRevisionIsSet;
    List<MapUpdated> updateEvents = Collections.emptyList();
    @JsonProperty("mapId")
    public void setMapId(String mapId) {
      this.mapId = mapId;
    }
    @JsonProperty("mapRevision")
    public void setMapRevision(long mapRevision) {
      this.mapRevision = mapRevision;
      this.mapRevisionIsSet = true;
    }
    @JsonProperty("updateEvents")
    public void setUpdateEvents(List<MapUpdated> updateEvents) {
      this.updateEvents = updateEvents;
    }
    @Override
    public String mapId() { throw new UnsupportedOperationException(); }
    @Override
    public long mapRevision() { throw new UnsupportedOperationException(); }
    @Override
    public List<MapUpdated> updateEvents() { throw new UnsupportedOperationException(); }
  }

  /**
   * @param json A JSON-bindable data structure
   * @return An immutable value type
   * @deprecated Do not use this method directly, it exists only for the <em>Jackson</em>-binding infrastructure
   */
  @Deprecated
  @JsonCreator
  static ImmutableUpdatesFinished fromJson(Json json) {
    ImmutableUpdatesFinished.Builder builder = ImmutableUpdatesFinished.builder();
    if (json.mapId != null) {
      builder.mapId(json.mapId);
    }
    if (json.mapRevisionIsSet) {
      builder.mapRevision(json.mapRevision);
    }
    if (json.updateEvents != null) {
      builder.addAllUpdateEvents(json.updateEvents);
    }
    return builder.build();
  }

  /**
   * Creates an immutable copy of a {@link UpdatesFinished} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable UpdatesFinished instance
   */
  public static ImmutableUpdatesFinished copyOf(UpdatesFinished instance) {
    if (instance instanceof ImmutableUpdatesFinished) {
      return (ImmutableUpdatesFinished) instance;
    }
    return ImmutableUpdatesFinished.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableUpdatesFinished ImmutableUpdatesFinished}.
   * @return A new ImmutableUpdatesFinished builder
   */
  public static ImmutableUpdatesFinished.Builder builder() {
    return new ImmutableUpdatesFinished.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableUpdatesFinished ImmutableUpdatesFinished}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  public static final class Builder {
    private static final long INIT_BIT_MAP_ID = 0x1L;
    private static final long INIT_BIT_MAP_REVISION = 0x2L;
    private long initBits = 0x3L;

    private String mapId;
    private long mapRevision;
    private List<MapUpdated> updateEvents = new ArrayList<MapUpdated>();

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code UpdatesFinished} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * Collection elements and entries will be added, not replaced.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(UpdatesFinished instance) {
      Objects.requireNonNull(instance, "instance");
      mapId(instance.mapId());
      mapRevision(instance.mapRevision());
      addAllUpdateEvents(instance.updateEvents());
      return this;
    }

    /**
     * Initializes the value for the {@link UpdatesFinished#mapId() mapId} attribute.
     * @param mapId The value for mapId 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder mapId(String mapId) {
      this.mapId = Objects.requireNonNull(mapId, "mapId");
      initBits &= ~INIT_BIT_MAP_ID;
      return this;
    }

    /**
     * Initializes the value for the {@link UpdatesFinished#mapRevision() mapRevision} attribute.
     * @param mapRevision The value for mapRevision 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder mapRevision(long mapRevision) {
      this.mapRevision = mapRevision;
      initBits &= ~INIT_BIT_MAP_REVISION;
      return this;
    }

    /**
     * Adds one element to {@link UpdatesFinished#updateEvents() updateEvents} list.
     * @param element A updateEvents element
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder addUpdateEvents(MapUpdated element) {
      this.updateEvents.add(Objects.requireNonNull(element, "updateEvents element"));
      return this;
    }

    /**
     * Adds elements to {@link UpdatesFinished#updateEvents() updateEvents} list.
     * @param elements An array of updateEvents elements
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder addUpdateEvents(MapUpdated... elements) {
      for (MapUpdated element : elements) {
        this.updateEvents.add(Objects.requireNonNull(element, "updateEvents element"));
      }
      return this;
    }

    /**
     * Sets or replaces all elements for {@link UpdatesFinished#updateEvents() updateEvents} list.
     * @param elements An iterable of updateEvents elements
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder updateEvents(Iterable<? extends MapUpdated> elements) {
      this.updateEvents.clear();
      return addAllUpdateEvents(elements);
    }

    /**
     * Adds elements to {@link UpdatesFinished#updateEvents() updateEvents} list.
     * @param elements An iterable of updateEvents elements
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder addAllUpdateEvents(Iterable<? extends MapUpdated> elements) {
      for (MapUpdated element : elements) {
        this.updateEvents.add(Objects.requireNonNull(element, "updateEvents element"));
      }
      return this;
    }

    /**
     * Builds a new {@link ImmutableUpdatesFinished ImmutableUpdatesFinished}.
     * @return An immutable instance of UpdatesFinished
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableUpdatesFinished build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new ImmutableUpdatesFinished(mapId, mapRevision, createUnmodifiableList(true, updateEvents));
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<String>();
      if ((initBits & INIT_BIT_MAP_ID) != 0) attributes.add("mapId");
      if ((initBits & INIT_BIT_MAP_REVISION) != 0) attributes.add("mapRevision");
      return "Cannot build UpdatesFinished, some of required attributes are not set " + attributes;
    }
  }

  private static <T> List<T> createSafeList(Iterable<? extends T> iterable, boolean checkNulls, boolean skipNulls) {
    ArrayList<T> list;
    if (iterable instanceof Collection<?>) {
      int size = ((Collection<?>) iterable).size();
      if (size == 0) return Collections.emptyList();
      list = new ArrayList<T>();
    } else {
      list = new ArrayList<T>();
    }
    for (T element : iterable) {
      if (skipNulls && element == null) continue;
      if (checkNulls) Objects.requireNonNull(element, "element");
      list.add(element);
    }
    return list;
  }

  private static <T> List<T> createUnmodifiableList(boolean clone, List<T> list) {
    switch(list.size()) {
    case 0: return Collections.emptyList();
    case 1: return Collections.singletonList(list.get(0));
    default:
      if (clone) {
        return Collections.unmodifiableList(new ArrayList<T>(list));
      } else {
        if (list instanceof ArrayList<?>) {
          ((ArrayList<?>) list).trimToSize();
        }
        return Collections.unmodifiableList(list);
      }
    }
  }
}
