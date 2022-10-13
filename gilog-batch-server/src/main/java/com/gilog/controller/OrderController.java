package com.gilog.controller;

import com.gilog.dto.OrderDto;
import com.gilog.entity.GiLog;
import com.gilog.service.OrderService;
import com.gilog.vo.OrderFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OrderController {

    @Value("${file.dir}")
    private String dir;

    private final OrderService orderService;

    @GetMapping("/order")
    public String orderListPage() {

        return "order/orderList";
    }

    @GetMapping("/delivery")
    public String deliveryManagePage(Model model) {
        List<OrderDto> orders = orderService.getAll();

        int pay1Count = 0;
        int delivery1Count = 0;
        int delivery3Count = 0;
        int delivery4Count = 0;
        int delivery5Count = 0;
        for (OrderDto order : orders) {
            if (order.getPayState().equals(1)) pay1Count++;
            if (order.getDeliveryState().equals(1)) delivery1Count++;
            else if (order.getDeliveryState().equals(3)) delivery3Count++;
            else if (order.getDeliveryState().equals(4)) delivery4Count++;
            else if (order.getDeliveryState().equals(5)) delivery5Count++;
        }

        model.addAttribute("pay1Count", pay1Count);
        model.addAttribute("delivery1Count", delivery1Count);
        model.addAttribute("delivery3Count", delivery3Count);
        model.addAttribute("delivery4Count", delivery4Count);
        model.addAttribute("delivery5Count", delivery5Count);

        return "delivery/deliveryList";
    }

    @GetMapping("/refund")
    public String refundManagePage(Model model) {
        OrderFilter orderFilter = OrderFilter.builder().payState(5).build();
        List<OrderDto> orders = orderService.getOrderList(orderFilter);

//        int pay1Count = 0;
//        int delivery1Count = 0;
//        int delivery3Count = 0;
//        int delivery4Count = 0;
//        int delivery5Count = 0;
//        for (OrderDto order : orders) {
//            if (order.getPayState().equals(1)) pay1Count++;
//            if (order.getDeliveryState().equals(1)) delivery1Count++;
//            else if (order.getDeliveryState().equals(3)) delivery3Count++;
//            else if (order.getDeliveryState().equals(4)) delivery4Count++;
//            else if (order.getDeliveryState().equals(5)) delivery5Count++;
//        }
//
//        model.addAttribute("pay1Count", pay1Count);
//        model.addAttribute("delivery1Count", delivery1Count);
//        model.addAttribute("delivery3Count", delivery3Count);
//        model.addAttribute("delivery4Count", delivery4Count);
//        model.addAttribute("delivery5Count", delivery5Count);

        return "refund/refundList";
    }

    @GetMapping("/orders")
    @ResponseBody
    public List<OrderDto> orderListData(@RequestParam(required = false) String searchKey, @RequestParam(required = false) String searchValue, @RequestParam(required = false) String product, @RequestParam(required = false) String beforeDateString, @RequestParam(required = false) String afterDateString, @RequestParam(required = false) Integer payState, @RequestParam(required = false) Integer refundState) {

        //널값 변환
        product = (product.equals("on")) ? null : product;

        // 날짜 처리
        if (beforeDateString.isEmpty()) { beforeDateString = "2099-01-01"; }
        if (afterDateString.isEmpty()) { afterDateString = "2000-01-01"; }

        LocalDate beforeDate = LocalDate.parse(beforeDateString, DateTimeFormatter.ISO_DATE);
        LocalDate afterDate = LocalDate.parse(afterDateString, DateTimeFormatter.ISO_DATE);

        OrderFilter filter =  OrderFilter.builder()
                .searchKey(searchKey)
                .searchValue(searchValue)
                .payState(payState)
                .product(product)
                .beforeDate(beforeDate)
                .afterDate(afterDate)
                .refundState(refundState)
                .build();

        return orderService.getOrderList(filter);
    }

    // 결제 상태 변경
    @ResponseBody
    @PostMapping("/order/change-pay-state")
    public Boolean changePayState(Long id, Integer state) {
        try {
            orderService.updateOrderPayState(id, state);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // 결제 상태 변경
    @ResponseBody
    @PostMapping("/order/change-refund-state")
    public Boolean changeRefundState(Long id, Integer state) {
        try {
            orderService.updateOrderRefundState(id, state);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // 배송 상태 변경
    @ResponseBody
    @PostMapping("/order/change-delivery-state")
    public Boolean changeDeliveryState(Long id, Integer state) {
        try {
            orderService.updateOrderDeliveryState(id, state);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // 운송장번호 변경
    @ResponseBody
    @PostMapping("/order/change-way-bill-number")
    public Boolean changeWayBillNumber(Long id, String wayBillNumber) {
        try {
            orderService.updateOrderWayBillNumber(id, wayBillNumber);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // 엑셀파일 다운로드
    @GetMapping("/order/excel/{id}")
    public void excelDownload(HttpServletResponse response, @PathVariable Long id, Authentication authentication) throws IOException {
//        Workbook wb = new HSSFWorkbook();
        List<GiLog> giLogList = orderService.getOrderGiLogList(id);

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("첫번째 시트");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;



        // Header
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("날짜");
        cell = row.createCell(1);
        cell.setCellValue("질문");
        cell = row.createCell(2);
        cell.setCellValue("답변");
        cell = row.createCell(3);
        cell.setCellValue("사진");

        // Body
        for (GiLog giLog: giLogList) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(giLog.getWriteDate());
            cell = row.createCell(1);
            cell.setCellValue(giLog.getQuestion());
            cell = row.createCell(2);
            cell.setCellValue(giLog.getRequest());
            cell = row.createCell(3);
            cell.setCellValue("");
        }


        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
//        response.setHeader("Content-Disposition", "attachment;filename=example.xls");
        response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");

        // Excel File Output
        wb.write(response.getOutputStream());
        wb.close();
    }

    //사진파일 다운로드
    @GetMapping("/order/image-zip/{id}")
    public void downloadZip(HttpServletResponse response, @PathVariable Long id, Authentication authentication) {
        List<GiLog> giLogList = orderService.getOrderGiLogList(id);

        ZipOutputStream zout = null;
        String zipName = "image.zip";		//ZIP 압축 파일명
        String tempPath = dir;

        try {	//ZIP 압축 파일 저장경로

            //ZIP파일 압축 START
            zout = new ZipOutputStream(new FileOutputStream(dir + zipName));
            byte[] buffer = new byte[10240];
            FileInputStream in = null;

            int k = 1;
            for (GiLog giLog : giLogList){
                in = new FileInputStream(dir + giLog.getImage());		//압축 대상 파일
                zout.putNextEntry(new ZipEntry((k++) + ".png"));	//압축파일에 저장될 파일명

                int len;
                while((len = in.read(buffer)) > 0){
                    zout.write(buffer, 0, len);			//읽은 파일을 ZipOutputStream에 Write
                }

                zout.closeEntry();
                in.close();
            }

            zout.close();
            //ZIP파일 압축 END

            //파일다운로드 START
            response.setContentType("application/zip");
            response.addHeader("Content-Disposition", "attachment;filename=" + zipName);

            FileInputStream fis = new FileInputStream(tempPath + zipName);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ServletOutputStream so = response.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(so);

            int n = 0;
            while((n = bis.read(buffer)) > 0){
                bos.write(buffer, 0, n);
                bos.flush();
            }

            if(bos != null) bos.close();
            if(bis != null) bis.close();
            if(so != null) so.close();
            if(fis != null) fis.close();
            //파일다운로드 END
        }catch(IOException e){
            //Exception
        }finally{
            if (zout != null){
                zout = null;
            }
        }
    }



//    @GetMapping("/ordersss")
//    @ResponseBody
//    public List<OrderDto> orderListPagesss(@RequestParam(required = false) String product, @RequestParam(required = false) LocalDate toDate) {
//
//        return a;
//    }

}
