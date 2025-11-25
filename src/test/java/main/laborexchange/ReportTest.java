package main.laborexchange;

import org.junit.*;
import java.util.Date;
import static org.junit.Assert.*;

public class ReportTest {
    private Report report;
    private Unemployed employed;
    private Vacancy closedVacancy;

    @Before
    public void setUp() {
        report = new Report("R001");
        employed = new Unemployed("Працевлаштований", "Адреса", "Тел",
                "Спеціальність", 30, "employed@test.com");
        employed.setEmployed(true);

        closedVacancy = new Vacancy("V001", "Позиція", "Компанія", "Вимоги", 25000.0);
        closedVacancy.setActive(false);
    }

    @Test
    public void testReportCreation() {
        assertNotNull("Звіт має бути створений", report);
        assertEquals("ID звіту", "R001", report.getReportId());
        assertNotNull("Дата звіту", report.getReportDate());

        // Більш надійна перевірка дати - переконуємося, що дата не в майбутньому
        assertFalse("Дата звіту не має бути в майбутньому",
                report.getReportDate().after(new Date()));

        assertTrue("Список працевлаштованих спочатку порожній", report.getEmployedList().isEmpty());
        assertTrue("Список вакансій спочатку порожній", report.getClosedVacancies().isEmpty());
        assertEquals("Початкова кількість оброблених", 0, report.getTotalProcessed());
        assertEquals("Початковий успіх", 0.0, report.getSuccessRate(), 0.001);
    }

    @Test
    public void testAddEmployed() {
        report.addEmployed(employed);
        assertEquals("Кількість працевлаштованих", 1, report.getEmployedList().size());
        assertEquals("Загальна кількість оброблених", 1, report.getTotalProcessed());
        assertEquals("Успішність 100%", 100.0, report.getSuccessRate(), 0.1);

        // Дублювання не додається
        report.addEmployed(employed);
        assertEquals("Не додає дублікати", 1, report.getEmployedList().size());
    }

    @Test
    public void testAddClosedVacancy() {
        report.addClosedVacancy(closedVacancy);
        assertEquals("Кількість закритих вакансій", 1, report.getClosedVacancies().size());
        assertEquals("Загальна кількість оброблених", 1, report.getTotalProcessed());
        assertEquals("Успішність 0%", 0.0, report.getSuccessRate(), 0.1);
    }

    @Test
    public void testSuccessRateCalculation() {
        // Тільки працевлаштовані - 100% успішність
        report.addEmployed(employed);
        assertEquals("Успішність 100%", 100.0, report.getSuccessRate(), 0.1);

        // Додаємо закриту вакансію - 50% успішність
        report.addClosedVacancy(closedVacancy);
        assertEquals("Загальна кількість оброблених", 2, report.getTotalProcessed());
        assertEquals("Успішність 50%", 50.0, report.getSuccessRate(), 0.1);

        // Ще один працевлаштований - 66.7% успішність
        Unemployed employed2 = new Unemployed("Другий", "Адреса", "Тел", "Спец", 25, "test2@test.com");
        employed2.setEmployed(true);
        report.addEmployed(employed2);
        assertEquals("Успішність 66.7%", 66.7, report.getSuccessRate(), 0.1);
    }

    @Test
    public void testIsValid() {
        assertTrue("Валідний звіт", report.isValid());

        Report invalidReport = new Report("");
        assertFalse("Невалідний звіт з пустим ID", invalidReport.isValid());

        Report nullReport = new Report(null);
        assertFalse("Невалідний звіт з null ID", nullReport.isValid());
    }

    @Test
    public void testGenerateSummary() {
        report.addEmployed(employed);
        String summary = report.generateSummary();

        assertNotNull("Згенерований зведення не null", summary);
        assertTrue("Зведення містить ID звіту", summary.contains("R001"));
        assertTrue("Зведення містить кількість працевлаштованих", summary.contains("1 employed"));
        assertTrue("Зведення містить успішність", summary.contains("Success rate"));
    }

    @Test
    public void testGenerateDetailedReport() {
        report.addEmployed(employed);
        report.addClosedVacancy(closedVacancy);

        String detailedReport = report.generateDetailedReport();
        assertNotNull("Детальний звіт не null", detailedReport);
        assertTrue("Містить заголовок", detailedReport.contains("Labor Exchange Report"));
        assertTrue("Містить ID", detailedReport.contains("R001"));
        assertTrue("Містить працевлаштованих", detailedReport.contains("Employed: 1"));
        assertTrue("Містить список працевлаштованих", detailedReport.contains("Employed Persons"));
        assertTrue("Містить ім'я працевлаштованого", detailedReport.contains("Працевлаштований"));
    }

    @Test
    public void testNullSafety() {
        report.addEmployed(null);
        report.addClosedVacancy(null);

        assertEquals("Не додає null працевлаштованих", 0, report.getEmployedList().size());
        assertEquals("Не додає null вакансій", 0, report.getClosedVacancies().size());
    }

    @Test
    public void testOnlyEmployedCanBeAdded() {
        Unemployed notEmployed = new Unemployed("Не працевлаштований", "Адреса", "Тел", "Спец", 25, "test@test.com");
        report.addEmployed(notEmployed);

        assertEquals("Не додає не працевлаштованих", 0, report.getEmployedList().size());
    }

    @Test
    public void testOnlyClosedVacanciesCanBeAdded() {
        Vacancy activeVacancy = new Vacancy("V002", "Активна", "Компанія", "Вимоги", 30000.0);
        report.addClosedVacancy(activeVacancy);

        assertEquals("Не додає активних вакансій", 0, report.getClosedVacancies().size());
    }

    @Test
    public void testToString() {
        String toString = report.toString();
        assertNotNull("toString не повертає null", toString);
        assertTrue("toString містить ID", toString.contains("R001"));
        assertTrue("toString містить кількість працевлаштованих", toString.contains("employed=0"));
    }

    @Test
    public void testReportDateIsReasonable() {
        // Перевіряємо, що дата звіту є розумною (не дуже давно в минулому і не в майбутньому)
        Date reportDate = report.getReportDate();
        Date now = new Date();

        // Звіт не повинен бути старішим ніж 1 хвилина (на випадок затримок)
        long maxAge = 60 * 1000; // 1 хвилина в мілісекундах
        assertTrue("Дата звіту не занадто стара",
                now.getTime() - reportDate.getTime() <= maxAge);

        // Звіт не повинен бути в майбутньому
        assertFalse("Дата звіту не в майбутньому", reportDate.after(now));
    }
}