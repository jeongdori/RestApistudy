package com.board.study.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.board.study.dto.board.BoardFileResponseDto;
import com.board.study.service.BoardFileService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BoardFileController {
	
	private BoardFileService boardFileService;
	
	public BoardFileController(BoardFileService boardFileService) {
		this.boardFileService = boardFileService;
	}
	
	@GetMapping("/file/download")
	public void downloadFile(@RequestParam()Long id, HttpServletResponse response) throws Exception{
		
		try {
			
			// 파일 조회
			BoardFileResponseDto fileinfo = boardFileService.findById(id);
			
			if (fileinfo ==null) throw new FileNotFoundException("Empty FileData");
			
			//파일 객체 생성
			File dfile = new File(fileinfo.getFilePath(), fileinfo.getSaveFileName());
			
			//파일 실제 존재 여부 확인하기 위해 길이를 생성하고 확인
			int fSize = dFile.length();
			
			//파일 다운로드 요청에 응답
			if (fSize > 0) {
				//파일명 인코딩 후 attachment, Content-Disposition Header로 설정
				String encodedFilename = "attachment; filename*=" + "UTF-8" + "''" + URLEncoder.encode(fileinfo.getOrigFileName(), "UTF-8");
				response.setContentType("application/octet-stream; charset=utf-8"); // 컨텐츠타입
				response.setHeader("Content-Disposition", encodedFilename); // 헤더
				response.setContentLengthLong(fSize); //컨텐츠길이
				
				
				// 기본파일 입출력 클래스, 버퍼X => 속도느림, 속도개선을 위해 버퍼를 사용하는 클래스와 같이 사용
				BufferedInputStream in = null;
				in = new BufferedInputStream(new FileInputStream(dFile));
				
                BufferedOutputStream out = null;
                out = new BufferedOutputStream(response.getOutputStream());
                
                try {
                    byte[] buffer = new byte[4096];
                    int bytesRead = 0;

		             /*
				    모두 현재 파일 포인터 위치를 기준으로 함 (파일 포인터 앞의 내용은 없는 것처럼 작동)
				    int read() : 1byte씩 내용을 읽어 정수로 반환
				    int read(byte[] b) : 파일 내용을 한번에 모두 읽어서 배열에 저장
				    int read(byte[] b. int off, int len) : 'len'길이만큼만 읽어서 배열의 'off'번째 위치부터 저장
				    */
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }

                    // 버퍼에 남은 내용이 있다면, 모두 파일에 출력					
                    out.flush();
                } finally {
                    /*
                    현재 열려 in,out 스트림을 닫음
                    메모리 누수를 방지하고 다른 곳에서 리소스 사용이 가능하게 만듬
                     */
                    in .close();
                    out.close();
                }
				
			}else {
                throw new FileNotFoundException("Empty FileData.");
            }
			

			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	
	
	
	@PostMapping("/file/delete.ajax")
	public String updateDeleteYn(Model model, BoardFileRequestDto boardFileRequestDto) throws Exception{
		
		try {
            model.addAttribute("result", boardFileService.updateDeleteYn(boardFileRequestDto.getIdArr()));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return "jsonView";
	}

}
