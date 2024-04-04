package com.signupfacebook.Newlife_project_1.repository.repository2;

import com.signupfacebook.Newlife_project_1.model.entity2.PhoneNumberEntity2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("companyEntityManagerFactory")
public interface IPhoneNumberRepository2 extends JpaRepository<PhoneNumberEntity2, String> {
    @Query(value = "SELECT id, phone_number, action_type, app_type, sms_ack, timestamp, app_name " +
                    "FROM sim_account_info " +
                    "WHERE is_active=1 and phone_number not in(:existsPhoneNumber) ORDER BY (RAND()) limit 50", nativeQuery = true)
    List<PhoneNumberEntity2> findAllByActive(@Param("existsPhoneNumber") String existsPhoneNumber);

    Optional<PhoneNumberEntity2> findByPhoneNumber(String phoneNumber);
}
