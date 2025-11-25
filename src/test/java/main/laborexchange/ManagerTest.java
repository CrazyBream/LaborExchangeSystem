package main.laborexchange;

import org.junit.*;
import java.util.List;
import static org.junit.Assert.*;

public class ManagerTest {
    private Manager manager;
    private Unemployed unemployed;
    private Vacancy vacancy;
    private Unemployed invalidUnemployed;

    @BeforeClass
    public static void setUpBeforeClass() {
        System.out.println("=== Початок тестування класу Manager ===");
    }

    @AfterClass
    public static void tearDownAfterClass() {
        System.out.println("=== Завершення тестування класу Manager ===");
    }

    @Before
    public void setUp() {
        manager = new Manager("Менеджер Олена", "M001");
        unemployed = new Unemployed("Іваненко Петро", "Київ", "0501112233",
                "Java Developer", 30, "petro@test.com");
        vacancy = new Vacancy("V001", "Java Developer", "IT Company",
                "Java, Spring", 25000.0);
        invalidUnemployed = new Unemployed("", "", "", "", 15, "invalid");
    }

    @Test
    public void testManagerCreation() {
        assertNotNull("Менеджер має бути створений", manager);
        assertEquals("Ім'я менеджера", "Менеджер Олена", manager.getName());
        assertEquals("ID менеджера", "M001", manager.getEmployeeId());
        assertTrue("Список безробітних спочатку порожній", manager.getProcessedUnemployed().isEmpty());
        assertTrue("Список вакансій спочатку порожній", manager.getProcessedVacancies().isEmpty());
    }

    @Test
    public void testRegisterUnemployed() {
        assertTrue("Реєстрація валідного безробітного", manager.registerUnemployed(unemployed));
        assertFalse("Повторна реєстрація того ж безробітного", manager.registerUnemployed(unemployed));
        assertFalse("Реєстрація невалідного безробітного", manager.registerUnemployed(invalidUnemployed));
        assertFalse("Реєстрація null безробітного", manager.registerUnemployed(null));

        assertEquals("Кількість оброблених безробітних", 1, manager.getProcessedUnemployed().size());
        assertTrue("Список містить безробітного", manager.getProcessedUnemployed().contains(unemployed));
    }

    @Test
    public void testRegisterVacancy() {
        assertTrue("Реєстрація валідної вакансії", manager.registerVacancy(vacancy));
        assertFalse("Повторна реєстрація тієї ж вакансії", manager.registerVacancy(vacancy));
        assertFalse("Реєстрація null вакансії", manager.registerVacancy(null));

        assertEquals("Кількість оброблених вакансій", 1, manager.getProcessedVacancies().size());
        assertTrue("Список містить вакансію", manager.getProcessedVacancies().contains(vacancy));
    }

    @Test
    public void testFindMatchingVacancies() {
        // Без реєстрації - немає результатів
        List<Vacancy> emptyMatches = manager.findMatchingVacancies(unemployed);
        assertTrue("Без реєстрації - немає результатів", emptyMatches.isEmpty());

        // Реєструємо безробітного, але без вакансій - все одно немає результатів
        manager.registerUnemployed(unemployed);
        List<Vacancy> stillEmpty = manager.findMatchingVacancies(unemployed);
        assertTrue("Без вакансій - немає результатів", stillEmpty.isEmpty());

        // Реєструємо вакансію - тепер є результати
        manager.registerVacancy(vacancy);
        List<Vacancy> matches = manager.findMatchingVacancies(unemployed);
        assertFalse("Знайдені відповідні вакансії", matches.isEmpty());
        assertEquals("Знайдена одна відповідна вакансія", 1, matches.size());
        assertEquals("Позиція знайденої вакансії", "Java Developer", matches.get(0).getPosition());

        // Незареєстрований безробітний не знаходить вакансії
        Unemployed unregistered = new Unemployed("Інший", "Адреса", "Тел", "Java", 25, "other@test.com");
        List<Vacancy> unregisteredMatches = manager.findMatchingVacancies(unregistered);
        assertTrue("Незареєстрований безробітний не знаходить вакансії", unregisteredMatches.isEmpty());
    }


    @Test
    public void testFindMatchingVacanciesForAny() {
        // Цей метод працює для будь-якої спеціальності без реєстрації
        manager.registerVacancy(vacancy);

        List<Vacancy> javaMatches = manager.findMatchingVacanciesForAny("Java");
        assertFalse("Java спеціальність знаходить вакансії", javaMatches.isEmpty());

        List<Vacancy> pythonMatches = manager.findMatchingVacanciesForAny("Python");
        assertTrue("Python спеціальність не знаходить Java вакансії", pythonMatches.isEmpty());
    }

    @Test
    public void testEmployUnemployed() {
        // Тест 1: Працевлаштування з попередньою реєстрацією
        manager.registerUnemployed(unemployed);
        manager.registerVacancy(vacancy);

        assertTrue("Успішне працевлаштування", manager.employUnemployed(unemployed, vacancy));
        assertTrue("Безробітний позначений як працевлаштований", unemployed.isEmployed());
        assertFalse("Вакансія позначена як неактивна", vacancy.isActive());

        // Перевіряємо статистику
        assertEquals("Кількість працевлаштованих", 1, manager.getEmployedCount());
    }

    @Test
    public void testGetProcessedCount() {
        assertEquals("Початкова кількість оброблених", 0, manager.getProcessedCount());

        manager.registerUnemployed(unemployed);
        assertEquals("Після реєстрації безробітного", 1, manager.getProcessedCount());

        manager.registerVacancy(vacancy);
        assertEquals("Після реєстрації вакансії", 2, manager.getProcessedCount());
    }

    @Test
    public void testGetEmployedCount() {
        assertEquals("Початкова кількість працевлаштованих", 0, manager.getEmployedCount());

        // Працевлаштування
        manager.registerUnemployed(unemployed);
        manager.registerVacancy(vacancy);
        manager.employUnemployed(unemployed, vacancy);

        assertEquals("Після працевлаштування", 1, manager.getEmployedCount());
    }

    @Test
    public void testGetActiveVacancies() {
        manager.registerVacancy(vacancy);
        assertEquals("Одна активна вакансія", 1, manager.getActiveVacancies().size());

        vacancy.setActive(false);
        assertEquals("Після деактивації - немає активних вакансій", 0, manager.getActiveVacancies().size());

        // Додаємо ще одну активну вакансію
        Vacancy activeVacancy = new Vacancy("V002", "Python Dev", "Company", "Python", 30000.0);
        manager.registerVacancy(activeVacancy);
        assertEquals("Одна активна вакансія після додавання нової", 1, manager.getActiveVacancies().size());
    }

    @Test
    public void testFindUnemployedBySpecialty() {
        manager.registerUnemployed(unemployed);

        List<Unemployed> javaDevs = manager.findUnemployedBySpecialty("Java Developer");
        assertEquals("Знайдено одного Java розробника", 1, javaDevs.size());

        List<Unemployed> pythonDevs = manager.findUnemployedBySpecialty("Python Developer");
        assertTrue("Не знайдено Python розробників", pythonDevs.isEmpty());
    }

    @Test
    public void testUnemploy() {
        manager.registerUnemployed(unemployed);
        manager.registerVacancy(vacancy);
        manager.employUnemployed(unemployed, vacancy);

        assertEquals("Працевлаштований перед скасуванням", 1, manager.getEmployedCount());

        assertTrue("Скасування працевлаштування", manager.unemploy(unemployed));
        assertEquals("Після скасування працевлаштування", 0, manager.getEmployedCount());
        assertFalse("Безробітний більше не працевлаштований", unemployed.isEmployed());

        // Скасування для незареєстрованого
        Unemployed unregistered = new Unemployed("Незареєстрований", "Адреса", "Тел", "Спец", 25, "test@test.com");
        assertFalse("Скасування для незареєстрованого", manager.unemploy(unregistered));
    }

    @Test
    public void testToString() {
        String toString = manager.toString();
        assertNotNull("toString не повертає null", toString);
        assertTrue("toString містить ім'я", toString.contains("Менеджер Олена"));
        assertTrue("toString містить ID", toString.contains("M001"));
        assertTrue("toString містить статистику", toString.contains("unemployed=0"));
    }
}