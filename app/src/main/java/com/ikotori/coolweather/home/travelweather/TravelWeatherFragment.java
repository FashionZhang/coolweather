package com.ikotori.coolweather.home.travelweather;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Selection;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.ikotori.coolweather.GlobalApplication;
import com.ikotori.coolweather.R;
import com.ikotori.coolweather.baidumap.DrivingRouteOverlay;
import com.ikotori.coolweather.baidumap.MyDrivingRouteOverlay;
import com.ikotori.coolweather.baidumap.OverlayManager;
import com.ikotori.coolweather.citysearch.CitiesAdapter;
import com.ikotori.coolweather.citysearch.CitySearchFragment;
import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.entity.CityWeather;
import com.ikotori.coolweather.data.entity.WeatherForecast;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TravelWeatherFragment extends Fragment implements TravelWeatherContract.View, View.OnClickListener {


    private TravelWeatherContract.Presenter mPresenter;

    private Button mConfirmView;

    private TextView mWeatherView;

    private Dialog mFullScreenDialog;

    private CitiesAdapter mCitiesAdapter;

    private RecyclerView mCityListView;

    private EditText mOriginRegionView;

    private EditText mDestinationView;

    private QueryItem mOriginCity;

    private QueryItem mDestinationCity;

    private SearchView searchView;

    private List<CityWeather> mCityWeathers;

    private TravelWeatherAdapter mWeatherAdapter;

    private RecyclerView mWeatherListView;

    private MapView mBMapView;

    private BaiduMap mBaiduMap;

    private RoutePlanSearch mRouteSearch;

    private RouteLine mRouteLine;

    private OverlayManager mOverlayManager;

    private Button mChangeMApAndTextView;

    private View mInputView;

    private TextView mChangeToMapView;

    private View mMapRLView;

    private int nodeIndex = -1;

    private static final int ORIGIN = 0;

    private static final int DESTINATION = 1;

    private BaiduMap.OnMarkerClickListener mResultListClickListener;

    private List<InfoWindow> mInfoWindows = new ArrayList<>();

    private List<OverlayOptions> options = new ArrayList<>();

    private List<Marker> mMarkers = new ArrayList<>();

    int width;
    int height;

    public TravelWeatherFragment() {
        // Required empty public constructor
    }

    public static TravelWeatherFragment newInstance() {
        TravelWeatherFragment fragment = new TravelWeatherFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_travel_weather, container, false);

        mConfirmView = root.findViewById(R.id.confirm);
        mConfirmView.setOnClickListener(this);
//        mWeatherView = root.findViewById(R.id.weather_result);
        mOriginRegionView = root.findViewById(R.id.origin_region);
        mDestinationView = root.findViewById(R.id.destination);
        mDestinationView.setFocusable(false);
        mDestinationView.setOnClickListener(this);
        mOriginRegionView.setFocusable(false);
        mOriginRegionView.setOnClickListener(this);

        mWeatherAdapter = new TravelWeatherAdapter();
        mWeatherListView = root.findViewById(R.id.weather_list);
        mWeatherListView.setVisibility(View.GONE);
        mWeatherListView.setLayoutManager(new LinearLayoutManager(getContext()));
        mWeatherListView.setAdapter(mWeatherAdapter);

        mBMapView = root.findViewById(R.id.mapView);
        mBaiduMap = mBMapView.getMap();
        mChangeMApAndTextView = root.findViewById(R.id.change_to_text);
        mChangeMApAndTextView.setOnClickListener(this);
        mInputView = root.findViewById(R.id.input_view);
        mChangeToMapView = root.findViewById(R.id.change_to_map);
        mChangeToMapView.setOnClickListener(this);
        mMapRLView = root.findViewById(R.id.mapRL);
        showBaiduMapAndOverlay();

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        width = metrics.widthPixels;
        height = metrics.heightPixels;
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        mBMapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBMapView.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBMapView.onDestroy();
    }

    private void showFullScreenDialog(@Nullable String query, int type) {
        final int finalType = type;
        if (mFullScreenDialog == null) {
            mFullScreenDialog = new Dialog(getContext(), R.style.DialogFullscreen);
            mFullScreenDialog.setContentView(R.layout.dialog_search_fullscreen);

            ImageView closeView = mFullScreenDialog.findViewById(R.id.dialog_close);
            searchView = mFullScreenDialog.findViewById(R.id.dialog_searchview);
            mCityListView = mFullScreenDialog.findViewById(R.id.city_list);
            mCityListView.setLayoutManager(new LinearLayoutManager(getContext()));
            //添加分割线
            android.support.v7.widget.DividerItemDecoration dividerItemDecoration = new android.support.v7.widget.DividerItemDecoration(getActivity(), android.support.v7.widget.DividerItemDecoration.VERTICAL);
            dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
            mCityListView.addItemDecoration(dividerItemDecoration);

            mCityListView.setAdapter(mCitiesAdapter);
            //不显示搜索(放大镜图标)
            searchView.setIconifiedByDefault(true);
            closeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFullScreenDialog.dismiss();
                }
            });
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (null != query && query.trim().length() > 0) {
                        // 当用户搜索我的位置时直接返回,并获取全局的位置信息
                        if (query.equals(getString(R.string.my_location))) {
                            mOriginCity = GlobalApplication.mLocationCity;
                            mFullScreenDialog.dismiss();
                        }
                        mPresenter.queryCity(query.trim());
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (null != newText && newText.trim().length() > 0) {
                        mPresenter.queryCity(newText);
                    }
                    return true;
                }
            });
        }

       /* int textId = searchView.getContext().getResources().getIdentifier(
                "android:id/search_src_text", null, null
        );*/
        searchView.setIconified(false);
        searchView.setQuery(query, false);

        searchView.setQueryHint(getString(R.string.destination));

        /* 设置搜索框的输入框默认选中所有文字 */
        if (searchView != null) {
            SearchView.SearchAutoComplete textView = searchView.findViewById(R.id.search_src_text);
            textView.selectAll();
        }

        mCitiesAdapter = new CitiesAdapter(new CitySearchFragment.QueryItemListener() {
            @Override
            public void onCityClick(QueryItem city) {
                if (finalType == ORIGIN) {
                    mOriginCity = city;
                    mOriginRegionView.setText(city.getLocation());
                } else {
                    mDestinationCity = city;
                    mDestinationView.setText(city.getLocation());
                }
                mFullScreenDialog.dismiss();
            }
        });
        mCityListView.setAdapter(mCitiesAdapter);
        mFullScreenDialog.show();
    }

    @Override
    public void setPresenter(TravelWeatherContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showMatchCities(List<QueryItem> resultList) {
        mCitiesAdapter.setData(resultList);
//        mCitiesAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNoMatchCity() {
        mCitiesAdapter.setData(new ArrayList<QueryItem>(0));
    }

    @Override
    public void showWeathers(List<CityWeather> weathers) {
        mCityWeathers = weathers;
        mWeatherAdapter.setData(weathers);
        bindResultListListener(weathers);
        mWeatherListView.setVisibility(View.VISIBLE);
        mChangeToMapView.setVisibility(View.VISIBLE);
    }

    private void bindResultListListener(final List<CityWeather> weathers) {
        mResultListClickListener = null;
        mInfoWindows.clear();
        options.clear();
        mMarkers.clear();
        for (int i = 0; i < weathers.size(); i++) {
            CityWeather temp = weathers.get(i);
            LatLng point = new LatLng(Double.valueOf(temp.city.getLat()), Double.valueOf(temp.city.getLon()));
//            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(inflatePreviewView(temp, null));
            OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
            Marker marker = (Marker) mBaiduMap.addOverlay(option);
            mMarkers.add(marker);
            options.add(option);

//            Button button = new Button(getContext());
//            button.setText(String.format("%s%s %s", temp.city.getParentCity(), temp.city.getLocation(), temp.forecasts.get(0).condTxtD));
            InfoWindow infoWindow = new InfoWindow(inflateView(temp, null), marker.getPosition(), -67);
            mInfoWindows.add(infoWindow);

        }
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (int i = 0; i < mMarkers.size(); i++) {
                    if (marker == mMarkers.get(i)) {
                        mBaiduMap.showInfoWindow(mInfoWindows.get(i));
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void showNoWeather() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
//                mPresenter.queryWeathers("117.227692", "31.822651", "115.858836", "28.690469");
                if (mOriginCity != null && mDestinationCity != null && mOriginCity.getCid() != null && mDestinationCity.getCid() != null) {
                    mPresenter.queryWeathers(mOriginCity.getLon(), mOriginCity.getLat(), mDestinationCity.getLon(), mDestinationCity.getLat());
                    mBaiduMap.clear();
                    searchRoute();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.please_input_origin_and_destination), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.destination:
                String queryD = "";
                if (mDestinationCity != null) {
                    queryD = mDestinationCity.getLocation();
                }
                showFullScreenDialog(queryD, DESTINATION);
                mWeatherListView.setVisibility(View.GONE);
                break;
            case R.id.origin_region:
                String query = getString(R.string.my_location);
                if (mOriginCity != null) {
                    query = mOriginCity.getLocation();
                }
                showFullScreenDialog(query, ORIGIN);
                mWeatherListView.setVisibility(View.GONE);
                break;
            case R.id.change_to_text:
                mMapRLView.setVisibility(View.GONE);
                mWeatherListView.setVisibility(View.VISIBLE);
                mInputView.setVisibility(View.VISIBLE);
                mChangeToMapView.setVisibility(View.VISIBLE);
                break;
            case R.id.change_to_map:
                mWeatherListView.setVisibility(View.GONE);
                mInputView.setVisibility(View.GONE);
                mChangeToMapView.setVisibility(View.GONE);
                mMapRLView.setVisibility(View.VISIBLE);
                break;
        }
    }

    private BaiduMap.OnMapClickListener mMapClickListener = new BaiduMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            mBaiduMap.hideInfoWindow();
        }

        @Override
        public boolean onMapPoiClick(MapPoi mapPoi) {
            return false;
        }
    };

    private OnGetRoutePlanResultListener mOnGetRoutePlanResultListener = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
            if (drivingRouteResult == null || drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                KLog.e("查询规划失败");
            }
            if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                //起始点或途经地点有歧义
            }
            if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                nodeIndex = -1;
                if (drivingRouteResult.getRouteLines().size() >= 1) {
                    mRouteLine = drivingRouteResult.getRouteLines().get(0);
                    DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap, getContext());
                    mOverlayManager = overlay;
                    mBaiduMap.setOnMarkerClickListener(overlay);
                    overlay.setData(drivingRouteResult.getRouteLines().get(0));
                    overlay.addToMap();
                    overlay.zoomToSpan();
                } else {
                    KLog.d("路线规划结果为0");
                    return;
                }
            }
        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }
    };

    private void searchRoute() {
        mRouteLine = null;
        mBaiduMap.clear();
        PlanNode stNode = PlanNode.withCityNameAndPlaceName(mOriginCity.getParentCity(), mOriginCity.getLocation());
        PlanNode enNode = PlanNode.withCityNameAndPlaceName(mDestinationCity.getParentCity(), mDestinationCity.getLocation());

        mRouteSearch.drivingSearch((new DrivingRoutePlanOption().from(stNode).to(enNode)));
    }

    private void showBaiduMapAndOverlay() {

//        mBaiduMap.setOnMapClickListener(mMapClickListener);
        mRouteSearch = RoutePlanSearch.newInstance();
        mRouteSearch.setOnGetRoutePlanResultListener(mOnGetRoutePlanResultListener);
       /* LatLng point = new LatLng(39.963175, 116.400244);
        TextView textView = new TextView(getContext());
        textView.setText("aaa");
        BitmapDescriptor tmp = BitmapDescriptorFactory.fromView(textView);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.destination_black_24dp);
        OverlayOptions options = new MarkerOptions().position(point).icon(tmp)
                .title("yahello,晴");
        final Marker markerA = (Marker) mBaiduMap.addOverlay(options);
        final Button button = new Button(getContext());
        button.setText("晴");
        button.setBackgroundColor(getResources().getColor(R.color.white));
        InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick() {
                KLog.d();
                mBaiduMap.hideInfoWindow();
            }
        };
        final InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), point, -67, listener);
//        mBaiduMap.showInfoWindow(mInfoWindow);
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker == markerA) {
                    mBaiduMap.showInfoWindow(mInfoWindow);
                }
                return true;
            }
        });
*/
    }


    /**
     * 生成城市天气信息的详细卡片视图
     *
     * @param cityWeather
     * @param view
     * @return
     */
    private View inflateView(CityWeather cityWeather, View view) {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        if (view == null) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.item_weather_map, null, false);
            view.layout(0, 0, width, height);
        }
        TextView location = view.findViewById(R.id.location);
        location.setText(String.format("%s%s", cityWeather.city.getParentCity(), cityWeather.city.getLocation()));

        TextView condTxt = view.findViewById(R.id.cond_txt);
        condTxt.setText(cityWeather.forecasts.get(0).condTxtD);

        WeatherForecast forecast = cityWeather.forecasts.get(0);
        TextView tmpRange = view.findViewById(R.id.tmp_range);
        tmpRange.setText(String.format("%s~%s°", forecast.tmpMax, forecast.tmpMin));

        forecast = cityWeather.forecasts.get(1);
        TextView tCondTxt = view.findViewById(R.id.tomorrow_cond);
        tCondTxt.setText(forecast.condTxtD);
        TextView tTmpRange = view.findViewById(R.id.tomorrow_tmp_range);
        tTmpRange.setText(String.format("%s~%s°", forecast.tmpMax, forecast.tmpMin));

        forecast = cityWeather.forecasts.get(2);
        TextView ACondTxt = view.findViewById(R.id.after_cond);
        ACondTxt.setText(forecast.condTxtD);
        TextView ATmpRange = view.findViewById(R.id.after_tmp_range);
        ATmpRange.setText(String.format("%s~%s°", forecast.tmpMax, forecast.tmpMin));

        int measureWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measureHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST);
        view.measure(measureWidth, measureHeight);

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        return view;
    }

    /**
     * 生成城市天气信息的预览卡片视图
     *
     * @param cityWeather
     * @param view
     * @return
     */
    private View inflatePreviewView(CityWeather cityWeather, View view) {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        if (view == null) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.item_preview_map, null, false);
            view.layout(0, 0, width, height);
        }
        TextView location = view.findViewById(R.id.location);
        location.setText(String.format("%s", cityWeather.city.getLocation()));

        TextView condTxt = view.findViewById(R.id.cond_txt);
        condTxt.setText(cityWeather.forecasts.get(0).condTxtD);

        int measureWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measureHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST);
        view.measure(measureWidth, measureHeight);

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        return view;
    }

}
