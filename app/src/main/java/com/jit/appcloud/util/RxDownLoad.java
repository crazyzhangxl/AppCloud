package com.jit.appcloud.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import com.jit.appcloud.commom.AppConst;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/5/29.
 *         discription:
 */

public class RxDownLoad {
    public static Observable<Uri> saveImageAndGetPathObservable(Context context, String url,String nameAdd){
        return Observable.create(
                (ObservableOnSubscribe<Bitmap>) emitter -> {
                    Bitmap bitmap = null;
                    bitmap = Picasso.with(context).load(url).get();
                    if (bitmap == null){
                        emitter.onError(new Exception("无法下载图片"));
                    }else {
                        emitter.onNext(bitmap);
                    }
                    emitter.onComplete();
                }
        ).flatMap(
                bitmap -> {
                    File appDir = new File(Environment.getExternalStorageDirectory(), AppConst.PS_SAVE_LEAST_DIR);
                    if (!appDir.exists()){
                        appDir.mkdir();
                    }

                    String fileName = nameAdd+TimeUtil.date2String(new Date(),"yyyy-MM-dd-HH-mm-ss")+".jpg";
                    File file = new File(appDir,fileName);
                    try {
                        FileOutputStream outputStream = new FileOutputStream(file);
                        assert bitmap != null;
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Uri uri = Uri.fromFile(file);
                    LogUtils.e("路径",uri.getPath());
                    return Observable.just(uri);
                }
        ).subscribeOn(Schedulers.io());
    }

}
