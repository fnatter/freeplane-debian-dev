package org.freeplane.plugin.collaboration.client.event.children;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Generated;
import org.freeplane.plugin.collaboration.client.event.NodeUpdated;

/**
 * Immutable implementation of {@link SpecialNodeTypeSet}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableSpecialNodeTypeSet.builder()}.
 */
@SuppressWarnings("all")
@Generated({"Immutables.generator", "SpecialNodeTypeSet"})
public final class ImmutableSpecialNodeTypeSet
    implements SpecialNodeTypeSet {
  private final SpecialNodeTypeSet.SpecialNodeType content;
  private final String nodeId;

  private ImmutableSpecialNodeTypeSet(
      SpecialNodeTypeSet.SpecialNodeType content,
      String nodeId) {
    this.content = content;
    this.nodeId = nodeId;
  }

  /**
   * @return The value of the {@code content} attribute
   */
  @JsonProperty("content")
  @Override
  public SpecialNodeTypeSet.SpecialNodeType content() {
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
   * Copy the current immutable object by setting a value for the {@link SpecialNodeTypeSet#content() content} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param content A new value for content
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableSpecialNodeTypeSet withContent(SpecialNodeTypeSet.SpecialNodeType content) {
    if (this.content == content) return this;
    SpecialNodeTypeSet.SpecialNodeType newValue = Objects.requireNonNull(content, "content");
    return new ImmutableSpecialNodeTypeSet(newValue, this.nodeId);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link SpecialNodeTypeSet#nodeId() nodeId} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param nodeId A new value for nodeId
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableSpecialNodeTypeSet withNodeId(String nodeId) {
    if (this.nodeId.equals(nodeId)) return this;
    String newValue = Objects.requireNonNull(nodeId, "nodeId");
    return new ImmutableSpecialNodeTypeSet(this.content, newValue);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableSpecialNodeTypeSet} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(Object another) {
    if (this == another) return true;
    return another instanceof ImmutableSpecialNodeTypeSet
        && equalTo((ImmutableSpecialNodeTypeSet) another);
  }

  private boolean equalTo(ImmutableSpecialNodeTypeSet another) {
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
   * Prints the immutable value {@code SpecialNodeTypeSet} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return "SpecialNodeTypeSet{"
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
  static final class Json
      implements SpecialNodeTypeSet {
    SpecialNodeTypeSet.SpecialNodeType content;
    String nodeId;
    @JsonProperty("content")
    public void setContent(SpecialNodeTypeSet.SpecialNodeType content) {
      this.content = content;
    }
    @JsonProperty("nodeId")
    public void setNodeId(String nodeId) {
      this.nodeId = nodeId;
    }
    @Override
    public SpecialNodeTypeSet.SpecialNodeType content() { throw new UnsupportedOperationException(); }
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
  static ImmutableSpecialNodeTypeSet fromJson(Json json) {
    ImmutableSpecialNodeTypeSet.Builder builder = ImmutableSpecialNodeTypeSet.builder();
    if (json.content != null) {
      builder.content(json.content);
    }
    if (json.nodeId != null) {
      builder.nodeId(json.nodeId);
    }
    return builder.build();
  }

  /**
   * Creates an immutable copy of a {@link SpecialNodeTypeSet} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable SpecialNodeTypeSet instance
   */
  public static ImmutableSpecialNodeTypeSet copyOf(SpecialNodeTypeSet instance) {
    if (instance instanceof ImmutableSpecialNodeTypeSet) {
      return (ImmutableSpecialNodeTypeSet) instance;
    }
    return ImmutableSpecialNodeTypeSet.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableSpecialNodeTypeSet ImmutableSpecialNodeTypeSet}.
   * @return A new ImmutableSpecialNodeTypeSet builder
   */
  public static ImmutableSpecialNodeTypeSet.Builder builder() {
    return new ImmutableSpecialNodeTypeSet.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableSpecialNodeTypeSet ImmutableSpecialNodeTypeSet}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  public static final class Builder {
    private static final long INIT_BIT_CONTENT = 0x1L;
    private static final long INIT_BIT_NODE_ID = 0x2L;
    private long initBits = 0x3L;

    private SpecialNodeTypeSet.SpecialNodeType content;
    private String nodeId;

    private Builder() {
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

    /**
     * Fill a builder with attribute values from the provided {@code org.freeplane.plugin.collaboration.client.event.children.SpecialNodeTypeSet} instance.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(SpecialNodeTypeSet instance) {
      Objects.requireNonNull(instance, "instance");
      from((Object) instance);
      return this;
    }

    private void from(Object object) {
      if (object instanceof NodeUpdated) {
        NodeUpdated instance = (NodeUpdated) object;
        nodeId(instance.nodeId());
      }
      if (object instanceof SpecialNodeTypeSet) {
        SpecialNodeTypeSet instance = (SpecialNodeTypeSet) object;
        content(instance.content());
      }
    }

    /**
     * Initializes the value for the {@link SpecialNodeTypeSet#content() content} attribute.
     * @param content The value for content 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder content(SpecialNodeTypeSet.SpecialNodeType content) {
      this.content = Objects.requireNonNull(content, "content");
      initBits &= ~INIT_BIT_CONTENT;
      return this;
    }

    /**
     * Initializes the value for the {@link SpecialNodeTypeSet#nodeId() nodeId} attribute.
     * @param nodeId The value for nodeId 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder nodeId(String nodeId) {
      this.nodeId = Objects.requireNonNull(nodeId, "nodeId");
      initBits &= ~INIT_BIT_NODE_ID;
      return this;
    }

    /**
     * Builds a new {@link ImmutableSpecialNodeTypeSet ImmutableSpecialNodeTypeSet}.
     * @return An immutable instance of SpecialNodeTypeSet
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableSpecialNodeTypeSet build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new ImmutableSpecialNodeTypeSet(content, nodeId);
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<String>();
      if ((initBits & INIT_BIT_CONTENT) != 0) attributes.add("content");
      if ((initBits & INIT_BIT_NODE_ID) != 0) attributes.add("nodeId");
      return "Cannot build SpecialNodeTypeSet, some of required attributes are not set " + attributes;
    }
  }
}
