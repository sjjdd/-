package com.example.demo.dao;

import javax.persistence.*;

@Table(name="article")
@Entity
public class article {
    @GeneratedValue(strategy=GenerationType.IDENTITY)//自增属性
    @Id//主键
    private Integer articleid;
    private String articletitle;
    private String articleauthor;
    private String articlecontent;

    public Integer getArticleid() {
        return articleid;
    }

    public void setArticleid(Integer articleid) {
        this.articleid = articleid;
    }

    public String getArticletitle() {
        return articletitle;
    }

    public void setArticletitle(String articletitle) {
        this.articletitle = articletitle;
    }

    public String getArticleauthor() {
        return articleauthor;
    }

    public void setArticleauthor(String articleauthor) {
        this.articleauthor = articleauthor;
    }

    public String getArticlecontent() {
        return articlecontent;
    }

    public void setArticlecontent(String articlecontent) {
        this.articlecontent = articlecontent;
    }
}
