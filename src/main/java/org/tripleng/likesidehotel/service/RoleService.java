package org.tripleng.likesidehotel.service;

import org.tripleng.likesidehotel.model.Role;
import org.tripleng.likesidehotel.model.User;

import java.util.List;

public interface RoleService {

    List<Role> getRoles();
    Role createRole(Role role);
    void deleteRole(Long id);
    Role findRoleByName(String name);
    User removeUserFromRole(Long UserId,Long roleId);
    User assignRoleToUser(Long UserId,Long roleId);
    Role removeAllUsersFromRole(Long roleId);
}
