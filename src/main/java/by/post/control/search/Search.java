package by.post.control.search;

import java.util.Collection;

public interface Search<T> {

    Collection<T> search(String searchValue);

    void cancel();
}
