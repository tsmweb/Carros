package br.com.tsmweb.carros.ui.presentation.common;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SingleLiveEvent<T> extends MediatorLiveData<T> {

    private final Set<ObserverWrapper<T>> observers = new HashSet<>();

    @MainThread
    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        final ObserverWrapper<T> wrapper = new ObserverWrapper(observer);
        observers.add(wrapper);

        super.observe(owner, wrapper);
    }

    @MainThread
    @Override
    public void removeObserver(@NonNull Observer<? super T> observer) {
        if (observers.remove(observer)) {
            super.removeObserver(observer);
            return;
        }

        final Iterator<ObserverWrapper<T>> iterator = observers.iterator();

        while (iterator.hasNext()) {
            final ObserverWrapper<T> wrapper = iterator.next();

            if (wrapper.observer.equals(observer)) {
                iterator.remove();
                super.removeObserver(wrapper);
                break;
            }
        }
    }

    @MainThread
    @Override
    public void setValue(T value) {
        for (ObserverWrapper wrapper : observers) {
            wrapper.newValue();
        }

        super.setValue(value);
    }

    private class ObserverWrapper<T> implements Observer<T> {

        private boolean pending = false;
        private final Observer<T> observer;

        public ObserverWrapper(final Observer<T> observer) {
            this.observer = observer;
        }

        @Override
        public void onChanged(T t) {
            if (pending) {
                pending = false;
                observer.onChanged(t);
            }
        }

        public void newValue() {
            pending = true;
        }

    }

}
