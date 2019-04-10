package com.example.ukeje.hackacthon4justice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChildUser {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("child_id")
    @Expose
    private String childId;
    @SerializedName("DOB")
    @Expose
    private String dob;
    @SerializedName("Parent")
    @Expose
    private String parent;
    @SerializedName("contact")
    @Expose
    private String contact;
    @SerializedName("contact1")
    @Expose
    private String contact1;
    @SerializedName("contact2")
    @Expose
    private String contact2;

    public String getContact2() {
        return contact2;
    }

    public void setContact2(String contact2) {
        this.contact2 = contact2;
    }



    public String getContact1() {
        return contact1;
    }

    public void setContact1(String contact1) {
        this.contact1 = contact1;
    }



    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }
}
