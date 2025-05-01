package vn.dathocjava.dathocjava_sample.service.interfaceClass;

import vn.dathocjava.dathocjava_sample.dto.request.RoleDTO;
import vn.dathocjava.dathocjava_sample.model.Role;

import java.util.List;

public interface IRoleService {
    Role createRole (RoleDTO roleDTO);
    List<Role> getRoleList();
}
