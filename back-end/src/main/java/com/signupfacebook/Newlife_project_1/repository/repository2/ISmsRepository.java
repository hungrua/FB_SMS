package com.signupfacebook.Newlife_project_1.repository.repository2;

import com.signupfacebook.Newlife_project_1.model.entity2.PhoneNumberEntity2;
import com.signupfacebook.Newlife_project_1.model.entity2.SmsEntity2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.*;

@Repository
@Qualifier("companyEntityManagerFactory")
public interface ISmsRepository extends JpaRepository<SmsEntity2, String> {
    @Query(value = "select * from result_sms_a2p_v2 " +
                    "where result_sms_a2p_v2.sender_id like CONCAT(:sender, '%') " + // số điện thoại đăng kí
                    "and result_sms_a2p_v2.received_id like '%FACEBOOK%' " + // thực hiện bằng facebook
                    "and result_sms_a2p_v2.received_time between :beforeSend and :afterSend "  // tính toán thời gian nhận sms (tìm kiếm gần đúng theo thời gian k vượt quá 2 phút)
                    , nativeQuery = true)
    List<SmsEntity2> findBySenderIdAndReceivedTime(@Param("sender") String sender,
                                                   @Param("beforeSend") String beforeSend,
                                                   @Param("afterSend") String afterSend);

    @Query(value = "select * from result_sms_a2p_v2 limit 5", nativeQuery = true)
    List<SmsEntity2> findAllTest();

}
