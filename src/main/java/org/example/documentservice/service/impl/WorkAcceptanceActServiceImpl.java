package org.example.documentservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.documentservice.configuration.WorkAcceptanceActProperties;
import org.example.documentservice.dto.DocumentReference;
import org.example.documentservice.dto.OrganizationInfo;
import org.example.documentservice.dto.WorkTableRow;
import org.example.documentservice.dto.WorkTableStage;
import org.example.documentservice.dto.request.WorkAcceptanceActRequest;
import org.example.documentservice.dto.response.WorkAcceptanceActFileResponse;
import org.example.documentservice.dto.response.WorkAcceptanceActResponse;
import org.example.documentservice.exception.WorkAcceptanceActNotFoundException;
import org.example.documentservice.mapper.WorkAcceptanceActMapper;
import org.example.documentservice.model.Document;
import org.example.documentservice.model.Organization;
import org.example.documentservice.model.WorkAcceptanceAct;
import org.example.documentservice.repository.WorkAcceptanceActNumberRepository;
import org.example.documentservice.repository.WorkAcceptanceActRepository;
import org.example.documentservice.service.DocumentService;
import org.example.documentservice.service.WorkAcceptanceActService;
import org.example.documentservice.utils.NameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkAcceptanceActServiceImpl implements WorkAcceptanceActService {
  private final WorkAcceptanceActProperties workAcceptanceActProperties;
  private final DocumentService documentService;
  private final WorkAcceptanceActRepository workAcceptanceActRepository;
  private final WorkAcceptanceActNumberRepository workAcceptanceActNumberRepository;
  private final WorkAcceptanceActMapper workAcceptanceActMapper;
  private final NameUtils nameUtils;
  private final ZoneId zoneId;
  private final DateTimeFormatter dateFormatter;

  @Override
  @Transactional
  public WorkAcceptanceActResponse generate(WorkAcceptanceActRequest request) {
    DocumentReference actReference = new DocumentReference(
        workAcceptanceActNumberRepository.nextValue().toString(),
        LocalDate.now(zoneId)
    );

    Map<String, Object> data = new HashMap<>();
    BigDecimal totalWithVat = fillActData(data, request, actReference);
    Document document = documentService.generate(workAcceptanceActProperties.templateName(), data);

    WorkAcceptanceAct act = createAct(request, actReference, totalWithVat, document);
    workAcceptanceActRepository.save(act);

    return workAcceptanceActMapper.toResponse(act);
  }

  private WorkAcceptanceAct createAct(
      WorkAcceptanceActRequest request,
      DocumentReference actReference,
      BigDecimal totalWithVat,
      Document document
  ) {
    return WorkAcceptanceAct.builder()
        .id(UUID.randomUUID())
        .number(actReference.number())
        .document(document)
        .date(actReference.date())
        .contractNumber(request.contract().number())
        .requestNumber(request.request().number())
        .customer(Organization.builder()
            .name(request.customer().name())
            .tin(request.customer().tin())
            .build())
        .contractor(Organization.builder()
            .name(request.contractor().name())
            .tin(request.contractor().tin())
            .build())
        .product(request.product())
        .totalAmount(totalWithVat)
        .build();
  }

  private BigDecimal fillActData(
      Map<String, Object> data,
      WorkAcceptanceActRequest request,
      DocumentReference actReference
  ) {
    fillDocumentReference(data, "act", actReference);
    fillDocumentReference(data, "contract", request.contract());
    fillDocumentReference(data, "request", request.request());

    fillOrganization(data, "customer", request.customer());
    fillOrganization(data, "contractor", request.contractor());

    data.put("product", request.product());

    data.put("start_date", request.startDate().format(dateFormatter));
    data.put("end_date", request.endDate().format(dateFormatter));

    fillWorks(data, request.works());
    BigDecimal totalAmount = fillStages(data, request.workTable().stages());

    return fillTotalWithVat(data, totalAmount, request.vatPercent());
  }

  private void fillDocumentReference(Map<String, Object> data, String name, DocumentReference documentReference) {
    data.put(name + "_number", documentReference.number());
    data.put(name + "_date", documentReference.date().format(dateFormatter));
  }

  private void fillOrganization(Map<String, Object> data, String name, OrganizationInfo organization) {
    data.put(name + "_name", organization.name());
    data.put(name + "_tin", organization.tin());
    data.put(name + "_director", nameUtils.getGenitiveFullName(
        organization.director().firstName(),
        organization.director().lastName(),
        organization.director().patronymic(),
        organization.director().gender()
    ));
    data.put(name + "_director_short", nameUtils.getReducedFullName(
        organization.director().firstName(),
        organization.director().lastName(),
        organization.director().patronymic()
    ));
  }

  private void fillWorks(Map<String, Object> data, List<String> works) {
    List<Object> worksData = new ArrayList<>();
    for (int i = 0; i < works.size(); i++) {
      worksData.add(works.get(i) + (i < works.size() - 1 ? workAcceptanceActProperties.listDelimiter() : "."));
    }
    data.put("works", worksData);
  }

  private BigDecimal fillStages(Map<String, Object> data, List<WorkTableStage> stages) {
    List<Object> stagesData = new ArrayList<>();
    BigDecimal totalAmount = BigDecimal.ZERO;
    for (int i = 0; i < stages.size(); i++) {
      Map<String, Object> stageData = new HashMap<>();
      stageData.put("stage_number", i + 1);

      List<Object> rowsData = new ArrayList<>();
      BigDecimal stageAmount = fillRows(rowsData, stages.get(i).rows());
      stageData.put("rows", rowsData);

      stageData.put("stage_total_amount", stageAmount);
      totalAmount = totalAmount.add(stageAmount);
      stagesData.add(stageData);
    }
    data.put("stages", stagesData);
    data.put("total_amount", totalAmount);
    return totalAmount;
  }

  private BigDecimal fillRows(List<Object> rowsData, List<WorkTableRow> rows) {
    BigDecimal rowsAmount = BigDecimal.ZERO;
    for (int j = 0; j < rows.size(); j++) {
      WorkTableRow row = rows.get(j);
      Map<String, Object> rowData = new HashMap<>();
      BigDecimal rowAmount = row.rate().multiply(BigDecimal.valueOf(row.hours()));

      rowData.put("number", j + 1);
      rowData.put("work", row.work());
      rowData.put("rate", row.rate());
      rowData.put("hours", row.hours());
      rowData.put("start_date", row.startDate().format(dateFormatter));
      rowData.put("end_date", row.endDate().format(dateFormatter));
      rowData.put("amount", rowAmount);

      rowsAmount = rowsAmount.add(rowAmount);
      rowsData.add(rowData);
    }
    return rowsAmount;
  }

  private BigDecimal fillTotalWithVat(Map<String, Object> data, BigDecimal totalAmount, BigDecimal vatPercent) {
    data.put("vat_percent", vatPercent);
    BigDecimal vatAmount = totalAmount.multiply(vatPercent)
        .divide(BigDecimal.valueOf(100), RoundingMode.DOWN);
    BigDecimal totalWithVat = totalAmount.add(vatAmount);
    data.put("vat_amount", vatAmount);
    data.put("total_with_vat", totalWithVat);
    return totalWithVat;
  }

  @Override
  @Transactional
  public WorkAcceptanceActFileResponse download(UUID id) {
    WorkAcceptanceAct act = workAcceptanceActRepository.findById(id)
        .orElseThrow(WorkAcceptanceActNotFoundException::new);
    return WorkAcceptanceActFileResponse.builder()
        .name(String.format(workAcceptanceActProperties.nameFormat(), act.getNumber()))
        .content(documentService.download(act.getDocument().getId()))
        .build();
  }
}
