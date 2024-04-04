package com.signupfacebook.Newlife_project_1.model.entity1;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "config")
public class ConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "profile_path")
    private String profilePath;

    @Column(name = "create_date")
    private String create_date;

    @Column(name = "status")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "id_list_sim")
    private ListSimEntity listSimEntity;

    @OneToMany(mappedBy = "configEntity")
    private List<PhoneNumberEntity> phoneNumberEntity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public ListSimEntity getListSimEntity() {
        return listSimEntity;
    }

    public void setListSimEntity(ListSimEntity listSimEntity) {
        this.listSimEntity = listSimEntity;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public List<PhoneNumberEntity> getPhoneNumberEntity() {
        return phoneNumberEntity;
    }

    public void setPhoneNumberEntity(List<PhoneNumberEntity> phoneNumberEntity) {
        this.phoneNumberEntity = phoneNumberEntity;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
