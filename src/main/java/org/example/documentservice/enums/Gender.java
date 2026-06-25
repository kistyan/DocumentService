package org.example.documentservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.github.petrovich4j.Gender.Both;
import static com.github.petrovich4j.Gender.Female;
import static com.github.petrovich4j.Gender.Male;

@Getter
@RequiredArgsConstructor
public enum Gender {
  MALE(Male),
  FEMALE(Female),
  BOTH(Both);

  final com.github.petrovich4j.Gender gender;
}
