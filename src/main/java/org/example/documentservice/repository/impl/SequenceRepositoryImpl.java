package org.example.documentservice.repository.impl;

import lombok.RequiredArgsConstructor;
import org.example.documentservice.enums.Sequence;
import org.example.documentservice.repository.SequenceRepository;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public abstract class SequenceRepositoryImpl implements SequenceRepository {
  private final JdbcTemplate jdbcTemplate;

  protected Long nextValue(Sequence sequence) {
    return jdbcTemplate.queryForObject(
        String.format("select nextval('%s')", sequence.getName()),
        Long.class
    );
  }
}