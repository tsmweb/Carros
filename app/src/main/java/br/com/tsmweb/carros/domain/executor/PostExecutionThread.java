package br.com.tsmweb.carros.domain.executor;

import io.reactivex.Scheduler;

public interface PostExecutionThread {

    Scheduler getScheduler();

}
