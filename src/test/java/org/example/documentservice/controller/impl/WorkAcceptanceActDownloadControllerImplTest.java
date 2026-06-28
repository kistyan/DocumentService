package org.example.documentservice.controller.impl;

import org.example.documentservice.dto.response.WorkAcceptanceActFileResponse;
import org.example.documentservice.exception.InternalServerErrorException;
import org.example.documentservice.exception.NotFoundException;
import org.example.documentservice.service.WorkAcceptanceActService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkAcceptanceActDownloadControllerImpl.class)
public class WorkAcceptanceActDownloadControllerImplTest {
  private static final UUID ACT_ID = UUID.fromString("f999808d-a0d0-4594-8363-f52ba67d60d1");
  private static final WorkAcceptanceActFileResponse ACT_FILE_RESPONSE = WorkAcceptanceActFileResponse.builder()
      .name("file.docx")
      .content("test".getBytes())
      .build();

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private WorkAcceptanceActService workAcceptanceActService;

  @Test
  void download_shouldReturn200() throws Exception {
    when(workAcceptanceActService.download(ACT_ID))
        .thenReturn(ACT_FILE_RESPONSE);

    mockMvc.perform(get("/api/v1/document-service/work-acceptance-acts/download/" + ACT_ID))
        .andExpect(status().isOk())
        .andExpect(header().exists("Content-Disposition"))
        .andExpect(content().bytes(ACT_FILE_RESPONSE.content()));
  }

  @Test
  void download_shouldReturn404_whenBaseExceptionNotFound() throws Exception {
    when(workAcceptanceActService.download(ACT_ID))
        .thenThrow(new NotFoundException("Not found"));

    mockMvc.perform(get("/api/v1/document-service/work-acceptance-acts/download/" + ACT_ID))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Not found"));
  }

  @Test
  void download_shouldReturn500_whenInternalServerError() throws Exception {
    when(workAcceptanceActService.download(ACT_ID))
        .thenThrow(new InternalServerErrorException("Internal server error"));

    mockMvc.perform(get("/api/v1/document-service/work-acceptance-acts/download/" + ACT_ID))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string("Internal server error"));
  }

  @Test
  void download_shouldReturn500_whenUnexpectedException() throws Exception {
    when(workAcceptanceActService.download(ACT_ID))
        .thenThrow(new RuntimeException("Unexpected exception"));

    mockMvc.perform(get("/api/v1/document-service/work-acceptance-acts/download/" + ACT_ID))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string("Unexpected exception"));
  }
}
