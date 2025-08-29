package home.thienph.xyahoo_server.data.mapping;

import home.thienph.xyahoo_server.data.users.UserContext;

public interface IPipeline<T, Q> {
    Q addPipeline(T data);

    Q addPipeline(IPipelineGroup<T> function);

    Q endPipeline();

    void flushPipeline(UserContext userContext);

    @FunctionalInterface
    interface IPipelineGroup<T> {
        T group();
    }
}
