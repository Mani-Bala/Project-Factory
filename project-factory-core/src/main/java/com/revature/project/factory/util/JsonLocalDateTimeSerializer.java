package com.revature.project.factory.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@Component
public class JsonLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

  @Override
  public void serialize(LocalDateTime date, JsonGenerator gen, SerializerProvider provider)
      throws IOException {
    String formattedDateTime = DateTimeFormatter.ISO_DATE_TIME.format(date);
    gen.writeString(formattedDateTime);
  }
}
