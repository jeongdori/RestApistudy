package com.board.study.dto.board;

import com.board.study.entity.board.Board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BoardRequestDto {
	
	private Long id;
	private String title;
	private String content;
	private String registerId;
	
	public Board toEntity() {
		return Board.builder()
				.title(title)
				.content(content)
				.registerId(registerId)
				.build();
	}

}
