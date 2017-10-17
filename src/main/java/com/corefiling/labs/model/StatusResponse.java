package com.corefiling.labs.model;

import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.Preconditions;

/**
 * StatusResponse.
 */
public class StatusResponse {
  private String _apiVersion = null;
  private String _implVersion = null;
  private Optional<String> _message = Optional.empty();
  private String _name = null;
  private StatusEnum _status = null;

  /**
   * Service status.
   */
  public enum StatusEnum {
    OK("OK"),

    FAILED("FAILED");

    private final String _value;

    StatusEnum(final String value) {
      this._value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(_value);
    }

    @JsonCreator
    public static StatusEnum fromValue(final String text) {
      for (final StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b._value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  public StatusResponse apiVersion(final String apiVersion) {
    setApiVersion(apiVersion);
    return this;
  }

   /**
   * API version number.
   * @return apiVersion
  **/
  public String getApiVersion() {
    return _apiVersion;
  }

  public void setApiVersion(final String apiVersion) {
    this._apiVersion = Preconditions.checkNotNull(apiVersion);
  }

  public StatusResponse implVersion(final String implVersion) {
    setImplVersion(implVersion);
    return this;
  }

   /**
   * Implementation version number.
   * @return implVersion
  **/
  public String getImplVersion() {
    return _implVersion;
  }

  public void setImplVersion(final String implVersion) {
    this._implVersion = Preconditions.checkNotNull(implVersion);
  }

  public StatusResponse message(final Optional<String> message) {
    setMessage(message);
    return this;
  }

   /**
   * Failure message, if any.
   * @return message
  **/
  public Optional<String> getMessage() {
    return _message;
  }

  public void setMessage(final Optional<String> message) {
    this._message = Preconditions.checkNotNull(message);
  }

  public StatusResponse name(final String name) {
    setName(name);
    return this;
  }

   /**
   * Name of this service.
   * @return name
  **/
  public String getName() {
    return _name;
  }

  public void setName(final String name) {
    this._name = Preconditions.checkNotNull(name);
  }

  public StatusResponse status(final StatusEnum status) {
    setStatus(status);
    return this;
  }

   /**
   * Service status.
   * @return status
  **/
  public StatusEnum getStatus() {
    return _status;
  }

  public void setStatus(final StatusEnum status) {
    this._status = Preconditions.checkNotNull(status);
  }


  @Override
  public boolean equals(final java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final StatusResponse statusResponse = (StatusResponse) o;
    return Objects.equals(this._apiVersion, statusResponse._apiVersion)
        && Objects.equals(this._implVersion, statusResponse._implVersion)
        && Objects.equals(this._message, statusResponse._message)
        && Objects.equals(this._name, statusResponse._name)
        && Objects.equals(this._status, statusResponse._status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_apiVersion, _implVersion, _message, _name, _status);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("class StatusResponse {\n");
    sb.append("    apiVersion: ").append(toIndentedString(_apiVersion)).append("\n");
    sb.append("    implVersion: ").append(toIndentedString(_implVersion)).append("\n");
    sb.append("    message: ").append(toIndentedString(_message)).append("\n");
    sb.append("    name: ").append(toIndentedString(_name)).append("\n");
    sb.append("    status: ").append(toIndentedString(_status)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(final Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}


