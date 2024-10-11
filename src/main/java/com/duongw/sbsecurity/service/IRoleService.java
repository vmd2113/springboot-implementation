package com.duongw.sbsecurity.service;

import com.duongw.sbsecurity.entity.Role;

public interface IRoleService {

    Role getAllRole();
    Role getRoleById(Long id);
}
