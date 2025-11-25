package main.laborexchange;

import org.junit.*;
import static org.junit.Assert.*;

public class UnemployedTest {
    private Unemployed unemployed;
    private Unemployed invalidUnemployed;

    @BeforeClass
    public static void setUpBeforeClass() {
        System.out.println("=== Початок тестування класу Unemployed ===");
    }

    @AfterClass
    public static void tearDownAfterClass() {
        System.out.println("=== Завершення тестування класу Unemployed ===");
    }

    @Before
    public void setUp() {
        unemployed = new Unemployed("Іваненко Петро Сидорович",
                "Київ, вул. Хрещатик, 1",
                "+380501234567",
                "Java Developer", 30,
                "petro@example.com");

        invalidUnemployed = new Unemployed("", "", "", "", 15, "invalid-email");
    }

    @After
    public void tearDown() {
        unemployed = null;
        invalidUnemployed = null;
    }

    @Test
    public void testUnemployedCreation() {
        assertNotNull("Об'єкт безробітного має бути створений", unemployed);
        assertEquals("ПІБ має співпадати", "Іваненко Петро Сидорович", unemployed.getFullName());
        assertEquals("Спеціальність має співпадати", "Java Developer", unemployed.getSpecialty());
        assertEquals("Вік має співпадати", 30, unemployed.getAge());
        assertEquals("Email має співпадати", "petro@example.com", unemployed.getEmail());
    }

    @Test
    public void testIsValid() {
        assertTrue("Валідний безробітний", unemployed.isValid());
        assertFalse("Невалідний безробітний", invalidUnemployed.isValid());
    }

    @Test
    public void testEmploymentStatus() {
        assertFalse("Спочатку не працевлаштований", unemployed.isEmployed());
        unemployed.setEmployed(true);
        assertTrue("Після встановлення статусу - працевлаштований", unemployed.isEmployed());
    }

    @Test
    public void testCanRetire() {
        Unemployed young = new Unemployed("Молодий", "Адреса", "Тел", "Спец", 25, "email@test.com");
        Unemployed old = new Unemployed("Літній", "Адреса", "Тел", "Спец", 65, "email@test.com");

        assertFalse("Молода людина не може на пенсію", young.canRetire());
        assertTrue("Літня людина може на пенсію", old.canRetire());
    }

    @Test
    public void testGetInitials() {
        assertEquals("Ініціали мають співпадати", "І.П.Сидорович", unemployed.getInitials());

        Unemployed shortName = new Unemployed("Петро", "Адреса", "Тел", "Спец", 25, "test@test.com");
        assertEquals("Коротке ім'я", "Петро", shortName.getInitials());
    }

    @Test
    public void testEqualsAndHashCode() {
        Unemployed sameEmail = new Unemployed("Інше Ім'я", "Інша адреса", "Інший телефон",
                "Інша спеціальність", 35, "petro@example.com");

        assertEquals("Об'єкти з однаковим email мають бути рівні", unemployed, sameEmail);
        assertEquals("Хеш-коди мають співпадати", unemployed.hashCode(), sameEmail.hashCode());

        Unemployed differentEmail = new Unemployed("Іваненко Петро Сидорович",
                "Київ, вул. Хрещатик, 1",
                "+380501234567",
                "Java Developer", 30,
                "other@example.com");

        assertNotEquals("Об'єкти з різними email не рівні", unemployed, differentEmail);
    }

    @Test
    public void testToString() {
        String toString = unemployed.toString();
        assertNotNull("toString не повертає null", toString);
        assertTrue("toString містить ім'я", toString.contains("Іваненко"));
        assertTrue("toString містить спеціальність", toString.contains("Java Developer"));
        assertTrue("toString містить статус", toString.contains("employed=false"));
    }

    @Test
    public void testBoundaryAge() {
        Unemployed minAge = new Unemployed("Тест", "Адреса", "Тел", "Спец", 16, "test@test.com");
        Unemployed maxAge = new Unemployed("Тест", "Адреса", "Тел", "Спец", 70, "test@test.com");
        Unemployed tooYoung = new Unemployed("Тест", "Адреса", "Тел", "Спец", 15, "test@test.com");
        Unemployed tooOld = new Unemployed("Тест", "Адреса", "Тел", "Спец", 71, "test@test.com");

        assertTrue("Мінімальний вік - валідний", minAge.isValid());
        assertTrue("Максимальний вік - валідний", maxAge.isValid());
        assertFalse("Замалий вік - невалідний", tooYoung.isValid());
        assertFalse("Завеликий вік - невалідний", tooOld.isValid());
    }
}