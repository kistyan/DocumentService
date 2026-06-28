package org.example.documentservice.utils.impl;

import com.github.petrovich4j.Case;
import com.github.petrovich4j.NameType;
import com.github.petrovich4j.Petrovich;
import org.example.documentservice.enums.Gender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NameUtilsImplTest {
  @Mock
  private Petrovich petrovich;

  @InjectMocks
  private NameUtilsImpl nameUtils;

  @ParameterizedTest
  @CsvSource(
      value = {
          "Константин,Альшаев,Николаевич,К. Н. Альшаев",
          "Константин,Альшаев,,К. Альшаев"
      },
      nullValues = ""
  )
  void getReducedFullName(String firstName, String lastName, String patronymic, String expected) {
    String actual = nameUtils.getReducedFullName(
        firstName,
        lastName,
        patronymic
    );

    assertEquals(expected, actual);
  }

  @Test
  void getGenitiveFullName_WhenHasPatronymic() {
    String lastName = "Альшаев", firstName = "Константин", patronymic = "Николаевич";
    Gender gender = Gender.MALE;
    com.github.petrovich4j.Gender genderValue = gender.getGender();
    String expected = "Альшаева Константина Николаевича";

    when(petrovich.say(lastName, NameType.LastName, genderValue, Case.Genitive))
        .thenReturn("Альшаева");
    when(petrovich.say(firstName, NameType.FirstName, genderValue, Case.Genitive))
        .thenReturn("Константина");
    when(petrovich.say(patronymic, NameType.PatronymicName, genderValue, Case.Genitive))
        .thenReturn("Николаевича");

    String actual = nameUtils.getGenitiveFullName(
        firstName,
        lastName,
        patronymic,
        gender
    );

    assertEquals(expected, actual);
  }

  @Test
  void getGenitiveFullName_WhenNoPatronymic() {
    String lastName = "Альшаев", firstName = "Константин", patronymic = null;
    Gender gender = Gender.MALE;
    com.github.petrovich4j.Gender genderValue = gender.getGender();
    String expected = "Альшаева Константина";

    when(petrovich.say(lastName, NameType.LastName, genderValue, Case.Genitive))
        .thenReturn("Альшаева");
    when(petrovich.say(firstName, NameType.FirstName, genderValue, Case.Genitive))
        .thenReturn("Константина");

    String actual = nameUtils.getGenitiveFullName(
        firstName,
        lastName,
        patronymic,
        gender
    );

    assertEquals(expected, actual);
  }
}