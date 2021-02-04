package com.google.common.collect;

import com.google.common.base.Function;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

public class ImmutableComputingMap<K, V> extends ImmutableMap<K, V> {

    private final Set<K> keySet;

    private final Function<K, V> valueProvider;

    public ImmutableComputingMap(Set<K> keySet ,Function<K, V> valueProvider) {
        checkNotNull(keySet);
        checkNotNull(valueProvider);
        this.keySet = keySet;
        this.valueProvider = valueProvider;
    }


    /**
     * Returns the number of key-value mappings in this map.  If the
     * map contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of key-value mappings in this map
     */
    @Override
    public int size() {
        return keySet.size();
    }

    @Override
    public V get(@Nullable Object key) {
        if(key == null || !keySet.contains(key)){
            return null;
        }
        return valueProvider.apply((K)key);
    }

    @Override
    ImmutableSet<Entry<K, V>> createEntrySet() {
        Set<Entry<K,V>> result = new HashSet<>();
        for(K key : keySet){
            Entry<K,V> insert =
            result.add(insert);
        }
        return ImmutableSet.copyOf(result);
    }

    @Override
    ImmutableSet<K> createKeySet() {
        return ImmutableSet.copyOf(keySet);
    }

    @Override
    ImmutableCollection<V> createValues() {
        // noch in stream abändern
        Set<V> result = new HashSet<>();
        for(K key : this.keySet){
            result.add(get(key));
        }
        return ImmutableSet.copyOf(result);
    }

    @Override
    boolean isPartialView() {
        return false;
    }
}
