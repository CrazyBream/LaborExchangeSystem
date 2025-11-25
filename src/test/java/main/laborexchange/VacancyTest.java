package main.laborexchange;

import org.junit.*;
import java.util.Locale;
import static org.junit.Assert.*;

public class VacancyTest {
    private Vacancy vacancy;
    private Vacancy highSalaryVacancy;
    private Vacancy invalidVacancy;

    @Before
    public void setUp() {
        vacancy = new Vacancy("V001", "Java Developer", "IT Company",
                "Java, Spring, SQL", 25000.0);

        highSalaryVacancy = new Vacancy("V002", "Senior Developer", "Tech Corp",
                "Java, Microservices", 50000.0);

        invalidVacancy = new Vacancy("", "", "", "", -1000.0);
    }

    @Test
    public void testVacancyCreation() {
        assertNotNull("Об'єкт вакансії має бути створений", vacancy);
        assertEquals("ID має співпадати", "V001", vacancy.getId());
        assertEquals("Позиція має співпадати", "Java Developer", vacancy.getPosition());
        assertEquals("Компанія має співпадати", "IT Company", vacancy.getCompany());
        assertEquals("Зарплата має співпадати", 25000.0, vacancy.getSalary(), 0.001);
        assertTrue("Спочатку активна", vacancy.isActive());
    }

    @Test
    public void testMatchesSpecialty() {
        assertTrue("Має співпадати з Java", vacancy.matchesSpecialty("Java"));
        assertTrue("Має співпадати з spring", vacancy.matchesSpecialty("spring"));
        assertTrue("Має співпадати з SQL", vacancy.matchesSpecialty("SQL"));
        assertFalse("Не має співпадати з Python", vacancy.matchesSpecialty("Python"));
        assertFalse("Не має співпадати з null", vacancy.matchesSpecialty(null));
    }

    @Test
    public void testVacancyStatus() {
        assertTrue("Спочатку активна", vacancy.isActive());
        vacancy.setActive(false);
        assertFalse("Після деактивації - неактивна", vacancy.isActive());
    }

    @Test
    public void testIsHighSalary() {
        assertFalse("Зарплата 25000 не вважається високою", vacancy.isHighSalary());
        assertTrue("Зарплата 50000 вважається високою", highSalaryVacancy.isHighSalary());
    }

    @Test
    public void testIsValid() {
        assertTrue("Валідна вакансія", vacancy.isValid());
        assertFalse("Невалідна вакансія", invalidVacancy.isValid());

        Vacancy zeroSalary = new Vacancy("V003", "Position", "Company", "Req", 0);
        assertFalse("Нульова зарплата - невалідно", zeroSalary.isValid());

        Vacancy nullId = new Vacancy(null, "Position", "Company", "Req", 1000);
        assertFalse("Null ID - невалідно", nullId.isValid());
    }

    @Test
    public void testGetDisplayInfo() {
        String displayInfo = vacancy.getDisplayInfo();
        // Використовуємо contains замість точного співпадіння через різні локалі
        assertTrue("Інформація для відображення містить позицію", displayInfo.contains("Java Developer"));
        assertTrue("Інформація для відображення містить компанію", displayInfo.contains("IT Company"));
        assertTrue("Інформація для відображення містить зарплату", displayInfo.contains("25000"));
        assertTrue("Інформація для відображення містить UAH", displayInfo.contains("UAH"));
    }

    @Test
    public void testEqualsAndHashCode() {
        Vacancy sameId = new Vacancy("V001", "Інша позиція", "Інша компанія",
                "Інші вимоги", 30000.0);

        assertEquals("Об'єкти з однаковим ID мають бути рівні", vacancy, sameId);
        assertEquals("Хеш-коди мають співпадати", vacancy.hashCode(), sameId.hashCode());

        Vacancy differentId = new Vacancy("V999", "Java Developer", "IT Company",
                "Java, Spring, SQL", 25000.0);

        assertNotEquals("Об'єкти з різними ID не рівні", vacancy, differentId);
    }

    @Test
    public void testNullSafety() {
        assertFalse("Null спеціальність", vacancy.matchesSpecialty(null));

        Vacancy vacancyWithNulls = new Vacancy(null, null, null, null, 0);
        assertFalse("Вакансія з null полями невалідна", vacancyWithNulls.isValid());
    }

    @Test
    public void testToString() {
        String toString = vacancy.toString();
        assertNotNull("toString не повертає null", toString);
        assertTrue("toString містить позицію", toString.contains("Java Developer"));
        assertTrue("toString містить компанію", toString.contains("IT Company"));
        assertTrue("toString містить зарплату", toString.contains("25000"));
        assertTrue("toString містить статус", toString.contains("active=true"));
    }

    @Test
    public void testSalaryFormatting() {
        // Тест форматування зарплати незалежно від локалі
        String displayInfo = vacancy.getDisplayInfo();
        String salaryPart = displayInfo.replace("Java Developer at IT Company - ", "").replace(" UAH", "");

        // Перевіряємо, що зарплата містить числа
        assertTrue("Зарплата містить 25000", salaryPart.contains("25000"));
        assertTrue("Зарплата містить два десяткових знаки", salaryPart.length() >= "25000.00".length());
    }
}