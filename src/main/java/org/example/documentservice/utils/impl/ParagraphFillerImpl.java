package org.example.documentservice.utils.impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.example.documentservice.utils.ParagraphFiller;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
class ParagraphFillerImpl implements ParagraphFiller {
  private static class Replacer {
    private final XWPFParagraph paragraph;
    private final List<XWPFRun> runs;

    private String text;
    private int index;
    private int start;
    private int end;

    public Replacer(XWPFParagraph paragraph) {
      this.paragraph = paragraph;
      this.runs = paragraph.getRuns();
      if (runs.isEmpty())
        return;

      index = 0;
      start = 0;
      text = runs.get(index).getText(0);
      end = start + text.length();
    }

    private void moveTo(int position) {
      while (end <= position) {
        text = runs.get(++index).getText(0);

        start = end;
        end += text.length();
      }
      while (start > position) {
        text = runs.get(--index).getText(0);

        end = start;
        start -= text.length();
      }
    }

    public void replace(int placeholderStart, int placeholderEnd, String value) {
      moveTo(placeholderStart);

      if (placeholderEnd <= end) {
        replaceSingleRun(placeholderStart, placeholderEnd, value);
      } else {
        replaceMultipleRuns(placeholderStart, placeholderEnd, value);
      }
    }

    private void replaceSingleRun(int placeholderStart, int placeholderEnd, String value) {
      text = text.substring(0, placeholderStart - start) + value + text.substring(placeholderEnd - start);
      runs.get(index).setText(text, 0);

      end += value.length() - (placeholderEnd - placeholderStart);
    }

    private void replaceMultipleRuns(int placeholderStart, int placeholderEnd, String value) {
      text = text.substring(0, placeholderStart - start) + value;
      runs.get(index).setText(text, 0);
      ++index;

      removeFullyReplacedRuns(placeholderEnd);
      trimLastRun(placeholderStart, placeholderEnd, value);
    }

    private void removeFullyReplacedRuns(int placeholderEnd) {
      while (end < placeholderEnd) {
        int currentRunLength = runs.get(index).getText(0).length();
        if (end + currentRunLength > placeholderEnd)
          break;
        start = end;
        end += currentRunLength;
        paragraph.removeRun(index);
      }
    }

    private void trimLastRun(int placeholderStart, int placeholderEnd, String value) {
      if (end < placeholderEnd) {
        // Последний ран частично занят плейсхолдером
        text = runs.get(index).getText(0).substring(placeholderEnd - end);
        runs.get(index).setText(text, 0);

        start = placeholderStart + value.length();
        end = start + text.length();
      } else {
        // Последний ран полностью занят плейсхолдером и уже удалён
        index--;

        end = placeholderStart + value.length();
        start = end - text.length();
      }
    }
  }

  private final Pattern placeholderPattern;

  @Override
  public void fill(XWPFParagraph paragraph, Map<String, Object> data) {
    Matcher matcher = placeholderPattern.matcher(paragraph.getText());
    Replacer replacer = new Replacer(paragraph);

    int matchOffset = 0;
    while (matcher.find()) {
      int start = matcher.start() + matchOffset, end = matcher.end() + matchOffset;
      String key = matcher.group(2), value = String.valueOf(data.get(key));

      replacer.replace(start, end, value);
      matchOffset += value.length() - (end - start);
    }
  }
}
