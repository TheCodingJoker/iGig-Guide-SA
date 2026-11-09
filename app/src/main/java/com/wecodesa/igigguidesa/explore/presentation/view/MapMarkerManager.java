package com.wecodesa.igigguidesa.explore.presentation.view;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wecodesa.igigguidesa.explore.domain.model.Event;

import java.util.List;

public class MapMarkerManager {

    private final GoogleMap googleMap;

    public MapMarkerManager(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public void updateMarkers(List<Event> events) {
        if (googleMap == null || events == null) {
            return;
        }

        googleMap.clear();
        for (Event event : events) {
            if (event.getVenue() != null) {
                LatLng position = new LatLng(
                        event.getVenue().getLatitude(),
                        event.getVenue().getLongitude()
                );
                googleMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(event.getTitle())
                        .snippet(event.getVenue().getName()));
            }
        }
    }
}