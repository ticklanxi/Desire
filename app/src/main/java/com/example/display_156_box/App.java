package com.example.display_156_box;
import android.app.Application;
import android.os.Build;
import androidx.annotation.NonNull;
import com.example.display_156_box.utils.CrashHandler;
import org.litepal.LitePal;


public class App extends Application {
    private static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        String id = Build.SERIAL;//获取设备ID

        LitePal.initialize(this);

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext(), this);
    }

    @NonNull
    public static App getInstance() {
        return mInstance;
    }


}
