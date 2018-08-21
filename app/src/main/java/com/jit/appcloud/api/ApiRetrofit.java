package com.jit.appcloud.api;
import com.jit.appcloud.api.base.MyBaseApiRetrofit;
import com.jit.appcloud.model.bean.AgAddPondBean;
import com.jit.appcloud.model.bean.DeviceBatchInsert;
import com.jit.appcloud.model.cache.UserCache;
import com.jit.appcloud.model.request.AddPondRequest;
import com.jit.appcloud.model.request.AgAddPondResponse;
import com.jit.appcloud.model.request.AllocateLookUserRequest;
import com.jit.appcloud.model.request.ChangePwdRequest;
import com.jit.appcloud.model.request.DeviceInsertRequest;
import com.jit.appcloud.model.request.EpDeviceRequest;
import com.jit.appcloud.model.request.EpInsertFeedRequest;
import com.jit.appcloud.model.request.EpInsertWaterRequest;
import com.jit.appcloud.model.request.EpPondRequest;
import com.jit.appcloud.model.request.InsertDeviceLogRequest;
import com.jit.appcloud.model.request.InsertDrugRequest;
import com.jit.appcloud.model.request.InsertFeedRequest;
import com.jit.appcloud.model.request.InsertSeedRequest;
import com.jit.appcloud.model.request.InsertWaterRequest;
import com.jit.appcloud.model.request.LoginRequest;
import com.google.gson.Gson;
import com.jit.appcloud.model.request.PublishProductRequest;
import com.jit.appcloud.model.request.RgCustomRequest;
import com.jit.appcloud.model.request.UserInfoUpRequest;
import com.jit.appcloud.model.response.DeviceManageResponse;
import com.jit.appcloud.model.response.DiaryResponse;
import com.jit.appcloud.model.response.DownLogResponse;
import com.jit.appcloud.model.response.EpDeviceResponse;
import com.jit.appcloud.model.response.FriendAddGetResponse;
import com.jit.appcloud.model.response.FriendCircleResponse;
import com.jit.appcloud.model.response.GroupCreateResponse;
import com.jit.appcloud.model.response.GroupInfoByIdResponse;
import com.jit.appcloud.model.response.GroupMbQyResponse;
import com.jit.appcloud.model.response.GroupsQyResponse;
import com.jit.appcloud.model.response.LogDeviceResponse;
import com.jit.appcloud.model.response.LogDrugResponse;
import com.jit.appcloud.model.response.LogFeedResponse;
import com.jit.appcloud.model.response.LogSeedResponse;
import com.jit.appcloud.model.response.LogWaterResponse;
import com.jit.appcloud.model.response.PhotoResponse;
import com.jit.appcloud.model.response.PondAddResponse;
import com.jit.appcloud.model.response.PondGetByMGResponse;
import com.jit.appcloud.model.response.PublishNewsResponse;
import com.jit.appcloud.model.response.RgCustomResponse;
import com.jit.appcloud.model.response.SearchFriendResponse;
import com.jit.appcloud.model.response.SensorDfInfoResponse;
import com.jit.appcloud.model.response.SensorNmResponse;
import com.jit.appcloud.model.response.SinglePondResponse;
import com.jit.appcloud.model.response.UserBBAMResponse;
import com.jit.appcloud.model.response.UserBdAmResponse;
import com.jit.appcloud.model.response.ChangePwdResponse;
import com.jit.appcloud.model.response.UserBdMgResponse;
import com.jit.appcloud.model.response.UserLoginResponse;
import com.jit.appcloud.model.response.NormalResponse;
import com.jit.appcloud.model.response.MyPublishResponse;
import com.jit.appcloud.model.response.NewsAllInfoResponse;
import com.jit.appcloud.model.response.PondMangeResponse;
import com.jit.appcloud.model.response.UserHeadPostResponse;
import com.jit.appcloud.model.response.UserInfoResponse;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * @author 张先磊
 * @date 2018/4/17
 */

public class ApiRetrofit extends MyBaseApiRetrofit {

    public MyApi mApi;
    public static ApiRetrofit mInstance;
    private ApiRetrofit(){
        //在构造方法中完成对Retrofit接口的初始化

        mApi = new Retrofit.Builder()
                .baseUrl(MyApi.BASE_URL)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(MyApi.class);
    }

    public static ApiRetrofit getInstance(){
        if (mInstance == null){
            synchronized (ApiRetrofit.class){
                if (mInstance == null){
                    mInstance = new ApiRetrofit();
                }
            }
        }
        return  mInstance;
    }


    public Observable<UserLoginResponse<UserLoginResponse.DataBean>> getLoginCode(LoginRequest loginRequest){
        return mApi.getLoginCode(getMyRequestBody(loginRequest));
    }

    public Observable<ChangePwdResponse> getChangePwdCode(String auth, ChangePwdRequest changePwdRequest){
        return mApi.getChangePwdCode(auth,changePwdRequest.getOldPassword(),changePwdRequest.getNewPassword());
    }

    public Observable<RgCustomResponse> registerNextUser(RgCustomRequest rgCustomRequest){
        return mApi.registerNextUser(UserCache.getToken(),getMyRequestBody(rgCustomRequest));
    }


    /* ====================== 平行用户管理  start============================*/

    /**
     * 增加平行用户
     * @param auth token
     * @param allocateLookUserRequest 平行用户bean
     * @return 反馈
     */
    public Observable<NormalResponse> getAllocateUserCode(String auth,AllocateLookUserRequest allocateLookUserRequest){
        return mApi.getAllocateUserCode(auth,getMyRequestBody(allocateLookUserRequest));
    }


    /**
     * 更新平行用户
     * @param allocateLookUserRequest 平行用户bean
     * @return 反馈
     */
    public Observable<NormalResponse> updateViceInfo(AllocateLookUserRequest allocateLookUserRequest){
        return mApi.updateViceInfo(UserCache.getToken(),getMyRequestBody(allocateLookUserRequest));
    }


    /**
     * 通过id 删除平行用户
     * @param viceId id
     * @return 反馈
     */
    public Observable<NormalResponse> deleteViceById(int viceId){
        return mApi.deleteViceById(UserCache.getToken(),viceId);
    }


    /**
     * 用户查询所有的平行用户
     * @return 返回平行用户列表
     *
     */
    public Observable<UserBBAMResponse> queryAllViceInfo(){
        return mApi.queryAllViceInfo(UserCache.getToken());
    }


    /* ====================== 平行用户管理  end============================*/



    /* ================= 巡视检查 ====================*/

    /* admin获得经销商信息 */
    public Observable<UserBdAmResponse> getAgencyNextUserInfo(String auth){
        return mApi.getAgencyNextUserInfo(auth);
    }


    public Observable<PondGetByMGResponse> getAgentPondsInfo(String username){
        return mApi.getAgentPondsInfo(UserCache.getToken(),username);
    }

    /* 获得用户信息*/
    public Observable<UserInfoResponse> getUserInfoByName(String userName){
        return mApi.getUserInfoByName(UserCache.getToken(),userName);
    }


    /* 根据用户名获取其下的客户信息*/
    public Observable<UserBBAMResponse> getMMFMInfo(String auth,String name){
        return mApi.getMMFMInfo(auth,name);
    }

    /* 更新用户信息*/

    public Observable<NormalResponse> updateUserInfo(String auth, UserInfoUpRequest userInfoUpRequest){
        return mApi.updateUserInfo(auth,getMyRequestBody(userInfoUpRequest));
    }

    public Observable<NormalResponse> updateCustomInfo(int customerId,UserInfoUpRequest userInfoUpRequest){
        return mApi.updateCustonmInfo(UserCache.getToken(),customerId,getMyRequestBody(userInfoUpRequest));
    }




    public Observable<PondAddResponse> addPondByManager(String auth, AddPondRequest addPondRequest){
        return mApi.addPond(auth,getMyRequestBody(addPondRequest));
    }

    public Observable<AgAddPondResponse> addPondBratch(List<AgAddPondBean> beans){
        return mApi.addPondBatch(UserCache.getToken(),getMyRequestBody(beans));
    }

    public Observable<PondMangeResponse> getPondInfoByName(String auth,String userName){
        return mApi.getPondInfoByName(auth,userName);
    }

    public Observable<NormalResponse> deletePondByPondId(String auth,int pondId){
        return mApi.deletePondByPondId(auth,pondId);
    }

    public Observable<NormalResponse> updatePond(String auth,int pondId,AddPondRequest addPondRequest){
        return mApi.updatePond(auth,pondId,getMyRequestBody(addPondRequest));
    }


    public Observable<NormalResponse> insertDrug(String auth, InsertDrugRequest insertDrugRequest){
        return mApi.insertDrug(auth,getMyRequestBody(insertDrugRequest));
    }

    public Observable<NormalResponse> insertFeed(String auth, InsertFeedRequest insertFeedRequest){
        return mApi.insertFeed(auth,getMyRequestBody(insertFeedRequest));
    }

    public Observable<NormalResponse> insertSeed(String auth, InsertSeedRequest insertSeedRequest){
        return mApi.insertSeed(auth,getMyRequestBody(insertSeedRequest));
    }

    public Observable<NormalResponse> insertWater(String auth, InsertWaterRequest insertWaterRequest){
        return mApi.insertWater(auth,getMyRequestBody(insertWaterRequest));
    }

    public Observable<NormalResponse> insertDeviceRecord(String auth, InsertDeviceLogRequest insertDeviceLogRequest){
        return mApi.insertDeviceRecord(auth,getMyRequestBody(insertDeviceLogRequest));
    }


    public Observable<LogFeedResponse> getLogFeed(String auth, int pondId,int pageNum){
        return mApi.getLogFeed(auth,pondId,pageNum);
    }

    public Observable<LogSeedResponse> getLogSeed(String auth, int pondId, int pageNum){
        return mApi.getLogSeed(auth,pondId,pageNum);
    }

    public Observable<LogDrugResponse> getLogMedicine(String auth, int pondId, int pageNum){
        return mApi.getLogMedicine(auth,pondId,pageNum);
    }

    public Observable<LogWaterResponse> getLogWater(String auth, int pondId, int pageNum){
        return mApi.getLogWater(auth,pondId,pageNum);
    }

    public Observable<LogDeviceResponse> getLogDevice(String auth, int pondId, int pageNum){
        return mApi.getLogDevice(auth,pondId,pageNum);
    }


    public Observable<NormalResponse> deviceInsert(String auth, DeviceInsertRequest insertRequest){
        return mApi.deviceInsert(auth,getMyRequestBody(insertRequest));
    }

    public Observable<NormalResponse> initPassword(String username){
        return mApi.initPassword(UserCache.getToken(),username);
    }

    public Observable<NormalResponse> deleteUser(int userId){
        return mApi.deleteUser(UserCache.getToken(),userId);
    }

    public Observable<NormalResponse> deviciceBatchInsert(List<DeviceBatchInsert> insertList){
        return mApi.deviceBatchInsert(UserCache.getToken(),getMyRequestBody(insertList));
    }


    public Observable<DeviceManageResponse> deviceInfoGet(String auth,String farmName){
        return mApi.deviceInfoGet(auth,farmName);
    }

    public Observable<NormalResponse> deviceDelete(String auth,int deviceId){
        return mApi.deviceDelete(auth,deviceId);
    }

    public Observable<NormalResponse> deviceUpdate(String auth,DeviceInsertRequest insertRequest,int deviceId){
        return mApi.deviceUpdate(auth,getMyRequestBody(insertRequest),deviceId);
    }

    public Observable<DeviceManageResponse> deviceGetByUserAndPond(String auth,String userName,int pond_id){
        return mApi.deviceGetByUserAndPond(auth,userName,pond_id);
    }

    public Observable<PondGetByMGResponse> queryPoundByMgName(String realname){
        return mApi.queryPoundByMgName(UserCache.getToken(),realname);
    }

    public Observable<PondGetByMGResponse> queryPoundByEpName(String realname){
        return mApi.queryPoundByEpName(UserCache.getToken(),realname);

    }

    public Observable<PondGetByMGResponse> getPondByEp(){
        return mApi.getPondByEp(UserCache.getToken());
    }

    public Observable<SinglePondResponse> getSinglePondInfo(int pound_id){
        return mApi.getSinglePondInfo(UserCache.getToken(),pound_id);
    }

    public Observable<DiaryResponse> getDiaryInfo(int pound_id,String start_date,String end_date){
        return mApi.getDiaryInfo(UserCache.getToken(),pound_id,start_date,end_date);
    }

    public Observable<NormalResponse> epAddPound(EpPondRequest epPondRequest){
        return mApi.epAddPound(UserCache.getToken(),getMyRequestBody(epPondRequest));
    }

    public Observable<NormalResponse> epUpdatePound(int poundId,EpPondRequest epPondRequest){
        return mApi.epUpdatePound(UserCache.getToken(),poundId,getMyRequestBody(epPondRequest));
    }

    public Observable<NormalResponse> epDeletePound(int poundId){
        return mApi.epDeletePound(UserCache.getToken(),poundId);
    }

    public Observable<EpDeviceResponse> epQueryAllDevice(String userName){
        return mApi.epQueryAllDevice(UserCache.getToken(),userName);
    }

    public Observable<NormalResponse> epDeleteSingleDevice(int id){
        return mApi.epDeleteSingleDevice(UserCache.getToken(),id);
    }

    public Observable<NormalResponse> epUpdateDevice(int id, EpDeviceRequest request){
        return mApi.epUpdateDevice(UserCache.getToken(),id,getMyRequestBody(request));
    }

    public Observable<NormalResponse> epInsertDevice(EpDeviceRequest request){
        return mApi.epInsertDevice(UserCache.getToken(),getMyRequestBody(request));
    }

    public Observable<NormalResponse> getDeviceDFByTime(int deviceID,String startTime,String endTime){
        return mApi.getDeviceDFByTime(UserCache.getToken(),deviceID,startTime,endTime);
    }

    public Observable<SensorDfInfoResponse> getDeviceDFDay(int deviceID, String dateString){
        return mApi.getDeviceDFDay(UserCache.getToken(),deviceID,dateString);
    }

    public Observable<NormalResponse> getDeviceLatestIF(int deviceID){
        return mApi.getDeviceLatestIF(UserCache.getToken(),deviceID);
    }

    public Observable<NormalResponse> getMiniDeviceDFByTime(int deviceID,String startTime,String endTime){
        return mApi.getMiniDeviceDFByTime(UserCache.getToken(),deviceID,startTime,endTime);
    }

    public Observable<NormalResponse> getMiniDeviceDFDay(int deviceID){
        return mApi.getMiniDeviceDFDay(UserCache.getToken(),deviceID);
    }

    public Observable<NormalResponse> getMiniDeviceLatestIF(int deviceID){
        return mApi.getMiniDeviceLatestIF(UserCache.getToken(),deviceID);
    }

    public Observable<SensorNmResponse> queryMiniSensorDayByName(String userName,String date){
        return mApi.queryMiniSensorDayByName(UserCache.getToken(),userName,date);
    }

    public Observable<SensorNmResponse> queryMiniSensorDayByPondID(int pondID,String date){
        return mApi.queryMiniSensorDayByPondID(UserCache.getToken(),pondID,date);
    }

    public Observable<SensorNmResponse> querySensorLatestByPondID(int poundID){
        return mApi.querySensorLatestByPondID(UserCache.getToken(),poundID);
    }

    public Observable<SensorNmResponse> querySensorLatestByUName(String username){
        return mApi.querySensorLatestByUName(UserCache.getToken(),username);
    }

    public Observable<SensorNmResponse> queryFarmSsLatestByUName(String userName){
        return mApi.queryFarmSsLatestByUName(UserCache.getToken(),userName);
    }


    /* ================= 巡视检查 ====================*/
    //--------------------------日志管理
    public Observable<NormalResponse> addDownloadLog(String downloadName){
        return mApi.addDownloadLog(UserCache.getToken(),downloadName);
    }

    public Observable<NormalResponse> deleleSmLogs(String ids){
        return mApi.deleteSmLogs(UserCache.getToken(),ids);
    }

    public Observable<DownLogResponse> getAllLogs(){
        return mApi.getaAllLogs(UserCache.getToken());
    }

    public Observable<NormalResponse> deleteSMPhotos(String ids){
        return mApi.deleteSMPhotos(UserCache.getToken(),ids);
    }

    public Observable<PhotoResponse> queryAllPhotos(String username){
        return mApi.queryAllPhotos(UserCache.getToken(),username);
    }

    public Observable<PhotoResponse> addSMPhotos(List<File> mImages){
        return mApi.addSMPhotos(UserCache.getToken(),getImageListBody(mImages));
    }


    public Observable<NormalResponse> updateSignature(String sign){
        return mApi.updateSignature(UserCache.getToken(),sign);
    }

    /* ================= 资讯相关开始 ===============*/

    public Observable<NewsAllInfoResponse> getNewsByType(String flag,String type){
        return mApi.getNewsByType(UserCache.getToken(),flag,type);
    }

    public Observable<MyPublishResponse> getMyPublish(String auth,String username){
        return mApi.getMyPublish(auth,username);
    }

    public Observable<NormalResponse> getPbDeleteCode(String auth, String  ids){
        return mApi.getPbDeleteCode(auth,ids);
    }

    public Observable<PublishNewsResponse> updateNewsById(int id,PublishProductRequest productRequest){
        return mApi.updateNewsById(UserCache.getToken(),id,
                getFormRequestBody(productRequest.getTitle()),
                productRequest.getPrice(),getFormRequestBody(productRequest.getType()),
                getFormRequestBody(productRequest.getMsg_type()),
                productRequest.getHot(),
                productRequest.getDiscount(),
                getFormRequestBody(productRequest.getSummary()),
                getFormRequestBody(productRequest.getDescription()),
                getMultipartBody(productRequest.getImage()),getContentImageBody(productRequest.getContent()));
    }

    public Observable<PublishNewsResponse> publishProduct(String auth, PublishProductRequest productRequest){
        return mApi.publishProduct(auth,getFormRequestBody(productRequest.getTitle()),
                productRequest.getPrice(),getFormRequestBody(productRequest.getType()),
                getFormRequestBody(productRequest.getMsg_type()),
                productRequest.getHot(),
                productRequest.getDiscount(),
                getFormRequestBody(productRequest.getSummary()),
                getFormRequestBody(productRequest.getDescription()),
                getMultipartBody(productRequest.getImage()),getContentImageBody(productRequest.getContent()));
    }

    public Observable<NormalResponse> saveMyPbPosition(int id,List<Integer> mList){
        return mApi.saveMyPbPosition(UserCache.getToken(),id,mList);
    }

    /* ================= 资讯相关结束 ===============*/

    /**
     * 上传头像
     * @param auth
     * @param file 文件
     * @return
     */
    public Observable<UserHeadPostResponse> postHeadImage(String auth, File file){
        return mApi.postHeadImage(auth,getMultipartBody(file,"file"));
    }

    public Observable<SearchFriendResponse> searchFriend(String auth,String friendName){
        return mApi.searchFriend(auth,friendName);
    }

    public Observable<NormalResponse> sendFriendInvitation(String auth,String friendName,String message){
        return mApi.sendFriendInvitation(auth,friendName,message);
    }

    public Observable<FriendAddGetResponse> getAllUserRelationship(String auth){
        return mApi.getAllUserRelationship(auth);
    }

    public Observable<NormalResponse> agreeFriend(String auth,String friendName){
        return mApi.agreeFriend(auth,friendName);
    }

    public Observable<UserInfoResponse> getFriendInfoByName(String auth,String friendName){
        return mApi.getFriendInfoByName(auth,friendName);
    }

    public Observable<NormalResponse> deleteFriendByName(String auth,String friendName){
        return mApi.deleteFriendByName(auth,friendName);
    }

    public Observable<NormalResponse> changeNickName(String auth,String newNickName,String friendName){
        return mApi.changeNickName(auth,newNickName,friendName);
    }

    public Observable<GroupCreateResponse> createGroup(String groupName, String groupMembers, File groupImage){
        return mApi.createGroup(UserCache.getToken(),groupName,groupMembers,getMultipartBody(groupImage));
    }

    public Observable<GroupsQyResponse> queryAllGroups(){
        return mApi.queryAllGroups(UserCache.getToken());
    }

    public Observable<GroupMbQyResponse> queryGpMbById(int groupId){
        return mApi.queryGpMbById(UserCache.getToken(),groupId);
    }

    public Observable<GroupInfoByIdResponse> queryGpInfoById(int groupId){
        return mApi.queryGpInfoById(UserCache.getToken(),groupId);
    }

    public Observable<NormalResponse> updateGroupName(String groupName,int groupId){
        return mApi.updateGroupName(UserCache.getToken(),groupName,groupId);
    }

    public Observable<NormalResponse> addGroupMb(int groupID,String groupMbIDS){
        return mApi.addGroupMb(UserCache.getToken(),groupID,groupMbIDS);
    }

    public Observable<NormalResponse> joinGroup(int groupID){
        return mApi.joinGroup(UserCache.getToken(),groupID);
    }

    public Observable<NormalResponse> removeGroupMb(int groupID,String groupMbIDS){
        return mApi.removeGroupMb(UserCache.getToken(),groupID,groupMbIDS);
    }

    public Observable<NormalResponse> quitGroup(int groupID){
        return mApi.quitGroup(UserCache.getToken(),groupID);
    }

    public Observable<NormalResponse> dismissGroup(int groupID){
        return mApi.dismissGroup(UserCache.getToken(),groupID);
    }

    public Observable<NormalResponse> updateNickNameInGroup(int groupID,String userID,String nickName){
        return mApi.updateNickNameInGroup(UserCache.getToken(),groupID,userID,nickName);
    }

    public Observable<NormalResponse> updateShowNickStatus(int groupID,String userID,String status){
        return mApi.updateShowNickStatus(UserCache.getToken(),groupID,userID,status);
    }

    public Observable<NormalResponse> publishMyCircleInfo(String content,List<File> mList){
        return mApi.publishMyCircleInfo(UserCache.getToken(),content,getPicListBody(mList));
    }


    public Observable<FriendCircleResponse> queryAllFdCircleInfo(){
        return mApi.queryAllFdCircleInfo(UserCache.getToken());
    }

    public Observable<NormalResponse> cancelClickYes(int messageID){
        return mApi.cancelClickYes(UserCache.getToken(),messageID);
    }

    public Observable<NormalResponse> addClickYes(int messageID){
        return mApi.addClickYes(UserCache.getToken(),messageID);
    }

    public Observable<NormalResponse> deleteMyFdCircle(int messageID){
        return mApi.deleteMyFdCircle(UserCache.getToken(),messageID);
    }

    public Observable<NormalResponse> pbCircleComment(int messageID,String content){
        return mApi.pbCircleComment(UserCache.getToken(),messageID,content);
    }

    public Observable<NormalResponse> pbPersonCommit(int commentID,int messageID,String content){
        return mApi.pbPersonCommit(UserCache.getToken(),commentID,messageID,content);
    }

    public Observable<NormalResponse> epInsertFeed(EpInsertFeedRequest request){
        return mApi.epInsertFeed(UserCache.getToken(),request.getPound_id(),request.getCount1()
        ,request.getCount2(),request.getCount3(),request.getCount4(),request.getCount5(),request.getCount6()
        ,request.getDate());
    }

    public Observable<NormalResponse> epInsertWater(EpInsertWaterRequest request){
        return mApi.epInsertWater(UserCache.getToken(),
                getFormRequestBody(request.getNh()),
                getFormRequestBody(request.getNano2()),
                getFormRequestBody(request.getAlkali()),
                getFormRequestBody(request.getO2()),
                request.getPound_id(),
                getFormRequestBody(request.getRemark()),
                getFormRequestBody(request.getMedicine()),
                getFormRequestBody(request.getData()),
                getFormRequestBody(request.getWeather()),
                getImageListBody(request.getContent()));
    }

    private RequestBody getMyRequestBody(Object obj) {
        String route = new Gson().toJson(obj);
        return RequestBody.create(MediaType.parse("application/json"),route);
    }

    private RequestBody getFormRequestBody(String obj){
        if (obj != null) {
            return RequestBody.create(MediaType.parse("multipart/form-data"), obj);
        }
        return null;
    }

    private RequestBody getRequestBody(Object obj) {
        String route = new Gson().toJson(obj);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),route);
        return body;
    }

    private MultipartBody.Part getMultipartBody(File file,String type){
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multipartBody =
                MultipartBody.Part.createFormData(type, file.getName(), requestFile);
        return multipartBody;
    }



    private MultipartBody.Part getMultipartBody(File file){
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part multipartBody =
                MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        return multipartBody;
    }

    private List<MultipartBody.Part> getContentImageBody(List<File> content){
        if (content != null && content.size()!=0) {
            List<MultipartBody.Part> parts = new ArrayList<>(content.size());
            for (File file : content) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData("content", file.getName(), requestBody);
                parts.add(part);
            }
            return parts;
        }
        return null;
    }

    private List<MultipartBody.Part> getImageListBody(List<File> content){
        if (content != null && content.size()!=0) {
            List<MultipartBody.Part> parts = new ArrayList<>(content.size());
            for (File file : content) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
                parts.add(part);
            }
            return parts;
        }
        return null;
    }

    private List<MultipartBody.Part> getPicListBody(List<File> content){
        if (content != null && content.size()!=0) {
            List<MultipartBody.Part> parts = new ArrayList<>(content.size());
            for (File file : content) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData("picture", file.getName(), requestBody);
                parts.add(part);
            }
            return parts;
        }
        return null;
    }
}
