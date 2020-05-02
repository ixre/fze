package net.fze.arch.commons.infrastructure;
/*
  created for mzl-server [ PagingData.java ]
  user: liuming (jarrysix@gmail.com)
  date: 12/11/2017 12:04
  description: 
 */


import java.util.List;
import java.util.Map;

/**
 * 分页结果
 */
public class PagingData {
    /**
     * 代码
     */

    private int errCode; // required
    /**
     * 消息
     */

    private String errMsg; // required
    /**
     * 总数
     */

    private int count; // required
    /**
     * 数据
     */

    private List<?> data; // required

    /**
     * 额外信息
     */

    private Map<String, String> extras;

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    public Map<String, String> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, String> extras) {
        this.extras = extras;
    }
}
