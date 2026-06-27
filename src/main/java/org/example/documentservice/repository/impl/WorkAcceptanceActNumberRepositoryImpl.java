package org.example.documentservice.repository.impl;

import org.example.documentservice.enums.Sequence;
import org.example.documentservice.repository.WorkAcceptanceActNumberRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WorkAcceptanceActNumberRepositoryImpl extends SequenceRepositoryImpl
    implements WorkAcceptanceActNumberRepository {
  public WorkAcceptanceActNumberRepositoryImpl(JdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
  }

  @Override
  public Long nextValue() {
    return super.nextValue(Sequence.WORK_ACCEPTANCE_ACT_NUMBER);
  }
}
