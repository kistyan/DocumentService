package org.example.documentservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Sequence {
  WORK_ACCEPTANCE_ACT_NUMBER("work_acceptance_act_number");

  private final String name;
}