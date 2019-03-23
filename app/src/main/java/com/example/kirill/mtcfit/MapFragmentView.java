package com.example.kirill.mtcfit;

import android.app.Activity;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.SupportMapFragment;

import android.support.v7.app.AppCompatActivity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.common.LocationDataSourceHERE;
import com.here.android.positioning.StatusListener;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.LocationDataSourceHERE;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.SupportMapFragment;
import com.here.android.mpa.mapping.MapState;
import com.here.android.positioning.StatusListener;

import java.io.File;
import java.lang.ref.WeakReference;
//import java.util.Map;

public class MapFragmentView implements PositioningManager.OnPositionChangedListener{
    private SupportMapFragment m_mapFragment;
    private AppCompatActivity m_activity;
    private Map m_map;
    private PositioningManager mPositioningManager;
    private LocationDataSourceHERE mHereLocation;
    // flag that indicates whether maps is being transformed
    private boolean mTransforming;
    // callback that is called when transforming ends
    private Runnable mPendingUpdate;


    @Override
    public void onPositionUpdated(final PositioningManager.LocationMethod locationMethod, final GeoPosition geoPosition, final boolean mapMatched) {
        final GeoCoordinate coordinate = geoPosition.getCoordinate();
        if (mTransforming) {
            mPendingUpdate = new Runnable() {
                @Override
                public void run() {
                    onPositionUpdated(locationMethod, geoPosition, mapMatched);
                }
            };
        } else {
            m_map.setCenter(coordinate, Map.Animation.BOW);
        }
    }

    @Override
    public void onPositionFixChanged(PositioningManager.LocationMethod locationMethod, PositioningManager.LocationStatus locationStatus) {
        // ignored
    }


    public MapFragmentView(AppCompatActivity activity) {
        m_activity = activity;
        initMapFragment();
    }

    private SupportMapFragment getMapFragment() {
        return (SupportMapFragment) m_activity.getSupportFragmentManager().findFragmentById(R.id.mapfragment);
    }

    private void initMapFragment() {
        /* Locate the mapFragment UI element */
        m_mapFragment = getMapFragment();

        // Set path of isolated disk cache
        String diskCacheRoot = Environment.getExternalStorageDirectory().getPath()
                + File.separator + ".isolated-here-maps";
        // Retrieve intent name from manifest
        String intentName = "";
        try {
            ApplicationInfo ai = m_activity.getPackageManager().getApplicationInfo(m_activity.getPackageName(),
                    PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            intentName = bundle.getString("INTENT_NAME");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(this.getClass().toString(), "Failed to find intent name, NameNotFound: " + e.getMessage());
        }

        boolean success = com.here.android.mpa.common.MapSettings.setIsolatedDiskCacheRootPath(diskCacheRoot,
                intentName);
        if (!success) {
            // Setting the isolated disk cache was not successful, please check if the path is valid and
            // ensure that it does not match the default location
            // (getExternalStorageDirectory()/.here-maps).
            // Also, ensure the provided intent name does not match the default intent name.
        } else {
            if (m_mapFragment != null) {
                /* Initialize the SupportMapFragment, results will be given via the called back. */
                m_mapFragment.init(new OnEngineInitListener() {
                    @Override
                    public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {

                        if (error == Error.NONE) {
                            /*
                             * If no error returned from map fragment initialization, the map will be
                             * rendered on screen at this moment.Further actions on map can be provided
                             * by calling Map APIs.
                             */
                            m_map = m_mapFragment.getMap();

                            /*
                             * Map center can be set to a desired location at this point.
                             * It also can be set to the current location ,which needs to be delivered by the PositioningManager.
                             * Please refer to the user guide for how to get the real-time location.
                             */

//                            m_map.setCenter(new GeoCoordinate(49.258576, -123.008268), Map.Animation.NONE);
                            m_map.setZoomLevel(m_map.getMaxZoomLevel() - 1);
//                            m_map.addTransformListener(m_activity);
                            mPositioningManager = PositioningManager.getInstance();
                            m_map.setCenter(mPositioningManager.getPosition().getCoordinate(), Map.Animation.NONE);
                            Log.e(String.valueOf(mPositioningManager.getPosition().getCoordinate().getLongitude()), String.valueOf(mPositioningManager.getPosition().getCoordinate().getLatitude()));
                            mHereLocation = LocationDataSourceHERE.getInstance(
                                    new StatusListener() {
                                        @Override
                                        public void onOfflineModeChanged(boolean offline) {
                                            // called when offline mode changes
                                        }

                                        @Override
                                        public void onAirplaneModeEnabled() {
                                            // called when airplane mode is enabled
                                        }

                                        @Override
                                        public void onWifiScansDisabled() {
                                            // called when Wi-Fi scans are disabled
                                        }

                                        @Override
                                        public void onBluetoothDisabled() {
                                            // called when Bluetooth is disabled
                                        }

                                        @Override
                                        public void onCellDisabled() {
                                            // called when Cell radios are switch off
                                        }

                                        @Override
                                        public void onGnssLocationDisabled() {
                                            // called when GPS positioning is disabled
                                        }

                                        @Override
                                        public void onNetworkLocationDisabled() {
                                            // called when network positioning is disabled
                                        }

                                        @Override
                                        public void onServiceError(ServiceError serviceError) {
                                            // called on HERE service error
                                        }

                                        @Override
                                        public void onPositioningError(PositioningError positioningError) {
                                            // called when positioning fails
                                        }

                                        @Override
                                        public void onWifiIndoorPositioningNotAvailable() {
                                            // called when running on Android 9.0 (Pie) or newer
                                        }
                                    });
                            mPositioningManager.setDataSource(mHereLocation);
                            mPositioningManager.addListener(new WeakReference<PositioningManager.OnPositionChangedListener>(
                                    MapFragmentView.this));
                            // start position updates, accepting GPS, network or indoor positions
                            if (mPositioningManager.start(PositioningManager.LocationMethod.GPS_NETWORK_INDOOR)) {
                                m_mapFragment.getPositionIndicator().setVisible(true);
                            }
                        } else {
                            Toast.makeText(m_activity,
                                    "ERROR: Cannot initialize Map with error " + error,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }
}