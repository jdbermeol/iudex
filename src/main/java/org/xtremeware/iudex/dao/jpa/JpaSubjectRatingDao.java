package org.xtremeware.iudex.dao.jpa;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.dao.SubjectRatingDao;
import org.xtremeware.iudex.entity.SubjectEntity;
import org.xtremeware.iudex.entity.SubjectRatingEntity;
import org.xtremeware.iudex.entity.UserEntity;
import org.xtremeware.iudex.vo.RatingSummaryVo;
import org.xtremeware.iudex.vo.SubjectRatingVo;

/**
 * JPADAO for the SubjectRatingVo.
 * 
 * @author josebermeo
 */
public class JpaSubjectRatingDao extends JpaCrudDao<SubjectRatingVo, SubjectRatingEntity> implements SubjectRatingDao<EntityManager> {

    /**
     * Returns a list of SubjectRatings Value objects given the subject
     *
     * @param em the DataAccessAdapter
     * @param subjectId subject id
     * @return list of SubjectRatingVo
     */
    @Override
    public List<SubjectRatingVo> getBySubjectId(DataAccessAdapter<EntityManager> em, Long subjectId) {
        checkDataAccessAdapter(em);
        List<SubjectRatingEntity> list = em.getDataAccess().createNamedQuery("getSubjectRatingBySubjectId", SubjectRatingEntity.class).setParameter("subjectId", subjectId).getResultList();
        return entitiesToVos(list);
    }

    /**
     * Returns a rating given by a user, identified by userId, to a subject,
     * identified by subjectId.
     *
     * @param em the DataAccessAdapter
     * @param subjectId subject id
     * @param userId user id
     * @return a SubjectRatingEntity
     */
    @Override
    public SubjectRatingVo getBySubjectIdAndUserId(DataAccessAdapter<EntityManager> em, Long subjectId, Long userId) {
        checkDataAccessAdapter(em);
        try {
            return em.getDataAccess().createNamedQuery("getSubjectRatingBySubjectIdAndUserId", SubjectRatingEntity.class).setParameter("subjectId", subjectId).setParameter("userId", userId).getSingleResult().toVo();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    /**
     * Returns a list of SubjectRatings value Objects given the user
     *
     * @param em the DataAccessAdapter
     * @param userId user id
     * @return list of SubjectRatingVo
     */
    @Override
    public List<SubjectRatingVo> getByUserId(DataAccessAdapter<EntityManager> em, Long userId) {
        checkDataAccessAdapter(em);
        List<SubjectRatingEntity> list = em.getDataAccess().createNamedQuery("getUserRatingBySubjectId").setParameter("userId", userId).getResultList();
        return entitiesToVos(list);
    }

    /**
     * Returns a summary of the ratings given a subject.
     *
     * @param em the DataAccessAdapter
     * @param subjectId subject id
     * @return a RatingSummaryVo object
     */
    @Override
    public RatingSummaryVo getSummary(DataAccessAdapter<EntityManager> em, Long subjectId) {
        checkDataAccessAdapter(em);
        RatingSummaryVo rsv = new RatingSummaryVo();

        try {
            rsv.setPositive(em.getDataAccess().createNamedQuery("countPositiveSubjectRating", Long.class).setParameter("subjectId", subjectId).getSingleResult().intValue());
        } catch (NoResultException noResultException) {
            return null;
        }

        try {
            rsv.setNegative(em.getDataAccess().createNamedQuery("countNegativeSubjectRating", Long.class).setParameter("subjectId", subjectId).getSingleResult().intValue());
        } catch (NoResultException noResultException) {
            return null;
        }

        return rsv;

    }

    @Override
    protected SubjectRatingEntity voToEntity(DataAccessAdapter<EntityManager> em, SubjectRatingVo vo) {
        SubjectRatingEntity entity = new SubjectRatingEntity();
        entity.setId(vo.getId());
        entity.setSubject(em.getDataAccess().getReference(SubjectEntity.class, vo.getEvaluetedObjectId()));
        entity.setUser(em.getDataAccess().getReference(UserEntity.class, vo.getUser()));
        entity.setValue(vo.getValue());
        return entity;
    }
    
    private List<SubjectRatingVo> entitiesToVos(List<SubjectRatingEntity> list){
        ArrayList<SubjectRatingVo> arrayList = new ArrayList<SubjectRatingVo>();
        for (SubjectRatingEntity entity : list) {
            arrayList.add(entity.toVo());
        }
        return arrayList;
    }

    @Override
    protected Class getEntityClass() {
        return SubjectRatingEntity.class;
    }
}
