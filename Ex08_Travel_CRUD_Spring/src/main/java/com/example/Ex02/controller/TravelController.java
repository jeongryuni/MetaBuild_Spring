package com.example.Ex02.controller;

import com.example.Ex02.dto.TravelDto;
import com.example.Ex02.mapper.TravelMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class TravelController {
    @Autowired
    private TravelMapper travelMapper;

    // 삽입폼으로 이동
    @GetMapping(value="/insert")
    public String form(@ModelAttribute("tDto")TravelDto tDto) {
        
        return "insert";
    }

    // 삽입
    @PostMapping(value = "/insertProc")
    public String insert(@ModelAttribute("tDto") @Valid TravelDto tDto, BindingResult br){

        String resultPage;
        if (br.hasErrors()) {
            resultPage = "insert";
        } else {
            travelMapper.insertTravel(tDto);
            resultPage = "redirect:/list";
        }
        return resultPage;
    }

    // 검색 입력(whatColumn, keyword)
    // 전체조회 list (입력 후, 삭제 후, 수정 후 list로 이동)
    @GetMapping(value = "/list")
    public String list(Model model,
                       // required = false : 작성하지 않으면 반드시 값을 넘겨야함
                       // 값을 넘길때가 있고 안넘길때가 있을때 작성
                       @RequestParam(value = "whatColumn", required = false) String whatColumn,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "page",defaultValue = "1") int page) /*페이지가 안넘어오면 디폴트= 1*/
    {

        /*페이징네이션*/
        int limit=3; // 한 페이지에 레코드 3개 보이기
        int offset= (page-1) * limit;

        Map<String, Object> params = new HashMap<String, Object>(); //key, value
        params.put("whatColumn", whatColumn); // 검색할 컬럼명
        params.put("keyword", keyword); // 검색어
        params.put("offset", offset);  // 페이징 offset
        params.put("limit", limit);  // 한 페이지 크기

        // (1) 전체 조회 리스트 SQL 실행
        List<TravelDto>lists = travelMapper.selectAll(params);

        // (2) 검색된 레코드 총 개수 SQL 실행
        int totalCount = travelMapper.getCount(params);

        // (3) 전체 페이지 수 = 올림(전체 레코드 개수 / limit)
        int totalPage = (int) Math.ceil((double) totalCount / limit);

        // 뷰로 전달할 데이터 세팅
        model.addAttribute("lists", lists);          // 목록 데이터
        model.addAttribute("page", page);          // 현재 페이지
        model.addAttribute("totalCount", totalCount); // 전체 레코드 수
        model.addAttribute("totalPage", totalPage); // 전체 페이지 수
        model.addAttribute("keyword", keyword);     // 검색어 유지
        model.addAttribute("whatColumn", whatColumn); // 검색 조건 유지

        /*
        *
        * 저 두개를 list.html의 버튼쪽에 작성해주지 않으면 검색하고나서
        * 다음이나 이전버튼을 눌렀을때 null값이 keyword랑 whatColumn에 들어가서 전체리스트가 조회되기때문에
        *  <a th:href="@{list(page=${page-1}, whatColumn=${whatColumn}, keyword=${keyword})}">이전</a>
        *<a th:href="@{list(page=${page+1}, whatColumn=${whatColumn}, keyword=${keyword})}">다음</a>
        * */

        return "list";
    }

    // 상세조회 이동
    @GetMapping(value = "/content_view")
    public String content_view(@RequestParam("num") int num, Model model){
        TravelDto tDtd = travelMapper.findByNum(num);
        model.addAttribute("num", num);
        model.addAttribute("tDtd", tDtd);
        return "content_view";
    }

    // 수정폼 이동
    @GetMapping(value = "/update")
    public String update(@RequestParam("num") int num,
                         @RequestParam(value = "whatColumn", required = false) String whatColumn,
                         @RequestParam(value = "keyword", required = false) String keyword,
                         @RequestParam(value = "page",defaultValue = "1") int page, /*페이지가 안넘어오면 디폴트= 1*/
                         Model model){

        model.addAttribute("page",page);
        model.addAttribute("whatColumn",whatColumn);
        model.addAttribute("keyword",keyword);

        TravelDto tDto = travelMapper.findByNum(num);
        
        model.addAttribute("num", num);
        model.addAttribute("tDto", tDto);
        model.addAttribute("areaArr", List.of("유럽", "동남아", "일본", "중국"));
        model.addAttribute("typeArr", List.of("패키지", "크루즈", "자유여행", "골프여행"));
        model.addAttribute("priceArr", List.of("100~200", "200~300", "300~400"));



        return "update";
    }

    // 수정처리 (유효성 검증) updateProc
    @PostMapping(value = "/updateProc")
    public String updateproc(@ModelAttribute("tDto") @Valid TravelDto tDto, BindingResult br, Model model,
                             @RequestParam(value = "whatColumn", required = false) String whatColumn,
                             @RequestParam(value = "keyword", required = false) String keyword,
                             @RequestParam(value = "page",defaultValue = "1") int page){


        /* 입력에 실패했을 때 update폼에 다시 렌더링 되기 위함 */
        model.addAttribute("areaArr", List.of("유럽", "동남아", "일본", "중국"));
        model.addAttribute("typeArr", List.of("패키지", "크루즈", "자유여행", "골프여행"));
        model.addAttribute("priceArr", List.of("100~200", "200~300", "300~400"));

        model.addAttribute("page",page);
        model.addAttribute("whatColumn",whatColumn);
        model.addAttribute("keyword",keyword);

        String resultPage="";
        if (br.hasErrors()) {
            resultPage = "update";
        } else {
            travelMapper.updateTravel(tDto);
            String encodeKeyword = keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "";
            resultPage = "redirect:/list?page="+page+"&whatColumn="+whatColumn+"&keyword="+encodeKeyword;
        }
        return resultPage;
    }

    @GetMapping(value = "/delete")
    public String delete(@RequestParam("num") int num, Model model,
                         @RequestParam(value = "whatColumn", required = false) String whatColumn,
                         @RequestParam(value = "keyword", required = false) String keyword,
                         @RequestParam(value = "page",defaultValue = "1") int page){


        model.addAttribute("page",page);
        model.addAttribute("whatColumn",whatColumn);
        model.addAttribute("keyword",keyword);

        travelMapper.deleteTravel(num);

        int limit=3; // 한 페이지 크기 (list 메서드랑 동일해야 함)
        int offset= (page-1) * limit;

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("whatColumn", whatColumn);
        params.put("keyword", keyword);
        params.put("offset", offset);
        params.put("limit", limit);

        int totalCount = travelMapper.getCount(params);
        if(totalCount % limit == 0) {
            page = page - 1;
        }

        String resultPage="";
        String encodeKeyword = keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "";
        resultPage = "redirect:/list?page="+page+"&whatColumn="+whatColumn+"&keyword="+encodeKeyword;
        return resultPage;
    }

}
