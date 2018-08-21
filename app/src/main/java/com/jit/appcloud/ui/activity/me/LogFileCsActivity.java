package com.jit.appcloud.ui.activity.me;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jit.appcloud.R;
import com.jit.appcloud.api.ApiRetrofit;
import com.jit.appcloud.commom.AppConst;
import com.jit.appcloud.model.bean.LogFileBean;
import com.jit.appcloud.model.response.DiaryResponse;
import com.jit.appcloud.ui.base.BaseActivity;
import com.jit.appcloud.ui.base.BasePresenter;
import com.jit.appcloud.util.ExcelUtils;
import com.jit.appcloud.util.TimeUtil;
import com.jit.appcloud.util.UIUtils;
import com.jit.appcloud.widget.DropDownView;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zxl on 2018/07/04.
 *         discription: 文件名选择的活动
 */
public class LogFileCsActivity extends BaseActivity {
    @BindView(R.id.ivToolbarNavigation)
    ImageView mIvToolbarNavigation;
    @BindView(R.id.vToolbarDivision)
    View mVToolbarDivision;
    @BindView(R.id.tvToolbarTitle)
    TextView mTvToolbarTitle;
    @BindView(R.id.llToolbarTitle)
    LinearLayout mLlToolbarTitle;
    @BindView(R.id.llDownload)
    LinearLayout mLlDownload;
    @BindView(R.id.ivDnLog)
    ImageView mIvDnLog;
    @BindView(R.id.llSave)
    LinearLayout mLlSave;
    @BindView(R.id.flToolBar)
    FrameLayout mFlToolBar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.drop_down_view)
    DropDownView mDropDownView;
    private ArrayList<ArrayList<String>> recordList;
    private TextView mTvFileName;
    private ImageView mArrowImage;
    private LinearLayout mLlFileType;
    private int selectPosition = 0;
    private int prePosition = -1;
    private View expandedView;
    private View collapsedView;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter<LogFileBean,BaseViewHolder> mAdapter ;
    private List<LogFileBean> mLogFileBeanList = new ArrayList<>();
    private TextView mTvFileType;
    private static String[] title = { "日期","天气","1","2","3","4","5","6",
            "合计","PH(min)","PH(max)","PH波动","溶解氧(min)","溶解氧(max)","溶解氧波动","水温(min)","水温(max)","水温波动",
            "氨氮","亚硝酸盐","总碱度","用药情况","备注"};
    private File file;
    private String mFileName;
    private String mFileType;
    private String mFileSavePath;
    private List<DiaryResponse.DataBean> mMList;

    @Override
    protected void init() {
        if (getIntent() != null){
            mFileName = getIntent().getExtras().getString(AppConst.EXTRA_DOWNLOAD_NAME);
            mMList = (List<DiaryResponse.DataBean >)getIntent().getExtras().getSerializable(AppConst.EXTRA_DOWNLOAD_LIST);
        }
        mLogFileBeanList.add(new LogFileBean(AppConst.FileSaveType.XLS,AppConst.FileSaveType.DES_FOR_XLS));
        mLogFileBeanList.add(new LogFileBean(AppConst.FileSaveType.CSV,AppConst.FileSaveType.DES_FOR_CVS));
        mLogFileBeanList.add(new LogFileBean(AppConst.FileSaveType.TXT,AppConst.FileSaveType.DES_FOR_TXT));

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_log_file_cs;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTvToolbarTitle.setText(R.string.title_insert_file_name);
        mLlSave.setVisibility(View.VISIBLE);
        setupDropView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mArrowImage.setRotation(mDropDownView.isExpanded()
                ? 180f : 0f);
    }

    private void setupDropView() {
        collapsedView = LayoutInflater.from(this).inflate(R.layout.drop_log_file_head, null, false);
        expandedView = LayoutInflater.from(this).inflate(R.layout.drop_body_rv, null, false);
        mTvFileName = collapsedView.findViewById(R.id.tvFileName);
        mArrowImage = collapsedView.findViewById(R.id.arrowImage);
        mLlFileType = collapsedView.findViewById(R.id.llFileType);
        mTvFileType = collapsedView.findViewById(R.id.tvFileType);
        mRecyclerView = expandedView.findViewById(R.id.recyclerView);
        setAdapter();
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        frameLayout.setBackgroundColor(UIUtils.getColor(R.color.white));
        mDropDownView.setHeaderView(collapsedView);
        mDropDownView.setExpandedView(expandedView,frameLayout);
        mDropDownView.setDropDownListener(dropDownListener);
        if (!TextUtils.isEmpty(mFileName)){
            mTvFileName.setText(mFileName);
        }
        mTvFileName.setOnClickListener(v -> {
            if (mDropDownView.isExpanded()){
                mDropDownView.collapseDropDown();
            }
            Bundle bundle = new Bundle();
            bundle.putString(AppConst.CONTENT_FORM_FILE_LOG, mTvFileName.getText().toString().trim());
            jumpToActivityForResult(EditInfoActivity.class, bundle, AppConst.RECODE_EDIT_FROM_LOG_FILE);
        });

    }

    private void setAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mAdapter = new BaseQuickAdapter<LogFileBean, BaseViewHolder>(R.layout.list_item_stand_drop_down,mLogFileBeanList) {
            @Override
            protected void convert(BaseViewHolder helper, LogFileBean item) {
                helper.itemView.setBackground (UIUtils.getDrawable(R.drawable.selector_item_select_state));
                helper.itemView.setSelected(helper.getAdapterPosition() == selectPosition);
                helper.setText(R.id.tvTitle,item.getFileType());
                helper.setText(R.id.tvStatus,item.getDes());
            }
        };

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (position != selectPosition) {
                prePosition = selectPosition;
                selectPosition = position;
                mAdapter.notifyItemChanged(prePosition);
                mAdapter.notifyItemChanged(selectPosition);
                mTvFileType.setText(mLogFileBeanList.get(selectPosition).getFileType());
            }
            mDropDownView.collapseDropDown();
        });

        mRecyclerView.setAdapter(mAdapter);
    }

    private final DropDownView.DropDownListener dropDownListener = new DropDownView.DropDownListener() {
        @Override
        public void onExpandDropDown() {
            // 刷新 ----
            ObjectAnimator.ofFloat(mArrowImage, View.ROTATION.getName(), 180).start();
        }

        @Override
        public void onCollapseDropDown() {
            ObjectAnimator.ofFloat(mArrowImage, View.ROTATION.getName(), -180, 0).start();
        }
    };

    @Override
    protected void initData() {

    }



    @Override
    protected void initListener() {
        mIvToolbarNavigation.setOnClickListener(v -> onBackPressed());

        mLlSave.setOnClickListener(v -> {
            if (mDropDownView.isExpanded()) {
                mDropDownView.collapseDropDown();
            }
            LogFileCsActivity.this.saveFile();
        });
    }

    private void saveFile() {
        mFileName = mTvFileName.getText().toString().trim();
        mFileType = mTvFileType.getText().toString().trim();
        /* 遍历文件夹检查是否存在该文件 ============= */
        // 判断SDK是否存在-----
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (!sdCardExist) {
            UIUtils.showToast("保存失败,不存在SD卡!");
            return;
        }
        sdDir = Environment.getExternalStorageDirectory();
        // 保存在 appcloud路径下了
        file = new File(sdDir.toString()
                + File.separator + AppConst.PS_SAVE_DIR + File.separator + AppConst.RECORD);
        makeDir(file);

        if (hasThisFile(mFileName+mFileType)){
            // 提示用户进行覆盖
            showMaterialDialog("保存文件", "同名文件已存在,确认覆盖?"
                    , getString(R.string.ensure), getString(R.string.cancel)
                    , (dialog, which) -> doExport(), (dialog, which) -> dialog.cancel());
        }else {
           doExport();
        }
    }

    /**
     *  判断手机SD卡是否存在该文件
     * */
    private boolean hasThisFile(String fileName) {
        boolean has = false;
        File[] listFiles = file.listFiles();
        for (int i=0;i<listFiles.length;i++){
            File file =  listFiles[i];
            if (fileName.equals(file.getName())){
                has = true;
                break;
            }
        }
        return has;
    }

    private void doExport(){
        showWaitingDialog(getString(R.string.str_please_waiting));
        mFileSavePath  = file.toString() + File.separator+mFileName+mFileType;
        Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            if (AppConst.FileSaveType.XLS.equals(mFileType)) {
                exportExcel(emitter);
            } else if (AppConst.FileSaveType.CSV.equals(mFileType)) {
                exportCvs(emitter);
            } else if (AppConst.FileSaveType.TXT.equals(mFileType)) {
                exportTxt(emitter);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(aBoolean -> {
                    if (aBoolean){
                        return ApiRetrofit.getInstance().addDownloadLog(mFileName+mFileType);
                    }
                    return null;
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getCode() == 1){
                        hideWaitingDialog();
                        UIUtils.showToast(String.format("文件已存储在/SD卡/%s/%s/%s%s"
                                , AppConst.PS_SAVE_DIR, AppConst.RECORD, mFileName, mFileType));
                        jumpToActivity(LogDdListActivity.class);
                        finish();
                    }
                }, throwable -> {
                    hideWaitingDialog();
                    UIUtils.showToast(throwable.getLocalizedMessage());

                });
    }

    /**
     *  执行文件操作 ======================
     * */
    private void exportExcel(ObservableEmitter<Boolean> emitter) {
        ExcelUtils.initExcel(mFileSavePath,title,mFileName,emitter);
        ExcelUtils.writeObjListToExcel(getRecordData(), mFileSavePath, this,emitter);
    }

    private  ArrayList<ArrayList<String>> getRecordData() {
        recordList = new ArrayList<>();
        for (int i = 0; i <mMList.size(); i++) {
            DiaryResponse.DataBean bean = mMList.get(i);
            ArrayList<String> beanList = new ArrayList<String>();
            beanList.add(TimeUtil.getMyTimeDay(bean.getDate()));
            beanList.add(bean.getWeather());
            beanList.add(String.valueOf(bean.getCount1()));
            beanList.add(String.valueOf(bean.getCount2()));
            beanList.add(String.valueOf(bean.getCount3()));
            beanList.add(String.valueOf(bean.getCount4()));
            beanList.add(String.valueOf(bean.getCount5()));
            beanList.add(String.valueOf(bean.getCount6()));
            beanList.add(String.valueOf(bean.getCount_total()));
            beanList.add(String.valueOf(bean.getPh_min()));
            beanList.add(String.valueOf(bean.getPh_max()));
            beanList.add(String.valueOf(bean.getPh_range()));
            beanList.add(String.valueOf(bean.getO2_min()));
            beanList.add(String.valueOf(bean.getO2_max()));
            beanList.add(String.valueOf(bean.getO2_range()));
            beanList.add(String.valueOf(bean.getTemperature_min()));
            beanList.add(String.valueOf(bean.getTemperature_max()));
            beanList.add(String.valueOf(bean.getTemperature_range()));
            beanList.add(bean.getNh());
            beanList.add(bean.getNano2());
            beanList.add(bean.getAlkali());
            beanList.add(bean.getMedicine());
            beanList.add(bean.getRemark());
            recordList.add(beanList);
        }
        return recordList;
    }

    private void exportTxt(ObservableEmitter<Boolean> emitter){
        StringBuilder sb = new StringBuilder();
        for (String aTitle : title) {
            sb.append(aTitle).append("\t");
        }
        sb.append("\n");
        for (DiaryResponse.DataBean  bean:mMList){
            sb.append(TimeUtil.getMyTimeDay(bean.getDate())).append("\t")
                    .append(bean.getWeather()).append("\t")
                    .append(String.valueOf(bean.getCount1())).append("\t")
                    .append(String.valueOf(bean.getCount2())).append("\t")
                    .append(String.valueOf(bean.getCount3())).append("\t")
                    .append(String.valueOf(bean.getCount4())).append("\t")
                    .append(String.valueOf(bean.getCount5())).append("\t")
                    .append(String.valueOf(bean.getCount6())).append("\t")
                    .append(String.valueOf(bean.getCount_total())).append("\t")
                    .append(String.valueOf(bean.getPh_min())).append("\t")
                    .append(String.valueOf(bean.getPh_max())).append("\t")
                    .append(String.valueOf(bean.getPh_range())).append("\t")
                    .append(String.valueOf(bean.getO2_min())).append("\t")
                    .append(String.valueOf(bean.getO2_max())).append("\t")
                    .append(String.valueOf(bean.getO2_range())).append("\t")
                    .append(String.valueOf(bean.getTemperature_min())).append("\t")
                    .append(String.valueOf(bean.getTemperature_max())).append("\t")
                    .append(String.valueOf(bean.getTemperature_range())).append("\t")
                    .append(bean.getNh()).append("\t")
                    .append(bean.getNano2()).append("\t")
                    .append(bean.getAlkali()).append("\t")
                    .append(bean.getMedicine()).append("\t")
                    .append(bean.getRemark()).append("\t")
                    .append("\n");
        }
        File saveCSV = new File(mFileSavePath);
        try {
            if(!saveCSV.exists()) {
                boolean createOk = saveCSV.createNewFile();
                if (!createOk){
                    UIUtils.showToast("创建文件失败!");
                    return;
                }
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(saveCSV));
            writer.write(sb.toString());
            writer.close();
            emitter.onNext(true);
            emitter.onComplete();
        } catch (IOException e) {
            e.printStackTrace();
            emitter.onNext(false);
            emitter.onError(e);
        }
    }

    private void exportCvs(ObservableEmitter<Boolean> emitter){
        StringBuilder sb = new StringBuilder();
        for (int i=0 ;i<title.length-1;i++) {
            sb.append(title[i]).append("\t").append(",");
        }
        sb.append(title[title.length-1]).append("\t").append("\n");
        for (DiaryResponse.DataBean  bean:mMList){
            sb.append(TimeUtil.getMyTimeDay(bean.getDate())).append("\t").append(",")
                    .append(bean.getWeather()).append("\t").append(",")
                    .append(String.valueOf(bean.getCount1())).append("\t").append(",")
                    .append(String.valueOf(bean.getCount2())).append("\t").append(",")
                    .append(String.valueOf(bean.getCount3())).append("\t").append(",")
                    .append(String.valueOf(bean.getCount4())).append("\t").append(",")
                    .append(String.valueOf(bean.getCount5())).append("\t").append(",")
                    .append(String.valueOf(bean.getCount6())).append("\t").append(",")
                    .append(String.valueOf(bean.getCount_total())).append("\t").append(",")
                    .append(String.valueOf(bean.getPh_min())).append("\t").append(",")
                    .append(String.valueOf(bean.getPh_max())).append("\t").append(",")
                    .append(String.valueOf(bean.getPh_range())).append("\t").append(",")
                    .append(String.valueOf(bean.getO2_min())).append("\t").append(",")
                    .append(String.valueOf(bean.getO2_max())).append("\t").append(",")
                    .append(String.valueOf(bean.getO2_range())).append("\t").append(",")
                    .append(String.valueOf(bean.getTemperature_min())).append("\t").append(",")
                    .append(String.valueOf(bean.getTemperature_max())).append("\t").append(",")
                    .append(String.valueOf(bean.getTemperature_range())).append("\t").append(",")
                    .append(bean.getNh()).append("\t").append(",")
                    .append(bean.getNano2()).append("\t").append(",")
                    .append(bean.getAlkali()).append("\t").append(",")
                    .append(bean.getMedicine()).append("\t").append(",")
                    .append(bean.getRemark()).append("\t")
                    .append("\n");
        }
        File saveCSV = new File(mFileSavePath);
        try {
            if(!saveCSV.exists()) {
                boolean createOk = saveCSV.createNewFile();
                if (!createOk){
                    UIUtils.showToast("创建文件失败!");
                    return;
                }
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(saveCSV));
            writer.write(sb.toString());
            writer.close();
            emitter.onNext(true);
            emitter.onComplete();
        } catch (IOException e) {
            e.printStackTrace();
            emitter.onNext(false);
            emitter.onError(e);
        }

    }

    public  void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case AppConst.RECODE_EDIT_FROM_LOG_FILE:
                    mTvFileName.setText(data.getStringExtra(AppConst.RESULT_FROM_EDIT));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
