package net.childman.libmvvm.common;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;

public class ErrorTransformer<T> implements FlowableTransformer<T,T> {
    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        return null;
    }
}
