package systems.porto.api.persistence;

import java.util.List;
import java.util.Optional;

/**
 * Shared CRUD persistence port for porto-api applications.
 *
 * @param <T>  entity / DTO type returned by reads
 * @param <ID> identifier type
 * @param <C>  create-request type
 * @param <U>  update-request type
 */
public interface Persistence<T, ID, C, U> {

    List<T> list(int page, int size);

    long count();

    Optional<T> findById(ID id);

    List<T> search(String term);

    T create(C request);

    Optional<T> update(ID id, U request);

    boolean delete(ID id);
}
