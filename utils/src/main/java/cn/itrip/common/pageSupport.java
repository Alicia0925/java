
package cn.itrip.common;

import java.util.List;

public class pageSupport<T> {
    private Integer pageSize=Constants.PAGE_SIZE;
    private Integer totalCount;
    private Integer totalPage;
    private Integer currentPage=Constants.CURRENT_PAGE;
    private List<T> list;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
        this.totalPage=this.totalCount%this.pageSize==0?(this.totalCount/this.pageSize):(this.totalCount/this.pageSize+1);
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {

        this.currentPage = currentPage<1 ? Constants.CURRENT_PAGE:currentPage;
        this.currentPage = currentPage>this.totalPage ? totalPage:currentPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
