package br.com.tsmweb.carros.data.repository;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import br.com.tsmweb.carros.data.dao.CarrosDatabase;

public class RepositoryLocator {

    private static final String TAG = RepositoryLocator.class.getSimpleName();

    private static RepositoryLocator instance;

    private Context context;
    private Map<String, IRepository> cache;

    private RepositoryLocator(Context context) {
        this.context = context;
        this.cache = new HashMap<>();
    }

    public static RepositoryLocator getInstance(Context context) {
        if (instance == null) {
            instance = new RepositoryLocator(context);
        }

        return instance;
    }

    private IRepository getRepositoryCache(String repositoryName) {
        return cache.get(repositoryName);
    }

    private void addRepositoryCache(String repositoryName, IRepository repository) {
        if (!cache.containsKey(repositoryName)) {
            cache.put(repositoryName, repository);
        }
    }

    private IRepository lookup(Class<? extends IRepository> repositoryClass) {
        if (repositoryClass.isAssignableFrom(ICarroRepository.class)) {
            return new CarroRepository(CarrosDatabase.getCarroDatabase(context).carroDAO());
        }

        throw new IllegalArgumentException("unknown repository class " + repositoryClass.getName());
    }

    @SuppressWarnings("unchecked")
    public <T extends IRepository> T locate(Class<T> repositoryClass) {
        IRepository repositoryInstance = getRepositoryCache(repositoryClass.getName());

        if (repositoryInstance != null) {
            return (T) repositoryInstance;
        }

        repositoryInstance = lookup(repositoryClass);
        addRepositoryCache(repositoryClass.getName(), repositoryInstance);

        return (T) repositoryInstance;
    }

}
