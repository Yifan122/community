package com.nowcoder.community.entity;

/**
 * 封装分页相关的信息
 */
public class Page {

    private int current = 1;
    private int limit = 10;
    // total pages
    private int rows;
    // 查询路径(用于分页的链接）
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if(limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if(rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页的起始行
     * @return
     */
    public int getOffset(){
        return (current - 1) * limit;
    }

    /**
     * 获取总页数
     * @return
     */
    public int getTotal(){
        if(rows % limit == 0){
            return rows / limit;
        }else{
            return rows/limit +1;
        }
    }

    /**
     *
     * @return
     */
    public int getFrom(){
       return current <=3 ? 1 : current - 2;
    }

    public int getTo(){
        int to = getFrom()+5;
        int total = getTotal();
        return to<=total ? to:total;
    }
}
