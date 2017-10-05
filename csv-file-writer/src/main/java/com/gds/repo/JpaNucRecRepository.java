package com.gds.repo;

import com.gds.db.AbstractMutatingEntityRepository;
import com.gds.entity.NucleusRec;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.springframework.util.Assert.notNull;

/**
 * @author Matt Vickery (matt.d.vickery@greendotsoftware.co.uk)
 * @since 03/10/2017
 */
@Repository
public class JpaNucRecRepository extends AbstractMutatingEntityRepository<NucleusRec, Long> {

    public JpaNucRecRepository() {
        super(NucleusRec.class, Long.class);
    }

    @PersistenceContext(name = "csv-file-writer")
    @Override
    public void setEntityManager(final EntityManager entityManager) {
        notNull(entityManager, "Mandatory argument 'entityManager' is missing.");
        super.setEntityManager(entityManager);
    }
}