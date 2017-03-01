package com.shr.filehelper.mvp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shr.filehelper.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

import static android.content.ContentValues.TAG;

public class DetailActivity extends Activity implements MainModel.CallBack<ResponseBody>, View.OnClickListener {
    /**
     * PIC
     */
    public static final int PIC_TYPE = 0;
    /**
     * DOC
     */
    public static final int DOC_TYPE = 1;
    /**
     * DOCX
     */
    public static final int DOCX_TYPE = 2;
    /**
     * PPT
     */
    public static final int PPT_TYPE = 3;
    /**
     * EXCEL
     */
    public static final int EXCEL_TYPE = 4;
    /**
     * PDF
     */
    public static final int PDF_TYPE = 5;
    /**
     * TXT
     */
    public static final int TXT_TYPE = 6;


    private SimpleDraweeView mPicture;
    private String name;
    private String currentFilePath = "";
    private ProgressBar mProgressBar;
    private int currentType;
    private Button mOpenBtn;
    private ImageView picIv;
    private TextView nameTv;
    private LinearLayout fileLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String url = getIntent().getStringExtra("url");
        currentType = getIntent().getIntExtra("type", -1);
        name = getIntent().getStringExtra("name");
        MainModel mainModel = new MainModel();

        picIv = (ImageView) findViewById(R.id.logo);
        nameTv = (TextView) findViewById(R.id.name);
        mPicture = (SimpleDraweeView) findViewById(R.id.picture);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mOpenBtn = (Button) findViewById(R.id.open);
        fileLayout = (LinearLayout) findViewById(R.id.file_layout);

        mOpenBtn.setOnClickListener(this);

        if (!TextUtils.isEmpty(url)) {
            if (TextUtils.isEmpty(name)) {
                name = url.substring(url.lastIndexOf("/") + 1, url.length());
            }
            switch (currentType) {
                case PIC_TYPE:
                    mProgressBar.setVisibility(View.GONE);
                    fileLayout.setVisibility(View.GONE);
                    parseImg(url);
                    break;
                case DOC_TYPE:
                    fileLayout.setVisibility(View.VISIBLE);
                    nameTv.setText(name);
                    picIv.setImageResource(R.mipmap.word);
                    mainModel.executeDownLoadTask(url, DetailActivity.this);
                    break;
                case DOCX_TYPE:
                    fileLayout.setVisibility(View.VISIBLE);
                    nameTv.setText(name);
                    picIv.setImageResource(R.mipmap.word);
                    mainModel.executeDownLoadTask(url, DetailActivity.this);
                    break;
                case PPT_TYPE:
                    fileLayout.setVisibility(View.VISIBLE);
                    nameTv.setText(name);
                    picIv.setImageResource(R.mipmap.power_point);
                    mainModel.executeDownLoadTask(url, DetailActivity.this);
                    break;
                case EXCEL_TYPE:
                    fileLayout.setVisibility(View.VISIBLE);
                    nameTv.setText(name);
                    picIv.setImageResource(R.mipmap.excel);
                    mainModel.executeDownLoadTask(url, DetailActivity.this);
                    break;
                case PDF_TYPE:
                    fileLayout.setVisibility(View.VISIBLE);
                    nameTv.setText(name);
                    picIv.setImageResource(R.mipmap.excel);
                    mainModel.executeDownLoadTask(url, DetailActivity.this);
                    break;
            }

        }
    }

    /**
     * 解析图片
     *
     * @param url 地址
     */
    private void parseImg(String url) {
        mPicture.setImageURI(url);
    }

    /**
     * 解析doc
     *
     * @param path 地址
     */
    private void parseDocFile(String path) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(path));
        intent.setDataAndType(uri, "application/msword");
        startActivity(intent);
    }

    /**
     * 解析ppt
     *
     * @param path 路径
     */
    private void parsePowerPointFile(String path) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(path));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        startActivity(intent);
    }

    /**
     * 解析excel
     *
     * @param path 路径
     */
    private void parseExcelFile(String path) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(path));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        startActivity(intent);
    }

    /**
     * 解析PDF
     *
     * @param path 路径
     */
    private void parsePDFFile(String path) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(path));
        intent.setDataAndType(uri, "application/pdf");
        startActivity(intent);
    }

    /**
     * 解析PDF
     *
     * @param path 路径
     */
    private void parseTxtFile(String path) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(path));
        intent.setDataAndType(uri, "application/pdf");
        startActivity(intent);
    }


    /**
     * 写文件到硬盘
     *
     * @param body 写入数据
     * @return 0 已下载 1 成功 -1 失败
     */
    private int writeResponseBodyToDisk(ResponseBody body, String name) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            File sd;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                sd = Environment.getExternalStorageDirectory();
            } else {
                sd = getFilesDir();
            }

            String filePath = sd.getPath() + File.separator + "FileHelper";
            File file = new File(filePath);
            if (!file.exists()) {
                //若不存在，创建目录，可以在应用启动的时候创建
                file.mkdirs();
            }
            String fileNamePath = filePath + File.separator + name;
            File downFile = new File(fileNamePath);
            currentFilePath = fileNamePath;
            if (!downFile.exists()) {
                downFile.createNewFile();
            } else {
                int progress = 0;
                while (progress < 100) {
                    progress += 1;
                    Thread.sleep(10);
                    publishProgress(progress);
                }
                return 0;
            }
            byte[] fileReader = new byte[1024];
            long fileSize = body.contentLength();
            long fileSizeDownloaded = 0;

            inputStream = body.byteStream();
            outputStream = new FileOutputStream(downFile);

            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }
                outputStream.write(fileReader, 0, read);
                fileSizeDownloaded += read;
                Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                int currentProgress = ((int) (fileSizeDownloaded * 100 / fileSize));
                Log.d(TAG, "file download: " + currentProgress);
                publishProgress(currentProgress);
            }

            outputStream.flush();

            return 1;
        } catch (IOException e) {
            return -1;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void publishProgress(int currentProgress) {
        mProgressBar.setProgress(currentProgress);
    }


    @Override
    public void onSuccess(ResponseBody t) {

        new AsyncTask<ResponseBody, Long, Integer>() {

            @Override
            protected Integer doInBackground(ResponseBody... params) {
                return writeResponseBodyToDisk(params[0], name);
            }

            @Override
            protected void onPostExecute(Integer s) {
                super.onPostExecute(s);
                switch (s) {
                    case 0:
                        mOpenBtn.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                        break;
                    case 1:
                        Toast.makeText(DetailActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
                        mProgressBar.setProgress(100);
                        mOpenBtn.setVisibility(View.VISIBLE);
                        break;
                    case -1:
                        Toast.makeText(DetailActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                        mOpenBtn.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        }.execute(t);

    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.open) {
            try {
                switch (currentType) {
                    case DOC_TYPE:
                        parseDocFile(currentFilePath);
                        break;
                    case DOCX_TYPE:
                        parseDocFile(currentFilePath);
                        break;
                    case PPT_TYPE:
                        parsePowerPointFile(currentFilePath);
                        break;
                    case EXCEL_TYPE:
                        parseExcelFile(currentFilePath);
                        break;
                    case PDF_TYPE:
                        parsePDFFile(currentFilePath);
                        break;
                    case TXT_TYPE:
                        parseTxtFile(currentFilePath);
                        break;
                }
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "没有发现可打开的Activity", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
