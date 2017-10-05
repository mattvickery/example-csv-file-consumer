package com.gds.batch.tasklet;

import com.gds.db.MutatingEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.Assert.state;

/**
 * @author Matt Vickery (matt.d.vickery@greendotsoftware.co.uk)
 * @since 03/10/2017
 */
public class RepositoryCleansingTasklet implements Tasklet {

    private static final Logger LOG = LoggerFactory.getLogger(RepositoryCleansingTasklet.class);
    private final List<MutatingEntityRepository> repositories;

    public RepositoryCleansingTasklet(final List<MutatingEntityRepository> repositories) {
        state(repositories != null, "Mandatory argument 'repositories' is missing.");
        this.repositories = repositories;
    }

    public RepositoryCleansingTasklet(final MutatingEntityRepository repository) {
        state(repository != null, "Mandatory argument 'repository' is missing.");
        this.repositories = new ArrayList<>();
        this.repositories.add(repository);
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        repositories.forEach(repo -> repo.deleteAll());
        return RepeatStatus.FINISHED;
    }
}
