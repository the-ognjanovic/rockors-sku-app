package com.rockors.sku;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileWriter;

public class RockorsWebInterface {

    private final Context ctx;

    public RockorsWebInterface(Context ctx) { this.ctx = ctx; }

    @JavascriptInterface
    public void shareFile(String content, String filename, String mimeType) {
        try {
            File dir = ctx.getExternalFilesDir(null);
            if (dir == null) dir = ctx.getCacheDir();
            File file = new File(dir, filename);
            FileWriter w = new FileWriter(file);
            w.write(content); w.flush(); w.close();

            Uri uri = FileProvider.getUriForFile(ctx, ctx.getPackageName() + ".fileprovider", file);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType(mimeType);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Rockors SKU Export");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);

            Intent chooser = Intent.createChooser(intent, "Share via");
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(chooser);
        } catch (Exception e) {
            android.util.Log.e("Rockors", "shareFile failed: " + e.getMessage());
        }
    }

    @JavascriptInterface
    public String getVersion() { return "1.0"; }
}
