package com.corefiling.labs.digit.analysis;

import java.util.List;
import java.util.UUID;

/** Obtains the facts from a filing ID. */
public interface FactRequester {

  List<NumericFactValue> getFacts(UUID filingVersionId);

}
