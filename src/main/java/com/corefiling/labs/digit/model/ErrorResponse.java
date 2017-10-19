package com.corefiling.labs.digit.model;

import java.util.Objects;
import java.util.Optional;

import com.google.common.base.Preconditions;

/**
 * ErrorResponse.
 */
public class ErrorResponse {
  private Optional<String> _error = Optional.empty();
  private Optional<String> _exception = Optional.empty();
  private Optional<String> _message = Optional.empty();
  private Optional<String> _path = Optional.empty();
  private Optional<Integer> _status = Optional.empty();
  private Optional<Long> _timestamp = Optional.empty();

  public ErrorResponse error(final Optional<String> error) {
    setError(error);
    return this;
  }

  /**
   * Error code.
   * @return error
   */
  public Optional<String> getError() {
    return _error;
  }

  public void setError(final Optional<String> error) {
    this._error = Preconditions.checkNotNull(error);
  }

  public ErrorResponse exception(final Optional<String> exception) {
    setException(exception);
    return this;
  }

  /**
   * Exception class.
   * @return exception
   */
  public Optional<String> getException() {
    return _exception;
  }

  public void setException(final Optional<String> exception) {
    this._exception = Preconditions.checkNotNull(exception);
  }

  public ErrorResponse message(final Optional<String> message) {
    setMessage(message);
    return this;
  }

  /**
   * Error message.
   * @return message
   */
  public Optional<String> getMessage() {
    return _message;
  }

  public void setMessage(final Optional<String> message) {
    this._message = Preconditions.checkNotNull(message);
  }

  public ErrorResponse path(final Optional<String> path) {
    setPath(path);
    return this;
  }

  /**
   * Request path.
   * @return path
   */
  public Optional<String> getPath() {
    return _path;
  }

  public void setPath(final Optional<String> path) {
    this._path = Preconditions.checkNotNull(path);
  }

  public ErrorResponse status(final Optional<Integer> status) {
    setStatus(status);
    return this;
  }

  /**
   * Status code.
   * @return status
   */
  public Optional<Integer> getStatus() {
    return _status;
  }

  public void setStatus(final Optional<Integer> status) {
    this._status = Preconditions.checkNotNull(status);
  }

  public ErrorResponse timestamp(final Optional<Long> timestamp) {
    setTimestamp(timestamp);
    return this;
  }

  /**
   * Timestamp (milliseconds).
   * @return timestamp
   */
  public Optional<Long> getTimestamp() {
    return _timestamp;
  }

  public void setTimestamp(final Optional<Long> timestamp) {
    this._timestamp = Preconditions.checkNotNull(timestamp);
  }


  @Override
  public boolean equals(final java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final ErrorResponse errorResponse = (ErrorResponse) o;
    return Objects.equals(this._error, errorResponse._error)
        && Objects.equals(this._exception, errorResponse._exception)
        && Objects.equals(this._message, errorResponse._message)
        && Objects.equals(this._path, errorResponse._path)
        && Objects.equals(this._status, errorResponse._status)
        && Objects.equals(this._timestamp, errorResponse._timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_error, _exception, _message, _path, _status, _timestamp);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("class ErrorResponse {\n");
    sb.append("    error: ").append(toIndentedString(_error)).append("\n");
    sb.append("    exception: ").append(toIndentedString(_exception)).append("\n");
    sb.append("    message: ").append(toIndentedString(_message)).append("\n");
    sb.append("    path: ").append(toIndentedString(_path)).append("\n");
    sb.append("    status: ").append(toIndentedString(_status)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(_timestamp)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(final java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}