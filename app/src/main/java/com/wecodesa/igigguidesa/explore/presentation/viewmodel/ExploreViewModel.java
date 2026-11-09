package com.wecodesa.igigguidesa.explore.presentation.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wecodesa.igigguidesa.explore.domain.model.Event;
import com.wecodesa.igigguidesa.explore.domain.model.EventType;
import com.wecodesa.igigguidesa.explore.domain.usecase.FilterEventsByTypeUseCase;
import com.wecodesa.igigguidesa.explore.domain.usecase.SearchEventsByAreaUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class ExploreViewModel extends ViewModel {

    private final SearchEventsByAreaUseCase searchEventsByAreaUseCase;
    private final FilterEventsByTypeUseCase filterEventsByTypeUseCase;

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<List<Event>> events = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private List<Event> allEvents;
    private EventType selectedType;

    @Inject
    public ExploreViewModel(SearchEventsByAreaUseCase searchEventsByAreaUseCase,
                            FilterEventsByTypeUseCase filterEventsByTypeUseCase) {
        this.searchEventsByAreaUseCase = searchEventsByAreaUseCase;
        this.filterEventsByTypeUseCase = filterEventsByTypeUseCase;
    }

    public LiveData<List<Event>> getEvents() {
        return events;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void searchEventsByArea(@NonNull String area) {
        isLoading.setValue(true);

        disposables.add(
                searchEventsByAreaUseCase.execute(area)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                result -> {
                                    allEvents = result;
                                    events.setValue(applyFilter());
                                    isLoading.setValue(false);
                                },
                                throwable -> {
                                    errorMessage.setValue(throwable.getMessage());
                                    isLoading.setValue(false);
                                }
                        )
        );
    }

    public void filterByType(EventType eventType) {
        selectedType = eventType;
        if (allEvents != null) {
            events.setValue(applyFilter());
        }
    }

    public void clearFilter() {
        selectedType = null;
        if (allEvents != null) {
            events.setValue(applyFilter());
        }
    }

    private List<Event> applyFilter() {
        if (selectedType == null) {
            return allEvents;
        }
        return filterEventsByTypeUseCase.execute(allEvents, selectedType);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}