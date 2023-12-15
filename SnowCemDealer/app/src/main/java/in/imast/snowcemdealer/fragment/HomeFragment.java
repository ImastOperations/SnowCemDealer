package in.imast.snowcemdealer.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import in.imast.snowcemdealer.Connection.ApiClient;
import in.imast.snowcemdealer.R;
import in.imast.snowcemdealer.activity.MainActivity;
import in.imast.snowcemdealer.helper.DialogClass;
import in.imast.snowcemdealer.helper.StaticSharedpreference;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static in.imast.snowcemdealer.activity.MainActivity.progress;

public class HomeFragment extends Fragment {

    //Declaration of variables
    private View parentView;
    private Context context;
    DialogClass dialogClass;
    Dialog dialog;
    View view;
    WebView webView;
    public static final int SPLASH_DELAY = 2; // in second

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    HashMap<String, String> headers;
    WebViewInitialize webViewInitialize;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        Bundle bundle = new Bundle();
        bundle.putString("userId", StaticSharedpreference.getInfo("UserId", getContext()));
        bundle.putString("mobile", StaticSharedpreference.getInfo("mobile", getContext()));
        mFirebaseAnalytics.logEvent("home_view", bundle);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //  setHasOptionsMenu(true);
        context = getActivity();
        dialogClass = new DialogClass();
        webView = view.findViewById(R.id.webView);
        webView.clearCache(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        final String acToken = StaticSharedpreference.getInfo("AccessToken", getActivity());
        headers = new HashMap<>();
        // String basicAuthHeader = android.util.Base64.encodeToString((username + ":" + password).getBytes(), android.util.Base64.NO_WRAP);
        headers.put("Authorization", acToken);
        // view.loadUrl(url, headers);
        webView.loadUrl(ApiClient.WEB_BASE_URL + "customer-dashboard", headers);
        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    progress.setVisibility(View.GONE);
                } else {
                    progress.setProgress(newProgress);
                    progress.setVisibility(View.VISIBLE);
                }
                Log.v("akram", "progress " + newProgress);
            }
        });
        MainActivity.webView = webView;

    }


    private class CustomWebViewClient extends WebViewClient {
        public CustomWebViewClient() {

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.contains("citybond.in")) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }else if(url.contains("/login")){
                dialogClass.alertDialogAuthentication(getActivity());
            } else
                view.loadUrl(url, headers);
            return true;
        }

        @Override
        public void onPageStarted(WebView webview, String url, Bitmap favicon) {
            Log.v("akram", "url2" + url);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.v("akram", "url3" + url);

            super.onPageFinished(view, url);
        }
    }

    public interface WebViewInitialize {
        public void onWebView(WebView webView);
    }

    public void setWebView(WebViewInitialize webView) {
        webViewInitialize = webView;
    }
}
