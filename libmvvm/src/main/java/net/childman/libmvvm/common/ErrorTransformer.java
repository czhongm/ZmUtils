package net.childman.libmvvm.common;

import org.reactivestreams.Publisher;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableTransformer;

public class ErrorTransformer<T> implements FlowableTransformer<T,T> {
    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        return null;
    }
}
