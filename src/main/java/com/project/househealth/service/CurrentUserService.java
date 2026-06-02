package com.project.househealth.service;

import com.project.househealth.entity.User;

public interface CurrentUserService {

    User getCurrentUser();

    Long getCurrentUserId();
}
