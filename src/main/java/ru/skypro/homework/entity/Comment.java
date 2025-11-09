package ru.skypro.homework.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id", nullable = false)
    private Ad ad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(name = "text", nullable = false, length = 255)
    private String text;

    @Column(name = "created_at", nullable = false)
    private Long createdAtEpochMillis;

    public Integer getId() {
        return id;
    }

    public Ad getAd() {
        return ad;
    }

    public User getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public Long getCreatedAtEpochMillis() {
        return createdAtEpochMillis;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreatedAtEpochMillis(Long createdAtEpochMillis) {
        this.createdAtEpochMillis = createdAtEpochMillis;
    }
}


