package com.reviewlah.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviewlah.common.util.RCode;
import com.reviewlah.controller.form.DeleteDiningCommentRequest;
import com.reviewlah.controller.form.InsertDiningCommentRequest;
import com.reviewlah.controller.form.SelectDCByMerAndCusIdRequest;
import com.reviewlah.controller.form.SelectDCByMerchantIdRequest;
import com.reviewlah.db.pojo.Customer;
import com.reviewlah.db.pojo.DiningComment;
import com.reviewlah.db.pojo.Merchant;
import com.reviewlah.remote.CustomerService;
import com.reviewlah.remote.MerchantService;
import com.reviewlah.service.DiningCommentService;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping({"/comment"})
public class DiningCommentController {
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private DiningCommentService diningCommentService;
    private final ObjectMapper mapper = new ObjectMapper();

    private Merchant selectMerchantById(BigInteger merchant_id) {
        RCode rCode = merchantService.selectMerchantById(merchant_id);
        if (rCode.get("merchant") == null) {
            return null;
        }
        return mapper.convertValue(rCode.get("merchant"), Merchant.class);
    }

    private Customer selectCustomerById(@PathVariable BigInteger customer_id) {
        RCode rCode = customerService.selectCustomerById(customer_id);
        if (rCode.get("customer") == null) {
            return null;
        }
        return mapper.convertValue(rCode.get("customer"), Customer.class);
    }
    public Double getAverageRateByMerchantId(BigInteger merchant_id) {
        Double rate = this.diningCommentService.getAverageRateByMerchantId(merchant_id);
        if(rate == null) rate = 0.0;
        BigDecimal bd = new BigDecimal(rate);
        double avg_rate = bd.setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
        return avg_rate;
    }

    @GetMapping("/merchants/{merchant_id}")
    public RCode selectDCByMerchantId(@PathVariable BigInteger merchant_id) {
        ArrayList<DiningComment> comments = diningCommentService.selectDCByMerchantId(merchant_id);
        return RCode.ok().put("comments", comments);
    }

    @PostMapping({"/showAllForMerchant"})
    public RCode selectDCByMerchantId(@RequestBody SelectDCByMerchantIdRequest request) {
        BigInteger user_id = request.getUser_id();
        ArrayList<HashMap> res = new ArrayList<>();
        Merchant merchant = selectMerchantById(user_id);
        if(merchant != null) {
            res = this.diningCommentService.selectDCMapByMerchantId(merchant.getMerchant_id());
            res.forEach(map -> {
                Customer customer = selectCustomerById(BigInteger.valueOf((long)map.get("customer_id")));
                map.put("name", customer.getName());
                map.put("avator", customer.getAvator());
            });
        }
        else {
            System.out.println("Merchant Does Not Exist");
            return RCode.error("Merchant Does Not Exist");
        }
        return RCode.ok().put("list", res);
    }
//    public ArrayList<Map<String, Object>> selectDCByMerchantId(@RequestBody SelectDCByMerchantIdRequest request) {
//        ArrayList<Map<String, Object>> res = new ArrayList<>();
//        BigInteger user_id = request.getUser_id();
//        User user = this.userService.selectUserById(user_id);
//        if(user != null && user.getType() == 2) {
//            BigInteger merchant_id = this.merchantService.selectMerchantIdByUserId(user_id);
//            if(merchant_id != null) {
//                ArrayList<DiningComment> list = this.diningCommentService.selectDCByMerchantId(merchant_id);
//                for(DiningComment tmp : list) {
//                    Map<String, Object> map = new HashMap<>();
//                    BigInteger customer_id = tmp.getCustomer_id();
//                    BigInteger tmp_user_id = this.customerService.selectUserIdByCustomerId(customer_id);
//                    User tmp_user = this.userService.selectUserById(tmp_user_id);
//                    String name = tmp_user.getName();
//                    String avator = tmp_user.getAvator();
////                if(user == null) {
////                    name = "New Glory";
////                    avator = "";
////                }
//                    map.put("content", tmp.getContent());
//                    map.put("rate", tmp.getRate());
//                    map.put("name", name);
//                    map.put("avator", avator);
//                    map.put("pic_dc", tmp.getPic_dc());
//                    map.put("time_dc", tmp.getTime_dc());
//                    res.add(map);
//                }
//                System.out.println("successful");
//                return res;
//            }
//            else{
//                System.out.println("Merchant Does Not Exist");
//            }
//        }
//        else {
//            System.out.println("User Does Not Exist");
//        }
//        return null;
//    }
    @PostMapping({"/showAllForCustomer"})
    public RCode selectDCByMerAndCusId(@RequestBody SelectDCByMerAndCusIdRequest request) {
        ArrayList<HashMap> res = new ArrayList<>();
        BigInteger customer_user_id = request.getCustomer_user_id();
        BigInteger merchant_user_id = request.getMerchant_user_id();
        Merchant merchant = selectMerchantById(merchant_user_id);
        Customer customer = selectCustomerById(customer_user_id);
        if(merchant == null) {
            System.out.println("Merchant Does Not Exist");
            return RCode.error("Merchant Does Not Exist");
        }
        if(customer == null) {
            System.out.println("Customer Does Not Exist");
            return RCode.error("Customer Does Not Exist");
        }
        res = this.diningCommentService.selectDCMapByMerAndCusId(merchant.getMerchant_id(), customer.getCustomer_id());
        res.forEach(map -> {
            Customer tmp = selectCustomerById(BigInteger.valueOf((long)map.get("customer_id")));
            map.put("name", tmp.getName());
            map.put("avator", tmp.getAvator());
        });
        return RCode.ok().put("list", res);
    }
//    public ArrayList<Map<String, Object>> selectDCByMerAndCusId(@RequestBody SelectDCByMerAndCusIdRequest request) {
//        ArrayList<Map<String, Object>> res = new ArrayList<>();
//        BigInteger customer_user_id = request.getCustomer_user_id();
//        BigInteger merchant_user_id = request.getMerchant_user_id();
//        User merchant_user = this.userService.selectUserById(merchant_user_id);
//        User customer_user = this.userService.selectUserById(customer_user_id);
//        if(merchant_user != null && merchant_user.getType() == 2 && customer_user != null && customer_user.getType() == 1) {
//            Merchant merchant = this.merchantService.selectMerchantByUserId(merchant_user_id);
//            Customer customer = this.customerService.selectCustomerByUserId(customer_user_id);
//            if(merchant == null) {
//                System.out.println("Merchant Does Not Exist");
//                return null;
//            }
//            if(customer == null) {
//                System.out.println("Customer Does Not Exist");
//                return null;
//            }
//            ArrayList<DiningComment> list = this.diningCommentService.selectDCByMerchantId(merchant.getMerchant_id());
//            for(DiningComment tmp : list) {
//                Map<String, Object> map = new HashMap<>();
//                BigInteger customer_id = tmp.getCustomer_id();
//                BigInteger user_id = this.customerService.selectUserIdByCustomerId(customer_id);
//                User user = this.userService.selectUserById(user_id);
//                String name = user.getName();
//                String avator = user.getAvator();
////                if(user == null) {
////                    name = "New Glory";
////                    avator = "";
////                }
//                map.put("content", tmp.getContent());
//                map.put("rate", tmp.getRate());
//                map.put("name", name);
//                map.put("avator", avator);
//                map.put("pic_dc", tmp.getPic_dc());
//                map.put("time_dc", tmp.getTime_dc());
//                if(tmp.getCustomer_id() == customer.getCustomer_id()) {
//                    map.put("own", 1);
//                }
//                else {
//                    map.put("own", 0);
//                }
//                res.add(map);
//            }
//        }
//        else {
//            System.out.println("User Does Not Exist");
//        }
//        return res;
//    }
    @PostMapping({"/insert"})
    public RCode insertDC(@RequestBody InsertDiningCommentRequest request) {
        BigInteger customer_user_id = request.getCustomer_user_id();
        BigInteger merchant_user_id = request.getMerchant_user_id();
        Merchant merchant = selectMerchantById(merchant_user_id);
        Customer customer = selectCustomerById(customer_user_id);
        if(merchant == null) {
            System.out.println("Merchant Does Not Exist");
            return RCode.error("Merchant Does Not Exist");
        }
        if(customer == null) {
            System.out.println("Customer Does Not Exist");
            return RCode.error("Customer Does Not Exist");
        }
        String content = request.getContent();
        int rate = request.getRate();
        Date time_dc = new Date();
        String pic_dc = request.getPic_dc();
        if(content == null || content.isEmpty()) {
            content = "The user did not provide a detailed evaluation";
        }
        //            if(pic_dc == null || pic_dc.isEmpty()) {
        //                pic_dc = "";
        //            }
        if(StringUtils.isEmpty(String.valueOf(rate))) {
            System.out.println("Rate Cannot Be Empty");
            return RCode.error("Rate Cannot Be Empty");
        }
        else {
            DiningComment diningComment = new DiningComment();
            diningComment.setMerchant_id(merchant.getMerchant_id());
            diningComment.setCustomer_id(customer.getCustomer_id());
            diningComment.setContent(content);
            diningComment.setRate(rate);
            diningComment.setTime_dc(time_dc);
            diningComment.setPic_dc(pic_dc);
            diningCommentService.insertDC(diningComment);
            double avgRate = diningCommentService.getAverageRateByMerchantId(merchant_user_id);
            merchantService.updateAvgRateByMerchantId(merchant_user_id, avgRate);
            System.out.println("successful");
        }
        return RCode.ok("successful");
    }
    @PostMapping({"/delete"})
    public RCode deleteDCById(@RequestBody DeleteDiningCommentRequest request) {
        BigInteger dining_com_id = request.getDining_com_id();
        DiningComment diningComment = this.diningCommentService.selectDCById(dining_com_id);
        if(diningComment != null) {
            this.diningCommentService.deleteDCById(dining_com_id);
            double avgRate = diningCommentService.getAverageRateByMerchantId(diningComment.getMerchant_id());
            merchantService.updateAvgRateByMerchantId(diningComment.getMerchant_id(), avgRate);
            System.out.println("successful");
        }
        else {
            System.out.println("Dining Comment Does Not Exist");
            return RCode.error("Dining Comment Does Not Exist");
        }
        return RCode.ok("successful");
    }


}
