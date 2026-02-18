package com.projeto.service;

import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    public CurrentUser getCurrentUser() {
        return new CurrentUser(1L);
    }
}

