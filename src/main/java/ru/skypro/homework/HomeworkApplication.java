package ru.skypro.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс Spring Boot-приложения «Resale Market».
 * <p>
 * Инициализирует и запускает backend-сервис доски объявлений.
 * Служит точкой входа для запуска приложения.
 */
@SpringBootApplication
public class HomeworkApplication {

  /**
   * Точка входа в приложение.
   *
   * @param args аргументы командной строки
   */
  public static void main(String[] args) {
    SpringApplication.run(HomeworkApplication.class, args);
  }
}
