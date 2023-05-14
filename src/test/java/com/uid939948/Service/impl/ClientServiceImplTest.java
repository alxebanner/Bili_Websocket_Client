package com.uid939948.Service.impl;

import com.uid939948.FavoritesApplication;

import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest(classes = FavoritesApplication.class)
public class ClientServiceImplTest {

    @Resource
    private ClientServiceImpl clientService;


}