package model;

public class User {

    private String fUID;
    private String email;
    private String name;
    private String lName;
    private String sex;
    private int age;

    public User(String fUID, String email, String name, String lName, String sex, int age) {
        this.fUID = fUID;
        this.email = email;
        this.name = name;
        this.lName = lName;
        this.sex = sex;
        this.age = age;
    }

    public User() {
    } // pour DataSnapshot

    public String getFUID() {

        return fUID;
    }

    public String getEmail() {

        return email;
    }

    public String getName() {

        return name;
    }

    public String getLastName() {
        return lName;
    }

    public int getAge() {

        return age;
    }

    public String getSex() {
        return sex;
    }

    public void setFUID(String fUID) {
        this.fUID = fUID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lName) {
        this.lName = lName;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
