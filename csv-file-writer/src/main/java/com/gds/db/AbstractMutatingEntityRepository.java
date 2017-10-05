package com.gds.db;

import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaDelete;
import java.io.Serializable;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.Assert.notNull;

/**
 * @author Matt Vickery (matt.d.vickery@greendotsoftware.co.uk)
 * @since 03/10/2017
 */
public abstract class AbstractMutatingEntityRepository<E extends Serializable, I extends Serializable>
        extends AbstractSearchRepository<E, I> implements MutatingEntityRepository<E, I> {

    private static final Logger LOG = getLogger(AbstractMutatingEntityRepository.class);

    protected AbstractMutatingEntityRepository(final Class<E> entityType, final Class<I> idType) {
        super(entityType, idType);
    }

    @Transactional
    @Override
    public E create(final E entity, final I id) {

        notNull(entity, "Mandatory argument 'entity' is missing.");
        notNull(id, "Mandatory argument 'id' is missing.");
        try {
            getEntityManager().persist(entity);
            return entity;
        } catch (PersistenceException e) {
            LOG.error("FAILURE");
            throw e;
        }
    }

    @Transactional
    @Override
    public E upsert(final E entity, final I id) {

        notNull(entity, "Mandatory argument 'entity' is missing.");
        notNull(id, "Mandatory argument 'id' is missing.");
        try {
            return findById(id).map(e -> getEntityManager().merge(entity)).orElse(create(entity, id));
        } catch (PersistenceException e) {
            throw e;
        }
    }

    @Transactional
    @Override
    public void delete(final E entity, final I id) {

        notNull(entity, "Mandatory argument 'entity' is missing.");
        notNull(id, "Mandatory argument 'id' is missing.");
        try {
            getEntityManager().remove(entity);
        } catch (PersistenceException e) {
            throw e;
        }
    }

    @Transactional
    @Override
    public void deleteAll() {

        try {
            final CriteriaDelete<E> deleteAllCriteria
                    = getEntityManager().getCriteriaBuilder().createCriteriaDelete(getEntityType());
            deleteAllCriteria.from(getEntityType());
            stopWatchThread.get().start();
            int rowCount = getEntityManager().createQuery(deleteAllCriteria).executeUpdate();
            stopWatchThread.get().stop();
            LOG.info("Deleted: [{}] rows during deleteAll({}) in [{}ms]", rowCount, getEntityType().getSimpleName(),
                    stopWatchThread.get().getLastTaskTimeMillis());
        } finally {
            if (stopWatchThread.get().isRunning())
                stopWatchThread.get().stop();
        }
    }

    @Override
    public void detach(final E entity) {

        notNull(entity, "Mandatory argument 'entity' is missing.");
        getEntityManager().detach(entity);
    }

    @Transactional
    @Override
    public void flush() {

        getEntityManager().flush();
    }

    @Transactional
    @Override
    public List<E> updateAll(E entity) {

        // Get all non-null attributes from the supplied entity and perform a bulk update for each one.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
