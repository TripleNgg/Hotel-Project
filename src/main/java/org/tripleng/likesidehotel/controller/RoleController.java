package org.tripleng.likesidehotel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tripleng.likesidehotel.exception.RoleAlreadyExistException;
import org.tripleng.likesidehotel.model.Role;
import org.tripleng.likesidehotel.model.User;
import org.tripleng.likesidehotel.service.RoleService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles/v1")
@CrossOrigin(origins = "https://hotel-project-client.onrender.com")
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/all-roles")
    public ResponseEntity<List<Role>> getAllRoles(){
        return new ResponseEntity<>(roleService.getRoles(), HttpStatus.FOUND);
    }

    @PostMapping("/create-new-role")
    public ResponseEntity<String> createRole(@RequestBody Role role){
        try {
            roleService.createRole(role);
            return ResponseEntity.ok("New role created successfully!");
        }catch (RoleAlreadyExistException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{roleId}")
    public void deleteRole(@PathVariable("roleId") Long roleId){
        roleService.deleteRole(roleId);
    }

    @DeleteMapping("/remove-all-users-from-role/{roleId}")
    public Role removeAllUsersFromRole(@PathVariable Long roleId){
        return roleService.removeAllUsersFromRole(roleId);
    }

    @PostMapping("/remove-user-from-role")
    public User removeUserFromRole(@RequestParam("userId") Long userId,@RequestParam("roleId") Long roleId){
        return roleService.removeUserFromRole(userId,roleId);
    }

    @PostMapping("/assign-role-to-user")
    public User assignRoleToUser(@RequestParam("userId") Long userId,@RequestParam("roleId") Long roleId){
        return roleService.assignRoleToUser(userId,roleId);
    }



}
