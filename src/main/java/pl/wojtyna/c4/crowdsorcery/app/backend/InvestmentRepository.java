package pl.wojtyna.c4.crowdsorcery.app.backend;

@Database
public interface InvestmentRepository {
    void save(Investment investment);
    Investment get(String id);
    void delete(String id);
}
