package com.jit.appcloud.model.response;

import com.jit.appcloud.db.db_model.MyPbNewsBean;

import java.util.List;

/**
 * @author zxl on 2018/5/25.
 *         discription:全部的新闻资讯返回
 */
public class NewsAllInfoResponse {

    /**
     * code : 1
     * msg : 成功
     * data : [{"id":15,"title":"龙虾","price":253,"image":"http://223.2.197.240:8888/yunlian/product/dd57303c-1dd4-40b8-a700-950fb659291e.jpg","time":"2018-06-27 15:36:14","username":"经销商1","type":"其他","msg_type":"打开网址","content":null,"description":"http://www.yzniuyang01.com/","hot":0,"discount":1,"summary":"鲤鱼跃龙门","position":4,"isTop":1},{"id":17,"title":"龙虾","price":143,"image":"http://223.2.197.240:8888/yunlian/product/9b5acec7-ff39-425a-a19c-dd1605d60888.jpg","time":"2018-06-28 10:13:57","username":"总部w","type":"料","msg_type":"图文消息","content":"http://223.2.197.240:8888/yunlian/product/74e72aef-007a-4a7b-879a-4037a8c677e1.jpg","description":"美味龙虾","hot":0,"discount":1,"summary":"龙虾的养殖","position":8,"isTop":1},{"id":11,"title":"龙虾","price":253,"image":"http://223.2.197.240:8888/yunlian/product/2bf4475c-46bb-404d-807a-cd3ecbcaf436.jpg","time":"2018-06-27 16:29:00","username":"总部w","type":"苗","msg_type":"打开网址","content":null,"description":"http://www.yzniuyang01.com/","hot":0,"discount":0,"summary":"鲤鱼跃龙门","position":7,"isTop":1},{"id":4,"title":"鲤鱼","price":25.3,"image":"http://223.2.197.240:8888/yunlian/product/c4d78fb3-adf8-4d65-a4e4-bf8417c74632.jpg","time":"2018-06-28 10:03:47","username":"总部w","type":"药","msg_type":"图文消息","content":"http://223.2.197.240:8888/yunlian/product/e22bebca-4f3b-4d61-887c-f5b67dd9b283.png, http://223.2.197.240:8888/yunlian/product/0ab5e60d-1d47-4429-a593-00e4a6d95d43.jpg","description":"哈哈哈","hot":0,"discount":1,"summary":"鲤鱼跃龙门","position":6,"isTop":1},{"id":16,"title":"龙虾","price":143,"image":"http://223.2.197.240:8888/yunlian/product/c65f5c49-d4b6-4256-997a-1132fe2c50a3.jpg","time":"2018-06-28 10:09:29","username":"总部w","type":"料","msg_type":"图文消息","content":"http://223.2.197.240:8888/yunlian/product/6f37382c-efaf-4bea-b350-e88c48288437.jpg","description":"美味龙虾","hot":0,"discount":1,"summary":"龙虾的养殖","position":4,"isTop":1},{"id":14,"title":"龙虾","price":253,"image":"http://223.2.197.240:8888/yunlian/product/b0c75d01-4d5e-426b-b5f2-634c789a99ef.jpg","time":"2018-06-25 15:35:29","username":"经销商1","type":"苗","msg_type":"打开网址","content":null,"description":"http://www.yzniuyang01.com/","hot":1,"discount":0,"summary":"鲤鱼跃龙门","position":3,"isTop":0},{"id":9,"title":"龙虾","price":253,"image":"http://223.2.197.240:8888/yunlian/product/f19152d5-61fa-4b4d-88f2-e092f41f14ee.jpg","time":"2018-06-25 13:40:10","username":"经销商1","type":"苗","msg_type":"打开网址","content":null,"description":"http://www.yzniuyang01.com/","hot":1,"discount":0,"summary":"龙虾养殖技术","position":2,"isTop":0},{"id":5,"title":"鲤鱼","price":25.3,"image":"http://223.2.197.240:8888/yunlian/product/416ebd74-8674-41eb-a5db-43c96ffe2526.jpg","time":"2018-06-21 17:01:39","username":"经销商1","type":"苗","msg_type":"图文消息","content":"http://223.2.197.240:8888/yunlian/product/4f760b29-25f6-4e72-a00a-a1f111677a59.png, http://223.2.197.240:8888/yunlian/product/b6de19fe-c6ac-4d3b-b3f4-491c627c0f01.jpg","description":"哈哈哈","hot":0,"discount":0,"summary":"鲤鱼跃龙门","position":1,"isTop":0},{"id":20,"title":"花鲢","price":143,"image":"http://223.2.197.240/yunlian/product/2a0556a5-c6ca-411e-83cc-b10c07114ecb.png","time":"2018-06-27 16:30:00","username":"总部w","type":"药","msg_type":"图文消息","content":"","description":"http://www.baidu.com","hot":0,"discount":1,"summary":"药效好","position":5,"isTop":0},{"id":1,"title":"鲤鱼","price":25.3,"image":"http://223.2.197.240/yunlian/product/27528b68-bf98-4788-90eb-a9c08c669b60.jpg","time":"2018-06-28 10:12:03","username":"总部w","type":"苗","msg_type":"图文消息","content":"http://223.2.197.240/yunlian/product/9f8df795-2fee-4178-b7c3-27b3e2154dc7.png, http://223.2.197.240/yunlian/product/cf2bf836-c5da-4814-9303-d0f444db9200.jpg","description":"哈哈哈","hot":1,"discount":0,"summary":"鲤鱼跃龙门","position":3,"isTop":0},{"id":19,"title":"花鲢","price":143,"image":"http://223.2.197.240:8888/yunlian/product/null","time":"2018-06-28 10:09:29","username":"总部w","type":"药","msg_type":"图文消息","content":"http://223.2.197.240:8888/yunlian/product/0cd84a71-f04e-4e75-8520-c44c99917dd6.jpg","description":"适合花鲢养殖的药","hot":0,"discount":0,"summary":"药效好","position":2,"isTop":0},{"id":2,"title":"鲤鱼","price":25.3,"image":"http://223.2.197.240:8888/yunlian/product/a2bfe347-4d32-4761-91eb-5d9fbb72b7a8.jpg","time":"2018-06-28 10:12:03","username":"总部w","type":"料","msg_type":"图文消息","content":"http://223.2.197.240:8888/yunlian/product/b3850a97-d2a9-4aa5-9f25-a3368912fc46.png, http://223.2.197.240:8888/yunlian/product/ecaba768-548c-4f3f-8a57-436f42d21bc4.jpg","description":"哈哈哈","hot":0,"discount":1,"summary":"鲤鱼跃龙门","position":1,"isTop":0},{"id":7,"title":"螃蟹","price":210,"image":"http://223.2.197.240:8888/yunlian/product/416ebd74-8674-41eb-a5db-43c96ffe2526.jpg","time":"2018-06-25 13:34:10","username":"总部2","type":"苗","msg_type":"图文消息","content":"http://223.2.197.240:8888/yunlian/product/4f760b29-25f6-4e72-a00a-a1f111677a59.png, http://223.2.197.240:8888/yunlian/product/b6de19fe-c6ac-4d3b-b3f4-491c627c0f01.jpg","description":"螃蟹苗的种类","hot":0,"discount":0,"summary":"螃蟹","position":1,"isTop":0},{"id":8,"title":"螃蟹","price":158,"image":"http://223.2.197.240:8888/yunlian/product/416ebd74-8674-41eb-a5db-43c96ffe2526.jpg","time":"2018-06-25 13:34:37","username":"总部1","type":"药","msg_type":"打开网址","content":null,"description":"http://www.baidu.com","hot":0,"discount":0,"summary":"螃蟹","position":1,"isTop":0},{"id":10,"title":"龙虾","price":253,"image":"http://223.2.197.240:8888/yunlian/product/ffca2301-a0ec-4649-9e66-27c06e85ebe6.jpg","time":"2018-06-25 13:41:28","username":"总部","type":"苗","msg_type":"打开网址","content":null,"description":"http://www.yzniuyang01.com/","hot":0,"discount":1,"summary":"鲤鱼跃龙门","position":1,"isTop":0}]
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
