package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.ProgramEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public interface ProgramDaoInterface extends CrudDaoInterface<ProgramEntity> {

    /**
     * Search a program which name contains the given parameter name
     *
     * @param em the entity manager
     * @param name
     * @return Return a list of programEntity objects
     */
    public List<ProgramEntity> getByNameLike(EntityManager em, String name) throws
            DataBaseException;

    public List<ProgramEntity> getAll(EntityManager em) throws
            DataBaseException;
}