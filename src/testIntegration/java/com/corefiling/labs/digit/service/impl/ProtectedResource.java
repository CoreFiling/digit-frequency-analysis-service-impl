package com.corefiling.labs.digit.service.impl;

import java.util.Set;

/***/
public interface ProtectedResource {

  String getName();

  String getURI() throws Exception;

  String getType();

  Set<String> getScopes();
}
