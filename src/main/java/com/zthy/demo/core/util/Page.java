package com.zthy.demo.core.util;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;


public class Page extends SimpleTagSupport implements Serializable {

    private static final long serialVersionUID = 1L;//序列号
    private Integer current;//当前页码
    private Long count;//记录总数
    private Integer pageCount;//每页记录数
    private String path;//页面链接
    private String param;//传入的参数
    private boolean notQueryCount = false;//为false在翻页时查询记录总数，默认false

    public Page() {
        this.current = 1; // 默认第一页
        this.count = 0L; // 共多少条记录
        this.pageCount = 10; // 默认每页10条记录
    }

    @Override
    public void doTag() throws JspException, IOException {
        int pageSize = (int) (this.count / this.pageCount + (this.count % this.pageCount > 0 ? 1 : 0));//共多少页
        //显示当前页和总页数
        JspWriter out = this.getJspContext().getOut();//指定输入流，用于页面输出分页信息
        StringBuffer sb = new StringBuffer();//构建StringBuffer对象，用户拼接分页标签
        sb.append("<div class=\"page\">");
        sb.append("<ul>");
        //如果当前页在第一页，则首页和上一页没有超链接
        if (this.current == 1) {
            sb.append("<li class=\"disabled\">首页</li><li class=\"disabled\">上一页</li>");
        } else {
            sb.append("<li><a href=\"");
            sb.append(this.path);
            sb.append("?current=");
            sb.append(1);
            if (this.param != null && !"".equals(this.param)) {
                sb.append("&");
                sb.append(this.param);
            }
            sb.append("\">首页</a></li>");
            sb.append("<li><a href=\"");
            sb.append(this.path);
            sb.append("?current=");
            sb.append(this.current - 1);
            if (this.param != null && !"".equals(this.param)) {
                sb.append("&");
                sb.append(this.param);
            }
            sb.append("\">上一页</a></li>");
        }
        //下面的代码显示页码，当前页在中间位置
        if (pageSize <= 10) {
            for (int i = 1; i <= pageSize; i++) {
                //如果页数小于等于10页,则全部显示
                if (i == this.current) {//如果页码等于当前页，则该页数没有超链接
                    sb.append("<li class=\"current\">");
                    sb.append(i);
                    sb.append("</li>");
                } else {//否则给出超链接
                    sb.append("<li><a href=\"");
                    sb.append(this.path);
                    sb.append("?current=");
                    sb.append(i);
                    if (this.param != null && !"".equals(this.param)) {
                        sb.append("&");
                        sb.append(this.param);
                    }
                    sb.append("\">");
                    sb.append(i);
                    sb.append("</a></li>");
                }
            }
        } else {//如果大于10页，则从当前页为中心只显示其中10页
            int index = 1;
            if (this.current > 4) {//并且如果当前页大于4页，从当前页前4页开始显示10个页数
                if (this.current + 4 >= pageSize) {//如果当前页+4 >= 总页数，最后10页全部显示出来
                    for (int j = pageSize - 9; j <= pageSize; j++) {
                        if (j == this.current) {//如果页码等于当前页，则该页数没有超链接
                            sb.append("<li class=\"current\">");
                            sb.append(j);
                            sb.append("</li>");
                        } else {//否则给定超链接
                            sb.append("<li><a href=\"");
                            sb.append(this.path);
                            sb.append("?current=");
                            sb.append(j);
                            if (this.param != null && !"".equals(this.param)) {
                                sb.append("&");
                                sb.append(this.param);
                            }
                            sb.append("\">");
                            sb.append(j);
                            sb.append("</a></li>");
                        }
                    }
                } else {
                    for (int j = this.current - 4; j <= pageSize; j++) {
                        if (j == this.current) {//如果页码等于当前页，则该页数没有超链接
                            sb.append("<li class=\"current\">");
                            sb.append(j);
                            sb.append("</li>");
                        } else {//否则给定超链接
                            sb.append("<li><a href=\"");
                            sb.append(this.path);
                            sb.append("?current=");
                            sb.append(j);
                            if (this.param != null && !"".equals(this.param)) {
                                sb.append("&");
                                sb.append(this.param);
                            }
                            sb.append("\">");
                            sb.append(j);
                            sb.append("</a></li>");
                        }
                        index++;
                        if (index > 10) {//如果循环到10次则退出循环
                            break;
                        }
                    }
                }
            } else {
                for (int i = 1; i <= pageSize; i++) {
                    //如果页数小于等于10页,则全部显示
                    if (i == this.current) {//如果页码等于当前页，则该页数没有超链接
                        sb.append("<li class=\"current\">");
                        sb.append(i);
                        sb.append("</li>");
                    } else {//否则给出超链接
                        sb.append("<li><a href=\"");
                        sb.append(this.path);
                        sb.append("?current=");
                        sb.append(i);
                        if (this.param != null && !"".equals(this.param)) {
                            sb.append("&");
                            sb.append(this.param);
                        }
                        sb.append("\">");
                        sb.append(i);
                        sb.append("</a></li>");
                    }
                    index++;
                    if (index > 10) {
                        break;
                    }
                }
            }
        }
        //如果当前页是最后一页，则末页和下一页没有超链接
        if (this.current.equals(pageSize) || this.count == 0) {
            sb.append("<li class=\"disabled\">下一页</li><li class=\"disabled\">末页</li>");
        } else {
            sb.append("<li><a href=\"");
            sb.append(this.path);
            sb.append("?current=");
            sb.append(this.current + 1);
            if (this.param != null && !"".equals(this.param)) {
                sb.append("&");
                sb.append(this.param);
            }
            sb.append("\">下一页</a></li>");
            sb.append("<li><a  href=\"");
            sb.append(this.path);
            sb.append("?current=");
            sb.append(pageSize);
            if (this.param != null && !"".equals(this.param)) {
                sb.append("&");
                sb.append(this.param);
            }
            sb.append("\">末页</a></li>");
        }
        sb.append("</ul>");
        sb.append("</div>");
        out.print(sb);
    }

    /**
     * 获取总记录条数
     *
     * @return
     */
    public Long getCount() {
        return this.count;
    }

    /**
     * 设置总记录条数
     *
     * @param count
     */
    public void setCount(Long count) {
        this.count = count;
    }

    /**
     * 获取当前第几页
     *
     * @return
     */
    public Integer getCurrent() {
        return this.current;
    }

    /**
     * 设置当前第几页
     *
     * @param current
     */
    public void setCurrent(Integer current) {
        try {
            if (current <= 0) {
                this.current = 1;
            } else {
                this.current = current;
            }
        } catch (Exception e) {
            this.current = 1;
        }
    }

    /**
     * 获取每页多少条记录
     *
     * @return
     */
    public Integer getPageCount() {
        return this.pageCount;
    }

    /**
     * 设置每页多少条记录
     *
     * @param pageCount
     */
    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    /**
     * 获取URI地址
     *
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置URI地址
     *
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取参数值
     *
     * @return
     */
    public String getParam() {
        return param;
    }

    /**
     * 设置参数值
     *
     * @param param
     */
    public void setParam(String param) {
        String[] x = param.split("&");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < x.length; i++) {
            String[] y = x[i].split("=");
            if (y.length > 1 && !"".equals(y[1].trim())) {
                sb.append(x[i]);
                sb.append("&");
            }
        }
        this.param = sb.toString().substring(0, sb.toString().lastIndexOf("&"));
    }

    /**
     * 获取总页数
     *
     * @return
     */
    public int getPages() {
        if (this.count % this.pageCount == 0) {
            return (int) (this.count / this.pageCount);
        } else {
            return (int) (this.count / this.pageCount + 1);
        }
    }

    /**
     * 是否为第一页
     *
     * @return
     */
    public boolean firstEnable() {
        return previoEnable();
    }

    /**
     * 是否为最后一页
     *
     * @return
     */
    public boolean lastEnable() {
        return nextEnable();
    }

    /**
     * 是否有下一页
     *
     * @return
     */
    public boolean nextEnable() {
        return this.current * this.pageCount < this.count;
    }

    /**
     * 是否有上一页
     *
     * @return
     */
    public boolean previoEnable() {
        return this.current > 1;
    }

    public boolean isNotQueryCount() {
        return this.notQueryCount;
    }

    public void setNotQueryCount(boolean notQueryCount) {
        this.notQueryCount = notQueryCount;
    }

    /**
     * 获取任一页第一条数据在数据集的位置.
     *
     * @param pageNo 从1开始的页号
     * @param pageSize 每页记录条数
     * @return 该页第一条数据
     */
    public int getStartOfPage(long pageNo, long pageSize) {
        return (int) ((pageNo - 1) * pageSize);
    }
}