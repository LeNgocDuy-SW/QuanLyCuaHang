package ProjectJava;

import java.time.LocalDate;
import java.util.Objects;

public class nhanvien {
    private int ID_employee;
    private String name_employee;
    private LocalDate date_of_birth;
    private String gender;
    private String phonenumber;
    private String address;
    private String email;
    private String part;
    private LocalDate start_date;
    private String role;
    private String username;
    private String password;
    private String avatar;
    

    public nhanvien(Main main) {
    }

    public nhanvien(int ID_employee, String name_employee, LocalDate date_of_birth, String gender, String phonenumber, String address, String email, String part, LocalDate start_date, String role, String username, String password, String avatar) {
        this.ID_employee = ID_employee;
        this.name_employee = name_employee;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.phonenumber = phonenumber;
        this.address = address;
        this.email = email;
        this.part = part;
        this.start_date = start_date;
        this.role = role;
        this.username= username;
        this.password = password;
        this.avatar = avatar;
    }

    public nhanvien(String name_employee, LocalDate date_of_birth, String gender, String phonenumber, String address, String email, String part, LocalDate start_date, String role, String username, String password, String avatar) {
        this.name_employee = name_employee;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.phonenumber = phonenumber;
        this.address = address;
        this.email = email;
        this.part = part;
        this.start_date = start_date;
        this.role = role;
        this.username= username;
        this.password = password;
        this.avatar = avatar;
    }

    public int getID_employee() {
        return ID_employee;
    }

    public void setID_employee(int ID_employee) {
        this.ID_employee = ID_employee;
    }

    public String getName_employee() {
        return name_employee;
    }

    public void setName_employee(String name_employee) {
        this.name_employee = name_employee;
    }

    public LocalDate getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(LocalDate date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }
    
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
   
}
