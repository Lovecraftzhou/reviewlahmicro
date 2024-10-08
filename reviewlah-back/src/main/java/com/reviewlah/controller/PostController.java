package com.reviewlah.controller;

import com.reviewlah.common.util.RCode;
import com.reviewlah.controller.form.*;
import com.reviewlah.db.pojo.Customer;
import com.reviewlah.db.pojo.Post;
import com.reviewlah.db.pojo.User;
import com.reviewlah.service.CustomerService;
import com.reviewlah.service.PostService;
import com.reviewlah.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping({"/post"})
@Tag(name = "帖子模块")
public class PostController {
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private PostService postService;

    @Operation(summary = "发布新帖子")
    @PostMapping({"/insert"})
    public RCode insertPost(@RequestBody InsertPostRequest request) {
        BigInteger user_id = request.getUser_id();
        String title = request.getTitle();
        String content = request.getContent();
        String pic_post = request.getPic_post();
        Date date = new Date();
        User user = this.userService.selectUserById(user_id);
        if(user != null) {
            if(content == null || content.isEmpty()) {
                System.out.println("Content Cannot Be Empty");
                return RCode.error("Content Cannot Be Empty");
            }
            if(title == null || title.isEmpty()) {
                System.out.println("Title Cannot Be Empty");
                return RCode.error("Title Cannot Be Empty");
            }
            if(pic_post == null || pic_post.isEmpty()) pic_post = "http://defaultPostPic";
            BigInteger customer_id = this.customerService.selectCustomerIdByUserId(user_id);
            Post post = new Post();
            post.setCustomer_id(customer_id);
            post.setTitle(title);
            post.setContent(content);
            post.setPic_post(pic_post);
            post.setTime_post(date);
            this.postService.insertPost(post);
            System.out.println("successful");
        }
        else {
            System.out.println("Failed");
            return RCode.error("Failed");
        }
        return RCode.ok("successful");
    }
    @Operation(summary = "删除帖子")
    @PostMapping({"/delete"})
    public RCode deletePost(@RequestBody DeletePostRequest request) {
        BigInteger post_id = request.getPost_id();
        Post post = this.postService.selectPostByPostId(post_id);
        if(post != null) {
            this.postService.deletePostByPostId(post_id);
            System.out.println("successful");
        }
        else {
            System.out.println("User Does Not Exist");
            return RCode.error("User Does Not Exist");
        }
        return RCode.ok("successful");
    }

    @Operation(summary = "获取帖子")
    @PostMapping({"/mypost"})
    public RCode selectPostByCustomerId(@RequestBody SelectPostByCustomerIdRequest request) {
        BigInteger user_Id = request.getUser_id();
        User user = this.userService.selectUserById(user_Id);
        ArrayList<HashMap> list = new ArrayList<>();
        if(user != null && user.getType() == 1) {
            Customer customer = this.customerService.selectCustomerByUserId(user_Id);
            if(customer != null) {
                list = this.postService.selectPostByCustomerId(customer.getCustomer_id());
                System.out.println("successful");
            }

        }
        else {
            System.out.println("User Does Not Exist");
            return RCode.error("User Does Not Exist");
        }
        return RCode.ok().put("list", list);
    }

    @Operation(summary = "获取所有帖子")
    @PostMapping({"/homepage"})
    public RCode selectAllPostExceptMine(@RequestBody SelectPostByCustomerIdRequest request) {
        BigInteger user_id = request.getUser_id();
        User user = this.userService.selectUserById(user_id);
        ArrayList<HashMap> list = new ArrayList<>();
        if(user != null) {
            BigInteger customer_id = this.customerService.selectCustomerIdByUserId(user_id);
            list = this.postService.selectAllPostExceptMine(customer_id);
            System.out.println("successful");
        }
        else {
            System.out.println("User Does Not Exist");
            return RCode.error("User Does Not Exist");
        }
        return RCode.ok().put("list", list);
    }

    @Operation(summary = "获取相对Post")
    @PostMapping({"/homepage/relative"})
    public RCode selectRelativePost(@RequestBody SelectRelativePostRequest request) {
        BigInteger user_id = request.getUser_id();
        String keyword = request.getKeyword();
        User user = this.userService.selectUserById(user_id);
        ArrayList<HashMap> list = new ArrayList<>();
        if(user != null) {
            BigInteger customer_id = this.customerService.selectCustomerIdByUserId(user_id);
            list = this.postService.selectRelativePost(keyword, customer_id);
            System.out.println("successful");
        }
        else {
            System.out.println("User Does Not Exist");
            return RCode.error("User Does Not Exist");
        }
        return RCode.ok().put("list", list);
    }

    @Operation(summary = "获取详细信息")
    @PostMapping({"/detail"})
    public RCode selectPostByPostId(@RequestBody SelectPostByPostIdRequest request) {
        BigInteger post_id = request.getPost_id();
        Post post = this.postService.selectPostByPostId(post_id);
        if(post == null) {
            System.out.println("Post Does Not Exist");
            return RCode.error("Post Does Not Exist");
        }
        else {
            System.out.println("successful");
        }
        return RCode.ok().put("list", post);
    }
}
