    package ru.skypro.homework;

    import org.junit.jupiter.api.Test;
    import org.springframework.boot.test.context.SpringBootTest;

    @SpringBootTest(properties = "logging.level.org.springframework=TRACE")
    class HomeworkApplicationTests {

        @Test
        void contextLoads() {
            System.out.println("Контекст Spring успешно загружен!");
        }

    }
