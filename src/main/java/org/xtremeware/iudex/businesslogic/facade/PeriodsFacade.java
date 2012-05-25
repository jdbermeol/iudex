package org.xtremeware.iudex.businesslogic.facade;

import java.util.*;
import javax.persistence.*;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelper;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.PeriodVo;

public class PeriodsFacade extends AbstractFacade {

    public PeriodsFacade(ServiceFactory serviceFactory, EntityManagerFactory emFactory) {
        super(serviceFactory, emFactory);
    }

    public void removePeriod(long periodId) throws DataBaseException {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            getServiceFactory().createPeriodsService().remove(entityManager, periodId);
            transaction.commit();
        } catch (DataBaseException exception) {
            getServiceFactory().createLogService().error(exception.getMessage(), exception);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
    }

    /**
     * Persist a new Period with the specified year and semester.
     *
     * @param em entity manager
     * @param year
     * @param semester
     * @return Returns null if there is a problem while persisting (logs all
     * errors) and throws an exception if data isn't valid.
     */
    public PeriodVo addPeriod(int year, int semester) 
            throws MultipleMessagesException, DataBaseException, DuplicityException {
       
        PeriodVo periodVo = new PeriodVo();
        periodVo.setYear(year);
        periodVo.setSemester(semester);
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            periodVo = getServiceFactory().createPeriodsService().create(entityManager, periodVo);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().createLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkException(exception, MultipleMessagesException.class);
            FacadesHelper.checkExceptionAndRollback(entityManager, transaction, exception, DuplicityException.class);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        }finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return periodVo;
    }

    public List<PeriodVo> listPeriods() throws Exception {
        List<PeriodVo> periodVos = new ArrayList<PeriodVo>();
        EntityManager entityManager = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            periodVos = getServiceFactory().createPeriodsService().list(entityManager);

        } catch (Exception enException) {
            getServiceFactory().createLogService().error(enException.getMessage(), enException);
            throw new RuntimeException(enException);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return periodVos;

    }
}
