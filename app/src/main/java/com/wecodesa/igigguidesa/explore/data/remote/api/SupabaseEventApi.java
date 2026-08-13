package com.wecodesa.igigguidesa.explore.data.remote.api;

import com.wecodesa.igigguidesa.explore.data.remote.dto.EventDto;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SupabaseEventApi {

    @GET("events?select=*,venues(*)")
    Single<List<EventDto>> searchEventsByCity(@Query("venues.city") String cityFilter);

    @GET("events?select=*,venues(*)")
    Single<List<EventDto>> getEventById(@Query("id") String idFilter);
}