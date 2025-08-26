package home.thienph.xyahoo_server.data.mapping;

import java.io.IOException;

@FunctionalInterface
public interface IPipelineGroup<T> {
    T groupPipeline();
}
