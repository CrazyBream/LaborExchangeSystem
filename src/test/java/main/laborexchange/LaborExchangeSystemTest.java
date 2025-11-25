package main.laborexchange;

import org.junit.*;
import java.util.List;
import static org.junit.Assert.*;

public class LaborExchangeSystemTest {
    private LaborExchangeSystem system;
    private Unemployed unemployed;
    private Vacancy vacancy;
    private Manager manager;
    private Report report;

    @BeforeClass
    public static void setUpBeforeClass() {
        System.out.println("=== Початок тестування системи біржі праці ===");
    }

    @AfterClass
    public static void tearDownAfterClass() {
        System.out.println("=== Завершення тестування системи біржі праці ===");
    }

    @Before
    public void setUp() {
        system = new LaborExchangeSystem();
        unemployed = new Unemployed("Тестовий Користувач", "Київ", "0501112233",
                "Java Developer", 25, "test@test.com");
        vacancy = new Vacancy("V001", "Java Developer", "IT Company",
                "Java, Spring", 25000.0);
        manager = new Manager("Тестовий Менеджер", "TM001");
        report = new Report("R001");
    }

    @Test
    public void testSystemCreation() {
        assertNotNull("Система має бути створена", system);
        assertEquals("Початкова кількість безробітних", 0, system.getTotalUnemployed());
        assertEquals("Початкова кількість вакансій", 0, system.getTotalVacancies());
        assertEquals("Початкова кількість менеджерів", 0, system.getManagerCount());
        assertEquals("Початкова кількість звітів", 0, system.getReportCount());
    }

    @Test
    public void testAddUnemployed() {
        assertTrue("Додавання валідного безробітного", system.addUnemployed(unemployed));
        assertFalse("Додавання дубліката безробітного", system.addUnemployed(unemployed));
        assertFalse("Додавання null безробітного", system.addUnemployed(null));

        assertEquals("Кількість безробітних після додавання", 1, system.getTotalUnemployed());
        assertEquals("Кількість працевлаштованих", 0, system.getEmployedCount());
    }

    @Test
    public void testAddVacancy() {
        assertTrue("Додавання валідної вакансії", system.addVacancy(vacancy));
        assertFalse("Додавання дубліката вакансії", system.addVacancy(vacancy));
        assertFalse("Додавання null вакансії", system.addVacancy(null));

        assertEquals("Кількість вакансій після додавання", 1, system.getTotalVacancies());
        assertEquals("Кількість активних вакансій", 1, system.getActiveVacancies());
    }

    @Test
    public void testAddManager() {
        assertTrue("Додавання менеджера", system.addManager(manager));
        assertFalse("Додавання дубліката менеджера", system.addManager(manager));
        assertFalse("Додавання null менеджера", system.addManager(null));

        assertEquals("Кількість менеджерів після додавання", 1, system.getManagerCount());
    }

    @Test
    public void testAddReport() {
        assertTrue("Додавання звіту", system.addReport(report));
        assertFalse("Додавання дубліката звіту", system.addReport(report));
        assertFalse("Додавання null звіту", system.addReport(null));

        assertEquals("Кількість звітів після додавання", 1, system.getReportCount());
    }

    @Test
    public void testFindVacanciesForUnemployed() {
        // Без додавання - немає результатів
        List<Vacancy> emptyMatches = system.findVacanciesForUnemployed(unemployed);
        assertTrue("Без додавання - немає результатів", emptyMatches.isEmpty());

        // Після додавання - є результати
        system.addUnemployed(unemployed);
        system.addVacancy(vacancy);

        List<Vacancy> matches = system.findVacanciesForUnemployed(unemployed);
        assertFalse("Знайдені відповідні вакансії", matches.isEmpty());
        assertEquals("Знайдена одна відповідна вакансія", 1, matches.size());
        assertEquals("Позиція знайденої вакансії", "Java Developer", matches.get(0).getPosition());
    }

    @Test
    public void testFindUnemployedForVacancy() {
        // Без додавання - немає результатів
        List<Unemployed> emptyMatches = system.findUnemployedForVacancy(vacancy);
        assertTrue("Без додавання - немає результатів", emptyMatches.isEmpty());

        // Додаємо безробітного
        system.addUnemployed(unemployed);
        system.addVacancy(vacancy);

        List<Unemployed> matches = system.findUnemployedForVacancy(vacancy);
        assertFalse("Знайдені відповідні безробітні", matches.isEmpty());
        assertEquals("Знайдено одного відповідного безробітного", 1, matches.size());
        assertEquals("Ім'я знайденого безробітного", "Тестовий Користувач", matches.get(0).getFullName());

        // Працевлаштований безробітний не знаходиться
        unemployed.setEmployed(true);
        List<Unemployed> employedMatches = system.findUnemployedForVacancy(vacancy);
        assertTrue("Працевлаштовані не знаходяться", employedMatches.isEmpty());

        // Неактивна вакансія не знаходить безробітних
        unemployed.setEmployed(false);
        vacancy.setActive(false);
        List<Unemployed> inactiveMatches = system.findUnemployedForVacancy(vacancy);
        assertTrue("Неактивна вакансія не знаходить безробітних", inactiveMatches.isEmpty());
    }

    @Test
    public void testStatistics() {
        assertEquals("Початкова статистика безробітних", 0, system.getTotalUnemployed());
        assertEquals("Початкова статистика вакансій", 0, system.getTotalVacancies());
        assertEquals("Початкова статистика активних вакансій", 0, system.getActiveVacancies());
        assertEquals("Початкова статистика працевлаштованих", 0, system.getEmployedCount());
        assertEquals("Початкова статистика менеджерів", 0, system.getManagerCount());
        assertEquals("Початкова статистика звітів", 0, system.getReportCount());

        system.addUnemployed(unemployed);
        system.addVacancy(vacancy);
        system.addManager(manager);
        system.addReport(report);

        assertEquals("Статистика після додавання безробітних", 1, system.getTotalUnemployed());
        assertEquals("Статистика після додавання вакансій", 1, system.getTotalVacancies());
        assertEquals("Статистика активних вакансій", 1, system.getActiveVacancies());
        assertEquals("Статистика менеджерів", 1, system.getManagerCount());
        assertEquals("Статистика звітів", 1, system.getReportCount());
    }

    @Test
    public void testGetSystemStatistics() {
        String statistics = system.getSystemStatistics();
        assertNotNull("Статистика не null", statistics);
        assertTrue("Містить інформацію про безробітних", statistics.contains("Unemployed"));
        assertTrue("Містить інформацію про вакансії", statistics.contains("Vacancies"));
        assertTrue("Містить інформацію про менеджерів", statistics.contains("Managers"));
        assertTrue("Містить інформацію про звіти", statistics.contains("Reports"));
    }

    @Test
    public void testGenerateMonthlyReport() {
        system.addUnemployed(unemployed);
        unemployed.setEmployed(true);
        system.addVacancy(vacancy);
        vacancy.setActive(false);

        Report monthlyReport = system.generateMonthlyReport("MONTHLY_001");
        assertNotNull("Місячний звіт створений", monthlyReport);
        assertTrue("Звіт доданий до системи", system.getReportCount() > 0);
        assertEquals("Кількість працевлаштованих у звіті", 1, monthlyReport.getEmployedList().size());
        assertEquals("Кількість закритих вакансій у звіті", 1, monthlyReport.getClosedVacancies().size());
    }

    @Test
    public void testIntegrationScenario() {
        // Комплексний сценарій роботи системи
        Unemployed unemployed1 = new Unemployed("Java Розробник", "Адреса1", "Тел1", "Java", 30, "java@test.com");
        Unemployed unemployed2 = new Unemployed("Python Розробник", "Адреса2", "Тел2", "Python", 25, "python@test.com");
        Unemployed unemployed3 = new Unemployed("Fullstack Розробник", "Адреса3", "Тел3", "JavaScript", 28, "fullstack@test.com");

        // Використовуємо різні технології для кожної вакансії
        Vacancy vacancy1 = new Vacancy("V001", "Java Backend Developer", "Comp1", "Java Spring Hibernate", 30000.0);
        Vacancy vacancy2 = new Vacancy("V002", "Python Data Scientist", "Comp2", "Python Pandas Django", 28000.0);
        Vacancy vacancy3 = new Vacancy("V003", "Frontend Developer", "Comp3", "JavaScript React HTML", 35000.0);

        Manager manager1 = new Manager("Менеджер Іван", "M001");
        Manager manager2 = new Manager("Менеджер Марія", "M002");

        // Додаємо дані до системи
        system.addUnemployed(unemployed1);
        system.addUnemployed(unemployed2);
        system.addUnemployed(unemployed3);
        system.addVacancy(vacancy1);
        system.addVacancy(vacancy2);
        system.addVacancy(vacancy3);
        system.addManager(manager1);
        system.addManager(manager2);

        // Перевірка пошуку вакансій
        List<Vacancy> javaVacancies = system.findVacanciesForUnemployed(unemployed1);
        assertEquals("Java розробник має знайти 1 вакансію", 1, javaVacancies.size());

        List<Vacancy> pythonVacancies = system.findVacanciesForUnemployed(unemployed2);
        assertEquals("Python розробник має знайти 1 вакансію", 1, pythonVacancies.size());

        List<Vacancy> jsVacancies = system.findVacanciesForUnemployed(unemployed3);
        assertEquals("JavaScript розробник має знайти 1 вакансію", 1, jsVacancies.size());

        // Перевірка пошуку безробітних
        List<Unemployed> javaUnemployed = system.findUnemployedForVacancy(vacancy1);
        assertEquals("Java вакансія знаходить 1 безробітного", 1, javaUnemployed.size());

        List<Unemployed> pythonUnemployed = system.findUnemployedForVacancy(vacancy2);
        assertEquals("Python вакансія знаходить 1 безробітного", 1, pythonUnemployed.size());

        List<Unemployed> jsUnemployed = system.findUnemployedForVacancy(vacancy3);
        assertEquals("JavaScript вакансія знаходить 1 безробітного", 1, jsUnemployed.size());

        // Перевірка статистики
        assertEquals("Загальна кількість безробітних", 3, system.getTotalUnemployed());
        assertEquals("Загальна кількість вакансій", 3, system.getTotalVacancies());
        assertEquals("Активні вакансії", 3, system.getActiveVacancies());
        assertEquals("Кількість менеджерів", 2, system.getManagerCount());

        // Працевлаштування
        system.employUnemployed(unemployed1, vacancy1);
        assertEquals("Кількість працевлаштованих після працевлаштування", 1, system.getEmployedCount());
        assertEquals("Активні вакансії після працевлаштування", 2, system.getActiveVacancies());

        // Генеруємо звіт
        Report integrationReport = system.generateMonthlyReport("INTEGRATION_TEST");
        assertNotNull("Інтеграційний звіт створений", integrationReport);
        assertEquals("Кількість звітів у системі", 1, system.getReportCount());
        assertEquals("Працевлаштовані у звіті", 1, integrationReport.getEmployedList().size());
    }

    @Test(timeout = 2000)
    public void testPerformance() {
        // Тест продуктивності - має виконуватися менше ніж за 2 секунди
        for (int i = 0; i < 1000; i++) {
            Unemployed u = new Unemployed("Name" + i, "Address" + i, "Phone" + i,
                    "Specialty" + i, 20 + (i % 50), "email" + i + "@test.com");
            system.addUnemployed(u);

            Vacancy v = new Vacancy("V" + i, "Position" + i, "Company" + i,
                    "Requirements" + i, 10000 + (i * 100));
            system.addVacancy(v);
        }
        assertEquals("Додано 1000 безробітних", 1000, system.getTotalUnemployed());
        assertEquals("Додано 1000 вакансій", 1000, system.getTotalVacancies());

        // Перевірка пошуку
        Unemployed testUnemployed = new Unemployed("Name500", "Address500", "Phone500",
                "Specialty500", 70, "email500@test.com");
        List<Vacancy> matches = system.findVacanciesForUnemployed(testUnemployed);
        assertNotNull("Результати пошуку не null", matches);
    }

    @Test
    public void testToString() {
        String toString = system.toString();
        assertNotNull("toString не повертає null", toString);
        assertTrue("toString містить інформацію про систему", toString.contains("LaborExchangeSystem"));
        assertTrue("toString містить кількість безробітних", toString.contains("unemployed=0"));
        assertTrue("toString містить кількість вакансій", toString.contains("vacancies=0"));
    }
}