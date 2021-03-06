package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.SubjectEntity;

/**
 * DAO for subject entities. Implements additionally some useful finders by name
 * or by professor id
 *
 * @author healarconr
 */
public class SubjectDao extends Dao<SubjectEntity> {

    /**
     * Returns a list of subject entities which name contains the argument name
     *
     * @param em the entity manager
     * @param name the subject's name
     * @return a list of matched subject entities
     */
    public List<SubjectEntity> getByName(EntityManager em, String name) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        return em.createNamedQuery("getSubjectsByName", SubjectEntity.class).setParameter("name", "%" + name + "%").getResultList();
    }

    /**
     * Returns a list of subject entities associated with a specific professor
     *
     * @param em the entity manager
     * @param professorId the professor's id
     * @return a list of matched subject entities
     */
    public List<SubjectEntity> getByProfessorId(EntityManager em, long professorId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        return em.createNamedQuery("getSubjectsByProfessorId", SubjectEntity.class).setParameter("professorId", professorId).getResultList();
    }
}
