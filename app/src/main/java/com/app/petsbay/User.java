package com.app.petsbay;

public class User {
    String UserName,age,location,Url,uniqueId,pet_type,pet_description;


    public User(){}

    public String getPet_type() {
        return pet_type;
    }

    public String getPet_description() {
        return pet_description;
    }



    public User(String userName, String age, String location, String Url, String uniqueId, String pet_type, String pet_description) {
        this.UserName = userName;
        this.age = age;
        this.location = location;
        this.Url=Url;
        this.uniqueId=uniqueId;
        this.pet_description=pet_description;
        this.pet_type=pet_type;
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
                ", pet_type='" + pet_type + '\'' +
                ", pet_description='" + pet_description + '\'' +
                '}';
    }
}
