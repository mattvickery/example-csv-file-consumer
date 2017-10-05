package com.gds.db;

import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.Assert.notNull;

/**
 * @author Matt Vickery (matt.d.vickery@greendotsoftware.co.uk)
 * @since 03/10/2017
 */
public class AbstractSearchRepository<E extends Serializable, I extends Serializable>
        implements SearchRepository<E, I> {

    private final Logger LOG = getLogger(AbstractSearchRepository.class);
    private final Class<E> entityType;
    private final Class<I> idType;
    protected ThreadLocal<StopWatch> stopWatchThread = new ThreadLocal<StopWatch>() {
        @Override
        protected StopWatch initialValue() {
            return new StopWatch();
        }
    };
    private EntityManager entityManager;

    public AbstractSearchRepository(final Class<E> entityType, final Class<I> idType) {

        notNull(entityType, "Mandatory argument 'entityType' is missing.");
        notNull(idType, "Mandatory argument 'idType' is missing.");
        this.entityType = entityType;
        this.idType = idType;
    }

    protected EntityManager getEntityManager() {

        return this.entityManager;
    }

    protected void setEntityManager(final EntityManager entityManager) {

        notNull(entityManager, "Mandatory argument 'entityManager' is missing.");
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public Optional<E> findById(final I id) {

        notNull(id, "Mandatory argument 'id' is missing.");
        return Optional.of(entityManager.find(entityType, id));
    }

    @Transactional(readOnly = true)
    public List<E> findAll() {

        try {
            final CriteriaQuery<E> query = getEntityManager().getCriteriaBuilder().createQuery(entityType);
            final Root<E> root = query.from(entityType);
            final CriteriaQuery<E> rootQuery = query.select(root);
            final TypedQuery<E> allQuery = getEntityManager().createQuery(rootQuery);
            stopWatchThread.get().start();
            final List<E> rows = allQuery.getResultList();
            stopWatchThread.get().stop();
            LOG.info("Located: [{} rows] during findAll({}) in [{}ms]", rows.size(), entityType.getSimpleName(),
                    stopWatchThread.get().getLastTaskTimeMillis());
            return rows;
        } finally {
            if (stopWatchThread.get().isRunning())
                stopWatchThread.get().stop();
        }
    }

    @Transactional(readOnly = true)
    public List<E> findAll(final long offset, final long size) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Transactional(readOnly = true)
    public long count() {

        final CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<Long> rootQuery = criteriaBuilder.createQuery(Long.class);
        rootQuery.select(criteriaBuilder.count(rootQuery.from(getEntityType())));

        return getEntityManager().createQuery(rootQuery).getSingleResult();
    }

    @Transactional(readOnly = true)
    public Optional<E> findByCharacterType(final String key, final String value) {

        notNull(key, "Mandatory argument 'key' is missing.");
        notNull(value, "Mandatory argument 'value' is missing.");
        final CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<E> query = builder.createQuery(getEntityType());
        final Root<E> root = query.from(getEntityType());
        query.select(root).where(
                builder.and(builder.equal(root.get(key), value))
        ).distinct(true);
        final List<E> results = getEntityManager().createQuery(query).getResultList();
        if ((results != null) && (results.size() == 1))
            return Optional.of(results.get(0));
        return Optional.empty();
    }

    protected Class<E> getEntityType() {

        return entityType;
    }

    protected Class<I> getIdType() {

        return idType;
    }
}
