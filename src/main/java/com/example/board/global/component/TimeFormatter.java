package com.example.board.global.component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class TimeFormatter {
	/**
	 * 주어진 시간을 현재 시간 기준으로 '상대적인 시간 문자열'로 포매팅하는 메서드
	 * (예: "방금", "5분 전", "3시간 전", "1일 전")
	 *
	 * @param time 포매팅할 LocalDateTime 객체
	 * @return 포매팅된 상대 시간 문자열
	 */
	public String formatTime(LocalDateTime time) {
		LocalDateTime now = LocalDateTime.now();
		Duration differenceOfDays = Duration.between(time, now);

		if (now.minusDays(1).isAfter(time))
			return "%s일 전".formatted(differenceOfDays.toDays());

		else if (now.minusHours(1).isAfter(time))
			return "%s시간 전".formatted(differenceOfDays.toHours());

		else if (now.minusMinutes(1).isAfter(time))
			return "%s분 전".formatted(differenceOfDays.toMinutes());

		else
			return "방금";
	}

	/**
	 * 주어진 시간을 "yyyy년 M월 dd일 HH:mm" 형식의 문자열로 포매팅하는 메서드
	 *
	 * @param time 포매팅할 LocalDateTime 객체
	 * @return "yyyy년 M월 dd일 HH:mm" 형식으로 포매팅된 시간 문자열
	 */
	public String formatTimeIntoYyyyMmDd(LocalDateTime time) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 HH:mm");

		return time.format(formatter);
	}
}
