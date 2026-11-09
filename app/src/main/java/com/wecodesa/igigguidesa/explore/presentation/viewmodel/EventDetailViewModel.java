package com.wecodesa.igigguidesa.explore.presentation.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wecodesa.igigguidesa.explore.domain.model.Event;
import com.wecodesa.igigguidesa.explore.domain.usecase.GetEventDetailsUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class EventDetailViewModel extends ViewModel {

    private final GetEventDetailsUseCase getEventDetailsUseCase;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<Event> event = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private String loadedEventId;

    @Inject
    public EventDetailViewModel(GetEventDetailsUseCase getEventDetailsUseCase) {
        this.getEventDetailsUseCase = getEventDetailsUseCase;
    }

    public LiveData<Event> getEvent() {
        return event;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void load(@NonNull String eventId) {
        // Already loaded (e.g. after a screen rotation): keep what we have
        if (eventId.equals(loadedEventId)) {
            return;
        }
        loadedEventId = eventId;
        isLoading.setValue(true);

        disposables.add(
                getEventDetailsUseCase.execute(eventId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                result -> {
                                    event.setValue(result);
                                    isLoading.setValue(false);
                                },
                                throwable -> {
                                    loadedEventId = null; // allow a retry
                                    errorMessage.setValue(throwable.getMessage());
                                    isLoading.setValue(false);
                                }
                        )
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}