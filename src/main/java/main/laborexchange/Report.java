package main.laborexchange;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Клас, що представляє звіт біржі праці
 */
public class Report {
    private String reportId;
    private Date reportDate;
    private List<Unemployed> employedList;
    private List<Vacancy> closedVacancies;
    private int totalProcessed;
    private double successRate;

    public Report(String reportId) {
        this.reportId = reportId;
        this.reportDate = new Date();
        this.employedList = new ArrayList<>();
        this.closedVacancies = new ArrayList<>();
        this.totalProcessed = 0;
        this.successRate = 0.0;
    }

    // Геттери
    public String getReportId() { return reportId; }
    public Date getReportDate() { return new Date(reportDate.getTime()); }
    public List<Unemployed> getEmployedList() { return new ArrayList<>(employedList); }
    public List<Vacancy> getClosedVacancies() { return new ArrayList<>(closedVacancies); }
    public int getTotalProcessed() { return totalProcessed; }
    public double getSuccessRate() { return successRate; }

    /**
     * Додає працевлаштованого до звіту
     */
    public void addEmployed(Unemployed unemployed) {
        if (unemployed != null && unemployed.isEmployed() &&
                !employedList.contains(unemployed)) {
            employedList.add(unemployed);
            calculateStatistics();
        }
    }

    /**
     * Додає закриту вакансію до звіту
     */
    public void addClosedVacancy(Vacancy vacancy) {
        if (vacancy != null && !vacancy.isActive() &&
                !closedVacancies.contains(vacancy)) {
            closedVacancies.add(vacancy);
            calculateStatistics();
        }
    }

    /**
     * Розраховує статистику звіту
     */
    private void calculateStatistics() {
        totalProcessed = employedList.size() + closedVacancies.size();
        if (totalProcessed > 0) {
            successRate = (double) employedList.size() / totalProcessed * 100;
        }
    }

    /**
     * Перевіряє чи є звіт валідним
     */
    public boolean isValid() {
        if (reportId == null || reportId.trim().isEmpty()) {
            return false;
        }

        if (reportDate == null) {
            return false;
        }

        // Дозволяємо дату в майбутньому на невеликий проміжок часу (на випадок розбіжностей в системному часі)
        Date now = new Date();
        long tolerance = 5000; // 5 секунд допуск
        return reportDate.getTime() <= (now.getTime() + tolerance);
    }

    /**
     * Генерує зведення звіту
     */
    public String generateSummary() {
        return String.format("Report %s: %d employed, %d closed vacancies, Success rate: %.1f%%",
                reportId, employedList.size(), closedVacancies.size(), successRate);
    }

    /**
     * Генерує детальний звіт
     */
    public String generateDetailedReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Labor Exchange Report ===\n");
        sb.append("Report ID: ").append(reportId).append("\n");
        sb.append("Date: ").append(reportDate).append("\n");
        sb.append("Employed: ").append(employedList.size()).append("\n");
        sb.append("Closed Vacancies: ").append(closedVacancies.size()).append("\n");
        sb.append("Success Rate: ").append(String.format("%.1f%%", successRate)).append("\n");

        if (!employedList.isEmpty()) {
            sb.append("\nEmployed Persons:\n");
            for (Unemployed employed : employedList) {
                sb.append(" - ").append(employed.getFullName()).append(" (").append(employed.getSpecialty()).append(")\n");
            }
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("Report{id='%s', date=%s, employed=%d, success=%.1f%%}",
                reportId, reportDate, employedList.size(), successRate);
    }
}