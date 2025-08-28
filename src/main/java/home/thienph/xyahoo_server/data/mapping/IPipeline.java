package home.thienph.xyahoo_server.data.mapping;

import io.netty.channel.Channel;

public interface IPipeline<T, Q> {
    Q addPipeline(T data);

    Q addPipeline(IPipelineGroup<T> function);

    Q endPipeline();

    void flushPipeline(Channel channel);

    @FunctionalInterface
    interface IPipelineGroup<T> {
        T group();
    }
}
