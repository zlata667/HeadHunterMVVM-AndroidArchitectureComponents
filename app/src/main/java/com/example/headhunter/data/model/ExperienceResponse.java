package com.example.headhunter.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExperienceResponse{

    @SerializedName("experience")
    private List<Experience> experience;

    public List<Experience> getExperience(){
        return experience;
    }

    public void setExperience(List<Experience> experience){
        this.experience = experience;
    }

    public static class Experience{

        @SerializedName("id")
        String id;
        @SerializedName("name")
        String name;

        public String getId(){
            return id;
        }

        public void setId(String id){
            this.id = id;
        }

        public String getName(){
            return name;
        }

        public void setName(String name){
            this.name = name;
        }
    }
}
