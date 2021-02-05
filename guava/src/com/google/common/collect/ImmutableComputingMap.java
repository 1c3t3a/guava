package com.google.common.collect;

import com.google.common.base.Function;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code Map()} implementation for {@link ImmutableMap}.
 *
 * @author Dominik Jantschar
 * @author Sebastian Kersting
 */
public final class ImmutableComputingMap<K, V> extends ImmutableMap<K, V>{

    /**
     * The given KeySet with at initialization of the class.
     */
    private final Set<K> keySet;

    /**
     * The Store for the already computed Key, Value pairs.
     */
    private final Map<K,V> data = CompactHashMap.create();

    /**
     * The function for computing the Values with the given keys.
     */
    private final Function<K, V> valueProvider;

    /**
     * Createds the ImmutableComputingMap with a given KeySet and a Function.
     * Computes and chaches the values when needed.
     * @param keySet the given keyset.
     * @param valueProvider the Function for computation.
     */
    private ImmutableComputingMap(Set<K> keySet ,Function<K, V> valueProvider) {
        checkNotNull(keySet);
        checkNotNull(valueProvider);
        this.keySet = ImmutableSet.copyOf(keySet);
        this.valueProvider = valueProvider;
    }

    public static <K,V> ImmutableMap<K, V> create(Set<K> keySet ,Function<K, V> valueProvider){
        return new ImmutableComputingMap<>(keySet, valueProvider);
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
        if(data.containsKey(key)){
            return data.get(key);
        }
        else {
            V result = valueProvider.apply((K)key);
            data.put((K)key, result);
            return result;
        }
    }

    @Override
    ImmutableSet<Entry<K, V>> createEntrySet() {
        createValues();
        return ImmutableSet.copyOf(data.entrySet());
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
            if(!data.containsKey(key)){
                result.add(get(key));
            }
        }
        return ImmutableSet.copyOf(result);
    }

    @Override
    boolean isPartialView() {
        return false;
    }
}
