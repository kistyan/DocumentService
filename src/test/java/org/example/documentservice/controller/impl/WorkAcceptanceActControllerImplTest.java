package org.example.documentservice.controller.impl;

import org.example.documentservice.dto.DocumentReference;
import org.example.documentservice.dto.OrganizationInfo;
import org.example.documentservice.dto.Person;
import org.example.documentservice.dto.WorkTable;
import org.example.documentservice.dto.WorkTableRow;
import org.example.documentservice.dto.WorkTableStage;
import org.example.documentservice.dto.request.WorkAcceptanceActRequest;
import org.example.documentservice.dto.response.WorkAcceptanceActResponse;
import org.example.documentservice.enums.Gender;
import org.example.documentservice.exception.InternalServerErrorException;
import org.example.documentservice.service.WorkAcceptanceActService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkAcceptanceActControllerImpl.class)
public class WorkAcceptanceActControllerImplTest {
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
  private static final WorkAcceptanceActResponse ACT_RESPONSE = WorkAcceptanceActResponse.builder()
      .id(UUID.fromString("f999808d-a0d0-4594-8363-f52ba67d60d1"))
      .number("1")
      .build();

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private WorkAcceptanceActService workAcceptanceActService;

  @Test
  void generate_shouldReturn200() throws Exception {
    when(workAcceptanceActService.generate(any())).thenReturn(ACT_RESPONSE);

    mockMvc.perform(post("/api/v1/document-service/work-acceptance-acts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(ACT_REQUEST)))
        .andExpect(status().isOk());
  }

  @Test
  void generate_shouldReturn500_whenInternalServerError() throws Exception {
    when(workAcceptanceActService.generate(any()))
        .thenThrow(new InternalServerErrorException("Internal server error"));

    mockMvc.perform(post("/api/v1/document-service/work-acceptance-acts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(ACT_REQUEST)))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string("Internal server error"));
  }

  @Test
  void generate_shouldReturn500_whenUnexpectedException() throws Exception {
    when(workAcceptanceActService.generate(any()))
        .thenThrow(new RuntimeException("Unexpected exception"));

    mockMvc.perform(post("/api/v1/document-service/work-acceptance-acts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(ACT_REQUEST)))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string("Unexpected exception"));
  }
}
