package org.example.documentservice.utils;

import com.github.petrovich4j.Case;
import com.github.petrovich4j.NameType;
import com.github.petrovich4j.Petrovich;
import lombok.RequiredArgsConstructor;
import org.example.documentservice.enums.Gender;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NameUtils {
  private final Petrovich petrovich;

  private static String getReducedName(String name) {
    return String.format("%s.", name.charAt(0));
  }

  public String getReducedFullName(String firstName, String lastName, String patronymic) {
    return getReducedName(firstName) + " " +
        Optional.ofNullable(patronymic)
            .map(name -> getReducedName(name) + " ")
            .orElse("") +
        lastName;
  }

  public String getGenitiveFullName(String firstName, String lastName, String patronymic, Gender gender) {
    com.github.petrovich4j.Gender genderValue = gender.getGender();
    return petrovich.say(lastName, NameType.LastName, genderValue, Case.Genitive) +
        " " + petrovich.say(firstName, NameType.FirstName, genderValue, Case.Genitive) +
        Optional.ofNullable(patronymic)
            .map(name -> " " + petrovich.say(name, NameType.PatronymicName, genderValue, Case.Genitive))
            .orElse("");
  }
}
