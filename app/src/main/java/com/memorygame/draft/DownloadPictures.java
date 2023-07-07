package com.memorygame.draft;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class DownloadPictures extends AppCompatActivity {
    private static final int MAX_PROGRESS = 20;
    private ProgressBar progressBar;
    private TableLayout tableLayout;

    private Handler handler;
    private Thread imageDownloadThread;
    Button downLoad;
    EditText editText;
    String downloadUrl;
    Pattern pattern = Pattern.compile("(?i)\\b((?:https?://|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:'\".,<>?«»“”‘’]))");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_pictures);

        progressBar = findViewById(R.id.progressBar);
        tableLayout = findViewById(R.id.imageButtonContainer);
        handler = new Handler();

        downLoad = findViewById(R.id.dowmloadBtn);
        editText = findViewById(R.id.editText);

        downLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText = editText.getText().toString();
                Matcher matcher = pattern.matcher(inputText);

                if (matcher.find()) {
                    downloadUrl = matcher.group();
                    startImageDownload(downloadUrl);
                } else {
                    Toast.makeText(getApplicationContext(), "Input not valid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void startImageDownload(String url) {
        tableLayout.removeAllViews();

        imageDownloadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Bitmap> downloadedImages = new ArrayList<>();

                try {
                    Document document = Jsoup.connect(url).get();
                    Elements imageElements = document.select("img");

                    int count = 0;
                    for (Element element : imageElements) {
                        if (count >= MAX_PROGRESS) {
                            break;
                        }

                        String imageUrl = element.attr("src");
                        Bitmap imageBitmap = downloadImageBitmap(imageUrl);
                        if (imageBitmap != null) {
                            downloadedImages.add(imageBitmap);
                        }

                        count++;
                        final int progress = count;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                updateProgress(progress);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final List<Bitmap> finalDownloadedImages = downloadedImages;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showDownloadedImages(finalDownloadedImages);
                    }
                });
            }
        });

        imageDownloadThread.start();
    }

    private Bitmap downloadImageBitmap(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateProgress(int progress) {
        progressBar.setProgress(progress);
    }

    private void showDownloadedImages(List<Bitmap> downloadedImages) {
        progressBar.setVisibility(View.GONE);

        int index = 0;
        int maxColumns = 4;
        TableRow currentRow = null;

        for (Bitmap bitmap : downloadedImages) {
            if (index % maxColumns == 0) {
                currentRow = new TableRow(this);
                tableLayout.addView(currentRow);
            }

            ImageButton imageButton = new ImageButton(this);
            imageButton.setImageBitmap(bitmap);

            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    1f
            );
            imageButton.setLayoutParams(params);
            currentRow.addView(imageButton);

            index++;
        }
    }
}
