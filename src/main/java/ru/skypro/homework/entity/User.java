package ru.skypro.homework.entity;

import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import java.util.List;

/**
 * Сущность пользователя системы.
 * <p>
 * Пользователь может создавать объявления, оставлять комментарии,
 * иметь аватар, роль (USER/ADMIN) и авторизационные данные.
 * <p>
 * Хранится в таблице <b>users</b>.
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Email пользователя.
     * <p>
     * Используется как логин для авторизации.
     */
    @Column(nullable = false, unique = true, length = 30)
    private String email;

    /**
     * Имя пользователя.
     */
    @Column(nullable = false, length = 30)
    private String firstName;

    /**
     * Фамилия пользователя.
     */
    @Column(nullable = false, length = 30)
    private String lastName;

    /**
     * Номер телефона пользователя.
     */
    @Column(nullable = false, length = 20)
    private String phone;

    /**
     * Роль пользователя: USER или ADMIN.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Role role;

    /**
     * Путь к изображению профиля (аватар).
     */
    @Column(length = 255)
    private String image;

    /**
     * Зашифрованный пароль пользователя.
     */
    @Column(length = 255)
    private String password;

    /**
     * Флаг активности пользователя.
     * <p>
     * Может использоваться для блокировки/разблокировки аккаунтов.
     */
    @Column(name = "enabled")
    private Boolean enabled = true;

    /**
     * Объявления, созданные пользователем.
     * <p>
     * Связь One-to-Many: один пользователь может иметь множество объявлений.
     */
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Ad> ads;

    /**
     * Комментарии, оставленные пользователем.
     */
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Comment> comments;

    /**
     * Конструктор без параметров.
     * Требуется JPA.
     */
    public User() {
    }

    /**
     * Конструктор для создания экземпляра пользователя вручную.
     *
     * @param email     email пользователя
     * @param firstName имя
     * @param lastName  фамилия
     * @param phone     телефон
     * @param role      роль пользователя
     * @param image     путь к изображению
     * @param password  зашифрованный пароль
     * @param enabled   флаг активности
     */
    public User(String email, String firstName, String lastName, String phone,
                Role role, String image, String password, Boolean enabled) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
        this.image = image;
        this.password = password;
        this.enabled = enabled;
    }

    /** @return идентификатор пользователя */
    public Integer getId() {
        return id;
    }

    /** @return email пользователя */
    public String getEmail() {
        return email;
    }

    /** @return имя пользователя */
    public String getFirstName() {
        return firstName;
    }

    /** @return фамилия пользователя */
    public String getLastName() {
        return lastName;
    }

    /** @return телефон пользователя */
    public String getPhone() {
        return phone;
    }

    /** @return роль пользователя */
    public Role getRole() {
        return role;
    }

    /** @return путь к аватару пользователя */
    public String getImage() {
        return image;
    }

    /** @return зашифрованный пароль */
    public String getPassword() {
        return password;
    }

    /** @return список объявлений пользователя */
    public List<Ad> getAds() {
        return ads;
    }

    /** @return список комментариев пользователя */
    public List<Comment> getComments() {
        return comments;
    }

    /** @return активен ли пользователь */
    public Boolean getEnabled() {
        return enabled;
    }

    /** @param id идентификатор пользователя */
    public void setId(Integer id) {
        this.id = id;
    }

    /** @param email email пользователя */
    public void setEmail(String email) {
        this.email = email;
    }

    /** @param firstName имя пользователя */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /** @param lastName фамилия пользователя */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /** @param phone телефон пользователя */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /** @param role роль пользователя */
    public void setRole(Role role) {
        this.role = role;
    }

    /** @param image путь к изображению профиля */
    public void setImage(String image) {
        this.image = image;
    }

    /** @param password зашифрованный пароль пользователя */
    public void setPassword(String password) {
        this.password = password;
    }

    /** @param ads список объявлений пользователя */
    public void setAds(List<Ad> ads) {
        this.ads = ads;
    }

    /** @param comments список комментариев пользователя */
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    /** @param enabled флаг активности пользователя */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}

