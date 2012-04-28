/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.jpa.JpaCrudDao;
import org.xtremeware.iudex.dao.jpa.JpaFeedbackTypeDao;
import org.xtremeware.iudex.entity.FeedbackEntity;
import org.xtremeware.iudex.entity.FeedbackTypeEntity;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.SecurityHelper;
import org.xtremeware.iudex.vo.FeedbackTypeVo;

/**
 *
 * @author josebermeo
 */
public class FeedbackTypesService extends SimpleCrudService<FeedbackTypeVo, FeedbackTypeEntity> {

    /**
     * FeedbackTypesService constructor
     *
     * @param daoFactory
     */
    public FeedbackTypesService(AbstractDaoFactory daoFactory) {
        super(daoFactory);
    }

    /**
     * returns the JpaFeedbackTypeDao to be used.
     *
     * @return
     */
    @Override
    protected JpaCrudDao<FeedbackTypeEntity> getDao() {
        return this.getDaoFactory().getFeedbackTypeDao();
    }

    /**
     * Validate the provided FeedbackTypeVo, if the FeedbackTypeVo is not
     * correct the methods throws an exception
     *
     * @param em EntityManager
     * @param vo FeedbackTypeVo
     * @throws InvalidVoException
     */
    @Override
    public void validateVo(EntityManager em, FeedbackTypeVo vo) throws InvalidVoException {
        if (vo == null) {
            throw new InvalidVoException("Null FeedbackTypeVo");
        }
        if (vo.getName() == null) {
            throw new InvalidVoException("Null name in the provided FeedbackTypeVo");
        }
    }

    /**
     * return a list of all different FeedbackTypeVo
     *
     * @param em EntityManager
     * @return A list of all different FeedbackTypeVo
     */
    public List<FeedbackTypeVo> list(EntityManager em) {
        List<FeedbackTypeEntity> feedbackTypeEntitys = ((JpaFeedbackTypeDao) this.getDao()).getAll(em);
        if (feedbackTypeEntitys.isEmpty()) {
            return null;
        }
        ArrayList<FeedbackTypeVo> arrayList = new ArrayList<FeedbackTypeVo>();
        for (FeedbackTypeEntity feedbackTypeEntity : feedbackTypeEntitys) {
            arrayList.add(feedbackTypeEntity.toVo());
        }
        return arrayList;
    }
    
  /**
    * Remove the FeedBack Type and all the Feedback Comments associated  to it.
    * 
    * @param em entity manager
    * @param id id of the FeedBackType
    */    
    @Override
    public void remove(EntityManager em, long id) {
            List<FeedbackEntity> feedBacks = getDaoFactory().getFeedbackDao().getByTypeId(em, id);
                for (FeedbackEntity feedBack : feedBacks){
                    getDaoFactory().getFeedbackDao().remove(em,feedBack.getId());
                }

            getDao().remove(em, id);
    }
}
