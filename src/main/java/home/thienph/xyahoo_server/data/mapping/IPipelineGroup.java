package home.thienph.xyahoo_server.data.mapping;

@FunctionalInterface
public interface IPipelineGroup<T> {
    T groupPipeline();
}
