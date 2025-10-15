package com.example.Ex02.controller;

import com.example.Ex02.dto.*;
import com.example.Ex02.mapper.MemberMapper;
import com.example.Ex02.mapper.OrderDetailsMapper;
import com.example.Ex02.mapper.OrderMapper;
import com.example.Ex02.mapper.ProductsMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
public class MallController {

    @Autowired
    ProductsMapper productsMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderDetailsMapper orderDetailsMapper;

    @Autowired
    MemberMapper memberMapper;

    @PostMapping(value = "/add.mall")
    public String add(ProductsDto product, HttpSession session,
                      @RequestParam(value = "whatColumn", required = false) String whatColumn,
                      @RequestParam(value = "keyword", required = false) String keyword,
                      @RequestParam(value = "page",defaultValue = "1") int page
                      ){
        System.out.println("product.getNum()" + product.getNum());
        System.out.println("product.getOrerqty()" + product.getOrderqty());

        // memberController에서 설정한 loginInfo를 get으로 불러와서 사용
        // 로그인 여부 확인 (세션에 "loginInfo"가 없으면 로그인 안된 상태)
        if(session.getAttribute("loginInfo") == null){
            String encodeKeyword = keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "";
            session.setAttribute("destination", "redirect:/pcontent.prd?" +
                    "num="+product.getNum()
                    +"&page="+page
                    +"&whatColumn="+whatColumn
                    +"&keyword=" +encodeKeyword);
            return "redirect:/login.mb";
        }else{
            // 로그인 된 경우 → 장바구니 처리
            // 장바구니 세션 설정 : mycart
            MyCartList mycart = (MyCartList) session.getAttribute("mycart");
            System.out.println("mycart1 : " + mycart);
            if(mycart == null){ // 장바구니가 없다면  == > 장바구니 생성
                mycart = new MyCartList(); // 장바구니 생성 (생성자)
            }
            System.out.println("mycart2 : " + mycart);

            mycart.addOrder(product.getNum(), product.getOrderqty());
            // 장바구니를 계속 끌고다닐수 있도록 세션 설정
            session.setAttribute("mycart", mycart);
            return "redirect:/list.mall";
        }
    }

    // 장바구니 리스트
    @GetMapping(value = "/list.mall")
    public String list(HttpSession session, Model model){

        System.out.println("/list.mall");
        MyCartList mycart = (MyCartList)session.getAttribute("mycart");

        Map<Integer,Integer> maplists =mycart.getAllOrderlists();
        System.out.println("maplists.size() " + maplists.size());
        System.out.println("maplists " + maplists); // {2=6, 6=4} 2번상품 6개, 6번상품 4개
        //System.out.println(maplists.get(6)); // 6번상품의 주문수량 출력:2

        Set<Integer> keylist = maplists.keySet(); // Set 중복x 순서x
        System.out.println("keylist : " + keylist); // 주문한 상품 번호

        /*
        * List : ArrayList
        * set : HashSet
        * Map : HashMap*/

        List<ShoppingInfo> shopLists = new ArrayList<ShoppingInfo>();
        int totalAmount = 0;
        for(Integer pnum : keylist){  //
            Integer qty = maplists.get(pnum); // pnum= 상품번호,  maplists.get(pnum) = 상품개수
            ProductsDto pDto = productsMapper.findByNum(pnum);

            ShoppingInfo info = new ShoppingInfo(); // 주문상세 객체생성

            info.setPnum(pnum);
            info.setPname(pDto.getName());
            info.setQty(qty);
            info.setPrice(pDto.getPrice());
            info.setAmount(pDto.getPrice() * qty);
            totalAmount += pDto.getPrice() * qty;

            shopLists.add(info); // 리스트에 추가
        }
        model.addAttribute("shopLists", shopLists);
        model.addAttribute("totalAmount", totalAmount);

        return "mall/mallList";
    }

    // mallList.html에서 결제하기 클릭
    @GetMapping(value = "/calculate.mall")
    public String calculate(HttpSession session, Model model){

        // 장바구니 정보 가져오기 (타입 다운캐스팅)
        MyCartList mycart = (MyCartList)session.getAttribute("mycart");
        if(mycart != null){
            // 장바구니의 전체 상품목록 (예: {4=3, 6=2})
            Map<Integer, Integer> maplists = mycart.getAllOrderlists();

            // 로그인 사용자 정보 가져오기
            MemberDto member = (MemberDto)session.getAttribute("loginInfo"); // 로그인 한 사람 정보 가져오기

            // 1️⃣ 주문 기본정보 저장 (orders 테이블)
            OrderDto orderDto = new OrderDto();
            orderDto.setMid(member.getId()); // kim
            orderMapper.insertOrder(orderDto); // orderstable 삽입

            // 2️⃣ 방금 넣은 주문의 PK (oid) 가져오기
            Set<Integer>keylist = maplists.keySet(); // [4, 6, 10]
            int maxOid = orderMapper.getMaxOid();
            System.out.println("maxOid" + maxOid);

            // 3️⃣ 장바구니 상품들 반복 → 주문상세(orderdetails) 삽입
            // 예) [4, 6, 10]이면 3번 반복
            // 장바구니에 담긴 상품 목록만큼 반복
            for(Integer pnum :keylist){
                OrderDetailDto odDto = new OrderDetailDto();
                int qty = maplists.get(pnum);
                System.out.println("qty :" + qty);
                odDto.setOid(maxOid);
                odDto.setPnum(pnum);
                odDto.setQty(qty);
                orderDetailsMapper.insertOrderDetails(odDto);

                ProductsDto pDto = new ProductsDto();
                pDto.setNum(pnum);
                pDto.setStock(qty);
                productsMapper.updateStock(pDto);
            }// for

            // mpoing 100점 추가
            int point = 100;

            member.setId(member.getId());
            member.setMpoint(point);
            memberMapper.addPoint(member);

        }
        session.removeAttribute("mycart"); // 장바구니 비우기
        return "redirect:/plist.prd";
    } // calculate.mall

    // 나의 주문내역 보기
    @GetMapping(value = "/order.mall")
    public String order(HttpSession session, Model model){
        MemberDto loginInfo = (MemberDto)session.getAttribute("loginInfo");

        if(loginInfo == null){
            session.setAttribute("destination", "redirect:/order.mall");
            return "redirect:/login.mb";
        }else{
           List<OrderDto>lists = orderMapper.orderMall(loginInfo);
           model.addAttribute("lists", lists);
        }
     return "mall/shopList";
    }

    // 나의 주문내역 => 상세보기
    @GetMapping(value = "/detailview.mall")
    public String detailview(@RequestParam("oid") int oid, HttpSession session, Model model){
        MemberDto loginInfo = (MemberDto)session.getAttribute("loginInfo");

        if(loginInfo == null){
            session.setAttribute("destination", "redirect/plist.prd");
            return "redirect:/login.mb";
        }else{
            model.addAttribute("oid", oid);

            // 매퍼에서 객체에 넣을 수 있도록 별칭을 지어줌
            List<ShoppingInfo> lists  =  orderMapper.showDetail(oid);
            System.out.println("lists.size()" + lists.size());
            model.addAttribute("lists", lists);
            return "mall/shopResult";
        }

        /*orderdtails ==> pnum/ products ==> num 조인*/
    }
}
