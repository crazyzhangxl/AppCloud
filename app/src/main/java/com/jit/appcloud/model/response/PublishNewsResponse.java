package com.jit.appcloud.model.response;

import com.jit.appcloud.db.db_model.MyPbNewsBean;

/**
 * @author zxl on 2018/7/3.
 *         discription:
 */

public class PublishNewsResponse {

    /**
     * code : 1
     * msg : 成功
     * data : {"id":20,"title":"花鲢","price":143,"image":"http://223.2.197.240:8888/yunlian/product/7f3e5fb8-2a03-47cb-bcab-ca5f00cabe9a.png","time":"2018-06-27 14:33:16","username":"总部w","type":"药","msg_type":"图文消息","content":"http://223.2.197.240:8888/yunlian/product/a9458359-eb38-4275-9fa6-9a4832679168.jpg","description":"适合花鲢养殖的药","hot":1,"discount":1,"summary":"药效好","position":8,"isTop":0}
     */

    private int code;
    private String msg;
    private MyPbNewsBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MyPbNewsBean getData() {
        return data;
    }

    public void setData(MyPbNewsBean data) {
        this.data = data;
    }

}
