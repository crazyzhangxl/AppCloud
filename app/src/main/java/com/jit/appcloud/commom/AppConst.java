package com.jit.appcloud.commom;
import com.jit.appcloud.util.FileUtils;
import com.jit.appcloud.util.StringUtils;
import com.jit.appcloud.widget.StatusBarCompat;

import retrofit2.http.PUT;

/**
 *  常量类 ===========
 * @author zxl
 * @date 2018/4/9
 */

public class AppConst {
    public static boolean isDebug = true;//发布时;false
    public static String SP_NAME = "config";//sp 文件保存名
    public static boolean isTesting = false; // 是否后台未开启
    public static String PS_SAVE_DIR = "appCloud"; // 文件保存Dir
    public static String RECORD = "record"; // log日志保存的2级文件夹
    public static String PS_SAVE_LEAST_DIR = "app_least_cloud"; // 临时保存的地点
    public static int NUM_KEY_INPUT = 50; // 设置输入的最大字符为50
    public static int HOBBY_MAX_NUM = 5;

    public static String SESSION_TOP_NAME = "com.jit.appcloud.ui.activity.message.SessionActivity";
    /** 最大的投喂餐数*/
    public static final int MAX_FEED_NUM = 6;

    /**
     *  检测界面的SPINNER类型
     */
    public static final int SP_TYPE_EMPLOYEE = 2;
    public static final int SP_TYPE_POND = 3;

    /*  用户权限*/
    public static String ROLE_FARMER = "ROLE_EMPLOYEE"; // 农户
    public static String ROLE_AGENCY= "ROLE_MANAGER";   // 经销商
    public static String ROLE_GENERAL_AGENCY = "ROLE_ADMIN"; // 总部
    public static String ROLE_AGENT = "ROLE_AGENT";// 总代理
    public static String ROLE_VICE_ADMIN= "ROLE_VICE_ADMIN";     // 平行总部
    public static String ROLE_VICE_AGENT = "ROLE_VICE_AGENT";    // 平行总代
    public static String ROLE_VICE_MANAGER = "ROLE_VICE_MANAGER";     // 平行经销商


    // 以下为天气相关
    public static String LOCATION_CITY_NOWS_ID = "location_city_now_id"; //当前天气城市id
    public static String LOCATE_CITY_str = "locate_city_str";   // 定位的城市str 存一个district就好了
    public static int UPDATE_WEATHER_FROM_FG = 201;
    public static String SIGN_UPDATE_VIEW = "更新View";
    /* ================================== 消息相关开始 ===================================*/

    public final static int SESSION_TYPE_PRIVATE = 1;
    public final static int SESSION_TYPE_GROUP = 2;

    public static final String FETCH_COMPLETE = "fetch_complete";//全局数据获取

    //好友

    public static final String UPDATE_FRIEND = "update_friend";  // 更新联系人 即已经是好友的关系
    public static final String UPDATE_NEW_FRIEND = "update_new_friend";/* 更新好友   即我添加和同意等进行反馈*/
    public static final String UPDATE_RED_DOT = "update_red_dot";

    //群组
    public static final String  GROUP_ROLE_CREATE = "1"; // 群创建者
    public static final String  GROUP_ROLE_NORMAL = "0"; // 群普通成员

    /**
     * 该条目 是否已近置顶
     */
    public static final String CONVERSATION_IS_TOP = "conversation_is_top";

    public static final String EXTRA_GROUP_ID = "extra_group_id";


    public static final String UPDATE_GROUP_NAME = "update_group_name";
    public static final String GROUP_LIST_UPDATE = "group_list_update";
    public static final String UPDATE_GROUP = "update_group";
    public static final String UPDATE_GROUP_MEMBER = "update_group_member";
    public static final String GROUP_DISMISS = "group_dismiss";

    //个人信息

    public static final String GROUP_FLAG_CREATE = "group_flag_create";
    public static final String GROUP_FLAG_UPDATE = "group_flag_update";

    public static final String CHANGE_INFO_FOR_ME = "change_info_for_me";
    public static final String CHANGE_INFO_FOR_CHANGE_NAME = "change_info_for_change_name";
    public static final String CHANGE_INFO_FOR_USER_INFO = "change_info_for_user_info";

    public static final String UPDATE_CONVERSATIONS = "update_conversations";
    public static final String UPDATE_CURRENT_SESSION = "update_current_session";
    public static final String UPDATE_CURRENT_SESSION_NAME = "update_current_session_name";
    public static final String REFRESH_CURRENT_SESSION = "refresh_current_session";
    public static final String CLOSE_CURRENT_SESSION = "close_current_session";
    public static final String SHOW_NOTIFICATION = "show_notification";
    /**
     * 刷新朋友圈
     */
    public static final String REFRESH_FRIEND_CIRCLE = "refresh_friend_circle";

    public static final class QrCodeCommon {
        public static final String ADD = "add:";//加好友
        public static final String JOIN = "join:";//入群
    }
    /* ================================== 消息相关结束 ===================================*/

    /* ================= 用户相关 =================*/

    public static final class User {
        public static final String ID = "id";
        public static final String Name = "name";
        public static final String ROLE = "role";
        public static final String Token = "token";
        public static final String RONG_TOKEN = "rong_token";
        public static final String IMAGE_HEAD = "image_head";
        public static final String REGISTER_TIME = "register_time";
        public static final String PASSWORD = "password";
        public static final String NOTIFY_MESSAGE = "notify_message";
    }


    public static final class UserNormal{
        public static final String DEPARTMENT = "department";
        public static final String PROVINCE = "province";
        public static final String CITY = "city";
        public static final String COUNTRY = "country";
        public static final String ADDRESS = "address";
        public static final String AREA = "area";
        public static final String TEL = "tel";
        public static final String EMAIL = "email";
        public static final String REALNAME = "realname";
        public static final String CATEGORY = "category";
        public static final String SIGN = "sign";
        public static final String HOBBY = "hobby";
        public static final String INCOME = "income";
    }

    public static final class FileSaveType{
        public static final String XLS = ".xls";
        public static final String CSV = ".csv";
        public static final String TXT = ".txt";
        public static final String DES_FOR_XLS ="极力推荐";
        public static final String DES_FOR_CVS ="效果一般";
        public static final String DES_FOR_TXT ="斟酌再三";
    }

    public static final class NewsFlag {
        public static final String SYN = "综合";
        public static final String DISCOUNT ="优惠";
        public static final String HOT = "热销";
    }


    public static final String FEED_MEAL = "feedMeal";
    public static final String SEED_MEAL = "seedMeal";


    public static final class UserOther{
        public static final String FARMER_NAME = "farmer_name";
        public static final String FEED_MEAL_SELECTED = "feed_meal_selected";
        public static final String SEED_MEAL_SELECTED = "seed_meal_selected";
        public static final String NEWS_PUBLISH_SORT =  "news_publish_sort"; // 新闻发布排序
        public static final String NEWS_IS_CACHE = "news_is_cache"; //新闻是否已经缓存了--
    }

    /**
     *  设置资讯的最大可添加图片
     */
    public static final int TOTAL_COUNT = 6;
    public static final int TOTAL_TALK_COUNT = 9;


    /**
     *  图片类型
     *       展示;添加
     */
    public static final class PicType{
        public static final int SHOW = 1;
        public static final int ADD_BUTTON = 2;
    }

    public static final class GALLERY_TYPE{
        public static final int EDIT = 0;
        public static final int SHOW = 1;
    }

    /**
     *  发布的资讯类型
     */
    public static final class MSG_TYPE {
        public static final String NET = "打开网址";
        public static final String PIC = "图文消息";
    }

    /**
     * 玻璃背景图片*/
    public static final String IMAGE_URL_GLIDE = "https://img3.doubanio.com/view/photo/s_ratio_poster/public/p2517753454.jpg";
    public  static String[]  SP_PRODUCT_TYPE = new String[]{"所有类型","苗","药","料","设备","其他"};
    public static String[] SP_AREA_UNIT = new String[]{"亩","平方米","公顷"};
    public static String[] SP_INPUT_UNIT = new String[]{"斤","千克"};
    public static String[] SP_POND_KIND = new String[]{"其他","鱼塘","虾塘","蟹塘"};
    public static String[] SP_SEED_UNIT = new String[]{"斤","千克","吨","尾"};

    public static String[] WEATHER_SHOW = new String[]{
            "晴","多云转晴","阴转晴","晴转阴","阴转多云","多云转阴",
            "小雪","中雪","大雪","雨夹雪","多云转小雨","阵雨转多云","多云转阵雨",
            "阴","小雪转小雨","阴转阵雨","雷阵雨转多云","浮沉",
            "雾霾","多云","雷雨","台风","小雨","中雨",
            "大雨","雷阵雨","暴雨","其他"
    };

    public static String[] ENJOY_ARRAYS = {"聚会","高科技","运动健身","购物狂",
            "理财","影视","音乐","自驾","读书","画画","DIY","游戏","涨知识",
            "旅游","汽车","居家","cosplay","其他"};

    public static String[] AGENCY_CATEGORY = {
            "饲料","药品","苗","其他","舍"
    };  // 经销商的类别



    public static String[] SILIAO_TYPE = {"丰硕达","南京彩虹","大苏公司","丰硕达","德邦"};
    public static String[] SILIAO_NAME = {"水草","饲料","肥料","改良液","冰鲜鱼"};
    public static String[] SEED_TYPE = {"常见鱼","名优鱼","虾蟹","观赏鱼","海水鱼"};
    public static String[] SEED_NAME = {"鲫鱼","","黄鳝","青虾","金鱼","龙胆"};
    public static String[] LIST_DEVICE_CH = {"PH","溶解氧","水温"};
    public static String[] LIST_DEVICE_EN = {"ph","o2","temperature"};
    public static String[] LIST_FEED_PUT_TYPE = {"虾类","鱼类","螃蟹","其他"};
    public static final int RECODE_TO_POND_LIST = 0x111; /* 这里由于塘口和天气出现在同一个界面*/
    public static final int RECODE_TO_SINGLE_ITEM_LIST = 0x112; // 单个条目的点击请求码
    public static final int RECODE_TO_SILIAO_ITEM_LIST = 0x125; // 饲料名称的点击请求码
    public static final int RECODE_TO_SEED_TYPE_ITEM_LIST = 0x125; // 种苗类型的点击请求码
    public static final int RECODE_TO_WEATHER_SINGLE_ITEM_LIST = 0x127; // 天气单个条目的点击请求码
    public static final int RECODE_TO_SILIAO_TYPE_SINGLE_ITEM_LIST = 0x128; // 饲料类型请求码
    public static final int RECODE_TO_FEED_MEAL_MANAGE = 0x129; // 转向投放套餐管理请求码
    public static final int RECODE_TO_SEED_MEAL_MANAGE = 0x130; // 转向投放套餐管理请求码
    public static final int RECODE_TO_DEVICE_ITEM_LIST = 0x126;
    public static final String POND_SELECTED =  "pond_selected"; // 被选择的塘口
    public static final String SINGLE_ITEM_SELECTED =  "single_item_selected"; // 单个条目的选择
    public static final String SINGLE_ITEM_SELECTED_ID =  "single_item_selected_id"; // 单个条目的id选择
    public static final String POND_ID_SELECTED =  "pond_id_selected"; // 单个塘口的id选择
    public static final String FLAG_WEATHER = "flag_weather";
    public static final String FLAG_AGENCY_CATEGORY = "flag_agency_category";
    public static final String FLAG_POND = "flag_pond";
    public static final String FLAG_FARMER = "flag_farm";



    public static final String FLAG_SILIAO = "flag_siliao";
    public static final String FLAG_SEED_NAME = "flag_seed_name";
    public static final String FLAG_DEVICE = "flag_device";
    public static final String FLAG_SILIAO_TYPE = "flag_siliao_type";
    public static final String FLAG_FEED_MEAL_NAME = "flag_feed_meal_name"; // 传递的套餐名
    public static final String TURN_TO_SHOW_AGENCY = "turn_to_show_agency";
    public static final String TURN_TO_SHOW_FARMER = "turn_to_show_farmer";
    public static final String INFO_SHOW_DETAIL = "info_show_detail";
    public static final String INFO_CUS_ID = "info_cus_id";
    public static final String FARM_NUM = "farm_num";


    public static final String AUDIO_SAVE_DIR = FileUtils.getDir("audio");//语音存放位置
    public static final int DEFAULT_MAX_AUDIO_RECORD_TIME_SECOND = 120;
    public static final String VIDEO_SAVE_DIR = FileUtils.getDir("ic_func_video");//视频存放位置
    public static final String PHOTO_SAVE_DIR = FileUtils.getDir("photo");    //照片存放位置
    public static final String HEADER_SAVE_DIR = FileUtils.getDir("header"); //头像保存位置
    /* intent 传递字符串 键列表*/
    public static final String AGENCY_NAME =  "agency_name"; // 经销商姓名
    public static final String FARMER_NAME =  "farmer_name"; // 经销商姓名
    public static final String AGENCY_DETAIL =  "agency_detail"; // 经销商详细信息传递
    public static final String POND_STR_ARRAY =  "pond_str_array"; // 塘口的数组
    public static final String POND_SERIALIZABLE_ARRAY = "pond_serializable_array"; // 塘口的serializable
    public static final String FM_SERIALIZABLE_ARRAY = "fm_serializable_array"; // 农户的serializable
    public static final String EXTRA_TIME = "extra_time"; // 时间
    public static final String K_SELECTED_INDEX = "k_selected_index"; // 被选择的index

    public static final String SHOW_IMAGE_URL = "show_image_url";
    public static final String KEY_INPUT_TITLE = "key_input_title"; // 打开输入框的标题
    public static final String KEY_FROM_INPUT = "key_from_input"; //   从输入框界面的返回值
    public static final String KEY_TO_INPUT_CONTENT = "key_to_input_content"; // 打开界面传递的内容
    public static final String HOBBY_KEY_ENJOY = "enjoy"; //喜好键值
    public static final String CONTENT_FORM_EMAIL = "content_form_email";
    public static final String CONTENT_FROM_PHONE = "content_from_phone";
    public static final String CONTENT_FROM_COMPANY = "content_from_company";
    public static final String CONTENT_FROM_DETAIL_ADS = "content_from_detail_ads";
    public static final String CONTENT_FROM_REAL_NAME = "content_from_real_name";
    public static final String CONTENT_FROM_SIGNATURE = "content_from_signature";
    public static final String CONTENT_FROM_INCOME = "content_from_income";
    public static final String CONTENT_FORM_FILE_LOG = "content_form_file_log";
    public static final String CONTENT_FORM_GROUP_NAME = "content_form_group_name";


    public static final String RESULT_FROM_EDIT = "result_from_edit";
    public static final String EXTRA_DOWNLOAD_LIST = "extra_download_list";
    public static final String EXTRA_DOWNLOAD_NAME  = "extra_download_name";
    public static final String EXTRA_PHOTO_LIST  = "extra_photo_list";

    /* 活动请求码开始*/
    public static final int RCODE_INPUT_FROM_AREA_DETAIL= 0x113; // 从地址详情转向输入框的请求码
    public static final int RCODE_INPUT_FROM_REMARK_DETAIL= 0x114; // 从地址详情转向输入框的请求码
    public static final int RCODE_INPUT_FROM_SYMPTOM= 0x115; // 从病害症状向输入框的请求码
    public static final int RCODE_INPUT_FROM_TYPEDEFINE= 0x116; // 从用法用量转向输入框的请求码
    public static final int RECODE_EDIT_FROM_EMAIL = 0x117;  // 从邮箱转向编辑
    public static final int RECODE_EDIT_FROM_PHONE = 0X118;  // 从电话转向编辑
    public static final int RECODE_EDIT_FROM_COMPANY = 0x119;// 从公司转向编辑
    public static final int RECODE_EDIT_FROM_DETAILADS = 0x120;// 从详细地址转向编辑
    public static final int RECODE_EDIT_FROM_REAL_NAME = 0x121;// 从真实姓名转向编辑
    public static final int RECODE_EDIT_FROM_SIGNATURE = 0x122;// 从签名转向
    public static final int RECODE_EDIT_FROM_INCOME = 0x124;// 从收入转向
    public static final int RECODE_EDIT_FROM_LOG_FILE = 0x126;// 从文件转入
    /* ox125开始*/
    /* 活动请求码结束*/

    /* 检测相关*/
    public static final String UPDATE_MGANAGE = "update_manage"; // 更新经销商管理相关
    public static final String UPDATE_POND_MG = "update_pond_mg"; // 更新塘口管理
    public static final String DETAIL_POND_INFO_LOOK = "detail_pond_info_look"; // 塘口信息
    public static final String MODIFY_POND_INFO = "modify_pond_info"; // 修改塘口跳转

    public static final String REGISTER_USER_INFO = "register_user_info"; // 注册的用户信息
    /* new资讯相关 只需要更新view即可*/
    public static final String UPDATE_NEWS = "update_news";
    public static final String UPDATE_MY_PB = "update_my_pb";
    public static final String ADD_MY_PB = "add_my_pb";

    public static final String TRANSITION_ANIMATION_NEWS_PHOTOS = "transition_animation_news_photos";
    public static final String NEWS_FROM_MY_TO_EDITOR = "news_from_my_to_editor";
    public static final String UPDATE_ME_FG_INFO = "update_me_fg_info";

    public static final String REGISTER_UPDATE_CUL = "register_update_cul";
    public static final String UPDATE_VICE_INFO = "update_vice_info";

    public static final String DEVICE_UPDATE = "device_update";
    public static final String MODIFY_DEVICE_INFO = "modify_device_info";

    public static final String DEVICE_POND_NAME = "pondName";
    public static final String DEVICE_POND_ID = "pondNameId";
    public static final String DEVICE_NO = "deviceNo";
    public static final String DEVICE_ID = "deviceId";


    public static final String UPDATE_FEED_MEAL = "update_feed_meal";
    public static final String UPDATE_SEED_MEAL = "update_seed_meal";
    public static final String UPDATE_SUBMIT_MEAL_NAME = "update_submit_meal_name";
    public static final String UPDATE_SUBMIT_SEED_MEAL_NAME = "update_submit_seed_meal_name";

    public static final String UPDATE_CUL_CUSTOM  = "update_cul_custom"; //总代理 --> 更新客户列表
    public static final String UPDATE_FM_CUSTOM  = "update_fm_custom"; //经销商 --> 更新客户列表
    public static final String UPDATE_EP_POUND  = "update_ep_pound"; // 更新养殖户的塘口
    public static final String UPDATE_EP_CUL_POUND = "update_ep_cul_pound"; // 更新养殖的塘口列表
    public static final String UPDATE_EP_DEVICE  = "update_ep_device"; // 更新养殖户的设备

    /**
     * 我曹 广播字符串同名 mmp 耽误好长时间啊
     */
    public static final String UPDATE_MG_EP_LIST  = "update_mg_ep_list"; // 更新养经销商下的养殖户数量
    public static final String UPDATE_DEVICE_SELECTED = "update_device_selected"; // 用于更新资讯模块设置指向的用户选择

    /**
     * 以下为消息相关
     * */
    public static final String EXTRA_FRIEND_INFO = "extra_friend_info";
    public static final String EXTRA_SIGNATURE = "extra_signature";
    public static final String EXTRA_AREA = "extra_area";
    public static final String EXTRA_FRIEND_NAME = "extra_friend_name";
    public static final String EXTRA_FRIEND_ID =  "extra_friend_id";
    public static final String EXTRA_FLAG_GROUP = "extra_flag_group";

    public static final int RECODE_FROM_POND_MANAGE = 0X222;
    public static final int RECODE_FROM_DEVICE_MANAGE = 0X223;
    public static final String EXTRA_SER_AG_POND_EDITOR = "extra_ser_ag_pond_editor";
    public static final String EXTRA_SER_AG_DEVICE_EDITOR = "extra_ser_ag_device_editor";
    public static final String EXTRA_VICE_INFO = "extra_vice_info";

}
