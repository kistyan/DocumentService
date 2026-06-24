package org.example.documentservice.utils;

import com.github.petrovich4j.Case;
import com.github.petrovich4j.Gender;
import com.github.petrovich4j.NameType;
import com.github.petrovich4j.Petrovich;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NameUtilsTest {
  @Mock
  private Petrovich petrovich;

  @InjectMocks
  private NameUtils nameUtils;

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
    boolean isMale = true;
    String expected = "Альшаева Константина Николаевича";

    Mockito.when(petrovich.say(lastName, NameType.LastName, Gender.Male, Case.Genitive))
        .thenReturn("Альшаева");
    Mockito.when(petrovich.say(firstName, NameType.FirstName, Gender.Male, Case.Genitive))
        .thenReturn("Константина");
    Mockito.when(petrovich.say(patronymic, NameType.PatronymicName, Gender.Male, Case.Genitive))
        .thenReturn("Николаевича");

    String actual = nameUtils.getGenitiveFullName(
        firstName,
        lastName,
        patronymic,
        isMale
    );

    assertEquals(expected, actual);
  }

  @Test
  void getGenitiveFullName_WhenNoPatronymic() {
    String lastName = "Альшаев", firstName = "Константин", patronymic = null;
    boolean isMale = true;
    String expected = "Альшаева Константина";

    Mockito.when(petrovich.say(lastName, NameType.LastName, Gender.Male, Case.Genitive))
        .thenReturn("Альшаева");
    Mockito.when(petrovich.say(firstName, NameType.FirstName, Gender.Male, Case.Genitive))
        .thenReturn("Константина");

    String actual = nameUtils.getGenitiveFullName(
        firstName,
        lastName,
        patronymic,
        isMale
    );

    assertEquals(expected, actual);
  }
}