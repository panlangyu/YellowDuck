package com.duck.yellowduck.controller;


import com.duck.yellowduck.domain.service.RedEnvelopesService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户红包管理
 */
@RestController
@RequestMapping("/red")
@Api(value="用户发红包", tags={"redEnvelopes service"})
public class RedEnvelopesController {


    @Autowired
    private RedEnvelopesService redEnvelopesService;                //红包Service




}
