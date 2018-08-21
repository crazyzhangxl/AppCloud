package com.jit.appcloud.ui.presenter;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.ui.view.NextView;


/**
 * Created by 张先磊 on 2018/4/17.
 */

public class NextPresenter extends BasePresenter<NextView> {


    public NextPresenter(BaseActivity context) {
        super(context);
    }

    public void register(){
/*        ApiRetrofit.getInstance().getRegisterCode().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        Log.e("测试", "onNext: "+o );
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });*/

    }

    public void login(){

    }

    public void postHeadImage(){



    }


}
