package org.example.documentservice.utils;

import com.github.petrovich4j.Case;
import com.github.petrovich4j.Gender;
import com.github.petrovich4j.NameType;
import com.github.petrovich4j.Petrovich;
import lombok.RequiredArgsConstructor;
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

  public String getGenitiveFullName(String firstName, String lastName, String patronymic, boolean isMale) {
    Gender gender = isMale ? Gender.Male : Gender.Female;
    return petrovich.say(lastName, NameType.LastName, gender, Case.Genitive) +
        " " + petrovich.say(firstName, NameType.FirstName, gender, Case.Genitive) +
        Optional.ofNullable(patronymic)
            .map(name -> " " + petrovich.say(name, NameType.PatronymicName, gender, Case.Genitive))
            .orElse("");
  }
}
