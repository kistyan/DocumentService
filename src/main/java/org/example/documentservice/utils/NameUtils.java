package org.example.documentservice.utils;

import org.example.documentservice.enums.Gender;

public interface NameUtils {
  String getReducedFullName(String firstName, String lastName, String patronymic);

  String getGenitiveFullName(String firstName, String lastName, String patronymic, Gender gender);
}
