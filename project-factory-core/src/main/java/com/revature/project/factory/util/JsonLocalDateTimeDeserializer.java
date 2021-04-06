package com.revature.project.factory.util;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.revature.project.factory.constant.AppConstants;

@Component
public class JsonLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

  @Override
  public LocalDateTime deserialize(JsonParser jsonparser,
      DeserializationContext deserializationcontext) throws IOException {
    if (jsonparser.getText().matches(AppConstants.NUMBERS_ONLY_REGEX)
        && jsonparser.getText().length() > 2) {
      Timestamp timestamp = new Timestamp(Long.parseLong(jsonparser.getText()));
      return timestamp.toLocalDateTime();
    }
    DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE_TIME;
    return LocalDateTime.parse(jsonparser.getText(), dtf);
  }
}
