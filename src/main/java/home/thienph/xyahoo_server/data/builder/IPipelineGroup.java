package home.thienph.xyahoo_server.data.builder;

@FunctionalInterface
public interface IPipelineGroup<T> {
    T groupPipeline();
}
