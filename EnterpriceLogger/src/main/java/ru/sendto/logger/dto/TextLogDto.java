package ru.sendto.logger.dto;

import lombok.Data;

@Data
public class TextLogDto {
	String text;
	boolean important=false;
}
