/* 
 * NMapViewer.java $version 2010. 1. 1
 * 
 * Copyright 2010 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */

package ajou.amicom.ajouInfo.map;

import org.xmlpull.v1.*;

import ajou.amicom.ajouInfo.*;
import android.app.*;
import android.content.*;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.maps.overlay.NMapPathData;
import com.nhn.android.maps.overlay.NMapPathLineStyle;
import com.nhn.android.mapviewer.overlay.NMapCalloutCustomOverlay;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapPathDataOverlay;

/**
 * Sample class for map viewer library.
 * 
 * @author kyjkim
 */
public class NMapViewer extends NMapActivity {
	private static final String LOG_TAG = intro.LOG_TAG;
	private static final boolean DEBUG = intro.DEBUG;

	// set your API key which is registered for NMapViewer library.
	private static final String API_KEY = "a6ef7d8413ba0cb443bf6919a47f2d64";

	private MapContainerView mMapContainerView;

	private NMapView mMapView;
	private NMapController mMapController;

	private static final NGeoPoint NMAP_LOCATION_DEFAULT = new NGeoPoint(127.0463203, 37.282354);
	private static final int NMAP_ZOOMLEVEL_DEFAULT = 11;
	private static final int NMAP_VIEW_MODE_DEFAULT = NMapView.VIEW_MODE_VECTOR;
	private static final boolean NMAP_TRAFFIC_MODE_DEFAULT = false;
	private static final boolean NMAP_BICYCLE_MODE_DEFAULT = false;

	private static final String KEY_ZOOM_LEVEL = "NMapViewer.zoomLevel";
	private static final String KEY_CENTER_LONGITUDE = "NMapViewer.centerLongitudeE6";
	private static final String KEY_CENTER_LATITUDE = "NMapViewer.centerLatitudeE6";
	private static final String KEY_VIEW_MODE = "NMapViewer.viewMode";
	private static final String KEY_TRAFFIC_MODE = "NMapViewer.trafficMode";
	private static final String KEY_BICYCLE_MODE = "NMapViewer.bicycleMode";

	private SharedPreferences mPreferences;

	private NMapOverlayManager mOverlayManager;

	private NMapMyLocationOverlay mMyLocationOverlay;
	private NMapLocationManager mMapLocationManager;
	private NMapCompassManager mMapCompassManager;

	private NMapViewerResourceProvider mMapViewerResourceProvider;

	private NMapPOIdataOverlay mFloatingPOIdataOverlay;
	private NMapPOIitem mFloatingPOIitem;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.map_naver);
		
		// create map view
		//mMapView = new NMapView(this);
		mMapView = (NMapView) findViewById(R.id.mapView);

		// set a registered API key for Open MapViewer Library
		mMapView.setApiKey(API_KEY);

		// create parent view to rotate map view
		//mMapContainerView = new MapContainerView(this);
		//mMapContainerView.addView(mMapView);

		// set the activity content to the parent view
        //setContentView(mMapContainerView);
		

		// initialize map view
		mMapView.setClickable(true);
		mMapView.setEnabled(true);
		mMapView.setFocusable(true);
		mMapView.setFocusableInTouchMode(true);
		mMapView.requestFocus();

		// register listener for map state changes
		mMapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
		mMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);
		mMapView.setOnMapViewDelegate(onMapViewTouchDelegate);

		// use map controller to zoom in/out, pan and set map center, zoom level etc.
		mMapController = mMapView.getMapController();

		// use built in zoom controls
		NMapView.LayoutParams lp = new NMapView.LayoutParams(NMapView.LayoutParams.WRAP_CONTENT,
			NMapView.LayoutParams.WRAP_CONTENT, NMapView.LayoutParams.BOTTOM_RIGHT);
		mMapView.setBuiltInZoomControls(true, lp);

		// create resource provider
		mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

		// set data provider listener
		super.setMapDataProviderListener(onDataProviderListener);

		// create overlay manager
		mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
		// register callout overlay listener to customize it.
		mOverlayManager.setOnCalloutOverlayListener(onCalloutOverlayListener);

		// location manager
		mMapLocationManager = new NMapLocationManager(this);
		mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);

		// compass manager
		mMapCompassManager = new NMapCompassManager(this);

		// create my location overlay
		mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);
		
		// message
		AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(
				this);
		alert_internet_status.setTitle("지도이용안내");
		alert_internet_status
				.setMessage("지도모드  및 GPS이용, 건물찾기는 메뉴 버튼을 누르시면 가능합니다.\nGPS의 경우 두번 누르시면 지도가 방위에 맞추어 회전됩니다.");
		alert_internet_status.setPositiveButton("닫기",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss(); // 닫기
						//finish();
					}
				});
		alert_internet_status.show();
		if (intro.DEBUG) {
			Context Context = this;
			Log.e(intro.LOG_TAG, "map: getPackageName = " + Context.getPackageName());
			Log.e(intro.LOG_TAG, "map: API_KEY = " + API_KEY);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		saveInstanceState();
		super.onResume();
	}

	@Override
	protected void onStop() {

		stopMyLocation();
		saveInstanceState();
		super.onStop();
	}

	@Override
	protected void onDestroy() {

		// save map view state such as map center position and zoom level.
		mMapController.setMapCenter(new NGeoPoint(127.0463203, 37.282354), 11);
		saveInstanceState();

		super.onDestroy();
	}

	/* Test Functions */

	private void startMyLocation() {

		if (mMyLocationOverlay != null) {
			if (!mOverlayManager.hasOverlay(mMyLocationOverlay)) {
				mOverlayManager.addOverlay(mMyLocationOverlay);
			}

			if (mMapLocationManager.isMyLocationEnabled()) {

				if (!mMapView.isAutoRotateEnabled()) {
					mMyLocationOverlay.setCompassHeadingVisible(true);

					mMapCompassManager.enableCompass();

					mMapView.setAutoRotateEnabled(true, false);

					mMapContainerView.requestLayout();
				} else {
					stopMyLocation();
				}

				mMapView.postInvalidate();
			} else {
				boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(false);
				if (!isMyLocationEnabled) {
					Toast.makeText(NMapViewer.this, "Please enable a My Location source in system settings",
						Toast.LENGTH_LONG).show();

					Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(goToSettings);

					return;
				}
			}
		}
	}

	private void stopMyLocation() {
		if (mMyLocationOverlay != null) {
			mMapLocationManager.disableMyLocation();

			if (mMapView.isAutoRotateEnabled()) {
				mMyLocationOverlay.setCompassHeadingVisible(false);

				mMapCompassManager.disableCompass();

				mMapView.setAutoRotateEnabled(false, false);

				mMapContainerView.requestLayout();
			}
		}
	}

	private void testPathDataOverlay() {

		// set path data points
		NMapPathData pathData = new NMapPathData(9);

		pathData.initPathData();
		pathData.addPathPoint(127.108099, 37.366034, NMapPathLineStyle.TYPE_SOLID);
		pathData.addPathPoint(127.108088, 37.366043, 0);
		pathData.addPathPoint(127.108079, 37.365619, 0);
		pathData.addPathPoint(127.107458, 37.365608, 0);
		pathData.addPathPoint(127.107232, 37.365608, 0);
		pathData.addPathPoint(127.106904, 37.365624, 0);
		pathData.addPathPoint(127.105933, 37.365621, NMapPathLineStyle.TYPE_DASH);
		pathData.addPathPoint(127.105929, 37.366378, 0);
		pathData.addPathPoint(127.106279, 37.366380, 0);
		pathData.endPathData();

		NMapPathDataOverlay pathDataOverlay = mOverlayManager.createPathDataOverlay(pathData);
		if (pathDataOverlay != null) {
			pathDataOverlay.showAllPathData(0);
		}
	}

	private void testPathPOIdataOverlay() {

		// set POI data
		NMapPOIdata poiData = new NMapPOIdata(4, mMapViewerResourceProvider, true);
		poiData.beginPOIdata(4);
		poiData.addPOIitem(349652983, 149297368, "Pizza 124-456", NMapPOIflagType.FROM, null);
		poiData.addPOIitem(349652966, 149296906, null, NMapPOIflagType.NUMBER_BASE + 1, null);
		poiData.addPOIitem(349651062, 149296913, null, NMapPOIflagType.NUMBER_BASE + 999, null);
		poiData.addPOIitem(349651376, 149297750, "Pizza 000-999", NMapPOIflagType.TO, null);
		poiData.endPOIdata();

		// create POI data overlay
		NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);

		// set event listener to the overlay
		poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

	}

	private void testPOIdataOverlay() {

		// Markers for POI item
		int markerId = NMapPOIflagType.PIN;

		// set POI data
		NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
		poiData.beginPOIdata(1);
		poiData.addPOIitem(127.0630205, 37.5091300, "Pizza 777-111", markerId, 0);
		poiData.endPOIdata();

		// create POI data overlay
		NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);

		// set event listener to the overlay
		poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

		// select an item
		poiDataOverlay.selectPOIitem(0, true);

		// show all POI data
		//poiDataOverlay.showAllPOIdata(0);
	}
	
	private void ajouPOIdataOverlay(double lng, double lat, String str) {

		// Markers for POI item
		int markerId = NMapPOIflagType.PIN;

		// set POI data
		NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
		poiData.beginPOIdata(1);
		poiData.addPOIitem(lng, lat, str, markerId, 0);
		poiData.endPOIdata();

		// create POI data overlay
		NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);

		// set event listener to the overlay
		poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

		// select an item
		poiDataOverlay.selectPOIitem(0, true);

		// show all POI data
		//poiDataOverlay.showAllPOIdata(0);
	}

	private void testFloatingPOIdataOverlay() {
		// Markers for POI item
		int marker1 = NMapPOIflagType.PIN;

		// set POI data
		NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
		poiData.beginPOIdata(1);
		NMapPOIitem item = poiData.addPOIitem(null, "Touch & Drag to Move", marker1, 0);
		if (item != null) {
			// initialize location to the center of the map view.
			item.setPoint(mMapController.getMapCenter());
			// set floating mode
			item.setFloatingMode(NMapPOIitem.FLOATING_TOUCH | NMapPOIitem.FLOATING_DRAG);
			// show right button on callout
			item.setRightButton(true);

			mFloatingPOIitem = item;
		}
		poiData.endPOIdata();

		// create POI data overlay
		NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
		if (poiDataOverlay != null) {
			poiDataOverlay.setOnFloatingItemChangeListener(onPOIdataFloatingItemChangeListener);

			// set event listener to the overlay
			poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

			poiDataOverlay.selectPOIitem(0, false);

			mFloatingPOIdataOverlay = poiDataOverlay;
		}
	}

	/* NMapDataProvider Listener */
	private final NMapActivity.OnDataProviderListener onDataProviderListener = new NMapActivity.OnDataProviderListener() {

		public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errInfo) {

			if (intro.DEBUG) {
				Log.i(intro.LOG_TAG, "onReverseGeocoderResponse: placeMark="
					+ ((placeMark != null) ? placeMark.toString() : null));
			}

			if (errInfo != null) {
				Log.e(intro.LOG_TAG, "Failed to findPlacemarkAtLocation: error=" + errInfo.toString());

				Toast.makeText(NMapViewer.this, errInfo.toString(), Toast.LENGTH_LONG).show();
				return;
			}

			if (mFloatingPOIitem != null && mFloatingPOIdataOverlay != null) {
				mFloatingPOIdataOverlay.deselectFocusedPOIitem();

				if (placeMark != null) {
					mFloatingPOIitem.setTitle(placeMark.toString());
				}
				mFloatingPOIdataOverlay.selectPOIitemBy(mFloatingPOIitem.getId(), false);
			}
		}

	};

	/* MyLocation Listener */
	private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {

		public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint myLocation) {

			if (mMapController != null) {
				mMapController.animateTo(myLocation);
			}

			return true;
		}

		public void onLocationUpdateTimeout(NMapLocationManager locationManager) {

			// stop location updating
			//			Runnable runnable = new Runnable() {
			//				public void run() {										
			//					stopMyLocation();
			//				}
			//			};
			//			runnable.run();	

			Toast.makeText(NMapViewer.this, "Your current location is temporarily unavailable", Toast.LENGTH_LONG).show();
		}

	};

	/* MapView State Change Listener*/
	private final NMapView.OnMapStateChangeListener onMapViewStateChangeListener = new NMapView.OnMapStateChangeListener() {

		public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {

			if (errorInfo == null) { // success
				// restore map view state such as map center position and zoom level.
				restoreInstanceState();

			} else { // fail
				Log.e(intro.LOG_TAG, "onFailedToInitializeWithError: " + errorInfo.toString());

				Toast.makeText(NMapViewer.this, errorInfo.toString(), Toast.LENGTH_LONG).show();
			}
		}

		public void onAnimationStateChange(NMapView mapView, int animType, int animState) {
			if (intro.DEBUG) {
				Log.i(intro.LOG_TAG, "onAnimationStateChange: animType=" + animType + ", animState=" + animState);
			}
		}

		public void onMapCenterChange(NMapView mapView, NGeoPoint center) {
			if (intro.DEBUG) {
				Log.i(intro.LOG_TAG, "onMapCenterChange: center=" + center.toString());
			}
		}

		public void onZoomLevelChange(NMapView mapView, int level) {
			if (intro.DEBUG) {
				Log.i(intro.LOG_TAG, "onZoomLevelChange: level=" + level);
			}
		}

		public void onMapCenterChangeFine(NMapView mapView) {

		}
	};

	private final NMapView.OnMapViewTouchEventListener onMapViewTouchEventListener = new NMapView.OnMapViewTouchEventListener() {

		public void onLongPress(NMapView mapView, MotionEvent ev) {
			// TODO Auto-generated method stub

		}

		public void onLongPressCanceled(NMapView mapView) {
			// TODO Auto-generated method stub

		}

		public void onSingleTapUp(NMapView mapView, MotionEvent ev) {
			// TODO Auto-generated method stub

		}

		public void onTouchDown(NMapView mapView, MotionEvent ev) {

		}

		public void onScroll(NMapView mapView, MotionEvent e1, MotionEvent e2) {
		}

	};

	private final NMapView.OnMapViewDelegate onMapViewTouchDelegate = new NMapView.OnMapViewDelegate() {

		public boolean isLocationTracking() {
			if (mMapLocationManager != null) {
				if (mMapLocationManager.isMyLocationEnabled()) {
					return mMapLocationManager.isMyLocationFixed();
				}
			}
			return false;
		}

	};

	/* POI data State Change Listener*/
	private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {

		public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
			if (intro.DEBUG) {
				Log.i(intro.LOG_TAG, "onCalloutClick: title=" + item.getTitle());
			}

			// [[TEMP]] handle a click event of the callout
			//Toast.makeText(NMapViewer.this, "onCalloutClick: " + item.getTitle(), Toast.LENGTH_LONG).show();
		}

		public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
			if (intro.DEBUG) {
				if (item != null) {
					Log.i(intro.LOG_TAG, "onFocusChanged: " + item.toString());
				} else {
					Log.i(intro.LOG_TAG, "onFocusChanged: ");
				}
			}
		}
	};

	private final NMapPOIdataOverlay.OnFloatingItemChangeListener onPOIdataFloatingItemChangeListener = new NMapPOIdataOverlay.OnFloatingItemChangeListener() {

		public void onPointChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
			NGeoPoint point = item.getPoint();

			if (intro.DEBUG) {
				Log.i(intro.LOG_TAG, "onPointChanged: point=" + point.toString());
			}

			findPlacemarkAtLocation(point.longitude, point.latitude);

			item.setTitle(null);

		}
	};

	private final NMapOverlayManager.OnCalloutOverlayListener onCalloutOverlayListener = new NMapOverlayManager.OnCalloutOverlayListener() {

		public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay itemOverlay, NMapOverlayItem overlayItem,
			Rect itemBounds) {

			// handle overlapped items
			if (itemOverlay instanceof NMapPOIdataOverlay) {
				NMapPOIdataOverlay poiDataOverlay = (NMapPOIdataOverlay)itemOverlay;

				// check if it is selected by touch event
				if (!poiDataOverlay.isFocusedBySelectItem()) {
					int countOfOverlappedItems = 1;

					NMapPOIdata poiData = poiDataOverlay.getPOIdata();
					for (int i = 0; i < poiData.count(); i++) {
						NMapPOIitem poiItem = poiData.getPOIitem(i);

						// skip selected item
						if (poiItem == overlayItem) {
							continue;
						}

						// check if overlapped or not
						if (Rect.intersects(poiItem.getBoundsInScreen(), overlayItem.getBoundsInScreen())) {
							countOfOverlappedItems++;
						}
					}

					if (countOfOverlappedItems > 1) {
						String text = countOfOverlappedItems + " overlapped items for " + overlayItem.getTitle();
						Toast.makeText(NMapViewer.this, text, Toast.LENGTH_LONG).show();
						return null;
					}
				}
			}

			// use custom callout overlay
			return new NMapCalloutCustomOverlay(itemOverlay, overlayItem, itemBounds, mMapViewerResourceProvider);

			// set basic callout overlay
			//return new NMapCalloutBasicOverlay(itemOverlay, overlayItem, itemBounds);			
		}

	};

	/* Local Functions */

	private void restoreInstanceState() {
		mPreferences = getPreferences(MODE_PRIVATE);

		int longitudeE6 = mPreferences.getInt(KEY_CENTER_LONGITUDE, NMAP_LOCATION_DEFAULT.getLongitudeE6());
		int latitudeE6 = mPreferences.getInt(KEY_CENTER_LATITUDE, NMAP_LOCATION_DEFAULT.getLatitudeE6());
		int level = mPreferences.getInt(KEY_ZOOM_LEVEL, NMAP_ZOOMLEVEL_DEFAULT);
		int viewMode = mPreferences.getInt(KEY_VIEW_MODE, NMAP_VIEW_MODE_DEFAULT);
		boolean trafficMode = mPreferences.getBoolean(KEY_TRAFFIC_MODE, NMAP_TRAFFIC_MODE_DEFAULT);
		boolean bicycleMode = mPreferences.getBoolean(KEY_BICYCLE_MODE, NMAP_BICYCLE_MODE_DEFAULT);

		mMapController.setMapViewMode(viewMode);
		mMapController.setMapViewTrafficMode(trafficMode);
		mMapController.setMapViewBicycleMode(bicycleMode);
		mMapController.setMapCenter(new NGeoPoint(longitudeE6, latitudeE6), level);
	}

	private void saveInstanceState() {
		if (mPreferences == null) {
			return;
		}

		NGeoPoint center = mMapController.getMapCenter();
		int level = mMapController.getZoomLevel();
		int viewMode = mMapController.getMapViewMode();
		boolean trafficMode = mMapController.getMapViewTrafficMode();
		boolean bicycleMode = mMapController.getMapViewBicycleMode();

		SharedPreferences.Editor edit = mPreferences.edit();

		edit.putInt(KEY_CENTER_LONGITUDE, center.getLongitudeE6());
		edit.putInt(KEY_CENTER_LATITUDE, center.getLatitudeE6());
		edit.putInt(KEY_ZOOM_LEVEL, level);
		edit.putInt(KEY_VIEW_MODE, viewMode);
		edit.putBoolean(KEY_TRAFFIC_MODE, trafficMode);
		edit.putBoolean(KEY_BICYCLE_MODE, bicycleMode);

		edit.commit();

	}

	/* Menus */
	private static final int MENU_ITEM_CLEAR_MAP = 10;
	private static final int MENU_ITEM_MAP_MODE = 20;
	private static final int MENU_ITEM_MAP_MODE_SUB_VECTOR = MENU_ITEM_MAP_MODE + 1;
	private static final int MENU_ITEM_MAP_MODE_SUB_SATELLITE = MENU_ITEM_MAP_MODE + 2;
	private static final int MENU_ITEM_MAP_MODE_SUB_HYBRID = MENU_ITEM_MAP_MODE + 3;
	private static final int MENU_ITEM_MAP_MODE_SUB_TRAFFIC = MENU_ITEM_MAP_MODE + 4;
	private static final int MENU_ITEM_MAP_MODE_SUB_BICYCLE = MENU_ITEM_MAP_MODE + 5;
	private static final int MENU_ITEM_ZOOM_CONTROLS = 30;
	private static final int MENU_ITEM_MY_LOCATION = 40;

	private static final int MENU_ITEM_TEST_MODE = 50;
	private static final int MENU_ITEM_TEST_POI_DATA = MENU_ITEM_TEST_MODE + 1;
	private static final int MENU_ITEM_TEST_PATH_DATA = MENU_ITEM_TEST_MODE + 2;
	private static final int MENU_ITEM_TEST_FLOATING_DATA = MENU_ITEM_TEST_MODE + 3;
	private static final int MENU_ITEM_TEST_AUTO_ROTATE = MENU_ITEM_TEST_MODE + 4;
	
	private static final int MENU_ITEM_BUILDING = 100;
	private static final int MENU_ITEM_BUILDING_01 = MENU_ITEM_BUILDING + 1;
	private static final int MENU_ITEM_BUILDING_02 = MENU_ITEM_BUILDING + 2;
	private static final int MENU_ITEM_BUILDING_03 = MENU_ITEM_BUILDING + 3;
	private static final int MENU_ITEM_BUILDING_04 = MENU_ITEM_BUILDING + 4;
	private static final int MENU_ITEM_BUILDING_05 = MENU_ITEM_BUILDING + 5;
	private static final int MENU_ITEM_BUILDING_06 = MENU_ITEM_BUILDING + 6;
	private static final int MENU_ITEM_BUILDING_07 = MENU_ITEM_BUILDING + 7;
	private static final int MENU_ITEM_BUILDING_08 = MENU_ITEM_BUILDING + 8;
	private static final int MENU_ITEM_BUILDING_09 = MENU_ITEM_BUILDING + 9;
	private static final int MENU_ITEM_BUILDING_10 = MENU_ITEM_BUILDING + 10;
	private static final int MENU_ITEM_BUILDING_11 = MENU_ITEM_BUILDING + 11;
	private static final int MENU_ITEM_BUILDING_12 = MENU_ITEM_BUILDING + 12;
	private static final int MENU_ITEM_BUILDING_13 = MENU_ITEM_BUILDING + 13;
	private static final int MENU_ITEM_BUILDING_14 = MENU_ITEM_BUILDING + 14;
	private static final int MENU_ITEM_BUILDING_15 = MENU_ITEM_BUILDING + 15;
	private static final int MENU_ITEM_BUILDING_16 = MENU_ITEM_BUILDING + 16;
	private static final int MENU_ITEM_BUILDING_17 = MENU_ITEM_BUILDING + 17;
	private static final int MENU_ITEM_BUILDING_18 = MENU_ITEM_BUILDING + 18;
	private static final int MENU_ITEM_BUILDING_19 = MENU_ITEM_BUILDING + 19;
	private static final int MENU_ITEM_BUILDING_20 = MENU_ITEM_BUILDING + 20;
	private static final int MENU_ITEM_BUILDING_21 = MENU_ITEM_BUILDING + 21;
	private static final int MENU_ITEM_BUILDING_22 = MENU_ITEM_BUILDING + 22;
	private static final int MENU_ITEM_BUILDING_23 = MENU_ITEM_BUILDING + 23;
	private static final int MENU_ITEM_BUILDING_24 = MENU_ITEM_BUILDING + 24;
	private static final int MENU_ITEM_BUILDING_25 = MENU_ITEM_BUILDING + 25;
	private static final int MENU_ITEM_BUILDING_26 = MENU_ITEM_BUILDING + 26;
	private static final int MENU_ITEM_BUILDING_27 = MENU_ITEM_BUILDING + 27;
	private static final int MENU_ITEM_BUILDING_28 = MENU_ITEM_BUILDING + 28;
	private static final int MENU_ITEM_BUILDING_29 = MENU_ITEM_BUILDING + 29;
	private static final int MENU_ITEM_BUILDING_30 = MENU_ITEM_BUILDING + 30;
	private static final int MENU_ITEM_BUILDING_31 = MENU_ITEM_BUILDING + 31;
	private static final int MENU_ITEM_BUILDING_32 = MENU_ITEM_BUILDING + 32;
	private static final int MENU_ITEM_BUILDING_33 = MENU_ITEM_BUILDING + 33;
	private static final int MENU_ITEM_BUILDING_34 = MENU_ITEM_BUILDING + 34;
	private static final int MENU_ITEM_BUILDING_35 = MENU_ITEM_BUILDING + 35;
	private static final int MENU_ITEM_BUILDING_36 = MENU_ITEM_BUILDING + 36;
	private static final int MENU_ITEM_BUILDING_37 = MENU_ITEM_BUILDING + 37;
	private static final int MENU_ITEM_BUILDING_38 = MENU_ITEM_BUILDING + 38;
	private static final int MENU_ITEM_BUILDING_39 = MENU_ITEM_BUILDING + 39;
	private static final int MENU_ITEM_BUILDING_40 = MENU_ITEM_BUILDING + 40;
	private static final int MENU_ITEM_BUILDING_41 = MENU_ITEM_BUILDING + 41;
	private static final int MENU_ITEM_BUILDING_42 = MENU_ITEM_BUILDING + 42;
	private static final int MENU_ITEM_BUILDING_43 = MENU_ITEM_BUILDING + 43;
	private static final int MENU_ITEM_BUILDING_44 = MENU_ITEM_BUILDING + 44;
	private static final int MENU_ITEM_BUILDING_45 = MENU_ITEM_BUILDING + 45;
	private static final int MENU_ITEM_BUILDING_46 = MENU_ITEM_BUILDING + 46;
	private static final int MENU_ITEM_BUILDING_47 = MENU_ITEM_BUILDING + 47;
	private static final int MENU_ITEM_BUILDING_48 = MENU_ITEM_BUILDING + 48;
	private static final int MENU_ITEM_BUILDING_49 = MENU_ITEM_BUILDING + 49;
	private static final int MENU_ITEM_BUILDING_50 = MENU_ITEM_BUILDING + 50;

	/**
	 * Invoked during init to give the Activity a chance to set up its Menu.
	 * 
	 * @param menu the Menu to which entries may be added
	 * @return true
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuItem menuItem = null;
		SubMenu subMenu = null;

		//menuItem = menu.add(Menu.NONE, MENU_ITEM_CLEAR_MAP, Menu.CATEGORY_SECONDARY, "Clear Map");
		//menuItem.setAlphabeticShortcut('c');
		//menuItem.setIcon(android.R.drawable.ic_menu_revert);

		subMenu = menu.addSubMenu(Menu.NONE, MENU_ITEM_MAP_MODE, Menu.CATEGORY_SECONDARY, "지도모드");
		subMenu.setIcon(android.R.drawable.ic_menu_mapmode);

		menuItem = subMenu.add(0, MENU_ITEM_MAP_MODE_SUB_VECTOR, Menu.NONE, "일반지도");
		menuItem.setAlphabeticShortcut('m');
		menuItem.setCheckable(true);
		menuItem.setChecked(false);

		//menuItem = subMenu.add(0, MENU_ITEM_MAP_MODE_SUB_SATELLITE, Menu.NONE, "Satellite");
		//menuItem.setAlphabeticShortcut('s');
		//menuItem.setCheckable(true);
		//menuItem.setChecked(false);

		menuItem = subMenu.add(0, MENU_ITEM_MAP_MODE_SUB_HYBRID, Menu.NONE, "위성(Hybrid)");
		menuItem.setAlphabeticShortcut('h');
		menuItem.setCheckable(true);
		menuItem.setChecked(false);

		//menuItem = subMenu.add(0, MENU_ITEM_MAP_MODE_SUB_TRAFFIC, Menu.NONE, "Traffic");
		//menuItem.setAlphabeticShortcut('t');
		//menuItem.setCheckable(true);
		//menuItem.setChecked(false);

		//menuItem = subMenu.add(0, MENU_ITEM_MAP_MODE_SUB_BICYCLE, Menu.NONE, "Bicycle");
		//menuItem.setAlphabeticShortcut('b');
		//menuItem.setCheckable(true);
		//menuItem.setChecked(false);

		//menuItem = menu.add(0, MENU_ITEM_ZOOM_CONTROLS, Menu.CATEGORY_SECONDARY, "Zoom Controls");
		//menuItem.setAlphabeticShortcut('z');
		//menuItem.setIcon(android.R.drawable.ic_menu_zoom);

		menuItem = menu.add(0, MENU_ITEM_MY_LOCATION, Menu.CATEGORY_SECONDARY, "My Location");
		menuItem.setAlphabeticShortcut('l');
		menuItem.setIcon(android.R.drawable.ic_menu_mylocation);

		//subMenu = menu.addSubMenu(Menu.NONE, MENU_ITEM_TEST_MODE, Menu.CATEGORY_SECONDARY, "Test mode");
		//subMenu.setIcon(android.R.drawable.ic_menu_more);

		//menuItem = subMenu.add(0, MENU_ITEM_TEST_POI_DATA, Menu.NONE, "Test POI data");
		//menuItem.setAlphabeticShortcut('p');

		//menuItem = subMenu.add(0, MENU_ITEM_TEST_PATH_DATA, Menu.NONE, "Test Path data");
		//menuItem.setAlphabeticShortcut('t');

		//menuItem = subMenu.add(0, MENU_ITEM_TEST_FLOATING_DATA, Menu.NONE, "Test Floating data");
		//menuItem.setAlphabeticShortcut('f');

		//menuItem = subMenu.add(0, MENU_ITEM_TEST_AUTO_ROTATE, Menu.NONE, "Test Auto Rotate");
		//menuItem.setAlphabeticShortcut('a');
		
		subMenu = menu.addSubMenu(Menu.NONE, MENU_ITEM_BUILDING, Menu.CATEGORY_SECONDARY, "건물 찾기");
		subMenu.setIcon(android.R.drawable.ic_menu_more);

		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_01, Menu.NONE, "아주대 정문");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_02, Menu.NONE, "율곡관/아주대박물관");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_03, Menu.NONE, "팔달관 ");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_04, Menu.NONE, "종합관");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_05, Menu.NONE, "다산관");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_06, Menu.NONE, "성호관(법학관)");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_07, Menu.NONE, "원천관");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_08, Menu.NONE, "서관");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_09, Menu.NONE, "동관");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_10, Menu.NONE, "에너지센터(전자계산소)");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_11, Menu.NONE, "산학협력원");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_12, Menu.NONE, "중앙도서관");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_13, Menu.NONE, "신학생회관");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_14, Menu.NONE, "구학생회관");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_15, Menu.NONE, "동아리/소학회실(깡통/아향 옆)");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_16, Menu.NONE, "동아리/소학회실(깡통/아향 위)");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_17, Menu.NONE, "성호관 앞 잔디밭");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_18, Menu.NONE, "신학광장");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_19, Menu.NONE, "선구자상");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_20, Menu.NONE, "더테라스");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_21, Menu.NONE, "노천극장");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_22, Menu.NONE, "대운동장");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_23, Menu.NONE, "체육관");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_24, Menu.NONE, "테니스장");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_25, Menu.NONE, "종합설계동");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_26, Menu.NONE, "대형지반연구실험동");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_27, Menu.NONE, "토목실험동");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_28, Menu.NONE, "가스화학실험동");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_29, Menu.NONE, "조립식교량실험동");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_30, Menu.NONE, "화공실험동");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_31, Menu.NONE, "기숙사식당/교직원식당");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_32, Menu.NONE, "아향(학생식당)");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_33, Menu.NONE, "남제관(기숙사)");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_34, Menu.NONE, "용지관(기숙사)");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_35, Menu.NONE, "화홍관(기숙사)");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_36, Menu.NONE, "광교관(기숙사)");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_37, Menu.NONE, "캠퍼스플라자");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_38, Menu.NONE, "학군단");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_39, Menu.NONE, "중앙열공급원(Plant)");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_40, Menu.NONE, "아주대 중문");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_41, Menu.NONE, "송재관");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_42, Menu.NONE, "송재관 옆 테니스/농구장");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_43, Menu.NONE, "아주대병원 정문");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_44, Menu.NONE, "아주대학교 병원");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_45, Menu.NONE, "웰빙센터");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_46, Menu.NONE, "병원별관");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_47, Menu.NONE, "병원 장례식장");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_48, Menu.NONE, "의과대학연구관");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_49, Menu.NONE, "임상수기센터/실험동물센터");
		menuItem = subMenu.add(0, MENU_ITEM_BUILDING_50, Menu.NONE, "약학대학 실험동");
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu pMenu) {
		super.onPrepareOptionsMenu(pMenu);

		int viewMode = mMapController.getMapViewMode();
		boolean isTraffic = mMapController.getMapViewTrafficMode();
		boolean isBicycle = mMapController.getMapViewBicycleMode();

		//pMenu.findItem(MENU_ITEM_CLEAR_MAP).setEnabled(
		//	(viewMode != NMapView.VIEW_MODE_VECTOR) || isTraffic || mOverlayManager.sizeofOverlays() > 0);
		pMenu.findItem(MENU_ITEM_MAP_MODE_SUB_VECTOR).setChecked(viewMode == NMapView.VIEW_MODE_VECTOR);
		//pMenu.findItem(MENU_ITEM_MAP_MODE_SUB_SATELLITE).setChecked(viewMode == NMapView.VIEW_MODE_SATELLITE);
		pMenu.findItem(MENU_ITEM_MAP_MODE_SUB_HYBRID).setChecked(viewMode == NMapView.VIEW_MODE_HYBRID);
		//pMenu.findItem(MENU_ITEM_MAP_MODE_SUB_TRAFFIC).setChecked(isTraffic);
		//pMenu.findItem(MENU_ITEM_MAP_MODE_SUB_BICYCLE).setChecked(isBicycle);

		if (mMyLocationOverlay == null) {
			pMenu.findItem(MENU_ITEM_MY_LOCATION).setEnabled(false);
		}

		return true;
	}

	/**
	 * Invoked when the user selects an item from the Menu.
	 * 
	 * @param item the Menu entry which was selected
	 * @return true if the Menu item was legit (and we consumed it), false
	 *         otherwise
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			/*case MENU_ITEM_CLEAR_MAP:
				if (mMyLocationOverlay != null) {
					stopMyLocation();
					mOverlayManager.removeOverlay(mMyLocationOverlay);
				}

				mMapController.setMapViewMode(NMapView.VIEW_MODE_VECTOR);
				mMapController.setMapViewTrafficMode(false);
				mMapController.setMapViewBicycleMode(false);

				mOverlayManager.clearOverlays();

				return true;
*/
			case MENU_ITEM_MAP_MODE_SUB_VECTOR:
				mMapController.setMapViewMode(NMapView.VIEW_MODE_VECTOR);
				return true;
/*
			case MENU_ITEM_MAP_MODE_SUB_SATELLITE:
				mMapController.setMapViewMode(NMapView.VIEW_MODE_SATELLITE);
				return true;
*/
			case MENU_ITEM_MAP_MODE_SUB_HYBRID:
				mMapController.setMapViewMode(NMapView.VIEW_MODE_HYBRID);
				return true;
/*
			case MENU_ITEM_MAP_MODE_SUB_TRAFFIC:
				mMapController.setMapViewTrafficMode(!mMapController.getMapViewTrafficMode());
				return true;

			case MENU_ITEM_MAP_MODE_SUB_BICYCLE:
				mMapController.setMapViewBicycleMode(!mMapController.getMapViewBicycleMode());
				return true;

			case MENU_ITEM_ZOOM_CONTROLS:
				mMapView.displayZoomControls(true);
				return true;
*/
			case MENU_ITEM_MY_LOCATION:
				startMyLocation();
				return true;
			
			case MENU_ITEM_BUILDING:
				return true;
				
			case MENU_ITEM_BUILDING_01:
			case MENU_ITEM_BUILDING_02:
			case MENU_ITEM_BUILDING_03:
			case MENU_ITEM_BUILDING_04:
			case MENU_ITEM_BUILDING_05:
			case MENU_ITEM_BUILDING_06:
			case MENU_ITEM_BUILDING_07:
			case MENU_ITEM_BUILDING_08:
			case MENU_ITEM_BUILDING_09:
			case MENU_ITEM_BUILDING_10:
			case MENU_ITEM_BUILDING_11:
			case MENU_ITEM_BUILDING_12:
			case MENU_ITEM_BUILDING_13:
			case MENU_ITEM_BUILDING_14:
			case MENU_ITEM_BUILDING_15:
			case MENU_ITEM_BUILDING_16:
			case MENU_ITEM_BUILDING_17:
			case MENU_ITEM_BUILDING_18:
			case MENU_ITEM_BUILDING_19:
			case MENU_ITEM_BUILDING_20:
			case MENU_ITEM_BUILDING_21:
			case MENU_ITEM_BUILDING_22:
			case MENU_ITEM_BUILDING_23:
			case MENU_ITEM_BUILDING_24:
			case MENU_ITEM_BUILDING_25:
			case MENU_ITEM_BUILDING_26:
			case MENU_ITEM_BUILDING_27:
			case MENU_ITEM_BUILDING_28:
			case MENU_ITEM_BUILDING_29:
			case MENU_ITEM_BUILDING_30:
			case MENU_ITEM_BUILDING_31:
			case MENU_ITEM_BUILDING_32:
			case MENU_ITEM_BUILDING_33:
			case MENU_ITEM_BUILDING_34:
			case MENU_ITEM_BUILDING_35:
			case MENU_ITEM_BUILDING_36:
			case MENU_ITEM_BUILDING_37:
			case MENU_ITEM_BUILDING_38:
			case MENU_ITEM_BUILDING_39:
			case MENU_ITEM_BUILDING_40:
			case MENU_ITEM_BUILDING_41:
			case MENU_ITEM_BUILDING_42:
			case MENU_ITEM_BUILDING_43:
			case MENU_ITEM_BUILDING_44:
			case MENU_ITEM_BUILDING_45:
			case MENU_ITEM_BUILDING_46:
			case MENU_ITEM_BUILDING_47:
			case MENU_ITEM_BUILDING_48:
			case MENU_ITEM_BUILDING_49:
			case MENU_ITEM_BUILDING_50:

				
				mOverlayManager.clearOverlays();

				double lng[] = new double[51];
				double lat[] = new double[51];
				String str[] = new String[51];

				lng[0] =127.0463203; 
				lat[0] =37.282354;
				str[0] ="아주대학교";
				
				lng[1] =127.0436499; 
				lat[1] =37.2799641;
				str[1] ="아주대 정문";
				
				lng[2] =127.0463841; 
				lat[2] =37.2820523;
				str[2] ="울곡관/아주대박물관";
				
				lng[3] =127.044362; 
				lat[3] =37.2843972;
				str[3] ="팔달관";
				
				lng[4] =127.0475503; 
				lat[4] =37.2822731;
				str[4] ="종합관";
				
				lng[5] =127.0475067; 
				lat[5] =37.282976;
				str[5] ="다산관";
				
				lng[6] =127.0450818; 
				lat[6] =37.2828811;
				str[6] ="성호관(법학관)";
				
				lng[7] =127.0434567; 
				lat[7] =37.282992;
				str[7] ="원천관";
				
				lng[8] =127.0426571; 
				lat[8] =37.2837101;
				str[8] ="서관";
				
				lng[9] =127.0437733; 
				lat[9] =37.283809;
				str[9] ="동관";
				
				lng[10] =127.0426201; 
				lat[10] =37.2823623;
				str[10] ="에너지센터(전자계산소)";
				
				lng[11] =127.0457822; 
				lat[11] =37.2864759;
				str[11] ="산학협력원";
			
				lng[12] =127.0441696; 
				lat[12] =37.2817013;
				str[12] ="중앙도서관";
				
				lng[13] =127.0459592; 
				lat[13] =37.2833081;
				str[13] ="신학생회관";
				
				lng[14] =127.0452464; 
				lat[14] =37.2836524;
				str[14] ="구학생회관";
				
				lng[15] =127.0432409; 
				lat[15] =37.2832391;
				str[15] ="동아리/소학회실(깡통/아향 옆)";
				
				lng[16] =127.0437189; 
				lat[16] =37.2834798;
				str[16] ="동아리/소학회실(깡통/아향 위)";
				
				lng[17] =127.0445653; 
				lat[17] =37.282496;
				str[17] ="성호관 앞 잔디밭";
				
				lng[18] =127.0454175; 
				lat[18] =37.2833376;
				str[18] ="신학광장";

				lng[19] =127.0434827; 
				lat[19] =37.2824243;
				str[19] ="선구자상";
				
				lng[20] =127.0442429; 
				lat[20] =37.2826345;
				str[20] ="더테라스";

				lng[21] =127.0454831; 
				lat[21] =37.2818055;
				str[21] ="노천극장";

				lng[22] =127.0443971; 
				lat[22] =37.2804537;
				str[22] ="대운동장";

				lng[23] =127.0454093; 
				lat[23] =37.2800205;
				str[23] ="체육관";

				lng[24] =127.0492601; 
				lat[24] =37.282158;
				str[24] ="테니스장";

				lng[25] =127.0423111; 
				lat[25] =37.2840332;
				str[25] ="종합설계동";

				lng[26] =127.0427455; 
				lat[26] =37.2840214;
				str[26] ="대형지반연구실험동";

				lng[27] =127.0432968; 
				lat[27] =37.2842534;
				str[27] ="토목실험동";

				lng[28] =127.0480342; 
				lat[28] =37.2815268;
				str[28] ="가스화학실험동";

				lng[29] =127.0472308; 
				lat[29] =37.2857107;
				str[29] ="조립식교량실험동";

				lng[30] =127.0427215; 
				lat[30] =37.2833227;
				str[30] ="화공실험동";
				
				lng[31] =127.0456523; 
				lat[31] =37.284623;
				str[31] ="기숙사식당/교직원식당";
				
				lng[32] =127.0436753; 
				lat[32] =37.2832272;
				str[32] ="아향(학생식당)";
				
				lng[33] =127.0455764; 
				lat[33] =37.2841134;
				str[33] ="남제관(기숙사)";
				
				lng[34] =127.0460965; 
				lat[34] =37.2848681;
				str[34] ="용지관(기숙사)";
				
				lng[35] =127.0463826; 
				lat[35] =37.2851306;
				str[35] ="화홍관(기숙사)";
				
				lng[36] =127.046646; 
				lat[36] =37.2854065;
				str[36] ="광교관(기숙사)";
				
				lng[37] =127.044117; 
				lat[37] =37.2791997;
				str[37] ="캠퍼스플라자";
				
				lng[38] =127.0449436; 
				lat[38] =37.2852377;
				str[38] ="학군단";
				
				lng[39] =127.0485806; 
				lat[39] =37.2806996;
				str[39] ="중앙열공급원(Plant)";
				
				lng[40] =127.0456808; 
				lat[40] =37.2789488;
				str[40] ="아주대 중문";
				
				lng[41] =127.0473388; 
				lat[41] =37.2808616;
				str[41] ="송재관";
			
				lng[42] =127.0480266; 
				lat[42] =37.2809138;
				str[42] ="송재관 옆 테니스/농구장";
				
				lng[43] =127.0475418; 
				lat[43] =37.2780411;
				str[43] ="아주대병원 정문";
				
				lng[44] =127.0476077; 
				lat[44] =37.2792807;
				str[44] ="아주대학교 병원";
				
				lng[45] =127.04612; 
				lat[45] =37.2790767;
				str[45] ="웰빙센터";
				
				lng[46] =127.0464102; 
				lat[46] =37.2795781;
				str[46] ="병원별관";
				
				lng[47] =127.0485817; 
				lat[47] =37.2795774;
				str[47] ="병원 장례식장";
				
				lng[48] =127.0481799; 
				lat[48] =37.2816851;
				str[48] ="의과대학연구관 ";
				
				lng[49] =127.0483532; 
				lat[49] =37.2819562;
				str[49] ="임상수기센터 및 실험동물센터";
				
				lng[50] =127.0484072; 
				lat[50] =37.282353;
				str[50] ="약학대학 실험동";
				
				ajouPOIdataOverlay(lng[item.getItemId()-MENU_ITEM_BUILDING], lat[item.getItemId()-MENU_ITEM_BUILDING], str[item.getItemId()-MENU_ITEM_BUILDING]);
				return true;
/*
			case MENU_ITEM_TEST_POI_DATA:
				mOverlayManager.clearOverlays();

				// add POI data overlay
				testPOIdataOverlay();
				//ajouPOIdataOverlay();
				return true;

			case MENU_ITEM_TEST_PATH_DATA:
				mOverlayManager.clearOverlays();

				// add path data overlay
				testPathDataOverlay();

				// add path POI data overlay
				testPathPOIdataOverlay();
				return true;

			case MENU_ITEM_TEST_FLOATING_DATA:
				mOverlayManager.clearOverlays();
				testFloatingPOIdataOverlay();
				return true;

			case MENU_ITEM_TEST_AUTO_ROTATE:
				if (mMapView.isAutoRotateEnabled()) {
					mMapView.setAutoRotateEnabled(false, false);

					mMapContainerView.requestLayout();

					mHnadler.removeCallbacks(mTestAutoRotation);
				} else {

					mMapView.setAutoRotateEnabled(true, false);

					mMapView.setRotateAngle(30);
					mHnadler.postDelayed(mTestAutoRotation, AUTO_ROTATE_INTERVAL);

					mMapContainerView.requestLayout();
				}
				return true;
*/
		}

		return super.onOptionsItemSelected(item);
	}

	private static final long AUTO_ROTATE_INTERVAL = 2000;
	private final Handler mHnadler = new Handler();
	private final Runnable mTestAutoRotation = new Runnable() {
		public void run() {
//        	if (mMapView.isAutoRotateEnabled()) {
//    			float degree = (float)Math.random()*360;
//    			
//    			degree = mMapView.getRoateAngle() + 30;
//
//    			mMapView.setRotateAngle(degree);	
//            	
//            	mHnadler.postDelayed(mTestAutoRotation, AUTO_ROTATE_INTERVAL);        		
//        	}
		}
	};

	/** 
	 * Container view class to rotate map view.
	 */
	private class MapContainerView extends ViewGroup {

		public MapContainerView(Context context) {
			super(context);
		}

		@Override
		protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
			final int width = getWidth();
			final int height = getHeight();
			final int count = getChildCount();
			for (int i = 0; i < count; i++) {
				final View view = getChildAt(i);
				final int childWidth = view.getMeasuredWidth();
				final int childHeight = view.getMeasuredHeight();
				final int childLeft = (width - childWidth) / 2;
				final int childTop = (height - childHeight) / 2;
				view.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
			}

			if (changed) {
				mOverlayManager.onSizeChanged(width, height);
			}
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			int w = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
			int h = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
			int sizeSpecWidth = widthMeasureSpec;
			int sizeSpecHeight = heightMeasureSpec;

			final int count = getChildCount();
			for (int i = 0; i < count; i++) {
				final View view = getChildAt(i);

				if (view instanceof NMapView) {
					if (mMapView.isAutoRotateEnabled()) {
						int diag = (((int)(Math.sqrt(w * w + h * h)) + 1) / 2 * 2);
						sizeSpecWidth = MeasureSpec.makeMeasureSpec(diag, MeasureSpec.EXACTLY);
						sizeSpecHeight = sizeSpecWidth;
					}
				}

				view.measure(sizeSpecWidth, sizeSpecHeight);
			}
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}
}
