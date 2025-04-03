package springboot.cruddemo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import springboot.cruddemo.entity.Role;

@Repository
public class RoleDaoImpl implements RoleDao {

	private EntityManager entityManager;

	public RoleDaoImpl(EntityManager theEntityManager) {
		entityManager = theEntityManager;
	}

	@Override
	public Role findRoleByName(String theRoleName) {

		TypedQuery<Role> theQuery = entityManager.createQuery("from Role where name=:roleName", Role.class);
		theQuery.setParameter("roleName", theRoleName);

		Role theRole = null;

		try {
			theRole = theQuery.getSingleResult();
		} catch (Exception ignored) {
        }

		return theRole;
	}
}
