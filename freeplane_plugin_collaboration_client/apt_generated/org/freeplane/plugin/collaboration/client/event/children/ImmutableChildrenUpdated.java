package org.freeplane.plugin.collaboration.client.event.children;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.annotation.Generated;
import org.freeplane.plugin.collaboration.client.event.NodeUpdated;

/**
 * Immutable implementation of {@link ChildrenUpdated}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableChildrenUpdated.builder()}.
 */
@SuppressWarnings("all")
@Generated({"Immutables.generator", "ChildrenUpdated"})
public final class ImmutableChildrenUpdated
    implements ChildrenUpdated {
  private final List<String> content;
  private final String nodeId;

  private ImmutableChildrenUpdated(List<String> content, String nodeId) {
    this.content = content;
    this.nodeId = nodeId;
  }

  /**
   * @return The value of the {@code content} attribute
   */
  @JsonProperty("content")
  @Override
  public List<String> content() {
    return content;
  }

  /**
   * @return The value of the {@code nodeId} attribute
   */
  @JsonProperty("nodeId")
  @Override
  public String nodeId() {
    return nodeId;
  }

  /**
   * Copy the current immutable object with elements that replace the content of {@link ChildrenUpdated#content() content}.
   * @param elements The elements to set
   * @return A modified copy of {@code this} object
   */
  public final ImmutableChildrenUpdated withContent(String... elements) {
    List<String> newValue = createUnmodifiableList(false, createSafeList(Arrays.asList(elements), true, false));
    return new ImmutableChildrenUpdated(newValue, this.nodeId);
  }

  /**
   * Copy the current immutable object with elements that replace the content of {@link ChildrenUpdated#content() content}.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param elements An iterable of content elements to set
   * @return A modified copy of {@code this} object
   */
  public final ImmutableChildrenUpdated withContent(Iterable<String> elements) {
    if (this.content == elements) return this;
    List<String> newValue = createUnmodifiableList(false, createSafeList(elements, true, false));
    return new ImmutableChildrenUpdated(newValue, this.nodeId);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link ChildrenUpdated#nodeId() nodeId} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param nodeId A new value for nodeId
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableChildrenUpdated withNodeId(String nodeId) {
    if (this.nodeId.equals(nodeId)) return this;
    String newValue = Objects.requireNonNull(nodeId, "nodeId");
    return new ImmutableChildrenUpdated(this.content, newValue);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableChildrenUpdated} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(Object another) {
    if (this == another) return true;
    return another instanceof ImmutableChildrenUpdated
        && equalTo((ImmutableChildrenUpdated) another);
  }

  private boolean equalTo(ImmutableChildrenUpdated another) {
    return content.equals(another.content)
        && nodeId.equals(another.nodeId);
  }

  /**
   * Computes a hash code from attributes: {@code content}, {@code nodeId}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 31;
    h = h * 17 + content.hashCode();
    h = h * 17 + nodeId.hashCode();
    return h;
  }

  /**
   * Prints the immutable value {@code ChildrenUpdated} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return "ChildrenUpdated{"
        + "content=" + content
        + ", nodeId=" + nodeId
        + "}";
  }

  /**
   * Utility type used to correctly read immutable object from JSON representation.
   * @deprecated Do not use this type directly, it exists only for the <em>Jackson</em>-binding infrastructure
   */
  @Deprecated
  @JsonDeserialize
  @JsonTypeInfo(use=JsonTypeInfo.Id.NONE)
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
  static final class Json implements ChildrenUpdated {
    List<String> content = Collections.emptyList();
    String nodeId;
    @JsonProperty("content")
    public void setContent(List<String> content) {
      this.content = content;
    }
    @JsonProperty("nodeId")
    public void setNodeId(String nodeId) {
      this.nodeId = nodeId;
    }
    @Override
    public List<String> content() { throw new UnsupportedOperationException(); }
    @Override
    public String nodeId() { throw new UnsupportedOperationException(); }
  }

  /**
   * @param json A JSON-bindable data structure
   * @return An immutable value type
   * @deprecated Do not use this method directly, it exists only for the <em>Jackson</em>-binding infrastructure
   */
  @Deprecated
  @JsonCreator
  static ImmutableChildrenUpdated fromJson(Json json) {
    ImmutableChildrenUpdated.Builder builder = ImmutableChildrenUpdated.builder();
    if (json.content != null) {
      builder.addAllContent(json.content);
    }
    if (json.nodeId != null) {
      builder.nodeId(json.nodeId);
    }
    return builder.build();
  }

  /**
   * Creates an immutable copy of a {@link ChildrenUpdated} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable ChildrenUpdated instance
   */
  public static ImmutableChildrenUpdated copyOf(ChildrenUpdated instance) {
    if (instance instanceof ImmutableChildrenUpdated) {
      return (ImmutableChildrenUpdated) instance;
    }
    return ImmutableChildrenUpdated.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableChildrenUpdated ImmutableChildrenUpdated}.
   * @return A new ImmutableChildrenUpdated builder
   */
  public static ImmutableChildrenUpdated.Builder builder() {
    return new ImmutableChildrenUpdated.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableChildrenUpdated ImmutableChildrenUpdated}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  public static final class Builder {
    private static final long INIT_BIT_NODE_ID = 0x1L;
    private long initBits = 0x1L;

    private List<String> content = new ArrayList<String>();
    private String nodeId;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code org.freeplane.plugin.collaboration.client.event.children.ChildrenUpdated} instance.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(ChildrenUpdated instance) {
      Objects.requireNonNull(instance, "instance");
      from((Object) instance);
      return this;
    }

    /**
     * Fill a builder with attribute values from the provided {@code org.freeplane.plugin.collaboration.client.event.NodeUpdated} instance.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(NodeUpdated instance) {
      Objects.requireNonNull(instance, "instance");
      from((Object) instance);
      return this;
    }

    private void from(Object object) {
      if (object instanceof ChildrenUpdated) {
        ChildrenUpdated instance = (ChildrenUpdated) object;
        addAllContent(instance.content());
      }
      if (object instanceof NodeUpdated) {
        NodeUpdated instance = (NodeUpdated) object;
        nodeId(instance.nodeId());
      }
    }

    /**
     * Adds one element to {@link ChildrenUpdated#content() content} list.
     * @param element A content element
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder addContent(String element) {
      this.content.add(Objects.requireNonNull(element, "content element"));
      return this;
    }

    /**
     * Adds elements to {@link ChildrenUpdated#content() content} list.
     * @param elements An array of content elements
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder addContent(String... elements) {
      for (String element : elements) {
        this.content.add(Objects.requireNonNull(element, "content element"));
      }
      return this;
    }

    /**
     * Sets or replaces all elements for {@link ChildrenUpdated#content() content} list.
     * @param elements An iterable of content elements
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder content(Iterable<String> elements) {
      this.content.clear();
      return addAllContent(elements);
    }

    /**
     * Adds elements to {@link ChildrenUpdated#content() content} list.
     * @param elements An iterable of content elements
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder addAllContent(Iterable<String> elements) {
      for (String element : elements) {
        this.content.add(Objects.requireNonNull(element, "content element"));
      }
      return this;
    }

    /**
     * Initializes the value for the {@link ChildrenUpdated#nodeId() nodeId} attribute.
     * @param nodeId The value for nodeId 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder nodeId(String nodeId) {
      this.nodeId = Objects.requireNonNull(nodeId, "nodeId");
      initBits &= ~INIT_BIT_NODE_ID;
      return this;
    }

    /**
     * Builds a new {@link ImmutableChildrenUpdated ImmutableChildrenUpdated}.
     * @return An immutable instance of ChildrenUpdated
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableChildrenUpdated build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new ImmutableChildrenUpdated(createUnmodifiableList(true, content), nodeId);
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<String>();
      if ((initBits & INIT_BIT_NODE_ID) != 0) attributes.add("nodeId");
      return "Cannot build ChildrenUpdated, some of required attributes are not set " + attributes;
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
