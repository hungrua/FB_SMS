package com.signupfacebook.Newlife_project_1.model.entity1;


import javax.persistence.*;

@Entity
@Table(name = "api_key")
public class ApiKeyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "api_key")
    private String apiKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
