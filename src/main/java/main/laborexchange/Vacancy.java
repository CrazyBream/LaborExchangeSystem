package main.laborexchange;

import java.util.Locale;
import java.util.Objects;

public class Vacancy {
    private String position;
    private String company;
    private String requirements;
    private double salary;
    private boolean isActive;
    private String id;

    public Vacancy(String id, String position, String company,
                   String requirements, double salary) {
        this.id = id;
        this.position = position;
        this.company = company;
        this.requirements = requirements;
        this.salary = salary;
        this.isActive = true;
    }

    // Геттери
    public String getId() { return id; }
    public String getPosition() { return position; }
    public String getCompany() { return company; }
    public String getRequirements() { return requirements; }
    public double getSalary() { return salary; }
    public boolean isActive() { return isActive; }

    // Сеттери
    public void setPosition(String position) { this.position = position; }
    public void setCompany(String company) { this.company = company; }
    public void setRequirements(String requirements) { this.requirements = requirements; }
    public void setSalary(double salary) { this.salary = salary; }
    public void setActive(boolean active) { this.isActive = active; }


    /**
     * Перевіряє чи відповідає вакансія спеціальності
     */
    public boolean matchesSpecialty(String specialty) {
        if (specialty == null || requirements == null) return false;

        String specLower = specialty.toLowerCase().trim();
        String reqLower = requirements.toLowerCase();

        // Список ключових слів для пошуку
        String[] keywords = specLower.split("[,\\s]+");

        for (String keyword : keywords) {
            if (keyword.length() < 3) continue; // Ігноруємо короткі слова

            // Шукаємо точне слово у вимогах (з границями слова)
            if (reqLower.matches(".*\\b" + keyword + "\\b.*")) {
                return true;
            }
        }
        return false;
    }


    public boolean isHighSalary() {
        return salary > 30000.0;
    }

    /**
     * Перевіряє чи є дані вакансії валідними
     */
    public boolean isValid() {
        return id != null && !id.trim().isEmpty() &&
                position != null && !position.trim().isEmpty() &&
                company != null && !company.trim().isEmpty() &&
                requirements != null && !requirements.trim().isEmpty() &&
                salary > 0;
    }

    /**
     * Повертає інформацію про вакансію для відображення
     */
    public String getDisplayInfo() {
        // Використовуємо Locale.US для гарантованого форматування з крапкою
        return String.format(Locale.US, "%s at %s - %.2f UAH", position, company, salary);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vacancy vacancy = (Vacancy) o;
        return Objects.equals(id, vacancy.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        // Використовуємо Locale.US для гарантованого форматування
        return String.format(Locale.US, "Vacancy{position='%s', company='%s', salary=%.2f, active=%s}",
                position, company, salary, isActive);
    }
}