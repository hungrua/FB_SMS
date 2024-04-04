package com.signupfacebook.Newlife_project_1.repository.repository1;

import com.signupfacebook.Newlife_project_1.model.entity1.PhoneNumberEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
@Qualifier("employeeEntityManagerFactory")
public interface IPhoneNumberRepository extends JpaRepository<PhoneNumberEntity, String> {
    List<PhoneNumberEntity> findAllByListSim_Id(String listSimId);
    List<PhoneNumberEntity> findAllByStatus(Integer status);
    Optional<PhoneNumberEntity> findByIdAndStatus(String id, Integer status);
}
