package com.example.splitit.RecyclerView;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="user")
public class User {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="user_id")
    private long id;
    @ColumnInfo(name="user_name")
    private String name;
    @ColumnInfo(name="user_email")
    private String email;
    @ColumnInfo(name="user_img")
    private String img;
    @ColumnInfo(name="user_code")
    private final String code;

    public User(String name, String email, String code, String img){
        this.name=name;
        this.email= email;
        this.img=img;
        this.code=code;
    }


    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCode() {
        return code;
    }




}
