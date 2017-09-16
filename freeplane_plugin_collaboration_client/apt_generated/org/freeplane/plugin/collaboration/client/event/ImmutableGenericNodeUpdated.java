package org.freeplane.plugin.collaboration.client.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Generated;

/**
 * Immutable implementation of {@link GenericNodeUpdated}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableGenericNodeUpdated.builder()}.
 */
@SuppressWarnings("all")
@Generated({"Immutables.generator", "GenericNodeUpdated"})
public final class ImmutableGenericNodeUpdated
    implements GenericNodeUpdated {
  private final String contentType;
  private final JsonNode content;
  private final String nodeId;

  private ImmutableGenericNodeUpdated(
      String contentType,
      JsonNode content,
      String nodeId) {
    this.contentType = contentType;
    this.content = content;
    this.nodeId = nodeId;
  }

  /**
   * @return The value of the {@code contentType} attribute
   */
  @JsonProperty("contentType")
  @Override
  public String contentType() {
    return contentType;
  }

  /**
   * @return The value of the {@code content} attribute
   */
  @JsonProperty("content")
  @Override
  public JsonNode content() {
    return content;
  }

  /**
   * @return The value of the {@code nodeId} attribute
   */
  @JsonProperty("nodeId")
  @Override
  public Optional<String> nodeId() {
    return Optional.ofNullable(nodeId);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link GenericNodeUpdated#contentType() contentType} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param contentType A new value for contentType
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableGenericNodeUpdated withContentType(String contentType) {
    if (this.contentType.equals(contentType)) return this;
    String newValue = Objects.requireNonNull(contentType, "contentType");
    return new ImmutableGenericNodeUpdated(newValue, this.content, this.nodeId);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link GenericNodeUpdated#content() content} attribute.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param content A new value for content
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableGenericNodeUpdated withContent(JsonNode content) {
    if (this.content == content) return this;
    JsonNode newValue = Objects.requireNonNull(content, "content");
    return new ImmutableGenericNodeUpdated(this.contentType, newValue, this.nodeId);
  }

  /**
   * Copy the current immutable object by setting a <i>present</i> value for the optional {@link GenericNodeUpdated#nodeId() nodeId} attribute.
   * @param value The value for nodeId
   * @return A modified copy of {@code this} object
   */
  public final ImmutableGenericNodeUpdated withNodeId(String value) {
    String newValue = Objects.requireNonNull(value, "nodeId");
    if (Objects.equals(this.nodeId, newValue)) return this;
    return new ImmutableGenericNodeUpdated(this.contentType, this.content, newValue);
  }

  /**
   * Copy the current immutable object by setting an optional value for the {@link GenericNodeUpdated#nodeId() nodeId} attribute.
   * An equality check is used on inner nullable value to prevent copying of the same value by returning {@code this}.
   * @param optional A value for nodeId
   * @return A modified copy of {@code this} object
   */
  public final ImmutableGenericNodeUpdated withNodeId(Optional<String> optional) {
    String value = optional.orElse(null);
    if (Objects.equals(this.nodeId, value)) return this;
    return new ImmutableGenericNodeUpdated(this.contentType, this.content, value);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableGenericNodeUpdated} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(Object another) {
    if (this == another) return true;
    return another instanceof ImmutableGenericNodeUpdated
        && equalTo((ImmutableGenericNodeUpdated) another);
  }

  private boolean equalTo(ImmutableGenericNodeUpdated another) {
    return contentType.equals(another.contentType)
        && content.equals(another.content)
        && Objects.equals(nodeId, another.nodeId);
  }

  /**
   * Computes a hash code from attributes: {@code contentType}, {@code content}, {@code nodeId}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 31;
    h = h * 17 + contentType.hashCode();
    h = h * 17 + content.hashCode();
    h = h * 17 + Objects.hashCode(nodeId);
    return h;
  }

  /**
   * Prints the immutable value {@code GenericNodeUpdated} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("GenericNodeUpdated{");
    builder.append("contentType=").append(contentType);
    builder.append(", ");
    builder.append("content=").append(content);
    if (nodeId != null) {
      builder.append(", ");
      builder.append("nodeId=").append(nodeId);
    }
    return builder.append("}").toString();
  }

  /**
   * Utility type used to correctly read immutable object from JSON representation.
   * @deprecated Do not use this type directly, it exists only for the <em>Jackson</em>-binding infrastructure
   */
  @Deprecated
  @JsonDeserialize
  @JsonTypeInfo(use=JsonTypeInfo.Id.NONE)
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
  static final class Json implements GenericNodeUpdated {
    String contentType;
    JsonNode content;
    Optional<String> nodeId = Optional.empty();
    @JsonProperty("contentType")
    public void setContentType(String contentType) {
      this.contentType = contentType;
    }
    @JsonProperty("content")
    public void setContent(JsonNode content) {
      this.content = content;
    }
    @JsonProperty("nodeId")
    public void setNodeId(Optional<String> nodeId) {
      this.nodeId = nodeId;
    }
    @Override
    public String contentType() { throw new UnsupportedOperationException(); }
    @Override
    public JsonNode content() { throw new UnsupportedOperationException(); }
    @Override
    public Optional<String> nodeId() { throw new UnsupportedOperationException(); }
  }

  /**
   * @param json A JSON-bindable data structure
   * @return An immutable value type
   * @deprecated Do not use this method directly, it exists only for the <em>Jackson</em>-binding infrastructure
   */
  @Deprecated
  @JsonCreator
  static ImmutableGenericNodeUpdated fromJson(Json json) {
    ImmutableGenericNodeUpdated.Builder builder = ImmutableGenericNodeUpdated.builder();
    if (json.contentType != null) {
      builder.contentType(json.contentType);
    }
    if (json.content != null) {
      builder.content(json.content);
    }
    if (json.nodeId != null) {
      builder.nodeId(json.nodeId);
    }
    return builder.build();
  }

  /**
   * Creates an immutable copy of a {@link GenericNodeUpdated} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable GenericNodeUpdated instance
   */
  public static ImmutableGenericNodeUpdated copyOf(GenericNodeUpdated instance) {
    if (instance instanceof ImmutableGenericNodeUpdated) {
      return (ImmutableGenericNodeUpdated) instance;
    }
    return ImmutableGenericNodeUpdated.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableGenericNodeUpdated ImmutableGenericNodeUpdated}.
   * @return A new ImmutableGenericNodeUpdated builder
   */
  public static ImmutableGenericNodeUpdated.Builder builder() {
    return new ImmutableGenericNodeUpdated.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableGenericNodeUpdated ImmutableGenericNodeUpdated}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  public static final class Builder {
    private static final long INIT_BIT_CONTENT_TYPE = 0x1L;
    private static final long INIT_BIT_CONTENT = 0x2L;
    private long initBits = 0x3L;

    private String contentType;
    private JsonNode content;
    private String nodeId;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code GenericNodeUpdated} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(GenericNodeUpdated instance) {
      Objects.requireNonNull(instance, "instance");
      contentType(instance.contentType());
      content(instance.content());
      Optional<String> nodeIdOptional = instance.nodeId();
      if (nodeIdOptional.isPresent()) {
        nodeId(nodeIdOptional);
      }
      return this;
    }

    /**
     * Initializes the value for the {@link GenericNodeUpdated#contentType() contentType} attribute.
     * @param contentType The value for contentType 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder contentType(String contentType) {
      this.contentType = Objects.requireNonNull(contentType, "contentType");
      initBits &= ~INIT_BIT_CONTENT_TYPE;
      return this;
    }

    /**
     * Initializes the value for the {@link GenericNodeUpdated#content() content} attribute.
     * @param content The value for content 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder content(JsonNode content) {
      this.content = Objects.requireNonNull(content, "content");
      initBits &= ~INIT_BIT_CONTENT;
      return this;
    }

    /**
     * Initializes the optional value {@link GenericNodeUpdated#nodeId() nodeId} to nodeId.
     * @param nodeId The value for nodeId
     * @return {@code this} builder for chained invocation
     */
    public final Builder nodeId(String nodeId) {
      this.nodeId = Objects.requireNonNull(nodeId, "nodeId");
      return this;
    }

    /**
     * Initializes the optional value {@link GenericNodeUpdated#nodeId() nodeId} to nodeId.
     * @param nodeId The value for nodeId
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder nodeId(Optional<String> nodeId) {
      this.nodeId = nodeId.orElse(null);
      return this;
    }

    /**
     * Builds a new {@link ImmutableGenericNodeUpdated ImmutableGenericNodeUpdated}.
     * @return An immutable instance of GenericNodeUpdated
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableGenericNodeUpdated build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new ImmutableGenericNodeUpdated(contentType, content, nodeId);
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<String>();
      if ((initBits & INIT_BIT_CONTENT_TYPE) != 0) attributes.add("contentType");
      if ((initBits & INIT_BIT_CONTENT) != 0) attributes.add("content");
      return "Cannot build GenericNodeUpdated, some of required attributes are not set " + attributes;
    }
  }
}
