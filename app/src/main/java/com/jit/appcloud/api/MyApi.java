package com.jit.appcloud.api;


import com.jit.appcloud.model.request.AgAddPondResponse;
import com.jit.appcloud.model.response.DeviceManageResponse;
import com.jit.appcloud.model.response.DiaryResponse;
import com.jit.appcloud.model.response.DownLogResponse;
import com.jit.appcloud.model.response.EpDeviceResponse;
import com.jit.appcloud.model.response.FriendAddGetResponse;
import com.jit.appcloud.model.response.FriendCircleResponse;
import com.jit.appcloud.model.response.GroupCreateResponse;
import com.jit.appcloud.model.response.GroupInfoByIdResponse;
import com.jit.appcloud.model.response.GroupMbQyResponse;
import com.jit.appcloud.model.response.LogDeviceResponse;
import com.jit.appcloud.model.response.LogDrugResponse;
import com.jit.appcloud.model.response.LogFeedResponse;
import com.jit.appcloud.model.response.LogSeedResponse;
import com.jit.appcloud.model.response.LogWaterResponse;
import com.jit.appcloud.model.response.PhotoResponse;
import com.jit.appcloud.model.response.PondAddResponse;
import com.jit.appcloud.model.response.PondGetByMGResponse;
import com.jit.appcloud.model.response.PublishNewsResponse;
import com.jit.appcloud.model.response.GroupsQyResponse;
import com.jit.appcloud.model.response.RgCustomResponse;
import com.jit.appcloud.model.response.SearchFriendResponse;
import com.jit.appcloud.model.response.SensorDfInfoResponse;
import com.jit.appcloud.model.response.SensorNmResponse;
import com.jit.appcloud.model.response.SinglePondResponse;
import com.jit.appcloud.model.response.UserBBAMResponse;
import com.jit.appcloud.model.response.UserBdAmResponse;
import com.jit.appcloud.model.response.ChangePwdResponse;
import com.jit.appcloud.model.response.CheckProductResponse;
import com.jit.appcloud.model.response.UserBdMgResponse;
import com.jit.appcloud.model.response.UserLoginResponse;
import com.jit.appcloud.model.response.NormalResponse;
import com.jit.appcloud.model.response.MyPublishResponse;
import com.jit.appcloud.model.response.NewsAllInfoResponse;
import com.jit.appcloud.model.response.PondMangeResponse;
import com.jit.appcloud.model.response.UserHeadPostResponse;
import com.jit.appcloud.model.response.UserInfoResponse;

import java.util.List;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 *
 * @author zxl
 * @date 2018/4/17
 */

public interface MyApi {
    /**
     * 基础URL
     */
    String BASE_URL = "http://223.2.197.240:8089/";


    /**
     * 用户登录 --- 唯一进入APP的入口
     * @param body
     * @return
     */
    @POST("auth")
    Observable<UserLoginResponse<UserLoginResponse.DataBean>> getLoginCode(@Body RequestBody body);


    /**
     * app 修改密码
     * 注意这是put请求,貌似和get有一点相似啊,
     * 就是不应该放在Body的意思哦
     * @param auto
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @PUT("users/password")
    Observable<ChangePwdResponse> getChangePwdCode(@Header("Authorization") String auto, @Query("oldPassword") String oldPassword, @Query("newPassword") String newPassword);


    /**
     * 注册下级用户
     * @param auto
     * @param body
     * @return
     */
    @POST("register/user")
    Observable<RgCustomResponse> registerNextUser(@Header("Authorization") String auto, @Body RequestBody body);


    /**
     * 头像上传
     * @param auto
     * @param file
     * @return
     * 文件上传一定要添加 Multipart
     */
    @Multipart
    @POST("users/image")
    Observable<UserHeadPostResponse> postHeadImage(@Header("Authorization") String auto,
                                                   @Part MultipartBody.Part file);

    /* ================== 巡视相关开始 ====================*/

    /**
     * 获取用户信息
     * 获取用户，Admin登录的获取的是MANAGER相关信息，MANAGER登录获取的是EMPLOYEE相关信息GET/users/getUsers
     * @param auto
     * @return
     */
    @GET("users/getUsers")
    Observable<UserBdAmResponse> getAgencyNextUserInfo(@Header("Authorization") String auto);


    /**
     * 根据总代理的名字返回对应的塘口信息
     * @param auto token
     * @param realname 总代名字
     * @return 总代对应的塘口信息
     */
    @GET("/users/pounds")
    Observable<PondGetByMGResponse> getAgentPondsInfo(@Header("Authorization") String auto,
                                                      @Query("realname") String realname);


    // 根据用户名获取客户信息 ===  这里即时个人信息,也是用于展示的农户的信息
    @GET("users/user")
    Observable<UserInfoResponse> getUserInfoByName(@Header("Authorization") String auto, @Query("username") String username);

    /* 根据不同角色获取所有客户信息,即通过用户名
    *  用于在 admin登录时获取旗下 manage对应的farm信息
    * */
    @GET("users/")
    Observable<UserBBAMResponse> getMMFMInfo(@Header("Authorization") String auto,@Query("username") String realname);

    // ================ 这个接口待定了 有问题的
    @PUT("users/")
    Observable<NormalResponse> updateUserInfo(@Header("Authorization") String auto,@Body RequestBody body);

    /* 上级更新下级*/
    @PUT("update/user/{id}")
    Observable<NormalResponse> updateCustonmInfo(@Header("Authorization") String auto,@Path("id") int customerId,@Body RequestBody body);


    /* 塘口相关*/
    // 增加  Manager可管理
    @POST("pound/")
    Observable<PondAddResponse> addPond(@Header("Authorization") String auto, @Body RequestBody body);


    //*******
    @POST("pound/batch")
    Observable<AgAddPondResponse> addPondBatch(@Header("Authorization") String auto, @Body RequestBody body);

    // 养殖户获取塘口
    @GET("pound/employee/")
    Observable<PondGetByMGResponse> getPondByEp(@Header("Authorization") String auto);


    /* 获取用户名下的塘口 只有养殖户有*/
    @GET("pound/{username}")
    Observable<PondMangeResponse> getPondInfoByName(@Header("Authorization") String auto,@Path("username") String username);

    /* 根据id删除对应的塘口*/
    @DELETE("pound/{id}")
    Observable<NormalResponse> deletePondByPondId(@Header("Authorization") String auto,@Path("id") int  pondId);

    /* 更新塘口信息*/
    @PUT("pound/{id}")
    Observable<NormalResponse> updatePond(@Header("Authorization") String auto, @Path("id") int  pondId,@Body RequestBody body);


    @POST("water/insert")
    Observable<NormalResponse> insertWater(@Header("Authorization") String auto,@Body RequestBody body);


    @POST("feed/insert")
    Observable<NormalResponse> insertFeed(@Header("Authorization") String auto,@Body RequestBody body);

    @POST("seed/insert")
    Observable<NormalResponse> insertSeed(@Header("Authorization") String auto,@Body RequestBody body);

    @POST("medicine/insert")
    Observable<NormalResponse> insertDrug(@Header("Authorization") String auto,@Body RequestBody body);


    /* 设备信息的录入 */
    @POST("record/insert")
    Observable<NormalResponse> insertDeviceRecord(@Header("Authorization") String auto,@Body RequestBody body);


    @GET("/feed/get")
    Observable<LogFeedResponse> getLogFeed(@Header("Authorization") String auto, @Query("pound_id") int pondId,@Query("pageNum") int pageNum);

    @GET("/seed/get")
    Observable<LogSeedResponse> getLogSeed(@Header("Authorization") String auto, @Query("pound_id") int pondId, @Query("pageNum") int pageNum);

    @GET("/medicine/get")
    Observable<LogDrugResponse> getLogMedicine(@Header("Authorization") String auto, @Query("pound_id") int pondId, @Query("pageNum") int pageNum);

    @GET("/water/get")
    Observable<LogWaterResponse> getLogWater(@Header("Authorization") String auto, @Query("pound_id") int pondId, @Query("pageNum") int pageNum);

    @GET("/record/get/pound")
    Observable<LogDeviceResponse> getLogDevice(@Header("Authorization") String auto, @Query("pound_id") int pondId, @Query("pageNum") int pageNum);

    /* 录入设备*/
    @POST("device/insert")
    Observable<NormalResponse> deviceInsert(@Header("Authorization") String auto,@Body RequestBody body);


    // 初始化密码 -----
    @PUT("users/initPassword")
    Observable<NormalResponse> initPassword(@Header("Authorization") String auto,@Query("username") String username);

    // 删除用户 ===================
    @DELETE("users/{ids}")
    Observable<NormalResponse> deleteUser(@Header("Authorization") String auto,@Path("ids") int id);

    //******* 批量增加设备
    @POST("device/insert/batch")
    Observable<NormalResponse > deviceBatchInsert(@Header("Authorization") String auto, @Body RequestBody body);

    @GET("device/get/user")
    Observable<DeviceManageResponse> deviceInfoGet(@Header("Authorization") String auto,@Query("username") String farmName);

    @DELETE("device/{id}")
    Observable<NormalResponse> deviceDelete(@Header("Authorization") String auto,@Path("id") int deviceId);

    @PUT("device/{id}")
    Observable<NormalResponse> deviceUpdate(@Header("Authorization") String auto,@Body RequestBody body,@Path("id") int deviceId);


    /* 根据塘口获取设备*/
    @GET("device/get/up")
    Observable<DeviceManageResponse> deviceGetByUserAndPond(@Header("Authorization") String auto,@Query("username") String farmName,@Query("pound_id") int pondID);


    /* 获取经销商名下的所有塘口信息*/
    @GET("pound/manager/")
    Observable<PondGetByMGResponse> queryPoundByMgName(@Header("Authorization") String auto, @Query("realname") String realname);


    /* 获取经销商名下的所有塘口信息*/
    @GET("pound/manager/pr")
    Observable<PondGetByMGResponse> queryPoundByEpName(@Header("Authorization") String auto, @Query("realname") String realname);

    // 根据塘口Id获取塘口信息
    @GET("pound/{id}")
    Observable<SinglePondResponse> getSinglePondInfo(@Header("Authorization") String auto,@Path("id") int pound_id);

    @GET("diary/get")
    Observable<DiaryResponse> getDiaryInfo(@Header("Authorization") String auto,@Query("pound_id") int pound_id,@Query("start_date") String start_date,@Query("end_date") String end_date);


    // 更新塘口========
    @POST("pound/private")
    Observable<NormalResponse> epAddPound(@Header("Authorization") String auto, @Body RequestBody requestBody);

    @PUT("pound/{id}")
    Observable<NormalResponse> epUpdatePound(@Header("Authorization") String auto,@Path("id") int id,@Body RequestBody requestBody);

    @DELETE("pound/{id}")
    Observable<NormalResponse> epDeletePound(@Header("Authorization") String auto,@Path("id") int id);

    //其实也可以其他身份的进行查取
    @GET("device/user")
    Observable<EpDeviceResponse> epQueryAllDevice(@Header("Authorization") String auto, @Query("username") String username);

    @DELETE("device/{id}")
    Observable<NormalResponse>  epDeleteSingleDevice(@Header("Authorization") String auto,@Path("id") int id);

    @PUT("device/{id}")
    Observable<NormalResponse> epUpdateDevice(@Header("Authorization") String auto,@Path("id") int id,@Body RequestBody requestBody);

    @POST("device/insert/private")
    Observable<NormalResponse> epInsertDevice(@Header("Authorization") String auto,
                                              @Body RequestBody requestBody);


    /**
     * 日常投放--- 按照餐数
     * @param auto
     * @param pound_id
     * @param count1
     * @param count2
     * @param count3
     * @param count4
     * @param count5
     * @param count6
     * @param date
     * @return
     */
    @POST("feed/insert")
    Observable<NormalResponse> epInsertFeed(@Header("Authorization") String auto,
                                            @Query("pound_id") int pound_id,
                                            @Query("count1") int count1,
                                            @Query("count2") int count2,
                                            @Query("count3") int count3,
                                            @Query("count4") int count4,
                                            @Query("count5") int count5,
                                            @Query("count6") int count6,
                                            @Query("date") String date);
    /**
     * 水质信息插入
     * @param auto
     * @param nh
     * @param nano2
     * @param alkali
     * @param O2
     * @param poundId
     * @param remark
     * @param medicine
     * @param data
     * @param weather
     * @param image
     * @return
     */
    @Multipart
    @POST("water/insert")
    Observable<NormalResponse> epInsertWater(@Header("Authorization") String auto,
                                             @Part("NH") RequestBody nh,
                                             @Part("Nano2") RequestBody nano2,
                                             @Part("Alkali") RequestBody alkali,
                                             @Part("O2") RequestBody O2,
                                             @Part("pound_id") int poundId,
                                             @Part("remark") RequestBody remark,
                                             @Part("medicine") RequestBody medicine,
                                             @Part("date") RequestBody data,
                                             @Part("weather") RequestBody weather,
                                             @Part List<MultipartBody.Part> image);


    // 传感器数据管理

    /**
     * 按时间去查询 普通设备的信息
     * @param auto
     * @param deviceID
     * @param startTime
     * @param endTime
     * @return
     */
    @GET("sensor/period")
    Observable<SensorDfInfoResponse> getDeviceDFByTime(@Header("Authorization") String auto,
                                                 @Query("device_id") int deviceID,
                                                 @Query("start_time") String startTime,
                                                 @Query("end_time") String endTime);

    /**
     * 按天查询普通设备信息
     * @param auto
     * @param deviceID
     * @param date
     * @return
     */
    @GET("sensor/day")
    Observable<SensorDfInfoResponse> getDeviceDFDay(@Header("Authorization") String auto,
                                                    @Query("device_id") int deviceID,
                                                    @Query("date") String date);

    /**
     * 获取普通设备最新的一条记录
     * @param auto
     * @param deviceID
     * @return
     */
    @GET("sensor/latest")
    Observable<NormalResponse> getDeviceLatestIF(@Header("Authorization") String auto,
                                                 @Query("device_id") int deviceID);


    /**
     * 获取便携式设备在某一时间段内的数据
     * @param auto
     * @param deviceID
     * @param startTime
     * @param endTime
     * @return
     */
    @GET("sensor/min/period")
    Observable<NormalResponse> getMiniDeviceDFByTime(@Header("Authorization") String auto,
                                                 @Query("device_id") int deviceID,
                                                 @Query("start_time") String startTime,
                                                 @Query("end_time") String endTime);

    /**
     * 按天查询 便携式设备的信息
     * @param auto
     * @param deviceID
     * @return
     */
    @GET("sensor/min/day")
    Observable<NormalResponse> getMiniDeviceDFDay(@Header("Authorization") String auto,
                                              @Query("device_id") int deviceID);
    /**
     * 获取便携式设备最新的一条记录
     * @param auto
     * @param deviceID
     * @return
     */
    @GET("sensor/min/latest")
    Observable<NormalResponse> getMiniDeviceLatestIF(@Header("Authorization") String auto,
                                                 @Query("device_id") int deviceID);


    /**
     * 根据养殖户的姓名去查询一天的便携设备信息
     * @param auto
     * @param username
     * @param date
     * @return
     */
    @GET("sensor/min/user/day")
    Observable<SensorNmResponse> queryMiniSensorDayByName(@Header("Authorization") String auto,
                                                          @Query("username") String username,
                                                          @Query("date") String date);

    /**
     * 根据塘口ID去查询一天的便携设备信息
     * @param auto
     * @param poundID
     * @param date
     * @return
     */
    @GET("sensor/min/pound/day")
    Observable<SensorNmResponse> queryMiniSensorDayByPondID(@Header("Authorization") String auto,
                                                            @Query("pound_id") int poundID,
                                                            @Query("date") String date);



    /**
     * 依据塘口ID获得最新的消息
     * @param auto
     * @param poundID
     * @return
     */
    @GET("sensor/pound/latest")
    Observable<SensorNmResponse> querySensorLatestByPondID(@Header("Authorization") String auto,
                                                           @Query("pound_id") int poundID);

    /**
     *
     * 依据养殖户姓名获取最新的设备信息
     * @param auto
     * @param username
     * @return
     */
    @GET("sensor/user/latest")
    Observable<SensorNmResponse> querySensorLatestByUName(@Header("Authorization") String auto,
                                                          @Query("username") String username);

    /**
     * 依据经销商名称获取最新的记录
     * @param auto
     * @param username
     * @return
     */
    @GET("sensor/manager/latest")
    Observable<SensorNmResponse> queryFarmSsLatestByUName(@Header("Authorization") String auto,
                                                          @Query("username") String username);




    /* ====================== 平行用户管理  start============================*/
    /**
     *   注册平行用户
     *   @param auto  token信息
     *   @param body  平行用户bean
     *                @return 注册反馈
     * */
    @POST("vice/register")
    Observable<NormalResponse> getAllocateUserCode(@Header("Authorization") String auto, @Body RequestBody body);

    /**
     *   更新平行用户
     *   @param auto token
     *   @param body 平行用户的信息bean
     *               @return 更新反馈
     * */
    @PUT("vice")
    Observable<NormalResponse> updateViceInfo(@Header("Authorization") String auto,@Body RequestBody body);

    /**
     *  删除平行用户
     *  @param auto token
     *  @param viceId 平行用户的id
     *               @return 删除反馈
     * */
    @DELETE("vice/{id}")
    Observable<NormalResponse> deleteViceById(@Header("Authorization") String auto,@Path("id") int viceId);

    /**
     * 用户查询所有的平行用户
     * @param auto token
     * @return 返回平行用户列表
     */
    @GET("vice/all")
    Observable<UserBBAMResponse> queryAllViceInfo(@Header("Authorization") String auto);
    /* ====================== 平行用户管理  start============================*/



    /* 日志记录的管理 ======================*/


    @POST("downlog/add")
    Observable<NormalResponse> addDownloadLog(@Header("Authorization") String auto,@Query("downlogname") String downlogname);

    @DELETE("downlog/{ids}")
    Observable<NormalResponse> deleteSmLogs(@Header("Authorization") String auto,@Path("ids") String ids);

    @GET("downlog/all")
    Observable<DownLogResponse> getaAllLogs(@Header("Authorization") String auto);


    /* 照片墙管理 */

    /**
     *  按照姓名去获取照片墙
      * @param auto
     * @param username
     * @return
     */
    @GET("photos/")
    Observable<PhotoResponse> queryAllPhotos(@Header("Authorization") String auto,@Query("username") String username);

    /**
     * 删除特定的照片
     * @param auto
     * @param ids
     * @return
     */
    @DELETE("photos/{ids}")
    Observable<NormalResponse> deleteSMPhotos(@Header("Authorization") String auto,@Path("ids") String ids);

    /**
     * 添加照片
     * @param auto
     * @param image
     * @return
     */
    @Multipart
    @POST("photos/add")
    Observable<PhotoResponse> addSMPhotos(@Header("Authorization") String auto,@Part List<MultipartBody.Part> image);


    /**
     * 修改签名
     * @param auto
     * @param sign
     * @return
     */
    @POST("users/sign")
    Observable<NormalResponse> updateSignature(@Header("Authorization") String auto,@Query("sign") String sign);


    /* ==================================== 资讯相关开始 =====================================*/

    /**
     * 按照类型查询信息
     * @param auto 权限
     * @param flag 大类别
     * @param type 小类别
     * @return
     */
    @GET("/product/checkProducts")
    Observable<NewsAllInfoResponse> getNewsByType(@Header("Authorization") String auto,
                                                  @Query("flag") String flag,@Query("type") String type);

    /**
     * 查询个人资讯
     * @param auto
     * @param username
     * @return
     */
    @GET("/product/my")
    Observable<MyPublishResponse> getMyPublish(@Header("Authorization") String auto,@Query("username") String username);

    /**
     * 删除个人资讯
     * @param auto
     * @param ids
     * @return
     */
    @DELETE("/product/{ids}")
    Observable<NormalResponse> getPbDeleteCode(@Header("Authorization") String auto, @Path("ids") String  ids);

    /**
     * 更新个人资讯
     * @param auto
     * @param id     资讯ID
     * @param title  标题
     * @param price  价格
     * @param type   类型
     * @param msg_type  消息类型
     * @param hot  是否热销
     * @param discount 价格
     * @param summary  总结
     * @param description 描述
     * @param image    图片
     * @param content  内容
     * @return
     */
    @Multipart
    @PUT("/product/{id}")
    Observable<PublishNewsResponse> updateNewsById(
            @Header("Authorization") String auto,
            @Path("id") int id,
            @Part("title") RequestBody title,
            @Part("price") float price, @Part("type") RequestBody type,
            @Part("msg_type") RequestBody msg_type,
            @Part("hot") int hot,
            @Part("discount") int discount,
            @Part("summary") RequestBody summary,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part image, @Part List<MultipartBody.Part> content);

    /**
     * 发布资讯
     * @param auto
     * @param title
     * @param price
     * @param type
     * @param msg_type
     * @param hot
     * @param discount
     * @param summary
     * @param description
     * @param image
     * @param content
     * @return
     */
    @Multipart
    @POST("product/")
    Observable<PublishNewsResponse> publishProduct(
            @Header("Authorization") String auto,
            @Part("title") RequestBody title,
            @Part("price") float price, @Part("type") RequestBody type,
            @Part("msg_type") RequestBody msg_type,
            @Part("hot") int hot,
            @Part("discount") int discount,
            @Part("summary") RequestBody summary,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part image, @Part List<MultipartBody.Part> content);

    /**
     * 保存操作的顺序
     * @param auto
     * @param id
     * @param ids
     * @return
     */
    @POST("/product/savePosition")
    Observable<NormalResponse> saveMyPbPosition(
            @Header("Authorization") String auto,
            @Query("id") int id,
            @Query("ids") List<Integer> ids
    );


    /*===================================== 资讯相关结束 =====================================*/


    /*===================================== 消息相关开始 =====================================*/
    /**
     * 搜索好友是否存在
     * @param auto
     * @param friendName
     * @return
     */
    @GET("friend/check")
    Observable<SearchFriendResponse> searchFriend(@Header("Authorization") String auto,@Query("friendName") String friendName);


    /**
     * 发送添加好友邀请
     * @param auto
     * @param friendName 好友登录名
     * @param message    好友验证信息
     * @return  返回状态
     */
    @POST("friend/add/")
    Observable<NormalResponse> sendFriendInvitation(@Header("Authorization") String auto,
                                                    @Query("friendName") String friendName,
                                                    @Query("message") String message);


    /**
     * 获得用户下所有好友信息
     * @param auto
     * @return
     */
    @GET("friend/all")
    Observable<FriendAddGetResponse> getAllUserRelationship(@Header("Authorization") String auto);


    /**
     * 同意添加该好友
     * @param auto
     * @param friendName
     * @return
     */
    @POST("friend/agree")
    Observable<NormalResponse> agreeFriend(@Header("Authorization") String auto, @Query("friendName") String friendName);

    /**
     * 根据好友名称获取好友信息
     * @param auto
     * @param friendName
     * @return
     */
    @GET("friend/")
    Observable<UserInfoResponse> getFriendInfoByName(@Header("Authorization") String auto, @Query("friendName") String friendName);

    /**
     * 删除好友
     * @param auto
     * @param friendName
     * @return
     */
    @DELETE("friend/{friendName}")
    Observable<NormalResponse> deleteFriendByName(@Header("Authorization") String auto, @Path("friendName") String friendName);

    /**
     * 修改备注姓名
     * @param auto
     * @param newName
     * @param friendName
     * @return
     */
    @PUT("friend/nickname")
    Observable<NormalResponse> changeNickName(@Header("Authorization") String auto,@Query("newName") String newName,@Query("friendName") String friendName);


    /* 群聊*/
    /**
     * 管理员创建群聊
     * @param auto
     * @param groupName 群聊名称
     * @param members   群成员 - 连接
     * @param image     群头像
     * @return
     */
    @Multipart
    @POST("group/create")
    Observable<GroupCreateResponse> createGroup(@Header("Authorization") String auto,
                                                @Query("groupName") String groupName,
                                                @Query("members") String members,
                                                @Part MultipartBody.Part image
                                           );

    /**
     * 用户登录获取所在群的信息
     * @param auto
     * @return
     */
    @GET("group/")
    Observable<GroupsQyResponse> queryAllGroups(@Header("Authorization") String auto);

    /**
     * 根据具体的群ID去查询群成员
     * @param auto
     * @param groupId
     * @return
     */
    @GET("group/users")
    Observable<GroupMbQyResponse> queryGpMbById(@Header("Authorization") String auto,@Query("group_id") int groupId);


    /**
     * 根据群ID 去查询群聊的信息
     * @param auto
     * @param groupId
     * @return
     */
    @GET("group/{group_id}")
    Observable<GroupInfoByIdResponse> queryGpInfoById(@Header("Authorization") String auto,@Path("group_id") int groupId);


    /**
     * 更新群名称
     * @param auto
     * @param groupName
     * @param groupID
     * @return
     */
    @POST("group/update")
    Observable<NormalResponse> updateGroupName(@Header("Authorization") String auto,@Query("groupName") String groupName,@Query("group_id") int groupID);


    /**
     * 群成员邀请进入群聊
     *     权限-所有成员
     * @param auto
     * @param groupID
     * @param groupIDs
     * @return
     */
    @POST("group/add")
    Observable<NormalResponse> addGroupMb(@Header("Authorization") String auto,
                                          @Query("group_id") int groupID,
                                          @Query("ids") String groupIDs);

    /**
     * 已经知道群号 -- 加入群聊
     * @param auto
     * @param groupID
     * @return
     */
    @POST("group/join")
    Observable<NormalResponse> joinGroup(@Header("Authorization") String auto,
                                         @Query("group_id") int groupID);

    /**
     * 移除,群成员
     *     权限-群主 admin = 1
     * @param auto
     * @param groupID
     * @param groupIDs
     * @return
     */
    @DELETE("group/remove")
    Observable<NormalResponse> removeGroupMb(@Header("Authorization") String auto,@Query("group_id") int groupID,@Query("ids") String groupIDs);


    /**
     * 群成员退出群聊
     * @param auto
     * @param groupID
     * @return
     */
    @DELETE("group/quit")
    Observable<NormalResponse> quitGroup(@Header("Authorization") String auto,@Query("group_id") int groupID);

    /**
     * 群主解散-群/切记 只有群主才有此权限
     * @param auto
     * @param groupID
     * @return
     */
    @DELETE("group/dismiss")
    Observable<NormalResponse> dismissGroup(@Header("Authorization") String auto,@Query("group_id") int groupID);


    /**
     * 群成员修改用于成员昵称
     * @param auto
     * @param userID
     * @param nickname
     * @return
     */
    @POST("group/nickname")
    Observable<NormalResponse> updateNickNameInGroup(@Header("Authorization") String auto,
                                                     @Query("group_id") int groupID,
                                                     @Query("user_id") String userID,
                                                     @Query("nickname") String nickname);

    /**
     * 改变成员  显示昵称状态
     * @param auto
     * @param groupID
     * @param userID
     * @param status
     * @return
     */
    @GET("group/status")
    Observable<NormalResponse> updateShowNickStatus(@Header("Authorization") String auto,
                                                    @Query("group_id") int groupID,
                                                    @Query("user_id") String userID,
                                                    @Query("status") String status);

    /**
     * 发表朋友圈信息
     * @param auto
     * @param content
     * @param image
     * @return
     */
    @Multipart
    @POST("circle/")
    Observable<NormalResponse> publishMyCircleInfo(@Header("Authorization") String auto,
                                                   @Query("content") String content,
                                                   @Part List<MultipartBody.Part> image);

    @GET("circle/list")
    Observable<NormalResponse> queryFdCircleInfo(@Header("Authorization") String auto,@Query("username") String username);


    /**
     * 获得所有的朋友的最新动态 -----
     * @param auto
     * @return
     */
    @GET("circle/all")
    Observable<FriendCircleResponse> queryAllFdCircleInfo(@Header("Authorization") String auto);

    /**
     * 取消赞
     * @param auto
     * @param messageID
     * @return
     */
    @DELETE("circle/yes/{message_id}")
    Observable<NormalResponse> cancelClickYes(@Header("Authorization") String auto,@Path("message_id") int messageID);

    /**
     * 点赞
     * @param auto
     * @param messageID
     * @return
     */
    @GET("circle/yes/{message_id}")
    Observable<NormalResponse> addClickYes(@Header("Authorization") String auto,@Path("message_id") int messageID);


    /**
     * 持有者删除朋友圈
     * @param auto
     * @param messageID
     * @return
     */
    @DELETE("circle/{message_id}")
    Observable<NormalResponse> deleteMyFdCircle(@Header("Authorization") String auto,@Path("message_id") int messageID);


    /**
     * 对朋友圈进行评论
     * @param auto
     * @param messageID
     * @param content
     * @return
     */
    @POST("circle/addComment/{id}")
    Observable<NormalResponse> pbCircleComment(@Header("Authorization") String auto,
                                               @Path("id") int messageID,
                                               @Query("content") String content);


    @POST("circle/comment/{comment_id}")
    Observable<NormalResponse> pbPersonCommit(@Header("Authorization") String auto,
                                              @Path("comment_id") int commentID,
                                              @Query("message_id") int messageID,
                                              @Query("content") String content);

    /*======================================= 消息相关结束 ====================================*/
}


