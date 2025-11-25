package main.laborexchange;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class LaborExchangeSystem {
    private List<Unemployed> unemployedList;
    private List<Vacancy> vacancyList;
    private List<Manager> managers;
    private List<Report> reports;

    public LaborExchangeSystem() {
        this.unemployedList = new ArrayList<>();
        this.vacancyList = new ArrayList<>();
        this.managers = new ArrayList<>();
        this.reports = new ArrayList<>();
    }



    public boolean addUnemployed(Unemployed unemployed) {
        if (unemployed != null && unemployed.isValid() &&
                !unemployedList.contains(unemployed)) {
            return unemployedList.add(unemployed);
        }
        return false;
    }


    public boolean addVacancy(Vacancy vacancy) {
        if (vacancy != null && vacancy.isValid() &&
                !vacancyList.contains(vacancy)) {
            return vacancyList.add(vacancy);
        }
        return false;
    }


    public boolean addManager(Manager manager) {
        if (manager != null && !managers.contains(manager)) {
            return managers.add(manager);
        }
        return false;
    }


    public boolean addReport(Report report) {
        if (report != null && report.isValid() &&
                !reports.contains(report)) {
            return reports.add(report);
        }
        return false;
    }


    public List<Vacancy> findVacanciesForUnemployed(Unemployed unemployed) {
        List<Vacancy> matches = new ArrayList<>();
        if (unemployed != null) {
            for (Vacancy vacancy : vacancyList) {
                if (vacancy.isActive() && vacancy.matchesSpecialty(unemployed.getSpecialty())) {
                    matches.add(vacancy);
                }
            }
        }
        return matches;
    }


    public List<Unemployed> findUnemployedForVacancy(Vacancy vacancy) {
        List<Unemployed> matches = new ArrayList<>();
        if (vacancy != null && vacancy.isActive()) {
            for (Unemployed unemployed : unemployedList) {
                if (!unemployed.isEmployed() &&
                        vacancy.matchesSpecialty(unemployed.getSpecialty())) {
                    matches.add(unemployed);
                }
            }
        }
        return matches;
    }


    public List<Unemployed> findUnemployedForSpecialty(String specialty) {
        return unemployedList.stream()
                .filter(u -> !u.isEmployed() && u.getSpecialty().toLowerCase().contains(specialty.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Статистичні методи

    public int getTotalUnemployed() {
        return unemployedList.size();
    }

    public int getTotalVacancies() {
        return vacancyList.size();
    }

    public int getActiveVacancies() {
        return (int) vacancyList.stream().filter(Vacancy::isActive).count();
    }

    public int getEmployedCount() {
        return (int) unemployedList.stream().filter(Unemployed::isEmployed).count();
    }

    public int getManagerCount() {
        return managers.size();
    }

    public int getReportCount() {
        return reports.size();
    }


    public String getSystemStatistics() {
        return String.format(
                "System Statistics:\n" +
                        "- Unemployed: %d (%d employed)\n" +
                        "- Vacancies: %d (%d active)\n" +
                        "- Managers: %d\n" +
                        "- Reports: %d",
                getTotalUnemployed(), getEmployedCount(),
                getTotalVacancies(), getActiveVacancies(),
                getManagerCount(), getReportCount()
        );
    }


    public Report generateMonthlyReport(String reportId) {
        Report report = new Report(reportId);

        // Додаємо працевлаштованих
        unemployedList.stream()
                .filter(Unemployed::isEmployed)
                .forEach(report::addEmployed);

        // Додаємо закриті вакансії
        vacancyList.stream()
                .filter(v -> !v.isActive())
                .forEach(report::addClosedVacancy);

        addReport(report);
        return report;
    }


    public boolean employUnemployed(Unemployed unemployed, Vacancy vacancy) {
        if (unemployed != null && vacancy != null &&
                unemployedList.contains(unemployed) &&
                vacancyList.contains(vacancy) &&
                vacancy.isActive() &&
                !unemployed.isEmployed() &&
                vacancy.matchesSpecialty(unemployed.getSpecialty())) {

            unemployed.setEmployed(true);
            vacancy.setActive(false);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format(
                "LaborExchangeSystem{unemployed=%d, vacancies=%d, managers=%d, reports=%d}",
                getTotalUnemployed(), getTotalVacancies(), getManagerCount(), getReportCount()
        );
    }
}