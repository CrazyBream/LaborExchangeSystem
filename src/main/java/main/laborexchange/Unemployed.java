package main.laborexchange;

import java.util.Objects;

/**
 * Клас, що представляє безробітного в системі біржі праці
 */
public class Unemployed {
    private String fullName;
    private String address;
    private String phone;
    private String specialty;
    private int age;
    private boolean isEmployed;
    private String email;

    public Unemployed(String fullName, String address, String phone,
                      String specialty, int age, String email) {
        this.fullName = fullName;
        this.address = address;
        this.phone = phone;
        this.specialty = specialty;
        this.age = age;
        this.email = email;
        this.isEmployed = false;
    }

    // Геттери
    public String getFullName() { return fullName; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getSpecialty() { return specialty; }
    public int getAge() { return age; }
    public String getEmail() { return email; }
    public boolean isEmployed() { return isEmployed; }

    // Сеттери
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public void setAge(int age) { this.age = age; }
    public void setEmail(String email) { this.email = email; }
    public void setEmployed(boolean employed) { this.isEmployed = employed; }

    /**
     * Перевіряє чи є дані безробітного валідними
     */
    public boolean isValid() {
        return fullName != null && !fullName.trim().isEmpty() &&
                address != null && !address.trim().isEmpty() &&
                phone != null && !phone.trim().isEmpty() &&
                specialty != null && !specialty.trim().isEmpty() &&
                email != null && email.contains("@") &&
                age >= 16 && age <= 70;
    }

    /**
     * Перевіряє чи може безробітний вийти на пенсію
     */
    public boolean canRetire() {
        return age >= 60;
    }

    /**
     * Повертає ініціали безробітного
     */
    public String getInitials() {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "";
        }
        String[] names = fullName.split(" ");
        if (names.length >= 3) {
            return names[0].charAt(0) + "." + names[1].charAt(0) + "." + names[2];
        }
        return fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unemployed that = (Unemployed) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return String.format("Unemployed{name='%s', specialty='%s', age=%d, employed=%s}",
                fullName, specialty, age, isEmployed);
    }
}