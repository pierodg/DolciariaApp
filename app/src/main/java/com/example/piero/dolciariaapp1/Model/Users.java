package com.example.piero.dolciariaapp1.Model;

public class Users {
    private String email;
    private String name;
    private String password;
    private String phone;
    private String address;
    private String username;
    private String city;

    public Users() {

    }



    public Users(String email, String name, String password, String phone, String address, String username, String city) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.username = username;
        this.city = city;

    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
