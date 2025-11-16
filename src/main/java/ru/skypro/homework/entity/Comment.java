package ru.skypro.homework.entity;

import javax.persistence.*;

/**
 * Сущность комментария к объявлению.
 * <p>
 * Представляет пользовательский комментарий, содержащий текст,
 * время создания, а также ссылки на автора и объявление.
 * <p>
 * Хранится в таблице <b>comments</b>.
 */
@Entity
@Table(name = "comments")
public class Comment {

    /**
     * Уникальный идентификатор комментария.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * Объявление, к которому оставлен комментарий.
     * <p>
     * Связь Many-to-One: одно объявление может иметь множество комментариев.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id", nullable = false)
    private Ad ad;

    /**
     * Автор комментария (пользователь).
     * <p>
     * Связь Many-to-One: один пользователь может оставить много комментариев.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    /**
     * Текст комментария.
     */
    @Column(name = "text", nullable = false, length = 255)
    private String text;

    /**
     * Время создания комментария.
     * <p>
     * Хранится как количество миллисекунд с эпохи Unix (timestamp).
     */
    @Column(name = "createdAt", nullable = false)
    private Long createdAt;

    /** @return идентификатор комментария */
    public Integer getId() {
        return id;
    }

    /** @return объявление, к которому оставлен комментарий */
    public Ad getAd() {
        return ad;
    }

    /** @return автор комментария */
    public User getAuthor() {
        return author;
    }

    /** @return текст комментария */
    public String getText() {
        return text;
    }

    /** @return время создания комментария в формате timestamp */
    public Long getCreatedAt() {
        return createdAt;
    }

    /** @param id идентификатор комментария */
    public void setId(Integer id) {
        this.id = id;
    }

    /** @param ad объявление, к которому относится комментарий */
    public void setAd(Ad ad) {
        this.ad = ad;
    }

    /** @param author автор комментария */
    public void setAuthor(User author) {
        this.author = author;
    }

    /** @param text текст комментария */
    public void setText(String text) {
        this.text = text;
    }

    /** @param createdAt время создания комментария */
    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}


