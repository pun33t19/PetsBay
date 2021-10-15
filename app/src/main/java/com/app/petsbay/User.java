package com.app.petsbay;

public class User {
    String UserName,age,location,Url,uniqueId;

    public User(){}

    public User(String userName, String age, String location,String Url,String uniqueId) {
        UserName = userName;
        this.age = age;
        this.location = location;
        this.Url=Url;
        this.uniqueId=uniqueId;
    }

    public String getUserName() {
        return UserName;
    }

    public String getAge() {
        return age;
    }

    public String getLocation() {
        return location;
    }

    public String getUrl(){ return Url;}

    public String getUniqueId() {
        return uniqueId;
    }

    @Override
    public String toString() {
        return "User{" +
                "UserName='" + UserName + '\'' +
                ", age='" + age + '\'' +
                ", location='" + location + '\'' +
                ", Url='" + Url + '\'' +
                ", uniqueId='" + uniqueId + '\'' +
                '}';
    }
}
