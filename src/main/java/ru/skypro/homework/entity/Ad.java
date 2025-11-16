package ru.skypro.homework.entity;

import javax.persistence.*;
import java.util.List;

/**
 * Сущность объявления.
 * <p>
 * Модель представляет объект объявления на платформе: содержит данные
 * о заголовке, описании, цене, изображении, а также информацию об авторе
 * и связанные комментарии.
 * <p>
 * Хранится в таблице <b>ads</b>.
 */
@Entity
@Table(name = "ads")
public class Ad {

    /**
     * Уникальный идентификатор объявления.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Автор объявления.
     * <p>
     * Связь Many-to-One: одно объявление принадлежит одному пользователю.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    /**
     * Заголовок объявления.
     */
    @Column(nullable = false, length = 90)
    private String title;

    /**
     * Цена товара или услуги.
     */
    @Column(nullable = false)
    private Integer price;

    /**
     * Описание объявления.
     */
    @Column(nullable = false, length = 255)
    private String description;

    /**
     * Путь к изображению, связанному с объявлением.
     */
    @Column(nullable = false, length = 255)
    private String imageUrl;

    /**
     * Комментарии, оставленные пользователями под объявлением.
     * <p>
     * Связь One-to-Many: одно объявление может иметь много комментариев.
     */
    @OneToMany(mappedBy = "ad", fetch = FetchType.LAZY)
    private List<Comment> comments;

    /** @return идентификатор объявления */
    public Integer getId() {
        return id;
    }

    /** @return автор объявления */
    public User getAuthor() {
        return author;
    }

    /** @return заголовок объявления */
    public String getTitle() {
        return title;
    }

    /** @return цена объявления */
    public Integer getPrice() {
        return price;
    }

    /** @return описание объявления */
    public String getDescription() {
        return description;
    }

    /** @return путь к изображению */
    public String getImageUrl() {
        return imageUrl;
    }

    /** @return список комментариев к объявлению */
    public List<Comment> getComments() {
        return comments;
    }

    /** @param id идентификатор объявления */
    public void setId(Integer id) {
        this.id = id;
    }

    /** @param author автор объявления */
    public void setAuthor(User author) {
        this.author = author;
    }

    /** @param title заголовок объявления */
    public void setTitle(String title) {
        this.title = title;
    }

    /** @param price цена объявления */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /** @param description описание объявления */
    public void setDescription(String description) {
        this.description = description;
    }

    /** @param imageUrl путь к изображению */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /** @param comments список комментариев к объявлению */
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}

