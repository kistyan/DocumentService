package org.example.documentservice.service.impl;

import org.example.documentservice.configuration.WorkAcceptanceActProperties;
import org.example.documentservice.dto.DocumentReference;
import org.example.documentservice.dto.OrganizationInfo;
import org.example.documentservice.dto.Person;
import org.example.documentservice.dto.WorkTable;
import org.example.documentservice.dto.WorkTableRow;
import org.example.documentservice.dto.WorkTableStage;
import org.example.documentservice.dto.request.WorkAcceptanceActRequest;
import org.example.documentservice.dto.response.WorkAcceptanceActFileResponse;
import org.example.documentservice.enums.Gender;
import org.example.documentservice.exception.WorkAcceptanceActNotFoundException;
import org.example.documentservice.mapper.WorkAcceptanceActMapper;
import org.example.documentservice.model.Document;
import org.example.documentservice.model.Template;
import org.example.documentservice.model.WorkAcceptanceAct;
import org.example.documentservice.repository.WorkAcceptanceActNumberRepository;
import org.example.documentservice.repository.WorkAcceptanceActRepository;
import org.example.documentservice.service.DocumentService;
import org.example.documentservice.utils.NameUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkAcceptanceActImplTest {
  private static final ZoneId ZONE_ID = ZoneId.of("Europe/Moscow");
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
  private static final Template TEMPLATE = Template.builder()
      .name("template")
      .path("template.docx")
      .build();
  private static final Document DOCUMENT = Document.builder()
      .id(UUID.fromString("588f8e09-6a3d-4454-97b1-4f3f6d2c71be"))
      .template(TEMPLATE)
      .path("file.docx")
      .creationDate(LocalDate.EPOCH)
      .build();
  private static final Long ACT_NUMBER = 1L;
  private static final WorkAcceptanceAct ACT = WorkAcceptanceAct.builder()
      .id(UUID.fromString("f999808d-a0d0-4594-8363-f52ba67d60d1"))
      .document(DOCUMENT)
      .number(ACT_NUMBER.toString())
      .build();
  private static final WorkAcceptanceActProperties ACT_PROPERTIES = WorkAcceptanceActProperties.builder()
      .listDelimiter(";")
      .templateName(TEMPLATE.getName())
      .nameFormat("%s.docx")
      .build();
  private static final WorkAcceptanceActRequest ACT_REQUEST = WorkAcceptanceActRequest.builder()
      .contract(DocumentReference.builder()
          .number("ABC")
          .date(LocalDate.of(2022, 1, 10))
          .build())
      .request(DocumentReference.builder()
          .number("16222")
          .date(LocalDate.of(2025, 5, 1))
          .build())
      .customer(OrganizationInfo.builder()
          .name("Ромашка")
          .tin(3344667788L)
          .director(Person.builder()
              .firstName("Иван")
              .lastName("Кураков")
              .patronymic("Иванович")
              .gender(Gender.MALE)
              .build())
          .build())
      .contractor(OrganizationInfo.builder()
          .name("Петрушка")
          .tin(3344667783L)
          .director(Person.builder()
              .firstName("Геннадий")
              .lastName("Геннадьев")
              .patronymic("Геннадиевич")
              .gender(Gender.MALE)
              .build())
          .build())
      .product("Классная система")
      .startDate(LocalDate.of(2025, 5, 1))
      .endDate(LocalDate.of(2025, 5, 31))
      .works(List.of(
          "Сделал экран",
          "Сделал кнопку",
          "Сделал окно"
      ))
      .vatPercent(BigDecimal.valueOf(5))
      .workTable(WorkTable.builder()
          .stages(List.of(
              WorkTableStage.builder()
                  .rows(List.of(
                      WorkTableRow.builder()
                          .work("Выполнение разработки/модификации Систем в части back-end (Java)")
                          .rate(BigDecimal.valueOf(1200))
                          .hours(175)
                          .startDate(LocalDate.of(2025, 4, 1))
                          .endDate(LocalDate.of(2025, 4, 30))
                          .build(),
                      WorkTableRow.builder()
                          .work("Выполнение разработки/модификации Систем в части back-end (Java)")
                          .rate(BigDecimal.valueOf(1200))
                          .hours(144)
                          .startDate(LocalDate.of(2025, 5, 1))
                          .endDate(LocalDate.of(2025, 5, 31))
                          .build(),
                      WorkTableRow.builder()
                          .work("Выполнение разработки/модификации Систем в части back-end (Java)")
                          .rate(BigDecimal.valueOf(1200))
                          .hours(151)
                          .startDate(LocalDate.of(2025, 6, 1))
                          .endDate(LocalDate.of(2025, 6, 30))
                          .build()
                  ))
                  .build(),
              WorkTableStage.builder()
                  .rows(List.of(
                      WorkTableRow.builder()
                          .work("Выполнение разработки/модификации Систем в части back-end (Java)")
                          .rate(BigDecimal.valueOf(1200))
                          .hours(175)
                          .startDate(LocalDate.of(2025, 4, 1))
                          .endDate(LocalDate.of(2025, 4, 30))
                          .build(),
                      WorkTableRow.builder()
                          .work("Выполнение разработки/модификации Систем в части back-end (Java)")
                          .rate(BigDecimal.valueOf(1200))
                          .hours(144)
                          .startDate(LocalDate.of(2025, 5, 1))
                          .endDate(LocalDate.of(2025, 5, 31))
                          .build()
                  ))
                  .build()
          ))
          .build())
      .build();
  private static final Map<String, Object> DATA = Map.ofEntries(
      Map.entry("act_number", ACT_NUMBER.toString()),

      Map.entry("contract_number", "ABC"),
      Map.entry("contract_date", LocalDate.of(2022, 1, 10).format(DATE_FORMATTER)),

      Map.entry("request_number", "16222"),
      Map.entry("request_date", LocalDate.of(2025, 5, 1).format(DATE_FORMATTER)),

      Map.entry("customer_name", "Ромашка"),
      Map.entry("customer_tin", 3344667788L),
      Map.entry("customer_director", "Куракова Ивана Ивановича"),
      Map.entry("customer_director_short", "И. И. Кураков"),

      Map.entry("contractor_name", "Петрушка"),
      Map.entry("contractor_tin", 3344667783L),
      Map.entry("contractor_director", "Геннадьева Геннадия Геннадиевича"),
      Map.entry("contractor_director_short", "Г. Г. Геннадьев"),

      Map.entry("product", "Классная система"),

      Map.entry("start_date", LocalDate.of(2025, 5, 1).format(DATE_FORMATTER)),
      Map.entry("end_date", LocalDate.of(2025, 5, 31).format(DATE_FORMATTER)),

      Map.entry("works", List.of(
          "Сделал экран;",
          "Сделал кнопку;",
          "Сделал окно."
      )),

      Map.entry("vat_percent", BigDecimal.valueOf(5)),

      Map.entry("total_amount", BigDecimal.valueOf(946800)),
      Map.entry("vat_amount", BigDecimal.valueOf(47340)),
      Map.entry("total_with_vat", BigDecimal.valueOf(994140)),

      Map.entry("stages", List.of(

          Map.ofEntries(
              Map.entry("stage_number", 1),
              Map.entry("stage_total_amount", BigDecimal.valueOf(564000)),
              Map.entry("rows", List.of(

                  Map.ofEntries(
                      Map.entry("number", 1),
                      Map.entry("work", "Выполнение разработки/модификации Систем в части back-end (Java)"),
                      Map.entry("rate", BigDecimal.valueOf(1200)),
                      Map.entry("hours", 175),
                      Map.entry("start_date", LocalDate.of(2025, 4, 1).format(DATE_FORMATTER)),
                      Map.entry("end_date", LocalDate.of(2025, 4, 30).format(DATE_FORMATTER)),
                      Map.entry("amount", BigDecimal.valueOf(210000))
                  ),

                  Map.ofEntries(
                      Map.entry("number", 2),
                      Map.entry("work", "Выполнение разработки/модификации Систем в части back-end (Java)"),
                      Map.entry("rate", BigDecimal.valueOf(1200)),
                      Map.entry("hours", 144),
                      Map.entry("start_date", LocalDate.of(2025, 5, 1).format(DATE_FORMATTER)),
                      Map.entry("end_date", LocalDate.of(2025, 5, 31).format(DATE_FORMATTER)),
                      Map.entry("amount", BigDecimal.valueOf(172800))
                  ),

                  Map.ofEntries(
                      Map.entry("number", 3),
                      Map.entry("work", "Выполнение разработки/модификации Систем в части back-end (Java)"),
                      Map.entry("rate", BigDecimal.valueOf(1200)),
                      Map.entry("hours", 151),
                      Map.entry("start_date", LocalDate.of(2025, 6, 1).format(DATE_FORMATTER)),
                      Map.entry("end_date", LocalDate.of(2025, 6, 30).format(DATE_FORMATTER)),
                      Map.entry("amount", BigDecimal.valueOf(181200))
                  )
              ))
          ),

          Map.ofEntries(
              Map.entry("stage_number", 2),
              Map.entry("stage_total_amount", BigDecimal.valueOf(382800)),
              Map.entry("rows", List.of(

                  Map.ofEntries(
                      Map.entry("number", 1),
                      Map.entry("work", "Выполнение разработки/модификации Систем в части back-end (Java)"),
                      Map.entry("rate", BigDecimal.valueOf(1200)),
                      Map.entry("hours", 175),
                      Map.entry("start_date", LocalDate.of(2025, 4, 1).format(DATE_FORMATTER)),
                      Map.entry("end_date", LocalDate.of(2025, 4, 30).format(DATE_FORMATTER)),
                      Map.entry("amount", BigDecimal.valueOf(210000))
                  ),

                  Map.ofEntries(
                      Map.entry("number", 2),
                      Map.entry("work", "Выполнение разработки/модификации Систем в части back-end (Java)"),
                      Map.entry("rate", BigDecimal.valueOf(1200)),
                      Map.entry("hours", 144),
                      Map.entry("start_date", LocalDate.of(2025, 5, 1).format(DATE_FORMATTER)),
                      Map.entry("end_date", LocalDate.of(2025, 5, 31).format(DATE_FORMATTER)),
                      Map.entry("amount", BigDecimal.valueOf(172800))
                  )
              ))
          )
      ))
  );

  @Mock
  private DocumentService documentService;

  @Mock
  private WorkAcceptanceActRepository workAcceptanceActRepository;

  @Mock
  private WorkAcceptanceActNumberRepository workAcceptanceActNumberRepository;

  @Mock
  private WorkAcceptanceActMapper workAcceptanceActMapper;

  @Mock
  private NameUtils nameUtils;

  private WorkAcceptanceActServiceImpl workAcceptanceActServiceImpl;

  @BeforeEach
  void setUp() {
    workAcceptanceActServiceImpl = new WorkAcceptanceActServiceImpl(
        ACT_PROPERTIES,
        documentService,
        workAcceptanceActRepository,
        workAcceptanceActNumberRepository,
        workAcceptanceActMapper,
        nameUtils,
        ZONE_ID,
        DATE_FORMATTER
    );
  }

  @Test
  void generate_shouldGenerateAndSaveAct() {
    when(workAcceptanceActNumberRepository.nextValue()).thenReturn(ACT_NUMBER);

    when(nameUtils.getGenitiveFullName("Геннадий", "Геннадьев", "Геннадиевич", Gender.MALE))
        .thenReturn("Геннадьева Геннадия Геннадиевича");
    when(nameUtils.getGenitiveFullName("Иван", "Кураков", "Иванович", Gender.MALE))
        .thenReturn("Куракова Ивана Ивановича");

    when(nameUtils.getReducedFullName("Геннадий", "Геннадьев", "Геннадиевич"))
        .thenReturn("Г. Г. Геннадьев");
    when(nameUtils.getReducedFullName("Иван", "Кураков", "Иванович"))
        .thenReturn("И. И. Кураков");

    workAcceptanceActServiceImpl.generate(ACT_REQUEST);

    verify(workAcceptanceActNumberRepository).nextValue();
    ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
    verify(documentService).generate(eq(TEMPLATE.getName()), captor.capture());
    verify(workAcceptanceActRepository).save(any(WorkAcceptanceAct.class));
    verify(workAcceptanceActMapper).toResponse(any());

    Map<String, Object> data = captor.getValue();
    for (String key : data.keySet()) {
      if (key.equals("act_date")) {
        continue;
      }
      assertEquals(data.get(key), DATA.get(key));
    }
  }

  @Test
  void download_shouldReturnFileResponse() {
    byte[] content = "test".getBytes();

    when(workAcceptanceActRepository.findById(ACT.getId()))
        .thenReturn(Optional.of(ACT));

    when(documentService.download(any()))
        .thenReturn(content);

    WorkAcceptanceActFileResponse response = workAcceptanceActServiceImpl.download(ACT.getId());

    assertEquals(String.format(ACT_PROPERTIES.nameFormat(), ACT.getNumber()), response.name());
    assertArrayEquals(content, response.content());

    verify(workAcceptanceActRepository).findById(ACT.getId());
    verify(documentService).download(any());
  }

  @Test
  void download_shouldThrowWhenNotFound() {
    when(workAcceptanceActRepository.findById(ACT.getId()))
        .thenReturn(Optional.empty());

    assertThrows(WorkAcceptanceActNotFoundException.class,
        () -> workAcceptanceActServiceImpl.download(ACT.getId()));
  }
}