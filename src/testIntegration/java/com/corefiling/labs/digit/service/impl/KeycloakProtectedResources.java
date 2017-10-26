/*
 *  Copyright 2017 CoreFiling S.A.R.L.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.corefiling.labs.digit.service.impl;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

import java.util.Set;

import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.representation.RegistrationResponse;
import org.keycloak.authorization.client.representation.ResourceRepresentation;
import org.keycloak.authorization.client.representation.ScopeRepresentation;

/**
 * Adds protected resources to Keycloak.
 */
public class KeycloakProtectedResources {

  private final AuthzClient _client;

  /**
   * @param client The client that talks to Keycloak.
   */
  public KeycloakProtectedResources(final AuthzClient client) {
    _client = client;
  }

  /**
   * Save the resource in Keycloak.
   */
  public String save(final ProtectedResource resource) throws Exception {
    final ResourceRepresentation representation = makeResourceRepresentation(resource);
    final RegistrationResponse response = _client.protection().resource().create(representation);
    return response.getId();
  }

  /**
   * Delete the resource in Keycloak.
   */
  public void delete(final String id) throws Exception {
    _client.protection().resource().delete(id);
  }

  private ResourceRepresentation makeResourceRepresentation(final ProtectedResource resource) throws Exception {
    final String name = resource.getName();
    final String uri = resource.getURI();
    final String type = resource.getType();
    final Set<ScopeRepresentation> scopes = resource.getScopes().stream().map(ScopeRepresentation::new).collect(toImmutableSet());
    return new ResourceRepresentation(name, scopes, uri, type);
  }

}

