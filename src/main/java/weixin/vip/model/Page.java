package weixin.vip.model;

/**
 * Created by Administrator on 2015/9/23.
 */

import java.util.List;

public class Page {

    /**
     * 当前页码（已知）
     */
    int pageNo = 0;
    /**
     * 每页记录数（已知）
     */
    int pageSize = 12;
    /**
     * 总记录数（已知）
     */
    int totalCount = 0;
    /**
     * 总页数（未知）
     */
    int totalPage = 0;
    /**
     * 开始行号（未知）
     */
    int startNum = 0;

    /**
     * 结果集
     */
    List<?> list;
    public int getPageNo() {
        return pageNo;
    }
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    public int getTotalPage() {
        int pages = totalCount / pageSize;
        if(totalCount % pageSize != 0){
            return ++pages;
        }else{
            if(pages == 0){
                pages++;
            }
            return pages;
        }

    }
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getStartNum() {
        int s = (pageNo - 1)* pageSize;
        return (pageNo - 1)* pageSize ;
    }
    public void setStartNum(int startNum) {
        this.startNum = startNum;
    }

    public List<?> getList() {
        return list;
    }
    public void setList(List<?> list) {
        this.list = list;
    }


}
