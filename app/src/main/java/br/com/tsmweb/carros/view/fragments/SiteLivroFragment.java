package br.com.tsmweb.carros.view.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.view.fragments.dialog.AboutDialog;

public class SiteLivroFragment extends BaseFragment {

    private static final String URL_SOBRE = "http://www.livroandroid.com.br/sobre.htm";

    private WebView webView;
    private ProgressBar progress;
    protected SwipeRefreshLayout swipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_site_livro, container, false);
        webView = view.findViewById(R.id.webview);
        progress = view.findViewById(R.id.progress);

        setWebViewClient(webView);

        // Carrega a página
        webView.loadUrl(URL_SOBRE);

        // Swipe to Refresh
        swipeLayout = view.findViewById(R.id.swipeToRefresh);
        swipeLayout.setOnRefreshListener(OnRefreshListener());
        // Cores da animação
        swipeLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);

        configJavaScript();

        return view;
    }

    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener() {
        return () -> {
            // Atualiza a página
            webView.reload();
        };
    }

    private void setWebViewClient(WebView webView) {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                // Liga o progress
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // Desliga o progress
                progress.setVisibility(View.INVISIBLE);
                // Termina a animação do Swipe to Refresh
                swipeLayout.setRefreshing(false);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, "webview url: " + url);

                if (url != null && url.endsWith("sobre.htm")) {
                    AboutDialog.showAbout(getFragmentManager());

                    // Retorna true para informar que interceptamos o evento
                    return true;
                }

                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    private void configJavaScript() {
        WebSettings settings = webView.getSettings();
        // Ativa o JavaScript na página
        settings.setJavaScriptEnabled(true);
        // Publica a interface para o JavaScript
        webView.addJavascriptInterface(new LivroAndroidInterface(), "LivroAndroid");
    }

    class LivroAndroidInterface {

        @JavascriptInterface
        public void sobre() {
            toast("Clicou na figura do livro!");
        }

    }

}
