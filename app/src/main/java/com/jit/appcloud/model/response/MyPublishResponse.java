package com.jit.appcloud.model.response;

import com.jit.appcloud.db.db_model.MyPbNewsBean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * @author zxl on 2018/5/25.
 *         discription:我的发布返回的Bean数据
 */

public class MyPublishResponse {

    /**
     * code : 1
     * msg : 成功
     * data : {"pageNum":1,"pageSize":10,"total":8,"pages":1,"list":[{"id":11,"title":"龙虾","price":253,"image":"http://223.2.197.240:8888/yunlian/product/2bf4475c-46bb-404d-807a-cd3ecbcaf436.jpg","time":"2018-06-26 17:04:40","username":"总部w","type":"苗","msg_type":"打开网址","content":null,"description":"http://www.yzniuyang01.com/","hot":0,"discount":0,"summary":"鲤鱼跃龙门","position":3,"isTop":1},{"id":16,"title":"龙虾","price":143,"image":"http://223.2.197.240:8888/yunlian/product/c65f5c49-d4b6-4256-997a-1132fe2c50a3.jpg","time":"2018-06-26 16:25:46","username":"总部w","type":"料","msg_type":"图文消息","content":"http://223.2.197.240:8888/yunlian/product/6f37382c-efaf-4bea-b350-e88c48288437.jpg","description":"美味龙虾","hot":0,"discount":1,"summary":"龙虾的养殖","position":1,"isTop":1},{"id":20,"title":"花鲢","price":143,"image":"http://223.2.197.240:8888/yunlian/product/7f3e5fb8-2a03-47cb-bcab-ca5f00cabe9a.png","time":"2018-06-27 14:33:17","username":"总部w","type":"药","msg_type":"图文消息","content":"http://223.2.197.240:8888/yunlian/product/a9458359-eb38-4275-9fa6-9a4832679168.jpg","description":"适合花鲢养殖的药","hot":1,"discount":1,"summary":"药效好","position":8,"isTop":0},{"id":19,"title":"花鲢","price":143,"image":"http://223.2.197.240:8888/yunlian/product/null","time":"2018-06-26 16:23:47","username":"总部w","type":"药","msg_type":"图文消息","content":"http://223.2.197.240:8888/yunlian/product/0cd84a71-f04e-4e75-8520-c44c99917dd6.jpg","description":"适合花鲢养殖的药","hot":0,"discount":0,"summary":"药效好","position":7,"isTop":0},{"id":2,"title":"鲤鱼","price":25.3,"image":"http://223.2.197.240:8888/yunlian/product/a2bfe347-4d32-4761-91eb-5d9fbb72b7a8.jpg","time":"2018-06-26 14:40:10","username":"总部w","type":"料","msg_type":"图文消息","content":"http://223.2.197.240:8888/yunlian/product/b3850a97-d2a9-4aa5-9f25-a3368912fc46.png, http://223.2.197.240:8888/yunlian/product/ecaba768-548c-4f3f-8a57-436f42d21bc4.jpg","description":"哈哈哈","hot":0,"discount":1,"summary":"鲤鱼跃龙门","position":6,"isTop":0},{"id":4,"title":"鲤鱼","price":25.3,"image":"http://223.2.197.240:8888/yunlian/product/c4d78fb3-adf8-4d65-a4e4-bf8417c74632.jpg","time":"2018-06-21 17:01:38","username":"总部w","type":"药","msg_type":"图文消息","content":"http://223.2.197.240:8888/yunlian/product/e22bebca-4f3b-4d61-887c-f5b67dd9b283.png, http://223.2.197.240:8888/yunlian/product/0ab5e60d-1d47-4429-a593-00e4a6d95d43.jpg","description":"哈哈哈","hot":0,"discount":1,"summary":"鲤鱼跃龙门","position":5,"isTop":0},{"id":17,"title":"龙虾","price":143,"image":"http://223.2.197.240:8888/yunlian/product/9b5acec7-ff39-425a-a19c-dd1605d60888.jpg","time":"2018-06-26 14:44:08","username":"总部w","type":"料","msg_type":"图文消息","content":"http://223.2.197.240:8888/yunlian/product/74e72aef-007a-4a7b-879a-4037a8c677e1.jpg","description":"美味龙虾","hot":0,"discount":1,"summary":"龙虾的养殖","position":4,"isTop":0},{"id":1,"title":"鲤鱼","price":25.3,"image":"http://223.2.197.240/yunlian/product/27528b68-bf98-4788-90eb-a9c08c669b60.jpg","time":"2018-06-26 14:29:54","username":"总部w","type":"苗","msg_type":"图文消息","content":"http://223.2.197.240/yunlian/product/9f8df795-2fee-4178-b7c3-27b3e2154dc7.png, http://223.2.197.240/yunlian/product/cf2bf836-c5da-4814-9303-d0f444db9200.jpg","description":"哈哈哈","hot":1,"discount":0,"summary":"鲤鱼跃龙门","position":2,"isTop":0}]}
     */

    private int code;
    private String msg;
    private List<MyPbNewsBean> data;

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

    public List<MyPbNewsBean> getData() {
        return data;
    }

    public void setData(List<MyPbNewsBean> data) {
        this.data = data;
    }
}
