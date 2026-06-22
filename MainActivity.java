package app.netlify.miswak;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // معالجة الـ Deep Link عند أول فتح للتطبيق
        handleDeepLink(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // معالجة الـ Deep Link لما يرجع المستخدم من متصفح Google
        handleDeepLink(intent);
    }

    private void handleDeepLink(Intent intent) {
        if (intent == null) return;
        Uri data = intent.getData();
        if (data == null) return;

        String url = data.toString();

        // لو الرابط يحتوي على token من Google/Supabase، نمرره للـ WebView
        if (url.contains("access_token") || url.contains("code=") || url.contains("error=")) {
            // نبعث الرابط للـ JavaScript داخل WebView
            bridge.getWebView().post(() -> {
                bridge.getWebView().loadUrl(
                    "javascript:window.dispatchEvent(new CustomEvent('authDeepLink', { detail: '" + url.replace("'", "\\'") + "' }))"
                );
            });
        }
    }
}
