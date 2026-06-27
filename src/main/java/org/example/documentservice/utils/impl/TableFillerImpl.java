package org.example.documentservice.utils.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.example.documentservice.exception.TablePartEndNotFoundException;
import org.example.documentservice.exception.UnexpectedTablePartEndException;
import org.example.documentservice.utils.ParagraphFiller;
import org.example.documentservice.utils.TableFiller;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
class TableFillerImpl implements TableFiller {
  private interface TableNode {
  }

  @RequiredArgsConstructor
  @Getter
  private static class TableRow implements TableNode {
    private final CTRow template;
  }

  @Getter
  private static class TableNodeGroup implements TableNode {
    private final List<TableNode> children = new ArrayList<>();
  }

  @RequiredArgsConstructor
  @Getter
  private static class TablePart extends TableNodeGroup {
    private final String key;
  }

  private static final String PART_START_PLACEHOLDER_TYPE = "TABLE_PART_START",
      PART_END_PLACEHOLDER_TYPE = "TABLE_PART_END";

  private final Pattern placeholderPattern;
  private final ParagraphFiller paragraphFiller;

  @Override
  public void fill(XWPFTable table, Map<String, Object> data) {
    TableNodeGroup tableTree = scan(table);
    fillTable(table, tableTree, data);
  }

  private TableNodeGroup scan(XWPFTable table) {
    Deque<TableNodeGroup> stack = new ArrayDeque<>();
    stack.push(new TableNodeGroup());

    scanRows(table, stack);

    if (stack.size() > 1) {
      throw new TablePartEndNotFoundException();
    }
    return stack.pop();
  }

  private void scanRows(XWPFTable table, Deque<TableNodeGroup> stack) {
    while (table.getNumberOfRows() > 0) {
      XWPFTableRow row = table.getRow(0);
      if (!scanPartPlaceholder(row, stack)) {
        stack.getFirst().getChildren().add(new TableRow((CTRow) row.getCtRow().copy()));
      }
      table.removeRow(0);
    }
  }

  private boolean scanPartPlaceholder(XWPFTableRow row, Deque<TableNodeGroup> stack) {
    if (!isSingleParagraphCell(row)) {
      return false;
    }
    Matcher matcher = placeholderPattern.matcher(row.getCell(0).getParagraphs().get(0).getText());
    if (!matcher.matches()) {
      return false;
    }
    String type = matcher.group(1), key = matcher.group(2);
    switch (type) {
      case PART_START_PLACEHOLDER_TYPE -> stack.push(new TablePart(key));
      case PART_END_PLACEHOLDER_TYPE -> {
        TableNodeGroup part = stack.pop();
        if (stack.isEmpty()) {
          throw new UnexpectedTablePartEndException();
        }
        stack.getFirst().getChildren().add(part);
      }
      default -> {
        return false;
      }
    }
    return true;
  }

  private boolean isSingleParagraphCell(XWPFTableRow row) {
    if (row.getTableCells().size() != 1) {
      return false;
    }
    return row.getCell(0).getParagraphs().size() == 1;
  }

  private void fillTable(XWPFTable table, TableNodeGroup tableTree, Map<String, Object> data) {
    for (TableNode node : tableTree.getChildren()) {
      if (node instanceof TableRow row) {
        XWPFTableRow newRow = new XWPFTableRow((CTRow) row.template.copy(), table);
        fillTableRow(newRow, data);
        table.addRow(newRow);
      }
      else if (node instanceof TablePart part) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> partData = (List<Map<String, Object>>) data.get(part.getKey());
        for (Map<String, Object> partDataItem : partData) {
          fillTable(table, part, partDataItem);
        }
      }
    }
  }

  private void fillTableRow(XWPFTableRow row, Map<String, Object> data) {
    for (XWPFTableCell cell : row.getTableCells()) {
      for (XWPFParagraph paragraph : cell.getParagraphs()) {
        paragraphFiller.fill(paragraph, data);
      }
    }
  }
}
