package home.thienph.xyahoo_server.data.builder;

public interface IPipeline<T, H> {
    T addPipeline(H process);

    T endPipeline();
}
