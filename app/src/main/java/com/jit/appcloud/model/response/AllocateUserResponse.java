package com.jit.appcloud.model.response;

/**
 * @author zxl on 2018/5/19.
 *         discription:分配用户返回的bean
 *         其实这里我只需要code和msg即可 其他的我不需要
 */

public class AllocateUserResponse {
    /**
     * code : 1
     * msg : 成功
     * data : {"id":16,"username":"gk","password":"$2a$10$h/OjCPAsLNHKdKiX1y.MD.4mqdMHD/7KEs8eoIwjhRFXHD0p23BF.","image":null,"realname":"程林","department":null,"province":null,"city":null,"country":null,"address":null,"tel":null,"income":null,"area":null,"category":null,"qr_code":null,"sign":null,"super_id":14,"lastPasswordResetDate":"2018-05-10 15:26:47"}
     */

    private int code;
    private String msg;

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
}
