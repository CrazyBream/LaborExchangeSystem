package main.laborexchange;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Клас, що представляє менеджера біржі праці
 */
public class Manager {
    private String name;
    private String employeeId;
    private List<Unemployed> unemployedList;
    private List<Vacancy> vacancyList;

    public Manager(String name, String employeeId) {
        this.name = name;
        this.employeeId = employeeId;
        this.unemployedList = new ArrayList<>();
        this.vacancyList = new ArrayList<>();
    }

    // Геттери
    public String getName() { return name; }
    public String getEmployeeId() { return employeeId; }
    public List<Unemployed> getProcessedUnemployed() { return new ArrayList<>(unemployedList); }
    public List<Vacancy> getProcessedVacancies() { return new ArrayList<>(vacancyList); }

    /**
     * Реєструє безробітного в системі
     */
    public boolean registerUnemployed(Unemployed unemployed) {
        if (unemployed != null && unemployed.isValid() &&
                !unemployedList.contains(unemployed)) {
            return unemployedList.add(unemployed);
        }
        return false;
    }

    /**
     * Реєструє вакансію в системі
     */
    public boolean registerVacancy(Vacancy vacancy) {
        if (vacancy != null && vacancy.isValid() &&
                !vacancyList.contains(vacancy)) {
            return vacancyList.add(vacancy);
        }
        return false;
    }

    /**
     * Знаходить відповідні вакансії для безробітного
     */
    public List<Vacancy> findMatchingVacancies(Unemployed unemployed) {
        List<Vacancy> matches = new ArrayList<>();
        if (unemployed != null && unemployedList.contains(unemployed)) {
            for (Vacancy vacancy : vacancyList) {
                if (vacancy.isActive() && vacancy.matchesSpecialty(unemployed.getSpecialty())) {
                    matches.add(vacancy);
                }
            }
        }
        return matches;
    }

    /**
     * Знаходить відповідні вакансії для будь-якого безробітного (без перевірки реєстрації)
     */
    public List<Vacancy> findMatchingVacanciesForAny(String specialty) {
        return vacancyList.stream()
                .filter(Vacancy::isActive)
                .filter(v -> v.matchesSpecialty(specialty))
                .collect(Collectors.toList());
    }

    /**
     * Працевлаштовує безробітного на вакансію
     */
    public boolean employUnemployed(Unemployed unemployed, Vacancy vacancy) {
        // Перевіряємо базові умови
        if (unemployed == null || vacancy == null) {
            return false;
        }

        // Перевіряємо, чи зареєстровані учасники
        boolean isUnemployedRegistered = unemployedList.contains(unemployed);
        boolean isVacancyRegistered = vacancyList.contains(vacancy);

        // Якщо не зареєстровані, автоматично реєструємо
        if (!isUnemployedRegistered) {
            if (!unemployed.isValid()) {
                return false;
            }
            unemployedList.add(unemployed);
        }

        if (!isVacancyRegistered) {
            if (!vacancy.isValid()) {
                return false;
            }
            vacancyList.add(vacancy);
        }

        // Перевіряємо, чи активна вакансія і чи відповідає спеціальність
        if (!vacancy.isActive() || !vacancy.matchesSpecialty(unemployed.getSpecialty())) {
            return false;
        }

        // Виконуємо працевлаштування
        unemployed.setEmployed(true);
        vacancy.setActive(false);
        return true;
    }

    /**
     * Повертає загальну кількість оброблених записів
     */
    public int getProcessedCount() {
        return unemployedList.size() + vacancyList.size();
    }

    /**
     * Повертає кількість успішно працевлаштованих
     */
    public long getEmployedCount() {
        return unemployedList.stream()
                .filter(Unemployed::isEmployed)
                .count();
    }

    /**
     * Повертає активні вакансії
     */
    public List<Vacancy> getActiveVacancies() {
        return vacancyList.stream()
                .filter(Vacancy::isActive)
                .collect(Collectors.toList());
    }

    /**
     * Пошук безробітних за спеціальністю
     */
    public List<Unemployed> findUnemployedBySpecialty(String specialty) {
        return unemployedList.stream()
                .filter(u -> u.getSpecialty().equalsIgnoreCase(specialty))
                .collect(Collectors.toList());
    }

    /**
     * Скасування працевлаштування (для тестування)
     */
    public boolean unemploy(Unemployed unemployed) {
        if (unemployed != null && unemployedList.contains(unemployed)) {
            unemployed.setEmployed(false);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("Manager{name='%s', id='%s', unemployed=%d, vacancies=%d, employed=%d}",
                name, employeeId, unemployedList.size(), vacancyList.size(), getEmployedCount());
    }
}