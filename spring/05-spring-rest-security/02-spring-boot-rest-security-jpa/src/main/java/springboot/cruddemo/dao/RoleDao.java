package springboot.cruddemo.dao;


import springboot.cruddemo.entity.Role;

public interface RoleDao {

	public Role findRoleByName(String theRoleName);
}
