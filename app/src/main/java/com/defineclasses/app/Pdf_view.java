package com.defineclasses.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Pdf_view extends Fragment {

    private WebView webView;
    ProgressBar progressBar;
    private String removePdfTopIcon = "javascript:(function() {" + "document.querySelector('[role=\"toolbar\"]').remove();})()";
    TextView topicName;
    PDFView pdfView;
    String pdfUrl;
    Loading_Dialog loading_dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pdf_view, container, false);
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        topicName = view.findViewById(R.id.play_topic_name);
        topicName.setText(getArguments().getString("topic"));
        pdfView = view.findViewById(R.id.pdfView);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        webView = view.findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webView.requestFocus();
        webView.invalidate();
        webView.getSettings().setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setBuiltInZoomControls(true);
        loading_dialog = new Loading_Dialog(getActivity());
        pdfUrl = "https://defineclasses.com//admin/" + getArguments().getString("topic_video");
        Log.e("PDF", pdfUrl);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100) {
                    //loading_dialog.startLoading();
                    progressDialog.setProgress(progress);
                    progressDialog.show();
                }
                if (progress == 100) {
                    //loading_dialog.startLoading();
                    progressDialog.dismiss();
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            boolean checkOnPageStartedCalled = false;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                checkOnPageStartedCalled = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (checkOnPageStartedCalled) {
                    webView.loadUrl(removePdfTopIcon);
                }
            }
        });
        //webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=" + pdfUrl);
        webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + pdfUrl);
        displayPDFfile();
        return view;
    }

    public void displayPDFfile() {
        FileLoader
                .with(getContext())
                .load(pdfUrl)
                .fromDirectory(getArguments().getString("topic_video"), FileLoader.DIR_INTERNAL)
                .asFile(new FileRequestListener<File>() {
                    @Override
                    public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                        File pdfFile = response.getBody();
                        pdfView.fromFile(pdfFile)
                                .enableSwipe(true)
                                .enableAnnotationRendering(true)
                                .swipeHorizontal(false)
                                .scrollHandle(new DefaultScrollHandle(getContext()))
                                .onPageError(new OnPageErrorListener() {
                                    @Override
                                    public void onPageError(int page, Throwable t) {
                                        Toast.makeText(getContext(), "Error in Opening File", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .onPageChange(new OnPageChangeListener() {
                                    @Override
                                    public void onPageChanged(int page, int pageCount) {

                                    }
                                })
                                .onTap(new OnTapListener() {
                                    @Override
                                    public boolean onTap(MotionEvent e) {
                                        return true;
                                    }
                                })
                                .onRender(new OnRenderListener() {
                                    @Override
                                    public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                                        pdfView.fitToWidth();
                                    }
                                })
                                .password(null)
                                .defaultPage(0)
                                .invalidPageColor(Color.RED)
                                .spacing(10)
                                .load();
                    }

                    @Override
                    public void onError(FileLoadRequest request, Throwable t) {
                        Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("PDF Error",t.getMessage());
                    }
                });
    }

}
