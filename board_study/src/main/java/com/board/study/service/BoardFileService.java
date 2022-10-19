package com.board.study.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.board.study.entity.board.BoardFileRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BoardFileService {

	
	private BoardFileRepository boardFileRepository;
	
	public BoardFileService(BoardFileRepository boardFileRepository) {
		super();
		this.boardFileRepository = boardFileRepository;
	}
	
	
	public BoardFileResponseDto findById(Long id) {
		return new BoardFileResponseDto(boardFileRepository.findById(id).get());
	}
	
	public List < Long > findByBoardId(Long boardId) throws Exception {
		return boardFileRepository.findByBoardId(boardId);
	}

	
	public boolean uploaFile(MultipartHttpServletRequest multRequest, Long boardId) throws Exception{
		
		if(boardId == null) throw new NullPointerException("Empty BOARD_ID.");
		
		//<파라미터 이름, 파일정보>
		Map<String, MultipartFile> files = multRequest.getMultiFileMap();
		
		
		/*
		 https://tychejin.tistory.com/31
		 Map에 값을 전체 출력하기 위해서는 entrySet(),keySet() 메소드를 사용하면 되는데 
		 entrySet() 메서드는 key와 value의 값이 모두 필요한 경우 사용
		 keySet() 메서드는 key의 값만 필요한 경우 사용
		*/
		/*
		 Iterator 인터페이스를 사용할 수 없는 컬렉션인 Map에서 Iterator 인터페이스를 사용하기 위해서는 
		 Map에 entrySet(), keySet() 메소드를 사용하여 Set 객체를 반환받은 후 Iterator 인터페이스를 사용
		*/
		
		//files의 emtrySet 요소 읽기
		Iterator <Entry <String, MultipartFile>> itr = files.entrySet().iterator();
		
		
		MultipartFile mFile;
		String savaFilePath = "", randomFileName = "";
		Calendar cal = Calendar.getInstance();
		List < Long > resultList = new ArrayList < Long > ();
		
		/*
		 hasNext와 Next의 차이점 - 반환타입
	 	 hasNext = boolean 타입으로 반환
	 	 Next = 매개변수 or iterator 되는 타입으로 반환
		  
		 */
		
		
		
		
	}



	
	
	
}
