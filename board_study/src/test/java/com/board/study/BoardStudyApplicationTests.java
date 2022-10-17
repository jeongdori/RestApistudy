package com.board.study;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.board.study.dto.board.BoardRequestDto;
import com.board.study.dto.board.BoardResponesDto;
import com.board.study.service.BoardService;

@SpringBootTest
class BoardStudyApplicationTests {

	@Autowired
	private BoardService boardService; 
	
	@Test
	void save() {
		BoardRequestDto boardSaveDto = new BoardRequestDto();
		
		boardSaveDto.setTitle("제목입니다.");
		boardSaveDto.setContent("내용입니다.");
		boardSaveDto.setRegisterId("작성자");
		
		Long result = boardService.save(boardSaveDto);
		
		if(result >0) {
			System.out.println("#Success save() ~");
			findAll();
			findById(result);
		}else {
			System.out.println("#Fail Save() ~");
		}
		
		
	}
	
	void findAll() {
		List<BoardResponesDto> list = boardService.findAll();
		
		if(list!=null) {
			System.out.println("# Success findAll() : "+ list.toString());
		}else {
			System.out.println("# Fail findAll() ~");
		}
	}
	
	
	void findById(Long id) {
		BoardResponesDto info = boardService.findById(id);
		
		if(info!=null) {
			System.out.println("# Success findById() : "+ info.toString());
		}else {
			System.out.println("# Fail findById() ~");
		}
	}
	
	
	void updateBoard(Long id) {
		
		BoardRequestDto boardRequestDto = new BoardRequestDto();
		
		boardRequestDto.setId(id);
		boardRequestDto.setTitle("업데이트 제목");
		boardRequestDto.setContent("업데이트 내용");
		boardRequestDto.setRegisterId("작성자");
		
		int result = boardService.updateBoard(boardRequestDto);
		
		if(result > 0) {
			System.out.println("# Success updateBoard() : ");
		}else {
			System.out.println("# Fail updateBoard() ~");
		}
	}

}
