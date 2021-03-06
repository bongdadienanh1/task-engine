package com.alexkasko.tasks.impl;

import com.alexkasko.tasks.TaskStageProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static java.lang.System.currentTimeMillis;

/**
 * User: alexkasko
 * Date: 5/23/12
 */
public interface ReportStageProcessor extends TaskStageProcessor {
}

@Service("ReportStageProcessor")
class ReportStageProcessorImpl implements ReportStageProcessor {
    @Inject
    private TaskManagerIface taskManager;
    @Inject
    private SuspensionChecker checker;

    @Override
    @Transactional
    public void process(long taskId) throws Exception {
        long awaitTime = taskManager.load(taskId).getPayload();
        long start = currentTimeMillis();
        while (currentTimeMillis() - start < awaitTime) {
            checker.checkSuspended(taskId);
            Thread.sleep(100);
        }
    }
}
