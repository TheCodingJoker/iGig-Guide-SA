package com.wecodesa.igigguidesa.explore.data.remote.api;

import com.wecodesa.igigguidesa.explore.data.remote.dto.EventDto;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SupabaseEventApi {

    // The venues table is capitalized ("Venues") in Supabase; PostgREST is case-sensitive.
    // ticket_types does not exist in the schema, so it cannot be embedded.
    @GET("events?select=*,%22Venues%22!inner(*)")
    Single<List<EventDto>> searchEventsByCity(@Query("\"Venues\".city") String cityFilter);

    @GET("events?select=*,%22Venues%22(*)")
    Single<List<EventDto>> getEventById(@Query("id") String idFilter);
}